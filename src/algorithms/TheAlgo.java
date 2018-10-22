package algorithms;

import robotsimulator.Brain;

import java.util.Timer;

//import javax.jws.soap.SOAPBinding.ParameterStyle;

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
	// private int compteurTirs = 0;

	// Compteur départ des 3 robots de combat
	private int compteurRobotCombat = 0;

	// Compteur départ des 2 robot tank
	private int compteurTank = 0;
	// private boolean aTourner = false;

	// Compteur placement des robot tank
	private int compteuPlacementTank1, compteuPlacementTank2 = 0;

	// Compteur move
	private int compteurMove = 0;

	// Compteur pour tourner
	// private int compteurTourne = 0;

	// Pour savoir si le robot a tourné
	// private Boolean tourne = false;

	// Compteur pour savoir quand le robot 4 doit tourner
	// private int avanceRobot4 = 0;

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

		// x 3000 y 2000

		//sendLogMessage("CompteurMove : " + compteurMove + " Action : " + action + " X : " + myX + " Y : " + myY);

		sendLogMessage("CompteurMove" + compteurMove + " Action : " + action);

		if (robotNum == 3 && compteurMove >= 100) {
			if (compteuPlacementTank1 <= 50) {
				stepTurn(Parameters.Direction.LEFT);
				compteuPlacementTank1++;
				return;
			}
		}
		
		if (robotNum == 4 && compteurMove >= 100) {
//			if (compteuPlacementTank2 < 20) {
			if (!isHeadingSouth()) {
				stepTurn(Parameters.Direction.LEFT);
				compteuPlacementTank2++;
				return;
			}
		}
		
		if (compteurMove > 2550) {
			myMoveBack();
			compteurMove = 0;
		}
		if (compteurMove > 2500) {
			compteurMove++;
			stepTurn(Parameters.Direction.RIGHT);
			return;
		}

		// Petit retardateur pour que les 3 rebots avance en même temps
		if (compteurRobotCombat < 200 && robotNum < 3) {
			compteurRobotCombat++;
			return;
		}

//		if (robotNum == 3 || robotNum == 4) {
//			if (compteurTank < 50) {
//				compteurTank++;
//				if (robotNum == 3) {
//					stepTurn(Parameters.Direction.LEFT);
//					return;
//				}
//				if (robotNum == 4) {
//					if (avanceRobot4 < 100) {
//						myMove();
//						return;
//					} else {
//						stepTurn(Parameters.Direction.RIGHT);
//						return;
//					}
//				}
//			}
//		}

		if (compteurBloque > 500) {
			if (isHeadingSouth() || isHeadingEast()) {
				compteurBloque = 0;
			} else {
				action = "Essaye de se débloquer";
				stepTurn(Parameters.Direction.RIGHT);
				compteurMove = 0;
				return;
			}
		}

		for (IRadarResult o : detectRadar()) {
			if (robotNum < 3) {
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
				}
			} else {
				if (o.getObjectType().name().equals("BULLET")) {
					myMoveBack();
					action = "Esquive Ball";
					return;
				}
			}

		}
		if (nothingAhead()) {
			if (isHeadingSouth() || isHeadingEast()) {
				compteurBloque++;
				action = "Move ";
				myMove();
				return;
			} else {
				stepTurn(Parameters.Direction.RIGHT);
				compteurMove = 0;
				action = "Tourne à droite car pas droit";
				return;
			}
		} else {
			stepTurn(Parameters.Direction.RIGHT);
			compteurMove = 0;
			action = "Tourne à droite ";
			return;
		}
	}

	private void goDiection(double go) {
		if (isHeading(go)) {
			myMove();
		} else {
			stepTurn(Parameters.Direction.RIGHT);
			compteurMove = 0;
		}
	}

	private void myMove() {
//		if (compteurMove > 100) {
//			if (tourne == false) {
//				tourne();
//			} else {
//				compteurMove = 0;
//			}
//			return;
		// } else {
		compteurMove++;
		myX += Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY += Parameters.teamBMainBotSpeed * Math.sin(getHeading());
		move();
		// }
	}

	private void myMoveBack() {
		myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
		moveBack();
	}

//	private void tourne() {
//		if (compteurTourne < 5000) {
//			stepTurn(Parameters.Direction.LEFT);
//			compteurTourne++;
//			tourne = false;
//		} else {
//			compteurTourne = 0;
//			tourne = true;
//		}
//	}

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