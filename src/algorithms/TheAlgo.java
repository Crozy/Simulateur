package algorithms;

import robotsimulator.Brain;

import java.util.Timer;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.Parameters;

public class TheAlgo extends Brain {

	private static int robotID;
	private int robotNum = 0;

	public TheAlgo() {
		super();
	}

	private double myX, myY;

	// Compteur pour se débloquer
	private int compteurBloque = 0;

	// Compteur de tirs
	private int compteurTirs = 0;

	private static final double HEADINGPRECISION = 0.001;
	private String action = "";

	public void activate() {
		// ---PARTIE A MODIFIER/ECRIRE---//

		String action = "";
		robotNum = robotID;
		compteurBloque = 0;

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

		sendLogMessage(action + " X : " + myX + " Y : " + myY);

		if (compteurBloque > 500) {
			if (isHeadingSouth() || isHeadingEast()) {
				compteurBloque = 0;
			} else {
				action = "Essaye de se débloquer";
				stepTurn(Parameters.Direction.RIGHT);
				return;
			}
		}

		for (IRadarResult o : detectRadar()) {
			if (robotNum > 2) {
				if (o.getObjectType().name().equals("BULLET")) {
					myMoveBack();
					action = "Esquive Ball";
					return;
				}
			} else {
				if (o.getObjectType().name().equals("OpponentMainBot")
						|| o.getObjectType().name().equals("OpponentSecondaryBot")) {

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
				} else {
					if (o.getObjectType().name().equals("BULLET")) {
						myMoveBack();
						action = "Esquive Ball";
						return;
					}
				}
			}
		}
		compteurTirs = 0;
		if (nothingAhead()) {
			if (isHeadingSouth() || isHeadingEast()) {
				compteurBloque++;
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

	private void goDiection(double go) {
		if (isHeading(go)) {
			myMove();
		} else {
			stepTurn(Parameters.Direction.RIGHT);
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
		// System.out.println(Math.abs(Math.sin(getHeading() - dir)));
		return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
	}
}