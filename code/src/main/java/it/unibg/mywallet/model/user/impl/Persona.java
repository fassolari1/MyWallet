package it.unibg.mywallet.model.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import it.unibg.mywallet.model.user.Utente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Azienda is a class that extend Utente
 * it have many attributes like nome, cognome, codFiscale, dataDiNascita
 * that characterize the all common user
 */
@Getter
public class Persona extends Utente {
	
	private String nome;
	private String cognome;
	private char[] codFiscale;
	private Date dataDiNascita;
	
	public Persona(int id, double bilancio, double risparmio) {
		super(id, bilancio, risparmio);
	}
	
	public Persona(int id) {
		super(id, 0, 0);
	}
	
	public Persona(ResultSet resultSet) throws SQLException {
		super(resultSet.getInt(1),
				resultSet.getDouble(7),
				resultSet.getDouble(6));
		this.nome = resultSet.getString(2);
		this.cognome = resultSet.getString(3);
		this.codFiscale = resultSet.getString(4).toCharArray();
		this.dataDiNascita = resultSet.getDate(5);
	}
}
