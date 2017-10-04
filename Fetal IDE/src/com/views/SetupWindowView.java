package com.views;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.actionListeners.FetalListeners;
import com.utils.PropertiesFile;

public class SetupWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	private FetalListeners fl;
	private JTextField path;
	private JTextField pFile;
	private final String propFile = "resources/config/ide.properties";
	private Properties prop = PropertiesFile.getProperties(propFile);
	
	public JTextField getPath() {
		return path;
	}
	
	public JTextField getpFile() {
		return pFile;
	}

	public SetupWindowView(FetalListeners fl) throws HeadlessException {
		super();
		this.fl = fl;
	}
	public void setupWindow() {
		SetupWindowView setupWindow =  this;
		setupWindow.setUndecorated(true);
		setupWindow.setLayout(new FlowLayout());
		Container cp = setupWindow.getContentPane();
		
		JLabel label = new JLabel("Path to workspace: ");
		path = new JTextField(30);
		path.setEditable(false);
		path.setText(prop.getProperty("workspace"));
		
		JLabel label2 = new JLabel("URL to properties file: ");
		pFile = new JTextField(30);
		pFile.setEditable(false);
		pFile.setText(prop.getProperty("setup"));
		
		JButton file = new JButton("Set Workspace");
		JButton close = new JButton("Close");
		JButton setup = new JButton("Set FTL Properties File");
		fl.setWorkspace(file);
		fl.setupWindowClose(close);
		fl.setFTLSetup(setup);
		
		cp.add(label);
		cp.add(path);
		cp.add(label2);
		cp.add(pFile);
		cp.add(file);
		cp.add(setup);
		cp.add(close);
		
		
		
		setupWindow.setAlwaysOnTop(true);
		setupWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setupWindow.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setupWindow.setSize(scaleScreenSize(screenSize, 30));
		setupWindow.setLocationRelativeTo(null);
		setupWindow.setVisible(true);
		
	}
	
	
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 200);
		d.width *= (percent / 100);
		
		return d;
	}


}
