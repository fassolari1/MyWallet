package it.unibg.mywallet.updater;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.unibg.mywallet.database.DatabaseManager;

public class UpdateManagerTest {
	
	private UpdateManager updater;
	
	@Before
	public void init() {
		updater = new UpdateManager(DatabaseManager.getInstance().getPerson(0), null, null, null, null);
	}
	
	@Test
	public void testUpdater() {
		assertTrue(updater.isRunning());
		updater.stop();
		assertFalse(updater.isRunning());	
	}

}
