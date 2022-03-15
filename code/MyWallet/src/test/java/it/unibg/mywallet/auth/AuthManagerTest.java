package it.unibg.mywallet.auth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AuthManagerTest {
	
	@Test
	public void testAuthManager() {
		AuthManager auth = AuthManager.getInstance();
		AuthResult wrongPasswordUser = auth.login(0, "passwordScorretta");
		AuthResult wrongPasswordAgency = auth.login(1, "passwordScorretta");
		AuthResult person = auth.login(0, "ciao123");
		AuthResult agency = auth.login(1, "123ciao");
		AuthResult notAnUser = auth.login(-1, "123ciao");
		
		assertEquals(wrongPasswordUser, AuthResult.DATI_INVALIDI);
		assertEquals(wrongPasswordAgency, AuthResult.DATI_INVALIDI);
		assertEquals(notAnUser, AuthResult.DATI_INVALIDI);
		assertEquals(agency, AuthResult.AZIENDA);
		assertEquals(person, AuthResult.PRIVATO);
	}

}
