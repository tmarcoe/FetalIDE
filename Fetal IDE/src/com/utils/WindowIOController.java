package com.utils;

import java.awt.Color;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class WindowIOController {
	private static PrintStream stdOut = null;
	private static PrintStream stdErr = null;
	private static PrintStream errPrintStream;
	private static PrintStream stdPrintStream;
	private static PipedInputStream errPipe;
	private static PipedInputStream outPipe;

	
	public static void errConsole(final JTextArea area) throws IOException {
		area.setForeground(Color.RED);
		errPipe = new PipedInputStream();
		errPrintStream = new PrintStream(new PipedOutputStream(errPipe), true);
		
		stdErr = System.err;
		System.setErr(errPrintStream);
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
        outPipe = new PipedInputStream();
        stdPrintStream = new PrintStream(new PipedOutputStream(outPipe), true);

        stdOut = System.out;
        System.setOut(stdPrintStream);
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
    
    public static void stdClose() throws IOException {
    	
    	if (stdOut == null) {
    		throw new IOException("StdOut already closed");
    	}
    	
    	System.setOut(stdOut);
    	stdOut = null;
    	stdPrintStream.close();
    	outPipe.close();
    }
    
    public static void errClose() throws IOException {
    	
    	if (stdErr == null) {
    		throw new IOException("StdErr already closed");
    	}
    	
    	System.setOut(stdErr);
    	stdErr = null;
    	errPrintStream.close();
    	errPipe.close();

    }  
   
	public static PrintStream getStdOut() {
		return stdOut;
	}
	
	public static void setStdOut(PrintStream stdOut) {
		WindowIOController.stdOut = stdOut;
	}
	
	public static PrintStream getStdErr() {
		return stdErr;
	}
	
	public static void setStdErr(PrintStream stdErr) {
		WindowIOController.stdErr = stdErr;
	}
    
    
}
