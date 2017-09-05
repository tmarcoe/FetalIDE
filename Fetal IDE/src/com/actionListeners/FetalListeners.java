package com.actionListeners;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import com.menuControllers.EditMenuController;
import com.menuControllers.ExecuteMenuController;
import com.menuControllers.FileMenuController;
import com.syntaxHighlighting.JEditTextArea;
import com.views.StepWindowView;

public class FetalListeners extends JFrame {
	private static final long serialVersionUID = 1L;

	private JFrame mainWindow;
	FileMenuController fmc;
	EditMenuController emc;
	ExecuteMenuController xmc;
	FetalDocumentListener fdl;
	String openFile = null;

	public FetalListeners(JFrame mainWindow, JEditTextArea mainEditor) throws HeadlessException {
		this.mainWindow = mainWindow;
		fmc = new FileMenuController();
		emc = new EditMenuController();
		fdl = new FetalDocumentListener();
		xmc = new ExecuteMenuController();
		mainEditor.getDocument().addDocumentListener(fdl);
	}

	public JFrame getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(JFrame mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void setExit(JMenuItem fmExit, JEditTextArea mainEditor) {
		fmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				fmc.exitApp(mainWindow, fdl.isModified());
			}
		});
	}

	public void setOpen(JMenuItem fmOpen, JEditTextArea mainEditor) {
		fmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fmc.openFile(mainWindow, mainEditor);
					openFile = fmc.getLastSaveFile().getName();
					fdl.setModified(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
	}
	
	public void setNew(JMenuItem fmNew, JEditTextArea mainEditor) {
		fmNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fdl.setModified(false);
				fmc.newFile(mainEditor);
				openFile = null;
			}});
		
	}
	
	public void setSaveAs(JMenuItem fmSave, JEditTextArea mainEditor) {
		
		fmSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fdl.setModified(!fmc.saveFileAs(mainWindow, mainEditor));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	public void setCopy(JMenuItem emCopy, JEditTextArea mainEditor) {
		emCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.copyText(mainEditor);
			}
		});
	}

	public void setCut(JMenuItem emCut, JEditTextArea mainEditor) {

		emCut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.cutText(mainEditor);
			}
		});
	}
	
	public void setDelete(JMenuItem emDelete, JEditTextArea mainEditor) {
		
		emDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					emc.deleteText(mainEditor);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void setPaste(JMenuItem emPaste, JEditTextArea mainEditor) {
		
		emPaste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.PasteText(mainEditor);
			}
		});
	}

	public void setSelectAll(JMenuItem emSelectAll, JEditTextArea mainEditor) {
		
		emSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.SelectAll(mainEditor);
			}
		});
	}

	public void setSave(JMenuItem fmSave, JEditTextArea mainEditor) {
		fmSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fdl.setModified(!fmc.saveFile(mainWindow, mainEditor));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});		
	}
	
	public void setRun(JMenuItem xmRun, JEditTextArea mainEditor) {
		
	xmRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String editText = mainEditor.getText();
					if (editText.contains("begin") && editText.contains("end")) {
						xmc.runApp(mainEditor.getText());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});		
	}
	
	public void setStep(JMenuItem stp, JEditTextArea mainEditor, JFrame mainWindow) {
		FetalListeners fl = this;
		stp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String editText = mainEditor.getText();
				if (editText.contains("begin") && editText.contains("end")) {
					try {
						StepWindowView stepWindow = new StepWindowView(fl,mainEditor);
						stepWindow.stepWindow();
						xmc.stepApp(editText, stepWindow, fdl, mainEditor, openFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}});
	}
	
	public void setNext(JButton xmNext, JEditTextArea mainEditor) {
		xmNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				xmc.Next(mainEditor);
			}});
	}
	public void setCloseButton(JButton close, JFrame stepWindow) {
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stepWindow.dispose();
			}});
	}

}
