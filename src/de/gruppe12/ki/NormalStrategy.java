package de.gruppe12.ki;

import de.fhhannover.inform.hnefatafl.vorgaben.Move;

/**
* Die Klasse NormalStrategy ist die normale Spielstrategie <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 1.0.0 19.11.2011
* Änderungen: 19.11. javadoc eingefügt
*/

public class NormalStrategy implements MoveStrategy {
	private int getNr;
	private String name;
	
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
		//Move newM = new Move(4,3);
		return null;
	}

	public Move calculateAttackerMove(Move lastMove, int thinktimeInSeconds) {
		//Move newM = new Move(2,4);
		return null;
	}
	
	public String toString(){
	return name+" Strategy, Gruppe"+getNr;	
	}

}
