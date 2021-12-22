package it.unibg.mywallet.updater;

import javax.swing.JLabel;
import javax.swing.JList;

import it.unibg.mywallet.database.DatabaseManager;
import it.unibg.mywallet.model.Transazione;
import it.unibg.mywallet.model.Utente;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateManager implements Runnable {

	private Utente utente;
	private JLabel bilancio;
	private JLabel risparmio;
	private JList<Transazione> transazioniRecenti;
	

	@Override
	public void run() {
		bilancio.setText(String.format(".2f% €", utente.getBilancio()));
		risparmio.setText(String.format(".2f% €", utente.getRisparmio()));
		transazioniRecenti.setListData(DatabaseManager.getInstance().getRecentTransaction(utente));
	}
	
	
}
