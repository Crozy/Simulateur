package algorithms;

import robotsimulator.Brain;
import robotsimulator.FrontSensorResult;

import java.util.Timer;

//import javax.jws.soap.SOAPBinding.ParameterStyle;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.Parameters;

public class AlgoCombatBot extends Brain {

	private static int robotID;
	private int robotNum = 0;

	public AlgoCombatBot() {
		super();
	}

	private int state = 0;

	private double myX, myY;

	// Compteur pour se débloquer
	private int compteurBloque = 0;

	// Compteur de tirs
	// private int compteurTirs = 0;

	// Compteur départ des 3 robots de combat
	private int compteurRobotCombat = 0;

	// Compteur placement des robot tank
	private int compteuPlacementTank1, compteuPlacementTank2 = 0;
	private boolean mouvementDeDepart = false;

	// Mouvement de déart des robot tireur
	private boolean mouvementDeDepart2, mouvementDeDepart3, mouvementDeDepart4 = false;
	private int compteurDepartRobot1 = 0;

	// Compteur move
	private int compteurMove = 0;

	// compteur tourne
	private int compteurTourne = 0;

	private static final double HEADINGPRECISION = 0.001;
	private String action = "";

	// Pour moveback tank
	private boolean moveBack = false;
	private int compteurMoveBack = 0;

	private double directionEnnemi = 0;
	private boolean positionDetecter = false;

	private boolean avanceDebut = false;

	private int compteurDeplacement = 0;

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

		// sendLogMessage("CompteurMove : " + compteurMove + " Action : " + action + " X
		// : " + myX + " Y : " + myY);

		sendLogMessage("y : " + myY + "x : " + myX + "CompteurMove" + compteurMove + " Action : " + action);

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
			}
		}

		// Petit retardateur pour que les 3 rebots avance en même temps
		if (compteurRobotCombat < 500 && robotNum == 1) {
			compteurRobotCombat++;
			return;
		}

		if (compteurRobotCombat < 800 && robotNum == 0) {
			compteurRobotCombat++;
			return;
		}

		if (robotNum > 2) {
			if (!isHeadingSouth()) {
				stepTurn(Parameters.Direction.LEFT);
				return;
			} else {
				myMove();
				return;
			}
		}

		if (robotNum < 3) {
			if (state == 0) {
				if (!isHeadingSouth()) {
					stepTurn(Parameters.Direction.RIGHT);
					action = "Mouvement de depart tourne";
					return;
				} else {
					if (nothingAhead()) {
						myMove();
						return;
					} else {
						compteurDeplacement = 0;
						state = 1;
					}
				}
			}

			if (state == 1) {
				if (!isHeadingEast()) {
					stepTurn(Parameters.Direction.LEFT);
					return;
				} else {
					state = 2;
				}
			}

			if (state == 2) {
//				if (robotNum == 0 && myX >= 2600) {
//					state = 3;
//					return;
//				}
//				if (robotNum == 1 && myX >= 2700) {
//					state = 3;
//					return;
//				}
//				if (robotNum == 2 && myX >= 2800) {
//					state = 3;
//					return;
//				}
				if (myX < 3000) {
					myMove();
					return;
				} else {
					state = 3;
				}
			}

			if (state == 3) {
				if (compteurTourne < 50) {
					compteurTourne++;
					stepTurn(Parameters.Direction.LEFT);
					return;
				} else {
					state = 4;
				}
			}

			if (state == 4) {
				if (myY > 100 && robotNum == 2) {
					myMove();
					return;
				}
				if (myY > 200 && robotNum == 1) {
					myMove();
					return;
				}
				if (myY > 300 && robotNum == 0) {
					myMove();
					return;
				}
				state = 6;
			}

			if (state == 6) {
				if (!isHeadingEast()) {
					stepTurn(Parameters.Direction.LEFT);
					return;
				} else {
					myMove();
					return;
				}
			}
		}

		///////////////////////////////////////////////////////////////////
		if (robotNum > 2 && state == 7) {
			if (nothingAhead()) {
				if (isHeadingSouth() || isHeadingEast()) {
					compteurBloque++;
					action = "Move ";
					myMove();
					return;
				} else {
					stepTurn(Parameters.Direction.RIGHT);
					action = "Tourne à droite car pas droit";
					compteurMove = 0;
					return;
				}
			} else {
				stepTurn(Parameters.Direction.RIGHT);
				compteurMove = 0;
				action = "Tourne à droite ";
				return;
			}
		}
		//////////////////////////////////////////////////
	}

	private void myMove() {
		compteurMove++;
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

	private boolean isWall() {
		return (detectFront().getObjectType() == IFrontSensorResult.Types.WALL);
	}
}