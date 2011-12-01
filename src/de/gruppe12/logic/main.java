package de.gruppe12.logic;

import java.io.IOException;

public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
//			System.out.println("Was ist in der Datei");
//			System.out.println(StrategyLoader.listContent("Ki.jar"));
//			System.out.println("Nur class bitte");
//			System.out.println(StrategyLoader.filterExtension(StrategyLoader.listContent("Ki.jar"),"class"));
			System.out.println("Versuche Klasse zu laden...");
			System.out.println(StrategyLoader.getFromClassPath("de.gruppe12.ki.NormalStrategy").getStrategyName());
//			System.out.println(StrategyLoader.getStrategy("Ki.jar", "de.gruppe12.ki.NormalStrategy").getStrategyName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
