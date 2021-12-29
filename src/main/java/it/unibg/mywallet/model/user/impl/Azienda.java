package it.unibg.mywallet.model.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.unibg.mywallet.model.user.Utente;
import lombok.Getter;

@Getter
public class Azienda extends Utente {
	
	private String ragioneSociale;
	private String partitaIVA;
	
	public Azienda(ResultSet resultSet) throws SQLException {
		super(resultSet.getInt(1),
				resultSet.getDouble(5),
				resultSet.getDouble(4));
		this.ragioneSociale = resultSet.getString(2);
		this.partitaIVA = resultSet.getString(3);
	}

}
