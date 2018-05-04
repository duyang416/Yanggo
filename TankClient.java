package com.bit.tank;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *这个类的作用是坦克游戏的主窗口 
 * @author DuYang
 *
 */

public class TankClient extends Frame{
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HIGHT = 600;
	//常量一般都是public static final
	
	Tank myTank = new Tank (20, 30, true, this);
	
	List<Tank> tanks = new ArrayList<Tank> ();
	List<Missile> Missiles = new ArrayList<Missile> ();
	List<Explode> explodes = new ArrayList<Explode> ();
	
	Wall w = new Wall (200, 300, 10, 100, this);
	
	Blood b = new Blood ();
	
	Image offScreenImage = null;
	
	public static void main(String[] args) {
		TankClient tc = new TankClient ();
		tc.launch();
	}
	
	public void launch () {
		for (int i=0; i<10; i++) {
			Tank t = new Tank (200 + (i*40), 250, false, this);
			tanks.add(t);
		}
		
		this.setBounds(300, 200, GAME_WIDTH, GAME_HIGHT);
		this.setResizable(false);
		this.setBackground(Color.WHITE);
		this.setTitle("TankWar");
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter () {
			public void windowClosing(WindowEvent e) {
				close ();
				System.exit(0);
			}	
		});
		this.addKeyListener(new KeyMonitor ());
		new Thread (new PaintThread ()).start();
	}
	
	public void close () {
		this.setVisible(false);
	}

	public void paint(Graphics g) {
		if (tanks.isEmpty()) {
			for (int i=0; i<10; i++) {
				Tank t = new Tank (200 + (i*40), 250, false, this);
				tanks.add(t);
			}
		}
		g.drawString("Missiles count: " + Missiles.size(), 30, 60);
		g.drawString("Tanks count: " + tanks.size(), 30, 80);
		g.drawString("Life: " + myTank.getLife(), 30, 100);
		for (int i=0; i<Missiles.size(); i++) {
			Missile m = Missiles.get(i);
			//m.hitTank(enemyTank);
			m.draw(g);
		}
		
		for (int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
		}
		
		for (int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		myTank.draw(g);
		
		w.draw(g);
		
		b.draw(g);
	}
		
	
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HIGHT);
		}
		Graphics offScreenGraphics = offScreenImage.getGraphics();
		Color c = offScreenGraphics.getColor();
		offScreenGraphics.setColor(Color.WHITE);
		offScreenGraphics.fillRect(0, 0, GAME_WIDTH, GAME_HIGHT);
		offScreenGraphics.setColor(c);
		paint (offScreenGraphics);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	private class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint ();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			myTank.keyPressed(e);
			switch (key) {
			case KeyEvent.VK_F2:
				if (!myTank.isLive()) {
					myTank.setLife(100);
					myTank.setLive(true);
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		
		
	}

	
}

















