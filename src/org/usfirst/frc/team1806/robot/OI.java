package org.usfirst.frc.team1806.robot;


import edu.wpi.first.wpilibj.Joystick.RumbleType;
import util.Latch;

import java.awt.Label;
import java.lang.Math;

import org.usfirst.frc.team1806.robot.commands.drivetrain.AutoShiftToHigh;
import org.usfirst.frc.team1806.robot.commands.drivetrain.AutoShiftToLow;
import org.usfirst.frc.team1806.robot.commands.drivetrain.ParkingBrake;
import org.usfirst.frc.team1806.robot.commands.drivetrain.TurnToAngle;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public XboxController dc = new XboxController(0);

	public double lsY;
	// private double lsY;
	public double rsX;
	public double rsY;
	public double rt;
	public double lt;
	public boolean rb;
	public boolean lb;
	public boolean shifting;
	public boolean buttonA;
	public boolean buttonBack;
	private Latch disableAutoShift;
	public OI() {
		disableAutoShift = new Latch();
	}
	

	public void update() {
		lsY = dc.getLeftJoyY();
		// lsY = dc.getLeftJoyY();
		rsX = dc.getRightJoyX();
		rsY = dc.getRightJoyY();
		rt = dc.getRightTrigger();
		lt = dc.getLeftTrigger();
		rb = dc.getButtonRB();
		lb = dc.getButtonLB();
		
		buttonA = dc.getButtonA();
		buttonBack = dc.getButtonBack();
		// arcade drive, with deadzone

		if (Robot.robotStates.driverDTControlTracker == States.driverDTControl.DRIVER){
			
			/*
			 * Low gear lock
			 */
			if(lt > .5){
				Robot.drivetrainSS.lowGearLockEnable();
			}else{
				Robot.drivetrainSS.lowGearLockDisable();
			}
			
			
			/*
			 * Drivetrain things
			 */
			
			if (Math.abs(lsY) > Constants.xboxJoystickDeadzone) {
				if (Math.abs(rsX) > Constants.xboxJoystickDeadzone) {
					Robot.drivetrainSS.execute(lsY, rsX);					
				} // end both out of deadzone
				else {
					// only power is out of deadzone
					Robot.drivetrainSS.execute(lsY, 0);
				} // end only turn is out of deadzone
			} // end turn is out of deadzone
			else if (Math.abs(rsX) > Constants.xboxJoystickDeadzone) {
				// only turn is out of deadzone
				Robot.drivetrainSS.execute(0, rsX);
			} // end only turn is out of deadzone
			else if (buttonA){
				new ParkingBrake().start();
			}
			else {
				Robot.drivetrainSS.execute(0, 0);
			}
			
			//rumbles based on what gear the drivetrain is in
			if(Robot.drivetrainSS.isInLowGear() && !shifting){
				dc.setRumble(RumbleType.kLeftRumble, (float) .35);
			}else if (Robot.drivetrainSS.isInHighGear() && !shifting){
				dc.setRumble(RumbleType.kRightRumble, (float) .7);
			}
			
			if(rt > .5){
				new TurnToAngle().start();
			}
			
		}
		
		//enable or disable automatic shifting based on back button
		if(disableAutoShift.update(buttonBack)){
			if(Robot.drivetrainSS.isAutoShiftActive()){
				Robot.drivetrainSS.disableAutoShift();
			}else{
				Robot.drivetrainSS.enableAutoShift();
			}
		}
		
		/*if (Robot.drivetrainSS.isAutoShiftActive() && disableAutoShift.update(buttonBack)){
			Robot.drivetrainSS.disableAutoShift();
		}
		if((!Robot.drivetrainSS.isAutoShiftActive())&& disableAutoShift.update(buttonBack)){
			Robot.drivetrainSS.enableAutoShift();
		}*/
		
		if(lb && !Robot.drivetrainSS.isAutoShiftActive()){
			//shift to low manually
			new AutoShiftToLow().start();
		}
		else if(rb && !Robot.drivetrainSS.isAutoShiftActive()){
			new AutoShiftToHigh().start();
		}
	}
	
	public void rumbleHigh(){
		//dc.setRumble(RumbleType.kLeftRumble, (float) 0);
		//dc.setRumble(RumbleType.kRightRumble, (float) .7);
	}
	
	public void rumbleLow(){
		//dc.setRumble(RumbleType.kLeftRumble, (float) .35);
		//dc.setRumble(RumbleType.kRightRumble, (float) 0);
		
	}
	
	public void rumbleStop(){
		dc.setRumble(RumbleType.kLeftRumble, 0);
		dc.setRumble(RumbleType.kRightRumble, 0);
	}
}
