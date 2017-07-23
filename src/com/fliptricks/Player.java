package com.fliptricks;

import processing.core.PApplet;

public class Player {
	private PApplet p;
	public int x;
	public int y;
	public int z;
	public int w;
	public int h;
	private int rx;
	private int ry;
	private int tr;
	int[] color = {120, 140, 160, 255};
	private boolean isMovingRight;
	private boolean isMovingLeft;
	private boolean isMovingUp;
	private boolean isMovingDown;
	private Camera cam;
	private float prevX;
	private float prevY;
	private int prevTimer;
	private float gravity;
	private int jumpTimer;
	private boolean isJumping;
	private int jumpDuration;
	public boolean isDead;
	private int breathTick;
	
	public Player(PApplet p, int x, int y, int z, Camera cam) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 50;
		this.h = 50;
		this.cam = cam;

		prevX = this.x;
		prevY = this.y;

		rx = 0;
		ry = 0;
		tr = 60/14;

		isMovingRight = false;
		isMovingLeft = false;
		isMovingUp = false;
		isMovingDown = false;
		
		gravity = 55.0f;
		jumpTimer = 0;
		isJumping = false;
		jumpDuration = 60/4;
		
		isDead = false;
		breathTick = 0;
	}

	public void update() {
		if (rx == 0 && ry == 0) {
			if (isMovingRight) x += w;
			if (isMovingLeft) x -= w;
			if (isMovingUp) y -= h;
			if (isMovingDown) y += h;
		}

		if (rx > 0) {
			// Move right
			p.translate(x+w, y, z);
			p.rotate((float) p.radians(p.map(rx, 0, tr, 180, 360)), 0, -1, 0);
			p.translate(-x-w, -y, -z);
			rx--;
		} else if (rx < 0) {
			// Move left
			p.translate(x, y, z);
			p.rotate((float) p.radians(p.map(rx, 0, tr, 180, 0)), 0, 1, 0);
			p.translate(-x, -y, -z);
			rx++;
		} else if (ry > 0) {
			// Move up
			p.translate(x+h, y, z);
			p.rotate((float) p.radians(p.map(ry, 0, tr, 180, 360)), -1, 0, 0);
			p.translate(-x-h, -y, -z);
			ry--;
		} else if (ry < 0) {
			// Move down
			p.translate(x, y+h, z);
			p.rotate((float) p.radians(p.map(ry, 0, tr, 180, 0)), 1, 0, 0);
			p.translate(-x, -y-h, -z);
			ry++;
		} else {
			rx = 0;
			isMovingRight = false;
			isMovingLeft= false;
			isMovingUp= false;
			isMovingDown= false;
		}
		
		if (tr >= 60/4) {
			adjustCameraPositionEveryNTicks(60);
		} else {
			adjustCameraPosition();
		}

		if (!isMovingLeft && !isMovingRight &&
				!isMovingUp && !isMovingDown) {
			cam.resetSwing();
		}

		gravitate();
		
		breathe();
	}
	
	private void adjustCameraPosition() {
		cam.centerOn(x, y);
	}

	private void adjustCameraPositionEveryNTicks(int n) {
		if (prevTimer > n) {
			prevTimer = 0;
			prevX = x;
			prevY = y;
			cam.centerOn(x, y);
		} else {
			prevTimer++;
		}
	}

	private void adjustCameraPositionEveryNTicksWithDistanceM(int n, int m) {
		adjustCameraPositionEveryNTicks(n);
		cam.centerOn(x, y);
		if (Math.abs(prevX - x) > w * m) {
			cam.centerOn(x, y);
		}
		if (Math.abs(prevY - y) > h * m) {
			cam.centerOn(x, y);
		}
	}

	public void render() {
		p.pushStyle();
		p.noStroke();
		p.fill(color[0], color[1], color[2]);
		p.translate(x, y, z);
		p.rect(0, 0, w, h);

//		p.ellipse(0+w/2, 0+h/2, w, h);

//		p.lights();
//		p.translate(w/2, h/2, w/2);
//		p.translate(0, 0, 80);
//		p.fill(80, 80, 80);
//		p.noStroke();
//		p.sphereDetail(8);
//		p.sphere(50);
//		p.translate(0, 0, -80);
//
//		p.translate(150, 50, 50);
//		p.pointLight(0, 255, 0, 0, 0, -1);
//		p.translate(-150, -50, -50);
//
//		p.noStroke();
//		p.fill(0, 255, 255);
//		p.box(w/2, h/2, w/2);

		p.popStyle();
	}
	
	public void moveRight() {
		if (!isMovingRight && !isMovingLeft &&
				!isMovingUp && !isMovingDown) {
			if (rx == 0) rx = tr;
			isMovingRight = true;
		}
		cam.swingEast();
	}

	public void moveLeft() {
		if (!isMovingRight && !isMovingLeft &&
				!isMovingUp && !isMovingDown) {
			if (rx == 0) rx = -tr;
			isMovingLeft = true;
		}
		cam.swingWest();
	}

	public void moveUp() {
		if (!isMovingRight && !isMovingLeft &&
				!isMovingUp && !isMovingDown) {
			if (ry == 0) ry = tr;
			isMovingUp= true;
		}
		cam.swingNorth();
	}

	public void moveDown() {
		if (!isMovingRight && !isMovingLeft &&
				!isMovingUp && !isMovingDown) {
			if (ry == 0) ry = -tr;
			isMovingDown= true;
		}
		cam.swingSouth();
	}
	
	private void gravitate() {
		if (z > 0) {
			z -= gravity;
		} else if (z < 0 && x < 800) {
			z = 0;
		}
		
		if (x > 800-w) {
			z -= gravity;
			if (z < -800) {
				die();
			}
		}
		
		// Handle jumping
		if (jumpTimer > 0) {
			jumpTimer--;
			if (z < w * 4) {
				z += p.map(jumpTimer, 0, jumpDuration, 0, 100);
			}
		} else {
			isJumping = false;
		}
	}

	private void die() {
		isDead = true;
	}

	public void jump() {
		if (!isJumping) {
			jumpTimer = jumpDuration;
			isJumping = true;
		}
	}
	
	private void breathe() {
		if (!isMovingUp && !isMovingLeft &&
				!isMovingDown && !isMovingRight) {
			if (breathTick == 30) {
				breathTick = 0;
			} else {
				breathTick++;
			}
			if (breathTick % 30 == 0) {
				color[0] = 200;
				color[1] = 200;
				color[2] = 200;
				color[3] = 255;
			} else {
				color[0] = 120;
				color[1] = 140;
				color[2] = 160;
				color[3] = 255;
			}
		}
	}
}
