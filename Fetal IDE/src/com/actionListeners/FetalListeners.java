package com.actionListeners;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.menuControllers.EditMenuController;
import com.menuControllers.ExecuteMenuController;
import com.menuControllers.FileMenuController;
import com.views.MainWindowView;
import com.views.StepWindowView;

public class FetalListeners extends JFrame {
	private static final long serialVersionUID = 1L;

	private MainWindowView mainWindow;
	FileMenuController fmc;
	EditMenuController emc;
	ExecuteMenuController xmc;
	FetalDocumentListener fdl;
	StepWindowView swv = null;
	String openFile = null;

	public FetalListeners(MainWindowView mainWindow, RSyntaxTextArea mainEditor) throws HeadlessException {
		this.mainWindow = mainWindow;
		fmc = new FileMenuController();
		emc = new EditMenuController();
		fdl = new FetalDocumentListener(mainEditor);
		xmc = new ExecuteMenuController();
		mainEditor.getDocument().addDocumentListener(fdl);
	}

	public MainWindowView getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindowView mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void setExit(JMenuItem fmExit, RSyntaxTextArea mainEditor) {
		fmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				fmc.exitApp(mainWindow, fdl.isModified());
			}
		});
	}

	public void setOpen(JMenuItem fmOpen, RSyntaxTextArea mainEditor) {
		fmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fmc.openFile(mainWindow, mainEditor);
					mainEditor.discardAllEdits();
					mainWindow.getEmUndo().setEnabled(false);
					mainWindow.getEmUndo().setText("Can't undo");
					mainWindow.getEmRedo().setEnabled(false);
					mainWindow.getEmRedo().setText("Can't undo");
					if (fmc.getLastSaveFile() != null) {
						openFile = fmc.getLastSaveFile().getName();
					}
					fdl.setModified(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
	}
	
	public void setNew(JMenuItem fmNew, RSyntaxTextArea mainEditor) {
		fmNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fdl.setModified(false);
				fmc.newFile(mainEditor);
				openFile = null;
			}});
		
	}
	
	public void setSaveAs(JMenuItem fmSave, RSyntaxTextArea mainEditor) {
		
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

	public void setUndo(JMenuItem emUndo, RSyntaxTextArea mainEditor ) {
		fdl.setUndo(emUndo);
		emUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				emc.undo(mainEditor);
				if (mainEditor.canUndo() == false) {
					fdl.setModified(false);
					emUndo.setEnabled(false);
					emUndo.setText("Can't undo");
				}
			}});
	}
	
	public void setRedo(JMenuItem emRedo, RSyntaxTextArea mainEditor ) {
		fdl.setRedo(emRedo);
		emRedo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.redo(mainEditor);
				fdl.setModified(true);
				if (mainEditor.canRedo() == false) {
					emRedo.setEnabled(false);
					emRedo.setText("Can't Redo");
				}
			}});
	}

	public void setCopy(JMenuItem emCopy, RSyntaxTextArea mainEditor) {
		emCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.copyText(mainEditor);
			}
		});
	}

	public void setCut(JMenuItem emCut, RSyntaxTextArea mainEditor) {

		emCut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.cutText(mainEditor);
			}
		});
	}
	
	public void setDelete(JMenuItem emDelete, RSyntaxTextArea mainEditor) {
		
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
	
	public void setPaste(JMenuItem emPaste, RSyntaxTextArea mainEditor) {
		
		emPaste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.PasteText(mainEditor);
			}
		});
	}

	public void setSelectAll(JMenuItem emSelectAll, RSyntaxTextArea mainEditor) {
		
		emSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emc.SelectAll(mainEditor);
			}
		});
	}

	public void setSave(JMenuItem fmSave, RSyntaxTextArea mainEditor) {
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
	
	public void setRun(JMenuItem xmRun, RSyntaxTextArea mainEditor) {
		
	xmRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String editText = mainEditor.getText();
					if (editText.contains("begin") && editText.contains("end")) {
						xmc.runApp(mainEditor.getText(), openFile);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});		
	}
	
	public void setStep(JMenuItem stp, RSyntaxTextArea mainEditor, JFrame mainWindow) {
		FetalListeners fl = this;
		stp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String editText = mainEditor.getText();
				if (editText.contains("begin") && editText.contains("end")) {
					try {
						StepWindowView stepWindow = new StepWindowView(fl,mainEditor);
						swv = stepWindow;
						stepWindow.stepWindow();
						xmc.stepApp(editText, stepWindow, fdl, mainEditor, openFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}});
	}
	
	public void setNext(JButton xmNext, RSyntaxTextArea mainEditor) {
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
				if (swv != null) {
					swv.getView().dispose();
				}
				stepWindow.dispose();
				swv = null;
			}});
	}
	
	public void setRefresh(JMenuItem refresh, RSyntaxTextArea mainEditor) {
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String menuBuffer = mainEditor.getText();
				mainEditor.setText(menuBuffer);
				fdl.setModified(false);
			}});
	}

}
