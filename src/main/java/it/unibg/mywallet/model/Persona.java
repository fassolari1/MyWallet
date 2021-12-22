package it.unibg.mywallet.model;

import java.util.Date;

import lombok.Getter;

@Getter
public class Persona extends Utente {
	
	private String nome;
	private String cognome;
	private char[] codFiscale;
	private Date dataDiNascita;

}
