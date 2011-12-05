package de.gruppe12.logic;

import java.util.Observable;
import de.gruppe12.shared.*;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;

public class LogicMain extends Observable {
	
	private Board board;
	private MoveStrategy attacker, defender;
	de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove;
	private de.fhhannover.inform.hnefatafl.vorgaben.Move lastMove;
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
		this.defPlayerTurn = false;
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
	
	public boolean getDefPlayerTurn(){
		return this.defPlayerTurn;
	}	
	
	public void move(Move move){	
		update(move);
		move();
	}
	
	private void saveCurrentMove(de.fhhannover.inform.hnefatafl.vorgaben.Move move){
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
				update(defender.calculateDefenderMove(this.lastMove, this.thinkTime));
			}
			// Wenn die Angreifer KI am Zug ist
			else if (!this.defPlayerTurn && !this.humanAttacker){
				update(attacker.calculateAttackerMove(this.lastMove, this.thinkTime));	
			}
		}	
	}
	
	private void update(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2){	
		//TODO: Das hier sauber machen und prüfen.. Aber erstmal läufts
		if (currentMove2 == null){
			GameLog.logDebugEvent("null Move erhalten");
			return;
		}
		
		//Erst prüfen ob der Zug erlaubt ist	
		if (MoveCheck.check(currentMove2, this.board, this.defPlayerTurn)) {	
			//Dann den letzen Move speichern
			saveCurrentMove(currentMove2);
			
			//Dann prüfen ob Steine geschlagen wurden und neues Bord setzen
			this.board = RemoveCheck.checkForRemove(currentMove2, this.board);
			
			//Und anschließend das Event loggen
			logGameEvent("Gezogen von: " + currentMove2.getFromCell().getCol() + "," + currentMove2.getFromCell().getRow() + 
					" nach: " + currentMove2.getToCell().getCol() + "," + currentMove2.getToCell().getRow());
			
			// Spieler wechseln
			this.defPlayerTurn = !this.defPlayerTurn;
			
			//Oberserver benachrichtigen
			setChanged();
			notifyObservers(currentMove2);
		}
		else {			
			//Wenn KI am Zug ist
			if ((this.defPlayerTurn && !this.humanDefender) ||
						!this.defPlayerTurn && !this.humanAttacker)
			{
				logGameEvent("Ungültiger Zug der KI");
				logGameEvent(currentMove2.toString());	
				
				// Spieler wechseln
				this.defPlayerTurn = !this.defPlayerTurn;
				return;
			}
			//Wenn Mensch am zu ist
			else{
				logGameEvent("Ungültiger Zug vom Menschen");
				logGameEvent(currentMove2.toString());	
				return;
			}	
		}		
		System.out.println(this.board.toString());
		
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

