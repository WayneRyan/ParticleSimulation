import java.awt.Color;
import java.awt.Graphics;


public class Bomb {
	private double x,y,timer;
	
	public Bomb(double x, double y){
		this.x = x;
		this.y = y;
		//timer = Math.random()*.1+.2;
		timer = .05;
	}
	
	public void update(){
		timer -= .0333;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.red);
		g.fillOval((int)x, (int)y, 10, 10);
	}
	
	public boolean shouldExplode(){
		return timer<0;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
