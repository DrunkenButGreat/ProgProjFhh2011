package de.gruppe12.logic;

import java.io.IOException;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.gruppe12.ki.NormalStrategy;
import de.gruppe12.shared.Cell;
import de.gruppe12.shared.Move;

public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
			GameLog.init("bla.txt");
			
			NormalStrategy n = new NormalStrategy();			
			LogicMain main = new LogicMain();
			main.humanDefHumanAtt(1000);
			main.move(new Move(new Cell(6,5, BoardContent.DEFENDER), new Cell(7,5, BoardContent.DEFENDER)));
			System.out.println(main.getLastGameLogEvent());
			
			System.out.println("Was ist in der Datei");
			System.out.println(StrategyLoader.listContent("Ki.jar"));
			System.out.println("Nur class bitte");
			System.out.println(StrategyLoader.filterExtension(StrategyLoader.listContent("Ki.jar"),"class"));
			System.out.println("Versuche Klasse zu laden...");
			System.out.println(StrategyLoader.getFromClassPath("de.gruppe12.ki.NormalStrategy").getStrategyName());
			System.out.println(StrategyLoader.getStrategy("Ki.jar", "de.gruppe12.ki.NormalStrategy").getStrategyName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
