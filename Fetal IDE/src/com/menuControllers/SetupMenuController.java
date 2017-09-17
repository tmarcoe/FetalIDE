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

	private Properties prop = PropertiesFile.getProperties(propFile);
	
	public void preferncesMenu(SetupWindowView prefWindow) {
		prefWindow.setupWindow();
	}
	
	public void setWorkspace(SetupWindowView prefWindow) throws IOException {
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

}
