package it.unibg.mywallet.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public abstract class Utente {
	
	protected int id;
	protected double bilancio;
	protected double risparmio;
	
	public boolean inviaPagamento(int amount) {
		if(bilancio - amount >= 0) {
			bilancio -= amount;
			return true;
		}
		return false;	
	}

}
