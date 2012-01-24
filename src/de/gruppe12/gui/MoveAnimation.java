package de.gruppe12.gui;

import java.awt.Point;

public class MoveAnimation {
	private static final int durationPerCrossedCell = 100;
	private Point sourceCell, destCell;
	private int animationDuration, remainingDuration;
	private GuiController gc;
	private boolean running;

	public MoveAnimation(GuiController gc) {
		this.gc = gc;
		running = false;
	}

	/**
	 * startAnimation
	 * 
	 * startet eine neue Animation. Zuerst wird die Animationszeit in
	 * abh�ngigkeit von der Entfernung berechnet und anschlie�end wird ein
	 * AnimationTimer gestartet
	 * 
	 * @param sourceCell
	 *            : Zelle mit zu bewegender Figur
	 * @param destCell
	 *            : Zielzelle der Figur
	 */
	protected void startAnimation(Point sourceCell, Point destCell) {
		this.sourceCell = sourceCell;
		this.destCell = destCell;
		initAnimationTimers();
		running = true;
		new AnimationTimer(this).start();
	}

	/**
	 * berechntet Animationsdauer in Abh�ngigkeit von der Entfernung zwischen
	 * Ursprungs und Ziel Zelle
	 */
	private void initAnimationTimers() {
		int distance = Math.max(Math.abs(sourceCell.x - destCell.x),
				Math.abs(sourceCell.y - destCell.y));
		remainingDuration = animationDuration = distance
				* durationPerCrossedCell;
	}

	/**
	 * getRemainingDuration
	 * 
	 * gibt die noch verbleibende Animationszeit in ms zur�ck
	 * 
	 * @return remaining Duration
	 */
	protected int getRemainingDuration() {
		return remainingDuration;
	}

	/**
	 * end
	 * 
	 * sagt dem GuiController, dass es sich das aktuelle Brett speichern kann,
	 * setzt (Animation) running auf false und weckt die schlafende Logik auf
	 * 
	 */
	protected void end() {
		gc.refreshBoard();
		running = false;
		gc.update();
		gc.wakeLogic();
	}

	/**
	 * update
	 * 
	 * zieht von der Restdauer die verstrichene Dauer ab und bewirkt ein repaint
	 * der GUI
	 * 
	 * @param dT
	 *            : verstrichene Zeit
	 */
	protected void update(long dT) {
		remainingDuration -= dT;
		remainingDuration = Math.max(0, remainingDuration);
		gc.update();
	}

	/**
	 * gibt wider ob derzeitig eine Animation l�uft
	 * 
	 * @return boolean isRunning
	 */
	protected boolean isRunning() {
		return running;
	}

	/**
	 * getStonePosition
	 * 
	 * berechnet die Position des bewegten Steins in Abh�ngigkeit zur Rest
	 * Animationsdauer
	 * 
	 * @return double Array mit x und y Position des Steins
	 */
	protected double[] getStonePosition() {
		return new double[] {
				destCell.x + (sourceCell.x - destCell.x) * remainingDuration
						/ (double) animationDuration,
				destCell.y + (sourceCell.y - destCell.y) * remainingDuration
						/ (double) animationDuration };
	}

	/**
	 * getSrcCell
	 * 
	 * @return gibt die Zelle wider, von der aus die Figur gestartet ist
	 */
	protected Point getSrcCell() {
		return sourceCell;
	}

}
