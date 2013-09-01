package com.gb.checkers;

import javax.swing.JOptionPane;

public class PacketParse {
	public static void parse(String[] data) {
		switch(data[0]) {
		case Server.DISCONNECT:
		{
			JOptionPane.showMessageDialog(Game.frame, "Other Player ended session", Game.NAME, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
			break;
		case Server.MOVE:
		{
			int x = Integer.parseInt(data[1]);
			int y = Integer.parseInt(data[2]);
			int tx = Integer.parseInt(data[3]);
			int ty = Integer.parseInt(data[4]);
			Board.movePiece(x, y, tx, ty);
			Board.switchTurn();
		}
			break;
		default:
			break;
		}
	}
}
