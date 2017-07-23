package com.fliptricks;

import java.util.HashMap;

import processing.core.PApplet;

public class Game extends PApplet {
	static int WIDTH = 680;
	static int HEIGHT = 480;
	private Player player;
	private Camera camera;
	private HashMap<Character, Boolean> keys;
	private HashMap<Integer, Boolean> keyCodes;

	public void settings() {
		size(640, 480, P3D);
	}

	public void setup() {
		camera = new Camera(this);
		player = new Player(this, 0, 0, 0, camera);
		keys = new HashMap<Character, Boolean>();
		keyCodes = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 300; i++) {
			keys.put((char) i, false);
			keyCodes.put(i, false);
		}
	}

	public void draw() {
		clear();
		if (player.isDead) {
			background(240, 50, 60);
		} else {
			background(40, 50, 60);
		}

		handleKeys();
		
		camera.update();
		renderCoordinates();

		if (!player.isDead) {
			player.update();
			player.render();
		} else {
			camera();
			noStroke();
			fill(255, 0, 0, 100);
			rect(0, 0, WIDTH, HEIGHT);
			fill(255, 255, 255);
		}
	}
	
	private void renderCoordinates() {
		int length = 800;
		pushStyle();
		pushMatrix();
		noFill();
		// x (red)
		stroke(255, 0, 0);
		line(0f, 0f, 0f, length, 0f, 0f);
		// y (green)
		stroke(0, 0255, 0);
		line(0f, 0f, 0f, 0f, length, 0f);
		// z (blue)
		stroke(0, 0, 255);
		line(0f, 0f, 0f, 0f, 0f, length);

		stroke(255, 255, 255);
		// negative x (red)
		line(0f, 0f, 0f, -length, 0f, 0f);
		// negative y (green)
		line(0f, 0f, 0f, 0f, -length, 0f);
		// negative z (blue)
		line(0f, 0f, 0f, 0f, 0f, -length);
		popMatrix();
		popStyle();

		// Plane
		pushStyle();
		pushMatrix();
		noStroke();
		fill(20, 30, 40);
//		rect(-length, -length, length*2, length*2);
		stroke(255, 255, 255, 180);
		translate(0, 0, -50);
		box(length*2, length*2, 100);
		popMatrix();
		popStyle();
	}

	public void handleKeys() {
		if (keyPressed) {
			keys.put(key, true);
			keyCodes.put(keyCode, true);
		}
		
		if (keyCodes.get(RIGHT) == true) {
			player.moveRight();
		}
		if (keyCodes.get(LEFT) == true) {
			player.moveLeft();
		}
		if (keyCodes.get(UP) == true) {
			player.moveUp();
		}
		if (keyCodes.get(DOWN) == true) {
			player.moveDown();
		}
		if (keys.get('d') == true) {
			camera.moveRight();
		}
		if (keys.get('a') == true) {
			camera.moveLeft();
		}
		if (keys.get('w') == true) {
			camera.moveUp();
		}
		if (keys.get('s') == true) {
			camera.moveDown();
		}
		if (keys.get(' ') == true) {
			player.jump();
		}
	}
	
	public void keyReleased() {
		keys.put(key, false);
		keyCodes.put(keyCode, false);
	}

	public static void main(String[] args) {
		PApplet.main("com.fliptricks.Game");
	}
}
