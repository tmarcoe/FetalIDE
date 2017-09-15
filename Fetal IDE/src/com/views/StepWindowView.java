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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.actionListeners.FetalListeners;

public class  StepWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea varDisplay;
	private JTextArea outDisplay;
	private JButton next;
	private JButton close;
	private JCheckBox hasTreeView;
	private FetalListeners fl;
	private RSyntaxTextArea mainEditor;
	private ErrorWindowView ewv;
	
	
	
	public StepWindowView(FetalListeners fl, RSyntaxTextArea editor) throws HeadlessException {
		this.fl = fl;
		this.mainEditor = editor;
	}
	public JFrame stepWindow() {
		JFrame stepWindow = this;
		setTitle("Step Window");

		stepWindow.setLayout(new BorderLayout());
		Container cp = stepWindow.getContentPane();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(16,1));
		buttonPanel.setBackground(new Color(240,240,255));
		buttonPanel.setPreferredSize(new Dimension(100,100));
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Dashboard"));
		next = new JButton("Next >>");
		fl.setNext(next, mainEditor);
		close = new JButton("Close");
		close.setVisible(false);
		hasTreeView = new JCheckBox();
		hasTreeView.setSelected(false);
		hasTreeView.setText("Tree View");
		fl.setCloseButton(close, stepWindow);
		buttonPanel.add(next);
		buttonPanel.add(close);
		buttonPanel.add(hasTreeView);
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
		ewv = new ErrorWindowView();
		ewv.display();
		
		return stepWindow;
		
	}
	private JTextArea newWindow(Container cp,  Dimension size, String layout, String title) {

		JTextArea textArea = new JTextArea();
		/*
		JScrollPane vScroll = new JScrollPane(textArea, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		*/
		textArea.setPreferredSize(size);
		textArea.setBorder(BorderFactory.createTitledBorder(title));
		
		cp.add(textArea, layout);

		
		return textArea;
	}

	public synchronized void updateVarDisplay(String text) {
		varDisplay.setText(text);
	}
	
	public synchronized JTextArea getVarDisplay() {
		return varDisplay;
	}
	
	public synchronized JTextArea getOutDisplay() {
		return outDisplay;
	}
	
	public synchronized JButton getClose() {
		return close;
	}

	public JCheckBox getHasTreeView() {
		return hasTreeView;
	}
	public JTextArea getErrorWindow() {
		return ewv.getErrWindow();
	}
	
	public ErrorWindowView getView() {
		return ewv;
	}
	
	public JButton getNext() {
		return next;
	}
	
	
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}

}
