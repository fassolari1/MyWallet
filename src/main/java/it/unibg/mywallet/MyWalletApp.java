package it.unibg.mywallet;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatDarkLaf;

import it.unibg.mywallet.auth.AuthManager;
import it.unibg.mywallet.database.DatabaseManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.JList;

public class MyWalletApp {

	private Set<JPanel> panels = new HashSet<JPanel>();
	
	private JFrame frmMywallet;
	//Panels
	private JPanel loginPanel;
	private JPanel sideBoard;
	private JPanel homePanel;
	private JPanel transazionePanel;
	private JPanel regChoicePanel;
	private JPanel regFormPrivato;
	private JPanel regFormAzienda;
	//Text Fields
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField textNomeAzienda;
	private JTextField textPartitaIVA;
	private JPasswordField passAzienda;
	private JTextField textNomePrivato;
	private JTextField textCognome;
	private JTextField textCodFiscale;
	private JTextField receiverText;
	private JTextField amountText;
	//Buttons
	private JButton btnHome;
	private JButton btnTransazione;
	private JButton sendBtn;
	private JButton loginButton;
	private JButton regButton;
	private JButton privatoButton;
	private JButton aziendaButton;
	private JButton submitRegisterAzienda;
	private JButton submitRegisterPrivato;
	private JTextField textDataNascita;
	//Labels
	private JLabel savings;
	private JLabel balance;
	private JLabel utente;
	private JLabel passAziendaLabel;
	//List
	private JList transactionList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//Re-style
				FlatDarkLaf.setup();
				try {
					MyWalletApp window = new MyWalletApp();
					window.frmMywallet.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MyWalletApp() {
		initialize();
		initListeners();
		hideAllExcept(loginPanel);
	}

	/**
	 * Here we init all our listeners
	 */
	private void initListeners() {
		

		/*
		 * SideBoard buttons
		 * 
		 */
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard,homePanel);
			}
		});
		

		btnTransazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard, transazionePanel);
			}
		});
		
		/*
		 * 
		 */
		
		
		/*
		 * Auth buttons
		 */
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(AuthManager.getInstance().login(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
					//we good send the user to main dashboard
					hideAllExcept(sideBoard,homePanel);
					//createUser();
				}else {
					//wrong pass/user send popup
					hideAllExcept(sideBoard,homePanel);
				    //JOptionPane.showMessageDialog(frmMywallet, "Utente o password errati!", "Login error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		regButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(regChoicePanel);
			}
		});
		

		aziendaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(regFormAzienda);
			}
		});
		

		privatoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(regFormPrivato);
			}
		});
		

		submitRegisterAzienda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard,homePanel);
				int idAzienda = DatabaseManager.getInstance().aggiungiAzienda(textNomeAzienda.getText(), textPartitaIVA.getText());
				DatabaseManager.getInstance().aggiungiCredenziali(idAzienda, String.valueOf(passAzienda.getPassword()));
			}
		});
		

		submitRegisterPrivato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard,homePanel);
			}
		});
		
		/*
		 * 
		 */
		

		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatabaseManager.getInstance().aggiungiPagamento(Integer.valueOf(receiverText.getText()), Integer.valueOf(amountText.getText()), new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				
			}
		});
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMywallet = new JFrame();
		frmMywallet.setIconImage(Toolkit.getDefaultToolkit().getImage(MyWalletApp.class.getResource("/pictures/mywalletLogo.png")));
		frmMywallet.setTitle("MyWallet");
		frmMywallet.setResizable(false);
		frmMywallet.setBounds(100, 100, 802, 527);
		frmMywallet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMywallet.getContentPane().setLayout(null);
		
		MaskFormatter dateMask = null;
		try {
		    dateMask = new MaskFormatter("##/##/####");
		    dateMask.setPlaceholder("01/01/1970");
		    dateMask.setValidCharacters("0123456789");
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		sideBoard = new JPanel();
		sideBoard.setBounds(0, 0, 157, 488);
		sideBoard.setLayout(null);
		frmMywallet.getContentPane().add(sideBoard);
		panels.add(sideBoard);
		
				JLabel utenteLabel = new JLabel("Utente");
				utenteLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/user32.png")));
				utenteLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
				utenteLabel.setHorizontalAlignment(SwingConstants.CENTER);
				utenteLabel.setBounds(20, 22, 107, 66);
				sideBoard.add(utenteLabel);
				
				utente = new JLabel("Pincopallino");
				utente.setHorizontalAlignment(SwingConstants.CENTER);
				utente.setBounds(10, 87, 137, 23);
				sideBoard.add(utente);
				
				btnTransazione = new JButton("Transazione");
				btnTransazione.setBackground(null);
				btnTransazione.setBorderPainted(false);
				btnTransazione.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/hand32.png")));
				btnTransazione.setBounds(0, 203, 157, 58);
				sideBoard.add(btnTransazione);
				
				btnHome = new JButton("DashBoard");
				btnHome.setBackground(null);
				btnHome.setBorderPainted(false);
				btnHome.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/home32.png")));
				btnHome.setBounds(0, 121, 157, 66);
				sideBoard.add(btnHome);
		
		transazionePanel = new JPanel();
		transazionePanel.setBounds(161, 0, 627, 488);
		transazionePanel.setLayout(null);
		frmMywallet.getContentPane().add(transazionePanel);
		panels.add(transazionePanel);
		
		receiverText = new JTextField();
		receiverText.setBounds(236, 162, 86, 20);
		transazionePanel.add(receiverText);
		receiverText.setColumns(10);
		
		amountText = new JTextField();
		amountText.setBounds(236, 205, 86, 20);
		transazionePanel.add(amountText);
		amountText.setColumns(10);
		
		sendBtn = new JButton("Invia Denaro");
		sendBtn.setBounds(198, 259, 124, 23);
		transazionePanel.add(sendBtn);
		
		JLabel labelRicevente = new JLabel("Ricevente:");
		labelRicevente.setHorizontalAlignment(SwingConstants.CENTER);
		labelRicevente.setBounds(145, 162, 81, 17);
		transazionePanel.add(labelRicevente);
		
		JLabel labelAmmontare = new JLabel("Ammontare:");
		labelAmmontare.setHorizontalAlignment(SwingConstants.CENTER);
		labelAmmontare.setBounds(145, 205, 81, 17);
		transazionePanel.add(labelAmmontare);

		
		
		homePanel = new JPanel();
		homePanel.setBounds(161, 0, 627, 488);
		homePanel.setLayout(null);
		frmMywallet.getContentPane().add(homePanel);
		panels.add(homePanel);
		
		JLabel balanceLabel = new JLabel("Bilancio");
		balanceLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/coin32.png")));
		balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		balanceLabel.setBounds(60, 35, 103, 32);
		homePanel.add(balanceLabel);
		
		JLabel savingsLabel = new JLabel("Totale Risparmi");
		savingsLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/savings32.png")));
		savingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		savingsLabel.setBounds(431, 35, 156, 32);
		homePanel.add(savingsLabel);
		

		
		regFormAzienda = new JPanel();
		panels.add(regFormAzienda);
		regFormAzienda.setBounds(0, 0, 786, 488);
		frmMywallet.getContentPane().add(regFormAzienda);
		regFormAzienda.setLayout(null);
		
		textNomeAzienda = new JTextField();
		textNomeAzienda.setBounds(336, 174, 161, 20);
		regFormAzienda.add(textNomeAzienda);
		textNomeAzienda.setColumns(10);
		
		textPartitaIVA = new JTextField();
		textPartitaIVA.setBounds(336, 217, 161, 20);
		regFormAzienda.add(textPartitaIVA);
		textPartitaIVA.setColumns(10);
		
		submitRegisterAzienda = new JButton("Invia Registrazione");
		submitRegisterAzienda.setBounds(292, 309, 177, 30);
		regFormAzienda.add(submitRegisterAzienda);
		
		JLabel labelNome = new JLabel("Nome Societ\u00E0:");
		labelNome.setHorizontalAlignment(SwingConstants.CENTER);
		labelNome.setBounds(217, 177, 95, 14);
		regFormAzienda.add(labelNome);
		
		JLabel labelPIVA = new JLabel("Partita IVA:");
		labelPIVA.setHorizontalAlignment(SwingConstants.CENTER);
		labelPIVA.setBounds(217, 220, 95, 14);
		regFormAzienda.add(labelPIVA);
		
		passAziendaLabel = new JLabel("Password:");
		passAziendaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passAziendaLabel.setBounds(217, 260, 95, 20);
		regFormAzienda.add(passAziendaLabel);
		
		passAzienda = new JPasswordField();
		passAzienda.setBounds(336, 260, 161, 20);
		regFormAzienda.add(passAzienda);
				
		JLabel transactionLabel = new JLabel("Transazioni recenti");
		transactionLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/clock32.png")));
		transactionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		transactionLabel.setBounds(60, 277, 192, 32);
		homePanel.add(transactionLabel);
		
		balance = new JLabel("0.0\u20AC");
		balance.setHorizontalAlignment(SwingConstants.CENTER);
		balance.setBounds(70, 78, 77, 32);
		homePanel.add(balance);
		
		savings = new JLabel("0.0\u20AC");
		savings.setHorizontalAlignment(SwingConstants.CENTER);
		savings.setBounds(458, 78, 77, 32);
		homePanel.add(savings);
		
		transactionList = new JList();
		transactionList.setBounds(10, 319, 607, 158);
		homePanel.add(transactionList);
		
		
		loginPanel = new JPanel();
		panels.add(loginPanel);
		loginPanel.setBounds(0, 0, 786, 488);
		frmMywallet.getContentPane().add(loginPanel);
		loginPanel.setLayout(null);
		
		JLabel passIcon = new JLabel("");
		passIcon.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/key.png")));
		passIcon.setBounds(289, 265, 40, 53);
		loginPanel.add(passIcon);
		
		loginButton = new JButton("Login");
		loginButton.setBounds(268, 329, 89, 23);
		loginPanel.add(loginButton);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(339, 280, 89, 23);
		loginPanel.add(passwordField);
		
		usernameField = new JTextField();
		usernameField.setBounds(342, 234, 86, 20);
		loginPanel.add(usernameField);
		usernameField.setColumns(10);
		
		regButton = new JButton("Registrati");
		regButton.setBounds(403, 329, 89, 23);
		loginPanel.add(regButton);
		
		JLabel userIcon = new JLabel("");
		userIcon.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/user32.png")));
		userIcon.setBounds(289, 216, 40, 53);
		loginPanel.add(userIcon);
		
		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/mywalletLogo.png")));
		logo.setBounds(268, 11, 231, 197);
		loginPanel.add(logo);
		
		regChoicePanel = new JPanel();
		panels.add(regChoicePanel);
		regChoicePanel.setBounds(0, 0, 786, 488);
		frmMywallet.getContentPane().add(regChoicePanel);
		regChoicePanel.setLayout(null);
		
		JLabel choiceLabel = new JLabel("Intendi registrarti come Azienda o Utente privato?");
		choiceLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		choiceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		choiceLabel.setBounds(173, 135, 394, 96);
		regChoicePanel.add(choiceLabel);
		
		privatoButton = new JButton("");
		privatoButton.setBackground(null);
		privatoButton.setBorderPainted(false);
		privatoButton.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/user64.png")));
		privatoButton.setBounds(216, 242, 89, 70);
		regChoicePanel.add(privatoButton);
		
		aziendaButton = new JButton("");
		aziendaButton.setBackground(null);
		aziendaButton.setBorderPainted(false);
		aziendaButton.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/azienda.png")));
		aziendaButton.setBounds(427, 242, 89, 70);
		regChoicePanel.add(aziendaButton);
		
		regFormPrivato = new JPanel();
		panels.add(regFormPrivato);
		regFormPrivato.setBounds(0, 0, 786, 488);
		frmMywallet.getContentPane().add(regFormPrivato);
		regFormPrivato.setLayout(null);
		
		submitRegisterPrivato = new JButton("Invia Registrazione");
		submitRegisterPrivato.setBounds(297, 238, 177, 30);
		regFormPrivato.add(submitRegisterPrivato);
		
		textNomePrivato = new JTextField();
		textNomePrivato.setBounds(225, 109, 123, 20);
		regFormPrivato.add(textNomePrivato);
		textNomePrivato.setColumns(10);
		
		textCognome = new JTextField();
		textCognome.setBounds(225, 140, 123, 20);
		regFormPrivato.add(textCognome);
		textCognome.setColumns(10);
		
		textCodFiscale = new JTextField();
		textCodFiscale.setBounds(472, 109, 123, 20);
		regFormPrivato.add(textCodFiscale);
		textCodFiscale.setColumns(10);
		
		textDataNascita = new JFormattedTextField(dateMask);
		textDataNascita.setColumns(10);
		textDataNascita.setBounds(472, 140, 123, 20);
		regFormPrivato.add(textDataNascita);
		
		JLabel nomeLabel = new JLabel("Nome:");
		nomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nomeLabel.setBounds(144, 111, 86, 17);
		regFormPrivato.add(nomeLabel);
		
		JLabel cognomeLabel = new JLabel("Cognome:");
		cognomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cognomeLabel.setBounds(144, 140, 86, 20);
		regFormPrivato.add(cognomeLabel);
		
		JLabel codFiscaleLabel = new JLabel("Codice Fiscale:");
		codFiscaleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		codFiscaleLabel.setBounds(383, 111, 79, 17);
		regFormPrivato.add(codFiscaleLabel);
		
		JLabel dataNascitaLabel = new JLabel("Data di Nascita:");
		dataNascitaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dataNascitaLabel.setBounds(376, 142, 86, 17);
		regFormPrivato.add(dataNascitaLabel);
	}


	private void hideAllExcept(JPanel... panelsToShow) {
		panels.forEach(pan -> pan.setEnabled(false));
		panels.forEach(pan -> pan.setVisible(false));
		for(JPanel panel : panelsToShow) {
			panel.setEnabled(true);
			panel.setVisible(true);
		}
	}
}
