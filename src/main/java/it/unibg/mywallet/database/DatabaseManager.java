package it.unibg.mywallet.database;
import java.io.File;
import java.sql.*;


/*Alla creazione di un'instanza dell'oggetto, verrà creata una connessione ad database col nome che viene passato come attirbuto. Se il database con quel nome esiste
nel percorso specificato, allora crea solamente una connessione, altrimenti se non esiste ne crea uno nuovo.
Alla creazione di un database venegono automaticamente create le tabelle PERSONA(), AZIENDA(), CREDENZIALI(), PAGAMENTO(), PRESTITO(), CASHBACK(), RISPARMIO()
Gli attributi delle tabelle sono:
PERSONA(ID int NOT NULL PRIMARY KEY, nome VARCHAR(30), cognome VARCHAR(30), codice_fiscale VARCHAR(16), data_nascita DATE, bilancio int)
AZIENDA(ID int NOT NULL PRIMARY KEY, ragione_sociale VARCHAR(30), partita_iva(11), bilancio int)
CREDENZIALE(ID_utente int NOT NULL PRIMARY KEY, password VARCHAR(30))
PAGAMENTO(ID_utente int NOT NULL PRIMARY KEY, data_contabilizzazione DATE)
PRESTITO(ID_utente int NOT NULL PRIMARY KEY, data_ultima_scadenza DATE)
CASHBACK(ID_utente int NOT NULL PRIMARY KEY, percentuale int)
RISPARMIO(ID_utente int NOT NULL PRIMARY KEY, totale int)
*/
public class DatabaseManager {
	//ATTRIBUTI 
	private static int static_ID=0;
	private int ID;
	private Connection conn = null;
	private String nome_database;
	private String database_path;
	
	//COSTRUTTORE
	public DatabaseManager(String nome, String path) {
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
				String create_credenziali = "CREATE TABLE CREDENZIALE(ID_utente int NOT NULL PRIMARY KEY, password VARCHAR(30))";
				String create_pagamento = "CREATE TABLE PAGAMENTO(ID_utente int NOT NULL PRIMARY KEY, data_contabilizzazione DATE)";
				String create_prestito = "CREATE TABLE PRESTITO(ID_utente int NOT NULL PRIMARY KEY, data_ultima_scadenza DATE)";
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
				System.out.println("Connessione al database avvenuta con successo...");
				System.out.println("database già esistente");
			}
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	

	//METODI
	
	public void aggiungiPersona(int id, String nome, String cognome, String cod_fisc, String date) { // da sostituire data con un oggetto di tipo Date
		String sql = "INSERT INTO PERSONA(ID, nome, cognome, codice_fiscale, data_nascita) VALUES('"+id+"','"+nome+"','"+cognome+"','"+cod_fisc+"','"+date+"')";
		Statement stmt = null;
		try {
			stmt = this.conn.createStatement();
			stmt.execute(sql);
			System.out.println("Persona aggiunta");
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	
	public void aggiungiAzienda(int id, String ragione_sociale, String p_iva) {
		String sql = "INSERT INTO AZIENDA(ID, ragione_sociale, partita_iva) VALUES('"+id+"','"+ragione_sociale+"','"+p_iva+"')";
		Statement stmt = null;
		try {
			stmt = this.conn.createStatement();
			stmt.execute(sql);
			System.out.println("Azienda aggiunta");
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	
	public void aggiungiCredenziali(int id_utente, String password) { // da sostituire poi "int id_utente" con "Utente u"
		String sql = "INSERT INTO CREDENZIALE(ID_utente, password) VALUES('"+id_utente+"','"+password+"')";
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
	
	public void aggiungiPagamanto(int id_utente, String data_contabilizzazione) {	//da sostituire poi "int id_utente" con "Utente u" // da sostituire data con un oggetto di tipo Date
		String sql = "INSERT INTO PAGAMENTO(ID_utente, data_contabilizzazione) VALUES('"+id_utente+"','"+data_contabilizzazione+"')";
		Statement stmt = null;
		try {
			stmt = this.conn.createStatement();
			stmt.execute(sql);
			System.out.println("Pagamento aggiunto");
		}catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
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
}
