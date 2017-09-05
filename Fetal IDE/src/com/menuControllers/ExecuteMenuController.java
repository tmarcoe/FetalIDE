package com.menuControllers;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import com.actionListeners.FetalDocumentListener;
import com.ftl.derived.FetalParser;
import com.ftl.derived.FetalParser.TransactionContext;
import com.ftl.events.Step;
import com.ftl.events.StepEvent;
import com.ftl.events.StepEventListener;
import com.ftl.helper.Variable;
import com.helper.TransactionService;
import com.syntaxHighlighting.JEditTextArea;
import com.views.StepWindowView;
import com.xml.ScriptSetupFile;

public class ExecuteMenuController {
	private Semaphore semaphore = null;
	private TransactionService trans = null;
	private JButton xmNext = null;
	private StepWindowView swv=null;
	private JEditTextArea editor = null;
	private FetalDocumentListener fetalListener;
	private String openFile;

	public void runApp(String editText) throws Exception {

		RunApplication ra = new RunApplication();
		ra.run(editText, null);
	}
	
	public void stepApp(String editText, StepWindowView stepWindow, FetalDocumentListener fdl, JEditTextArea mainEditor, String openFile) throws IOException {
		editor = mainEditor;
		fetalListener = fdl;
		this.openFile = openFile;
		swv = stepWindow;
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
	
	public void Next(JEditTextArea mainEditor) {
		if (semaphore != null && trans !=null) {
			semaphore.release();
		}
	}


	class RunApplication {

		public void run(String editText, Semaphore semaphore) throws Exception {
			trans = new TransactionService();
			ScriptSetupFile setup = new ScriptSetupFile();
			setup.readFile(openFile, trans);
			
			trans.setStep(new Step());
			StepListener stepListner = new StepListener();
			trans.getStep().addEventListener(stepListner);
			trans.setSemaphore(semaphore);
			trans.setDebugMode(true);
			try {
				trans.initTransaction("file:///home/donzalma/public_html/config/fetal.properties");
				trans.loadRule(editText);
				FetalParser parser = trans.getfParser();
				TransactionContext tCtx = trans.getTransCtx();
				trans.showGuiTree(parser, tCtx);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				semaphore = null;
				trans = null;
				if (xmNext != null) {
					xmNext.setVisible(false);
					xmNext = null;
				}
				if (swv != null) {
					String txt = editor.getText();
					editor.setText(txt);
					fetalListener.setModified(false);
					swv.getClose().setVisible(true);
				}
			}
		}

	}
    public static void console(final JTextPane area) throws IOException {
        area.setContentType("text/html");
        final PipedInputStream outPipe = new PipedInputStream();
        PrintStream printStream = new PrintStream(new PipedOutputStream(outPipe), true);

        System.setOut(printStream);
        new SwingWorker<Void, String>() {
            @SuppressWarnings("resource")
			@Override
            protected Void doInBackground() throws Exception {
                Scanner s = new Scanner(outPipe);
                while (s.hasNextLine()){
                    String line = s.nextLine();
                    publish(line + "\n");
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks){
                    area.setText("<font size=\"3\" color=\"black\">"+line+"</font>");
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
			editor.getPainter().HighlightLine(editor.getGraphics(), trans.getLineNum(), trans.getPrevLine());
		}
    	
    }
}
