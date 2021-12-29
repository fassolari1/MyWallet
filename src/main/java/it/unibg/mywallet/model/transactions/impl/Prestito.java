package it.unibg.mywallet.model.transactions.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.unibg.mywallet.model.transactions.Transazione;

public class Prestito extends Transazione {
	
	private Date dataScadenza;

	public Prestito(ResultSet lendings) throws SQLException {
		super(lendings.getInt(1),
				lendings.getInt(2),
				lendings.getDouble(3),
				lendings.getDate(4));
	
		this.dataScadenza = lendings.getDate(5);
	}
	

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return id + " | " + idUtente + " | " + ammontare + " | " + dateFormat.format(dataScadenza)+ " | " + dateFormat.format(dataContabilizzazione);
	
	}

}
