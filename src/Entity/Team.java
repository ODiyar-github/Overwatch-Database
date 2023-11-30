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
 * Die Klasse Team repräsentiert ein Team und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Team extends EntityParent implements IControllBuisness 
{
    private Connection connection;
    private int rowCounter = 0;
    /**
     * Standardkonstruktor für die Team-Klasse.
     */
    public Team(){}
    
    /**
     * Setzt die Datenbankverbindung für die Team-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Team verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Team-Entität.
     * Erlaubt dem Benutzer, neue Teams hinzuzufügen, existierende Teams zu aktualisieren,
     * Teams zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Team ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neues Team Hinzufügen");
			println("2: Bestehendes Team Ändern");
			println("3: Team Löschen");
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
     * Fügt ein neues Team in die Datenbank ein.
     * Fordert den Benutzer auf, den Teamnamen, den Clantag und die Turnier-ID des Teams einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
                String insertTeamQuery = "INSERT INTO Team (TeamName, TeamClanTag, TournamentID) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertTeamQuery); 
                preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                preparedStatement.setString(2, getString("Geben Sie den ClanTag ein"));
                preparedStatement.setInt(3, getInt("Geben Sie TurnierID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen eines Teams getätigt!");
            }
        }
    }

    /**
     * Aktualisiert vorhandene Teams in der Datenbank.
     * Erlaubt dem Benutzer, einen oder alle Werte eines Teams zu ändern.
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
                String updateTeamQuery;
                PreparedStatement preparedStatement;
                switch(scanner.nextLine()) 
                {
                case "1":
                	String selection = getSQL();
                	updateTeamQuery = "UPDATE Team SET"+selection+"WHERE = ?";
                	preparedStatement = connection.prepareStatement(updateTeamQuery);
                	
                	switch(selection) 
                	{
                	case "TeamName":
                		preparedStatement.setString(1, getString("Geben Sie einen Team Namen ein:"));
                		break;
                	case "TeamClanTag":
                		preparedStatement.setString(1, getString("Geben Sie ein Team ClanTag ein:"));
                		break;
                	case "VictoriousTeamID":
                		preparedStatement.setInt(1, getInt("Wenn das Team gewonnen hat, so geben Sie die TeamID ein, andernfalls 0:"));
                		break;
                	case "LosingTeamID":
                		preparedStatement.setInt(1, getInt("Wenn das Team verloren hat, so geben Sie die TeamID ein, andernfalls 0:"));
                		break;
                	}
                	preparedStatement.setInt(2, getInt("Geben Sie die TeamID ein:"));
                    preparedStatement.executeUpdate();
                    Show();
                case "2":
                    updateTeamQuery = "UPDATE Team SET TeamName = ?, TeamClanTag = ?, VictoriousTeamID = ?, LosingTeamID = ?  WHERE TeamID = ?";
                    preparedStatement = connection.prepareStatement(updateTeamQuery);
                    preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                    preparedStatement.setString(2, getString("Geben Sie den ClanTag ein"));
                    preparedStatement.setInt(3, getInt("Wenn das Team gewonnen hat, so geben Sie die TeamID ein, andernfalls 0:"));
                    preparedStatement.setInt(4, getInt("Wenn das Team verloren hat, so geben Sie die TeamID ein, andernfalls 0:"));
                    preparedStatement.setInt(5, getInt("Geben Sie die TeamID ein:"));
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
     * Löscht ein Team aus der Datenbank.
     * Der Benutzer wird aufgefordert, die TeamID des zu löschenden Teams einzugeben.
     */
    @Override
    public void Delete() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteTeamQuery = "DELETE FROM Team WHERE TeamID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteTeamQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die TeamID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("TeamID nicht gefunden!");
            }
        }
    }

    /**
     * Zeigt alle Teams in der Datenbank an.
     * Verwendet eine SQL-Abfrage, um alle Spalten und Zeilen aus der Team-Tabelle abzurufen und anzuzeigen.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Team";
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
        println("Auswahl: TeamName, TeamClanTag, GewinnerTeamID, VerliererTeamID");
        println("Zum Beenden 'Exit' Eingeben");
        while(true) 
        {
            String selectedValue = scanner.nextLine();
            if(!selectedValue.equals("TeamName") && !selectedValue.equals("TeamClanTag") && !selectedValue.equals("GewinnerTeamID") && !selectedValue.equals("VerliererTeamID"))
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
            	case "TeamName":
            		sql = "TeamName";
            		break;
            	case "TeamClanTag":
            		sql = "TeamClanTag";
            		break;
            	case "GewinnerTeamID":
            		sql = "VictoriousTeamID";
            		break;
            	case "VerliererTeamID":
            		sql = "LosingTeamID";
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
