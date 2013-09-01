package com.gb.checkers;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 520;
	public static final String NAME = "Checkers";
	
	public static JFrame frame;
	private Thread gameThread;
	private boolean running = false;
	public int tickCount = 0;
	
	public static Server server = null;
	public static Client client = null;
	
	public synchronized void start() {
		if(running) return;
		running = true;
		gameThread = new Thread(this, "MAIN_game");
		gameThread.start();
		
		if(JOptionPane.showConfirmDialog(Game.frame, "Run Server? ", NAME, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == 0) {
			server = new Server();
//			ConsoleWindow.output("Starting Server");
		} else {
			client = new Client();
//			ConsoleWindow.output("Starting Client");
		}
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	private void init() {
		Board.init();
		MouseHandler.init(this);
		Board.myTurn = (server == null) ? 1 : 0;
	}
	
	public void run() {
		init();
		
		long lt = System.nanoTime();
		double delta = 0.0;
		double nsPt = 1_000_000_000/60.0;
		long now;
		boolean shouldRender = false;
		
		while(running) {
			now = System.nanoTime();
			delta += (now - lt) / nsPt;
			lt = now;
			shouldRender = false;
			
			for(; delta >= 1; delta--) {
				tick();
				shouldRender = true;
			}
			
			if(shouldRender) {
				baseRender();
			}
		}
	}
	
	private void tick() {
		tickCount++;
		MouseHandler.poll();
		
		if(server != null) {
			if(server.madeConnection) {
				if(MouseHandler.buttonDownOnce(1)) {
					Board.movePos();
				}
				if(MouseHandler.buttonDownOnce(3)) {
					Board.clearPos();
				}
			}
		}
		if(client != null) {
			if(client.madeConnection) {
				if(MouseHandler.buttonDownOnce(1)) {
					Board.movePos();
				}
				if(MouseHandler.buttonDownOnce(3)) {
					Board.clearPos();
				}
				
			}
		}
	}
	
	private Graphics2D render(Graphics2D screen) {
		Board.renderBoard(screen);
		
		return screen;
	}
	
	private void baseRender() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics2D screen = (Graphics2D) bs.getDrawGraphics();
		screen.setColor(Color.WHITE);
		screen.fillRect(0, 0, WIDTH, HEIGHT);
		
		screen = render(screen);
		
		screen.dispose();
		bs.show();
	}
	
	
	public static void main(String[] args) {
		Game game = new Game();
		frame = new JFrame(NAME);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {	}
			public void windowClosed(WindowEvent arg0) { }

			public void windowClosing(WindowEvent arg0) {
				if(client != null)
					client.send(Server.DISCONNECT);
				else
					server.send(Server.DISCONNECT);
			}
			
			public void windowDeactivated(WindowEvent arg0) { }
			public void windowDeiconified(WindowEvent arg0) { }
			public void windowIconified(WindowEvent arg0) { }
			public void windowOpened(WindowEvent arg0) { }
		});
		frame.add(game);
		frame.setVisible(true);
//		ConsoleWindow.init();
		game.start();
	}
}
