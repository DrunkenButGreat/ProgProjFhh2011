package de.gruppe12.logic;

import java.io.IOException;
import java.util.Scanner;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.gruppe12.ki.NormalStrategy;
import de.gruppe12.shared.Cell;
import de.gruppe12.shared.Move;

public class main {
	
	static NormalStrategy n = new NormalStrategy();			
	static LogicMain main = new LogicMain();
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
			GameLog.init("bla.txt");
			main.humanDefHumanAtt(1000);
	
			//testWinKing();
			
			playHumanHuman();
			
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
	
	private static void testWinKing(){
		main.move(new Move(new Cell(6,8, BoardContent.DEFENDER), new Cell(6,10, BoardContent.DEFENDER)));
		main.move(new Move(new Cell(4,0, BoardContent.ATTACKER), new Cell(4,1, BoardContent.ATTACKER)));
		
		main.move(new Move(new Cell(6,7, BoardContent.DEFENDER), new Cell(6,9, BoardContent.DEFENDER)));
		main.move(new Move(new Cell(4,1, BoardContent.ATTACKER), new Cell(4,0, BoardContent.ATTACKER)));
		
		main.move(new Move(new Cell(6,6, BoardContent.KING), new Cell(6,8, BoardContent.KING)));
		main.move(new Move(new Cell(4,0, BoardContent.ATTACKER), new Cell(4,1, BoardContent.ATTACKER)));
		
		main.move(new Move(new Cell(6,8, BoardContent.KING), new Cell(11,8, BoardContent.KING)));
		main.move(new Move(new Cell(4,1, BoardContent.ATTACKER), new Cell(4,0, BoardContent.ATTACKER)));
		
		main.move(new Move(new Cell(11,8, BoardContent.KING), new Cell(11,12, BoardContent.KING)));
		main.move(new Move(new Cell(4,0, BoardContent.ATTACKER), new Cell(4,1, BoardContent.ATTACKER)));
		
		main.move(new Move(new Cell(11,12, BoardContent.KING), new Cell(12,12, BoardContent.KING)));
	}
	
	private static void playHumanHuman(){
		Scanner console = new Scanner(System.in);
		int xf, yf, xt, yt, type;
		while (!main.getBoard().isFinished()){
			if (main.getDefPlayerTurn() == true){
				System.out.println("Verteidiger ist am Zug");
			}
			else{
				System.out.println("Angreifer ist am Zug");
			}
			
			xf = console.nextInt();
			yf = console.nextInt();
			xt = console.nextInt();
			yt = console.nextInt();
			type = console.nextInt();
			
			switch (type){
			case 0:
				main.move(new Move(new Cell(xf, yf, BoardContent.DEFENDER), new Cell(xt, yt, BoardContent.DEFENDER)));
				break;
			case 1:
				main.move(new Move(new Cell(xf, yf, BoardContent.ATTACKER), new Cell(xt, yt, BoardContent.ATTACKER)));
				break;
			case 2:
				main.move(new Move(new Cell(xf, yf, BoardContent.KING), new Cell(xt, yt, BoardContent.KING)));
				break;
			default:
				break;			
			}			
		}
	}
}
