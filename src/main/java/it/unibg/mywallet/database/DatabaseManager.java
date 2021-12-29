package it.unibg.mywallet.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;

import it.unibg.mywallet.model.transactions.Transazione;
import it.unibg.mywallet.model.transactions.impl.Pagamento;
import it.unibg.mywallet.model.transactions.impl.Prestito;
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
			if(!db.exists()) {
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
			}else {
				Class.forName("org.sqlite.JDBC");
				this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.databasePath+this.nomeDatabase+".db");
				System.out.println("conn" + this.conn);
				System.out.println("Connessione al database avvenuta con successo...");
				System.out.println("database già esistente");
			}
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	

	//METODI
	
	public int aggiungiPersona(String nome, String cognome, String cod_fisc, java.util.Date date) { // da sostituire data con un oggetto di tipo Date
		Statement stmt = null;
		int new_id;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		try(PreparedStatement insertPerson = conn.prepareStatement("INSERT INTO PERSONA(ID, nome, cognome, codice_fiscale, data_nascita) VALUES(?,?,?,?,?)")) {
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
			
			insertPerson.executeUpdate();
			
			System.out.println("Persona aggiunta, id: "+new_id);
			return new_id;
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
			return 0;
		}
	}
	
	public int aggiungiAzienda(String ragione_sociale, String p_iva) {
		int new_id;
		ArrayList<Integer> ids = new ArrayList<Integer>();
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
			String sql = "INSERT INTO AZIENDA(ID, ragione_sociale, partita_iva) VALUES('"+new_id+"','"+ragione_sociale+"','"+p_iva+"')";
			stmt.execute(sql);
			System.out.println("Azienda aggiunta, id: "+new_id);
			return new_id;
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
			return 0;
		}
	}
	
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
	
	public void aggiungiPagamento(int id, double amount, Date date) {
		

		int new_id;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		String sql_select_pagamento = "SELECT ID FROM PAGAMENTO";
		String sql_select_prestito = "SELECT ID FROM PRESTITO";
		String sql_update_bilancio = null;

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
		} catch (SQLException ex) {
			System.err.println("Transazione fallita per l'utente con ID: " + id);
			ex.printStackTrace();
		}

	}
	
	public void aggiungiPrestito(int id_utente, Date data_ultima_scadenza) { //da sostituire poi "int id_utente" con "Utente u" // da sostituire data con un oggetto di tipo Date
		String sql = "INSERT INTO PRESTITO(ID_utente, data_ultima_scadenza) VALUES('"+id_utente+"','"+data_ultima_scadenza+"')";
		Statement stmt = null;
		try {
			stmt = this.conn.createStatement();
			stmt.execute(sql);
			System.out.println("Prestito aggiunto");
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	
	public void terminaConnessione() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	
	/**
	 * returns the Persona object associated with the given name.
	 * @param nome the given name.
	 * @return {@link Persona}
	 */
	public Persona getPerson(String nome) {
		try(PreparedStatement queryPerson = this.conn.prepareStatement("SELECT * FROM PERSONA WHERE nome = ?")) {
			queryPerson.setString(1, nome);
			ResultSet resultSet = queryPerson.executeQuery();
			return new Persona(resultSet);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * returns the Azienda object associated with the given name.
	 * @param nome the given name.
	 * @return {@link Azienda}
	 */
	public Azienda getAzienda(String nome) {
		try(PreparedStatement queryAzienda = this.conn.prepareStatement("SELECT * FROM AZIENDA WHERE ragione_sociale = ?")) {
			queryAzienda.setString(1, nome);
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
	public String getPasswordPerson(String nome) {
		try(PreparedStatement queryPass = this.conn.prepareStatement("SELECT password FROM CREDENZIALE as C JOIN PERSONA as P on P.ID = C.ID_utente WHERE nome = ?")) {
			queryPass.setString(1, nome);
			ResultSet resultSet = queryPass.executeQuery();
			return resultSet.getString(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + nome);
			return "";
		}
	}
	
	/**
	 * returns the password hash associated with an agency username.
	 * @param nome the username.
	 * @return the password hash.
	 */
	public String getPasswordAgency(String nome) {
		try(PreparedStatement queryPass = this.conn.prepareStatement("SELECT password FROM CREDENZIALE as C JOIN AZIENDA as A on A.ID = C.ID_utente WHERE ragione_sociale = ?")) {
			queryPass.setString(1, nome);
			ResultSet resultSet = queryPass.executeQuery();
			return resultSet.getString(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + nome);
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
				transactionData.add(String.valueOf(payments.getInt(1)));
				transactionData.add(String.format("%,.2f €", payments.getDouble(3)));
				transactionData.add(dateFormat.format(payments.getDate(4)));
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
