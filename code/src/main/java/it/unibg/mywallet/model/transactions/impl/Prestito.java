package it.unibg.mywallet.model.transactions.impl;

import java.util.Date;

import it.unibg.mywallet.model.transactions.Transazione;
import lombok.Getter;

/**
 * Prestito is a class that extend Transazione.
 * dataScadenza is a expiry date for return the money
 */
@Getter
public class Prestito extends Transazione {

	private Date dataScadenza;
	
	public Prestito(int id, int idUtente, double ammontare, Date dataContabilizzazione, Date dataScadenza) {
		super(id, idUtente, ammontare, dataContabilizzazione);
		this.dataScadenza = dataScadenza;
	}

}
