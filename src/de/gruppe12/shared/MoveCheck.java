/** MoveCheck
 * 
 * Prueft ob der Zug korrekt war
 * 
 * Copyright: (c) 2011 <p>
 * Company: Gruppe 12 <p>
 * @author Julian Kipka & Lennart Henke
 * @version 2011.12.10
 */
package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.Move;
import de.gruppe12.logic.GameLog;

public class MoveCheck {
	static final int boardSize = 12;
	static boolean gamelog=true;
	
	
	/** check
	 * 
	 * Prüft zunächt ob die Zugrichtung korrekt war.
	 * 
	 * @param board
	 * @param currentMove
	 * @return
	 */
	public static Boolean check (de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board, Boolean isDefTurn){ 	
		if (!checkIsMoving(currentMove, board)) return false;
		if (!checkInBoard(currentMove, board)) return false;
		if (!checkCorrectPlayer(currentMove, board, isDefTurn)) return false;
		if (!checkMoveDirection(currentMove, board)) return false;
		if (!checkForFortress(currentMove, board)) return false;
		if (!checkBoardContent(currentMove, board)) return false;
		if (!checkFreeWay(currentMove, board)) return false;
	
		if(gamelog) GameLog.logDebugEvent("__________Zug erlaubt__________");
		gamelog=true;
		return true;
	}
	
	public static Boolean check (de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board, Boolean isDefTurn,boolean gameLog){
		gamelog = gameLog;
		return check (currentMove,board, isDefTurn);
	}
	
	/** checkIsMoving
	 * 
	 * Pr�ft ob ein Stein wirklich bewegt wurde
	 * 
	 * @param currentMove : Der Aktuelle Zug
	 * @param board : Das aktuelle Board
	 * @return : true/false
	 */
	private static boolean checkIsMoving(Move currentMove, Board board) {
		if ((currentMove.getFromCell().getCol() == currentMove.getToCell().getCol()) && currentMove.getFromCell().getRow() == currentMove.getToCell().getRow()){
			if(gamelog) GameLog.logDebugEvent("Stein wird nicht bewegt");
			return false;
		}			
		return true;
	}
	
	
	/** checkCorrectPlayer
	 * 
	 * @param currentMove : Aktueller Zug
	 * @param board : Aktuelles Board
	 * @param isDefTurn : Ist es ein Defender Zug?
	 * @return : true/false
	 */
	private static boolean checkCorrectPlayer(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board, Boolean isDefTurn){
		if (isDefTurn && (currentMove.getFromCell().getContent() == BoardContent.DEFENDER || 
							currentMove.getFromCell().getContent() == BoardContent.KING)){
			return true;
		}
		if (!isDefTurn && currentMove.getFromCell().getContent() == BoardContent.ATTACKER){
			return true;
		}
		if(gamelog) GameLog.logDebugEvent("Spielstein vom Gegner gezogen");
		return false;		
	}
	
