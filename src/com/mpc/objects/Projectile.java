package com.mpc.objects;

import java.awt.geom.Point2D;  

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class Projectile {
	private final Point2D ACCELERATION = new Point2D.Double(0, -9.81 * 0.1);

    private final Point2D position = new Point2D.Double();
    private final Point2D velocity = new Point2D.Double();
    public Image img;
    public double speed;
    public double ay = -9.8;
    public double ax = 0;
    public double vx;
    public double vy;
    public double x;
    public double y;
    public double deltaTime = 0.0001;
    public double angle;

    public Projectile() throws SlickException {
    	img = new Image("./resources/images/Particle.png");
    }
	public void update() {
	    this.x += this.vx*this.deltaTime;
	    this.y += this.vy*this.deltaTime;

	    this.vx += this.ax*this.deltaTime;
	    this.vy += this.ay*this.deltaTime;
	}
	
    public Point2D getPosition()
    {
        return new Point2D.Double(position.getX(), position.getY());
    }
    public void setPosition(Point2D point)
    {
        position.setLocation(point);
    }

    public void setVelocity(Point2D point)
    {
        velocity.setLocation(point);
    }

    public void performTimeStep(double dt)
    {
        scaleAddAssign(velocity, dt, ACCELERATION);
        scaleAddAssign(position, dt, velocity);
    }

    private static void scaleAddAssign(
        Point2D result, double factor, Point2D addend)
    {
        double x = result.getX() + factor * addend.getX();
        double y = result.getY() + factor * addend.getY();
        result.setLocation(x, y);
    }
    
    public Circle getCircle() {
    	Circle cir = new Circle((float) x, (float) y, img.getWidth()/2 - 5);
    	return cir;
    }

}