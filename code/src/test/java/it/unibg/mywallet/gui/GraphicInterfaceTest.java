package it.unibg.mywallet.gui;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import it.unibg.mywallet.MyWalletApp;

public class GraphicInterfaceTest {
	
	@Test
	public void testGUI() {
		MyWalletApp app = new MyWalletApp();
		app.clearOldData();
		assertNotNull(app.getFrmMywallet());
		assertNotNull(app.getLoginPanel());
		assertNotNull(app.getUsernameField());
		assertNotNull(app.getPasswordField());
		assertNotNull(app.getLoginButton());
		assertNotNull(app.getRegFormAzienda());
		assertNotNull(app.getTextNomeAzienda());
		assertNotNull(app.getTextPartitaIVA());
		assertNotNull(app.getPassAzienda());
		assertNotNull(app.getSubmitRegAzienda());
		assertNotNull(app.getRegFormPrivato());
		assertNotNull(app.getTextNomePrivato());
		assertNotNull(app.getTextCognome());
		assertNotNull(app.getTextCodFiscale());
		assertNotNull(app.getTextDataNascita());
		assertNotNull(app.getPassPrivato());
		assertNotNull(app.getSubmitRegPriv());
		assertNotNull(app.getTransazionePanel());
		assertNotNull(app.getReceiverText());
		assertNotNull(app.getAmountText());
		assertNotNull(app.getLendingCheckBox());
		assertNotNull(app.getSendBtn());
		assertNotNull(app.getHomePanel());
		assertNotNull(app.getRisparmioText());
		assertNotNull(app.getSavings());
		assertNotNull(app.getBalance());
		assertNotNull(app.getBtnHome());
		assertNotNull(app.getBtnTransazione());
		assertNotNull(app.getBtnRisparmio());
		assertNotNull(app.getTransactionTable());
		assertNotNull(app.getSideBoard());
		assertNotNull(app.getUtente());
		assertNotNull(app.getLogoutButton());
		assertNotNull(app.getRegChoicePanel());
		assertNotNull(app.getRegButton());
		assertNotNull(app.getPrivatoButton());
		assertNotNull(app.getAziendaButton());
		assertNotNull(app);
	}

}
