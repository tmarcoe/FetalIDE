package com.actionListeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FetalDocumentListener implements DocumentListener {
	private boolean modified = false;
	
	

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		modified = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		modified = true;
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		modified = true;
		
	}

}
