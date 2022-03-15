package it.unibg.mywallet.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import com.google.common.hash.Hashing;

import it.unibg.mywallet.model.user.Utente;
import it.unibg.mywallet.model.user.impl.Azienda;
import it.unibg.mywallet.model.user.impl.Persona;
import lombok.Getter;


/*Alla creazione di un'instanza dell'oggetto, verrà creata una connessione ad database col nome che viene passato come attirbuto. Se il database con quel nome esiste
nel percorso specificato, allora crea solamente una connessione, altrimenti se non esiste ne crea uno nuovo.
Alla creazione di un database venegono automaticamente create le tabelle PERSONA(), AZIENDA(), CREDENZIALI(), PAGAMENTO(), PRESTITO(), CASHBACK(), RISPARMIO()
Gli attributi delle tabelle sono:
PERSONA(ID int NOT NULL PRIMARY KEY, nome VARCHAR(30), cognome VARCHAR(30), codice_fiscale VARCHAR(16), data_nascita DATE, bilancio int)
AZIENDA(ID int NOT NULL PRIMARY KEY, ragione_sociale VARCHAR(30), partita_iva(11), bilancio int)
CREDENZIALE(ID_utente int NOT NULL PRIMARY KEY, password VARCHAR(256))
PAGAMENTO(ID PRIMARY KEY, ID_utente int NOT NULL, ammontare DOUBLE, data_contabilizzazione DATE)
PRESTITO(ID PRIMARY KEY, ID_utente int NOT NULL , ammontare DOUBLE, data_contabilizzazione DATE, data_ultima_scadenza DATE)
CASHBACK(ID_utente int NOT NULL PRIMARY KEY, percentuale int)
RISPARMIO(ID_utente int NOT NULL PRIMARY KEY, totale int)
*/
public class DatabaseManager {

	@Getter
	private static DatabaseManager instance = new DatabaseManager("myWalletDB", "");
	
	//ATTRIBUTI 
	@Getter
	private Connection conn = null;
	private String nomeDatabase;
	private String databasePath;
	
	//COSTRUTTORE
	private DatabaseManager(String nome, String path) {
		this.nomeDatabase = nome;
		this.databasePath = path;
		try { 
			//connessione
			//Guarda se il database esiste già, in quel caso si limita a creare una connessione, altrimenti crea anche le tabelle
			File db = new File(this.databasePath + nomeDatabase+ ".db");
			if(db.exists()) {
				Class.forName("org.sqlite.JDBC");
				this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.databasePath+this.nomeDatabase+".db");
				System.out.println("conn" + this.conn);
				System.out.println("Connessione al database avvenuta con successo...");
				System.out.println("database già esistente");
			}else {
				Class.forName("org.sqlite.JDBC");
				this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.databasePath+this.nomeDatabase+".db");
				//creazione tabelle
				Statement stmt = this.conn.createStatement();
				String create_persona = "CREATE TABLE PERSONA(ID int NOT NULL PRIMARY KEY, nome VARCHAR(30), cognome VARCHAR(30), codice_fiscale VARCHAR(16), data_nascita DATE, risparmio DOUBLE DEFAULT 0.0, bilancio DOUBLE DEFAULT 0.0)";
				String create_azienda = "CREATE TABLE AZIENDA(ID int NOT NULL PRIMARY KEY, ragione_sociale VARCHAR(30), partita_iva VARCHAR(11), risparmio DOUBLE DEFAULT 0.0, bilancio DOUBLE DEFAULT 0.0)";
				String create_credenziali = "CREATE TABLE CREDENZIALE(ID_utente int NOT NULL PRIMARY KEY, password VARCHAR(256))";
				String create_pagamento = "CREATE TABLE PAGAMENTO(ID int PRIMARY KEY, ID_utente int NOT NULL, ammontare DOUBLE, data_contabilizzazione DATE)";
				String create_prestito = "CREATE TABLE PRESTITO(ID int PRIMARY KEY, ID_utente int NOT NULL, ammontare DOUBLE, data_contabilizzazione DATE, data_ultima_scadenza DATE)";
				String create_cashback = "CREATE TABLE CASHBACK(ID_utente int NOT NULL PRIMARY KEY, percentuale int)";
				String create_risparmio = "CREATE TABLE RISPARMIO(ID_utente int NOT NULL PRIMARY KEY, totale int)";
				stmt.execute(create_persona);
				stmt.execute(create_azienda);
				stmt.execute(create_credenziali);
				stmt.execute(create_pagamento);
				stmt.execute(create_prestito);
				stmt.execute(create_cashback);
				stmt.execute(create_risparmio);
			}
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	


