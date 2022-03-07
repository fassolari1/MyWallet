package it.unibg.mywallet.database;

import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import it.unibg.mywallet.model.user.Utente;

public class RecentTransactionsTest {
	
	Utente user;
	
	@Before
	public void init() {
		user = DatabaseManager.getInstance().getPerson(0);
	}
	
	@Test
	public void testTransactions() {
		Vector<Vector<String>> transactions = DatabaseManager.getInstance().getRecentTransaction(user);
		
		assertTrue(transactions.size() > 0);
	}

}
