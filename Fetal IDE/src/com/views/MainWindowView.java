package com.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.actionListeners.FetalListeners;

public class MainWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JMenuItem emUndo;
	private JMenuItem emRedo;
	
	public void MainView() throws IOException {
		JFrame mainWindow = this;
		setTitle("Fetal Editor");

		RSyntaxTextArea mainEditor = buildSyntaxEditor();
		
		
		RTextScrollPane sp = new RTextScrollPane(mainEditor);
		sp.setLineNumbersEnabled(true);

		//TextLineNumber tln = new TextLineNumber((Jcomponent)mainEditor);

		FetalListeners fl = new FetalListeners(this, mainEditor);
	
		JMenuBar mainMenuBar = new JMenuBar();
		mainWindow.setJMenuBar(mainMenuBar);

		
		// Add the menus
		mainMenuBar.add(buildFileMenu(fl, mainEditor));
		mainMenuBar.add(buildEditMenu(fl, mainEditor));
		mainMenuBar.add(buildExecMenu(fl, mainEditor, mainWindow));
		
		buildPopUpMenu(fl, mainEditor);

		
		mainWindow.add(sp);
		
		//buildPopupMenu(fl, mainEditor, mainWindow);
		
		// Prepare to display the window
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainWindow.setSize(scaleScreenSize(screenSize, 90));
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);

	}
	
	public JMenu buildFileMenu(FetalListeners fl, RSyntaxTextArea mainEditor) {
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
	
	public JMenu buildEditMenu(FetalListeners fl, RSyntaxTextArea mainEditor) {
		JMenu editMenu = new JMenu("Edit");
		
		emUndo = new JMenuItem("Can't undo");
		emUndo.setIcon(new ImageIcon("resources/image/undo.png"));
		emUndo.setEnabled(false);
		
		emRedo = new JMenuItem("can't Redo");
		emRedo.setIcon(new ImageIcon("resources/image/redo.png"));
		emRedo.setEnabled(false);
		
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
		
		editMenu.add(emUndo);
		editMenu.add(emRedo);
		editMenu.addSeparator();
		editMenu.add(emCopy);
		editMenu.add(emCut);
		editMenu.add(emPaste);
		editMenu.add(emDelete);
		editMenu.addSeparator();
		editMenu.add(emSelectAll);
		
		fl.setUndo(emUndo, mainEditor);
		fl.setRedo(emRedo, mainEditor);
		fl.setCopy(emCopy, mainEditor);
		fl.setCut(emCut, mainEditor);
		fl.setDelete(emDelete, mainEditor);
		fl.setPaste(emPaste, mainEditor);
		fl.setSelectAll(emSelectAll, mainEditor);

		return editMenu;
	}
	
	public JMenu buildExecMenu(FetalListeners fl, RSyntaxTextArea mainEditor, JFrame mainWindow) {
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
	private  RSyntaxTextArea buildSyntaxEditor() throws IOException {
		
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/fetal", "org.fife.ui.rsyntaxtextarea.modes.FetalTokenMaker");
		textArea.setSyntaxEditingStyle("text/fetal");
		Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml"));
		theme.apply(textArea);
		
		return textArea;
	}
	private void buildPopUpMenu(FetalListeners fl, RSyntaxTextArea mainEditor) {
		JPopupMenu popup = mainEditor.getPopupMenu();
		JMenuItem refresh = new JMenuItem("Refresh");
		
		fl.setRefresh(refresh, mainEditor);
		popup.add(refresh);
	}
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}

	public JMenuItem getEmUndo() {
		return emUndo;
	}

	public JMenuItem getEmRedo() {
		return emRedo;
	}

	
}
