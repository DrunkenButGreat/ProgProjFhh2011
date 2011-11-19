package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.Move;

public abstract class MoveCheck {
	
	BoardContent[][] board;
	Move move;
	
	/** check
	 * 
	 * Prüft zunächt ob die Zugrichtung korrekt war.
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	public Boolean check (Board board, Move move){ 
		this.board = board.get();
		this.move = move;
		
		/* Testen on der Zug auf den Feld statt fand
		 * 
		 * Erst ob die Reihen auch den Reihen des Board ensprechen. 
		 * Also zwischen 0-12 (Regelfall) liegen und danach das selbe
		 * mit den Spalten
		 */
		
		if(move.getFromCell().getCol()>=board.get().length &&
				move.getFromCell().getCol()<0 && 
				move.getToCell().getCol()>=board.get().length &&
				move.getToCell().getCol()<0) return false;
		//TODO: Das slebe für Spalten / Meine 6 auf der NB Tastatur ist abgefallen :D
		
		/* Teste ob Zugrichtung korrekt */
		if(move.getFromCell().getCol()!=move.getToCell().getCol() &&
				move.getFromCell().getRow()!=move.getToCell().getRow()) 
			return false;
		else return checkFreeWay(board,move);
	}
	
	/**checkFreeWay
	 * 
	 * Prüft ob nicht über andere Personen gegangen wird oder Felder die Inavlid sind
	 * 
	 * @param board
	 * @param move
	 * @return
	 */
	private boolean checkFreeWay(Board board, Move move){
		return true;
	}

}
