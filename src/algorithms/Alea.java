/* ******************************************************
 * Simovies - Eurobot 2015 Robomovies Simulator.
 * Copyright (C) 2014 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: algorithms/BrainCanevas.java 2014-10-19 buixuan.
 * ******************************************************/
package algorithms;

import robotsimulator.Brain;
import robotsimulator.FrontSensorResult;
import robotsimulator.RadarResult;

import characteristics.IFrontSensorResult;
import characteristics.IRadarResult;
import characteristics.IRadarResult.Types;
import characteristics.Parameters;
import characteristics.Parameters.Direction;

public class Alea extends Brain {
	private static int robotID;
	private int robotNum = 0;

	public Alea() {
		super();
	}

	private int state = 0;
	private int tabStateRobot[] = new int[5];
	private int compteurAlea = 0;
	private boolean tire = true;
	private int compteur = 0;
	private int compteurTailleMap = 0;
	private int compteurStateRobot = 0;
	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private double myX, myY;
	private int nbrTourPion;
	private Boolean demiTour = false;
	private int alea = (int) (Math.random() * (2 - 0));
	private int alea2 = (int) (Math.random() * (1100 - 1000));
	private Boolean AleaTourne = false;
	private String etat = "";

	//////////////////////////////////////////////////////////////////
	private boolean turnTask, turnRight, moveTask;
	private double endTaskDirection;
	private int endTaskCounter;
	private boolean firstMove;
	////////////////////////////////////////////////////////////////////

