package de.gruppe12.gui;

import java.awt.Point;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;
import de.gruppe12.logic.LogicMain;
import de.gruppe12.shared.Board;
import de.gruppe12.shared.Cell;
import de.gruppe12.shared.Move;

public class GuiController implements Observer{
	private final MoveAnimation anim;
	private GameGui gui;
	private LogicMain logic;
	private MoveStrategy[] moveStrats;
	private static int thinkTime= 5000;
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
	
	protected void doMove(int srcX, int srcY, int destX, int destY) {
		logic.move(new Move(new Cell(srcX, srcY, logic.getBoard().getCell(srcX, srcY).getContent()), new Cell(destX, destY,logic.getBoard().getCell(srcX, srcY).getContent())));
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
				//TODO: Ende Anzeigen
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
		MoveStrategy mStrat=null;
		//mStrat aus MoveStrategy Array bestimmen
		if (humanIsAttacker) logic.humanAttKiDef(mStrat, thinkTime);
		else logic.humanDefKiAtt(mStrat, thinkTime);
	}

	
	protected void initAvAGame(String offenderMoveStrategyName, String defenderMoveStrategyName) {
		MoveStrategy offStrat= null;
		MoveStrategy defStrat= null;
		//MoveStrategies aus MoveStrategy Array bestimmen
		logic.KiDefKiAtt(defStrat, offStrat, thinkTime);
	}
	
	protected boolean defenderWon() {
		return !logic.getBoard().attackerWon();
	}

}
