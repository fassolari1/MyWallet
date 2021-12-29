package it.unibg.mywallet.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Utente is a user can send money to the other users with an 'id' that identifies like a primary key
 * also it can send a Risparmio to the its account's balance
 */
@Getter
@AllArgsConstructor
public abstract class Utente {
	
	protected int id;
	protected double bilancio;
	protected double risparmio;
	
	public boolean inviaPagamento(double amount) {
		if(bilancio - amount >= 0) {
			bilancio -= amount;
			return true;
		}
		return false;	
	}
	
	public boolean inviaRisparmio(double amount) {
		if(bilancio - amount >= 0) {
			bilancio -= amount;
			risparmio += amount;
			return true;
		}
		return false;
	}

}
