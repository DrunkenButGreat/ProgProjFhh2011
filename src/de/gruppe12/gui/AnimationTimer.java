package de.gruppe12.gui;

public class AnimationTimer extends Thread {
	private MoveAnimation moveAnimation;
	private long lastTime;
	private long currentTime;
	
	public AnimationTimer(MoveAnimation moveAnimation) {
		this.moveAnimation= moveAnimation;
	}

	
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
