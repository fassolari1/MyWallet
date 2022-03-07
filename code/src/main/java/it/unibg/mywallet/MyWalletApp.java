package it.unibg.mywallet;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatDarkLaf;

import it.unibg.mywallet.auth.AuthManager;
import it.unibg.mywallet.database.DatabaseManager;
import it.unibg.mywallet.model.user.Utente;
import it.unibg.mywallet.updater.UpdateManager;
import lombok.Getter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

@Getter
public class MyWalletApp {
	
	private UpdateManager updateManager;
	
	private Set<JPanel> panels = new HashSet<>();
	
	private JFrame frmMywallet;
	//Login
	private JPanel loginPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	//RegFormAzienda
	private JPanel regFormAzienda;
	private JTextField textNomeAzienda;
	private JTextField textPartitaIVA;
	private JPasswordField passAzienda;
	private JButton submitRegAzienda;
	//RegFormPrivato
	private JPanel regFormPrivato;
	private JTextField textNomePrivato;
	private JTextField textCognome;
	private JTextField textCodFiscale;
	private JTextField textDataNascita;
	private JPasswordField passPrivato;
	private JButton submitRegPriv;
	//TransactionForm
	private JPanel transazionePanel;
	private JTextField receiverText;
	private JTextField amountText;
	private JCheckBox lendingCheckBox;
	private JButton sendBtn;
	//HomePanel
	private JPanel homePanel;
	private JTextField risparmioText;
	private JLabel savings;
	private JLabel balance;
	private JButton btnHome;
	private JButton btnTransazione;
	private JButton btnRisparmio;
	private JTable transactionTable;
	//SideBoard
	private JPanel sideBoard;
	private JLabel utente;
	private JButton logoutButton;
	//RegChoiceForm
	private JPanel regChoicePanel;
	private JButton regButton;
	private JButton privatoButton;
	private JButton aziendaButton;

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
	 * Clears all the input data from the previous session
	 */
	public void clearOldData() {
		if(updateManager != null) {
			updateManager.stop();
		}
		updateManager = null;
		//Login
		usernameField.setText(null);
		passwordField.setText(null);
		//Azienda
		textNomeAzienda.setText(null);
		textPartitaIVA.setText(null);
		passAzienda.setText(null);
		//Privato
		textNomePrivato.setText(null);
		textCognome.setText(null);
		textCodFiscale.setText(null);
		textDataNascita.setText(null);
		passPrivato.setText(null);
		//Transazione
		lendingCheckBox.setSelected(false);
		receiverText.setText(null);
		amountText.setText(null);
		//Home
		risparmioText.setText(null);
		((DefaultTableModel)transactionTable.getModel()).setRowCount(0);
	}

