/** MoveCheck
 * 
 * Pr�ft ob der Zug korrekt war
 * 
 * @author Julian Kipka
 * @version 0.1
 */
package de.gruppe12.shared;

public class MoveCheck {
	
	/** check
	 * 
	 * Prüft zunächt ob die Zugrichtung korrekt war.
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	public static Boolean check (Board board, Move move){ 
		
			
		if(
				checkMoveDirection(board,move)&&
				checkForFortress(board, move)&&
				checkBoardContent(move)&&
				checkFreeWay(board,move)&&				
				checkInBoard(board, move)				
		) return true;
		else return false;
	}
	
	/**checkFreeWay
	 * 
	 * Prüft ob nicht über andere Personen gegangen wird oder Felder die Inavlid sind
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	private static boolean checkFreeWay(Board board, Move move){
		return true;
	}
	
	/** checkBoardContent
	 * 
	 * Pr�t ob der Boardcontent der Quell-Zelle und der Zielzelle gleich ist.
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
	private static boolean checkInBoard(Board board, Move move){
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
	
	private static boolean checkForFortress(Board board, Move move){
		return true;
	}
	
	private static boolean checkMoveDirection(Board board, Move move){
		/* Teste ob Zugrichtung korrekt */
		if(move.getFromCell().getCol()!=move.getToCell().getCol() &&
				move.getFromCell().getRow()!=move.getToCell().getRow()) 
		return false;
		
		else return true;
	}

}
