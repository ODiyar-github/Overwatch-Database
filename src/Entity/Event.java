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
 * Die Klasse Event repräsentiert ein Ereignis und implementiert das IControllBuisness-Interface.
 * Sie erbt von der EntityParent-Klasse, die grundlegende Funktionen und Eingabeaufforderungen bereitstellt.
 */
public class Event extends EntityParent implements IControllBuisness
{
    private Connection connection;
    private int rowCounter = 0;
    
    /**
     * Standardkonstruktor für die Event-Klasse.
     */
    public Event(){}
    
    /**
     * Setzt die Datenbankverbindung für die Event-Instanz.
     *
     * @param con Die Datenbankverbindung, die für die Operationen auf dem Event verwendet wird.
     */
    public void SetConnection(Connection con) 
    {
    	this.connection = con;
    }
    
    /**
     * Startet eine Benutzerschnittstelle für die Interaktion mit der Event-Entität.
     * Erlaubt dem Benutzer, neue Events hinzuzufügen, existierende Events zu aktualisieren,
     * Events zu löschen und das Programm zu beenden.
     */
	@Override
	public void Start() 
	{
		boolean loop = true;
		while(loop) 
		{
			println("Sie haben die Relation Event ausgewählt");
			println("Was möchten Sie tun?");
			println("1: Neues Event Hinzufügen");
			println("2: Bestehendes Event Ändern");
			println("3: Event Löschen");
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
     * Fügt ein neues Event in die Datenbank ein.
     * Fordert den Benutzer auf, den Namen, das Datum, den Ort und die Beschreibung des Events einzugeben.
     */
    @Override
    public void Insert() 
    {
        if(connection != null)
        {
            try
            {
                String insertEventQuery = "INSERT INTO Event (EventName, EventDate, EventLocation, EventDescription) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertEventQuery); 
                preparedStatement.setString(1, getString("Geben Sie den Namen ein:"));
                preparedStatement.setString(2, getDate());
                preparedStatement.setString(3, getString("Geben Sie den Ort ein:"));
                preparedStatement.setString(4, getString("Geben Sie die Beschreibung ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("Falsche Eingabe bei dem Hinzufügen eines Events getätigt!");
            }
        }
    }

    /**
     * Aktualisiert vorhandene Events in der Datenbank.
     * Erlaubt dem Benutzer, einen oder alle Werte eines Events zu ändern.
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
            	String updateEventQuery;
            	PreparedStatement preparedStatement;
            	switch(scanner.nextLine()) 
            	{
            	case "1":
            		String selection = getSQL();
            		updateEventQuery = "UPDATE Event SET "+selection+" = ? WHERE EventID = ?";
            		preparedStatement = connection.prepareStatement(updateEventQuery);
            		
                	switch(selection) 
                	{
                	case "EventName":
                		preparedStatement.setString(1, getString("Bitte geben Sie einen Neuen Event Namen ein:"));
                		break;
                	case "EventDate":
                		preparedStatement.setString(1, getDate());
                		break;
                	case "EventLocation":
                		preparedStatement.setString(1, getString("Bitte geben Sie einen Neuen Ort ein:"));
                		break;
                	case "EventDescription":
                		preparedStatement.setString(1, getString("Bitte geben Sie einen Neue Beschreibung ein:"));
                		break;
                	}
                    preparedStatement.setInt(2, getInt("Bitte Geben Sie den EventID ein, wo Sie gerne eine Veränderung aufführen möchten."));
                    preparedStatement.executeUpdate();
                    Show();
            		break;
            	case "2":
            		nextLine();
                    updateEventQuery = "UPDATE Event SET EventName = ?, EventDate = ?, EventLocation = ?, EventDescription = ? WHERE EventID = ?";
                    preparedStatement = connection.prepareStatement(updateEventQuery);
                    preparedStatement.setString(1, getString("Bitte geben Sie einen Neuen Event Namen ein:"));
                    preparedStatement.setString(2, getDate());
                    preparedStatement.setString(3, getString("Bitte geben Sie einen Neuen Ort ein:"));
                    preparedStatement.setString(4, getString("Bitte geben Sie einen Neue Beschreibung ein:"));
                    preparedStatement.setInt(5, getInt("Bitte Geben Sie den EventID ein, wo Sie gerne eine Veränderung aufführen möchten."));
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
     * Löscht ein Event aus der Datenbank.
     * Der Benutzer wird aufgefordert, die EventID des zu löschenden Events einzugeben.
     */
    @Override
    public void Delete()
    {
        if(connection != null)
        {
            try
            {
            	Show();
                String deleteEventQuery = "DELETE FROM Event WHERE EventID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteEventQuery); 
                preparedStatement.setInt(1, getInt("Geben Sie die EventID ein:"));
                preparedStatement.executeUpdate();
                Show();
            }
            catch (SQLException e) 
            {
            	println("EventID nicht gefunden!");
            }
        }
    }
    
    /**
     * Zeigt alle Events in der Datenbank an.
     * Verwendet eine SQL-Abfrage, um alle Spalten und Zeilen aus der Event-Tabelle abzurufen und anzuzeigen.
     */
    @Override
    public void Show() 
    {
        try
        {
        	nextLine();
        	rowCounter = 0;
            String selectQuery = "SELECT * FROM Event";
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
        println("Auswahl: Name, Datum, Standort, Beschreibung");
        println("Zum Beenden 'Exit' Eingeben");
        while(true) 
        {
            String selectedValue = scanner.nextLine();
            if(!selectedValue.equals("Name") && !selectedValue.equals("Datum") && !selectedValue.equals("Standort") && !selectedValue.equals("Beschreibung"))
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
            	case "Name":
            		sql = "EventName";
            		break;
            	case "Datum":
            		sql = "EventDate";
            		break;
            	case "Standort":
            		sql = "EventLocation";
            		break;
            	case "Beschreibung":
            		sql = "EventDescription";
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
