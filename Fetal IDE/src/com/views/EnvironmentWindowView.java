package com.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

public class EnvironmentWindowView extends JFrame {
	private static final long serialVersionUID = 1L;
	RSyntaxTextArea xmlEditor;
	private final String environment = "resources/config/environment.xml";
	
	public void xmlEditor( ) {
		EnvironmentWindowView envEditor = this;
		Container cp = envEditor.getContentPane();
		setLayout(new BorderLayout());

		cp.add(buildEditorPanel(), BorderLayout.CENTER);
		cp.add(buildButtonPanel(), BorderLayout.PAGE_END);
		readXMLFile();
		envEditor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		envEditor.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		envEditor.setSize(scaleScreenSize(screenSize, 60));
		envEditor.setLocationRelativeTo(null);
		envEditor.setVisible(true);
		
	}
	private JPanel buildButtonPanel() {
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 2));
		JButton save = new JButton("Save");
		JButton cancel = new JButton("Cancel");
		
		setSave(save);
		setCancel(cancel);
		buttons.add(save);
		buttons.add(cancel);
		
		return buttons;
	}
	private void readXMLFile() {
		InputStream is;
		BufferedReader buf;
		StringBuilder sb;
		try {
			is = new FileInputStream(environment);
			buf = new BufferedReader(new InputStreamReader(is));
			String line = buf.readLine();
			sb = new StringBuilder();
			while(line != null){
				sb.append(line).append("\n");
				line = buf.readLine();
			}
			xmlEditor.setText(sb.toString());
			buf.close();
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private RTextScrollPane  buildEditorPanel() {
		xmlEditor = new RSyntaxTextArea();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/xml", "org.fife.ui.rsyntaxtextarea.modes.FetalJFlexTokenMaker");
		xmlEditor.setSyntaxEditingStyle("text/xml");

		try {
			Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml"));
			theme.apply(xmlEditor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		RTextScrollPane sp = new RTextScrollPane(xmlEditor);
		sp.setLineNumbersEnabled(true);

		return sp;
	}

	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 70);
		d.width *= (percent / 100);
		
		return d;
	}

	private void setSave(JButton save) {
		save.addActionListener(new ActionListener() {
			FileOutputStream fop;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					long timestamp = System.currentTimeMillis();
					File file = new File(environment);
					fop = new FileOutputStream(file);
					byte[] contentInBytes = xmlEditor.getText().getBytes();
					if (!file.exists()) {
						file.createNewFile();
					}
					fop.write(contentInBytes);
					file.setLastModified(timestamp);

				} catch (IOException e1) {
					e1.printStackTrace();
				}finally {
					try {
						fop.flush();
						fop.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					dispose();
				}
			}});
	}
	
	private void setCancel(JButton cancel) {
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}});
	}

}
