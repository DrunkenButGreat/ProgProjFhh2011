/** MoveCheck
 * 
 * Pr�ft ob der Zug korrekt war
 * 
 * @author Julian Kipka
 * @version 1.0
 */
package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.Move;
import de.gruppe12.logic.GameLog;

public class MoveCheck {
	static final int boardSize = 12;
	
	//TODO: Funktionstest ob die Funktionen richtig funktionieren
	
	/** check
	 * 
	 * Prüft zunächt ob die Zugrichtung korrekt war.
	 * 
	 * @param board
	 * @param currentMove2
	 * @return
	 */
	public static Boolean check (de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board, Boolean isDefTurn){ 	
		if (!checkIsMoving(currentMove2, board)) return false;
		if (!checkInBoard(currentMove2, board)) return false;
		if (!checkCorrectPlayer(currentMove2, board, isDefTurn)) return false;
		if (!checkMoveDirection(currentMove2, board)) return false;
		if (!checkForFortress(currentMove2, board)) return false;
		if (!checkBoardContent(currentMove2, board)) return false;
		if (!checkFreeWay(currentMove2, board)) return false;
	
		GameLog.logDebugEvent("__________Zug erlaubt__________");
		return true;
	}
	
	private static boolean checkIsMoving(Move currentMove2, Board board) {
		if ((currentMove2.getFromCell().getCol() == currentMove2.getToCell().getCol()) && currentMove2.getFromCell().getRow() == currentMove2.getToCell().getRow()){
			GameLog.logDebugEvent("Stein wird nicht bewegt");
			return false;
		}			
		return true;
	}

	private static boolean checkCorrectPlayer(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board, Boolean isDefTurn){
		if (isDefTurn && (currentMove2.getFromCell().getContent() == BoardContent.DEFENDER || 
							currentMove2.getFromCell().getContent() == BoardContent.KING)){
			return true;
		}
		if (!isDefTurn && currentMove2.getFromCell().getContent() == BoardContent.ATTACKER){
			return true;
		}
		GameLog.logDebugEvent("Spielstein vom Gegner gezogen");
		return false;		
	}
	