	public void activate() {
		// ---PARTIE A MODIFIER/ECRIRE---//
		state = 0;
		compteur = 0;
		robotNum = robotID;
		nbrTourPion = 0;
		compteurTailleMap = 0;
		compteurStateRobot = 0;
		tire = true;
		int alea = (int) (Math.random() * (2 - 0));
		int alea2 = (int) (Math.random() * (1050 - 1020));
		compteurAlea = 0;
		AleaTourne = false;
		etat = "";
		///////////////////////////////////////////////////////////////////////
//	    turnTask=true;
//	    moveTask=false;
//	    firstMove=true;
//	    endTaskDirection=(Math.random()-0.5)*0.5*Math.PI;
//	    turnRight=(endTaskDirection>0);
//	    endTaskDirection+=getHeading();
//	    if (turnRight) stepTurn(Parameters.Direction.RIGHT);
//	    else stepTurn(Parameters.Direction.LEFT);
//	    sendLogMessage("Turning point. Waza!");
		//////////////////////////////////////////////////////////////////////
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

//		sendLogMessage(myX + ", " + myY + " Etat : " + etat);
		sendLogMessage("Etat : " + etat);

//		for (IRadarResult o : detectRadar()) {
//			if (o.getObjectType().name().equals("BULLET")) {
//				myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
//				myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
//				moveBack();
//				return;
//			}
//		}

		// if (tire) {
		for (IRadarResult o : detectRadar()) {
//			if (o.getObjectType().TeamMainBot == null && o.getObjectType().TeamSecondaryBot == null) {
//				fire(Math.random() * Math.PI * 2);
//				return;
//			}
//			if (robotID > 2) {
//				if (o.getObjectType().name().equals("BULLET")) {
//					myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
//					myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
//					moveBack();
//					etat = "back";
//					return;
//				}
//			} else {
//				if (o.getObjectType().name().equals("OpponentMainBot")) {
//					fire(o.getObjectDirection());
//					etat = "Tire OpponentMainBot";
//					return;
//				} else {
//					if (o.getObjectType().name().equals("OpponentSecondaryBot")) {
//						fire(o.getObjectDirection());
//						etat = "Tire OpponentSecondaryBot";
//						return;
//					}
//				}
//			}
		}

		System.out.println(alea2);

//		if (isWall()) {
//			myMoveBack();
//			return;
//		} else {

		if (compteurAlea >= 1000 + alea2) {
			// alea = (int) (Math.random() * (1000 - 0));
			// AleaTourne = false;
			alea2 = (int) (Math.random() * (1050 - 1020));
			alea = (int) (Math.random() * (2 - 0));
			compteurAlea = 0;
		} else {
			if (compteurAlea >= 1000) {
//				alea = (int) (Math.random() * (2 - 0));

				if (alea == 1) {
					stepTurn(Parameters.Direction.RIGHT);
					etat = "droite";
				} else {
					stepTurn(Parameters.Direction.LEFT);
					etat = "gauche";
				}
			} else {
				// compteurAlea++;
				if (!nothingAhead()) {
					myMove();
					etat = "Avance";
				} else {
					myMoveBack();
					etat = "Reculer";
				}
			}
			compteurAlea++;
			return;
		}
	}

//		if (alea > 0 && alea <= 400) {
//			myMove();
//			etat = "avance";
//			return;
//		}
//		if (alea > 400 && alea <= 800) {
//			myMoveBack();
//			etat = "recule";
//			return;
//		}
//		if (alea > 800 && alea <= 900) {
//			AleaTourne = true;
//			stepTurn(Parameters.Direction.LEFTTURNFULLANGLE);
//			etat = "tourne à gauche";
//			// }
//			return;
//		}
//		if (alea > 900 && alea <= 1000) {
//			AleaTourne = true;
//			stepTurn(Parameters.Direction.RIGHTTURNFULLANGLE);
//			etat = "tourne à droite";
//			return;
//		}

//////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// tire = false;
	// } else {
//		tire = true;
//		if (robotNum == 2) {
//			if (myX == 750) {
//				state = 6;
//			}
//
//			if (myX == 1500) {
//				state = 6;
//			}
//
//			if (myX == 225) {
//				state = 6;
//			}
//		}
//
//		if (state == 0 && !isHeadingSouth()) {
//			stepTurn(Parameters.Direction.RIGHT);
//			// sendLogMessage(compteur + "pas.");
//			return;
//		}
//		if (state == 0 && isHeadingSouth()) {
//			state = 1;
//			return;
//		}
//		if (state == 1 && nothingAhead()) {
//			compteur++;
//			myMove();
//			return;
//		}
//		if (state == 1 && !nothingAhead()) {
//			state = 2;
//			return;
//		}
//		if (state == 2 && !isHeadingEast()) {
//			stepTurn(Parameters.Direction.LEFT);
//			// sendLogMessage(compteur + "pas.");
//			return;
//		}
//		if (state == 2 && isHeadingEast()) {
//			state = 3;
//			return;
//		}
//		if (state == 3 && nothingAhead()) {
//			compteur++;
//			myMove();
//			return;
//		}
//		if (state == 3 && !nothingAhead()) {
//			state = 4;
//			return;
//		}
//
//		if (state == 4 && !isHeadingNorth()) {
//			stepTurn(Parameters.Direction.LEFT);
//			// sendLogMessage(compteur + "pas.");
//			return;
//		}
//		if (state == 4 && nothingAhead()) {
//			state = 5;
//			return;
//		}
//		if (state == 5 && nothingAhead()) {
//			compteur++;
//			myMove();
//			return;
//		}
//		if (state == 5 && !nothingAhead()) {
//			state = 0;
//			return;
//		}
//
//		if (robotNum == 2) {
//			if (state == 6) {
//				myMove();
//				state = 7;
//				return;
//			}
//
//			if (state == 7) {
//				stepTurn(Parameters.Direction.RIGHT);
//				nbrTourPion++;
//				if (nbrTourPion == 200) {
//					nbrTourPion = 0;
//					state = 0;
//				}
//				return;
//			}
//		}
	// }
//	}

	private boolean nothingAhead() {
		return (detectFront().getObjectType() == IFrontSensorResult.Types.NOTHING);
	}

	private boolean isWall() {
		return (detectFront().getObjectType() == IFrontSensorResult.Types.WALL);
	}

	private boolean isHeadingSouth() {
		return isHeading(Parameters.SOUTH);
	}

	private boolean isHeadingNorth() {
		return isHeading(Parameters.NORTH);
	}

	private boolean isHeadingEast() {
		return isHeading(Parameters.EAST);
	}

	private boolean isHeadingWest() {
		return isHeading(Parameters.WEST);
	}

	private boolean isHeading(double dir) {
		return Math.abs(Math.sin(getHeading() - dir)) < HEADINGPRECISION;
	}

	private void myMove() {

//		Boolean recule = false;
//		for (IRadarResult o : detectRadar()) {
//			if (o.getObjectType().name().equals("BULLET")) {
//				recule = true;
//			}
//		}
//		if (recule) {
//			myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
//			myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
//			moveBack();
//		} else {
		myX += Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY += Parameters.teamBMainBotSpeed * Math.sin(getHeading());
		move();
//		}
	}

	private void myMoveBack() {
		myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
		myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
		moveBack();
//		}
	}

	private boolean isSameDirection(double dir1, double dir2) {
		return Math.abs(dir1 - dir2) < ANGLEPRECISION;
	}
}