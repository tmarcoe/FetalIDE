package com.menuControllers;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

import com.syntaxHighlighting.JEditTextArea;

public class EditMenuController extends JFrame {
	private static final long serialVersionUID = 1L;

	public void copyText(JEditTextArea mainEditor) {
		mainEditor.copy();
	}
	
	public void cutText(JEditTextArea mainEditor) {
		mainEditor.cut();
	}
	
	public void pasteText(JEditTextArea mainEditor) {
		mainEditor.paste();
	}

	public void deleteText(JEditTextArea mainEditor) throws BadLocationException {


		int start = mainEditor.getSelectionStart();
		int length = mainEditor.getSelectedText().length();
		mainEditor.getDocument().remove(start, length);
	}

	public void PasteText(JEditTextArea mainEditor) {
		mainEditor.paste();
	}

	public void SelectAll(JEditTextArea mainEditor) {
		mainEditor.selectAll();
	}



}
