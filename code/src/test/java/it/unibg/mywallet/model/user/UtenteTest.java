package it.unibg.mywallet.model.user;

import org.junit.Test;

import it.unibg.mywallet.database.DatabaseManager;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class UtenteTest {
	
	Utente user;
	
	@Before
	public void init() {
		user = DatabaseManager.getInstance().getPerson(0);
	}
	
	@Test
	public void testUtente() {

		assertEquals(user.getId(), 0);
		assertEquals(user.getBilancio(), 1000, 0);
		assertEquals(user.getRisparmio(), 100, 0);
	}
	
	@Test
	public void testPagamento() {
		int amount = 20;
		double originalBalance = user.getBilancio();
		
		user.inviaPagamento(amount);
		
		assertEquals(user.getBilancio(), originalBalance - (amount - (amount * 0.03 )), 0);
	}
	
	@Test
	public void testRisparmio() {
		user.inviaRisparmio(50);
		
		assertEquals(user.getRisparmio(), 150, 0);
	}

}
