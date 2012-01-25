package de.gruppe12.gui;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;
import de.gruppe12.logic.LogicMain;
import de.gruppe12.logic.StrategyLoader;
import de.gruppe12.shared.Cell;
import de.gruppe12.shared.Move;

public class GuiController implements Observer {
	private Map<String, String> kiPaths = new HashMap<String, String>();
	private final MoveAnimation anim;
	private GameGui gui;
	private LogicMain logic;
	private static int thinkTime = 5000;
	private String lastMoveLog = null;
	private BoardContent[][] board = null;
	private boolean kiFailed;

	public GuiController() {
		anim = new MoveAnimation(this);
	}

	protected void setGameGui(GameGui gui) {
		this.gui = gui;
	}

	protected String addKiPathName(String name) {
		String shortName = name.substring(name.lastIndexOf('\\') + 1);
		if (shortName.contains("."))
			shortName = shortName.substring(0, shortName.indexOf("."));
		if (kiPaths.containsKey(shortName)) {
			if (kiPaths.containsValue(name))
				return null;
			int extraIndex = 1;
			while (kiPaths.containsKey(shortName + "(" + extraIndex + ")")) {
				extraIndex++;
			}
			shortName += "(" + extraIndex + ")";

		}
		kiPaths.put(shortName, name);
		return shortName;
	}
	
	protected boolean kiDisqualified() {
		return kiFailed;
	}

	public void setLogicMain(LogicMain logic) {
		this.logic = logic;
		logic.addObserver(this);
	}
	
	private void reinit() {
		kiFailed= false;
		board= null;
	}

	public void resetLogic() {
		logic.resetLogic();
	}

	/**
	 * gameFinished
	 * 
	 * fragt das Board ab, ob das Spiel beendet ist
	 * 
	 * @return boolean ob Spiel zu Ende ist
	 */
	protected boolean gameFinished() {
		return logic.getBoard().isFinished();
	}

	protected boolean isDefendersTurn() {
		return logic.getDefPlayerTurn();
	}

	/**
	 * isPlayersTurn
	 * 
	 * @param cellX
	 * @param cellY
	 * @return boolean ob Zelle dem Spieler geh�rt, der am Zug ist
	 */

	protected boolean isPlayersTurn(int cellX, int cellY) {
		boolean defTurn = logic.getDefPlayerTurn();
		boolean result = false;
		if (((board[cellX][cellY] == BoardContent.DEFENDER || board[cellX][cellY] == BoardContent.KING) && defTurn)
				|| (board[cellX][cellY] == BoardContent.ATTACKER && !defTurn)) {
			result = true;
		}
		return result;
	}

	/**
	 * getBoard
	 * 
	 * @return BoardContent Array
	 */
	protected BoardContent[][] getBoard() {
		if (board == null)
			board = getBoardCopy();
		return board;
	}

	/**
	 * doMove
	 * 
	 * ruft die move() Methode der LogicMain auf.
	 * 
	 * @param srcX
	 *            : x-Wert der zu bewegenden Spielfigur
	 * @param srcY
	 *            : y-Wert der zu bewegenden Spielfigur
	 * @param destX
	 *            : x-Wert der Ziel Zelle
	 * @param destY
	 *            : y-Wert der Ziel Zelle
	 */
	protected void doMove(final int srcX, final int srcY, final int destX,
			final int destY) {
		new Thread() {
			@Override
			public void run() {
				logic.move(new Move(new Cell(srcX, srcY, logic.getBoard()
						.getCell(srcX, srcY).getContent()), new Cell(destX,
						destY, logic.getBoard().getCell(srcX, srcY)
								.getContent())));
			}
		}.start();
	}

	/**
	 * update
	 * 
	 * implementiert Observer-Interface Methode. Wenn ein Move Objekt �bergeben
	 * wird, wird eine Move Animation gestartet.
	 * 
	 * Wenn ein String mit "GameOver" �bergeben wird, wird die Update Methode
	 * der GUI aufgerufen um das Spielende anzuzeigen.
	 * 
	 */
	@Override
	public void update(Observable obsSrc, Object obj) {
		if (obj instanceof de.fhhannover.inform.hnefatafl.vorgaben.Move) {
			lastMoveLog = logic.getLastGameLogEvent();
			de.fhhannover.inform.hnefatafl.vorgaben.Move move = (de.fhhannover.inform.hnefatafl.vorgaben.Move) obj;
			Point sourceCell = new Point(move.getFromCell().getCol(), move
					.getFromCell().getRow());
			Point destCell = new Point(move.getToCell().getCol(), move
					.getToCell().getRow());

			anim.startAnimation(sourceCell, destCell);
		}
		if (obj instanceof String) {
			String str = (String) obj;
			if (str.equals("GameOver")) {
				kiFailed= true;
				update();
			}
		}
	}

	/**
	 * getMoveStratNames
	 * 
	 * @return gibt ein Array mit den Namen der vorhandenen MoveStrategies
	 *         zur�ck
	 */
	protected String[] getMoveStratNames() {
		return null;
	}

