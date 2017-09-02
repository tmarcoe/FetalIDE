package com.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.actionListeners.FetalListeners;
import com.syntaxHighlighting.JEditTextArea;
import com.syntaxHighlighting.languages.FetalTokenMarker;

public class MainWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	public void MainView() {
		JFrame mainWindow = new JFrame("Fetal Editor");
		JEditTextArea mainEditor = new JEditTextArea();
		mainEditor.getPainter().isBracketHighlightEnabled();
		FetalListeners fl = new FetalListeners(mainWindow, mainEditor);
	
		JMenuBar mainMenuBar = new JMenuBar();
		mainWindow.setJMenuBar(mainMenuBar);
		
		JButton xmNext = new JButton("Next>>");
		xmNext.setVisible(false);
		
		/*popup.add(emCopy);
		popup.add(emCut);
		popup.add(emPaste);
		
		popup.addPopupMenuListener(new PopupPrintListener());
		mainWindow.addMouseListener(new MousePopupListener());*/
		
		
		// Add the menus
		mainMenuBar.add(buildFileMenu(fl, mainEditor));
		mainMenuBar.add(buildEditMenu(fl, mainEditor));
		mainMenuBar.add(buildExecMenu(fl, mainEditor, mainWindow));
		mainMenuBar.add(xmNext);

		mainEditor.setTokenMarker(new FetalTokenMarker());
		
		mainWindow.add(mainEditor);
		
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
		JMenuItem fmOpen = new JMenuItem("Open", KeyEvent.VK_O);
		JMenuItem fmSave = new JMenuItem("Save", KeyEvent.VK_S);
		JMenuItem fmSaveAs = new JMenuItem("Save As", KeyEvent.VK_A);
		JMenuItem fmExit = new JMenuItem("Exit", KeyEvent.VK_X);

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
		JMenuItem emCut = new JMenuItem("Cut", KeyEvent.VK_T);
		JMenuItem emPaste = new JMenuItem("Paste", KeyEvent.VK_P);
		JMenuItem emDelete = new JMenuItem("Delete", KeyEvent.VK_D);
		JMenuItem emSelectAll = new JMenuItem("Select All", KeyEvent.VK_L);
		
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
		JMenuItem xmStep = new JMenuItem("Step");

		
		execMenu.add(xmRun);
		execMenu.add(xmStep);
		
		fl.setRun(xmRun, mainEditor);
		fl.setStep(xmStep, mainEditor, mainWindow);

		return execMenu;
	}
	
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}

}
