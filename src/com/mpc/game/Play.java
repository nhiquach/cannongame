package com.mpc.game;


import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random; 

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.mpc.objects.Cannon;
import com.mpc.objects.Projectile;
import com.mpc.objects.Target;

public class Play extends BasicGameState {

	Image cannonImg;
	Image targetImg;
	Image projImg;
	Image cannonBaseImg;
	Cannon cannon;
	Target target;
	Projectile proj;
	boolean hit = false;
	boolean fired = false;
	float cannonRadius;
	double angleToTurn;
	double deltaTime = 0.02;
	double endTime = 1000;
	double time = 0;
	
	//CSV file vals
	static String currDate;
	static String currAng;
	static String currPwr;
	static String TarX;
	static String TarY;
	static String InitX;
	static String InitY;
	static String finalX;
	static String finalY;
	static String targetHit;
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "time_fired,angle,power,targetX,targetY,initX,initY,finalX,finalY,targetHit";

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		cannonBaseImg = new Image("./resources/images/cannonbase8.png");
		cannon = new Cannon(55, 500);
		cannonImg = new Image("./resources/images/rect.png");
		int randX = randInt(1000, 1200);
		int randY = randInt(450, 625);
		target = new Target((float) randX, (float) randY);
		targetImg = new Image("./resources/images/Target.png");
		projImg = new Image("./resources/images/Particle.png"); 
		angleToTurn = cannon.getAngle();
	}
	
	private int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
	
	public static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateCreated = dateFormat.format(date);
        return dateCreated;
    }
	
	public static void writeCsvFile() {		
		FileWriter fileWriter = null;				
		try {
			fileWriter = new FileWriter("data.csv", true);
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			fileWriter.append(currDate);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(currAng);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(currPwr);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(TarX);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(TarY);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(InitX);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(InitY);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(finalX);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(finalY);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(targetHit);
			fileWriter.append(NEW_LINE_SEPARATOR);	
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter!!!");
			e.printStackTrace();
		} finally {		
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter!!!");
                e.printStackTrace();
			}	
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		g.setBackground(Color.darkGray);
		g.setColor(Color.white);
		g.drawImage(cannonBaseImg, 25, 600);
		g.drawImage(cannonImg, cannon.xPos, cannon.yPos);
		
		g.drawImage(targetImg,target.getX(), target.getY());
		
		g.drawString("Angle: "+ String.valueOf((int) cannon.getAngle()), 100, 100);
		g.drawString("Power: "+ String.valueOf((int) cannon.power), 100, 150);
		
		if (fired && time < endTime) { 
			proj.x += proj.vx * this.deltaTime;
		    proj.y -= proj.vy * this.deltaTime;
		    if (target.intersects(proj)) {
		    	hit = true;
		    	finalX = String.valueOf(proj.x);
		    	finalY = String.valueOf(proj.y);
		    }
		    proj.vx += proj.ax * this.deltaTime;
		    proj.vy += proj.ay * this.deltaTime;
            g.drawImage(projImg, (float) proj.x, (float) proj.y);
            time += deltaTime;
		}
		if (hit) {
			g.drawString("Target hit!", 560, 250);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		Input userInput = gc.getInput();
		if (userInput.isKeyDown(Input.KEY_RIGHT) && cannon.power <= 200) {
			cannon.power += 0.05;
		}
		if (userInput.isKeyDown(Input.KEY_LEFT) && cannon.power > 0) {
			cannon.power -= 0.05;
		}
		if (userInput.isKeyDown(Input.KEY_UP)) {
			angleToTurn += 0.05;
		}
		if (userInput.isKeyDown(Input.KEY_DOWN)) {
			angleToTurn -= 0.05;
		}
		if (proj == null && userInput.isKeyPressed(Input.KEY_SPACE)) {
			fired = true;
			hit = false;
			proj = cannon.executeShot();
			proj.x = 79 + Math.cos(cannon.getAngle()*(Math.PI/180.0)) * 120;
			proj.y = 652 - Math.sin(cannon.getAngle()*(Math.PI/180.0)) * 120;
			proj.vx = cannon.power*Math.cos(cannon.getAngle()*(Math.PI/180.0));
		    proj.vy = cannon.power*Math.sin(cannon.getAngle()*(Math.PI/180.0));
		    
		    //CSV data
		    currDate = currentDate();
		    currAng = String.valueOf((int)cannon.getAngle());
		    currPwr = String.valueOf((int)cannon.power);
		    InitX = String.valueOf(proj.x);
		    InitY = String.valueOf(proj.y);
		    TarX = String.valueOf(target.getX());
		    TarY = String.valueOf(target.getY());
		}
		
		if (proj != null && (proj.y < -200 || proj.y >= 720 || proj.x >= 1280)) {
			if (!hit) {
				finalX = String.valueOf(proj.x);
				finalY = String.valueOf(proj.y);
			}
			targetHit = String.valueOf(hit);
			writeCsvFile();
			fired = false;
			proj = null;
			time = 0;
			if (hit) {
				int randX = randInt(1000, 1200);
				int randY = randInt(450, 625);
				target = new Target((float) randX, (float) randY);
			}
		}

		cannonImg.setCenterOfRotation(40, 170);
		cannonImg.setRotation((float) -angleToTurn);
		cannon.setAngle((float) angleToTurn);
	}

	@Override
	public int getID() {
		return 1;
	}

}
