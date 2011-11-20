package de.gruppe12.gui;

import java.awt.Point;

public class MoveAnimation {
	private Point sourceCell, destCell;
	private int animationDuration, remainingDuration;
	GuiController gc;
	
	public MoveAnimation(Point sourceCell, Point destCell, GuiController gc) {
		this.sourceCell= sourceCell;
		this.destCell= destCell;
		this.gc= gc;
		
	}

}
