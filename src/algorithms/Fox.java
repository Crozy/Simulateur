package algorithms;

import characteristics.IFrontSensorResult;
import characteristics.IFrontSensorResult.Types;
import characteristics.IRadarResult;
import characteristics.Parameters;
import robotsimulator.Brain;

public class Fox extends Brain {
	private static int robotSerialID = 0;
	private int robot ;
	private int state; 
	double posX, posY;
	private int blockedTime;
	private boolean isMoving;
	private static final double HEADINGPRECISION = 0.001;
	private int target1 , target2, target3, target4;
	private int currentTarget;
	private boolean isStratMultipleTargetEnabled,enemyDetected, isBlocked;
	private double lastDirection;
	private double lastPosX, lastPosY;
	private int mapX, mapY;

	public Fox() {
		super(); 
		robot = robotSerialID;
		robotSerialID++;
	}

	public void activate() {

		blockedTime= 0;
		state=0;
		target1=750; target2=1500; target3=2250; target4 =3000;
		if (robot==0) {
			posX = Parameters.teamAMainBot1InitX;
			posY = Parameters.teamAMainBot1InitY;
		} else if (robot==1) {
			posX = Parameters.teamAMainBot2InitX;
			posY = Parameters.teamAMainBot2InitY;
		} else if (robot==2) {
			posX = Parameters.teamAMainBot3InitX;
			posY = Parameters.teamAMainBot3InitY;  
		} else if (robot==3) {
			posX = Parameters.teamASecondaryBot1InitX;
			posY = Parameters.teamASecondaryBot1InitY;  
		} else if (robot==4) {
			posX = Parameters.teamASecondaryBot2InitX;
			posY = Parameters.teamASecondaryBot2InitY;  
		} 
		currentTarget = target1;
		isStratMultipleTargetEnabled=true;
		lastPosX=posX;
		lastPosY=posY;
		isBlocked=false;
		mapX = 0;
		mapY =0 ;
	}

	public void step() {
		enemyDetected = detectEnemy();
		if (!enemyDetected) {
			if (state==0) { //rotate to south
				if (!isHeadingSouth()) {
					if (nothingAhead()) {
						if (robot==1) {
							stepTurn(Parameters.Direction.LEFT);
						}
						stepTurn(Parameters.Direction.RIGHT);									
					}
				} else {
					state=1;
				}
				return;
			} else if (state==1) { //go to south
				if (robot==1 || robot==2 || robot==3) {
					if (isAllyPresent()){
						enhancedMove();
					}
				}
				if (nothingAhead()) {
					enhancedMove();
				} else {
					state=2;
				}
				return;
			} else if (state==2) { //rotate to east/west
				fire();
				if (!isHeadingEast()) {
					if (robot==1) {
						stepTurn(Parameters.Direction.RIGHT);
					}
					stepTurn(Parameters.Direction.LEFT);
				} else {
					state=3;
				}
				return;
			} else if (state==3) { // move to east/west
				if (nothingAhead()) {
					//					mapX = (int)posX >= mapX? (int)posX : mapX;
					enhancedMove();
				} else {
					state=4;
				}
				return;
			} else if (state==4) { // rotate to north
				fire();
				if (!isHeadingNorth()) {
					if (robot==1) {
						stepTurn(Parameters.Direction.RIGHT);
					}
					stepTurn(Parameters.Direction.LEFT);
				} else {
					state=5;
				}
				return;
			} else if (state==5) {// go to north
				if (nothingAhead()) {
					//					mapY = (int)posY >= mapY? (int)posY : mapY;
					enhancedMove();
				} else {
					state=2;
				}
				return;
			} /*else if (state == 1000) { // custom strat 1
				stepTurn(Parameters.Direction.LEFT);
				if (posX>=currentTarget && !isHeading(lastDirection)) {
					stepTurn(Parameters.Direction.LEFT);
				} else {
					if (currentTarget != -1) {
						if (currentTarget==target1)currentTarget=target2;
						if (currentTarget==target2)currentTarget=target3;
						if (currentTarget==target3)currentTarget=target4;
						if (currentTarget==target4) {
							currentTarget=-1;
							state=3;
						}
					}
					return;
				}
			}*/
		} else {
			fire();
		}
	}

