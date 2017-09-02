package com.views;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class PublishWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public void publishWindow() {
		JFrame publishWindow = new JFrame("Publish Variables");
		
		publishWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		publishWindow.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		publishWindow.setSize(scaleScreenSize(screenSize, 40));
		publishWindow.setLocationRelativeTo(null);
		publishWindow.setVisible(true);

	}
	
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}

}
