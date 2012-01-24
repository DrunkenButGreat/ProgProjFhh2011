package de.gruppe12.ki;

import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;

/**
 * Die Klasse StrategyManager initialisiert die Strategien
 * <p>
 * Copyright: (c) 2011
 * <p>
 * Company: Gruppe 12
 * <p>
 * 
 * @author Markus
 * @version 1.0.1 04.12.2011 �nderungen: 04.12. javadoc erweitert, getStrategie
 *          verbessert
 */

public class StrategyManager {
	/**
	 * getStrategies
	 * 
	 * @return alle Strategien
	 */
	// muss noch �berarbeitet werden!!
	public MoveStrategy[] getStrategies() {
		MoveStrategy[] stListe = new KI_Gruppe12[12];
		return stListe;
	}

	/**
	 * MoveStrategy
	 * 
	 * @param getNr
	 * @param strategyName
	 * @return gibt die Strategie zur�ck die mit den �bergebenen Parametern
	 *         �bereinstimmt
	 */
	public MoveStrategy getStrategy(int getNr, String strategyName) {
		MoveStrategy[] list = getStrategies();
		for (int i = 0; i < list.length; i++) {
			if ((list[i].getGroupNr() == getNr)
					& (list[i].getStrategyName() == strategyName)) {
			}
		}
		return null;
	}
}
