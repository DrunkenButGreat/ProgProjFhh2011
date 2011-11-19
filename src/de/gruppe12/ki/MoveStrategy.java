package de.gruppe12.ki;

import de.fhhannover.inform.hnefatafl.vorgaben.Move;

/**
* Das Interface MoveStrategy ist das Interface über den verschiedenen Strategien <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 1.0.0 19.11.2011
* Änderungen: 19.11. javadoc eingefügt
*/

public interface MoveStrategy {

	/**
	 * 
	 * @return
	 */
	int getGroupNr();
	
	/**
	 * 
	 * @return
	 */
	String getStrategyName();
	
	/**
	 * 
	 * @param lastMove
	 * @param thinktimeInSeconds
	 * @return
	 */
	Move calculateDefenderMove(final Move lastMove,final int thinktimeInSeconds);
	
	/**
	 * 
	 * @param lastMove
	 * @param thinktimeInSeconds
	 * @return
	 */
	Move calculateAttackerMove(final Move lastMove,final int thinktimeInSeconds);
}

