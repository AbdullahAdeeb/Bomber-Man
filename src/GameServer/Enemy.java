package GameServer;

import java.util.Random;

public class Enemy {
	private int locX;
	private int locY;
	
	public int getX(){
		return locX;
	}
	public int getY(){
		return locY;
	}
	
	public Enemy(int xBoundary, int yBoundary){
		
		//Randomly positions the enemy
		 Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(xBoundary);
		 
		 locX = randomInt;
		 
		 randomInt = randomGenerator.nextInt(yBoundary);
		 
		 locY = randomInt;
		  
	}
	
	public void move(){
		
		//Randomly moves the enemy, no path checking
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
		 
		if (randomInt==0){
			locX++;
		}else if (randomInt == 1){
			locX--;
		}
		 
		randomInt = randomGenerator.nextInt(2);
		if (randomInt==0){
			locY++;
		}else if (randomInt == 1){
			locY--;
		}
		 
	}
}
