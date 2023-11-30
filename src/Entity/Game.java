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
 * Die Klasse Game repräsentiert ein Spiel und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Game extends EntityParent implements IControllBuisness 
{
    private Connection connection;
    private int rowCounter = 0;
    /**
     * Standardkonstruktor für die Game-Klasse.
     */
    public Game(){}
    
    /**
     * Setzt die Datenbankverbindung für die Game-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Game verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Game-Entität.
     * Erlaubt dem Benutzer, neue Spiele hinzuzufügen, existierende Spiele zu aktualisieren,
     * Spiele zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Game ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neues Game Hinzufügen");
			println("2: Bestehendes Game Ändern");
			println("3: Game Löschen");
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
				if(rowCounter >0) 
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
				if(rowCounter >0) 
				{
					println("Delete/Löschen gestartet!");
					nextLine();
					Delete();
					break;
				}
				else 
				{
					println("Keine Daten gefunden, um zu löschen!");
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
     * Fügt ein neues Spiel in die Datenbank ein.
     * Fordert den Benutzer auf, die Turnier-ID, die Karten-ID, die Team-IDs des Gewinner- und Verliererteams
     * sowie die Spielzeit einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
                String insertGameQuery = "INSERT INTO Game (TournamentID, MapID, VictoriousTeamID, LosingTeamID, GameTime) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertGameQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die TournamentID ein:"));
                preparedStatement.setInt(2, getInt("Geben Sie die MapID ein:"));
                preparedStatement.setInt(3, getInt("Geben Sie die TeamID vom Gewinner Team ein:"));
                preparedStatement.setInt(4, getInt("Geben SIe die TeamID vom Verlierer Team ein:"));
                preparedStatement.setString(5, getGameTime());
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen eines Spiel getätigt!");
            }
        }
    }

    /**
     * Aktualisiert ein vorhandenes Spiel in der Datenbank.
     * Erlaubt dem Benutzer, die Turnier-ID, die Karten-ID, die Team-IDs des Gewinner- und Verliererteams
     * oder die Spielzeit eines Spiels zu ändern.
     */
    @Override
    public void Update() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
            	println("Möchten Sie nur einen Wert in der Tabelle ändern?");
            	println("1: (nur einen!)");
            	println("2: (alle Werte!)");
            	println("sonstiges: Exit");
            	nextLine();
                String updateGameQuery;
                PreparedStatement preparedStatement;
                switch(scanner.nextLine()) {
                case "1":
                	String selection = getSQL();
                	updateGameQuery = "UPDATE Game SET"+selection+"WHERE = ?";
                	preparedStatement = connection.prepareStatement(updateGameQuery);
                	
                	switch(selection) 
                	{
                	case "TournamentID":
                		preparedStatement.setInt(1, getInt("Geben Sie die TournamentID ein:"));
                		break;
                	case "MapID":
                		preparedStatement.setInt(1, getInt("Geben Sie die MapID ein:"));
                		break;
                	case "VictoriousTeamID":
                		preparedStatement.setInt(1, getInt("Geben Sie die TeamID vom Gewinner Team ein:"));
                		break;
                	case "LosingTeamID":
                		preparedStatement.setInt(1, getInt("Geben SIe die TeamID vom Verlierer Team ein:"));
                		break;
                	case "GameTime":
                		preparedStatement.setString(1, getDate());
                		break;
                	}
                	preparedStatement.setInt(2, getInt("Geben Sie die SpielID ein:"));
                    preparedStatement.executeUpdate();
                    Show();
                case "2":
                	updateGameQuery = "UPDATE Game SET TournamentID = ?, MapID = ?, VictoriousTeamID = ?, LosingTeamID = ?, GameTimestamp = ? WHERE GameID = ?";
                	preparedStatement = connection.prepareStatement(updateGameQuery);
                	preparedStatement.setInt(1, getInt("Geben Sie die TournamentID ein:"));
                    preparedStatement.setInt(2, getInt("Geben Sie die MapID ein:"));
                    preparedStatement.setInt(3, getInt("Geben Sie die TeamID vom Gewinner Team ein:"));
                    preparedStatement.setInt(4, getInt("Geben SIe die TeamID vom Verlierer Team ein:"));
                    preparedStatement.setString(5, getDate());
                    preparedStatement.setInt(6, getInt("Geben Sie die SpielID ein:"));
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
     * Löscht ein vorhandenes Spiel aus der Datenbank.
     * Erlaubt dem Benutzer, die Spiel-ID des zu löschenden Spiels einzugeben.
     */
    @Override
    public void Delete() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteGameQuery = "DELETE FROM Game WHERE GameID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteGameQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die SpielID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
                println("SpielID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle Spiele in der Datenbank an, einschließlich aller Spalten für jedes Spiel.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Game";
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
     * Gibt einen SQL-Ausdruck für die Auswahl der zu aktualisierenden Spalte zurück.
     * Der Benutzer wird aufgefordert, eine Auswahl zu treffen, welche Spalte aktualisiert werden soll.
     * @return Der SQL-Name des zu ändernden Werts.
     */
    public String getSQL() 
    {
    	String sql = "";
    	println("Welche Werte möchten Sie in der Tabelle ändern?");
    	println("Auswahl: TurnierID, KartenID, GewinnerTeamID, VerliereTeamID, Spielzeit");
    	println("Zum Beenden 'Exit' Eingeben");
    	while(true) {
    		String selectedValue = scanner.nextLine();
    		if(!selectedValue.equals("TurnierID") && !selectedValue.equals("KartenID") && !selectedValue.equals("GewinnerTeamID") && !selectedValue.equals("VerliereTeamID") && !selectedValue.equals("Spielzeit")) 
    		{
    			println("Bitte geben Sie eine korrekte Eingabe ein!");
    		} 
    		else if(selectedValue.equals("Exit")) 
    		{
    			break;
    		} 
    		else 
    		{
    			switch(selectedValue) 
    			{
    			case "TurnierID":
    				sql = "TournamentID";
    				break;
    			case "KartenID":
    				sql = "MapID";
    				break;
    			case "GewinnerTeamID":
    				sql = "VictoriousTeamID";
    				break;
    			case "VerliereTeamID":
    				sql = "LosingTeamID";
    				break;
    			case "Spielzeit":
    				sql = "GameTime";
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
