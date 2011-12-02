package de.gruppe12.logic;

import java.util.Observable;
import de.gruppe12.ki.*;

import de.gruppe12.shared.*;

public class LogicMain extends Observable {
	
	private Board board;
	private MoveStrategy attacker, defender;
	private Move currentMove, lastMove;
	private boolean waitForMove, gameEnd, defPlayerTurn;
	private int thinkTime;
	private boolean humanAttacker, humanDefender;
	private long sleepTime = 10000;
	private String lastGameLogEvent;
	private String strAttacker = "Angreifer";
	private String strDefender = "Verteidiger";
	
	// Konstruktoren für die unterschiedlichen Spielersituationen
	
	
	public LogicMain(){
		initLogicMain();			
	}
	
	public void humanAttKiDef(MoveStrategy defender, int thinkTime){
		setDefender(defender);
		this.humanDefender = false;
		this.humanAttacker = true;	
		this.thinkTime = thinkTime;
		initLogicMain();
		move();
	}
	
	public void humanDefKiAtt(MoveStrategy attacker, int thinkTime){
		setAttacker(attacker);
		this.humanDefender = true;
		this.humanAttacker = false;	
		this.thinkTime = thinkTime;
		initLogicMain();
		move();
	}
	
	public void humanDefHumanAtt(int thinkTime){
		this.humanDefender = true;
		this.humanAttacker = true;
		this.thinkTime = thinkTime;
		initLogicMain();
		move();
	}
	
	public void KiDefKiAtt(MoveStrategy defender, MoveStrategy attacker, int thinkTime){
		setDefender(defender);
		setAttacker(attacker);
		this.humanDefender = false;
		this.humanAttacker = false;
		this.thinkTime = thinkTime;
		initLogicMain();
		move();
	}
	
	private void initLogicMain(){
		this.board = new Board();
		this.board.init();
		this.waitForMove = false;
		this.currentMove = null;
		this.lastMove = null;
		this.defPlayerTurn = true;
	}
	
	// Ende der Konstruktoren	
	
	private void setDefender(MoveStrategy player){
		this.defender = player;		
	}
	
	private void setAttacker(MoveStrategy player){
		this.attacker = player;		
	}
	
	public Board getBoard(){
		return this.board;
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
	
	private void logGameEvent(String event){
		if (this.defPlayerTurn){
			GameLog.logGameEvent(strDefender, event);
			this.lastGameLogEvent = strDefender + ": " + event;
		}
		else{
			GameLog.logGameEvent(strAttacker, event);
			this.lastGameLogEvent = strAttacker + ": " + event;
		}
	}
	
	public String getLastGameLogEvent(){
		return this.lastGameLogEvent;
	}
	
	private void move(){
		
		// Wenn der nächste Spieler eine KI ist, läuft die Schleife weiter
		// wenn der nächste Spieler ein Mensch ist, 
		// wird die Schleife beendet und muss erneut von der GUI aufgerufen werden
		
		while ((this.defPlayerTurn && !this.humanDefender) || 
				(!this.defPlayerTurn && !this.humanAttacker)){			
			try {
				Thread.sleep(0);
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
		System.out.println(this.board.toString());
		//Erst prüfen ob der Zug erlaubt ist	
		if (MoveCheck.check(move, this.board, this.defPlayerTurn)) {			
			//Dann prüfen ob Steine geschlagen wurden und neues Bord setzen
			this.board = RemoveCheck.checkForRemove(move, this.board);
			
			//Und anschließend das Event loggen
			logGameEvent("Gezogen von: " + move.getFromCell().getCol() + "," + move.getFromCell().getRow() + 
					" nach: " + move.getToCell().getCol() + "," + move.getToCell().getRow());
			
			// Spieler wechseln
			this.defPlayerTurn = !this.defPlayerTurn;
		}
		else {
			logGameEvent("Ungültig");
			//Wenn KI am Zug ist
			if ((this.defPlayerTurn && !this.humanDefender) ||
						!this.defPlayerTurn && !this.humanAttacker){
				//KI wegen Betruges disqualifizieren	
			}
			//Wenn Mensch am zu ist
			else{
				// Zug ignorieren und eventuell Meldung an KI
			}	
		}		
		System.out.println(this.board.toString());
		//Oberserver benachrichtigen
		setChanged();
		notifyObservers(currentMove);
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

