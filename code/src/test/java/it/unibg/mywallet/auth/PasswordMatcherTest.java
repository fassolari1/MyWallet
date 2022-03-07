package it.unibg.mywallet.auth;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.hash.Hashing;

public class PasswordMatcherTest {
	
	@Test
	public void testMatchPassword() {
		//"mysecretpassword" sha256 hash
		String mySecretPassword = "94aefb8be78b2b7c344d11d1ba8a79ef087eceb19150881f69460b8772753263";
		//compute the hash for "mysecretpassword"
		String mySecretHash = Hashing.sha256().hashString("mysecretpassword", StandardCharsets.UTF_8).toString();
		
		assertEquals(mySecretPassword, mySecretHash);
	}
	
}
