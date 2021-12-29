package it.unibg.mywallet.model.transactions.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.unibg.mywallet.model.transactions.Transazione;

/**
 * Pagamento is a class that extend Transazione 
 */
public class Pagamento extends Transazione {
	
	public Pagamento(ResultSet payments) throws SQLException {
		super(payments.getInt(1),
				payments.getInt(2),
				payments.getDouble(3),
				payments.getDate(4));
	}

}
