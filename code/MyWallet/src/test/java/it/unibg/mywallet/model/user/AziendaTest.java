package it.unibg.mywallet.model.user;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import it.unibg.mywallet.database.DatabaseManager;
import it.unibg.mywallet.model.user.impl.Azienda;

public class AziendaTest {
	

	Azienda agency;
	
	@Before
	public void init() {
		agency = DatabaseManager.getInstance().getAzienda(1);
	}
	
	@Test
	public void testAzienda() {

		assertEquals(agency.getId(), 1);
		assertEquals(agency.getBilancio(), 1000, 0);
		assertEquals(agency.getRisparmio(), 100, 0);
	}
}
