/** MoveCheck
 * 
 * Pr�ft ob der Zug korrekt war
 * 
 * @author Julian Kipka
 * @version 1.0
 */
package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.gruppe12.logic.GameLog;

public class MoveCheck {
	static final int boardSize = 12;
	
	//TODO: Funktionstest ob die Funktionen richtig funktionieren
	
	/** check
	 * 
	 * Prüft zunächt ob die Zugrichtung korrekt war.
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	public static Boolean check (Move move, Board board, Boolean isDefTurn){ 			
		if (!checkInBoard(move, board)) return false;
		if (!checkCorrectPlayer(move, board, isDefTurn)) return false;
		if (!checkMoveDirection(move, board)) return false;
		if (!checkForFortress(move, board)) return false;
		if (!checkBoardContent(move, board)) return false;
		if (!checkFreeWay(move, board)) return false;
	
		return true;
	}
	
	private static boolean checkCorrectPlayer(Move move, Board board, Boolean isDefTurn){
		if (isDefTurn && move.getFromCell().getContent() != BoardContent.DEFENDER){
			GameLog.logDebugEvent("Spielstein vom Gegner gezogen");
			return false;
		}
		if (!isDefTurn && move.getFromCell().getContent() != BoardContent.ATTACKER){
			GameLog.logDebugEvent("Spielstein vom Gegner gezogen");
			return false;
		}
		return true;		
	}
	
	/**checkFreeWay
	 * 
	 * Prüft ob nicht über andere Personen gegangen wird oder Felder die Inavlid sind
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	private static boolean checkFreeWay(Move move, Board board){
		/* Bewegungsrichtung ermitteln */
		if(move.getFromCell().getCol()!=move.getToCell().getCol()){
			
			//bewegungsvektor bestimmen
			if(move.getFromCell().getCol()>move.getToCell().getCol()){
				
				/* von  rechts nach links */
				for(int i = move.getFromCell().getCol() - 1; i>=move.getToCell().getCol(); i--){
					if(board.get()[i][move.getToCell().getRow()]!=BoardContent.EMPTY) {
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
				}
				
				return true;
			} else {
				
				/* von links nach rechts */
				for(int i = move.getFromCell().getCol() + 1; i<=move.getToCell().getCol(); i++){
					if(board.get()[i][move.getToCell().getRow()]!=BoardContent.EMPTY) {
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
				}
				
				return true;
			}
			
			
		} else {
			
			if(move.getFromCell().getRow()>move.getToCell().getRow()){
				
				/* von  unten nach oben */
				for(int i = move.getFromCell().getRow() - 1; i>=move.getToCell().getRow(); i--){
					if(board.get()[move.getToCell().getCol()][i]!=BoardContent.EMPTY) {
						GameLog.logDebugEvent("Weg blockiert");
						return false;
					}
				}
				
				return true;
				
			} else {
				
				/* von  oben nach unten */
				for(int i = move.getFromCell().getRow() + 1; i<=move.getToCell().getRow(); i++){
					if(board.get()[move.getToCell().getCol()][i]!=BoardContent.EMPTY) {
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
	private static boolean checkBoardContent(Move move, Board board){
		if(move.getFromCell().getContent() != board.getCellBC(move.getFromCell())){
			GameLog.logDebugEvent("BoardContent stimmt nicht");
			return false;
		}
		if(move.getFromCell().getContent()!=move.getToCell().getContent()) {
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
	 * @param move
	 * @return
	 */
	private static boolean checkInBoard(Move move, Board board){
		if(		move.getFromCell().getCol()>= boardSize ||
				move.getFromCell().getCol()<0 ||
				
				move.getToCell().getCol()>=boardSize ||
				move.getToCell().getCol()<0 ||
				
				move.getFromCell().getRow()>=boardSize ||
				move.getFromCell().getRow()<0 ||
				
				move.getToCell().getRow()>=boardSize ||
				move.getToCell().getRow()<0)
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
	 * @param move
	 * @return
	 */
	private static boolean checkForFortress(Move move, Board board){
		
		/* K�nig darf von daher erst pr�fen ob Content King */
		if(move.getFromCell().getContent()!=BoardContent.KING){
			
			/* Test von Quelle und Ziel Move / Test anhand des Boardcontents */
			
			if(
					board.get()[move.getToCell().getCol()][move.getToCell().getRow()]!=BoardContent.INVALID &&
					board.get()[move.getFromCell().getCol()][move.getFromCell().getRow()]!= BoardContent.INVALID
			) {
				return true;
			}
			else {
				GameLog.logDebugEvent("Normale Spielfigur in Festung gezogen");
				return false;
			}
				
			
		} else return true;
		
	}
	
	private static boolean checkMoveDirection(Move move, Board board){
		/* Teste ob Zugrichtung korrekt */
		if(move.getFromCell().getCol()!=move.getToCell().getCol() &&
				move.getFromCell().getRow()!=move.getToCell().getRow()) {
			GameLog.logDebugEvent("Falsche Bewegungsrichtung");
			return false;
		}
		
		
		else return true;
	}
	

}
