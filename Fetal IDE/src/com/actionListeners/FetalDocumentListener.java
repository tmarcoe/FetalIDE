package com.actionListeners;

import javax.swing.JMenuItem;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class FetalDocumentListener implements DocumentListener {
	private boolean modified;

	RSyntaxTextArea mainEditor;
	private JMenuItem undo;
	private JMenuItem redo;
	
	
	public FetalDocumentListener(RSyntaxTextArea mainEditor) {
		super();
		this.mainEditor = mainEditor;
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
		this.modified = modified;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		modified = true;
		setUndoToTrue();
		setRedoToFalse();
		setRedoToTrue();

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		modified = true;
		setUndoFalse();
		setRedoToFalse();
		setRedoToTrue();

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		modified = true;
		setUndoToTrue();
		setRedoToTrue();
		setRedoToFalse();

	}
	
	private void setUndoFalse() {
		
		if (undo != null && mainEditor.canUndo() == false) {
			undo.setEnabled(false);
			undo.setText("Can't Undo");
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
