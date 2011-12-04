package de.gruppe12.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;

public class JPanelBoardDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	private GuiController gc;
	private int fieldSize;
	private int boardStartX;
	private int boardStartY;
	private final Point selectedCell; 
	private BoardContent[][] board;
	
	private Image boardImage;
	private Image kingIcon;
	private Image defenderIcon;
	private Image defenderWinImage;
	private Image offenderIcon;
	private Image offenderWinImage;
	
	public JPanelBoardDisplay(final GuiController gc) {
		this.gc= gc;
		selectedCell= new Point(-1,-1);
		//setOpaque(false);
		try {
			boardImage= ImageIO.read(new File("images/boardimage.bmp"));
			kingIcon= ImageIO.read(new File("images/kingicon.gif"));
			defenderIcon= ImageIO.read(new File("images/defendericon.gif"));
			offenderIcon= ImageIO.read(new File("images/offendericon.gif"));
			defenderWinImage= ImageIO.read(new File("images/defbannerimg.gif"));
			offenderWinImage= ImageIO.read(new File("images/offbannerimg.gif"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		repaint();
		
		/**
		 * Mouse Listener
		 * 
		 * ermoeglicht das Auswaehlen und Bewegen von Spielfiguren
		 * prueft vor Markierung einer Zelle, ob die Logik ueberhaupt Spielerzug erwartet
		 * 
		 */
		
		addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if (!gc.logicAwaitsPlayerMove() || gc.getAnimation().isRunning() || gc.gameFinished()) return;
				Point cell= getCellOf(e.getX(), e.getY());
				if (cell==null ) return;
				
				if (gc.isPlayersTurn(cell.x, cell.y)) {
					selectedCell.setLocation(cell);

				} else if (!selectedCell.equals(new Point(-1, -1))) {
					gc.doMove(selectedCell.x, selectedCell.y, cell.x, cell.y);
					selectedCell.setLocation(-1, -1);
				}
				repaint();
				
			}
			/**
			 * grober Test ob Pfad existiert
			 * 
			 * @param selectedCell
			 * @param cell
			 * @return boolean: true, wenn Pfad existiert, false wenn nicht
			 */

		});
	}
	
	/**
	 * getCellOf
	 * 
	 * berechnet zu einem Punkt auf dem JPanel die zugehoerige Spielfeldzelle
	 * 
	 * @param x	: x-Koordinate auf JPanel
	 * @param y : y-Koordinate auf JPanel
	 * @return Point mit Spalte und Zeile der Zelle, null wenn ausserhalb des Felds
	 * 
	 */
	
	protected Point getCellOf(int x, int y) {
		Point result= new Point();
		
		result.x= (x-boardStartX)/fieldSize-1;
		result.y= (y-boardStartY)/fieldSize-1;
		
		if (result.x>=0 && result.x <13 && result.y>=0 && result.y <13)
			return result;
		return null;
	}
	
	/**
	 * paintComponent
	 * 
	 * paint-Aufruf der ausgefuehrt wird, wenn das JPanel groessenveraendert wird, oder durch expliziten repaint()-Aufruf
	 * 
	 * bestimmt anhand der Groesse des JPanels die Seitenlaenge des Spielfelds, zeichnet es 
	 */
	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D tempg= (Graphics2D) g.create();
				
		board= gc.getBoard();
		
		int roughBoardSize= (int)(Math.min(getWidth(), getHeight()) * 0.8);
		fieldSize = roughBoardSize/15;
		boardStartX = (getWidth()-roughBoardSize)/2;
		boardStartY = (getHeight()-roughBoardSize)/2;
		
		//Brett zeichnen
		tempg.setColor(Color.ORANGE);
		tempg.drawImage(boardImage, boardStartX-fieldSize, boardStartY-fieldSize, boardStartX-fieldSize+fieldSize*17, boardStartY-fieldSize+fieldSize*17, 0, 0, boardImage.getWidth(null), boardImage.getHeight(null), null);
		//tempg.drawRect(boardStartX-fieldSize, boardStartY-fieldSize, fieldSize*17, fieldSize*17);
		
		/*//Tuerme zeichnen
		int towerSize= 2*fieldSize;
		tempg.setColor(Color.GREEN);
		tempg.drawRect(boardStartX, boardStartY, towerSize, towerSize);
		tempg.drawRect(boardStartX+13*fieldSize, boardStartY, towerSize, towerSize);
		tempg.drawRect(boardStartX,boardStartY+13*fieldSize, towerSize, towerSize);
		tempg.drawRect(boardStartX+13*fieldSize, boardStartY+13*fieldSize, towerSize, towerSize);
		
		//Tron markieren
		tempg.drawLine(boardStartX+7*fieldSize, boardStartY+7*fieldSize, boardStartX+8*fieldSize, boardStartY+8*fieldSize);
		tempg.drawLine(boardStartX+7*fieldSize, boardStartY+8*fieldSize, boardStartX+8*fieldSize, boardStartY+7*fieldSize);
		
		for (int i=0; i<13; i++) {
			for (int j=0; j<13; j++) {
				if (!( (i==j && (i==0 || i==12)) || (i==0 && j==12) || (i==12 && j==0) )){
					tempg.setColor(Color.GREEN);
					tempg.drawRect(boardStartX+fieldSize*(i+1), boardStartY+fieldSize*(j+1), fieldSize, fieldSize);
				}
			}
		}*/
		for (int i=0; i<13; i++) {
			for (int j=0; j<13; j++) {
				if (board[i][j]!=BoardContent.EMPTY && board[i][j]!=BoardContent.INVALID) {
					drawStone(i, j, board[i][j], tempg);
				}
			}
		}
		
		

		if (!selectedCell.equals(new Point(-1,-1))) {
			tempg.setColor(Color.BLUE);
			
			tempg.drawRect(boardStartX+fieldSize*(selectedCell.x+1), boardStartY+fieldSize*(selectedCell.y+1), fieldSize, fieldSize);
			tempg.drawRect(boardStartX+fieldSize*(selectedCell.x+1)-1, boardStartY+fieldSize*(selectedCell.y+1)-1, fieldSize+2, fieldSize+2);
			
		}
		
		if (gc.gameFinished()) {
			Image banner= defenderWinImage;
			//if (gc.offenderWon()) banner= offenderWinImage;
			
			double imageRatio= banner.getHeight(null)/(double)banner.getWidth(null);
			double bannerHeight= imageRatio* 19*fieldSize;
			
			tempg.drawImage(banner, boardStartX-2*fieldSize, (int)Math.round(boardStartY+7.5*fieldSize-bannerHeight/2), 19*fieldSize, (int)Math.round(bannerHeight), null);
		}
		
		tempg.dispose();
	}

	private void drawStone(int i, int j, BoardContent bc, Graphics2D g) {
		Image img = null;
		if (bc==BoardContent.ATTACKER) img= offenderIcon;
		else if (bc==BoardContent.DEFENDER) img= defenderIcon;
		else img= kingIcon;
		
		if (gc.getAnimation().isRunning()) {
			Point cell= gc.getAnimation().getSrcCell();
			if (i== cell.x && j== cell.y) {
				double[] pos= gc.getAnimation().getStonePosition();
					g.drawImage(img, 
							(int)Math.round(boardStartX+fieldSize*(pos[0]+1)+1), 
							(int)Math.round(boardStartY+fieldSize*(pos[1]+1)+1), 
							fieldSize-2, fieldSize-2, null);
				/*} else {
					g.fillOval(
							(int)Math.round(boardStartX+fieldSize*(pos[0]+1)+4), 
							(int)Math.round(boardStartY+fieldSize*(pos[1]+1)+4), 
							fieldSize-8, fieldSize-8);
				}*/
				return;
			}
		}
			g.drawImage(img, 
					(int)Math.round(boardStartX+fieldSize*(i+1)+1), 
					(int)Math.round(boardStartY+fieldSize*(j+1)+1), 
					fieldSize-2, fieldSize-2, null);
		/*} else {
			g.fillOval(boardStartX+fieldSize*(i+1)+4, boardStartY+fieldSize*(j+1)+4, fieldSize-8, fieldSize-8);
		}*/
	}
}
