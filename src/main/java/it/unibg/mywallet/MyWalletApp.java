package it.unibg.mywallet;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MyWalletApp {

	private JFrame frmMywallet;
	private JPasswordField passwordField;
	private JTextField usernameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMywallet = new JFrame();
		frmMywallet.setTitle("MyWallet");
		frmMywallet.setResizable(false);
		frmMywallet.setBounds(100, 100, 802, 527);
		frmMywallet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMywallet.getContentPane().setLayout(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBounds(0, 0, 786, 488);
		frmMywallet.getContentPane().add(loginPanel);
		loginPanel.setLayout(null);
		
		JLabel passIcon = new JLabel("");
		passIcon.setIcon(new ImageIcon(MyWalletApp.class.getResource("/resource/key.png")));
		passIcon.setBounds(299, 193, 40, 53);
		loginPanel.add(passIcon);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(278, 257, 89, 23);
		loginPanel.add(loginButton);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(349, 208, 89, 23);
		loginPanel.add(passwordField);
		
		usernameField = new JTextField();
		usernameField.setBounds(352, 162, 86, 20);
		loginPanel.add(usernameField);
		usernameField.setColumns(10);
		
		JButton regButton = new JButton("Registrati");
		regButton.setBounds(413, 257, 89, 23);
		loginPanel.add(regButton);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(MyWalletApp.class.getResource("/resource/user.png")));
		lblNewLabel.setBounds(302, 150, 40, 53);
		loginPanel.add(lblNewLabel);
	}
}
