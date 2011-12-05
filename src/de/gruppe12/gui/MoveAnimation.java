package de.gruppe12.gui;

import java.awt.Point;

public class MoveAnimation {
	private static final int durationPerCrossedCell= 100;
	private Point sourceCell, destCell;
	private int animationDuration, remainingDuration;
	private GuiController gc;
	private boolean running;
	
	public MoveAnimation(GuiController gc) {
		this.gc= gc;
		running= false;
	}
	
	protected void startAnimation(Point sourceCell, Point destCell) {
		this.sourceCell= sourceCell;
		this.destCell= destCell;
		initAnimationTimers();
		running= true;
		new AnimationTimer(this).start();
	}

	private void initAnimationTimers() {
		int distance= Math.max(Math.abs(sourceCell.x-destCell.x), Math.abs(sourceCell.y-destCell.y));
		remainingDuration= animationDuration= distance*durationPerCrossedCell;
	}

	protected int getRemainingDuration() {
		return remainingDuration;
	}

	protected void end() {
		gc.refreshBoard();
		running= false;
		gc.update();
		gc.wakeLogic();
	}

	protected void update(long dT) {
		remainingDuration-= dT;
		remainingDuration= Math.max(0, remainingDuration);
		gc.update();
	}

	protected boolean isRunning() {
		return running;
	}

	protected double[] getStonePosition() {
		return new double[] {
				destCell.x+ (sourceCell.x-destCell.x)*remainingDuration/(double)animationDuration,
				destCell.y+ (sourceCell.y-destCell.y)*remainingDuration/(double)animationDuration
		};
	}

	protected Point getSrcCell() {
		return sourceCell;
	}

}
