package it.unibg.mywallet.database;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import it.unibg.mywallet.model.user.impl.Azienda;
import it.unibg.mywallet.model.user.impl.Persona;

public class InsertUpdateTest {
	
	@Before
	public void setupDatabase() {
		try {
			DatabaseManager.getInstance().getConn().setAutoCommit(false);
		} catch (SQLException ex) {
			System.err.println("Cannot disable autocommit");
			ex.printStackTrace();
		}
	}
	
	@AfterClass
	public static void restoreDatabase() {
		try {
			DatabaseManager.getInstance().getConn().rollback();
			DatabaseManager.getInstance().getConn().setAutoCommit(true);;
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.err.println("Cannot restore database");
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testAggiungiPersona() {
		int id = DatabaseManager.getInstance().aggiungiPersona("pippo", "pierino", "ppprn", new Date());
		
		Persona pippo = DatabaseManager.getInstance().getPerson(id);
		
		assertEquals(pippo.getNome(), "pippo");
		assertEquals(pippo.getCognome(), "pierino");
		assertEquals(String.valueOf(pippo.getCodFiscale()), "ppprn");
		
	}
	
	@Test
	public void testAggiungiAzienda() {
		int id = DatabaseManager.getInstance().aggiungiAzienda("MyWalletSPA", "1999777");
		
		Azienda myWallet = DatabaseManager.getInstance().getAzienda(id);
		
		assertEquals(myWallet.getRagioneSociale(), "MyWalletSPA");
		assertEquals(myWallet.getPartitaIVA(), "1999777");
	}
}
