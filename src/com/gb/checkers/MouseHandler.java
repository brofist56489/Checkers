package com.gb.checkers;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {

	private static final int BUTTON_COUNT = 3;
	public static int x;
	public static int y;
	private static int currentPosX;
	private static int currentPosY;
	private static boolean[] state = new boolean[BUTTON_COUNT];
	private static MouseState[] poll = new MouseState[] { MouseState.RELEASED, MouseState.RELEASED, MouseState.RELEASED };

	private enum MouseState {
		RELEASED, PRESSED, ONCE
	}

	public static void init(Game game) {
		MouseHandler m = new MouseHandler();
		game.addMouseListener(m);
		game.addMouseMotionListener(m);
	}
	
	public static Rectangle getRect() {
		return new Rectangle(currentPosX, currentPosY, 1, 1);
	}

	public static void poll() {
		x = currentPosX;
		y = currentPosY;
		for (int i = 0; i < BUTTON_COUNT; ++i) {
			if (state[i]) {
				if (poll[i] == MouseState.RELEASED)
					poll[i] = MouseState.ONCE;
				else
					poll[i] = MouseState.PRESSED;
			} else {
				poll[i] = MouseState.RELEASED;
			}
		}
	}

	public static boolean buttonDownOnce(int button) {
		return poll[button - 1] == MouseState.ONCE;
	}

	public static boolean buttonDown(int button) {
		return poll[button - 1] == MouseState.ONCE || poll[button - 1] == MouseState.PRESSED;
	}

	public synchronized void mousePressed(MouseEvent e) {
		state[e.getButton() - 1] = true;
	}

	public synchronized void mouseReleased(MouseEvent e) {
		state[e.getButton() - 1] = false;
	}

	public synchronized void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	public synchronized void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}

	public synchronized void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	public synchronized void mouseMoved(MouseEvent e) {
		currentPosX = e.getX();
		currentPosY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {

	}

}