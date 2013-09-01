package com.gb.checkers;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ConsoleWindow {
	
	private static JTextArea output;
	private static JFrame frame;
	
	public static void init() {
		output = new JTextArea();
		frame = new JFrame();
		frame.setTitle(Game.NAME + " Console Window");
		frame.setSize(200, 500);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.add(output);
		frame.setVisible(true);
		output.setEditable(false);
	}
	
	public static void output(String msg) {
		output.setText(output.getText() + "\n" + msg);
	}
	
	public static void clear() {
		output.setText("");
	}
}