	private void updateBlock() {
//		updateBlock({)
		if (lastPosX-posX <= 10 || lastPosY-posY<=10){
			lastPosX=posX;
			lastPosY=posY;
		} else {
			blockedTime++;
			if (blockedTime>200) {
				isBlocked=true;
				blockedTime=0;
			}
		}
	}

	private boolean detectEnemy() {
		if (robot != 3 && robot != 4) {
			for (IRadarResult r :  detectRadar()) {
				if (r.getObjectType().name().equals("OpponentMainBot") || r.getObjectType().name().equals("OpponentSecondaryBot")) {
					if (r.getObjectType().name().equals("Wreck")) {
						state=2;
						return false;
					}
					 if (isAllyPresent()) {
						return false;
					}else {
						return true;					
					}
				}
			}
		}
		return false;
	}

	private void fire() {
		for (IRadarResult r :  detectRadar()) {
			if (r.getObjectType().name().equals("OpponentMainBot") || 
					r.getObjectType().name().equals("OpponentSecondaryBot")) {
				fire(r.getObjectDirection());
			}
		}
		return;
	}

	private boolean nothingAhead() {
		return (detectFront().getObjectType()==IFrontSensorResult.Types.NOTHING);
	}

	private boolean isHeadingSouth() {
		return isHeading(Parameters.SOUTH);
	}

	private boolean isHeadingEast() {
		return isHeading(Parameters.EAST);
	}

	private boolean isHeadingNorth() {
		return isHeading(Parameters.NORTH);
	}

	private boolean isHeading(double dir) {
		return Math.abs(Math.sin(getHeading()-dir))<HEADINGPRECISION;
	}

	private boolean isEnemyPresent() {
		Types type = detectFront().getObjectType();
		return (type == IFrontSensorResult.Types.OpponentMainBot || type == IFrontSensorResult.Types.OpponentMainBot) ;
	}
	private boolean isAllyPresent() {
		Types type = detectFront().getObjectType();
		return (type == IFrontSensorResult.Types.TeamMainBot || type == IFrontSensorResult.Types.TeamSecondaryBot) ;
	}

	private boolean isWallPresent() {
		return (detectFront().getObjectType()==IFrontSensorResult.Types.WALL);
	}

	private void enhancedMove(){
		if (getHealth()>0 ) {
			move();
			if (robot==3||robot==4) {
				posX+=Parameters.teamASecondaryBotSpeed*Math.cos(getHeading());
				posY+=Parameters.teamASecondaryBotSpeed*Math.sin(getHeading());
			} else if (robot==0 || robot==1 || robot==2) {
				posX+=Parameters.teamAMainBotSpeed*Math.cos(getHeading());
				posY+=Parameters.teamAMainBotSpeed*Math.sin(getHeading());
			}
		}
	}

	//sendLogMessage("lastPosX : "+lastPosX+" / posX : "+posX+" / lastPosY : "+lastPosY+" / posY : "+posY);

	//				if ((lastPosX - posX )>=4 || (lastPosY - posY)>=4) {
	//					lastPosX = posX; 
	//					lastPosY = posY; 
	//				} else {
	//					blockedTime++; 
	//					if (blockedTime>200) {
	//						isBlocked=true;
	//						blockedTime=0;
	//					}
	//				}
	//			} else {
	//				state=2;

}

//	private boolean isBlocked(){
//		//		if(inc >)
//		return false;
//	}

//	private void customMove() {
//		if (getHealth()>0) {
//			move();
//			if (robotNumber == 3 || robotNumber == 4) {
//				posX+=Parameters.teamAMainBotSpeed*Math.cos(getHeading());
//				posY+=Parameters.teamAMainBotSpeed*Math.sin(getHeading());
//			} else {
//				posX+=Parameters.teamASecondaryBotSpeed*Math.cos(getHeading());
//				posY+=Parameters.teamASecondaryBotSpeed*Math.sin(getHeading());
//			}
//			isMoving=false;
//		}
//	}
//}