package de.gruppe12.gui;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.swing.SwingUtilities;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;
import de.gruppe12.logic.LogicMain;
import de.gruppe12.logic.StrategyLoader;
import de.gruppe12.shared.Board;
import de.gruppe12.shared.Cell;
import de.gruppe12.shared.Move;

public class GuiController implements Observer{
	private final String kiPathName= "Ki.jar";
	private final MoveAnimation anim;
	private GameGui gui;
	private LogicMain logic;
	private MoveStrategy[] moveStrats;
	private static int thinkTime= 100000;
	private String lastMoveLog= null;
	private BoardContent[][] board= null;
	
	public GuiController() {
		anim= new MoveAnimation(this);
	}
	
	protected void setGameGui(GameGui gui) {
		this.gui= gui;
	}
	
	public void setLogicMain(LogicMain logic) {
		this.logic= logic;
		logic.addObserver(this);
	}
	
	protected boolean gameFinished() {
		return logic.getBoard().isFinished();
	}
	
	protected boolean isPlayersTurn(int cellX, int cellY) {
		boolean defTurn= logic.getDefPlayerTurn();
		boolean result= false;
		if (((board[cellX][cellY]==BoardContent.DEFENDER || board[cellX][cellY]==BoardContent.KING) && defTurn) || (
		board[cellX][cellY]==BoardContent.ATTACKER && !defTurn))
		{
			result= true;
		}
		return result;
	}
	
	protected BoardContent[][] getBoard() {
		if (board==null) board= getBoardCopy();
		return board;
	}
	
	protected void doMove(final int srcX, final int srcY, final int destX, final int destY) {
		new Thread() {
			@Override public void run() {
				logic.move(new Move(new Cell(srcX, srcY, logic.getBoard().getCell(srcX, srcY).getContent()), new Cell(destX, destY,logic.getBoard().getCell(srcX, srcY).getContent())));
			}
		}.start();
	}

	@Override
	public void update(Observable obsSrc, Object obj) {
		if (obj instanceof Move) {
			lastMoveLog= logic.getLastGameLogEvent();
			Move move= (Move)obj;
			Point sourceCell= new Point(move.getFromCell().getCol(), move.getFromCell().getRow());
			Point destCell= new Point(move.getToCell().getCol(), move.getToCell().getRow());
			
			anim.startAnimation(sourceCell, destCell);
		}
		if (obj instanceof String) {
			String str= (String) obj;
			if (str.equals("GameOver")) {
				update();
			}
		}
	}
	
	/**
	 * getMoveStratNames
	 * 
	 * @return gibt ein Array mit den Namen der vorhandenen MoveStrategies zur�ck
	 */
	protected String[] getMoveStratNames() {
		return null;
	}
	
	protected boolean logicAwaitsPlayerMove() {
		//return logic.isWaiting();
		return true;
	}
	
	protected void update() {
		gui.update();
	}
	
	private BoardContent[][] getBoardCopy() {
		BoardContent[][] boardcopy= new BoardContent[13][13];
		for (int i=0; i<boardcopy.length; i++) {
			for (int j=0; j<boardcopy[i].length; j++) {
				boardcopy[i][j]= logic.getBoard().get()[i][j];
			}
		}
		return boardcopy; 
	}
	
	protected void refreshBoard() {
		board= getBoardCopy();
	}
	
	protected MoveAnimation getAnimation() {
		return anim;
	}
	
	protected void initHvHGame() {
		logic.humanDefHumanAtt(thinkTime);
		board= null;
	}
	
	protected String getLastMoveLog() {
		String last= lastMoveLog;
		lastMoveLog= null;
		return last;
	}
	
	protected void initHvAGame(boolean humanIsAttacker, String aiMoveStrategyName) {
		MoveStrategy mStrat= null;
		try {
			mStrat = StrategyLoader.getStrategy(kiPathName, aiMoveStrategyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (humanIsAttacker) logic.humanAttKiDef(mStrat, thinkTime);
		else logic.humanDefKiAtt(mStrat, thinkTime);
		board = logic.getBoard().get(); 
	}

	
	protected void initAvAGame(final String offenderMoveStrategyName, final String defenderMoveStrategyName) {
		new Thread () {
			@Override
			public void run() {
				MoveStrategy offStrat= null;
				MoveStrategy defStrat= null;
				try {
					offStrat = StrategyLoader.getStrategy(kiPathName, offenderMoveStrategyName);
					defStrat = StrategyLoader.getStrategy(kiPathName, defenderMoveStrategyName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				logic.KiDefKiAtt(defStrat, offStrat, thinkTime);
			}
		}.start();
		board = logic.getBoard().get();
	}
	
	protected boolean defenderWon() {
		return !logic.getBoard().attackerWon();
	}

	protected Map<String, String> getStrats() {
		ArrayList<String> moveStrategies= null;
		try {
			moveStrategies= StrategyLoader.listContent(kiPathName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveStrategies= StrategyLoader.filterExtension(moveStrategies, ".class");
		
		Map<String, String> shortPathMap= new TreeMap<String, String>();
		for (String s: moveStrategies) {
			String filename;
			if (s.contains("/")) {
				filename= s.substring(s.lastIndexOf('/')+1, s.lastIndexOf('.'));
			} else {
				filename= s.substring(0, s.lastIndexOf('.'));
			}
			
			shortPathMap.put(filename, s);
		}
		return shortPathMap;
	}

	protected void wakeLogic() {
		synchronized (logic) {
			logic.notifyAll();
		}
	}

}
