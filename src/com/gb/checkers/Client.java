package com.gb.checkers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client implements Runnable {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private String target = "localhost";
	
	private boolean running = false;
	
	public boolean madeConnection = false;
	
	public Client() {
		running = true;
		new Thread(this, "GAME_CLIENT").start();
	}
	
	public void run() {
		try {
			target = JOptionPane.showInputDialog(Game.frame, "Host IP:", Game.NAME, JOptionPane.PLAIN_MESSAGE);
			
			socket = new Socket(InetAddress.getByName(target), 8008);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			madeConnection = true;
			Game.frame.setTitle(Game.NAME + " : match against " + target);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Game.frame, "Unknown IP", Game.NAME, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		String input;
		String[] data;
		while(running) {
			try {
				while(in.available() < 0) { }
				while(in.available() >= 0) {
					input = in.readUTF();
					data = input.split("/");
					PacketParse.parse(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void send(String msg) {
		try {
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
