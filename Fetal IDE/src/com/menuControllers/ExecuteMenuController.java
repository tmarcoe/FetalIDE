package com.menuControllers;

import java.awt.Color;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
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
import com.utils.PropertiesFile;
import com.views.MainWindowView;
import com.views.StepWindowView;
import com.xml.ScriptSetupFile;

public class ExecuteMenuController {
	private Semaphore semaphore = null;
	private TransactionService trans = null;
	@SuppressWarnings("unused")
	private JButton xmNext = null;
	private StepWindowView swv=null;
	private RSyntaxTextArea editor = null;
	@SuppressWarnings("unused")
	private FetalDocumentListener fetalListener;
	private String openFile;
	private static PrintStream stdOut;
	private static PrintStream stdErr;
	private final String propFile = "resources/config/ide.properties";
	private MainWindowView mainWindow;

	public void runApp(String editText, String openFile) throws Exception {
		this.openFile = openFile;
		swv = null;
		semaphore = null;
		
		RunApplication ra = new RunApplication();
		ra.run(editText, null);
	}
	
	public void stepApp(String editText, StepWindowView stepWindow, FetalDocumentListener fdl, 
						RSyntaxTextArea mainEditor, String openFile, MainWindowView mainWindow) throws IOException {
		editor = mainEditor;
		//editor.getVertical().setValue(0);
		fetalListener = fdl;
		this.openFile = openFile;
		swv = stepWindow;
		this.mainWindow = mainWindow;
		mainWindow.getXmRun().setEnabled(false);
		mainWindow.getXmStep().setEnabled(false);
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
			
			Properties prop = PropertiesFile.getProperties(propFile);
			trans = new TransactionService();
			ScriptSetupFile setup = new ScriptSetupFile();
			setup.readFile(openFile, trans);
			trans.initTransaction("file://" + prop.getProperty("setup") + "/fetal.properties", semaphore);

			if (semaphore != null) {
				StepListener stepListner = new StepListener();
				trans.getStep().addEventListener(stepListner);
			}
			trans.setDebugMode(true);
			try {
				long startTime = System.nanoTime();
				trans.loadRule(editText);
				long endTime = System.nanoTime();
				long execTime = endTime - startTime;
				if (semaphore != null) execTime = 0;
				FetalParser parser = trans.getfParser();
				TransactionContext tCtx = trans.getTransCtx();
				if (swv == null || swv.getHasTreeView().isSelected() == true) {
					trans.showGuiTree(parser, tCtx, execTime);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				semaphore = null;
				trans = null;

				if (stdOut != null ) {
					System.setOut(stdOut);
					stdOut = null;
				}
				if (stdErr != null) {
					System.setErr(stdErr);
					stdErr = null;
				}
				if (mainWindow != null) {
					mainWindow.getXmRun().setEnabled(true);
					mainWindow.getXmStep().setEnabled(true);
				}
				if (swv != null) {
					editor.removeAllLineHighlights();
					swv.getClose().setVisible(true);
					swv.getNext().setEnabled(false);
					swv.getHasTreeView().setEnabled(false);
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
				
				Scanner s = new Scanner(errPipe);
                while (s.hasNextLine()){
                    String line = s.nextLine() + '\n';
                    publish(line);

                }
				return null;
			}
            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks){
                    area.append(line);
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
                    area.append(line);
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
				editor.addLineHighlight((trans.getLineNum() - 1), new Color(250,178,185));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
    	
    }
}
