package Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import EntityParent.EntityParent;
import Interface.IControllBuisness;

/**
 * Die Klasse Tournament repräsentiert ein Turnier und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Tournament extends EntityParent implements IControllBuisness 
{
    private Connection connection;
    private int rowCounter = 0;
    
    /**
     * Standardkonstruktor für die Tournament-Klasse.
     */
    public Tournament(){}
    
    /**
     * Setzt die Datenbankverbindung für die Turnier-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Turnier verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Turnier-Entität.
     * Erlaubt dem Benutzer, neue Turniere hinzuzufügen, existierende Turniere zu aktualisieren,
     * Turniere zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Tournament ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neues Tournament Hinzufügen");
			println("2: Bestehendes Tournament Ändern");
			println("3: Tournament Löschen");
			println("sonstiges: Exit");
			nextLine();
			switch(scanner.nextLine()) 
			{
			case "1":
				println("Insert/Einfügen gestartet!");
				nextLine();
				Insert();
				break;
			case "2": 
				if(rowCounter > 0) {
					println("Update/Änderung gestartet!");
					nextLine();
					Update();
					break;
				}
				else {
					println("Keine Daten gefunden, um zu verändern");
					break;
				}

			case "3":
				if(rowCounter > 0) 
				{
					println("Delete/Löschen gestartet!");
					nextLine();
					Delete();
					break;
				}
				else 
				{
					println("Keine Daten gefunden, um zu Löschen");
					break;
				}
			default:
				println("Exit...");
				nextLine();
				loop = false;
				break;
			}
		}
	}  
    
    /**
     * Fügt ein neues Turnier in die Datenbank ein.
     * Fordert den Benutzer auf, die EventID, den Namen, das Datum, die maximale Teamgröße und die Erfahrungsschwelle des Turniers einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
                String insertTournamentQuery = "INSERT INTO Tournament (EventID, TournamentName, TournamentDate, TournamentMaxTeamSize, TournamentExperienceLevel) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertTournamentQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die eine Verfügbare EventID ein:"));
                preparedStatement.setString(2, getString("Geben Sie den Turnier Namen ein:"));
                preparedStatement.setString(3, getDate());
                preparedStatement.setInt(4, getInt("Geben Sie die Maximale Team größe ein:"));
                preparedStatement.setInt(5, getInt("Geben Sie die Erfahrungswerte ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen eines Turniers getätigt!");
            }
        }
    }

    /**
     * Aktualisiert vorhandene Turniere in der Datenbank.
     * Erlaubt dem Benutzer, einen oder alle Werte eines Turniers zu ändern.
     */
    @Override
    public void Update() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
            	println("Möchten Sie nur einen Wert in der Tabelle verändern?");
            	println("1: (Nur Einen)");
            	println("2: (Alle Werte)");
            	println("Exit");
            	nextLine();
            	String updateTournamentQuery;
            	PreparedStatement preparedStatement;
            	switch(scanner.nextLine()) 
            	{
            	case "1":
            		String selection = getSQL();
            		updateTournamentQuery ="UPDATE Tournament SET "+selection+" = ? WHERE TournamentID = ?";
            		preparedStatement = connection.prepareStatement(updateTournamentQuery);
                	switch(selection) 
                	{
                	case "EventID":
                		preparedStatement.setString(1, getString("Bitte geben Sie einen Neuen Event Namen ein:"));
                		break;
                	case "TournamentName":
                		preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                		break;
                	case "TournamentDate":
                		preparedStatement.setString(1, getDate());
                		break;
                	case "TournamentMaxTeamSize":
                		preparedStatement.setInt(1, getInt("Geben Sie die Maximale Team größe ein:"));
                		break;
                 	case "TournamentExperienceLevel":
                 		 preparedStatement.setString(1, getString("Geben Sie die Erfahrungswerte ein:"));
                		break;
                	}
                    preparedStatement.setInt(2, getInt("Geben Sie die TurnierID ein, wo Sie gerne die Veränderung vornehmen möchten:"));
                    preparedStatement.executeUpdate();
                    Show();
            		break;
            	case "2":
                    updateTournamentQuery = "UPDATE Tournament SET EventID = ?, TournamentName = ?, TournamentDate = ?, TournamentMaxTeamSize = ?, TournamentExperienceLevel = ? WHERE TournamentID = ?";
                    preparedStatement = connection.prepareStatement(updateTournamentQuery); 
                    preparedStatement.setInt(1, getInt("Geben Sie die EventID ein:"));
                    preparedStatement.setString(2, getString("Geben Sie den Namen ein:"));
                    preparedStatement.setString(3, getDate());
                    preparedStatement.setInt(4, getInt("Geben Sie die Maximale Team größe ein:"));
                    preparedStatement.setString(5, getString("Geben Sie die Erfahrungswerte ein:"));
                    preparedStatement.setInt(6, getInt("Geben Sie die TurnierID ein:"));
                    preparedStatement.executeUpdate();
                    Show();
            		break;
            	default:
            		println("Update/Änderung wurde Abgebrochen...");
            		break;
            	}
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe getätigt.");
            }
        }
    }

    /**
     * Löscht ein Turnier aus der Datenbank.
     * Der Benutzer wird aufgefordert, die TurnierID des zu löschenden Turniers einzugeben.
     */
    @Override
    public void Delete() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteTournamentQuery = "DELETE FROM Tournament WHERE TournamentID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteTournamentQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die TurnierID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("TurnierID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle Turniere in der Datenbank an.
     * Verwendet eine SQL-Abfrage, um alle Spalten und Zeilen aus der Tournament-Tabelle abzurufen und anzuzeigen.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Tournament";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery); 
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) 
            {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();
            
            while (resultSet.next()) 
            {
            	rowCounter++;
                for (int i = 1; i <= columnCount; i++) 
                {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
            nextLine();
        }
        catch(SQLException e) 
        {
        	println("Fehler beim Anzeigen der Datenbank!");
        }
    }
    
    /**
     * Hilfsmethode, die den Benutzer nach den zu ändernden Werten in der Tabelle fragt
     * und den entsprechenden SQL-Namen zurückgibt.
     *
     * @return Der SQL-Name des zu ändernden Werts.
     */
    private String getSQL() 
    {
    	String sql = "";
        println("Welche Werte in der Tabelle möchten Sie Verändern?");
        println("Auswahl: EventID, Name, Datum, MaximaleSpielerAnzahl, Erfahrungsschwelle");
        println("Zum Beenden 'Exit' Eingeben");
        while(true) 
        {
            String selectedValue = scanner.nextLine();
            if(!selectedValue.equals("EventID") && !selectedValue.equals("Namen") && !selectedValue.equals("Datum") && !selectedValue.equals("MaximaleSpielerAnzahl")&& !selectedValue.equals("Erfahrungsschwelle"))
            {
                println("Bitte machen Sie eine Korrekte Eingabe!");
            }
            else if(selectedValue.equals("Exit")) 
            {
            	break;
            }
            else 
            {
            	switch(selectedValue) 
            	{
            	case "EventID":
            		sql = "EventID";
            		break;
            	case "Name":
            		sql = "TournamentName";
            		break;
            	case "Datum":
            		sql = "TournamentDate";
            		break;
            	case "MaximaleSpielerAnzahl":
            		sql = "TournamentMaxTeamSize";
            		break;
            	case "Erfahrungsschwelle":
            		sql = "TournamentExperienceLevel";
            		break;
            	}
            	break;
            }
        }
		return sql;
    }
    
    /**
     * Schließt die Datenbankverbindung, wenn sie nicht geschlossen ist.
     * Kann eine SQLException auslösen, wenn ein Fehler beim Schließen der Verbindung auftritt.
     */
	@Override
	public void Dispose() throws SQLException 
	{
		if(!this.connection.isClosed()) 
		{
			this.connection.close();
		}
	}
}
