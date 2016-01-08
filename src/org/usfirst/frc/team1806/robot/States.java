package org.usfirst.frc.team1806.robot;

public class States {

	public States(){
		reset();
	}
	
	public enum driverDTControl{
		DRIVER, AUTOMATIC
	}
	
	public enum dataLogger{
		ON, OFF
	}
	
	public void reset(){
		//use this to reset your enums to desired values
		//make more methods if needed to have extra configurations
		
		driverDTControlTracker = driverDTControl.DRIVER;
	}
	
	//Initialize enum objects. These are what are manipulated by the program.
	public driverDTControl driverDTControlTracker;
	public dataLogger dataLoggerTracker;
		
}
