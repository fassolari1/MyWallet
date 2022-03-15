package it.unibg.mywallet.model.transactions.impl;

import java.util.Date;

import it.unibg.mywallet.model.transactions.Transazione;

/**
 * Pagamento is a class that extend Transazione 
 */
public class Pagamento extends Transazione {

	public Pagamento(int id, int idUtente, double ammontare, Date dataContabilizzazione) {
		super(id, idUtente, ammontare, dataContabilizzazione);
	}
	
}
