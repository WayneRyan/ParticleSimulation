import java.awt.Graphics;
import java.awt.Color;

public class Particle
{
	public double x,   y;
	public double Vx, Vy;
	public double Ax, Ay;
	public Particle top, bottom,left,right;
	public boolean selected;
	public boolean anchored;
	public static ParticleSimulation parent;

	public Particle(double x, double y){
		this.x = x;
		this.y = y;
		Vx = 0;
		Vy = 0;
		Ax = 0;
		Ay = 0;
		selected = false;
		anchored = false;
	}

	public void paint(Graphics g){
		if(selected)
			g.setColor(Color.green);
		else if(anchored)
			g.setColor(Color.blue);
		else
			g.setColor(Color.black);
		g.fillOval((int)x,(int)y,10,10);
		if(top != null) g.drawLine((int)top.x+5,(int)top.y+5, (int)x+5,(int)y+5);
		if(left != null) g.drawLine((int)left.x+5,(int)left.y+5, (int)x+5,(int)y+5);
	}
	public void updateVP(){
		if(!selected && !anchored){
			Vx = (Vx + Ax);
			Vy = (Vy + Ay);
			Vx *= 0.98;
			Vy *= 0.98;
			//Vy += 0.15;
			x = x + Vx;
			y = y + Vy;		
		}
	}

	public void updateA(){
		if(!selected && !anchored){
			double dbx  = bottom==null ? 0.0 : bottom.x-x;
			double dby  = bottom==null ? 0.0 : bottom.y-y;
			//double distanceB = Math.sqrt(dbx*dbx + dby*dby);
			//dbx = (dbx/distanceB)*(distanceB);
			//dby = (dby/distanceB)*(distanceB);
			double dtx  = top==null ? 0.0 : top.x-x;
			double dty  = top==null ? 0.0 : top.y-y;
			//double distanceT = Math.sqrt(dtx*dtx + dty*dty);
			//dtx = (dtx/distanceT)*(distanceT);
			//dty = (dty/distanceT)*(distanceT);
			double dlx  = left==null ? 0.0 : left.x-x;
			double dly  = left==null ? 0.0 : left.y-y;
			//double distanceL = Math.sqrt(dlx*dlx + dly*dly);
			//dlx = (dlx/distanceL)*(distanceL);
			//dly = (dly/distanceL)*(distanceL);
			double drx  = right==null ? 0.0 : right.x-x;
			double dry  = right==null ? 0.0 : right.y-y;
			//double distanceR = Math.sqrt(drx*drx + dry*dry);
			//drx = (drx/distanceR)*(distanceR);
			//dry = (dry/distanceR)*(distanceR);

			Ax = (dbx + dtx + dlx + drx)*0.1;
			Ay = (dby + dty + dly + dry)*0.1;
			//System.out.println("Ax=" + Ax + " Ay=" + Ay);
		}
		/*
		if(x>parent.dim.width || x<0){
			Vx *=-1;
		}
		if(y>parent.dim.height || y<0){
			Vy *=-1;
		}
		*/
		Vy += 0.1;

	}
}