	/**checkFreeWay
	 * 
	 * Prüft ob nicht über andere Personen gegangen wird oder Felder die Inavlid sind
	 * 
	 * @param board
	 * @param currentMove
	 * @return : true/false
	 */
	private static boolean checkFreeWay(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board){
		boolean isKing = false;
		if (currentMove.getFromCell().getContent() == BoardContent.KING){
			isKing = true;
		}	
		
		/* Bewegungsrichtung ermitteln */
		if(currentMove.getFromCell().getCol()!=currentMove.getToCell().getCol()){
			
			//bewegungsvektor bestimmen
			if(currentMove.getFromCell().getCol()>currentMove.getToCell().getCol()){
				
				/* von  rechts nach links */
				for(int i = currentMove.getFromCell().getCol() - 1; i>=currentMove.getToCell().getCol(); i--){
						if(!isKing && board.get()[i][currentMove.getToCell().getRow()]!=BoardContent.EMPTY) {
							if(gamelog) GameLog.logDebugEvent("Weg blockiert");
							return false;
						}
						if (isKing && !(board.get()[i][currentMove.getToCell().getRow()] == BoardContent.EMPTY || board.get()[i][currentMove.getToCell().getRow()] == BoardContent.INVALID)){
							if(gamelog) GameLog.logDebugEvent("Weg blockiert");
							return false;
						}							
					}
								
				return true;
				
			} else {
				
				/* von links nach rechts */
				for(int i = currentMove.getFromCell().getCol() + 1; i<=currentMove.getToCell().getCol(); i++){
					if(!isKing && board.get()[i][currentMove.getToCell().getRow()]!=BoardContent.EMPTY) {
						if(gamelog) GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
					if (isKing && !(board.get()[i][currentMove.getToCell().getRow()]==BoardContent.EMPTY || board.get()[i][currentMove.getToCell().getRow()]==BoardContent.INVALID)){
						if(gamelog) GameLog.logDebugEvent("Weg blockiert");
						return false;
					}		
				}				
				return true;
			}
			
			
		} else {
			
			if(currentMove.getFromCell().getRow()>currentMove.getToCell().getRow()){
				
				/* von  unten nach oben */
				for(int i = currentMove.getFromCell().getRow() - 1; i>=currentMove.getToCell().getRow(); i--){
					if(!isKing && board.get()[currentMove.getToCell().getCol()][i]!=BoardContent.EMPTY) {
						if(gamelog) GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
					if (isKing && !(board.get()[currentMove.getToCell().getCol()][i]==BoardContent.EMPTY || board.get()[currentMove.getToCell().getCol()][i]==BoardContent.INVALID)){
						if(gamelog) GameLog.logDebugEvent("Weg blockiert");
						return false;
					}		
				}
				
				return true;
				
			} else {
				
				/* von  oben nach unten */
				for(int i = currentMove.getFromCell().getRow() + 1; i<=currentMove.getToCell().getRow(); i++){
					if(!isKing && board.get()[currentMove.getToCell().getCol()][i]!=BoardContent.EMPTY) {
						if(gamelog) GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
					if (isKing && !(board.get()[currentMove.getToCell().getCol()][i]==BoardContent.EMPTY || board.get()[currentMove.getToCell().getCol()][i]==BoardContent.INVALID)){
						if(gamelog) GameLog.logDebugEvent("Weg blockiert");
						return false;
					}		
				}
				
				return true;				
			}			
		}
	}
	
	/** checkBoardContent
	 * 
	 * Pr�t ob der Boardcontent der Quell-Zelle und der Zielzelle gleich ist.
	 * Sonst Betrugsversuch
	 * 
	 * UPDATE: Da scheinbar der BoardContent in den Moves egals ist. Auf minimale Pr�fung ge�ndert.
	 * 
	 * @param move: Der zu Analysierende Zug
	 * @return : true/false
	 */
	private static boolean checkBoardContent(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board){
		
		/* Pruefe ob das Zielfeld frei ist. */
		if((board.getCellBC(currentMove.getToCell())!=BoardContent.EMPTY) &&
				(board.getCellBC(currentMove.getToCell()) != BoardContent.INVALID)){
			if(gamelog) GameLog.logDebugEvent("BoardContent des Zieles nicht Leer");
			return false;
		}
		
		/* Pruefe ob das Quellfeld nicht frei ist */
		if(board.getCellBC(currentMove.getFromCell())==BoardContent.EMPTY){
			if(gamelog) GameLog.logDebugEvent("Boardcontent der Quelle nicht leer");
			return false;
		}
		
		return true;
	}
	
	/** checkInBoard
	 * 
	 * Testen on der Zug auf den Feld statt fand
	 * 
	 * Erst ob die Reihen auch den Reihen des Board ensprechen. 
	 * Also zwischen 0-12 (Regelfall) liegen und danach das selbe
	 * mit den Spalten
	 * 
	 * @param board
	 * @param currentMove
	 * @return : true/false
	 */
	private static boolean checkInBoard(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board){
		if(		currentMove.getFromCell().getCol()>boardSize ||
				currentMove.getFromCell().getCol()<0 ||
				
				currentMove.getToCell().getCol()>boardSize ||
				currentMove.getToCell().getCol()<0 ||
				
				currentMove.getFromCell().getRow()>boardSize ||
				currentMove.getFromCell().getRow()<0 ||
				
				currentMove.getToCell().getRow()>boardSize ||
				currentMove.getToCell().getRow()<0)
		{
			if(gamelog) GameLog.logDebugEvent("Move außerhalb vom Board");
			return false;
		}
		
		else return true;
	}
	
	
	/** checkForFortress
	 * 
	 * Pr�ft ob von oder auf ein INVALID feld gezogen wird
	 * 
	 * @param board
	 * @param currentMove
	 * @return : true/false
	 */
	private static boolean checkForFortress(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board){
		
		/* K�nig darf von daher erst pr�fen ob Content King */
		if(currentMove.getFromCell().getContent()!=BoardContent.KING){
			
			/* Test von Quelle und Ziel Move / Test anhand des Boardcontents */
			
			if(
					board.get()[currentMove.getToCell().getCol()][currentMove.getToCell().getRow()]!=BoardContent.INVALID &&
					board.get()[currentMove.getFromCell().getCol()][currentMove.getFromCell().getRow()]!= BoardContent.INVALID
			) {
				return true;
			}
			else {
				if(gamelog) GameLog.logDebugEvent("Normale Spielfigur in Festung gezogen");
				return false;
			}
				
			
		} else return true;
		
	}
	
	/** checkMoveDirection
	 * 
	 * @param currentMove : Auktueller Zug
	 * @param board : Aktuelles Board
	 * @return true/flase
	 */
	private static boolean checkMoveDirection(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove, Board board){
		/* Teste ob Zugrichtung korrekt */
		if(currentMove.getFromCell().getCol()!=currentMove.getToCell().getCol() &&
				currentMove.getFromCell().getRow()!=currentMove.getToCell().getRow()) {
			if(gamelog) GameLog.logDebugEvent("Falsche Bewegungsrichtung");
			return false;
		}
		
		
		else return true;
	}
	

}
