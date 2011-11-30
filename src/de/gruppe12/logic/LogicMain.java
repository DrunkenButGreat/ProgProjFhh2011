package de.gruppe12.logic;

import java.util.Observable;
import de.gruppe12.ki.*;

import de.gruppe12.shared.*;

public class LogicMain extends Observable {
	
	private GameLog logger;
	private Board board;
	private MoveStrategy attacker, defender;
	private Move currentMove, lastMove;
	private boolean waitForMove, gameEnd, defPlayerTurn;
	private int thinkTime;
	private boolean humanAttacker, humanDefender;
	private long sleepTime = 10000;
	
	// Konstruktoren für die unterschiedlichen Spielersituationen
	
		
	public LogicMain (MoveStrategy attacker, MoveStrategy defender, int thinkTime){
		setDefender(defender);
		setAttacker(attacker);
		this.humanDefender = false;
		this.humanAttacker = false;
		initLogicMain(thinkTime);
	}
	
	public LogicMain (boolean humanAttacker, MoveStrategy defender, int thinkTime){
		setDefender(defender);
		this.humanDefender = false;
		this.humanAttacker = true;			
		initLogicMain(thinkTime);		
	}
	
	public LogicMain (MoveStrategy attacker, boolean humanDefender, int thinkTime){
		setAttacker(attacker);
		this.humanDefender = true;
		this.humanAttacker = false;		
		initLogicMain(thinkTime);
	}
	
	public LogicMain (boolean humanAttacker, boolean humanDefender, int thinkTime){
		this.humanDefender = true;
		this.humanAttacker = true;
		initLogicMain(thinkTime);
	}
	
	private void initLogicMain(int thinkTime){
		this.board = new Board();
		this.board.init();
		
		this.waitForMove = false;
		this.thinkTime=thinkTime;
		
		this.currentMove = null;
		this.lastMove = null;
		
		this.defPlayerTurn = true;
		
		move();
	}
	
	// Ende der Konstruktoren	
	
	public void init(){
		//Alles auf Anfang
		this.board.init();
		this.currentMove = null;
		this.lastMove = null;
		this.waitForMove = false;		
	}
	
	private void setDefender(MoveStrategy player){
		this.defender = player;		
	}
	
	private void setAttacker(MoveStrategy player){
		this.attacker = player;		
	}
	
	public Board getBoard(){
		return this.board;
	}	
	
	public String getLog() {
		//TODO: logger to String Funktion schreiben
		return this.logger.toString();
	}
		
	void finish(boolean player){
		
	}	
	
	public void move(Move move){	
		update(move);	
		saveCurrentMove(move);
		move();
	}
	
	private void saveCurrentMove(Move move){
		this.lastMove = this.currentMove;
		this.currentMove = move;
	}
	
	private void move(){
		
		// Wenn der nächste Spieler eine KI ist, läuft die Schleife weiter
		// wenn der nächste Spieler ein Mensch ist, 
		// wird die Schleife beendet und muss erneut von der GUI aufgerufen werden
		
		while ((this.defPlayerTurn && !this.humanDefender) || 
				(!this.defPlayerTurn && !this.humanAttacker)){
			
			try {
				Thread.sleep(this.sleepTime);
			} catch (InterruptedException e) {}
			
			// Wenn die Verteidiger KI am Zug ist
			if (this.defPlayerTurn && !this.humanDefender){	
				saveCurrentMove(defender.calculateDefenderMove(this.lastMove, this.thinkTime));
				update(this.currentMove);
			}
			// Wenn die Angreifer KI am Zug ist
			else if (!this.defPlayerTurn && !this.humanAttacker){
				saveCurrentMove(attacker.calculateAttackerMove(this.lastMove, this.thinkTime));
				update(this.currentMove);	
			}
		}	
	}
	
	private void update(Move move){
		String strPlayer;
		if (this.defPlayerTurn){
			strPlayer = "Verteidiger";
		}
		else {
			strPlayer = "Angreifer";
		}
		
		// Spieler wechseln
		this.defPlayerTurn = !this.defPlayerTurn;
		
		//Erst prüfen ob der Zug erlaubt ist
		
		if (MoveCheck.check(move, this.board)) {
			
			//Dann prüfen ob Steine geschlagen wurden und neues Bord setzen
			this.board = RemoveCheck.checkForRemove(move, this.board);
			
			//Und anschließend das Event loggen
			GameLog.logGameEvent(strPlayer, 
					"Gezogen von: " + move.getFromCell().getCol() + ";" + move.getFromCell().getRow() + 
					" nach: " + move.getToCell().getCol() + ";" + move.getToCell().getRow()
					);
		}
		else {
			//KI wegen Betruges disqualifizieren
			
		}
		
		//Oberserver benachrichtigen
		setChanged();
		notifyObservers();
		
	}
}

