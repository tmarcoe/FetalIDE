package com.views;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ErrorWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea errWindow;

	public ErrorWindowView() throws HeadlessException {
		super("Error Window");
	}
	
	public void display() {

		setUndecorated(true);
		setOpacity((float) .8);
		setAlwaysOnTop(true);
		errWindow = new JTextArea();
		add(errWindow);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(scaleScreenSize(screenSize, 40));
		placeWindow(this);
		setVisible(true);

	}
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}

	private void placeWindow(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();

	    int x = (int) (dimension.getWidth() - frame.getWidth());
	    int y = (int) (0);

		frame.setLocation(x, y);
	}
	
	public JTextArea getErrWindow() {
		return errWindow;
	}

}
