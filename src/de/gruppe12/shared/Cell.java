package de.gruppe12.shared;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;

public class Cell implements de.fhhannover.inform.hnefatafl.vorgaben.Cell {
	
	private int col;
	private int row;
	private BoardContent boardContent;
	
	public Cell(int col, int row, BoardContent boardContent){
		this.col = col;
		this.row = row;
		this.boardContent = boardContent;
	}

	@Override
	public int getCol() {
		return col;
	}

	@Override
	public BoardContent getContent() {
		return boardContent;
	}

	@Override
	public int getRow() {
		return row;
	}
	
	@Override
	public String toString(){
		return "Spalte: " + col + " Zeile: " + row + " Inhalt: " + boardContent.toString();
	}	
}