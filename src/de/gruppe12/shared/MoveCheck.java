/** MoveCheck
 * 
 * Prï¿½ft ob der Zug korrekt war
 * 
 * @author Julian Kipka
 * @version 1.0
 */
package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;

public class MoveCheck {
	
	//TODO: Funktionstest ob die Funktionen richtig funktionieren
	
	/** check
	 * 
	 * PrÃ¼ft zunÃ¤cht ob die Zugrichtung korrekt war.
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	public static Boolean check (Move move, Board board){ 
		
			
		if(
				checkMoveDirection(move, board)&&
				checkForFortress(move, board)&&
				checkBoardContent(move)&&
				checkFreeWay(move, board)&&				
				checkInBoard(move, board)				
		) return true;
		else return false;
	}
	
	/**checkFreeWay
	 * 
	 * PrÃ¼ft ob nicht Ã¼ber andere Personen gegangen wird oder Felder die Inavlid sind
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
				for(int i = move.getFromCell().getCol(); i>=move.getToCell().getCol(); i--){
					if(board.get()[i][move.getToCell().getRow()]!=BoardContent.EMPTY) return false;
				}
				
				return true;
			} else {
				
				/* von links nach rechts */
				for(int i = move.getFromCell().getCol(); i<=move.getToCell().getCol(); i++){
					if(board.get()[i][move.getToCell().getRow()]!=BoardContent.EMPTY) return false;
				}
				
				return true;
			}
			
			
		} else {
			
			if(move.getFromCell().getRow()>move.getToCell().getRow()){
				
				/* von  unten nach oben */
				for(int i = move.getFromCell().getRow(); i>=move.getToCell().getRow(); i--){
					if(board.get()[move.getToCell().getCol()][i]!=BoardContent.EMPTY) return false;
				}
				
				return true;
				
			} else {
				
				/* von  oben nach unten */
				for(int i = move.getFromCell().getRow(); i<=move.getToCell().getRow(); i++){
					if(board.get()[move.getToCell().getCol()][i]!=BoardContent.EMPTY) return false;
				}
				
				return true;
				
			}
			
		}
	}
	
	/** checkBoardContent
	 * 
	 * Prüt ob der Boardcontent der Quell-Zelle und der Zielzelle gleich ist.
	 * Sonst Betrugsversuch
	 * 
	 * @param move: Der zu Analysierende Zug
	 * @return
	 */
	private static boolean checkBoardContent(Move move){
		if(move.getFromCell().getContent()!=move.getToCell().getContent()) return false;
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
		if(		move.getFromCell().getCol()>=board.get().length &&
				move.getFromCell().getCol()<0 &&
				
				move.getToCell().getCol()>=board.get().length &&
				move.getToCell().getCol()<0 &&
				
				move.getFromCell().getRow()>=board.get()[0].length &&
				move.getFromCell().getRow()<0 &&
				
				move.getToCell().getRow()>=board.get()[0].length &&
				move.getToCell().getRow()<0)
		return false;
		else return true;
	}
	
	
	/** checkForFortress
	 * 
	 * Prüft ob von oder auf ein INVALID feld gezogen wird
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	private static boolean checkForFortress(Move move, Board board){
		
		/* König darf von daher erst prüfen ob Content King */
		if(move.getFromCell().getContent()!=BoardContent.KING){
			
			/* Test von Quelle und Ziel Move / Test anhand des Boardcontents */
			
			if(
					board.get()[move.getToCell().getCol()][move.getToCell().getRow()]!=BoardContent.INVALID &&
					board.get()[move.getFromCell().getCol()][move.getFromCell().getRow()]!= BoardContent.INVALID
			) return true;
			else return false;
			
		} else return true;
		
	}
	
	private static boolean checkMoveDirection(Move move, Board board){
		/* Teste ob Zugrichtung korrekt */
		if(move.getFromCell().getCol()!=move.getToCell().getCol() &&
				move.getFromCell().getRow()!=move.getToCell().getRow()) 
		return false;
		
		else return true;
	}
	

}
