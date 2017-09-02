package com.views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PopupWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	
	
	
	  class MousePopupListener extends MouseAdapter {
		    public void mousePressed(MouseEvent e) {
		      checkPopup(e);
		    }

		    public void mouseClicked(MouseEvent e) {
		      checkPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		      checkPopup(e);
		    }

		    private void checkPopup(MouseEvent e) {
		      if (e.isPopupTrigger()) {
		        //popup.show(FetalEditor.this, e.getX(), e.getY());
		      }
		    }
		  }
	  class PopupPrintListener implements PopupMenuListener {
		    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		      System.out.println("Popup menu will be visible!");
		    }

		    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		      System.out.println("Popup menu will be invisible!");
		    }

		    public void popupMenuCanceled(PopupMenuEvent e) {
		      System.out.println("Popup menu is hidden!");
		    }
		  }


}
