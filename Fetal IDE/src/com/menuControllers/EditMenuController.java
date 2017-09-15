package com.menuControllers;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class EditMenuController extends JFrame {
	private static final long serialVersionUID = 1L;

	public void copyText(RSyntaxTextArea mainEditor) {
		mainEditor.copy();
	}
	
	public void cutText(RSyntaxTextArea mainEditor) {
		mainEditor.cut();
	}
	
	public void pasteText(RSyntaxTextArea mainEditor) {
		mainEditor.paste();
	}

	public void undo(RSyntaxTextArea mainEditor) {
		mainEditor.undoLastAction();
	}

	public void redo(RSyntaxTextArea mainEditor) {
		mainEditor.redoLastAction();
	}


	
	public void deleteText(RSyntaxTextArea mainEditor) throws BadLocationException {


		int start = mainEditor.getSelectionStart();
		int length = mainEditor.getSelectedText().length();
		mainEditor.getDocument().remove(start, length);
	}

	public void PasteText(RSyntaxTextArea mainEditor) {
		mainEditor.paste();
	}

	public void SelectAll(RSyntaxTextArea mainEditor) {
		mainEditor.selectAll();
	}



}
