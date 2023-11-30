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
 * Die Klasse Player repräsentiert einen Spieler und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Player extends EntityParent implements IControllBuisness
{
    private Connection connection;
    private int rowCounter = 0;
    
    /**
     * Standardkonstruktor für die Player-Klasse.
     */
    public Player(){}
    
    /**
     * Setzt die Datenbankverbindung für die Player-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Player verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Player-Entität.
     * Erlaubt dem Benutzer, neue Spieler hinzuzufügen, existierende Spieler zu aktualisieren,
     * Spieler zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Player ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neuen Player Hinzufügen");
			println("2: Bestehenden Player Ändern");
			println("3: Player Löschen");
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
     * Fügt einen neuen Spieler in die Datenbank ein.
     * Fordert den Benutzer auf, den Namen, die Erfahrungswerte, die Spielerinformation und die Team-ID des Spielers einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String insertPlayerQuery = "INSERT INTO Player (Name, ExpierenceLevel, Information, TeamID) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertPlayerQuery); 
                preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                preparedStatement.setInt(2, getInt("Geben Sie die Erfahrungwerte ein:"));
                preparedStatement.setString(3, getString("Geben Sie die Spieler Information ein:"));
                preparedStatement.setInt(4, getInt("Geben Sie die TeamID des Spielers ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen eines Spielers getätigt!");
            }
        }
    }

    /**
     * Aktualisiert einen vorhandenen Spieler in der Datenbank.
     * Erlaubt dem Benutzer, den Namen, die Erfahrungswerte, die Spielerinformation oder die Team-ID eines Spielers zu ändern.
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
                String updatePlayerQuery;
                PreparedStatement preparedStatement;
                switch(scanner.nextLine())
                {
                case "1":
                	String selection = scanner.nextLine();
                	updatePlayerQuery = "UPDATE Player SET "+selection+"WHERE = ? ";
                	preparedStatement = connection.prepareStatement(updatePlayerQuery);
                	
                	switch(selection)
                	{
                	case "PlayerName":
                		preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                		break;
                	case "PlayerExperienceLevel":
                		preparedStatement.setInt(1, getInt("Geben Sie die Erfahrungwerte ein:"));
                		break;
                	case "PlayerInformation":
                		preparedStatement.setString(1, getString("Geben Sie die Spieler Information ein:"));
                		break;
                	case "TeamID":
                		preparedStatement.setInt(1, getInt("Geben Sie die TeamID des Spielers ein:"));
                		break;
                	}
                	preparedStatement.setInt(2, getInt("Geben Sie die SpielerID ein:"));
                    preparedStatement.executeUpdate();
                    Show();
                    break;
                case "2":
                	updatePlayerQuery = "UPDATE Player SET Name = ?, ExpierenceLevel = ?, Information = ?, TeamID = ? WHERE PlayerID = ?";
                	preparedStatement = connection.prepareStatement(updatePlayerQuery);
                	preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                	preparedStatement.setInt(2, getInt("Geben Sie die Erfahrungwerte ein:"));
                	preparedStatement.setString(3, getString("Geben Sie die Spieler Information ein:"));
                	preparedStatement.setInt(4, getInt("Geben Sie die TeamID des Spielers ein:"));
                	preparedStatement.setInt(5, getInt("Geben Sie die SpielerID ein:"));
                	preparedStatement.executeUpdate();
                	Show();
                	break;
            	default:
            		println("Update/Änderung wurde Abgebrochen...");
            		break;
            	}
            }
            catch(SQLException e)
            {
            	println("Falsche Eingabe getätigt.");
            }
        }
    }
    
    /**
     * Löscht einen Spieler aus der Datenbank.
     * Der Benutzer wird aufgefordert, die Spieler-ID des zu löschenden Spielers einzugeben.
     */
    @Override
    public void Delete()
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deletePlayerQuery = "DELETE FROM Player WHERE PlayerID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deletePlayerQuery); 
                preparedStatement.setInt(1, getInt("Gebene Sie die SpielerID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("SpielerID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle Spieler in der Datenbank an.
     * Verwendet eine SELECT-Abfrage, um alle Spalten für jeden Spieler abzurufen und anzuzeigen.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Player";
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
    	println("Auswahl: SpielerName, Erfahrungswerte, SpielerInformation, TeamID");
    	println("Zum Beenden 'Exit' Eingeben");
    	while(true) 
    	{
    		String selectedValue = scanner.nextLine();
    		if(!selectedValue.equals("SpielerName") && !selectedValue.equals("Erfahrungswerte") && !selectedValue.equals("SpielerInformation") && !selectedValue.equals("TeamID"))
    		{
    			println("Bitte geben Sie eine korrekte Eingabe aus!");
    		} 
    		else if (selectedValue.equals("Exit")) 
    		{
    			break;
    		} 
    		else 
    		{
    			switch(selectedValue)
    			{
    			case "Name":
    				sql = "PlayerName";
    				break;
    			case "ExperienceLevel":
    				sql = "PlayerExperienceLevel";
    				break;
    			case "Information":
    				sql = "PlayerInformation";
    				break;
    			case "TeamID":
    				sql = "TeamID";
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
