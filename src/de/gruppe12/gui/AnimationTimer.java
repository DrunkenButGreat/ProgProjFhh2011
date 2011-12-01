package de.gruppe12.gui;
/**
 * AnimationTimer
 * 
 * @author Simon Karyo
 * @version 1.0
 *
 */

public class AnimationTimer extends Thread {
	private MoveAnimation moveAnimation;
	private long lastTime;
	private long currentTime;
	
	/**
	 * AnimationTimer-Konstruktor
	 * 
	 * erstellt AnimationTimer und setzt dessen moveAnimation
	 * 
	 * @param moveAnimation
	 */
	public AnimationTimer(MoveAnimation moveAnimation) {
		this.moveAnimation= moveAnimation;
	}

	/**
	 * run
	 * 
	 * verringert solange die MoveAnimation Restdauer hat 
	 * die Restdauer in ~50ms Abstaenden durch Aufruf der MoveAnimation.update(dT) Methode
	 *
	 */
	@Override
	public void run() {
		lastTime=System.currentTimeMillis();
		while (moveAnimation.getRemainingDuration() > 0) {
			currentTime= System.currentTimeMillis();

			moveAnimation.update(currentTime - lastTime);
			
			lastTime= currentTime;
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		moveAnimation.end();
	}
}
