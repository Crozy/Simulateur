package algorithms;

import robotsimulator.Brain;

import java.util.Timer;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.Parameters;

public class theAlgo extends Brain {

	private static int robotID;
	private int robotNum = 0;

	public theAlgo() {
		super();
	}

	private double myX, myY;
	private int state = 0;
	private static final double HEADINGPRECISION = 0.001;
	private String action = "";

	public void activate() {
		// ---PARTIE A MODIFIER/ECRIRE---//

		String action = "";
		robotNum = robotID;
		state = 0;

		if (robotNum == 0) {
			myX = Parameters.teamAMainBot1InitX;
			myY = Parameters.teamAMainBot1InitY;
			robotID++;
		}
		if (robotNum == 1) {
			myX = Parameters.teamAMainBot2InitX;
			myY = Parameters.teamAMainBot2InitY;
			robotID++;
		}
		if (robotNum == 2) {
			myX = Parameters.teamAMainBot3InitX;
			myY = Parameters.teamAMainBot3InitY;
			robotID++;
		}
		if (robotNum == 3) {
			myX = Parameters.teamASecondaryBot1InitX;
			myY = Parameters.teamASecondaryBot1InitY;
			robotID++;
		}
		if (robotNum == 4) {
			myX = Parameters.teamASecondaryBot2InitX;
			myY = Parameters.teamASecondaryBot2InitY;
			robotID = 0;
		}
	}

	public void step() {

		sendLogMessage("Robot : " + robotNum + " Action : " + action + " X : " + myX + "Y : " + myY);

		for (IRadarResult o : detectRadar()) {
			if (o.getObjectType().name().equals("BULLET")) {
				myMoveBack();
				action = "Esquive Ball";
				return;
			}
			if (o.getObjectType().name().equals("OpponentMainBot")) {
				fire(o.getObjectDirection());
				action = "Tire sur un MainBot";
				return;
			} else {
				if (o.getObjectType().name().equals("OpponentSecondaryBot")) {
					fire(o.getObjectDirection());
					action = "Tire sur un SecondaryBot";
					return;
				}
			}

		}

		if (nothingAhead()) {
			if (isHeadingSouth() || isHeadingEast()) {
				action = "Move ";
				myMove();
				return;
			} else {
				stepTurn(Parameters.Direction.RIGHT);
				action = "Tourne à droite car pas droit";
				return;
			}
		} else {
			stepTurn(Parameters.Direction.RIGHT);
			action = "Tourne à droite ";
			return;
		}
	}

	private void myMove() {

		myX += Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY += Parameters.teamBMainBotSpeed * Math.sin(getHeading());
		move();
	}

	private void myMoveBack() {
		myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
		moveBack();
	}

	private boolean nothingAhead() {
		return (detectFront().getObjectType() == IFrontSensorResult.Types.NOTHING);
	}

	private boolean isHeadingSouth() {
		return isHeading(Parameters.SOUTH);
	}

	private boolean isHeadingEast() {
		return isHeading(Parameters.EAST);
	}

	private boolean isHeading(double dir) {
		return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
	}
}