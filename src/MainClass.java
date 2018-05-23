import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainClass extends JFrame implements MouseListener,
		MouseMotionListener, Runnable, KeyListener {

	public static final int WIDTH = 1200;
	public static final int HEIGHT = 800;

	private ArrayList<Dot> allDots;
	private Dot selected;
	private ArrayList<Bomb> allBombs;

	private BufferedImage offscreen;
	private Graphics bg;
	private boolean paused;

	public MainClass() {
		allBombs = new ArrayList<Bomb>();
		paused = true;
		allDots = new ArrayList<Dot>();
		offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		bg = offscreen.getGraphics();
		Graphics2D g2 = (Graphics2D) bg;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int numRows = 75;
		int numCols = 100;
		int spaceBetween = 6;
		for (int r = 0; r < numRows; r++) {
			Dot previous = null;
			for (int c = 0; c < numCols; c++) {
				Dot current = new Dot(new Color(0.9f, 0.6f, 0.4f, 0.5f), 50 + c
						* spaceBetween, 75 + r * spaceBetween);
				// if(c==0)current.setPin();
				allDots.add(current);
				if (previous != null) {
					current.connect(previous);
					previous.connect(current);
				}
				previous = current;
			}
		}

		for (int r = 1; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				Dot one = allDots.get(r * numCols + c);
				Dot two = allDots.get((r - 1) * numCols + c);
				one.connect(two);
				two.connect(one);
			}
		}
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		new Thread(this).start();
	}

	public void paint(Graphics g) {
		bg.setColor(new Color(0x0B4D63));
		bg.fillRect(0, 0, WIDTH, HEIGHT);
		for (Dot d : allDots)
			d.draw(bg);
		for (Bomb b : allBombs)
			b.draw(bg);
		g.drawImage(offscreen, 0, 0, null);
	}

	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.setResizable(false);
		mc.setSize(WIDTH, HEIGHT);
		mc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mc.setVisible(true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (selected == null)
			return;
		selected.moveTo(e);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (allDots.isEmpty())
				return;
			selected = allDots.get(0);
			for (Dot d : allDots) {
				if (d.getDistance(e) < selected.getDistance(e))
					selected = d;
			}
			selected.setPin();
		} else {
			allBombs.add(new Bomb(e.getX(), e.getY()));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		selected = null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (paused) {
				repaint();
				continue;
			}
			for (Dot d : allDots) {
				if (d != selected)
					d.updateVelocity();
			}
			for (Dot d : allDots) {
				if (d != selected)
					d.updatePosition();
			}
			for (int i = 0; i < allBombs.size(); i++) {
				Bomb b = allBombs.get(i);
				b.update();
				if (b.shouldExplode()) {
					Sound.sound1.play();
					allBombs.remove(b);
					for (Dot d : allDots)
						d.push(b);
				}
			}
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			paused = !paused;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
