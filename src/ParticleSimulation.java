import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.JFrame;

public class ParticleSimulation extends JFrame implements MouseListener, MouseMotionListener, Runnable
{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;
	
	private ArrayList<Particle> myParticles;
	private Particle selected;
	private int gridSize = 20;

	public BufferedImage offscreen;
	public Graphics bufferGraphics;

	public ParticleSimulation(){
		offscreen = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		bufferGraphics = offscreen.getGraphics();
		myParticles = new ArrayList<Particle>();
		Particle previous;
		for(int x=0 ; x<gridSize ; x++){
			previous = new Particle(x*(WIDTH/gridSize),10);
			myParticles.add(previous);
			previous.anchored = true;
			for(int y=1 ; y<gridSize ; y++){
				Particle next = new Particle(x*(WIDTH/gridSize),10);
				next.top = previous;
				previous.bottom = next;
				myParticles.add(next);
				previous = next;
			}
		}
		for(int x=1 ; x<gridSize ; x++)
		for(int y=0 ; y<gridSize ; y++){
			Particle right = myParticles.get(gridSize*x+y);
			Particle left = myParticles.get(gridSize*(x-1)+y);
			right.left = left;
			left.right = right;
		}
		addMouseListener(this);
		addMouseMotionListener(this);
		Particle.parent = this;
	}
	
	public static void main(String[] args) {
		ParticleSimulation ps = new ParticleSimulation();
		ps.setSize(WIDTH, HEIGHT);
		ps.setResizable(false);
		ps.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Thread(ps).start();
		ps.setVisible(true);
	}

	public void run(){
		while(true){
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Particle p : myParticles) p.updateA(); // update the acceleration of all particles
			for(Particle p : myParticles) p.updateVP(); // update the velocity and position of all particles
			repaint();
		}
	}

	public void repaint(){
		paint(getGraphics());
	}

	public void paint(Graphics g)
	{   if(g==null)return;
	    bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0,0,WIDTH,HEIGHT);
		bufferGraphics.setColor(Color.black);
		for(Particle p : myParticles) p.paint(bufferGraphics);
		g.drawImage(offscreen,0,30,null);
	}

	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public void mousePressed(MouseEvent e){
		selected = myParticles.get(0);
		double minDistance = distance(e, selected);

		for(Particle p : myParticles){
			//p.selected = false;
			double distance = distance(e, p);
			if(distance < minDistance){
				minDistance = distance;
				selected = p;
			}
		}
		selected.selected = true;
	}

	private double distance(MouseEvent e, Particle p){
		int x1 = e.getX();
		int y1 = e.getY();
		double x2 = p.x;
		double y2 = p.y;
		double dx = x1-x2;
		double dy = y1-y2;
		return Math.sqrt(dx*dx+dy*dy);
	}

	public void mouseReleased(MouseEvent e){
		selected.selected = false;
		selected.anchored = !selected.anchored;
	}

	public void mouseDragged(MouseEvent e){
		selected.x = e.getX();
		selected.y = e.getY();
	}

	public void mouseMoved(MouseEvent e){}
}