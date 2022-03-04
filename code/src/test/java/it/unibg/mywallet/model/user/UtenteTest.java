package it.unibg.mywallet.model.user;

import org.junit.Test;

import it.unibg.mywallet.model.user.impl.Persona;

import static org.junit.Assert.assertEquals;

public class UtenteTest {
	
	
	@Test
	public void testUtente() {
		Utente person = new Persona(1,1000,100);
		Utente emptyPerson = new Persona(2);

		assertEquals(person.getId(), 1);
		assertEquals(person.getBilancio(), 1000, 0);
		assertEquals(person.getRisparmio(), 100, 0);
		
		assertEquals(emptyPerson.getBilancio(), 0.0, 0);
		assertEquals(emptyPerson.getRisparmio(), 0.0, 0);
	}

}
