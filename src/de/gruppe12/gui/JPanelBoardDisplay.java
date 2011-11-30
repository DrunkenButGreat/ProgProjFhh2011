package de.gruppe12.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JPanelBoardDisplay extends JPanel {
	private GuiController gc;
	private int fieldSize;
	private int boardStartX;
	private int boardStartY;
	private final Point selectedCell; 
	private int[][] board;
	
	public JPanelBoardDisplay(final GuiController gc) {
		this.gc= gc;
		selectedCell= new Point(-1,-1);
		setOpaque(false);
		
		repaint();
		
		
		
		addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				Point cell= getCellOf(e.getX(), e.getY());
				if (cell==null) return;
				
				if (gc.isPlayersTurn(cell.x, cell.y)) {
					selectedCell.setLocation(cell);

				} else if (!selectedCell.equals(new Point(-1, -1))) {
					if (pathExists(selectedCell, cell)) {
						gc.doMove(selectedCell.x, selectedCell.y, cell.x, cell.y);
						selectedCell.setLocation(-1, -1);
					}
				}
				repaint();
				
			}

			private boolean pathExists(Point selectedCell, Point cell) {
				if (selectedCell.x != cell.x && selectedCell.y != cell.y) {
					return false;
				} 
				return true;
			}	
		});
	}
	
	protected Point getCellOf(int x, int y) {
		Point result= new Point();
		
		result.x= (x-boardStartX)/fieldSize-1;
		result.y= (y-boardStartY)/fieldSize-1;
		
		if (result.x>=0 && result.x <13 && result.y>=0 && result.y <13)
			return result;
		return null;
	}

	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D tempg= (Graphics2D) g.create();
		
		board= gc.getBoard();
		
		int roughBoardSize= (int)(Math.min(getWidth(), getHeight()) * 0.8);
		fieldSize = roughBoardSize/15;
		boardStartX = (getWidth()-roughBoardSize)/2;
		boardStartY = (getHeight()-roughBoardSize)/2;
		
		//Brett Umrandung zeichnen
		tempg.setColor(Color.ORANGE);
		tempg.drawRect(boardStartX-fieldSize, boardStartY-fieldSize, fieldSize*17, fieldSize*17);
		
		//Tuerme zeichnen
		int towerSize= 2*fieldSize;
		tempg.setColor(Color.GREEN);
		tempg.drawRect(boardStartX, boardStartY, towerSize, towerSize);
		tempg.drawRect(boardStartX+13*fieldSize, boardStartY, towerSize, towerSize);
		tempg.drawRect(boardStartX,boardStartY+13*fieldSize, towerSize, towerSize);
		tempg.drawRect(boardStartX+13*fieldSize, boardStartY+13*fieldSize, towerSize, towerSize);
		
		for (int i=0; i<13; i++) {
			for (int j=0; j<13; j++) {
				if (!( (i==j && (i==0 || i==12)) || (i==0 && j==12) || (i==12 && j==0) )){
					tempg.setColor(Color.GREEN);
					tempg.drawRect(boardStartX+fieldSize*(i+1), boardStartY+fieldSize*(j+1), fieldSize, fieldSize);
					if (board[i][j]>0) {
						drawStone(i, j, tempg);
					}
				}
			}
		}
		
		if (!selectedCell.equals(new Point(-1,-1))) {
			tempg.setColor(Color.PINK);
			
			tempg.drawRect(boardStartX+fieldSize*(selectedCell.x+1), boardStartY+fieldSize*(selectedCell.y+1), fieldSize, fieldSize);
			tempg.drawRect(boardStartX+fieldSize*(selectedCell.x+1)-1, boardStartY+fieldSize*(selectedCell.y+1)-1, fieldSize+2, fieldSize+2);
			
		}
		
		tempg.dispose();
	}

	private void drawStone(int i, int j, Graphics2D g) {
		if (board[i][j]==3) g.setColor(Color.RED);
		else if (board[i][j]==2) g.setColor(Color.BLUE);
		else g.setColor(Color.CYAN);
		
		if (gc.getAnimation().isRunning()) {
			Point cell= gc.getAnimation().getDestCell();
			if (i== cell.x && j== cell.y) {
				double[] pos= gc.getAnimation().getStonePosition();
				g.drawOval(
						(int)Math.round(boardStartX+fieldSize*(pos[0]+1)+1), 
						(int)Math.round(boardStartY+fieldSize*(pos[1]+1)+1), 
						fieldSize-2, fieldSize-2);
				return;
			}
		}
		
		g.drawOval(boardStartX+fieldSize*(i+1)+1, boardStartY+fieldSize*(j+1)+1, fieldSize-2, fieldSize-2);
	}
}
