package de.gruppe12.ki;

/**
* Die Klasse StrategyManager initialisiert die Strategien <p>
* Copyright: (c) 2011 <p>
* Company: Gruppe 12 <p>
* @author Markus
* @version 1.0.1 04.12.2011
* Änderungen: 04.12. javadoc erweitert, getStrategie verbessert
*/

public class StrategyManager {
	private MoveStrategy strategy;
	
	/**
	 * register
	 * 
	 * initialisiert eine Spielstrategie
	 * 
	 * @param strategy 
	 */
	public void register(MoveStrategy strategy){
	 this.strategy = strategy;
	}
	
	/**
	 * getStrategies
	 * 
	 * @return alle Strategien
	 */
	//muss noch überarbeitet werden!!
	public MoveStrategy[] getStrategies(){
		MoveStrategy[] stListe = new NormalStrategy[12];
		return stListe;
	}
	
	/**
	 * MoveStrategy  
	 *  
	 * @param getNr
	 * @param strategyName
	 * @return gibt die Strategie zurück die mit den übergebenen Parametern übereinstimmt
	 */
	public MoveStrategy getStrategy(int getNr, String strategyName){
		MoveStrategy[] list= getStrategies();
		for(int i=0;i<list.length;i++){
		if((list[i].getGroupNr()==getNr) &(list[i].getStrategyName()==strategyName)){
		}
		}
		return null;
	}
}
