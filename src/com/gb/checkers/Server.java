package com.gb.checkers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	public static final String MOVE = "0";
	public static final String DISCONNECT = "1";

	private ServerSocket socket;
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	
	private boolean running = false;
	
	public boolean madeConnection = false;
	
	public Server() {
		try {
			socket = new ServerSocket(8008);
		} catch (IOException e) {
			e.printStackTrace();
		}
		running = true;
		new Thread(this, "GAME_SERVER").start();
	}
	
	public void run() {

		try {
			client = socket.accept();
			out = new DataOutputStream(client.getOutputStream());
			in = new DataInputStream(client.getInputStream());
			madeConnection = true;
			Game.frame.setTitle(Game.NAME + " : match against " + client.getInetAddress());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String input;
		String[] data;
		try {
			while(running) {
				while(in.available() < 0) { }
				while(in.available() >= 0) {
					input = in.readUTF();
					data = input.split("/");
					PacketParse.parse(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
