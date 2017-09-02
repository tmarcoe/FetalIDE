package com.menuControllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.syntaxHighlighting.JEditTextArea;

public class FileMenuController extends JFrame {
	private static final long serialVersionUID = 1L;

	private File lastDir = null;
	private File lastSaveFile = null;

	public File getLastSaveFile() {
		return lastSaveFile;
	}

	public void exitApp(JFrame mainWindow, boolean modified) {
		String msg = "Are you sure you want to discard your changes and exit?";
		String titleBar = "File Not Saved!";
		if (modified) {
			int result = JOptionPane.showConfirmDialog(null, msg, titleBar, JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
	}

	public void openFile(JFrame mainWindow, JEditTextArea mainEditor) throws IOException {
		JFileChooser fc = new JFileChooser();

		if (lastDir != null)
			fc.setCurrentDirectory(lastDir);

		int result = fc.showOpenDialog(mainWindow);

		if (result == JFileChooser.APPROVE_OPTION) {
			lastSaveFile = fc.getSelectedFile();
			lastDir = fc.getCurrentDirectory();
			String abs = fc.getSelectedFile().getAbsolutePath();
			String editPath = abs.substring(0, abs.lastIndexOf('\\'));
			String editFile = abs.substring(abs.lastIndexOf('\\') + 1);
			Path filePath = Paths.get(editPath, editFile);
			List<String> lines = Files.readAllLines(filePath);
			String editBuffer = "";
			for (String line : lines) {
				editBuffer += line + "\n";
			}
			mainEditor.setText(editBuffer);
		}

	}

	public boolean saveFile(JFrame mainWindow, JEditTextArea mainEditor) throws IOException {
		if (lastSaveFile == null) {
			saveFileAs(mainWindow, mainEditor);
		}
		save(lastSaveFile, mainEditor.getText());

		return true;
	}

	public boolean saveFileAs(JFrame mainWindow, JEditTextArea mainEditor) throws IOException {

		JFileChooser fc = new JFileChooser();

		if (lastDir != null)
			fc.setCurrentDirectory(lastDir);

		int result = fc.showSaveDialog(mainWindow);

		if (result == JFileChooser.APPROVE_OPTION) {
			lastSaveFile = fc.getSelectedFile();
			lastDir = fc.getCurrentDirectory();

			save(lastSaveFile, mainEditor.getText());
			return true;

		}
		return false;
	}

	private void save(File file, String buffer) throws IOException {
		if (file != null) {
			FileWriter fw = new FileWriter(file);
			fw.write(buffer);
			fw.flush();
			fw.close();
		}
	}

	public void newFile(JEditTextArea mainEditor) {
		lastSaveFile = null;
		mainEditor.setText("");
	}
}
