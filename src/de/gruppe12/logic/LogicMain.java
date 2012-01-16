package de.gruppe12.logic;

import java.util.Observable;
import de.gruppe12.shared.*;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;

public class LogicMain extends Observable {
	
	private Board board;
	private MoveStrategy attacker, defender;
	de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove;
	private boolean gameEnd, gameLog, commandLine, defPlayerTurn;
	private int thinkTime;
	private boolean humanAttacker, humanDefender;
	private String lastGameLogEvent;
	private String strAttacker = "Angreifer";
	private String strDefender = "Verteidiger";
	
	// Konstruktoren für die unterschiedlichen Spielersituationen
	
	
	public LogicMain(){
		initLogicMain();
		gameLog = true;
		commandLine = true;
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
		this.currentMove = null;
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
		this.currentMove = move;
	}
	
	
	/**
	 * Loggt auftretende Events für die GUI
	 * @param event Zu loggendes Event
	 */
	
	private void logGameEvent(String event){
		if (this.defPlayerTurn){
			GameLog.logGameEvent(this.strDefender, event);
			this.lastGameLogEvent = this.strDefender + ": " + event;
		}
		else{
			GameLog.logGameEvent(this.strAttacker, event);
			this.lastGameLogEvent = this.strAttacker + ": " + event;
		}
	}
	
	/**
	 * Liest das letzte GameLog Event aus
	 * @return Letztes GameLog Event
	 */
	
	public String getLastGameLogEvent(){
		return this.lastGameLogEvent;
	}
	
	/**
	 * Move-Schleife für die KI. Schleife läuft so lange, wie eine KI am Zug ist.
	 */
	private void move(){
		
		// Wenn der nächste Spieler eine KI ist, läuft die Schleife weiter
		// wenn der nächste Spieler ein Mensch ist, 
		// wird die Schleife beendet und muss erneut von der GUI aufgerufen werden
		
		while ((this.defPlayerTurn && !this.humanDefender) || 
				(!this.defPlayerTurn && !this.humanAttacker)){	
			if (this.gameEnd) return;
			if (this.board.isFinished()) return;
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {}
			
			// Wenn die Verteidiger KI am Zug ist
			if (this.defPlayerTurn && !this.humanDefender){	
				update(defender.calculateDefenderMove(this.currentMove, this.thinkTime));
			}
			// Wenn die Angreifer KI am Zug ist
			else if (!this.defPlayerTurn && !this.humanAttacker){
				update(attacker.calculateAttackerMove(this.currentMove, this.thinkTime));	
			}
		}	
	}
	
	/**
	 * Nimmt einen Move entgegend. Dieser wird geprüft und ggf. ausgeführt. Nach der Ausführung wird die
	 * GUI benachrichtigt.
	 * @param move Move der ausgeführt werden soll
	 */
	
	private void update(de.fhhannover.inform.hnefatafl.vorgaben.Move move){	
		//TODO: Das hier sauber machen und prüfen.. Aber erstmal läufts
		if (move == null){
			if (this.gameLog) GameLog.logDebugEvent("null Move erhalten");
			return;
		}
		
		//Erst prüfen ob der Zug erlaubt ist	
		if (MoveCheck.check(move, this.board, this.defPlayerTurn)) {	
			//Dann den letzen Move speichern
			saveCurrentMove(move);
			
			//Dann prüfen ob Steine geschlagen wurden und neues Board setzen
			this.board = RemoveCheck.checkForRemove(move, this.board);
			
			//Und anschließend das Event loggen
			if (this.gameLog) logGameEvent("Gezogen von: " + move.getFromCell().getCol() + "," + move.getFromCell().getRow() + 
					" nach: " + move.getToCell().getCol() + "," + move.getToCell().getRow());
			
			// Spieler wechseln
			this.defPlayerTurn = !this.defPlayerTurn;
			//Oberserver benachrichtigen
			setChanged();
			notifyObservers(move);
			
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {			
			//Wenn KI am Zug ist
			if ((this.defPlayerTurn && !this.humanDefender) ||
						!this.defPlayerTurn && !this.humanAttacker)
			{
				if (this.gameLog) logGameEvent("Ungültiger Zug der KI");
				if (this.gameLog) logGameEvent(move.toString());	
				
				//### Prototyp von Ende ###				
				
				this.gameEnd = true;
				this.board.setFinish();
				if (this.defPlayerTurn) this.board.setAttackerWon();
				else this.board.setDefenderWon();
				
				setChanged();
				notifyObservers("GameOver");
				
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//### Ende vom Prototyp Ende ###
				
				return;
			}
			//Wenn Mensch am zu ist
			else{
				if (this.gameLog) logGameEvent("Ungültiger Zug vom Menschen");
				if (this.gameLog) logGameEvent(move.toString());	
				return;
			}	
		}		
		if (this.commandLine) System.out.println(this.board.toString());
		
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

