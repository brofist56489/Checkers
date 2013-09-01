package com.gb.checkers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Board {
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	public static final int TW = 57;
	public static final int TH = 57;
	public static int OFFX = 92;
	public static int OFFY = 16;
	private static Piece[] pieces;
	
	private static Point movePoint;
	
	public static int myTurn = -1;
	public static int turn = 0;
	
	public static void init() {
		pieces = new Piece[WIDTH * HEIGHT];
		startPos1();
		movePoint = null;
	}
	
	private static void startPos1() {
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				if(y < 3 || y > HEIGHT - 4)
					if((x + y) % 2 == 0) {
						pieces[x + y * WIDTH] = new Piece(x, y, ((y < 3) ? 0 : 1), false);
					}
			}
		}
		
	}
	
	public static Point absPoint() {
		return new Point(movePoint.x / TW, movePoint.y / TH);
	}
	
	public static void movePos() {
		if(movePoint == null) {
			int x = MouseHandler.x - OFFX;
			int y = MouseHandler.y - OFFY;
			if((x / TW + y / TH) % 2 == 0) {
				movePoint = new Point(x, y);
				if(getPiece(x / TW, y / TH) != null) {
					if(turn != myTurn) {
						movePoint = null;
					}
					if(getPiece(x / TW, y / TH).side != myTurn) {
						movePoint = null;
					}
				} else {
					movePoint = null;
				}
			}
			
		} else {
			movePieceAbs(movePoint.x, movePoint.y, MouseHandler.x - OFFX, MouseHandler.y - OFFY);
			movePoint = null;
		}
	}
	
	public static Piece getPiece(int x, int y) {
		if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return null;
		return pieces[x + y * WIDTH];
	}
	
	public static void clearPos() {
		movePoint = null;
	}
	
	public static void movePieceAbs(int x, int y, int tx, int ty) {
		movePiece(x / TW, y / TH, tx / TW, ty / TH);
	}
	
	public static void movePiece(int x, int y, int tx, int ty) {
		clearPos();
		if((x + y) % 2 != 0 || (tx + ty) % 2 != 0) {
			return;
		}
		if(pieces[x + y * WIDTH] == null) {
			return;
		}
		if(pieces[tx + ty * WIDTH] != null) {
			return;
		}
		if(x == tx && y == ty) {
			return;
		}
		int vm = getPiece(x, y).validMove(tx, ty);
		if(vm == -2) {
			return;
		}
		if(vm >= 0) {
			int sx = vm / 10;
			int sy = vm % 10;
			pieces[sx + sy * WIDTH] = null;
		}
		Piece p = pieces[x + y * WIDTH].copy(tx, ty);
		pieces[x + y * WIDTH] = null;
		pieces[tx + ty * WIDTH] = p;
		
		if(p.checkKing()) {
			p.makeKing();
		}

		String msg = Server.MOVE + "/" + x + "/" + y + "/" + tx + "/" + ty;
		if(Game.server == null) {
			Game.client.send(msg);
		} else {
			Game.server.send(msg);
		}
	}
	
	public static void renderBoard(Graphics2D screen) {
		screen.setColor(Color.DARK_GRAY);
		screen.setStroke(new BasicStroke(10));
		screen.drawRect(OFFX, OFFY, WIDTH * TW, HEIGHT * TH);
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				if((x + y) % 2 == 0) {
					if(movePoint != null) {
						if(x == absPoint().x && y == absPoint().y) {
							screen.setColor(Color.white);
						}
						else screen.setColor(Color.red);
					} else
						screen.setColor(Color.red);
				} else {
					screen.setColor(Color.black);
				}
				screen.fillRect(x * TW + OFFX, y * TH + OFFY, TW, TH);
			}
		}
		for(Piece p : pieces) {
			if(p != null) {
				p.render(screen);
			}
		}
	}

	public static void switchTurn() {
		turn = (turn == 0) ? 1 : 0;
	}
}
