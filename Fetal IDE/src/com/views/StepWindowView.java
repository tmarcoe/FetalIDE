package com.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.actionListeners.FetalListeners;
import com.syntaxHighlighting.JEditTextArea;

public class StepWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextPane varDisplay;
	private JTextPane outDisplay;
	private JButton next;
	private JButton close;
	private FetalListeners fl;
	private JEditTextArea mainEditor;
	
	
	
	public StepWindowView(FetalListeners fl, JEditTextArea editor) throws HeadlessException {
		this.fl = fl;
		this.mainEditor = editor;
	}
	public JFrame stepWindow() {
		JFrame stepWindow = new JFrame("Step Window");

		stepWindow.setLayout(new BorderLayout());
		Container cp = stepWindow.getContentPane();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(16,1));
		buttonPanel.setBackground(new Color(240,240,255));
		buttonPanel.setPreferredSize(new Dimension(100,100));
		next = new JButton("Next >>");
		fl.setNext(next, mainEditor);
		close = new JButton("Close");
		close.setVisible(false);
		fl.setCloseButton(close, stepWindow);
		buttonPanel.add(next);
		buttonPanel.add(close);
		cp.add(buttonPanel, BorderLayout.LINE_END);
		varDisplay = newWindow(cp, new Dimension(700,100), BorderLayout.CENTER, "Variables");
		outDisplay = newWindow(cp, new Dimension(800,100), BorderLayout.PAGE_END, "Output");
		
		stepWindow.setAlwaysOnTop(true);
		stepWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		stepWindow.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		stepWindow.setSize(scaleScreenSize(screenSize, 60));
		stepWindow.setLocationRelativeTo(null);
		stepWindow.setVisible(true);
		
		return stepWindow;
		
	}
	private JTextPane newWindow(Container cp,  Dimension size, String layout, String title) {
		JTextPane textArea = new JTextPane();
		textArea.setPreferredSize(size);
		textArea.setBorder(BorderFactory.createTitledBorder(title));
		cp.add(textArea, layout);
		
		return textArea;
	}

	public JTextPane getVarDisplay() {
		return varDisplay;
	}
	
	public JTextPane getOutDisplay() {
		return outDisplay;
	}
	
	public JButton getClose() {
		return close;
	}

	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}

}
