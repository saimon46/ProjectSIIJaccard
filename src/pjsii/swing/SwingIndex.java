package pjsii.swing;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import pjsii.jaccard.JaccardSimilarity;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Panel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class SwingIndex {

	private JFrame frame;
	private JTextField textFieldNUsers;
	private JTextField textFieldPercent;
	private JTextArea textArea;
	private EntityManagerFactory emf;
	private EntityManager em;
	private JaccardSimilarity jaccard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingIndex window = new SwingIndex();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingIndex() {
		StartEm();
		initialize();
	}
	
	public void StartEm() {
		emf = Persistence.createEntityManagerFactory("projectSII");
		em = emf.createEntityManager();
		jaccard = new JaccardSimilarity(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 970, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		springLayout.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Index", null, panel, null);
		springLayout.putConstraint(SpringLayout.NORTH, panel, 83, SpringLayout.SOUTH, tabbedPane);
		
		JButton button = new JButton("Avvia");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				tabbedPane.setSelectedIndex(1);
				try {
					jaccard.runAllForSize(em, Integer.parseInt(textFieldNUsers.getText()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JComboBox<String> comboBoxUser1 = new JComboBox<String>();
		JComboBox<String> comboBoxUser2 = new JComboBox<String>();
		
		textFieldNUsers = new JTextField();
		textFieldNUsers.setText("50");
		textFieldNUsers.setColumns(10);
		
		JLabel label = new JLabel("N° utenti:");
		
		JLabel lblAnalisiSimilaritTra = new JLabel("Analisi similarità tra i primi n utenti del database creato da Twitter con parola chiave \"Spotify\", pubblicati nel giro di una settimana");
		lblAnalisiSimilaritTra.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton button_1 = new JButton("Avvia");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				tabbedPane.setSelectedIndex(1);
				try {
					jaccard.runOneUser(em, comboBoxUser1.getSelectedItem().toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button_1.setEnabled(false);
		
		JLabel label_3 = new JLabel("Analisi similarità tra un utente del database e tutti gli altri utenti");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblAnalisiSimilaritTra_1 = new JLabel("Analisi similarità tra un utente del database e tutti gli altri utenti, usando una parte del DB come Training e l'altra come Test");
		lblAnalisiSimilaritTra_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel label_5 = new JLabel("");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldPercent = new JTextField();
		textFieldPercent.setText("50");
		textFieldPercent.setColumns(10);
		
		JLabel label_6 = new JLabel("Percentuale DB Training:");
		
		JButton button_2 = new JButton("Avvia");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				tabbedPane.setSelectedIndex(1);
				try {
					jaccard.runOneUserPercent(em, comboBoxUser2.getSelectedItem().toString(), Integer.parseInt(textFieldPercent.getText()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button_2.setEnabled(false);
		
		JLabel label_7 = new JLabel("Utente");
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> nameUsers = jaccard.getFirstUsers(em);
				
				comboBoxUser1.removeAllItems();
				
				for(String s: nameUsers){
					comboBoxUser1.addItem(s);
				}
				
				button_1.setEnabled(true);
			}
		});
		
		
		
		JButton button_3 = new JButton("Refresh");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> nameUsers = jaccard.getFirstUsers(em);
				
				comboBoxUser2.removeAllItems();
				
				for(String s: nameUsers){
					comboBoxUser2.addItem(s);
				}
				
				button_2.setEnabled(true);
			}
		});
		
		JLabel label_1 = new JLabel("Utente");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_3)
					.addContainerGap(490, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(comboBoxUser2, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(628, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_6)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldPercent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 305, Short.MAX_VALUE)
							.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_7)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBoxUser1, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED, 282, Short.MAX_VALUE)
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGap(314)
							.addComponent(label_5))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(label)
									.addGap(18)
									.addComponent(textFieldNUsers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(407)
									.addComponent(button, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
								.addComponent(lblAnalisiSimilaritTra))))
					.addContainerGap(24, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAnalisiSimilaritTra_1)
					.addContainerGap(67, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAnalisiSimilaritTra)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(button)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(textFieldNUsers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label)))
					.addGap(28)
					.addComponent(label_3)
					.addGap(27)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_7)
						.addComponent(comboBoxUser1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton)
						.addComponent(button_1))
					.addGap(39)
					.addComponent(lblAnalisiSimilaritTra_1)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_3)
						.addComponent(comboBoxUser2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addGap(55)
							.addComponent(label_5))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_6)
								.addComponent(textFieldPercent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(button_2))
							.addContainerGap())))
		);
		panel.setLayout(gl_panel);
		
		Panel panel_1 = new Panel();
		tabbedPane.addTab("Similarità", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		
		textArea.setLineWrap(true);
	    textArea.setEditable(false);
	    textArea.setVisible(true);

	    JScrollPane scroll = new JScrollPane (textArea);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
	    panel_1.add(scroll, BorderLayout.CENTER);
	}
	
	public void printOnTextPane(String s){
		this.textArea.setText(s);
	}
	
	public void printAddOnTextPane(String s){
		String old = this.textArea.getText();
		
		if(!old.equals(""))
			this.textArea.setText(old + "\n" + s);
		else
			this.textArea.setText(s);
	}
}
