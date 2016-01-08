package org.usfirst.frc.team1806.robot.commands.drivetrain;

import org.usfirst.frc.team1806.robot.Constants;
import org.usfirst.frc.team1806.robot.Robot;
import org.usfirst.frc.team1806.robot.States;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ParkingBrake extends Command {
	PIDSource EncoderSource;
	PIDOutput PidOut;
	PIDController brakePID;
	double encoderStartPoint;
    public ParkingBrake() {
    	 requires(Robot.drivetrainSS);
     	Robot.robotStates.driverDTControlTracker = States.driverDTControl.AUTOMATIC;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	encoderStartPoint = Robot.drivetrainSS.getDriveDistance();
    	EncoderSource = new PIDSource() {
			
			@Override
			public double pidGet() {
				//Returns the difference between where you started braking at and where you are.
				return Math.abs(encoderStartPoint - Robot.drivetrainSS.getDriveDistance());
			}
		};
    	PidOut = new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
				
				// Need to read each encoder separately and have two PID loops
				Robot.drivetrainSS.arcadeDrive(output, 0);
				
			}
		};
    	brakePID = new PIDController(Constants.brakepidP, Constants.brakepidI, Constants.brakepidD, EncoderSource, PidOut);
    	brakePID.reset();
    	brakePID.setSetpoint(Robot.drivetrainSS.getDriveDistance());
    	brakePID.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       return !Robot.oi.buttonA;
    }

    // Called once after isFinished returns true
    protected void end() {
    	brakePID.disable();
    	Robot.drivetrainSS.arcadeDrive(0, 0);
    	Robot.robotStates.driverDTControlTracker = States.driverDTControl.DRIVER;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	brakePID.disable();
    	Robot.robotStates.driverDTControlTracker = States.driverDTControl.DRIVER;
    }
}
