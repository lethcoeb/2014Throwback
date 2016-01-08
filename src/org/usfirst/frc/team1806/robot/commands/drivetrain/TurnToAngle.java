package org.usfirst.frc.team1806.robot.commands.drivetrain;

import org.usfirst.frc.team1806.robot.Constants;
import org.usfirst.frc.team1806.robot.Robot;
import org.usfirst.frc.team1806.robot.States;

import util.SWATLib;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToAngle extends Command {

	/*
	 * Currently, if this command is running and the right joystick angle is under the threshold, the robot will attempt to stick at
	 * whatever angle it was at when the joystick WAS above threshold. May wanna change this because we might not want it to stick to
	 * an angle.
	 */
	
	PIDSource turnControllerSource;
	PIDOutput turnControllerOutput;
	PIDController turnController;
	double lastAngle;
	double currentAngle;
	double targetAngle;
	boolean isOverThreshold;
	double power;
	double turn;
	
    public TurnToAngle() {
        //requires(Robot.drivetrainSS);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	System.out.println("turn to angle command started");
    	
    	Robot.robotStates.driverDTControlTracker = States.driverDTControl.AUTOMATIC;
		Robot.drivetrainSS.lowGearLockEnable();

    	
    	currentAngle = Robot.drivetrainSS.getAngle();
    	lastAngle = currentAngle;
    	

    	
    	turnControllerSource = new PIDSource() {
			@Override
			public double pidGet() {
				return Robot.drivetrainSS.getAngle();
			}};
    	
    	turnControllerOutput = new PIDOutput() {
			@Override
			public void pidWrite(double output) {
				turn = output;
			}};
    	turnController = new PIDController(Constants.drivetrainAutoturnP, Constants.drivetrainAutoturnI, Constants.drivetrainAutoturnD, turnControllerSource, turnControllerOutput);
    	turnController.setContinuous(true);
    	turnController.setOutputRange(-1, 1);
    	turnController.enable();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	targetAngle = SWATLib.joystickAngle(Robot.oi.rsX, Robot.oi.rsY);
    	isOverThreshold = targetAngle > Constants.xboxJoystickDeadzone;
    	if(isOverThreshold){
    		//because you only want to update lastAngle if you're over the threshold
    		//otherwise you want the last angle to stick because that's where you want to point
    		lastAngle = currentAngle;
    		currentAngle = Robot.drivetrainSS.getAngle();
    	}
    	
    	//set power
    	power = Robot.oi.lsY;
    	if(power < Constants.xboxJoystickDeadzone){
    		power = 0;
    	}
    	
    	//set turn
    	if(isOverThreshold){
    		turnController.setSetpoint(targetAngle);
    	}else{
    		turnController.setSetpoint(lastAngle);
    	}
    	
    	
    	Robot.drivetrainSS.execute(power, turn);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//.1 gap between this .4 and the .5 needed to invoke the command so there's no wobbling
        return Robot.oi.rt < .4;
    }

    // Called once after isFinished returns true
    protected void end() {
    	turnController.disable();
    	Robot.robotStates.driverDTControlTracker = States.driverDTControl.DRIVER;
    	Robot.drivetrainSS.lowGearLockDisable();
    	System.out.println("turn to angle command finished");

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	turnController.disable();
    	Robot.robotStates.driverDTControlTracker = States.driverDTControl.DRIVER;
    	System.out.println("turn to angle command interrupted");

    }
}
