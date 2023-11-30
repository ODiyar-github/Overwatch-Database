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
 * Die Klasse Ranking repräsentiert ein Ranking und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Ranking extends EntityParent implements IControllBuisness
{
    private Connection connection;
    private int rowCounter = 0;
    
    /**
     * Standardkonstruktor für die Ranking-Klasse.
     */
    public Ranking(){}
    
    /**
     * Setzt die Datenbankverbindung für die Ranking-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Ranking verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Ranking-Entität.
     * Erlaubt dem Benutzer, neue Rankings hinzuzufügen, existierende Rankings zu aktualisieren,
     * Rankings zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Ranking ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neues Ranking Hinzufügen");
			println("2: Bestehendes Ranking Ändern");
			println("3: Ranking Löschen");
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
				if(rowCounter > 0) 
				{
					println("Update/Änderung gestartet!");
					nextLine();
					Update();
					break;
				}
				else 
				{
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
					println("Keine Daten gefunden, um zu verändern");
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
     * Fügt ein neues Ranking in die Datenbank ein.
     * Fordert den Benutzer auf, die Turnier-ID, die Spieler-ID und die Platzierung des Spielers einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
                String insertRankingQuery = "INSERT INTO Ranking (TournamentID, PlayerID, RankingPlacement) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertRankingQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die TurnierID ein:"));
                preparedStatement.setInt(2, getInt("Geben Sie die SpielerID ein:"));
                preparedStatement.setString(3, getString("Geben Sie die Platzierung des Spielers ein:"));
                preparedStatement.executeUpdate();
                nextLine();
                Show();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen eines Events getätigt!");
            }
        }
    }

    /**
     * Aktualisiert ein vorhandenes Ranking in der Datenbank.
     * Erlaubt dem Benutzer, die Platzierung eines Spielers in einem bestimmten Turnier zu ändern.
     */
    @Override
    public void Update() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String updateRankingQuery = "UPDATE Ranking SET RankingPlacement = ? WHERE TournamentID = ? AND PlayerID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateRankingQuery);
                preparedStatement.setString(1, getString("Geben Sie die Platzierung des Spielers ein:"));
                preparedStatement.setInt(2, getInt("Geben Sie die TurnierID ein:"));
                preparedStatement.setInt(3, getInt("Geben Sie die SpielerID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch(SQLException e)
            {
            	println("Falsche Eingabe getätigt.");
            }
        }
    }
    
    /**
     * Löscht ein Ranking aus der Datenbank.
     * Der Benutzer wird aufgefordert, die Turnier-ID und die Spieler-ID des zu löschenden Rankings einzugeben.
     */
    @Override
    public void Delete()
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteRankingQuery = "DELETE FROM Ranking WHERE TournamentID = ? AND PlayerID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteRankingQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die TurnierID ein:"));
                preparedStatement.setInt(2, getInt("Geben Sie die SpielerID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("TurnierID oder SpielerID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle Rankings in der Datenbank an.
     * Verwendet eine SQL-Abfrage, um alle Spalten und Zeilen aus der Ranking-Tabelle abzurufen und anzuzeigen.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Ranking";
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
