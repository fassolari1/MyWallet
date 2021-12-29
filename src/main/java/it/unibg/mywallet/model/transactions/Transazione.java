package it.unibg.mywallet.model.transactions;

import java.util.Date;

import lombok.AllArgsConstructor;

/**
 * abstract class Transazione that implements the compareTo method to compare the Transazione ammont 
 */

@AllArgsConstructor
public abstract class Transazione implements Comparable<Transazione> {

	protected int id;
	protected int idUtente;
	protected double ammontare;
	protected Date dataContabilizzazione;

	@Override
	public int compareTo(Transazione other) {
		return Double.compare(other.ammontare, ammontare);
	}
}
