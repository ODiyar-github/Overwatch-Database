package Interface;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Das Interface IControllBuisness definiert Methoden für die Anwendung von
 * Datenbankentitäten.
 */
public interface IControllBuisness
{
    /**
     * Setzt die Datenbankverbindung für die Geschäftslogik.
     *
     * @param con Die zu setzende Datenbankverbindung.
     */
	void SetConnection(Connection con);
	
    /**
     * Startet die Anwendung und ermöglicht Benutzerinteraktionen.
     */
	void Start();
	
    /**
     * Fügt eine neue Datensatz in die Datenbank ein.
     */
    void Insert();
    
    /**
     * Aktualisiert vorhandene Datensätze in der Datenbank.
     */
    void Update();
    
    /**
     * Löscht Datensätze aus der Datenbank.
     */
    void Delete();
    
    /**
     * Zeigt Datensätze aus der Datenbank an.
     */
    void Show();
    
    /**
     * Schließt Ressourcen, die von der Anwendung verwendet werden (z.B. schließt die
     * Datenbankverbindung).
     *
     * @throws SQLException Wenn ein Fehler beim Schließen der Ressourcen auftritt.
     */
    void Dispose() throws SQLException;
}