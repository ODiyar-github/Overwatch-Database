package EntityParent;

import java.util.Scanner;

/**
 * Die Klasse EntityParent ist eine Oberklasse, die grundlegende Funktionen und
 * Eingabeaufforderungen für Klassen bereitstellt, die mit Datenbankentitäten interagieren.
 */
public class EntityParent {
	/**
	 * Scanner-Objekt zum Einlesen von Benutzereingaben
	 */
	public static Scanner scanner = new Scanner(System.in);
	
    /**
     * Gibt den angegebenen Text gefolgt von einer neuen Zeile auf der Konsole aus.
     *
     * @param text Der auszugebende Text.
     */
	public void println(String text) {
		System.out.println(text);
	}
	
    /**
     * Gibt den angegebenen Text auf der Konsole aus.
     *
     * @param text Der auszugebende Text.
     */
	public void print(String text) {
		System.out.print(text);
	}
	
    /**
     * Bewegt den Cursor auf die nächste Zeile auf der Konsole.
     */
	public void nextLine() {
		println("");
	}
	
    /**
     * Fordert den Benutzer auf, Datum und Uhrzeit einzugeben, und gibt das Ergebnis als String zurück.
     *
     * @return Ein String, der das eingegebene Datum und die Uhrzeit repräsentiert.
     */
	public String getDate() {
		try {
			println("Geben Sie das Jahr ein:");
			int year = Integer.parseInt(scanner.nextLine());
			println("Geben Sie den Monat ein:");
			int month = Integer.parseInt(scanner.nextLine());
			println("Geben Sie den Tag ein:");
			int day = Integer.parseInt(scanner.nextLine());
			println("Geben Sie die Uhrzeit(Stunde) ein:");
			int hour = Integer.parseInt(scanner.nextLine());
			println("Geben Sie die Minuten ein:");
			int min = Integer.parseInt(scanner.nextLine());
			return year+"-"+month+"-"+day+" "+hour+":"+min+":"+"00";
		}catch(Exception e) {
			println("Sie habe eine Falsche Eingabe getätig!");
			return null;
		}
	}
	
    /**
     * Fordert den Benutzer auf, die Spielzeit einzugeben, und gibt das Ergebnis als String zurück.
     *
     * @return Ein String, der die eingegebene Spielzeit repräsentiert.
     */
	public String getGameTime() {
		try {
			println("Geben Sie die Spielzeit (Stunden) ein:");
			int hour = Integer.parseInt(scanner.nextLine());
			println("Geben Sie die Spielzeit (Minuten) ein:");
			int min = Integer.parseInt(scanner.nextLine());
			return hour+":"+min+":00";
		}catch(Exception e) {
			println("Sie habe eine Falsche Eingabe getätig!");
			return null;
		}
	}
	
    /**
     * Fordert den Benutzer auf, eine Zeichenkette einzugeben, und gibt die eingegebene Zeichenkette zurück.
     *
     * @param text Die Eingabeaufforderung für den Benutzer.
     * @return Die vom Benutzer eingegebene Zeichenkette.
     */
	public String getString(String text) {
		println(text);
		return scanner.nextLine();
	}
	
    /**
     * Fordert den Benutzer auf, eine ganze Zahl einzugeben, und gibt die eingegebene Zahl zurück.
     *
     * @param text Die Eingabeaufforderung für den Benutzer.
     * @return Die vom Benutzer eingegebene ganze Zahl.
     */
	public int getInt(String text) {
		try {
			println(text);
			return Integer.parseInt(scanner.nextLine());
		}catch(Exception e) {
			println("Sie habe eine Falsche Eingabe getätig!");
			return -1;
		}
	}
}
