package it.unibg.mywallet.model.user;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import it.unibg.mywallet.database.DatabaseManager;
import it.unibg.mywallet.model.user.impl.Persona;

public class PersonaTest {
	
	Persona person;
	
	@Before
	public void init() {
		person = DatabaseManager.getInstance().getPerson(0);
	}
	
	@Test
	public void testUtente() {

		assertEquals(person.getId(), 0);
		assertEquals(person.getBilancio(), 1000, 0);
		assertEquals(person.getRisparmio(), 100, 0);
	}

}