	/**
	 * logicAwaitsPlayerMove
	 * 
	 * @return boolean-Wert ob Logik momentan auf Spieler Move wartet
	 */
	protected boolean logicAwaitsPlayerMove() {
		// return logic.isWaiting();
		return true;
	}

	/**
	 * update
	 * 
	 * ruft update()-Methode der GUI auf
	 */
	protected void update() {
		gui.update();
	}

	/**
	 * getBoardCopy
	 * 
	 * gibt eine Kopie des aktuellen Boardinhalts wieder (um Steine f�r
	 * Animation tempor�r zu speichern)
	 * 
	 * @return BoardContent Array
	 */
	private BoardContent[][] getBoardCopy() {
		BoardContent[][] boardcopy = new BoardContent[13][13];
		for (int i = 0; i < boardcopy.length; i++) {
			for (int j = 0; j < boardcopy[i].length; j++) {
				boardcopy[i][j] = logic.getBoard().get()[i][j];
			}
		}
		return boardcopy;
	}

	/**
	 * refreshBoard
	 * 
	 * setzt das Board Attribut auf das aktuelle Board der Logik
	 * 
	 */
	protected void refreshBoard() {
		board = getBoardCopy();
	}

	/**
	 * getAnimation
	 * 
	 * @return aktuelle MoveAnimation
	 */
	protected MoveAnimation getAnimation() {
		return anim;
	}

	/**
	 * initHvHGame
	 * 
	 * ruft Methode der Logik auf um Mensch vs Mensch Spiel zu starten
	 */
	protected void initHvHGame() {
		logic.humanDefHumanAtt(thinkTime);
		reinit();
	}

	/**
	 * getLastMoveLog
	 * 
	 * @return LogString des letzten Moves
	 */
	protected String getLastMoveLog() {
		String last = lastMoveLog;
		lastMoveLog = null;
		return last;
	}

	/**
	 * initHvAGame
	 * 
	 * ruft Methode der Logik auf um Mensch vs KI Spiel zu starten
	 * 
	 * @param humanIsAttacker
	 * @param aiMoveStrategyName
	 *            : Name der KI Strategie Class
	 */
	protected void initHvAGame(boolean humanIsAttacker, String path,
			String aiMoveStrategyName) {
		MoveStrategy mStrat = null;
		try {
			mStrat = StrategyLoader.getStrategy(path, aiMoveStrategyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (humanIsAttacker)
			logic.humanAttKiDef(mStrat, thinkTime);
		else
			logic.humanDefKiAtt(mStrat, thinkTime);
		reinit();
	}

	/**
	 * initAvaGame
	 * 
	 * ruft Methode der Logik auf um KI vs KI Spiel zu starten
	 * 
	 * @param offenderMoveStrategyName
	 *            : Name der Angreifer KI Class
	 * @param defenderMoveStrategyName
	 *            : Name der Verteidiger KI Class
	 */
	protected void initAvAGame(final String path1,
			final String offenderMoveStrategyName, final String path2,
			final String defenderMoveStrategyName) {
		new Thread() {
			@Override
			public void run() {
				MoveStrategy offStrat = null;
				MoveStrategy defStrat = null;
				try {
					offStrat = StrategyLoader.getStrategy(path1,
							offenderMoveStrategyName);
					defStrat = StrategyLoader.getStrategy(path2,
							defenderMoveStrategyName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				logic.KiDefKiAtt(defStrat, offStrat, thinkTime);
			}
		}.start();
		reinit();
	}

	/**
	 * defenderWon
	 * 
	 * @return boolean-Wert der widergibt ob Verteidiger gewonnen hat
	 */
	protected boolean defenderWon() {
		return !logic.getBoard().attackerWon();
	}

	protected Map<String, String> getKiFolderList() {
		return kiPaths;
	}

	/**
	 * getStrats
	 * 
	 * liefert eine Map mit kurzen Namen als Key und relativem Pfad als Wert
	 * 
	 * @return Map, die alle .class Dateien, die im aktuellen Ki Jar Pfad sind,
	 *         speichert
	 */
	protected Map<String, String> getStrats(String path) {
		ArrayList<String> moveStrategies = null;
		try {
			moveStrategies = StrategyLoader.listContent(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveStrategies = StrategyLoader.filterExtension(moveStrategies,
				".class");

		Map<String, String> shortPathMap = new TreeMap<String, String>();
		for (String s : moveStrategies) {
			String filename;
			if (s.contains("/")) {
				filename = s.substring(s.lastIndexOf('/') + 1,
						s.lastIndexOf('.'));
			} else {
				filename = s.substring(0, s.lastIndexOf('.'));
			}

			shortPathMap.put(filename, s);
		}
		return shortPathMap;
	}

	/**
	 * wakeLogic
	 * 
	 * weckt LogicMain nach Beenden der MoveAnimation
	 */
	protected void wakeLogic() {
		synchronized (logic) {
			logic.notifyAll();
		}
	}

}