	/**
	 * saves a person object to the database
	 * @param nome the name of the person.
	 * @param cognome surname of the person.
	 * @param cod_fisc SSN of the person.
	 * @param date date of birth of the person
	 * @return an integer representing the id associated with the account.
	 */
	public int aggiungiPersona(String nome, String cognome, String cod_fisc, java.util.Date date) { // da sostituire data con un oggetto di tipo Date
		Statement stmt = null;
		int new_id;
		ArrayList<Integer> ids = new ArrayList<>();
		try(PreparedStatement insertPerson = conn.prepareStatement("INSERT INTO PERSONA(ID, nome, cognome, codice_fiscale, data_nascita, bilancio) VALUES(?,?,?,?,?,?)")) {
			stmt = this.conn.createStatement();
			ResultSet rs_azienda = stmt.executeQuery("SELECT ID FROM AZIENDA");
			while (rs_azienda.next()) ids.add(Integer.valueOf(rs_azienda.getInt("ID")));
			ResultSet rs_persona = stmt.executeQuery("SELECT ID FROM PERSONA");
			while (rs_persona.next()) ids.add(Integer.valueOf(rs_persona.getInt("ID")));
			if(ids.isEmpty()) {
				new_id = 0;
			}else {
				new_id = Collections.max(ids).intValue()+1;
			}
			insertPerson.setInt(1, new_id);
			insertPerson.setString(2, nome);
			insertPerson.setString(3, cognome);
			insertPerson.setString(4, cod_fisc);
			insertPerson.setDate(5, new java.sql.Date(date.getTime()));
			insertPerson.setInt(6, 0);
			
			insertPerson.executeUpdate();
			
			System.out.println("Persona aggiunta, id: "+new_id);
			return new_id;
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
			return 0;
		}
	}
	
	/**
	 * saves an agency to the database.
	 * @param ragione_sociale the agency name.
	 * @param p_iva the agency EIN.
	 * @return an integer representing the account id.
	 */
	public int aggiungiAzienda(String ragione_sociale, String p_iva) {
		int new_id;
		ArrayList<Integer> ids = new ArrayList<>();
		String sql_select_azienda = "SELECT ID FROM AZIENDA";
		String sql_select_persona = "SELECT ID FROM PERSONA";
		
		Statement stmt = null;
		try {
			stmt = this.conn.createStatement();
			ResultSet rs_azienda = stmt.executeQuery(sql_select_azienda);
			while (rs_azienda.next()) ids.add(Integer.valueOf(rs_azienda.getInt("ID")));
			ResultSet rs_persona = stmt.executeQuery(sql_select_persona);
			while (rs_persona.next()) ids.add(Integer.valueOf(rs_persona.getInt("ID")));
			if(ids.isEmpty()) {
				new_id = 0;
			}else {
			new_id = Collections.max(ids).intValue()+1;
			}
			String sql = "INSERT INTO AZIENDA(ID, ragione_sociale, partita_iva, bilancio) VALUES('"+new_id+"','"+ragione_sociale+"','"+p_iva+"',0)";
			stmt.execute(sql);
			System.out.println("Azienda aggiunta, id: "+new_id);
			return new_id;
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
			return 0;
		}
	}
	