	/**checkFreeWay
	 * 
	 * Prüft ob nicht über andere Personen gegangen wird oder Felder die Inavlid sind
	 * 
	 * @param board
	 * @param currentMove2
	 * @return
	 */
	private static boolean checkFreeWay(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		boolean isKing = false;
		if (currentMove2.getFromCell().getContent() == BoardContent.KING){
			isKing = true;
		}	
		
		/* Bewegungsrichtung ermitteln */
		if(currentMove2.getFromCell().getCol()!=currentMove2.getToCell().getCol()){
			
			//bewegungsvektor bestimmen
			if(currentMove2.getFromCell().getCol()>currentMove2.getToCell().getCol()){
				
				/* von  rechts nach links */
				for(int i = currentMove2.getFromCell().getCol() - 1; i>=currentMove2.getToCell().getCol(); i--){
						if(!isKing && board.get()[i][currentMove2.getToCell().getRow()]!=BoardContent.EMPTY) {
							GameLog.logDebugEvent("Weg blockiert");
							return false;
						}
						if (isKing && !(board.get()[i][currentMove2.getToCell().getRow()] == BoardContent.EMPTY || board.get()[i][currentMove2.getToCell().getRow()] == BoardContent.INVALID)){
							GameLog.logDebugEvent("Weg blockiert");
							return false;
						}							
					}
								
				return true;
				
			} else {
				
				/* von links nach rechts */
				for(int i = currentMove2.getFromCell().getCol() + 1; i<=currentMove2.getToCell().getCol(); i++){
					if(!isKing && board.get()[i][currentMove2.getToCell().getRow()]!=BoardContent.EMPTY) {
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
					if (isKing && !(board.get()[i][currentMove2.getToCell().getRow()]==BoardContent.EMPTY || board.get()[i][currentMove2.getToCell().getRow()]==BoardContent.INVALID)){
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}		
				}				
				return true;
			}
			
			
		} else {
			
			if(currentMove2.getFromCell().getRow()>currentMove2.getToCell().getRow()){
				
				/* von  unten nach oben */
				for(int i = currentMove2.getFromCell().getRow() - 1; i>=currentMove2.getToCell().getRow(); i--){
					if(!isKing && board.get()[currentMove2.getToCell().getCol()][i]!=BoardContent.EMPTY) {
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
					if (isKing && !(board.get()[currentMove2.getToCell().getCol()][i]==BoardContent.EMPTY || board.get()[currentMove2.getToCell().getCol()][i]==BoardContent.INVALID)){
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}		
				}
				
				return true;
				
			} else {
				
				/* von  oben nach unten */
				for(int i = currentMove2.getFromCell().getRow() + 1; i<=currentMove2.getToCell().getRow(); i++){
					if(!isKing && board.get()[currentMove2.getToCell().getCol()][i]!=BoardContent.EMPTY) {
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
					if (isKing && !(board.get()[currentMove2.getToCell().getCol()][i]==BoardContent.EMPTY || board.get()[currentMove2.getToCell().getCol()][i]==BoardContent.INVALID)){
						GameLog.logDebugEvent("Weg blockiert");
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
	 * @param move: Der zu Analysierende Zug
	 * @return
	 */
	private static boolean checkBoardContent(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		if(currentMove2.getFromCell().getContent() != board.getCellBC(currentMove2.getFromCell())){
			GameLog.logDebugEvent("BoardContent stimmt nicht");
			return false;
		}
		if(currentMove2.getFromCell().getContent()!=currentMove2.getToCell().getContent()) {
			GameLog.logDebugEvent("BoardContent nicht gleich");
			return false;
		}		
		else return true;
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
	 * @param currentMove2
	 * @return
	 */
	private static boolean checkInBoard(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		if(		currentMove2.getFromCell().getCol()>boardSize ||
				currentMove2.getFromCell().getCol()<0 ||
				
				currentMove2.getToCell().getCol()>boardSize ||
				currentMove2.getToCell().getCol()<0 ||
				
				currentMove2.getFromCell().getRow()>boardSize ||
				currentMove2.getFromCell().getRow()<0 ||
				
				currentMove2.getToCell().getRow()>boardSize ||
				currentMove2.getToCell().getRow()<0)
		{
			GameLog.logDebugEvent("Move außerhalb vom Board");
			return false;
		}
		
		else return true;
	}
	
	
	/** checkForFortress
	 * 
	 * Pr�ft ob von oder auf ein INVALID feld gezogen wird
	 * 
	 * @param board
	 * @param currentMove2
	 * @return
	 */
	private static boolean checkForFortress(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		
		/* K�nig darf von daher erst pr�fen ob Content King */
		if(currentMove2.getFromCell().getContent()!=BoardContent.KING){
			
			/* Test von Quelle und Ziel Move / Test anhand des Boardcontents */
			
			if(
					board.get()[currentMove2.getToCell().getCol()][currentMove2.getToCell().getRow()]!=BoardContent.INVALID &&
					board.get()[currentMove2.getFromCell().getCol()][currentMove2.getFromCell().getRow()]!= BoardContent.INVALID
			) {
				return true;
			}
			else {
				GameLog.logDebugEvent("Normale Spielfigur in Festung gezogen");
				return false;
			}
				
			
		} else return true;
		
	}
	
	private static boolean checkMoveDirection(de.fhhannover.inform.hnefatafl.vorgaben.Move currentMove2, Board board){
		/* Teste ob Zugrichtung korrekt */
		if(currentMove2.getFromCell().getCol()!=currentMove2.getToCell().getCol() &&
				currentMove2.getFromCell().getRow()!=currentMove2.getToCell().getRow()) {
			GameLog.logDebugEvent("Falsche Bewegungsrichtung");
			return false;
		}
		
		
		else return true;
	}
	

}
