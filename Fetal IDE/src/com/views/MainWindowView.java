package com.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.actionListeners.FetalListeners;
import com.syntaxHighlighting.JEditTextArea;
import com.syntaxHighlighting.languages.FetalTokenMarker;

public class MainWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	JPopupMenu popup;
	
	public void MainView() {
		JFrame mainWindow = new JFrame("Fetal Editor");
		JEditTextArea mainEditor = new JEditTextArea();
		mainEditor.getPainter().isBracketHighlightEnabled();
		FetalListeners fl = new FetalListeners(mainWindow, mainEditor);
	
		JMenuBar mainMenuBar = new JMenuBar();
		mainWindow.setJMenuBar(mainMenuBar);
		

		MouseListener popupListener = new PopupListener(popup);
		mainEditor.addMouseListener(popupListener);

		
		// Add the menus
		mainMenuBar.add(buildFileMenu(fl, mainEditor));
		mainMenuBar.add(buildEditMenu(fl, mainEditor));
		mainMenuBar.add(buildExecMenu(fl, mainEditor, mainWindow));


		mainEditor.setTokenMarker(new FetalTokenMarker());
		mainWindow.add(mainEditor);
		
		//buildPopupMenu(fl, mainEditor, mainWindow);
		
		// Prepare to display the window
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setSize(scaleScreenSize(screenSize, 90));
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);

	}
	
	public JMenu buildFileMenu(FetalListeners fl, JEditTextArea mainEditor) {
		JMenu fileMenu = new JMenu("File");
		JMenuItem fmNew = new JMenuItem("New", KeyEvent.VK_N );
		fmNew.setIcon(new ImageIcon("resources/image/new.gif"));
		JMenuItem fmOpen = new JMenuItem("Open", KeyEvent.VK_O);
		fmOpen.setIcon(new ImageIcon("resources/image/open.png"));
		JMenuItem fmSave = new JMenuItem("Save", KeyEvent.VK_S);
		fmSave.setIcon(new ImageIcon("resources/image/save.gif"));
		JMenuItem fmSaveAs = new JMenuItem("Save As", KeyEvent.VK_A);
		fmSaveAs.setIcon(new ImageIcon("resources/image/saveAs.png"));
		JMenuItem fmExit = new JMenuItem("Exit", KeyEvent.VK_X);
		fmExit.setIcon(new ImageIcon("resources/image/exit.png"));

		fileMenu.add(fmNew);
		fileMenu.add(fmOpen);
		fileMenu.add(fmSave);
		fileMenu.add(fmSaveAs);
		fileMenu.addSeparator();
		fileMenu.add(fmExit); 
		
		fl.setNew(fmNew, mainEditor);
		fl.setOpen(fmOpen, mainEditor);
		fl.setSave(fmSave, mainEditor); 
		fl.setSaveAs(fmSaveAs, mainEditor);
		fl.setExit(fmExit, mainEditor);
	
		return fileMenu;
	}
	
	public JMenu buildEditMenu(FetalListeners fl, JEditTextArea mainEditor) {
		JMenu editMenu = new JMenu("Edit");
		JMenuItem emCopy = new JMenuItem("Copy", KeyEvent.VK_C);
		emCopy.setIcon(new ImageIcon("resources/image/copy.png"));
		JMenuItem emCut = new JMenuItem("Cut", KeyEvent.VK_T);
		emCut.setIcon(new ImageIcon("resources/image/cut.png"));
		JMenuItem emPaste = new JMenuItem("Paste", KeyEvent.VK_P);
		emPaste.setIcon(new ImageIcon("resources/image/paste.png"));
		JMenuItem emDelete = new JMenuItem("Delete", KeyEvent.VK_D);
		emDelete.setIcon(new ImageIcon("resources/image/delete.png"));
		JMenuItem emSelectAll = new JMenuItem("Select All", KeyEvent.VK_L);
		emSelectAll.setIcon(new ImageIcon("resources/image/selectAll.png"));
		
		editMenu.add(emCopy);
		editMenu.add(emCut);
		editMenu.add(emPaste);
		editMenu.add(emDelete);
		editMenu.addSeparator();
		editMenu.add(emSelectAll);
		
		fl.setCopy(emCopy, mainEditor);
		fl.setCut(emCut, mainEditor);
		fl.setDelete(emDelete, mainEditor);
		fl.setPaste(emPaste, mainEditor);
		fl.setSelectAll(emSelectAll, mainEditor);

		return editMenu;
	}
	
	public JMenu buildExecMenu(FetalListeners fl, JEditTextArea mainEditor, JFrame mainWindow) {
		JMenu execMenu = new JMenu("Execute");
		JMenuItem xmRun = new JMenuItem("Run");
		xmRun.setIcon(new ImageIcon("resources/image/run.png"));
		JMenuItem xmStep = new JMenuItem("Step");
		xmStep.setIcon(new ImageIcon("resources/image/step.png"));

		
		execMenu.add(xmRun);
		execMenu.add(xmStep);
		
		fl.setRun(xmRun, mainEditor);
		fl.setStep(xmStep, mainEditor, mainWindow);

		return execMenu;
	}
	@SuppressWarnings("unused")
	private JPopupMenu buildPopupMenu(FetalListeners fl, JEditTextArea mainEditor, JFrame mainWindow) {
		popup = new JPopupMenu();
		
		JMenuItem pCopy = new JMenuItem("Copy");
		JMenuItem pCut = new JMenuItem("Cut");
		JMenuItem pPaste = new JMenuItem("Paste");
		popup.add(pCopy);
		popup.add(pCut);
		popup.add(pPaste);
		
		fl.setCopy(pCopy, mainEditor);
		fl.setCut(pCut, mainEditor);
		fl.setPaste(pPaste, mainEditor);
		
		/*mainEditor.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {            
	            popup.show(mainEditor, e.getX(), e.getY());
	          }               

		});*/
		mainWindow.add(popup);
		//mainEditor.setInheritsPopupMenu(true);
		
		return popup;
	}
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}
	class PopupListener extends MouseAdapter {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.setVisible(true);
            }
        }
    }

}
