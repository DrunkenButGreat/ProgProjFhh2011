package de.gruppe12.gui;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;
import de.gruppe12.ki.MoveStrategy;
import de.gruppe12.logic.LogicMain;
import de.gruppe12.shared.Board;
import de.gruppe12.shared.Cell;
import de.gruppe12.shared.Move;

public class GuiController extends Observable implements Observer{
	private final MoveAnimation anim;
	private GameGui gui;
	private LogicMain logic;
	private MoveStrategy[] moveStrats;
	private static int thinkTime= 5000;
	
	public GuiController() {
		addObserver(this);
		anim= new MoveAnimation(this);
		
		int k= 1;
		int d= 2;
		int a= 3;
	}
	
	protected void setGameGui(GameGui gui) {
		this.gui= gui;
	}
	
	public void setLogicMain(LogicMain logic) {
		this.logic= logic;
		logic.addObserver(this);
	}
	
	protected boolean isPlayersTurn(int cellX, int cellY) {
		boolean result=false;
		/*if (((board[cellX][cellY]==BoardContent.DEFENDER || dummyBoard[cellX][cellY]==BoardContent.KING) && defendersTurn) || (
		dummyBoard[cellX][cellY]==BoardContent.ATTACKER && !defendersTurn))
		{
			result= true;
		}*/
		return result;
	}
	
	protected BoardContent[][] getBoard() {
		//return logic.getBoard().get();
		return new Board().get();
	}
	
	protected String getLog() {
		return null;
	}
	
	protected void doMove(int srcX, int srcY, int destX, int destY) {
		//###############
		/*[destX][destY]= dummyBoard[srcX][srcY];
		dummyBoard[srcX][srcY]= 0;
		
		setChanged();
		notifyObservers(new Move(new Cell(srcX, srcY, null), new Cell(destX, destY, null)));
		defendersTurn= !defendersTurn;
		//###############*/
		logic.move(new Move(new Cell(srcX, srcY, null), new Cell(destX, destY, null)));
	}

	@Override
	public void update(Observable obsSrc, Object obj) {
		if (obj instanceof Move) {
			Move move= (Move)obj;
			Point sourceCell= new Point(move.getFromCell().getCol(), move.getFromCell().getRow());
			Point destCell= new Point(move.getToCell().getCol(), move.getToCell().getRow());
			anim.startAnimation(sourceCell, destCell);
		}
	}
	
	/**
	 * getMoveStratNames
	 * 
	 * @return gibt ein Array mit den Namen der vorhandenen MoveStrategies zurück
	 */
	protected String[] getMoveStratNames() {
		return null;
	}
	
	protected boolean logicAwaitsPlayerMove() {
		//return logic.isWaiting();
		return true;
	}
	
	protected void update() {
		gui.redraw();
	}
	
	protected MoveAnimation getAnimation() {
		return anim;
	}
	
	protected void initHvHGame() {
		logic.humanDefHumanAtt(thinkTime);
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

}
