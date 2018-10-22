//package algorithms;
//
//import robotsimulator.Brain;
//import characteristics.IFrontSensorResult;
//import characteristics.Parameters;
//
//public class Stage4 extends Brain {
//	private static int cpt = 0;
//	private int id;
//
//	public Stage4() { super(); id = cpt++; doitBouger = id >= 0 && id <= 2;}
//	private int state=0;
//	private int dim = 0;
//	private int debug = 0;
//	private static final double HEADINGPRECISION = 0.0001;
//	private static double speed = Parameters.teamAMainBotSpeed;
//	private static double detectRange = Parameters.teamAMainBotFrontalDetectionRange;
//	private static double hauteur = 2000;
//	private static double longueur = 3500;  
//	private static double quartLongueur = longueur/4;
//	private static double preci = Parameters.teamAMainBotSpeed/2;
//	private double x = Parameters.teamASecondaryBot1InitX;
//	private boolean doitBouger;
//	private static boolean aFini1 = false;
//	private static boolean aFini2 = false;
//
//	
//	public void activate() {
//
//		//---PARTIE A MODIFIER/ECRIRE---//
//
//	}
//	public void step() {
//		boolean cond1 = (x >= (quartLongueur - preci) && x <= (quartLongueur + preci) );
//		
//		boolean cond2 = ( x >= (2*quartLongueur - preci) && x <= (2*quartLongueur + preci) );
//		
//		boolean cond3 = ( x >= (3*quartLongueur - preci) && x <= (3*quartLongueur + preci) );
//		
//		if(id == 0 && cond1  ){
//			
//			return;
//		}
//		if(id == 1 && cond2  ){
//			aFini2 = true;
//			return;
//		}
//		
//		if(id == 2 && cond3  ){
//			aFini1 = true;
//			return;
//		}
//		
//		
//		if(id == 2 && state == 0 && !isHeadingSouth()) {
//			sendLogMessage("je tourne vers le Sud");
//			stepTurn(Parameters.Direction.RIGHT);
//			return;
//		}
//
//		
//		
//		if(id == 2 && state == 0 && isHeadingSouth()) {
//			state = 1;
////			System.out.println("0 fin");
//			return;
//		}
//
//		
//		
//		if(id == 2 && state == 1 && nothingAhead()) {
//			sendLogMessage("j'avance vers le Sud");
//			if(debug == 0) {
////			System.out.println("1 debut");
//			debug++;
//			}
//			move();
//			return;
//		}
//		
//		
//		if(id == 2 && state == 1 && !nothingAhead()) {
//			state = 4;
////			System.out.println("1 fin");
//			return;
//		}
//		
//		
//		if(id == 2 && state == 4 && !isHeadingEast()) {
//			sendLogMessage("je tourne vers l'Est");
//			stepTurn(Parameters.Direction.LEFT);
//			if(debug == 3) {
////				System.out.println("4 debut");
//				debug++;
//				}
//			return;
//		}
//		
//		if(id == 2 && state == 4 && isHeadingEast()) {
////			System.out.println("4 fin");
//			state = 5;
//			return;
//		}
//		
//		if(id == 2 && state == 5 && nothingAhead()) {
//			sendLogMessage("j'avance ver l'Est");
//			if(debug == 4) {
////				System.out.println("5 debut");
//				debug++;
//				}
//			//System.out.println(x);
//			dim+=speed;
//			x+=speed;
//			move();
//			return;
//		}
//		
//		if(id == 2 && state == 5 && !nothingAhead()) {
//			sendLogMessage("longueur = "+(dim+2*detectRange));//3005
//			return;
//		}
//		
//		
//		if(id == 1 && aFini1 && state == 0 && !isHeadingSouth()) {
//			sendLogMessage("je tourne vers le Sud");
//			stepTurn(Parameters.Direction.RIGHT);
//			return;
//		}
//
//		
//		
//		if(id == 1 && aFini1 && state == 0 && isHeadingSouth()) {
//			state = 1;
////			System.out.println("0 fin");
//			return;
//		}
//
//		
//		
//		if(id == 1 && aFini1 && state == 1 && nothingAhead()) {
//			sendLogMessage("j'avance vers le Sud");
//			if(debug == 0) {
////			System.out.println("1 debut");
//			debug++;
//			}
//			move();
//			return;
//		}
//		
//		
//		if(id == 1 && aFini1 && state == 1 && !nothingAhead()) {
//			state = 4;
////			System.out.println("1 fin");
//			return;
//		}
//		
//		
//		if(id == 1 && aFini1 && state == 4 && !isHeadingEast()) {
//			sendLogMessage("je tourne vers l'Est");
//			stepTurn(Parameters.Direction.LEFT);
//			if(debug == 3) {
////				System.out.println("4 debut");
//				debug++;
//				}
//			return;
//		}
//		
//		if(id == 1 && aFini1 && state == 4 && isHeadingEast()) {
////			System.out.println("4 fin");
//			state = 5;
//			return;
//		}
//		
//		if(id == 1 && aFini1 && state == 5 && nothingAhead()) {
//			sendLogMessage("j'avance ver l'Est");
//			if(debug == 4) {
////				System.out.println("5 debut");
//				debug++;
//				}
//			//System.out.println(x);
//			dim+=speed;
//			x+=speed;
//			move();
//			return;
//		}
//		
//		if(id == 1 && aFini1 && state == 5 && !nothingAhead()) {
//			sendLogMessage("longueur = "+(dim+2*detectRange));//3005
//			return;
//		}
//		
//		if(id == 0 && aFini2 && state == 0 && !isHeadingSouth()) {
//			sendLogMessage("je tourne vers le Sud");
//			stepTurn(Parameters.Direction.RIGHT);
//			return;
//		}
//
//		
//		
//		if(id == 0 && aFini2 && state == 0 && isHeadingSouth()) {
//			state = 1;
////			System.out.println("0 fin");
//			return;
//		}
//
//		
//		
//		if(id == 0 && aFini2 && state == 1 && nothingAhead()) {
//			sendLogMessage("j'avance vers le Sud");
//			if(debug == 0) {
////			System.out.println("1 debut");
//			debug++;
//			}
//			move();
//			return;
//		}
//		
//		
//		if(id == 0 && aFini2 && state == 1 && !nothingAhead()) {
//			state = 4;
////			System.out.println("1 fin");
//			return;
//		}
//		
//		
//		if(id == 0 && aFini2 && state == 4 && !isHeadingEast()) {
//			sendLogMessage("je tourne vers l'Est");
//			stepTurn(Parameters.Direction.LEFT);
//			if(debug == 3) {
////				System.out.println("4 debut");
//				debug++;
//				}
//			return;
//		}
//		
//		if(id == 0 && aFini2 && state == 4 && isHeadingEast()) {
////			System.out.println("4 fin");
//			state = 5;
//			return;
//		}
//		
//		if(id == 0 && aFini2 && state == 5 && nothingAhead()) {
//			sendLogMessage("j'avance ver l'Est");
//			if(debug == 4) {
////				System.out.println("5 debut");
//				debug++;
//				}
//			//System.out.println(x);
//			dim+=speed;
//			x+=speed;
//			move();
//			return;
//		}
//		
//		if(id == 1 && aFini1 && state == 5 && !nothingAhead()) {
//			sendLogMessage("longueur = "+(dim+2*detectRange));//3005
//			return;
//		}
//		
//	}
//	private boolean nothingAhead(){
//		return (detectFront().getObjectType()==IFrontSensorResult.Types.NOTHING);
//	}
//	private boolean isHeadingSouth(){return isHeading(Parameters.SOUTH);}
//	private boolean isHeadingEast(){return isHeading(Parameters.EAST);}
//	private boolean isHeadingWest(){return isHeading(Parameters.WEST);}
//	private boolean isHeadingNorth(){return isHeading(Parameters.NORTH);}
//	private boolean isHeading(double dir){
//		return Math.abs(Math.sin(getHeading()-dir))<HEADINGPRECISION;
//	}
//}

