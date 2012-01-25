package de.gruppe12.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import de.fhhannover.inform.hnefatafl.vorgaben.BoardContent;

public class JPanelBoardDisplay extends JPanel {
	private final Point unselected = new Point(-1, -1);
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
	private Image kiFailOffWinImage;
	private Image kiFailDefWinImage;

	/**
	 * JPanelBoardDisplay
	 * 
	 * Konstruktor, der die Ressourcen l�dt, den GuiController speichert und
	 * anschlie�end ein repaint aufruft
	 * 
	 * @param gc
	 *            : GuiController zur Interaktion
	 */
	public JPanelBoardDisplay(final GuiController gc) {
		this.gc = gc;
		selectedCell = new Point(unselected);
		try {
			boardImage = ImageIO.read(getClass().getResource(
					"images/boardimage.bmp"));
			kingIcon = ImageIO.read(getClass().getResource(
					"images/kingicon.gif"));
			defenderIcon = ImageIO.read(getClass().getResource(
					"images/defendericon.gif"));
			offenderIcon = ImageIO.read(getClass().getResource(
					"images/offendericon.gif"));
			defenderWinImage = ImageIO.read(getClass().getResource(
					"images/defbannerimg.gif"));
			offenderWinImage = ImageIO.read(getClass().getResource(
					"images/offbannerimg.gif"));
			kiFailDefWinImage= ImageIO.read(getClass().getResource(
			"images/kifaildefwin.gif"));
			kiFailOffWinImage= ImageIO.read(getClass().getResource(
			"images/kifailoffwin.gif"));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		repaint();

		/**
		 * Mouse Listener
		 * 
		 * ermoeglicht das Auswaehlen und Bewegen von Spielfiguren prueft vor
		 * Markierung einer Zelle, ob die Logik ueberhaupt Spielerzug erwartet
		 * 
		 */

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!gc.logicAwaitsPlayerMove()
						|| gc.getAnimation().isRunning() || gc.gameFinished())
					return;
				Point cell = getCellOf(e.getX(), e.getY());
				if (cell == null)
					return;

				if (gc.isPlayersTurn(cell.x, cell.y)) {
					selectedCell.setLocation(cell);

				} else if (!selectedCell.equals(unselected)) {
					gc.doMove(selectedCell.x, selectedCell.y, cell.x, cell.y);
					selectedCell.setLocation(unselected);
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

	protected void resetSelectedCell() {
		selectedCell.setLocation(unselected);
	}

	/**
	 * getCellOf
	 * 
	 * berechnet zu einem Punkt auf dem JPanel die zugehoerige Spielfeldzelle
	 * 
	 * @param x
	 *            : x-Koordinate auf JPanel
	 * @param y
	 *            : y-Koordinate auf JPanel
	 * @return Point mit Spalte und Zeile der Zelle, null wenn ausserhalb des
	 *         Felds
	 * 
	 */

	private Point getCellOf(int x, int y) {
		Point result = new Point();

		result.x = (x - boardStartX) / fieldSize - 1;
		result.y = (y - boardStartY) / fieldSize - 1;

		if (result.x >= 0 && result.x < 13 && result.y >= 0 && result.y < 13)
			return result;
		return null;
	}

	/**
	 * paintComponent
	 * 
	 * paint-Aufruf der ausgefuehrt wird, wenn das JPanel groessenveraendert
	 * wird, oder durch expliziten repaint()-Aufruf
	 * 
	 * bestimmt anhand der Groesse des JPanels die Seitenlaenge des Spielfelds,
	 * zeichnet es
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D tempg = (Graphics2D) g.create();

		board = gc.getBoard();

		int roughBoardSize = (int) (Math.min(getWidth(), getHeight()) * 0.8);
		fieldSize = roughBoardSize / 15;
		boardStartX = (getWidth() - roughBoardSize) / 2;
		boardStartY = (getHeight() - roughBoardSize) / 2;

		tempg.drawImage(boardImage, boardStartX - fieldSize, boardStartY
				- fieldSize, boardStartX - fieldSize + fieldSize * 17,
				boardStartY - fieldSize + fieldSize * 17, 0, 0,
				boardImage.getWidth(null), boardImage.getHeight(null), null);

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (board[i][j] != BoardContent.EMPTY
						&& board[i][j] != BoardContent.INVALID) {
					drawStone(i, j, board[i][j], tempg);
				}
			}
		}

		if (!selectedCell.equals(unselected)) {
			tempg.setColor(Color.BLUE);

			tempg.drawRect(boardStartX + fieldSize * (selectedCell.x + 1),
					boardStartY + fieldSize * (selectedCell.y + 1), fieldSize,
					fieldSize);
			tempg.drawRect(boardStartX + fieldSize * (selectedCell.x + 1) - 1,
					boardStartY + fieldSize * (selectedCell.y + 1) - 1,
					fieldSize + 2, fieldSize + 2);

		}

		if (gc.gameFinished() && !gc.getAnimation().isRunning()) {
			Image banner;
			if (gc.kiDisqualified()) {
				if (gc.defenderWon()) banner= kiFailDefWinImage;
				else banner= kiFailOffWinImage;
			} else {
				if (gc.defenderWon()) banner= defenderWinImage;
				else banner= offenderWinImage;
			}

			double imageRatio = banner.getHeight(null)
					/ (double) banner.getWidth(null);
			double bannerHeight = imageRatio * 19 * fieldSize;

			tempg.drawImage(
					banner,
					boardStartX - 2 * fieldSize,
					(int) Math.round(boardStartY + 7.5 * fieldSize
							- bannerHeight / 2), 19 * fieldSize,
					(int) Math.round(bannerHeight), null);
		}

		tempg.dispose();
	}

	/**
	 * drawStone
	 * 
	 * zeichnet Spielfigur an der Zelle xy (Icon h�ngt vom BoardContent ab)
	 * 
	 * @param x
	 *            : X-Position der Figur
	 * @param y
	 *            : Y-Position der Figur
	 * @param bc
	 *            : Typ der Figur
	 * @param g
	 *            : Graphics Objekt
	 */
	private void drawStone(int x, int y, BoardContent bc, Graphics2D g) {
		Image img = null;
		if (bc == BoardContent.ATTACKER)
			img = offenderIcon;
		else if (bc == BoardContent.DEFENDER)
			img = defenderIcon;
		else
			img = kingIcon;

		if (gc.getAnimation().isRunning()) {
			Point cell = gc.getAnimation().getSrcCell();
			if (x == cell.x && y == cell.y) {
				double[] pos = gc.getAnimation().getStonePosition();
				g.drawImage(
						img,
						(int) Math.round(boardStartX + fieldSize * (pos[0] + 1)
								+ 1),
						(int) Math.round(boardStartY + fieldSize * (pos[1] + 1)
								+ 1), fieldSize - 2, fieldSize - 2, null);
				return;
			}
		}
		g.drawImage(img,
				(int) Math.round(boardStartX + fieldSize * (x + 1) + 1),
				(int) Math.round(boardStartY + fieldSize * (y + 1) + 1),
				fieldSize - 2, fieldSize - 2, null);
	}
}
