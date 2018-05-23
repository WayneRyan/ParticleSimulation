import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Dot {
	private double x,y,vX,vY,size;
	private ArrayList<Dot> adjacents;
	private boolean pinned;
	private Color myColor;
	
	public Dot(Color c, int x,int y){
		myColor = c;
		pinned = false;
		this.x = x;
		this.y = y;
		size = 1;
		vX=0;
		vY=0;
		adjacents = new ArrayList<Dot>();
	}
	
	public void setPin(){
		pinned = !pinned;
	}
	
	public void connect(Dot d){
		adjacents.add(d);
	}
	
	public void updatePosition(){
		x += vX;
		y += vY;
	}
	
	public void updateVelocity(){
		for(int i=0 ; i<adjacents.size() ; i++){
			Dot d = adjacents.get(i);
			double dX = d.x - this.x;
			double dY = d.y - this.y;
			vX += dX*0.5;
			vY += dY*0.5;
		}
		vX*= 0.9999;
		vY *= 0.9999;
		vX += 0.005;
		vY += .005;
		if(pinned){
			vX = 0;
			vY = 0;
		}
		
	}
	
	public void draw(Graphics g){
		g.setColor(myColor);
		g.fillOval((int)(x-size/2), (int)(y-size/2), (int)size, (int)size);
		for(Dot d : adjacents){
			g.drawLine((int)this.x, (int)this.y, (int)d.x, (int)d.y);
		}
	}
	
	public double getDistance(MouseEvent me){
		double dX = x - me.getX();
		double dY = y - me.getY();
		return Math.sqrt(dX*dX+dY*dY);
	}
	
	public void moveTo(MouseEvent me){
		x = me.getX();
		y = me.getY();
	}

	public void push(Bomb b) {
		double dX = x-b.getX();
		double dY = y-b.getY();
		double dist = Math.sqrt(dX*dX+dY*dY)+25;
		dX /=dist;
		dY /=dist;
		dist*=dist;
		vX += dX*50000/dist;
		vY += dY*50000/dist;
	}

}