	/**
	 * Here we init all our listeners
	 */
	private void initListeners() {
		DatabaseManager db = DatabaseManager.getInstance();

		/*
		 * SideBoard buttons
		 * 
		 */
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard, homePanel);
			}
		});
		

		btnTransazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard, transazionePanel);
			}
		});
		

	    logoutButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		clearOldData();
				hideAllExcept(loginPanel);
	    	}
	    });
		
		/*
		 * Auth buttons
		 */
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					switch(AuthManager.getInstance().login(Integer.parseInt(usernameField.getText()), String.valueOf(passwordField.getPassword()))) {
				
					case PRIVATO:
						loginPanel.setEnabled(false);
						loginPanel.setVisible(false);
						hideAllExcept(sideBoard,homePanel);
						updateManager = new UpdateManager(db.getPerson(Integer.parseInt(usernameField.getText())), utente, balance, savings, (DefaultTableModel)transactionTable.getModel());
						break;
					case AZIENDA:
						loginPanel.setEnabled(false);
						loginPanel.setVisible(false);
						hideAllExcept(sideBoard,homePanel);
						updateManager = new UpdateManager(db.getAzienda(Integer.parseInt(usernameField.getText())), utente, balance, savings, (DefaultTableModel)transactionTable.getModel());
						break;
					default:
						JOptionPane.showMessageDialog(frmMywallet, "Utente o password errati!", "Login error",JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
				catch(NumberFormatException ex) {

				    JOptionPane.showMessageDialog(frmMywallet, "L'utente deve essere un id numerico!", "Login error",JOptionPane.ERROR_MESSAGE);
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
		

		submitRegAzienda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard,homePanel);
				int idAzienda = db.aggiungiAzienda(textNomeAzienda.getText(), textPartitaIVA.getText());
				db.aggiungiCredenziali(idAzienda,	String.valueOf(passAzienda.getPassword()));
				updateManager = new UpdateManager(db.getAzienda(idAzienda), utente, balance, savings, (DefaultTableModel)transactionTable.getModel());
			}
		});
		

		submitRegPriv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideAllExcept(sideBoard,homePanel);
				try {
					int idPersona = db.aggiungiPersona(textNomePrivato.getText(), textCognome.getText(), textCodFiscale.getText(), new SimpleDateFormat("dd/MM/yyyy").parse(textDataNascita.getText()));
					db.aggiungiCredenziali(idPersona,String.valueOf(passPrivato.getPassword()));
					

					updateManager = new UpdateManager(db.getPerson(idPersona), utente, balance, savings, (DefaultTableModel)transactionTable.getModel());
				} catch (ParseException ex) {
					// TODO Auto-generated catch block
				    JOptionPane.showMessageDialog(frmMywallet, "La data di nascita inserita non è nel formato corretto!", "Registration error",JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		

		/*
		 * Transaction buttons
		 */
		
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				double amount = Double.valueOf(amountText.getText().replace(",", "."));
				
				if(amount <= 0) {
				    JOptionPane.showMessageDialog(frmMywallet, "La somma da inviare deve essere maggiore di 0.00 €", "Transaction error",JOptionPane.ERROR_MESSAGE);
				} else {

				Utente sender = updateManager.getUtente();
				if(lendingCheckBox.isSelected()) {
					//Si tratta di un prestito quindi no cashback
					if(sender.inviaPagamento(amount)) {
						db.aggiungiPrestito(Integer.valueOf(receiverText.getText()), amount, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
						db.aggiungiPrestito(sender.getId(), -amount, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
					} else {
					    JOptionPane.showMessageDialog(frmMywallet, "Saldo insufficiente!", "Transaction error",JOptionPane.ERROR_MESSAGE);
					}
				}else {
					//Non si tratta di un prestito
					if(!sender.inviaPagamento(amount)) {
						//Invia un pagamento detraendo il 3% di cashback
					    JOptionPane.showMessageDialog(frmMywallet, "Saldo insufficiente!", "Transaction error",JOptionPane.ERROR_MESSAGE);
					    return;
					}
					db.aggiungiPagamento(Integer.valueOf(receiverText.getText()), amount, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
					db.aggiungiPagamento(sender.getId(), -amount + amount * 0.03, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				}
			    }
			}
		});
		

		btnRisparmio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double amount = Double.valueOf(risparmioText.getText().replace(",", "."));
				
				if(amount <= 0) {
				    JOptionPane.showMessageDialog(frmMywallet, "La somma da inviare deve essere maggiore di 0.00 €", "Transaction error",JOptionPane.ERROR_MESSAGE);
				} else {
					
				Utente sender = updateManager.getUtente();
				if(!sender.inviaRisparmio(amount)) {
				    JOptionPane.showMessageDialog(frmMywallet, "Saldo insufficiente!", "Transaction error",JOptionPane.ERROR_MESSAGE);
					}
				}
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
						utente.setBounds(12, 86, 137, 23);
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
						
					    logoutButton = new JButton("Logout");
						logoutButton.setBackground(null);
						logoutButton.setBorderPainted(false);
						logoutButton.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/logout32.png")));
						logoutButton.setBounds(10, 446, 127, 31);
						sideBoard.add(logoutButton);
		
				
				
				homePanel = new JPanel();
				homePanel.setBounds(161, 0, 627, 488);
				homePanel.setLayout(null);
				frmMywallet.getContentPane().add(homePanel);
				panels.add(homePanel);
				
				JLabel balanceLabel = new JLabel("Bilancio");
				balanceLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/coin32.png")));
				balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
				balanceLabel.setBounds(10, 35, 103, 32);
				homePanel.add(balanceLabel);
				
				JLabel savingsLabel = new JLabel("Totale Risparmi");
				savingsLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/savings32.png")));
				savingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
				savingsLabel.setBounds(186, 35, 156, 32);
				homePanel.add(savingsLabel);
				
		JLabel transactionLabel = new JLabel("Transazioni recenti");
		transactionLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/clock32.png")));
		transactionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		transactionLabel.setBounds(10, 274, 192, 32);
		homePanel.add(transactionLabel);
		
		balance = new JLabel("0.0\u20AC");
		balance.setHorizontalAlignment(SwingConstants.CENTER);
		balance.setBounds(20, 78, 77, 32);
		homePanel.add(balance);
		
		savings = new JLabel("0.0\u20AC");
		savings.setHorizontalAlignment(SwingConstants.CENTER);
		savings.setBounds(196, 78, 77, 32);
		homePanel.add(savings);
		
		transactionTable = new JTable();
		transactionTable.setShowVerticalLines(false);
		transactionTable.setShowGrid(false);
		transactionTable.setShowHorizontalLines(false);
		transactionTable.setRowSelectionAllowed(false);
		transactionTable.getTableHeader().setReorderingAllowed(false);
		transactionTable.setModel(new DefaultTableModel() {
			private static final long serialVersionUID = 7415589682770089601L;

			//Non-Editable Jtable
			@Override
			   public boolean isCellEditable(int row, int column) {
		       return false;
		   }
			
			
		});
		transactionTable.setBounds(10, 317, 607, 160);
		
		JScrollPane scrollPane = new JScrollPane(transactionTable);
		scrollPane.setBackground(null);
		scrollPane.setForeground(null);
		scrollPane.setSize(607, 160);
		scrollPane.setLocation(10, 317);
		homePanel.add(scrollPane);
		
		risparmioText = new JTextField();
		risparmioText.setHorizontalAlignment(SwingConstants.CENTER);
		risparmioText.setBounds(432, 84, 86, 20);
		homePanel.add(risparmioText);
		risparmioText.setColumns(10);
		
		btnRisparmio = new JButton("");
		btnRisparmio.setBackground(null);
		btnRisparmio.setBorderPainted(false);
		btnRisparmio.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/send32.png")));
		btnRisparmio.setBounds(521, 78, 77, 32);
		homePanel.add(btnRisparmio);
		
		JLabel labelRisparmio = new JLabel("Invia Denaro al Conto Risparmio");
		labelRisparmio.setIcon(new ImageIcon(MyWalletApp.class.getResource("/pictures/wealth32.png")));
		labelRisparmio.setBounds(390, 31, 208, 41);
		homePanel.add(labelRisparmio);
		
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
		sendBtn.setBounds(198, 283, 124, 23);
		transazionePanel.add(sendBtn);
		
		JLabel labelRicevente = new JLabel("Ricevente:");
		labelRicevente.setHorizontalAlignment(SwingConstants.CENTER);
		labelRicevente.setBounds(145, 162, 81, 17);
		transazionePanel.add(labelRicevente);
		
		JLabel labelAmmontare = new JLabel("Ammontare:");
		labelAmmontare.setHorizontalAlignment(SwingConstants.CENTER);
		labelAmmontare.setBounds(145, 205, 81, 17);
		transazionePanel.add(labelAmmontare);
		
		lendingCheckBox = new JCheckBox("Prestito");
		lendingCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		lendingCheckBox.setBounds(236, 242, 86, 23);
		transazionePanel.add(lendingCheckBox);
		
		
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
		
		submitRegPriv = new JButton("Invia Registrazione");
		submitRegPriv.setBounds(332, 232, 177, 30);
		regFormPrivato.add(submitRegPriv);
		
		textNomePrivato = new JTextField();
		textNomePrivato.setBounds(386, 46, 123, 20);
		regFormPrivato.add(textNomePrivato);
		textNomePrivato.setColumns(10);
		
		textCognome = new JTextField();
		textCognome.setBounds(386, 76, 123, 20);
		regFormPrivato.add(textCognome);
		textCognome.setColumns(10);
		
		textCodFiscale = new JTextField();
		textCodFiscale.setBounds(386, 107, 123, 20);
		regFormPrivato.add(textCodFiscale);
		textCodFiscale.setColumns(10);
		
		textDataNascita = new JFormattedTextField(dateMask);
		textDataNascita.setColumns(10);
		textDataNascita.setBounds(386, 138, 123, 20);
		regFormPrivato.add(textDataNascita);
		
		JLabel nomeLabel = new JLabel("Nome:");
		nomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nomeLabel.setBounds(315, 48, 86, 17);
		regFormPrivato.add(nomeLabel);
		
		JLabel cognomeLabel = new JLabel("Cognome:");
		cognomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cognomeLabel.setBounds(304, 76, 86, 20);
		regFormPrivato.add(cognomeLabel);
		
		JLabel codFiscaleLabel = new JLabel("Codice Fiscale:");
		codFiscaleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		codFiscaleLabel.setBounds(297, 109, 79, 17);
		regFormPrivato.add(codFiscaleLabel);
		
		JLabel dataNascitaLabel = new JLabel("Data di Nascita:");
		dataNascitaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dataNascitaLabel.setBounds(290, 140, 86, 17);
		regFormPrivato.add(dataNascitaLabel);
		
		JLabel passPrivatoLabel = new JLabel("Password:");
		passPrivatoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passPrivatoLabel.setBounds(297, 171, 90, 17);
		regFormPrivato.add(passPrivatoLabel);
		
		passPrivato = new JPasswordField();
		passPrivato.setBounds(386, 169, 123, 20);
		regFormPrivato.add(passPrivato);
		

		
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
		
		submitRegAzienda = new JButton("Invia Registrazione");
		submitRegAzienda.setBounds(292, 309, 177, 30);
		regFormAzienda.add(submitRegAzienda);
		
		JLabel labelNome = new JLabel("Nome Societ\u00E0:");
		labelNome.setHorizontalAlignment(SwingConstants.CENTER);
		labelNome.setBounds(217, 177, 95, 14);
		regFormAzienda.add(labelNome);
		
		JLabel labelPIVA = new JLabel("Partita IVA:");
		labelPIVA.setHorizontalAlignment(SwingConstants.CENTER);
		labelPIVA.setBounds(217, 220, 95, 14);
		regFormAzienda.add(labelPIVA);
		
		JLabel passAziendaLabel = new JLabel("Password:");
		passAziendaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passAziendaLabel.setBounds(217, 260, 95, 20);
		regFormAzienda.add(passAziendaLabel);
		
		passAzienda = new JPasswordField();
		passAzienda.setBounds(336, 260, 161, 20);
		regFormAzienda.add(passAzienda);
	}


	/**
	 * Hide all panels except the given ones.
	 * @param panelsToShow the panels to show.
	 */
	private void hideAllExcept(JPanel... panelsToShow) {
		panels.forEach(pan -> pan.setEnabled(false));
		panels.forEach(pan -> pan.setVisible(false));
		for(JPanel panel : panelsToShow) {
			panel.setEnabled(true);
			panel.setVisible(true);
		}
	}
}
