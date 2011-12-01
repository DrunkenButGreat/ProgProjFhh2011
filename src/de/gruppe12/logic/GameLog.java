package de.gruppe12.logic;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLog {
	static private FileHandler fileTxt;	
	static private Logger logger;
	
	static void init(String fileName) throws IOException {
		logger = Logger.getLogger("");
		logger.setLevel(Level.INFO);
		fileTxt = new FileHandler(fileName);
		logger.addHandler(fileTxt);
	}	
	
	static void logGameEvent(String player, String event){
		logger.info(player + ": " + event);
	}
	
	static void logDebugEvent(String event){
		logger.info(event);
	}
}
	
	

