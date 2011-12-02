/** Board
 * 
 * Board-Objekt
 * 
 * @author Julian Kipka
 * @Version 1.0
 */

package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;


public class Board {
	
	final int fields = 13;
	private BoardContent[][] board = new BoardContent[fields][fields];
	private boolean finish;
	
	public Board(){
		init();
	}
	
	/** init
	 * setzt das Spielfeld auf die Anfangspositionen
	 */
	public void init(){
		
		finish = false;
		
		/* Die Positionen setzten die für alle außer dem König tabu sind */
		board[0][0] = BoardContent.INVALID;
		board[0][12] = BoardContent.INVALID;
		board[12][0] = BoardContent.INVALID;
		board[12][12] = BoardContent.INVALID;
		
		/* König setzten */
		board[6][6] = BoardContent.KING;
		
		/* Verteidiger setzten */
		board[6][5] = BoardContent.DEFENDER;
		board[6][4] = BoardContent.DEFENDER;
		board[6][7] = BoardContent.DEFENDER;
		board[6][8] = BoardContent.DEFENDER;
		board[5][6] = BoardContent.DEFENDER;
		board[4][6] = BoardContent.DEFENDER;
		board[7][6] = BoardContent.DEFENDER;
		board[8][6] = BoardContent.DEFENDER;
		board[5][5] = BoardContent.DEFENDER;
		board[7][7] = BoardContent.DEFENDER;
		board[5][7] = BoardContent.DEFENDER;
		board[7][5] = BoardContent.DEFENDER;
		
		/* Angreifer setzten */
		
		for(int i = 4; i<=8; i++){
			board[0][i] 	= BoardContent.ATTACKER;
			board[12][i] 	= BoardContent.ATTACKER;
			board[i][0] 	= BoardContent.ATTACKER;
			board[i][12] 	= BoardContent.ATTACKER;
		}
		
		board[6][1] 	= BoardContent.ATTACKER;
		board[6][11] 	= BoardContent.ATTACKER;
		board[1][6] 	= BoardContent.ATTACKER;
		board[11][6] 	= BoardContent.ATTACKER;
		
		/* Rest leer setzen */
		
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j]==null){
					board[i][j]=BoardContent.EMPTY;
				}
			}
		}
		
	}
	
	/** empty
	 * 
	 * @deprecated
	 * 
	 * Leert das komplette board
	 */
	private void empty(){
		board = new BoardContent[fields][fields];
	}
	
	public BoardContent[][] get(){
		return board;
	}
	
	/** get
	 * @deprecated
	 * 
	 * gibt den Paramenter der Position des Arrays aus
	 * 
	 * @param x: X-Koordinate
	 * @param y: Y-Koordinate
	 * @return: Gibt einen Typ des Boardcontents zurück.
	 */
	public BoardContent get(int x, int y){
		return board[x][y];
	}
	
	/** set
	 * 
	 * @param board Das Board das neu geschrieben werden soll
	 */
	public void set(BoardContent[][] board){
		/* Testen ob das Board welches gesetzt wird auch die selben Dimensionen hat wie
		 * das Ursprüngliche
		 */
		if(board.length==this.board.length && board[0].length==this.board[0].length){
			this.board=board;
		}
	}
	
	/** set
	 * @deprecated
	 * 
	 * @param x: X-Koordinate
	 * @param y: Y-Koordinate
	 * @param bc: Neuer Boardcontent der Celle
	 */
	public void set(int x, int y, BoardContent bc){
		board[x][y] = bc;
	}
	
	/** to String
	 * 
	 * Bildet das Ganze Board als String ab
	 * 
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0;i < board.length; i++){
			for(int j= 0; j < board[i].length; j++){
				switch(board[j][i]){
					case INVALID:
						sb.append("I ");
						break;
					case ATTACKER:
						sb.append("A ");
						break;
					case DEFENDER:
						sb.append("D ");
						break;
					case KING:
						sb.append("K ");
						break;
					case EMPTY:
						sb.append("  ");
						break;
					default:
						break;
				}				
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void setFinish(){
		finish = true;
	}
	
	public boolean isFinished(){
		return finish;
	}
	
	
	/** getCell
	 * 
	 * Es kann eine gesammt Cell abgerufen werden
	 * 
	 * @param x : Spalte des Boards
	 * @param y : Reihe des Boards
	 * @return	�beribt die Zelle mit Inhalt
	 */
	public Cell getCell(int x, int y){
		return new Cell(x,y,board[x][y]);
	}
	
	
	/** setCell
	 * 
	 * Setzte den Inhalt des Boardes ueber die Zelle
	 * 
	 * @param cell : Zelle zum einf�gen ins Board
	 */
	public void setCell(Cell cell){
		int x = cell.getCol(), y=cell.getRow();
		BoardContent bc = cell.getContent();
		board[x][y]= bc;
	}
	
	/**getCellBC
	 * 
	 * Liefert den BoardContentWert vom Board der Zelle. 
	 * Praktisch um zu �berpr�fen ob auch die richtigen Informationen getestet werden
	 * 
	 * @param cell : Zelle -> Zelle auf Board
	 * @return Einen Boardcontent
	 */
	public BoardContent getCellBC(Cell cell){
		return board[cell.getCol()][cell.getRow()];
	}
}
