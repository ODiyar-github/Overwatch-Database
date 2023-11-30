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
 * Die Klasse Map repräsentiert eine Spielkarte und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Map extends EntityParent implements IControllBuisness 
{
    private Connection connection;
    private int rowCounter = 0;
    
    /**
     * Standardkonstruktor für die Map-Klasse.
     */
    public Map(){}
    
    
    /**
     * Setzt die Datenbankverbindung für die Map-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf der Map verwendet wird.
     */
    public void SetConnection(Connection con)
    {
    	this.connection = con;
    }
    
    /**
     * 
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Map-Entität.
     * Erlaubt dem Benutzer, neue Maps hinzuzufügen, existierende Maps zu aktualisieren,
     * Maps zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Map ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neue Map Hinzufügen");
			println("2: Bestehendes Map Ändern");
			println("3: Map Löschen");
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
     * Fügt eine neue Map in die Datenbank ein.
     * Fordert den Benutzer auf, den Namen und den Spielmodus der Map einzugeben.
     */
    @Override
    public void Insert() {
        if(connection != null)
        {
            try
            {
                String insertMapQuery = "INSERT INTO Map (MapName, MapGameMode) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertMapQuery); 
                preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                preparedStatement.setString(2, getString("Geben Sie den SpielModus ein:"));
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen einer Karte getätigt!");
            }
        }
    }

    /**
     * Aktualisiert vorhandene Maps in der Datenbank.
     * Erlaubt dem Benutzer, einen oder alle Werte einer Map zu ändern.
     */
    @Override
    public void Update() {
        if(connection != null)
        {
            try
            {
            	Show();
            	println("Möchten Sie nur einen Wert in der Tabelle verändern?");
            	println("1: (Nur Einen)");
            	println("2: (Alle Werte)");
            	println("Exit");
                String updateMapQuery; 
                PreparedStatement preparedStatement; 
                switch(scanner.nextLine()) 
                {
                case "1":
                	String selection = getSQL();
                	updateMapQuery = "UPDATE Map SET "+selection+"WHERE = ?";
                	preparedStatement = connection.prepareStatement(updateMapQuery);
                	
                	switch(selection)
                	{
                	case "MapName":
                		preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                		break;
                	case "MapGameMode":
                		preparedStatement.setString(1, getString("Geben Sie den SpielModus ein:"));
                		break;
                	}
                	preparedStatement.setInt(2, getInt("Geben Sie die MapID ein:"));
                    preparedStatement.executeUpdate();
                    Show();
                    break;
                case "2":
                	updateMapQuery = "UPDATE Map SET MapName = ?, MapGameMode = ? WHERE MapID = ?";
                	preparedStatement = connection.prepareStatement(updateMapQuery);
                	preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                	preparedStatement.setString(2, getString("Geben Sie den SpielModus ein:"));
                	preparedStatement.setInt(3, getInt("Geben Sie die MapID ein:"));
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
     * Löscht eine Map aus der Datenbank anhand der MapID.
     * Fordert den Benutzer auf, die MapID der zu löschenden Map einzugeben.
     */
    @Override
    public void Delete() 
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteMapQuery = "DELETE FROM Map WHERE MapID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteMapQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die MapID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("KartenID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle vorhandenen Maps in der Datenbank an.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Map";
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
    public String getSQL() 
    {
    	String sql = "";
    	println("Welche Werte möchten Sie in dieser Tabelle ändern?");
    	println("Auswahl: Name, Spielmodus");
        println("Zum Beenden 'Exit' Eingeben");
    	while(true) 
    	{
    		String selectedValue = scanner.nextLine();
    		if(!selectedValue.equals("Name") && !selectedValue.equals("Spielmodus")) 
    		{
    			println("Bitte geben Sie eine korrekte Eingabe ein!");
    		}
    		else if(!selectedValue.equals("Exit")) 
    		{
    			break;
    		}
    		else 
    		{
    			switch(selectedValue) 
    			{
    			case "Name":
    				sql = "MapName";
    				break;
    			case "GameMode":
    				sql = "MapGameMode";
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

