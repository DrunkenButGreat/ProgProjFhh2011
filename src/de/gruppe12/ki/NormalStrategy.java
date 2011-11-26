package de.gruppe12.ki;

import de.gruppe12.shared.*;


/**
* Die Klasse NormalStrategy ist die normale Spielstrategie <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 0.0.3 23.11.2011
* Änderungen: 24.11. 2Methoden für calculate Berechnung + Erweiterung von calculate
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

	//ein erster Versuch, noch nicht ganz der MinMax Algo. Müsste aber fürs Erste reichen.
	public Move calculateDefenderMove(Move lastMove, int thinktimeInSeconds) {
		//Speichern in Verlaufbaum, muss noch durchdacht werden
		//verlauf.setLeft(new Node<Move>(lastMove));
		
		//Abbruchbedingung Zeit muss eingefügt werden
		//if(time>= thinktimeInSeconds){
		//zeit abgelaufen -> Spiel vorbei ?
		//} else {
		//Hauptcode zur Berechnung nächster Schritt
	    Move[] moves = GenerateMoves();
	    Move move;
	    Move best_move=moves[0];
	    for(int i=0; i<=moves.length; i++) {
	       move = moves[i];
	       if (calculateValue(move) > calculateValue(best_move)) {
	          best_move = move;
	       }
	    }
	    return best_move;
		//}	
	}
	

	
	public Move calculateAttackerMove(Move lastMove, int thinktimeInSeconds) {
		//Speichern in Verlaufbaum, muss noch durchdacht werden
		//verlauf.setRight(new Node<Move>(lastMove));
		
		//Abbruchbedingung Zeit muss eingefügt werden
		//if(time>= thinktimeInSeconds){
		//zeit abgelaufen -> Spiel vorbei ?
		//} else {
		//Hauptcode zur Berechnung nächster Schritt
	    Move[] moves = GenerateMoves();
	    Move move;
	    Move best_move=moves[0];
	    for(int i=1; i<=moves.length; i++) {
	       move = moves[i];
	       if (calculateValue(move) > calculateValue(best_move)) {
	          best_move = move;
	       }
	    }
	    return best_move;
		//}	
		}
	
	
	/**
	 * 
	 * GenerateMoves()
	 * 
	 * @return ein Array mit allen Möglichen Moves
	 */
	//muss noch implementiert werden
	private Move[] GenerateMoves() {
		return null;
	}

	/**
	 * calculateValue
	 * 
	 * @return Bewertung des Moves
	 */
	   private int calculateValue(Move m) {
		      int value=0;
		     //implementierung von Bewertung eines Moves fehlt hir noch
		      return value;
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
