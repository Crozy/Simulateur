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

public class BrainCanevas extends Brain {
	private static int robotID;
	private int robotNum = 0;

	public BrainCanevas() {
		super();
	}

	private int state = 0;
	private boolean tire = true;
	private int compteur = 0;
	private int compteurTailleMap = 0;
	private static final double HEADINGPRECISION = 0.001;
	private static final double ANGLEPRECISION = 0.1;
	private double myX, myY;
	private int nbrTourPion;
	private Boolean demiTour = false;

	public void activate() {
		// ---PARTIE A MODIFIER/ECRIRE---//
		state = 0;
		compteur = 0;
		robotNum = robotID;
		nbrTourPion = 0;
		compteurTailleMap = 0;
		tire = true;

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

		sendLogMessage(myX + ", " + myY + " State : " + state);

		if (tire) {
			for (IRadarResult o : detectRadar()) {
				if (o.getObjectType().name().equals("OpponentMainBot")
						|| o.getObjectType().name().equals("OpponentSecondaryBot")) {
					fire(o.getObjectDirection());
				}
			}
			tire = false;
		} else {
			tire = true;
			if (robotNum == 2) {
				if (myX == 750) {
					// if (demiTour == false) {
					state = 6;
					// demiTour = true;
					// }
				}

				if (myX == 1500) {
					state = 6;
				}

				if (myX == 225) {
					state = 6;
				}
			}

//		if (robotNum == 0) {
//			if (state == 0) {
//				compteurTailleMap++;
//				stepTurn(Parameters.Direction.LEFT);
//				if (isHeadingNorth()) {
//					state = 1;
//					compteurTailleMap = 0;
//				}
//				return;
//			}
//			if (state == 1 && nothingAhead()) {
//				myMove();
//				return;
//			}
//			if (state == 1 && !nothingAhead()) {
////				compteurTailleMap++;
//				stepTurn(Parameters.Direction.LEFT);
//				if (isHeadingSouth() && !nothingAhead()) {
//					state = 2;
//				}				
//				return;
//			}
//			if(state == 2) {
////				stepTurn(Parameters.Direction.LEFT);
////				ifisHeadingSouth()()
////				return;
//			}
//		}
//		
//		if(robotNum == 1) {
//			myMove();
//			return;
//		}

			if (state == 0 && !isHeadingSouth()) {
				stepTurn(Parameters.Direction.RIGHT);
				// sendLogMessage(compteur + "pas.");
				return;
			}
			if (state == 0 && isHeadingSouth()) {
				state = 1;
				return;
			}
			if (state == 1 && nothingAhead()) {
				compteur++;
				myMove();
				return;
			}
			if (state == 1 && !nothingAhead()) {
				state = 2;
				return;
			}
			if (state == 2 && !isHeadingEast()) {
				stepTurn(Parameters.Direction.LEFT);
				// sendLogMessage(compteur + "pas.");
				return;
			}
			if (state == 2 && isHeadingEast()) {
				state = 3;
				return;
			}
			if (state == 3 && nothingAhead()) {
				compteur++;
				myMove();
				return;
			}
			if (state == 3 && !nothingAhead()) {
				state = 4;
				return;
			}

			if (state == 4 && !isHeadingNorth()) {
				stepTurn(Parameters.Direction.LEFT);
				// sendLogMessage(compteur + "pas.");
				return;
			}
			if (state == 4 && nothingAhead()) {
				state = 5;
				return;
			}
			if (state == 5 && nothingAhead()) {
				compteur++;
				myMove();
				return;
			}
			if (state == 5 && !nothingAhead()) {
				state = 0;
				return;
			}

			if (robotNum == 2) {
				if (state == 6) {
					myMove();
					state = 7;
					return;
				}

				if (state == 7) {
					stepTurn(Parameters.Direction.RIGHT);
					nbrTourPion++;
					if (nbrTourPion == 200) {
						nbrTourPion = 0;
						state = 0;
					}
					return;
				}
			}
		}
	}

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
		Boolean recule = false;
		for (IRadarResult o : detectRadar()) {
			if (o.getObjectType().name().equals("BULLET")) {
				recule = true;
			}
		}
		if (recule) {
			myX -= Parameters.teamBMainBotSpeed * Math.cos(getHeading());
			myY -= Parameters.teamBMainBotSpeed * Math.sin(getHeading());
			moveBack();
		} else {
			myX += Parameters.teamBMainBotSpeed * Math.cos(getHeading());
			myY += Parameters.teamBMainBotSpeed * Math.sin(getHeading());
			move();
		}
	}

	private boolean isSameDirection(double dir1, double dir2) {
		return Math.abs(dir1 - dir2) < ANGLEPRECISION;
	}
}