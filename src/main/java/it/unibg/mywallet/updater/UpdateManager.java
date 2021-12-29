package it.unibg.mywallet.updater;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import it.unibg.mywallet.database.DatabaseManager;
import it.unibg.mywallet.model.user.Utente;
import it.unibg.mywallet.model.user.impl.Azienda;
import it.unibg.mywallet.model.user.impl.Persona;
import lombok.Getter;

/**
<<<<<<< HEAD
 * UpdateManager is used for refersh the application interface and upgrade the data
 * @Getter of all the attributes
 * @running can stop the while circle
 *
 */

=======
 * Update manager is a class which handles the graphic interface by updating its components
 *
 */
>>>>>>> d620e4503189cb7054887d874a9e81dfe870f1bd
public class UpdateManager implements Runnable {
	
	private final Vector<String> columnsIdentifier =  new Vector<String>();
	private final ExecutorService exeService = Executors.newSingleThreadExecutor();
	
	private boolean running = true;
	
	@Getter
	private Utente utente;
	private JLabel nome;
	private JLabel bilancio;
	private JLabel risparmio;
	private DefaultTableModel transazioniRecenti;
	
	public UpdateManager(Utente utente, JLabel nome, JLabel bilancio, JLabel risparmio, DefaultTableModel transazioniRecenti) {
		this.utente = utente;
		this.nome = nome;
		this.bilancio = bilancio;
		this.risparmio = risparmio;
		this.transazioniRecenti = transazioniRecenti;
		
		columnsIdentifier.add("ID");
		columnsIdentifier.add("Somma");
		columnsIdentifier.add("Data Contabilizzazione");
		
		exeService.execute(this);
	}
	
	/**
	 * Stops the thread which updates the graphic
	 */
	public void stop() {
		running = false;
	}
	
	
	/**
	 * Update the name,balance,savings and recentTransactions labels.
	 */
	@Override
	public void run() {
		while(running) {
			if(utente instanceof Persona) {
				Persona persona = (Persona)utente;
				nome.setText(persona.getNome() + " | ID: " + persona.getId());
			}
			if(utente instanceof Azienda) {
				Azienda azienda = (Azienda)utente;
				nome.setText(azienda.getRagioneSociale() + " | ID: " + azienda.getId());
			}
			bilancio.setText(String.format("%,.2f €", utente.getBilancio()));
			risparmio.setText(String.format("%,.2f €", utente.getRisparmio()));
			transazioniRecenti.setDataVector(DatabaseManager.getInstance().getRecentTransaction(utente), columnsIdentifier);
			try {
				//Update every 1 s
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
