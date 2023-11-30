import java.sql.Connection;
import java.sql.SQLException;
import Entity.*;

/**
 * Die Klasse DataHandler verwaltet den Zugriff auf verschiedene Datenbankentitäten.
 */
public class DataHandler {
	private Game game;
	private Event event;
	private Tournament tournament;
	private Ranking ranking;
	private Team team;
	private Player player;
	private Hero hero;
	private Map map;
	
	/**
	 * Konstruktor der DataHandler-Klasse. Hier werden Instanzen der verschiedenen Entity-Klassen erstellt.
	 */
	public DataHandler() 
	{
		game = new Game();
		event = new Event();
		tournament = new Tournament();
		ranking = new Ranking();
		team = new Team();
		hero = new Hero();
		map = new Map();
		player = new Player();
	}
	
	/**
	 * Gibt einen Text ohne Zeilenumbruch aus.
	 * @param text Der auszugebende Text.
	 */
	public void print(String text) 
	{
		System.out.print(text);
	}
	
	/**
	 * Gibt einen Text mit Zeilenumbruch aus.
	 * @param text Der auszugebende Text.
	 */
	public void println(String text) 
	{
		System.out.println(text);
	}
	
	/**
	 * Führt einen Zeilenumbruch aus.
	 */
	public void nextLine() {
		println("");
	}
	
	/**
	 * Leert die Konsole durch Ausgabe von Leerzeilen.
	 */
	public void clearConsole() 
	{
		for(int i=0;i<50;i++) {
			System.out.println();
		}
	}
	
	/**
	 * Setzt die Verbindungen zu allen Entity-Klassen mit der übergebenen Connection.
	 * @param con Die Connection zur Datenbank.
	 */
	public void setAllEntityConnection(Connection con) 
	{
		game.SetConnection(con);
		event.SetConnection(con);
		tournament.SetConnection(con);
		ranking.SetConnection(con);
		team.SetConnection(con);
		player.SetConnection(con);
		hero.SetConnection(con);
		map.SetConnection(con);
	}
	
	/**
	 * Gibt alle Ressourcen der Entity-Klassen frei.
	 */
	public void disposeAll()
	{
		try {
			game.Dispose();
			event.Dispose();
			tournament.Dispose();
			ranking.Dispose();
			team.Dispose();
			player.Dispose();
			hero.Dispose();
			map.Dispose();
			println("Alle Ressourcen wurden wieder freigegeben.");
		} catch (SQLException e) 
		{
			println("Dispose war Fehlerhaft: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Zeigt alle Tabellen der verschiedenen Entity-Klassen an.
	 */
	public void showAllTables() 
	{
		game.Show();
		event.Show();
		tournament.Show();
		ranking.Show();
		team.Show();
		player.Show();
		hero.Show();
		map.Show();
	}
	
	/**
	 * Ruft die Methode Show für die ausgewählte Entity-Klasse auf und startet die Benutzerinteraktion.
	 * @param Entity Der Name der ausgewählten Entität.
	 */
	public void printTable(String Entity) 
	{
		switch(Entity) {
			case "Game":
				game.Show();
				game.Start();
				break;
			case "Event":
				event.Show();
				event.Start();
				break;
			case "Tournament":
				tournament.Show();
				tournament.Start();
				break;
			case "Ranking":
				ranking.Show();
				ranking.Start();
				break;
			case "Team":
				team.Show();
				team.Start();
				break;
			case "Player":
				player.Show();
				player.Start();
				break;
			case "Hero":
				hero.Show();
				hero.Start();
				break;
			case "Map":
				map.Show();
				map.Start();
				break;
			default:
				println("Ihre Eingabe wurde nicht erkannt.");
				break;
		}
	}
	
	/**
	 * Gibt eine Liste der verfügbaren Entity-Klassen zurück.
	 * @return Eine Zeichenkette mit den Namen der Entity-Klassen.
	 */
	public String showEntitys() 
	{
		return "Entitäten/Relationen: Game, Event, Tournament, Map, Team, Player, Ranking, Hero";
	}
	
}
