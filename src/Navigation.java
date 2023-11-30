import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 * Die Klasse Navigation dient der Benutzerinteraktion und Steuerung des Datenbankzugriffs.
 * Sie erbt von der Klasse DataHandler.
 */
public class Navigation extends DataHandler{
	
	private Connection connection;
	private Scanner scanner = new Scanner(System.in);
	
	/**
	 * Konstruktor der Navigation-Klasse. Hier wird die Datenbankverbindung hergestellt und die
	 * Benutzerinteraktion gestartet.
	 */
	public Navigation() 
	{
		super();
		if(setConnection()) 
		{
			setAllEntityConnection(connection);
			run();
		}
	}
	
	/**
	 * Versucht, eine Verbindung zur Datenbank herzustellen
	 * @return true, wenn die Verbindung erfolgreich hergestellt wurde, ansonsten false.
	 */
	private boolean setConnection() {
		String url = "";
		String user = "";
		String password = "";
		boolean con = false;
		println("Möchten Sie sich Verbinden?");
		println("1: Ja");
		println("2: Nein");
		boolean loop = true;
		int trys = 0;
		switch(scanner.nextLine()) {
		case "1":
			while(loop) 
			{
				try 
				{
					if(trys < 5) 
					{
						println("Bitte Verbinden Sie sich mit ihrer Datenbank.");
						println("URL eingeben:");
						url = scanner.nextLine();
						println("Username eingeben:");
						user = scanner.nextLine();
						println("Passwort eingeben:");
						password = scanner.nextLine();
						connection = DriverManager.getConnection(url, user, password);
						println("Verbindung wurde erfolgreich hergestellt");
						loop = false;
						con = true;
						break;
					}
					else 
					{
						loop = false;
						con = false;
						println("Zu viele anfragen versucht!");
					}
				}
				catch(Exception e) 
				{
					trys++;
					println("Verbindung fehlgeschlagen!");
					con = false;
				}
			}
		default:
			println("Programm Beendet!");
			con = false;
			break;
		}
		return con;
	}
	
	/**
	 * Startet die Benutzerinteraktion und ermöglicht das Navigieren durch Tabellen und das Anzeigen
	 * von Informationen.
	 */
	public void run() {
        boolean loop = true;
        while(loop) 
        {
            println("----------------------------------");
            println("1: Zeige mir die einzelen Relationen an");
            println("2: Zeige Alle Tabellen an");
            println("3: Konsole Löschen");
            println("4: Exit");
            println("----------------------------------");
            nextLine();
            
            switch(scanner.nextLine())
            {
            	case "1" -> {
            		println("Navigieren durch die Tabelle.");
            		println(showEntitys());
            		println("Welche der Relationen möchten Sie Betrachten?");
            		nextLine();
            		String selected = scanner.nextLine();
        			printTable(selected);
        			break;
            	}

            	case "2" -> {
            		println("-------------------------------------");
            		showAllTables();
            		println("-------------------------------------");
            		break;
            	}
            	case "3" ->{
            		clearConsole();
            	}
            	case "4" -> {
            		disposeAll();
            		println("Das Programm wurde Erfolgreich Beendet!");
            		loop = false;
            		break;
            	}

            	default -> {
            		println("Eingabe nicht Erkannt!");
            		break;
            	}
            }
        }
    }
	
}
