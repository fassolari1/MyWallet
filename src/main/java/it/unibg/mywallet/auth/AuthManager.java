package it.unibg.mywallet.auth;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

import it.unibg.mywallet.database.DatabaseManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthManager {
	
	@Getter
	private static AuthManager instance = new AuthManager();
	

	/**
	 * return if the user and password combinations are correct
	 * @param userName the given username.
	 * @param password the given password.
	 * @return true or false.
	 */
	public boolean login(String userName, String password) {
		String hash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
		if(DatabaseManager.getInstance().isAgency(userName)) {
			return DatabaseManager.getInstance().getPasswordAgency(userName).equals(hash);
		}
		if(DatabaseManager.getInstance().isPerson(userName)) {
			return DatabaseManager.getInstance().getPasswordPerson(userName).equals(hash);
		}
		return false;
	}

}
