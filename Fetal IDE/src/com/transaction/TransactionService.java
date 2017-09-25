package com.transaction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import com.ftl.derived.FetalLexer;
import com.ftl.derived.FetalParser;
import com.ftl.derived.FetalParser.TransactionContext;
import com.ftl.helper.BailErrorStrategy;
import com.ftl.helper.FetalErrorListener;
import com.ftl.helper.FetalTransaction;


public class TransactionService extends FetalTransaction {

	@Override
	public void beginTrans() {
		System.out.println("begin");
	}

	@Override
	public void commitTrans() {
		if (getErrorCount() > 0 ) {
			rollback();
		}else {
			System.out.println("end");
		}

	}

	@Override
	public void rollback() {
		System.out.println("rollback");

	}

	@Override
	public void credit(Double amount, String account) {

		System.out.printf("credit( %.02f , %s )%n", amount, account);

	}

	@Override
	public void debit(Double amount, String account) {

		System.out.printf("debit( %.02f , %s )%n", amount, account);

	}

	@Override
	public void ledger(char type, Double amount, String account, String description) {

		System.out.printf("ledger( %C, %.02f, %s, %s )%n", type, amount, account, description);

	}

	@Override
	public double getBalance(String account) {

		System.out.printf("getBalance( %s )%n", account );
		return 0;
	}

	@Override
	public Object lookup(String sql, Object... args) {

		System.out.printf("lookup( " +sql+" )%n", args);
		return null;
	}

	@Override
	public void update(String sql, Object... args) {
		
		System.out.printf("update( " + sql + " )%n", args);

	}

	@Override
	public Set<Object> list(String sql, Object... args) {

		System.out.printf("list( " + sql + " )%n", args);
		return null;
	}
	
	public void showGuiTree(FetalParser parser, TransactionContext tree ) {
        JFrame frame = new JFrame("Fetal Tree");
        frame.setAlwaysOnTop(true);
        
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 100, 50);
        JButton button = new JButton("Close");
        JPanel sliderPanel = new JPanel();
 
        frame.getContentPane().setLayout(new BorderLayout());
        sliderPanel.setLayout(new  BoxLayout(sliderPanel, BoxLayout.LINE_AXIS));

        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        
       slideAction(slider, viewr);
       buttonAction(button, frame);
       JScrollPane scrollPane = new JScrollPane(viewr);
       scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        viewr.setScale(1);//scale a little
        sliderPanel.add(slider);
        sliderPanel.add(button);
        
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(sliderPanel, BorderLayout.PAGE_END);
       
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(scaleScreenSize(screenSize, 60));
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);

	}
	private Dimension scaleScreenSize(Dimension d, double percent) {

		d.height *= (percent / 100);
		d.width *= (percent / 100);
		
		return d;
	}
	
	public void slideAction(JSlider slider, TreeViewer viewr) {
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				double value;
				value = Double.valueOf(String.valueOf(slider.getValue())) / 50.0;
				viewr.setScale(value);
				viewr.repaint();
			}});
	}
	
	public void buttonAction(JButton button, JFrame frame) {

		button.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}});
	}
	
	@Override
	public void loadRule(String rule) throws IOException, RecognitionException, RuntimeException {
		
		ANTLRInputStream in = new ANTLRInputStream(rule);
		FetalLexer lexer = new FetalLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FetalParser parser = new FetalParser(tokens);
		setfParser(parser);

		parser.removeErrorListeners(); // remove ConsoleErrorListener
		parser.addErrorListener(new FetalErrorListener()); // add ours
		if (isDebugMode() == false) {
			parser.setErrorHandler(new BailErrorStrategy());
		}
		try {
			 setTransCtx(parser.transaction(this));
			
		} catch (RuntimeException e) {
			if (getErrMsg().length() == 0) {
				setErrMsg(e.toString());
				
			}
			throw new RuntimeException(getErrMsg());

		}
	}

	@Override
	public void commitStock(Set<?> items) {
		System.out.printf("Commiting %d items %n",items.size());
	}

	@Override
	public void depleteStock(Set<?> items) {
		System.out.printf("Depleting %d items %n",items.size());		
	}

	@Override
	public void addStock(String sku, Long qty) {
		System.out.printf("addStock( %s, %d ) %n", sku, qty);
	}


}
