package de.gruppe12.ki;

/**
* Die Klasse StrategyManager initialisiert die Strategien <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 1.0.1 21.11.2011
* Änderungen: 21.11. 
*/

public class StrategyManager {
	private MoveStrategy strategy;
	
	/**
	 * register
	 * 
	 * @param strategy
	 */
	public void register(MoveStrategy strategy){
	 this.strategy = strategy;
	}
	
	/**
	 * getStrategy
	 * 
	 * @return alle Strategien
	 */
	public MoveStrategy[] getStrategies(){
		MoveStrategy[] stListe = new NormalStrategy[12];
		return stListe;
	}
	
	/**
	 * MoveStrategy 
	 *  
	 * @param getNr
	 * @param strategyName
	 * @return
	 */
	public MoveStrategy getStrategy(int getNr, String strategyName){
		return strategy;
	}
}
