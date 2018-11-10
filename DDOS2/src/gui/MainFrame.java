package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import ddos.DDOS;
import ddos.Logger;

public final class MainFrame extends JFrame {
	private final JPanel contentPane;
	
	private HintTextField textField;
	
	private JButton start;
	private JButton checkConnection;
	private JButton stop_ddos;
	
	public void enableButtons()
	{
		start.setEnabled(true);
		checkConnection.setEnabled(true);
	}
	public void disable_stop_btn()
	{
		stop_ddos.setEnabled(false);
	}
	
	private static boolean already_aborting = false;
	
	public MainFrame() {
		try{UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");}catch(Exception ex) {JOptionPane.showMessageDialog(null, "Your system does not support windows look and feel!", "Error", JOptionPane.ERROR_MESSAGE);}
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		
		setTitle("DDoS by .flaz");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 610, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new HintTextField("google.com");
		textField.setFont(new Font("Consolas", Font.PLAIN, 16));
		textField.setBounds(326, 11, 258, 20);
		textField.setColumns(1);
		
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(textField);
		
		JTextField ports_field = new JTextField();
		ports_field.setText("80");
		ports_field.setHorizontalAlignment(SwingConstants.CENTER);
		ports_field.setFont(new Font("Consolas", Font.PLAIN, 16));
		ports_field.setColumns(1);
		ports_field.setBounds(527, 42, 57, 20);
		contentPane.add(ports_field);
		
		start = new JButton("Start");
		start.setBorder(emptyBorder);
		start.setBounds(354, 284, 187, 53);
		contentPane.add(start);
		
		checkConnection = new JButton("Check connection");
		checkConnection.setBounds(354, 347, 230, 53);
		contentPane.add(checkConnection);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 306, 389);
		contentPane.add(scrollPane);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

		     BoundedRangeModel brm = scrollPane.getVerticalScrollBar().getModel();
		     boolean wasAtBottom = true;

		     public void adjustmentValueChanged(AdjustmentEvent e) {
		        if (!brm.getValueIsAdjusting()) {
		           if (wasAtBottom)
		              brm.setValue(brm.getMaximum());
		        } else
		           wasAtBottom = ((brm.getValue() + brm.getExtent()) >= brm.getMaximum() - ((brm.getValue() + brm.getExtent()) / 50));

		     }
		  });   
		
		JTextPane log_pane = new JTextPane();
		scrollPane.setViewportView(log_pane);
		log_pane.setEditable(false);
		log_pane.setFont(new Font("Consolas", Font.PLAIN, 14));
		
		
		
		JButton cls = new JButton("x");
		cls.setToolTipText("Clear  log");
		cls.setForeground(Color.RED);
		cls.setBorder(new EmptyBorder(0, 0, 0, 0));
		cls.setFont(new Font("Consolas", Font.BOLD, 23));
		cls.setBounds(316, 375, 25, 25);
		cls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log_pane.setText(">");
			}
		});
		contentPane.add(cls);
		
		stop_ddos = new JButton("Stop");
		stop_ddos.setEnabled(false);
		stop_ddos.setBounds(540, 284, 44, 53);
		stop_ddos.setBorder(emptyBorder);
		stop_ddos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(current_ddos != null) {
					if(already_aborting) {
						System.out.println("Force stop triggered!");
						current_ddos.force_end();
					}
					
					already_aborting = true;
					
					current_ddos.end();
					current_ddos = null;

					already_aborting = false;
					
					start.setEnabled(true);
					checkConnection.setEnabled(true);
				}
			}
		});
		contentPane.add(stop_ddos);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(32767), new Integer(1), null, new Integer(1)));
		spinner.setValue(32767);
		spinner.setBounds(327, 123, 64, 20);
		contentPane.add(spinner);
		
		JLabel junksize_label = new JLabel("<html>Junk request size,\r\n 8096-32767 recommended<html\\>");
		junksize_label.setFont(new Font("Consolas", Font.PLAIN, 12));
		junksize_label.setBounds(401, 117, 192, 31);
		contentPane.add(junksize_label);
		
		JSpinner client_amt_spinner = new JSpinner();
		client_amt_spinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		client_amt_spinner.setBounds(327, 92, 64, 20);
		contentPane.add(client_amt_spinner);
		
		JLabel client_amt_hint = new JLabel("<html>Amount of client threads to be created<html\\>");
		client_amt_hint.setFont(new Font("Consolas", Font.PLAIN, 12));
		client_amt_hint.setBounds(401, 85, 187, 31);
		contentPane.add(client_amt_hint);
		
		JSpinner timeout_spinner = new JSpinner();
		timeout_spinner.setModel(new SpinnerNumberModel(2500, 200, 10000, 100));
		timeout_spinner.setBounds(326, 154, 65, 20);
		contentPane.add(timeout_spinner);
		
		JLabel timeout_hint = new JLabel("<html>Connection timeout delay<html\\>");
		timeout_hint.setFont(new Font("Consolas", Font.PLAIN, 12));
		timeout_hint.setBounds(401, 153, 192, 20);
		contentPane.add(timeout_hint);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(326, 72, 258, 2);
		contentPane.add(separator);
		
		JSpinner query_delay_spinner = new JSpinner();
		query_delay_spinner.setEnabled(false);
		query_delay_spinner.setModel(new SpinnerNumberModel(0, 0, 10000, 1));
		query_delay_spinner.setBounds(326, 185, 65, 20);
		contentPane.add(query_delay_spinner);
		
		JComboBox<String> query_delay_selector = new JComboBox<>();
		query_delay_selector.setModel(new DefaultComboBoxModel<String>(new String[] {"Max (no delay)", "Normal (1 ms delay)", "Low (10 ms delay)", "Custom"}));
		query_delay_selector.setBounds(401, 184, 183, 20);
		query_delay_selector.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getItem().equals("Custom"))
				{
					query_delay_spinner.setEnabled(true);
				}
				else
				{
					query_delay_spinner.setEnabled(false);
				}
			}
		});
		contentPane.add(query_delay_selector);
		
		JLabel cpu_load_hint = new JLabel("<html>Delay between queries (CPU load)<html\\>");
		cpu_load_hint.setFont(new Font("Consolas", Font.PLAIN, 12));
		cpu_load_hint.setBounds(401, 204, 183, 30);
		contentPane.add(cpu_load_hint);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(326, 271, 258, 2);
		contentPane.add(separator_1);
		
		JCheckBox random_junk_bool = new JCheckBox("Randomize junk content?");
		random_junk_bool.setBounds(322, 241, 146, 23);
		contentPane.add(random_junk_bool);
		
		JCheckBox pentagon_joke = new JCheckBox("Hack The Pentagon?");
		pentagon_joke.setEnabled(false);
		pentagon_joke.setBounds(465, 241, 123, 23);
		contentPane.add(pentagon_joke);
		
		Logger.init(log_pane.getDocument());
		
		Logger.get.log_str("DDOS app by .flaz aka 0x666", Logger.MSG.INFO);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				checkConnection.setEnabled(false);
				
				InfoPackage pk = new InfoPackage(true, false,
												 true, true,
												 false, textField.getText(),
												 ports_field.getText(), (Integer)client_amt_spinner.getValue(),
												 (Integer)spinner.getValue(), (Integer)timeout_spinner.getValue(),
												 (String)query_delay_selector.getSelectedItem(), (Integer)query_delay_spinner.getValue(), random_junk_bool.isSelected());
				current_ddos = new DDOS();
				current_ddos.begin(pk);
				stop_ddos.setEnabled(true);
			}
		});
		
		getRootPane().setDefaultButton(start);
		
		checkConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				checkConnection.setEnabled(false);
				
				InfoPackage pk = new InfoPackage(true, false,
												 true, false,
												 true, textField.getText(),
												 ports_field.getText(), (Integer)client_amt_spinner.getValue(),
												 (Integer)spinner.getValue(), (Integer)timeout_spinner.getValue(),
												 (String)query_delay_selector.getSelectedItem(), (Integer)query_delay_spinner.getValue(), random_junk_bool.isSelected());
				current_ddos = new DDOS();
				current_ddos.begin(pk);
			}
		});
	}
	
	private static DDOS current_ddos = null;
	
	private class HintTextField extends JTextField implements FocusListener
	{
		private String hint;
		private boolean isShowingHint;
		
		private Color prevForeground;
		
		public HintTextField(String hint)
		{
			super(hint);
			this.hint = hint;
			this.isShowingHint = true;
			super.addFocusListener(this);
			prevForeground = super.getForeground();
			super.setForeground(Color.LIGHT_GRAY);
		}

		public void focusGained(FocusEvent e) {
			if(this.getText().isEmpty()) {
				super.setText("");
				super.setForeground(prevForeground);
				isShowingHint = false;
			}
		}

		public void focusLost(FocusEvent e) {
			 if(this.getText().isEmpty()) {
				 super.setText(hint);
				 prevForeground = super.getForeground();
				 super.setForeground(Color.LIGHT_GRAY);
			     isShowingHint = true;
			 }
		}
		
		@Override
		public String getText() {
			return isShowingHint ? "" : super.getText();
		}
	}
	
	/*
	Max (no delay)
	Normal (1 ms delay)
	Low (10 ms delay)
	Custom
	*/
	
	public static enum DELAY_MODE
	{
		LOW_DELAY(0),
		MEDIUM_DELAY(1),
		HIGH_DELAY(10),
		CUSTOM_DELAY(10000);
		
		public int delay;
		
		private DELAY_MODE(int val) {
			this.delay = val;
		}
		
		public static DELAY_MODE findByString(String val)
		{
				 if("Max (no delay)".equals(val))
					return LOW_DELAY;
			else if("Normal (1 ms delay)".equals(val))
					return MEDIUM_DELAY;
			else if("Low (10 ms delay)".equals(val))
					return HIGH_DELAY;
			else if("Custom".equals(val))
					return CUSTOM_DELAY;
				 
			return CUSTOM_DELAY;
		}
	}
}
