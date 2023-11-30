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
 * Die Klasse Hero repräsentiert einen Helden und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Hero extends EntityParent implements IControllBuisness
{
    private Connection connection;
    private int rowCounter = 0;
    
    /**
     * Standardkonstruktor für die Hero-Klasse.
     */
    public Hero(){}
    
    /**
     * Setzt die Datenbankverbindung für die Hero-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Hero verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Hero-Entität.
     * Erlaubt dem Benutzer, neue Helden hinzuzufügen, existierende Helden zu aktualisieren,
     * Helden zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Hero ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neuer Hero Hinzufügen");
			println("2: Bestehendes Hero Ändern");
			println("3: Hero Löschen");
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
				}
				else {
					println("Keine Daten gefunden, um zu verändern");
					break;
				}
				break;
			case "3":
				if(rowCounter > 0) 
				{
					println("Delete/Löschen gestartet!");
					nextLine();
					Delete();
					break;
				}
				else {
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
     * Fügt einen neuen Helden in die Datenbank ein.
     * Fordert den Benutzer auf, den Helden-Namen, die Fähigkeiten und die Spieler-ID einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
                String insertHeroQuery = "INSERT INTO Hero (HeroName, HeroSkills, PlayerID) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertHeroQuery); 
                preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                preparedStatement.setString(2, getString("Geben Sie die Fähigkeiten ein:"));
                preparedStatement.setInt(3, getInt("Geben Sie die HeldenID ein:"));
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) 
            {
                System.out.println("Falsche Eingabe bei dem Hinzufügen eines Helden!");
            }
        }
    }

    /**
     * Aktualisiert einen vorhandenen Helden in der Datenbank.
     * Erlaubt dem Benutzer, den Helden-Namen, die Fähigkeiten oder die Spieler-ID eines Helden zu ändern.
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
                String updateHeroQuery;
                PreparedStatement preparedStatement;
                switch(scanner.nextLine()) {
                case "1":
                	String selection = getSQL();
                	updateHeroQuery = "UPDATE Hero SET "+selection+" = ? WHERE HeroID = ?";
                	preparedStatement = connection.prepareStatement(updateHeroQuery);
                	
                	switch(selection)
                	{
                	case "HeroName":
                		preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                		break;
                	case "HeroSkills":
                		preparedStatement.setString(1, getString("Geben Sie die Fähigkeiten ein:"));
                		break;
                	case "PlayerID":
                		preparedStatement.setInt(1, getInt("Geben Sie den zugehörigen SpielerID ein:"));
                		break;
                	}
                	preparedStatement.setInt(2, getInt("Geben Sie die HeldenID ein:"));
                	preparedStatement.executeUpdate();
                	Show();
                	break;
                case "2":
                	updateHeroQuery = "UPDATE Hero SET HeroName = ?, HeroSkills = ?, PlayerID = ? WHERE HeroID = ?";
                    preparedStatement = connection.prepareStatement(updateHeroQuery);
                	preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                	preparedStatement.setString(2, getString("Geben Sie die Fähigkeiten ein:"));
                	preparedStatement.setInt(3, getInt("Geben Sie den zugehörigen SpielerID ein:"));
                	preparedStatement.setInt(4, getInt("Geben Sie die HeldenID ein:"));
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
     * Löscht einen vorhandenen Helden aus der Datenbank.
     * Erlaubt dem Benutzer, die Helden-ID des zu löschenden Helden einzugeben.
     */
    @Override
    public void Delete()
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteHeroQuery = "DELETE FROM Hero WHERE HeroID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteHeroQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die HeldenID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("HeldenID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle Helden in der Datenbank an, einschließlich aller Spalten für jeden Helden.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Hero";
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
     */
    private String getSQL() 
    {
    	String sql = "";
    	println("Welche Werte möchten Sie in der Tabelle ändern?");
    	println("Auswahl: Name, Fähigkeiten, SpielerID"); 
    	println("Zum Beenden 'Exit' Eingeben");
    	while(true) {
    		String selectedValue = scanner.nextLine();
    		if(!selectedValue.equals("Name") && !selectedValue.equals("Fähigkeiten") && !selectedValue.equals("SpielerID")) 
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
    			case "Name":
    				sql = "HeroName";
    				break;
    			case "Fähigkeiten":
    				sql = "HeroSkills";
    				break;
    			case "SpielerID":
    				sql = "PlayerID";
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