package algorithms;

import robotsimulator.Brain;
import characteristics.IFrontSensorResult;
import characteristics.Parameters;

public class Stage4 extends Brain {
	private static int cpt = 0;
	private int id;

	public Stage4() { super(); id = cpt++;}
	private int state=0;
	private int dim = 0;
	private int debug = 0;
	private static final double HEADINGPRECISION = 0.0001;
	private static double speed = Parameters.teamASecondaryBotSpeed;
	private static double detectRange = Parameters.teamASecondaryBotFrontalDetectionRange;
	private static double hauteur = 2000;
	private static double longueur = 3000;  
	private static double quartLongueur = longueur/4;
	private static double preci = Parameters.teamASecondaryBotSpeed/2;
	private double x = Parameters.teamASecondaryBot1InitX;
	
	
	private int doCercle = 0; 
	
	public void activate() {

		//---PARTIE A MODIFIER/ECRIRE---//

	}
	public void step() {
		boolean cond = (x >= (quartLongueur - preci) && x <= (quartLongueur + preci) )
				|| ( x >= (2*quartLongueur - preci) && x <= (2*quartLongueur + preci) )
			||	( x >= (3*quartLongueur - preci) && x <= (3*quartLongueur + preci) );	
		
		if(cond  && doCercle == 0){
			System.out.println("0");
			doCercle = 1;
			stepTurn(Parameters.Direction.RIGHT);
			return;
		}
		
		if( doCercle == 1 && !isHeadingEast()) {
			System.out.println("1");
			stepTurn(Parameters.Direction.RIGHT);
			return; 
		}
		
		if( doCercle == 1 && isHeadingEast()) {
			System.out.println("2");
			stepTurn(Parameters.Direction.RIGHT);
			doCercle++;
			return; 
		}
		
		if( doCercle == 2 && !isHeadingEast()) {
			System.out.println("3");
			stepTurn(Parameters.Direction.RIGHT);
			return; 
		}
		
		if(  doCercle == 2 && isHeadingEast()) {
			doCercle=0;
			move();
			x+=speed;
			dim+=speed;
			return; 
		}
		
		if(state == 0 && !isHeadingSouth()) {
			sendLogMessage("je tourne vers le Sud");
			stepTurn(Parameters.Direction.RIGHT);
			return;
		}

		
		
		if(state == 0 && isHeadingSouth()) {
			state = 1;
//			System.out.println("0 fin");
			return;
		}

		
		
		if( state == 1 && nothingAhead()) {
			sendLogMessage("j'avance vers le Sud");
			if(debug == 0) {
//			System.out.println("1 debut");
			debug++;
			}
			move();
			return;
		}
		
		
		if( state == 1 && !nothingAhead()) {
			state = 4;
//			System.out.println("1 fin");
			return;
		}
		
		
		if(state == 4 && !isHeadingEast()) {
			sendLogMessage("je tourne vers l'Est");
			stepTurn(Parameters.Direction.LEFT);
			if(debug == 3) {
//				System.out.println("4 debut");
				debug++;
				}
			return;
		}
		
		if( state == 4 && isHeadingEast()) {
//			System.out.println("4 fin");
			state = 5;
			return;
		}
		
		if(state == 5 && nothingAhead()) {
			sendLogMessage("j'avance ver l'Est");
			if(debug == 4) {
//				System.out.println("5 debut");
				debug++;
				}
			//System.out.println(x);
			dim+=speed;
			x+=speed;
			move();
			return;
		}
		
		if(state == 5 && !nothingAhead()) {
			sendLogMessage("longueur = "+(dim+2*detectRange));//3005
			return;
		}
		
		
		
		
	}
	private boolean nothingAhead(){
		return (detectFront().getObjectType()==IFrontSensorResult.Types.NOTHING);
	}
	private boolean isHeadingSouth(){return isHeading(Parameters.SOUTH);}
	private boolean isHeadingEast(){return isHeading(Parameters.EAST);}
	private boolean isHeadingWest(){return isHeading(Parameters.WEST);}
	private boolean isHeadingNorth(){return isHeading(Parameters.NORTH);}
	private boolean isHeading(double dir){
		return Math.abs(Math.sin(getHeading()-dir))<HEADINGPRECISION;
	}
}
