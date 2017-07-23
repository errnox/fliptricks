package com.fliptricks;

import processing.core.PApplet;

public class Camera {
	public float x;
	public float y;
	private PApplet p;
	private float speed;
	private float targetX;
	private float targetY;
	private boolean isCentering;
	private float camZ;
	private float camX;
	private float camZSwingSpeed;
	private boolean camIsSwingingClockwise;
	private int swingRadius;
	private boolean isResettingSwing;

	public Camera(PApplet p) {
		this.p = p;
		this.x = -Game.WIDTH / 2;
		this.y = -Game.HEIGHT / 2;
		this.speed = 4.4f;
		targetX = x;
		targetY = y;
		isCentering = false;
		camZ = 0;
		camX = 0;
		camZSwingSpeed = 0.5f;
		camIsSwingingClockwise = true;
		swingRadius = 20;
		isResettingSwing = false;
	}
	
	public void update() {
		p.rotate((float) p.radians(45), 1, 0, 0);
		p.rotate((float) p.radians(camZ), 0, 0, 1);
		p.rotate((float) p.radians(camX), 1, 0, 0);
		p.translate(-x, -y-350, -350);

		if (isCentering) {
			// Move east
			if (x + Game.WIDTH / 2 < targetX) {
				x += speed * 3;
			}
			// Move west
			if (x + Game.WIDTH / 2 > targetX) {
				x -= speed * 3;
			}
			// Move north
			if (y + Game.HEIGHT / 2 < targetY) {
				y += speed * 3;
			}
			// Move south
			if (y + Game.HEIGHT / 2 > targetY) {
				y -= speed * 3;
			}
		}
		
		if (isResettingSwing) {
			unswing();
		}

//		swingCamera();
	}
	
	private void swingCamera() {
		if (camIsSwingingClockwise) {
			camZ += camZSwingSpeed;
		} else {
			camZ -= camZSwingSpeed;
		}
		if (camZ > 22) {
			camIsSwingingClockwise = false;
		}
		if (camZ < -22) {
			camIsSwingingClockwise = true;
		}
	}

	public void moveRight() {
		x += speed * 5;
		isCentering = false;
	}

	public void moveLeft() {
		x -= speed * 5;
		isCentering = false;
	}

	public void moveUp() {
		y -= speed * 5;
		isCentering = false;
	}

	public void moveDown() {
		y += speed * 5;
		isCentering = false;
	}

	public void centerOn(int x, int y) {
		targetX = x;
		targetY = y;
		isCentering = true;
	}
	
	public void swingEast() {
		if (camZ > -swingRadius) camZ -= camZSwingSpeed;
		isResettingSwing = false;
	}

	public void swingWest() {
		if (camZ < swingRadius) camZ += camZSwingSpeed;
		isResettingSwing = false;
	}

	public void swingNorth() {
		if (camX < swingRadius / 4) camX += camZSwingSpeed;
		isResettingSwing = false;
	}

	public void swingSouth() {
		if (camX > -swingRadius * 2) camX -= camZSwingSpeed;
		isResettingSwing = false;
	}
	
	private void unswing() {
		if (camZ > 0) camZ -= camZSwingSpeed * 2;
		if (camZ < 0) camZ += camZSwingSpeed * 2;
		if (camX > 0) camX -= camZSwingSpeed * 2;
		if (camX < 0) camX += camZSwingSpeed * 2;
	}
	
	public void resetSwing() {
		isResettingSwing = true;
	}
}
