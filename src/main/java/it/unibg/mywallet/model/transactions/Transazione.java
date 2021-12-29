package it.unibg.mywallet.model.transactions;

import java.util.Date;

import lombok.AllArgsConstructor;


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