	/**
	 * saves to the database the credentials for a user.
	 * @param id_utente the given user id.
	 * @param password the user's password.
	 */
	public void aggiungiCredenziali(int id_utente, String password) { // da sostituire poi "int id_utente" con "Utente u"
		String passHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
		String sql = "INSERT INTO CREDENZIALE(ID_utente, password) VALUES('"+id_utente+"','"+passHash+"')";
		Statement stmt = null;
		try {
			stmt = this.conn.createStatement();
			stmt.execute(sql);
			System.out.println("Credenziale aggiunta");
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	
	/**
	 * saves a payment log to the database.
	 * @param id the given user's id.
	 * @param amount the given amount.
	 * @param date the payment date.
	 */
	public void aggiungiPagamento(int id, double amount, Date date) {
		

		int new_id;
		ArrayList<Integer> ids = new ArrayList<>();
		String sql_select_pagamento = "SELECT ID FROM PAGAMENTO";
		String sql_select_prestito = "SELECT ID FROM PRESTITO";
		String sql_update_bilancio = null;
		
		if(this.isPerson(id)) sql_update_bilancio = "UPDATE PERSONA SET bilancio = bilancio+"+amount+" WHERE ID = "+id+"";
		if(this.isAgency(id)) sql_update_bilancio = "UPDATE AZIENDA SET bilancio = bilancio+"+amount+" WHERE ID = "+id+"";

		Statement stmt = null;
		
		try(PreparedStatement insertPayment = this.conn.prepareStatement("INSERT INTO PAGAMENTO(ID, ID_utente, ammontare, data_contabilizzazione) VALUES(?,?,?,?)")) {

			stmt = this.conn.createStatement();
			ResultSet rs_azienda = stmt.executeQuery(sql_select_pagamento);
			while (rs_azienda.next()) ids.add(Integer.valueOf(rs_azienda.getInt("ID")));
			ResultSet rs_persona = stmt.executeQuery(sql_select_prestito);
			while (rs_persona.next()) ids.add(Integer.valueOf(rs_persona.getInt("ID")));
			if(ids.isEmpty()) {
				new_id = 0;
			}else {
			new_id = Collections.max(ids).intValue()+1;
			}

			insertPayment.setInt(1, new_id);
			insertPayment.setInt(2, id);
			insertPayment.setDouble(3, amount);
			insertPayment.setDate(4, date);
			insertPayment.executeUpdate();
			
			Statement another = this.conn.createStatement();
			another.execute(sql_update_bilancio);
		} catch (SQLException ex) {
			System.err.println("Transazione fallita per l'utente con ID: " + id);
			ex.printStackTrace();
		}

	}
	
	/**
	 * saves a lending to the database.
	 * @param id the given user's id.
	 * @param amount the given amount.
	 * @param date the lending's date.
	 */
	public void aggiungiPrestito(int id, double amount, Date date) { //da sostituire poi "int id_utente" con "Utente u" // da sostituire data con un oggetto di tipo Date
		int new_id;
		ArrayList<Integer> ids = new ArrayList<>();
		String sql_select_pagamento = "SELECT ID FROM PAGAMENTO";
		String sql_select_prestito = "SELECT ID FROM PRESTITO";
		String sql_update_bilancio = null;
		if(this.isPerson(id)) sql_update_bilancio = "UPDATE PERSONA SET bilancio = bilancio+"+amount+" WHERE ID = "+id+"";
		if(this.isAgency(id)) sql_update_bilancio = "UPDATE AZIENDA SET bilancio = bilancio+"+amount+" WHERE ID = "+id+"";

		Statement stmt = null;
		
		try(PreparedStatement insertPayment = this.conn.prepareStatement("INSERT INTO PRESTITO(ID, ID_utente, ammontare, data_contabilizzazione) VALUES(?,?,?,?)")) {

			stmt = this.conn.createStatement();
			ResultSet rs_azienda = stmt.executeQuery(sql_select_pagamento);
			while (rs_azienda.next()) ids.add(Integer.valueOf(rs_azienda.getInt("ID")));
			ResultSet rs_persona = stmt.executeQuery(sql_select_prestito);
			while (rs_persona.next()) ids.add(Integer.valueOf(rs_persona.getInt("ID")));
			if(ids.isEmpty()) {
				new_id = 0;
			}else {
			new_id = Collections.max(ids).intValue()+1;
			}

			insertPayment.setInt(1, new_id);
			insertPayment.setInt(2, id);
			insertPayment.setDouble(3, amount);
			insertPayment.setDate(4, date);
			insertPayment.executeUpdate();
			
			Statement another = this.conn.createStatement();
			another.execute(sql_update_bilancio);
		} catch (SQLException ex) {
			System.err.println("Prestito fallita per l'utente con ID: " + id);
			ex.printStackTrace();
		}

		
	}
	
	/**
	 * Closes all the connections with the database.
	 */
	public void terminaConnessione() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	
	/**
	 * returns the Persona object associated with the given id.
	 * @param nome the given id.
	 * @return {@link Persona}
	 */
	public Persona getPerson(int id) {
		try(PreparedStatement queryPerson = this.conn.prepareStatement("SELECT * FROM PERSONA WHERE ID = ?")) {
			queryPerson.setInt(1, id);
			ResultSet resultSet = queryPerson.executeQuery();
			return new Persona(resultSet);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * returns the Azienda object associated with the given id.
	 * @param nome the given id.
	 * @return {@link Azienda}
	 */
	public Azienda getAzienda(int id) {
		try(PreparedStatement queryAzienda = this.conn.prepareStatement("SELECT * FROM AZIENDA WHERE ID = ?")) {
			queryAzienda.setInt(1, id);
			ResultSet resultSet = queryAzienda.executeQuery();
			return new Azienda(resultSet);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * returns the password hash associated with a person username.
	 * @param nome the username.
	 * @return the password hash.
	 */
	public String getPasswordPerson(int id) {
		try(PreparedStatement queryPass = this.conn.prepareStatement("SELECT password FROM CREDENZIALE as C JOIN PERSONA as P on P.ID = C.ID_utente WHERE ID = ?")) {
			queryPass.setInt(1, id);
			ResultSet resultSet = queryPass.executeQuery();
			return resultSet.getString(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + id);
			return "";
		}
	}
	
	/**
	 * returns the password hash associated with an agency username.
	 * @param nome the username.
	 * @return the password hash.
	 */
	public String getPasswordAgency(int id) {
		try(PreparedStatement queryPass = this.conn.prepareStatement("SELECT password FROM CREDENZIALE as C JOIN AZIENDA as A on A.ID = C.ID_utente WHERE ID = ?")) {
			queryPass.setInt(1, id);
			ResultSet resultSet = queryPass.executeQuery();
			return resultSet.getString(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + id);
			return "";
		}
	}
	
	/**
	 * returns true if there is a person associated with the given id.
	 * @param name the id.
	 * @return true or false.
	 */
	public boolean isPerson(int id) {
		try(PreparedStatement queryUser = this.conn.prepareStatement("SELECT EXISTS(SELECT * FROM PERSONA WHERE ID = ?)")) {
			queryUser.setInt(1, id);
			ResultSet resultSet = queryUser.executeQuery();
			return resultSet.getBoolean(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + id);
			return false;
		}
	}
	
	/**
	 * returns true if there is an ageny associated with the given id.
	 * @param name the id.
	 * @return true or false.
	 */
	public boolean isAgency(int id) {
		//Use try with resource to release the connection after query to avoid memory leak
		try(PreparedStatement queryUser = this.conn.prepareStatement("SELECT EXISTS(SELECT * FROM AZIENDA WHERE ID = ?)")) {
			//assign to placeholder with index 1 value the name
			queryUser.setInt(1, id);
			//get ResultSet from our query
			ResultSet resultSet = queryUser.executeQuery();
			//return result with index 1
			return resultSet.getBoolean(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + id);
			return false;
		}
	}


	/**
	 * returns all the top ten recent transactions for a given user.
	 * @param utente the given user.
	 * @return a "Table"(that will be used inside a JTable) of strings representing the recent transactions.
	 */
	public Vector<Vector<String>> getRecentTransaction(Utente utente) {

		Vector<Vector<String>> transactions = new Vector<>();
		Vector<String> transactionData = new Vector<>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try(PreparedStatement queryLending = this.conn.prepareStatement("SELECT * FROM PRESTITO WHERE ID_Utente = ? ORDER BY data_contabilizzazione DESC LIMIT 10");
			PreparedStatement queryPayment = this.conn.prepareStatement("SELECT * FROM PAGAMENTO WHERE ID_Utente = ? ORDER BY data_contabilizzazione DESC LIMIT 10")) {
			
			queryLending.setInt(1, utente.getId());
			queryPayment.setInt(1, utente.getId());
			
			ResultSet payments = queryPayment.executeQuery();
			ResultSet lendings = queryLending.executeQuery();
		
			while(payments.next()) {
				transactionData.add(String.valueOf(payments.getInt(1)));
				transactionData.add(String.format("%,.2f €", payments.getDouble(3)));
				transactionData.add(dateFormat.format(payments.getDate(4)));
				transactions.add((Vector<String>) transactionData.clone());
				transactionData.clear();
			}
			
			while(lendings.next()) {
				transactionData.add(String.valueOf(lendings.getInt(1)));//Here
				transactionData.add(String.format("%,.2f €", lendings.getDouble(3)));
				transactionData.add(dateFormat.format(lendings.getDate(4)));
				transactions.add((Vector<String>) transactionData.clone());
				transactionData.clear();
			}
			
			return transactions;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return transactions;
		}
	}
}
