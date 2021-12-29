package it.unibg.mywallet.model.transactions.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.unibg.mywallet.model.transactions.Transazione;

/**
 * Prestito is a class that extend Transazione.
 * dataScadenza is a expiry date for return the money
 */
public class Prestito extends Transazione {
	
	private Date dataScadenza;

	public Prestito(ResultSet lendings) throws SQLException {
		super(lendings.getInt(1),
				lendings.getInt(2),
				lendings.getDouble(3),
				lendings.getDate(4));
	
		this.dataScadenza = lendings.getDate(5);
	}

}
