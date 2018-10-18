/* ******************************************************
 * Simovies - Eurobot 2015 Robomovies Simulator.
 * Copyright (C) 2014 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: algorithms/Stage1.java 2014-10-18 buixuan.
 * ******************************************************/
package algorithms;

import robotsimulator.Brain;
import characteristics.Parameters;
import characteristics.IFrontSensorResult;

public class Stage1 extends Brain {
  //---PARAMETERS---//
  private static final double HEADINGPRECISION = 0.001;

  //---VARIABLES---//
  private boolean turnRightTask;
  private boolean taskOne;
  private double endTaskDirection;

  //---CONSTRUCTORS---//
  public Stage1() { super(); }

  //---ABSTRACT-METHODS-IMPLEMENTATION---//
  public void activate() {
    turnRightTask=true;
    taskOne=true;
    move();
    sendLogMessage("Moving a head. Waza!");
  }
  public void step() {
    if (taskOne) {
      if (turnRightTask) {
        if (isHeading(-0.5*Math.PI)) {
          turnRightTask=false;
          taskOne=false;
          move();
          sendLogMessage("Moving a head. Waza!");
        } else {
          stepTurn(Parameters.Direction.RIGHT);
          sendLogMessage("Iceberg at 12 o'clock. Heading to my three!");
        }
        return;
      }
    }
    if (turnRightTask) {
      if (isHeading(endTaskDirection)) {
	turnRightTask=false;
	move();
        sendLogMessage("Moving a head. Waza!");
      } else {
	stepTurn(Parameters.Direction.LEFT);
        sendLogMessage("Iceberg at 12 o'clock. Heading to my three!");
      }
      return;
    }
    if (detectFront().getObjectType()!=IFrontSensorResult.Types.WALL) {
      move(); //And what to do when blind blocked?
      sendLogMessage("Moving a head. Waza!");
    } else {
      turnRightTask=true;
      endTaskDirection=getHeading()+Parameters.LEFTTURNFULLANGLE;
      stepTurn(Parameters.Direction.LEFT);
      sendLogMessage("Iceberg at 12 o'clock. Heading to my three!");
    }
  }
  private boolean isHeading(double dir){
    return Math.abs(Math.sin(getHeading()-dir))<HEADINGPRECISION;
  }
}