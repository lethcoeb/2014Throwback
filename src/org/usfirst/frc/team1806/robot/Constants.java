package org.usfirst.frc.team1806.robot;

public class Constants {
	//Amperage Limits
	//TODO: fix the amperage limits lol THEY DON'T WANT YOU TO HAVE REALISTIC AMPERAGE LIMITS
	public final static int max3CimAmps = 200;
	public final static int max2CimAmps = 250;
	public final static int max1CimAmps = 300;
	
	//Voltage Limits
	//TODO: Make sure these are reasonable.
	public final static double min3CimVoltage = 8;
	public final static double min2CimVoltage = 7;
	public final static double min1CimVoltage = 5;
	
	public final static double xboxJoystickDeadzone = .2;
	//drivetrain encoder constants
	//THEY DON'T WANT YOU TO HAVE SHORT NUMBERS
	public final static double drivetrainInchesPerCount = 0.04908738521234051935097880286374;
	
	//value that limits power change
	public final static double drivetrainMaxPowerChange = .1;
	public final static double drivetrainMaxTurnChange = .25;
	
	//TODO: Tweak shifting values
	//drive shifting constants
	public final static double drivetrainShiftWaitTime = .1;
	
	//TODO: Does it need a different power to shift up vs shifting down??
	public final static double drivetrainShiftPower = .2; //power with which the motors are given while shifting
	public final static double drivetrainAccelerationThreshold = .02; // applied for determining actual accel for shifting
	//a "Power" downshift is one in which the speed is low but applied power is high
	public final static double drivetrainPowerDownshiftPowerThreshold = .97 ; //power must be over this
	//a normal downshift is when the robot is more than likely coming to rest
	public final static double drivetrainDownshiftPowerThreshold = .2; // power must be less than this
	public final static double drivetrainDownshiftSpeedThreshold = 3; //speed must be less than this
	
	//normal upshift that would occur when going from a stop to full speed
	public final static double drivetrainUpshiftPowerThreshold = .75; //power must be more than this
	public final static double drivetrainUpshiftSpeedThreshold = 4.5; //speed must be more than this
	public final static double drivetrainMaxLowGearSpeed = 6; //exceeding this speed will force an autoshift to high gear.
	//also used for a power downshift
	public final static double drivetrainMinTimeBetweenShifts = 0.5;
	//drive forward PID values
	public final static double brakepidP = 0.1;
	public final static double brakepidI = 0.1;
	public final static double brakepidD = 0;
	
	//the drivetrain execute loop will send a warning to the console if it doesn't run in the specified period.
	public final static double drivetrainExecuteWarnTime = .25;
	
	//integrity testing variables
	public final static double motorMinCurrent = .2;
	public final static double motorMinPower = .2;
	public final static int cyclesUntilDead = 10;
	
	//How much a side should compensate for if the other side has a dead motor
	public final static double deadMotorCompensation = .25;
	
	//PID CONSTANTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public final static double drivetrainAutoturnP = .01;
	public final static double drivetrainAutoturnI = 0;
	public final static double drivetrainAutoturnD = 0;

	
	
	
}
