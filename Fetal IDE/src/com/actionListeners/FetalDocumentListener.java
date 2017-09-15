package com.actionListeners;

import javax.swing.JMenuItem;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.views.MainWindowView;

public class FetalDocumentListener implements DocumentListener {
	private boolean modified;

	RSyntaxTextArea mainEditor;
	MainWindowView mainWindow;
	private JMenuItem undo;
	private JMenuItem redo;
	
	
	public FetalDocumentListener(RSyntaxTextArea mainEditor, MainWindowView mainWindow) {
		super();
		this.mainEditor = mainEditor;
		this.mainWindow = mainWindow;
	}

	public JMenuItem getUndo() {
		return undo;
	}

	public void setUndo(JMenuItem undo) {
		this.undo = undo;
	}

	public JMenuItem getRedo() {
		return redo;
	}

	public void setRedo(JMenuItem redo) {
		this.redo = redo;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {

		String fileAndStatus = mainWindow.getStatus().getText().replace('*', '\0');
		this.modified = modified;
		if (modified == false && fileAndStatus.length() > 0) {
			mainWindow.getStatus().setText(fileAndStatus);
		}else if (fileAndStatus.length() > 0 ){
			mainWindow.getStatus().setText(fileAndStatus + "*");
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {

		setModified(true);
		setUndoToTrue();
		setRedoToFalse();
		setRedoToTrue();

	}

	@Override
	public void removeUpdate(DocumentEvent e) {

		setModified(true);
		setUndoFalse();
		setRedoToFalse();
		setRedoToTrue();

	}

	@Override
	public void changedUpdate(DocumentEvent e) {

		setModified(true);
		setUndoToTrue();
		setRedoToTrue();
		setRedoToFalse();

	}
	
	private void setUndoFalse() {
		
		if (undo != null && mainEditor.canUndo() == false) {
			undo.setEnabled(false);
			undo.setText("Can't Undo");
			setModified(false);
		}
	}
	
	private void setRedoToFalse() {
		
		if (redo != null && mainEditor.canRedo() == false) {
			redo.setEnabled(false);
			redo.setText("Can't Redo");
			
		}
	}
	
	private void setUndoToTrue() {
		
		if (undo != null ) {
			undo.setEnabled(true);
			undo.setText("Undo");
		}
		
	}
	
	private void setRedoToTrue() {
		
		if (redo != null && mainEditor.canRedo() == true) {
			redo.setEnabled(true);
			redo.setText("Redo");
		}
	}
	

}
