package de.gruppe12.ki;

import de.gruppe12.shared.Move;;

/**
* Das Interface MoveStrategy ist das Interface ï¿½ber den verschiedenen Strategien <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 1.0.1 04.12.2011
* Änderungen: 04.12. javadoc erweitert
*/

public interface MoveStrategy {

	/**
	 * getGroupNr
	 * 
	 * @return die Gruppen Nr
	 */
	int getGroupNr();
	
	/**
	 *  getStrategyName
	 *  
	 * @return den Namen der Strategie
	 */
	String getStrategyName();
	
	/**
	 * calculateDefenderMove
	 * 
	 * @param lastMove
	 * @param thinktimeInSeconds
	 * @return berechnet den nächsten Spielzug des Verteidigers
	 */
	Move calculateDefenderMove(final Move lastMove,final int thinktimeInSeconds);
	
	/**
	 * calculateAttackeMove
	 * 
	 * @param lastMove
	 * @param thinktimeInSeconds
	 * @return berechnet den nächsten Spielzug des Angreifers
	 */
	Move calculateAttackerMove(final Move lastMove,final int thinktimeInSeconds);
}

