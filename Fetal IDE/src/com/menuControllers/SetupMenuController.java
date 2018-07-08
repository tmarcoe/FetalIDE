package com.menuControllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.actionListeners.FetalListeners;
import com.utils.PropertiesFile;
import com.views.EnvironmentWindowView;
import com.views.SetupWindowView;

public class SetupMenuController extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final String propFile = "resources/config/ide.properties";

	
	public void preferncesMenu(SetupWindowView prefWindow) {
		prefWindow.setupWindow();
	}
	
	public void setWorkspace(SetupWindowView prefWindow) throws IOException {
		Properties prop = PropertiesFile.getProperties(propFile);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fc.showSaveDialog(prefWindow);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			FileOutputStream fos = new FileOutputStream(propFile);
			File dir = fc.getSelectedFile();
			prop.setProperty("workspace", dir.getAbsolutePath());
			prefWindow.getPath().setText(dir.getAbsolutePath());
			prop.store(fos, "Changed by Java program");
		}
	}
	
	public void closeSetup(SetupWindowView prefWindow) {
		prefWindow.dispose();
	}

	public void saveEnvironment(FetalListeners fl) {
		new EnvironmentWindowView().xmlEditor();
	}
	
	public void setupProps(SetupWindowView prefWindow) throws IOException {
		Properties prop = PropertiesFile.getProperties(propFile);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fc.showSaveDialog(prefWindow);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			FileOutputStream fos = new FileOutputStream(propFile);
			File dir = fc.getSelectedFile();
			String propDir = dir.getAbsolutePath();
			propDir = pathToURL(propDir);
			

			prop.setProperty("setup", propDir);
			
			prefWindow.getpFile().setText(propDir);
			prop.store(fos, "Changed by Java program");
		}
		
	}
	private String pathToURL(String path) {
		String urlStr = "";
		int ndxOf = path.indexOf("C:\\");
		
		path = path.substring(ndxOf + 2);
		
		urlStr =  path.replace(File.separatorChar, '/');
		
		return urlStr;
	}

}
