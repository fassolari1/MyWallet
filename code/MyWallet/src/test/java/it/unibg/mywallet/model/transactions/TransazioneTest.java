package it.unibg.mywallet.model.transactions;

import static org.junit.Assert.assertEquals;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

import it.unibg.mywallet.model.transactions.impl.Pagamento;
import it.unibg.mywallet.model.transactions.impl.Prestito;

public class TransazioneTest {
	
	
	@Test
	public void testCompare() {
		Date now = new Date();
		Transazione payment = new Pagamento(0, 0, 100.0, now);
		Transazione bigPayment = new Pagamento(1, 0, 10000.0, now);
		
		assertEquals(payment.compareTo(bigPayment), 1);
	}
	
	@Test
	public void testPagamento() {
		Date now = new Date();
		Pagamento payment = new Pagamento(0, 0, 100.0, now);
		
		assertEquals(payment.getId(), 0);
		assertEquals(payment.getIdUtente(), 0);
		assertEquals(payment.getAmmontare(), 100.0, 0);
		assertEquals(payment.getDataCont(), now);
	}
	
	@Test
	public void testPrestito() {
		Date now = new Date();
		//Il prestito scade dopo 1 mese
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		
		Prestito payment = new Prestito(0, 0, 100.0, now, calendar.getTime());
		
		assertEquals(payment.getId(), 0);
		assertEquals(payment.getIdUtente(), 0);
		assertEquals(payment.getAmmontare(), 100.0, 0);
		assertEquals(payment.getDataCont(), now);
		assertEquals(payment.getDataScadenza(), calendar.getTime());
	}
	

}
