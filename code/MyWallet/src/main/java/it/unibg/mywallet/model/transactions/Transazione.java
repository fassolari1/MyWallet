package it.unibg.mywallet.model.transactions;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * abstract class Transazione that implements the compareTo method to compare the Transazione ammont 
 */
@Getter
@AllArgsConstructor
public abstract class Transazione implements Comparable<Transazione> {

	protected int id;
	protected int idUtente;
	protected double ammontare;
	protected Date dataCont;

	@Override
	public int compareTo(Transazione other) {
		return Double.compare(other.ammontare, ammontare);
	}
}
