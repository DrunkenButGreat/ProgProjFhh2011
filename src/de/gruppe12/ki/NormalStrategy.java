package de.gruppe12.ki;

import de.gruppe12.shared.*;


/**
* Die Klasse NormalStrategy ist die normale Spielstrategie <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 0.0.2 23.11.2011
* Änderungen: 23.11. Problem mit Vorgaben.MoveStrategy / Kommentare in calculate
*/

public class NormalStrategy implements MoveStrategy {
	private int getNr;
	private String name;
	private Node<Move> verlauf;
	
	public NormalStrategy(int grpNr){
		name = "normal";
		getNr = grpNr;
	}
	
	public int getGroupNr() {
		return getNr;
	}

	public String getStrategyName() {
		return name;
	}

	public Move calculateDefenderMove(Move lastMove, int thinktimeInSeconds) {
		//Speichern in Verlaufbaum, muss noch durchdacht werden
		//verlauf.setLeft(new Node<Move>(lastMove));
		
		//Abbruchbedingung Zeit muss eingefügt werden
		//if(time>= thinktimeInSeconds){
		
		//Hauptcode zur Berechnung nächster Schritt
		//Platzhalter:
		Move newM = new Move(lastMove.getToCell(),new Cell(2,3, null));
		
		//} else {
		return newM;
		//}
	}

	public Move calculateAttackerMove(Move lastMove, int thinktimeInSeconds) {
		//Speichern in Verlaufbaum, muss noch durchdacht werden
		//verlauf.setRight(new Node<Move>(lastMove));
		
		//Abbruchbedingung Zeit muss eingefügt werden
		//if(time>= thinktimeInSeconds){
		
		//Hauptcode zur Berechnung nächster Schritt
		//Platzhalter:
		Move newM = new Move(lastMove.getToCell(),new Cell(2,3, null));
		
		//} else {
		return newM;
		//}	
		}
	
	public String toString(){
	return name+" Strategy, Gruppe"+getNr;	
	}

	//Methoden müssen importiert werden, vlt in vorgaben löschen ?
	@Override
	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateDefenderMove(
			de.fhhannover.inform.hnefatafl.vorgaben.Move lastMove,
			int thinktimeInSeconds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.fhhannover.inform.hnefatafl.vorgaben.Move calculateAttackerMove(
			de.fhhannover.inform.hnefatafl.vorgaben.Move lastMove,
			int thinktimeInSeconds) {
		// TODO Auto-generated method stub
		return null;
	}

}
