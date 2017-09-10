package com.menuControllers;

import java.awt.Color;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.actionListeners.FetalDocumentListener;
import com.ftl.derived.FetalParser;
import com.ftl.derived.FetalParser.TransactionContext;
import com.ftl.events.StepEvent;
import com.ftl.events.StepEventListener;
import com.ftl.helper.Variable;
import com.transaction.TransactionService;
import com.views.StepWindowView;
import com.xml.ScriptSetupFile;

public class ExecuteMenuController {
	private Semaphore semaphore = null;
	private TransactionService trans = null;
	private JButton xmNext = null;
	private StepWindowView swv=null;
	private RSyntaxTextArea editor = null;
	@SuppressWarnings("unused")
	private FetalDocumentListener fetalListener;
	private String openFile;
	private static PrintStream stdOut;
	private static PrintStream stdErr;

	public void runApp(String editText, String openFile) throws Exception {
		this.openFile = openFile;
		swv = null;
		semaphore = null;
		
		RunApplication ra = new RunApplication();
		ra.run(editText, null);
	}
	
	public void stepApp(String editText, StepWindowView stepWindow, FetalDocumentListener fdl, RSyntaxTextArea mainEditor, String openFile) throws IOException {
		editor = mainEditor;
		//editor.getVertical().setValue(0);
		fetalListener = fdl;
		this.openFile = openFile;
		swv = stepWindow;
		errConsole(swv.getErrorWindow());

		RunApplication ra = new RunApplication();
		semaphore = new Semaphore(0);

		console(swv.getOutDisplay());
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ra.run(editText, semaphore);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		thread.start();
	}
	
	public void Next(RSyntaxTextArea mainEditor) {
		if (semaphore != null && trans !=null) {
			semaphore.release();
		}
	}

	class RunApplication {

		public void run(String editText, Semaphore semaphore) throws Exception {
			trans = new TransactionService();
			ScriptSetupFile setup = new ScriptSetupFile();
			setup.readFile(openFile, trans);
			trans.initTransaction("file:///home/donzalma/public_html/config/fetal.properties", semaphore);

			if (semaphore != null) {
				StepListener stepListner = new StepListener();
				trans.getStep().addEventListener(stepListner);
			}
			trans.setDebugMode(true);
			try {
				trans.loadRule(editText);
				FetalParser parser = trans.getfParser();
				TransactionContext tCtx = trans.getTransCtx();
				if (swv == null || swv.getHasTreeView().isSelected() == true) {
					trans.showGuiTree(parser, tCtx);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				semaphore = null;
				trans = null;

				if (xmNext != null) {
					xmNext.setVisible(false);
					xmNext = null;
				}

				if (stdOut != null ) {
					System.setOut(stdOut);
					stdOut = null;
				}
				if (stdErr != null) {
					System.setErr(stdErr);
					stdErr = null;
				}

				if (swv != null) {
					editor.removeAllLineHighlights();
					swv.getClose().setVisible(true);
				}
			}
		}

	}
	public static void errConsole(final JTextArea area) throws IOException {
		area.setForeground(Color.RED);
		final PipedInputStream errPipe = new PipedInputStream();
		PrintStream printStream = new PrintStream(new PipedOutputStream(errPipe), true);
		
		stdErr = System.err;
		System.setErr(printStream);
		new SwingWorker<Void, String>() {
			@SuppressWarnings("resource")
			@Override
			protected Void doInBackground() throws Exception {
				String line = "";
				Scanner s = new Scanner(errPipe);
                while (s.hasNextLine()){
                    line += s.nextLine() + '\n';
                    publish(line);
                }
				return null;
			}
            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks){
                    area.setText(line);
                }
            }		
		}.execute();
		
	}
    public static void console(final JTextArea area) throws IOException {
        //area.setContentType("text/html");
    	area.setForeground(Color.BLACK);
        final PipedInputStream outPipe = new PipedInputStream();
        PrintStream printStream = new PrintStream(new PipedOutputStream(outPipe), true);
        
        stdOut = System.out;
        System.setOut(printStream);
        new SwingWorker<Void, String>() {
            @SuppressWarnings("resource")
			@Override
            protected Void doInBackground() throws Exception {
            	String line = "";
                Scanner s = new Scanner(outPipe);
                while (s.hasNextLine()){
                    line = s.nextLine() + '\n';
                    publish(line);
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks){
                    area.setText(line);
                }
            }
        }.execute();
    }
    protected class StepListener implements StepEventListener{

		@Override
		public void StepReceived(StepEvent se) {

			List<Variable> varList = trans.getVarList();
			String displayVarList = "";
			for(Variable var : varList) {
				displayVarList += String.format("%s %s = %s\n", var.getType(), var.getName(), var.getValue());
			}
			swv.getVarDisplay().setText(displayVarList);
			try {
				editor.removeAllLineHighlights();
				editor.addLineHighlight((trans.getLineNum() - 1), new Color(200,200,255));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//editor.getPainter().HighlightLine(editor.getGraphics(), trans.getLineNum(), trans.getPrevLine());
		}
    	
    }
}
