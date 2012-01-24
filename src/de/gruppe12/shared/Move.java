package de.gruppe12.shared;

import de.gruppe12.shared.Cell;

public class Move implements de.fhhannover.inform.hnefatafl.vorgaben.Move {
	
	Cell from, to;
	
	public Move(Cell from, Cell to){
		this.from = from;
		this.to = to;
	}

	@Override
	public Cell getFromCell() {
		return from;
	}

	@Override
	public Cell getToCell() {
		return to;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Von: "+ from.getCol() +" / "+from.getRow()+" / "+ from.getContent() + "\n");
		sb.append("Nach: "+ to.getCol() +" / "+to.getRow()+" / "+ to.getContent() + "\n");
		return sb.toString();
	}

}
