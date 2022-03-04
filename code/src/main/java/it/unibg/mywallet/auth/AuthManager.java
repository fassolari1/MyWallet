package it.unibg.mywallet.auth;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

import it.unibg.mywallet.database.DatabaseManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Authentication Manager class
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthManager {
	
	@Getter
	private static AuthManager instance = new AuthManager();
	
	/**
	 * return if the user and password combinations are correct and which account type it is.
	 * @param userName the given username.
	 * @param password the given password.
	 * @return {@link AuthResult}
	 */
	public AuthResult login(int userId, String password) {
		String hash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
		if(DatabaseManager.getInstance().isAgency(userId)) {
			if(DatabaseManager.getInstance().getPasswordAgency(userId).equals(hash)) {
				return AuthResult.AZIENDA;
				
			}else {
				return AuthResult.DATI_INVALIDI;
			}
		}
		if(DatabaseManager.getInstance().isPerson(userId)) {
			if (DatabaseManager.getInstance().getPasswordPerson(userId).equals(hash)) {
				return AuthResult.PRIVATO;
			}else {
				return AuthResult.DATI_INVALIDI;
			}
		}
		return AuthResult.DATI_INVALIDI;
	}

}
