package org.usfirst.frc.team1806.robot;

import util.SWATLib;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardInterface {

	public void init(){
		
	}
	
	public void updateData(){
		SmartDashboard.putNumber("Power", Robot.drivetrainSS.getPower());
		SmartDashboard.putNumber("Turn", Robot.drivetrainSS.getTurn());
		SmartDashboard.putNumber("Drive Distance", Robot.drivetrainSS.getDriveDistance());
		SmartDashboard.putBoolean("High Gear?", Robot.drivetrainSS.isInHighGear());
		SmartDashboard.putNumber("Total Amperage", Robot.drivetrainSS.getAmperage());
		SmartDashboard.putNumber("Number of CIMs Running Per Side", Robot.drivetrainSS.getCimsRunningPerSide());
		SmartDashboard.putBoolean("AutoShift:", Robot.drivetrainSS.isAutoShiftActive());
		SmartDashboard.putNumber("Right Encoder", Robot.drivetrainSS.getRightDistance());
		SmartDashboard.putNumber("Left Encoder", Robot.drivetrainSS.getLeftDistance());
		SmartDashboard.putNumber("Last speed", Robot.drivetrainSS.lastSpeed);
		SmartDashboard.putNumber("Current speed", Robot.drivetrainSS.currentSpeed);
		SmartDashboard.putBoolean("CanAutoShiftAgain", Robot.drivetrainSS.canAutoShiftAgain());
		SmartDashboard.putNumber("Drive Speed fps", Robot.drivetrainSS.getDriveSpeedFPS());
		SmartDashboard.putNumber("Acceleration fpsps", Robot.drivetrainSS.getDriveAccelFPSPS());
		SmartDashboard.putBoolean("Low Gear Lock?", Robot.drivetrainSS.lowGearLock);
		SmartDashboard.putNumber("Right side dead CIMs: ", Robot.drivetrainSS.rightsideDeadTalons);
		SmartDashboard.putNumber("Left side dead CIMs: ", Robot.drivetrainSS.leftsideDeadTalons);
		SmartDashboard.putNumber("PDP Temperature", Robot.PDP.getTemperature());
		SmartDashboard.putNumber("PDP Total Wattage", Robot.PDP.getTotalPower());
		SmartDashboard.putBoolean("IntegrityTesting?", Robot.drivetrainSS.integrityTesting);
		SmartDashboard.putNumber("Right cim pwm 0 amps", Robot.PDP.getCurrent(13));
		SmartDashboard.putNumber("dead cycles for unplugged motor", Robot.drivetrainSS.rightDrive0.deadCycles);
		SmartDashboard.putNumber("NavX acceleration", 0);
		SmartDashboard.putNumber("right joystick angle", SWATLib.joystickAngle(Robot.oi.rsX, Robot.oi.rsY));
		SmartDashboard.putNumber("Robot angle", Robot.drivetrainSS.navX.getAngle());
	
	}
	
}
