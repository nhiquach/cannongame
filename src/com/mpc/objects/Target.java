package com.mpc.objects;

import org.newdawn.slick.Image;   
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class Target {
	protected Image target = null;
	protected float x;
	protected float y;
	protected float vely;
	protected float velx;
	
	
	public Target(float x, float y) throws SlickException{
		target = new Image("./resources/images/Target.png");
		this.x = x;
		this.y = y;
		
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean intersects(Projectile projectile) {
		Circle targ = new Circle(x, y, target.getWidth()/2);
		
		if(targ.intersects(projectile.getCircle())){
			return true;
		}
		
		return false;
	}	
	
	
}
