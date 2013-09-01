package com.gb.checkers;

import java.awt.Color;
import java.awt.Graphics2D;

public class Piece {
	public boolean isKing;
	public int x;
	public int y;
	public int side;
	
	public Piece(int x, int y, int side, boolean isKing) {
		this.x = x;
		this.y = y;
		this.side = side;
		this.isKing = isKing;
	}
	
	public Piece copy(int x, int y) {
		return new Piece(x, y, side, isKing);
	}
	
	public int validMove(int tx, int ty) {
		double d = Math.sqrt((x - tx) * (x - tx) + (y - ty) * (y - ty));
		
		int dx, dy;
		Piece p, p2;
		for(int i = 0; i < 4; i++) {
			dx = (i == 0 || i == 3) ? -1 : 1;
			if(isKing)
				dy = (i == 0 || i == 1) ? -1 : 1;
			else
				dy = (side == 0) ? 1 : -1;
			
			p = Board.getPiece(dx + x, dy + y);
			if(p == null) continue;
			if(p.side != side) {
				if(tx == x + dx * 2 && ty == y + dy * 2) {
					p2 = Board.getPiece(tx, ty);
					if(p2 == null)
						return (x + dx) * 10 + (y + dy);
				}
			}
		}
		
		if(d < 1.5) {
			if(side == 0 && ty - y <= 0 && !isKing) {
				return -2;
			}
			if(side == 1 && y - ty <= 0 && !isKing) {
				return -2;
			}
			return -1;
		} else
			return -2;
	}
	
	public boolean checkKing() {
		if(side == 0) {
			if(y == Board.HEIGHT - 1) {
				return true;
			}
		}
		if(side == 1) {
			if(y == 0) {
				return true;
			}
		}
		return false;
	}
	
	public void makeKing() {
		isKing = true;
	}
	
	public void render(Graphics2D screen) {
		if(side == 0) {
			screen.setColor(Color.decode("" + 0xffaf0000));
		} else {
			screen.setColor(Color.gray);
		}
		
		screen.fillOval(x * Board.TW + Board.OFFX + 2, y * Board.TH + Board.OFFY + 2, Board.TW - 4, Board.TH - 4);
		if(isKing) {
			screen.setColor(Color.yellow);
			screen.fillOval(x * Board.TW + Board.OFFX + 8, y * Board.TH + Board.OFFY + 8, Board.TW - 16, Board.TH - 16);
		}
	}
}
