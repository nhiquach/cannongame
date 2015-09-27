package com.mpc.objects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.newdawn.slick.SlickException;

public class Cannon {

	float angle;
	public float power;
	public float xPos;
	public float yPos;
	Projectile proj;

	public Cannon(float xPos, float yPos) {
		this.angle = 45;
		this.xPos = xPos;
		this.yPos = yPos;
		this.power = 100;
	}

	public Projectile executeShot() throws SlickException {
        proj = new Projectile();
        double angleRad = Math.toRadians(angle);
        Point2D velocity = AffineTransform.getRotateInstance(angleRad).transform(new Point2D.Double(1,0), null);
        velocity.setLocation(velocity.getX() * power * 0.5, velocity.getY() * power * 0.5);
        proj.setVelocity(velocity);
        return proj;
    }

	public Projectile getProjectile() {
        return proj;
    }

	public void setPosition(float x, float y) {
		xPos = x;
		yPos = y;
	}
	
	public void setAngle(float a) {
		angle = a;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void setPower(float pwr) {
		power = pwr;
	}

}
