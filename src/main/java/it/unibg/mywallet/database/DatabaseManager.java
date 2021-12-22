package it.unibg.mywallet.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;

import it.unibg.mywallet.model.Transazione;
import it.unibg.mywallet.model.Utente;
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
	private static int static_ID=0;
	private int ID;
	private Connection conn = null;
	private String nome_database;
	private String database_path;
	
	//COSTRUTTORE
	private DatabaseManager(String nome, String path) {
		this.ID = this.setID();
		this.nome_database=this.setNome(nome);
		this.database_path = this.setPath(path);
		try { //connessione
			//Guarda se il database esiste già, in quel caso si limita a creare una connessione, altrimenti crea anche le tabelle
			File db = new File(this.database_path+nome_database+".db");
			if(!db.exists()) {
				Class.forName("org.sqlite.JDBC");
				this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.database_path+this.nome_database+".db");
				System.out.println("Connessione al database avvenuta con successo...");
				//creazione tabelle
				Statement stmt = this.conn.createStatement();
				String create_persona = "CREATE TABLE PERSONA(ID int NOT NULL PRIMARY KEY, nome VARCHAR(30), cognome VARCHAR(30), codice_fiscale VARCHAR(16), data_nascita DATE, bilancio int)";
				String create_azienda = "CREATE TABLE AZIENDA(ID int NOT NULL PRIMARY KEY, ragione_sociale VARCHAR(30), partita_iva VARCHAR(11), bilancio int)";
				String create_credenziali = "CREATE TABLE CREDENZIALE(ID_utente int NOT NULL PRIMARY KEY, password VARCHAR(256))";
				String create_pagamento = "CREATE TABLE PAGAMENTO(ID PRIMARY KEY, ID_utente int NOT NULL, ammontare DOUBLE, data_contabilizzazione DATE)";
				String create_prestito = "CREATE TABLE PRESTITO(ID PRIMARY KEY, ID_utente int NOT NULL , ammontare DOUBLE, data DATE, data_ultima_scadenza DATE)";
				String create_cashback = "CREATE TABLE CASHBACK(ID_utente int NOT NULL PRIMARY KEY, percentuale int)";
				String create_risparmio = "CREATE TABLE RISPARMIO(ID_utente int NOT NULL PRIMARY KEY, totale int)";
				stmt.execute(create_persona);
				System.out.println("Tabella PERSONA creata");
				stmt.execute(create_azienda);
				System.out.println("Tabella AZIENDA creata");
				stmt.execute(create_credenziali);
				System.out.println("Tabella CREDENZIALE creata");
				stmt.execute(create_pagamento);
				System.out.println("Tabella PAGAMENTO creata");
				stmt.execute(create_prestito);
				System.out.println("Tabella PRESTITO creata");
				stmt.execute(create_cashback);
				System.out.println("Tabella CASHBACK creata");
				stmt.execute(create_risparmio);
				System.out.println("Tabella RISPARMIO creata");
				System.out.println("Tutte le tabelle sono state create");
			}else {
				Class.forName("org.sqlite.JDBC");
				this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.database_path+this.nome_database+".db");
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
	
	public void aggiungiPersona(String nome, String cognome, String cod_fisc, String date) { // da sostituire data con un oggetto di tipo Date
		Statement stmt = null;
		int new_id;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		String sql_select_azienda = "SELECT ID FROM AZIENDA";
		String sql_select_persona = "SELECT ID FROM PERSONA";
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
			String sql = "INSERT INTO PERSONA(ID, nome, cognome, codice_fiscale, data_nascita) VALUES('"+new_id+"','"+nome+"','"+cognome+"','"+cod_fisc+"','"+date+"')";
			stmt.execute(sql);
			System.out.println("Persona aggiunta, id: "+new_id);
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
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
		
		try(PreparedStatement insertPayment = this.conn.prepareStatement("INSERT INTO PAGAMENTO(ID_utente, ammontare, data) VALUES(?,?,?)")) {
			insertPayment.setInt(1, id);
			insertPayment.setDouble(2, amount);
			insertPayment.setDate(3, date);
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
	
	private int setID() {
		return static_ID++;
	}
	
	private String setNome(String nome) {
		return nome;
	}
	
	private String setPath(String path) {
		return path;
	}
	
	public int getID() {
		return this.ID;
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
	 * returns true if there is a person associated with the given username.
	 * @param name the username.
	 * @return true or false.
	 */
	public boolean isPerson(String name) {
		try(PreparedStatement queryUser = this.conn.prepareStatement("SELECT EXISTS(SELECT * FROM PERSONA WHERE nome = ?)")) {
			queryUser.setString(1, name);
			ResultSet resultSet = queryUser.executeQuery();
			return resultSet.getBoolean(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + name);
			return false;
		}
	}
	
	/**
	 * returns true if there is an ageny associated with the given username.
	 * @param name the username.
	 * @return true or false.
	 */
	public boolean isAgency(String name) {
		//Use try with resource to release the connection after query to avoid memory leak
		try(PreparedStatement queryUser = this.conn.prepareStatement("SELECT EXISTS(SELECT * FROM AZIENDA WHERE ragione_sociale = ?)")) {
			//assign to placeholder with index 1 value the name
			queryUser.setString(1, name);
			//get ResultSet from our query
			ResultSet resultSet = queryUser.executeQuery();
			//return result with index 1
			return resultSet.getBoolean(1);
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + name);
			return false;
		}
	}


	public Transazione[] getRecentTransaction(Utente utente) {
		String query = "SELECT * FROM PAGAMENTO WHERE ID_Utente = ?"
				+    "UNION"
				+    "SELECT * FROM PRESTITO WHERE ID_Utente = ?"
				+    "ORDER BY data_contabilizzazione"
				+    "LIMIT 10";
				
		try(PreparedStatement queryTransaction = this.conn.prepareStatement(query)) {
			queryTransaction.setInt(1, utente.getId());
			queryTransaction.setInt(2, utente.getId());
			ResultSet resultSet = queryTransaction.executeQuery();
			
			Set<Transazione> transactions = Sets.newHashSet();
			while(resultSet.next()) {
				//transactions.add(new Transazione())
			}
			
			
			return (Transazione[]) transactions.toArray();
		} catch (SQLException ex) {
			System.err.println("Autenticazione fallita per l'utente: " + utente);
			return null;
		}
	}
}
