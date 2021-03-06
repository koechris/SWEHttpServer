package de.general;

public class Log {

	public static void log (String string) {
		System.out.println ( string );
	}

	public static void info (String string) {
		log ( string );
	}

	public static void errorLog (String string) {
		log ( string );
	}

	public static void errorLog (Exception e) {
		e.printStackTrace();
	}

	public static void debugLog (String string) {
		log ( string );
	}
}
