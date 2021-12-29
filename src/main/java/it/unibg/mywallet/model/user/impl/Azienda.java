package it.unibg.mywallet.model.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.unibg.mywallet.model.user.Utente;
import lombok.Getter;

/**
 * Azienda is a class that extend Utente
 * it have two attributes that characterize the class, ragioneSociale and partita IVA
 */
@Getter
public class Azienda extends Utente {
	
	private String ragioneSociale;
	private String partitaIVA;
	
	public Azienda(ResultSet resultSet) throws SQLException {
		super(resultSet.getInt(1),
				resultSet.getDouble(4),
				resultSet.getDouble(5));
		this.ragioneSociale = resultSet.getString(2);
		this.partitaIVA = resultSet.getString(3);
	}

}
