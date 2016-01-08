package org.usfirst.frc.team1806.robot.subsystems;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.usfirst.frc.team1806.robot.Constants;
import org.usfirst.frc.team1806.robot.Robot;
import org.usfirst.frc.team1806.robot.RobotMap;
import org.usfirst.frc.team1806.robot.commands.drivetrain.AutoShiftToHigh;
import org.usfirst.frc.team1806.robot.commands.drivetrain.AutoShiftToLow;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import util.SWATLib;
import util.SWATTalon;

public class SixCimTwoSpeed extends Subsystem {
	// init hardware
	private SWATTalon leftDrive0;
	private SWATTalon leftDrive1;
	private SWATTalon leftDrive2;

	private Encoder leftEncoder;

	//TODO make this private
	public SWATTalon rightDrive0;
	private SWATTalon rightDrive1;
	private SWATTalon rightDrive2;

	private Encoder rightEncoder;

	private DoubleSolenoid shifter;

	public AHRS navX;


	// init software variables
	private double power;
	private double turn;
	private int cimsRunningPerSide;
	private boolean autoShift;
	public boolean driverControl;
	public boolean lowGearLock;

	// TODO Make these private. They're only referenced by SmartDashInterface
	public static double currentSpeed;
	public static double lastSpeed;

	private double lastPower;
	private double lastTurn;

	// integrity testing vars
	public boolean integrityTesting;
	private ArrayList<SWATTalon> talonArray;
	public int leftsideDeadTalons;
	public int rightsideDeadTalons;
	private double leftpower;
	private double rightpower;
	public double feetPerMeters;
	


	// CIM stress tracking variables
	private Timer timer;
	private double lastTrackedTime;
	private double period;
	private double lastAutoShift;
	private double timeSinceLastAutoShift;

	public SixCimTwoSpeed() {

		// initialize left side CIMs
		leftDrive0 = new SWATTalon(RobotMap.leftCim0, RobotMap.leftCim0PDP);
		leftDrive1 = new SWATTalon(RobotMap.leftCim1, RobotMap.leftCim1PDP);
		leftDrive2 = new SWATTalon(RobotMap.leftCim2, RobotMap.leftCim2PDP);
		// initialize left side Encoder
		leftEncoder = new Encoder(RobotMap.leftEncoderA, RobotMap.leftEncoderB, true);
		leftEncoder.setDistancePerPulse(Constants.drivetrainInchesPerCount);

		// initialize right side CIMs
		rightDrive0 = new SWATTalon(RobotMap.rightCim0, RobotMap.rightCim0PDP);
		rightDrive1 = new SWATTalon(RobotMap.rightCim1, RobotMap.rightCim1PDP);
		rightDrive2 = new SWATTalon(RobotMap.rightCim2, RobotMap.rightCim2PDP);
		// initialize right side Encoder
		rightEncoder = new Encoder(RobotMap.rightEncoderA, RobotMap.rightEncoderB, true);
		rightEncoder.setDistancePerPulse(Constants.drivetrainInchesPerCount);


		// initialize shifter
		shifter = new DoubleSolenoid(RobotMap.shiftSolenoidA, RobotMap.shiftSolenoidB);
		shiftLow();

		// initialize navX gyro
		navX = new AHRS(SerialPort.Port.kMXP);

		// initialize Software values
		power = 0;
		turn = 0;
		cimsRunningPerSide = 3;
		autoShift = true;
		driverControl = true;
		lowGearLock = false;
		currentSpeed = 0;
		lastSpeed = 0;

		integrityTesting = false;
		talonArray = new ArrayList<SWATTalon>();
		talonArray.add(leftDrive0);
		talonArray.add(leftDrive1);
		talonArray.add(leftDrive2);
		talonArray.add(rightDrive0);
		talonArray.add(rightDrive1);
		talonArray.add(rightDrive2);
		leftsideDeadTalons = 0;
		rightsideDeadTalons = 0;
		leftpower = 0;
		rightpower = 0;
		
		//init talon array
		//add more talons here if you'd like to test them for integrity
		

		

		// initialize time tracking
		timer = new Timer();
		timer.start();
		lastTrackedTime = 0;
		lastAutoShift = 0;
		timeSinceLastAutoShift = 0;

	}

	protected void initDefaultCommand() {

	}

	// Private Methods for setting the left and right drive all at once
	private void setLeftDrive(double speed, int numCimsRunning) {
		// clip speed to [-1, 1]
		if (speed > 1) {
			speed = 1;
		} else if (speed < -1) {
			speed = -1;
		}
		speed = -speed; //invert speed to left side

///>>>>>>> refs/remotes/lethcoeb/master
		// set power
		if (numCimsRunning < 1) {
			leftDrive0.set(0);
			leftDrive1.set(0);
			leftDrive2.set(0);
		} // end if numCimsRunning is less than 1
		else if (numCimsRunning == 1) {
			leftDrive0.set(speed);
			leftDrive1.set(0);
			leftDrive2.set(0);
		} /// end 1 CIM running
		else if (numCimsRunning == 2) {
			leftDrive0.set(speed);
			leftDrive1.set(speed);
			leftDrive2.set(0);
		} // end 2 CIMS running
		else if (numCimsRunning > 2) {
			leftDrive0.set(speed);
			leftDrive1.set(speed);
			leftDrive2.set(speed);
		} else {
			System.out.println("Something has gone horribly wrong with setLeftDrive.");
			leftDrive0.set(0);
			leftDrive1.set(0);
			leftDrive2.set(0);
		}
	}// end setLeftDrive

	private void setRightDrive(double speed, int numCimsRunning) {
		// clip speed to [-1, 1]
		if (speed > 1) {
			speed = 1;
		}
		if (speed < -1) {
			speed = -1;
		}

		if (numCimsRunning < 1) {
			rightDrive0.set(0);
			rightDrive1.set(0);
			rightDrive2.set(0);
		} // end if numCimsRunning is less than 1
		else if (numCimsRunning == 1) {
			rightDrive0.set(speed);
			rightDrive1.set(0);
			rightDrive2.set(0);
		} /// end 1 CIM running
		else if (numCimsRunning == 2) {
			rightDrive0.set(speed);
			rightDrive1.set(speed);
			rightDrive2.set(0);
		} // end 2 CIMS running
		else if (numCimsRunning > 2) {
			rightDrive0.set(speed);
			rightDrive1.set(speed);
			rightDrive2.set(speed);
		} else {
			System.out.println("Something has gone horribly wrong with setRightDrive.");
			leftDrive0.set(0);
			leftDrive1.set(0);
			leftDrive2.set(0);
		}
	}// end setRightDrive
		// private method for determining when to shift

	private void shiftAutomatically() {
		// shifts if neccessary, returns whether shifting was done
		if (getDriveSpeedFPS() > Constants.drivetrainUpshiftSpeedThreshold
				&& Math.abs(power) > Constants.drivetrainUpshiftPowerThreshold && isSpeedingUp() && isInLowGear()
				&& canAutoShiftAgain() && !lowGearLock) {
			// Normal Upshift
			// if fast enough to need to upshift, driver is applying sufficient
			// throttle, the robot is speeding up and it's in low gear, upshift
			new AutoShiftToHigh().start();
			lastAutoShift = timer.get();
			timeSinceLastAutoShift = 0;
		} else if (getDriveSpeedFPS() > Constants.drivetrainMaxLowGearSpeed && isInLowGear() && canAutoShiftAgain()
				&& !lowGearLock) {
			// the rev limiter was hit because driver wasn't hitting the
			// throttle hard enough to change gear
			new AutoShiftToHigh().start();
			lastAutoShift = timer.get();
			timeSinceLastAutoShift = 0;
		} else if (getDriveSpeedFPS() < Constants.drivetrainMaxLowGearSpeed
				&& Math.abs(power) > Constants.drivetrainPowerDownshiftPowerThreshold && isSlowingDown()
				&& isInHighGear() && canAutoShiftAgain()) {
			// if the robot is slowing down while the driver is applying
			// sufficient power, and is at a reasonable speed to be in low gear,
			// downshift.
			// Think of a pushing match that started at high speed
			new AutoShiftToLow().start();
			lastAutoShift = timer.get();
			timeSinceLastAutoShift = 0;
		} else if (getDriveSpeedFPS() < Constants.drivetrainDownshiftSpeedThreshold
				&& Math.abs(power) > Constants.drivetrainPowerDownshiftPowerThreshold && isInHighGear()
				&& canAutoShiftAgain()) {
			new AutoShiftToLow().start();
			lastAutoShift = timer.get();
			timeSinceLastAutoShift = 0;
		} else if (getDriveSpeedFPS() < Constants.drivetrainDownshiftSpeedThreshold
				&& Math.abs(power) < Constants.drivetrainDownshiftPowerThreshold && isInHighGear()
				&& canAutoShiftAgain()) {
			// if the robot is slowing down, not being given considerable
			// throttle
			// a coasting/stopping downshift
			new AutoShiftToLow().start();
			lastAutoShift = timer.get();
			timeSinceLastAutoShift = 0;
		}
	}
	
    public double getShiftingSpeed(){
		double currentVelocity = getDriveVelocityFPS();
		
		if(Math.abs(currentVelocity) > .05){
			
			if(currentVelocity > 0){
		    	return -Constants.drivetrainShiftPower;
			}else{
				return Constants.drivetrainShiftPower;
			}
			
		}else {
			return 0;
		}
    	
    }

	// Methods for getting encoder values
	public double getDriveDistance() {
		// returns an the average distance of the left and right encoders
		return ((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2);
	}

	public double getLeftDistance() {
		return leftEncoder.getDistance();
	}

	public double getRightDistance() {
		return rightEncoder.getDistance();
	}

	public double getDriveVelocity() {
		// returns the average speed of the left and right side in inches per
		// second
		
		
		// getVelocity is in m/s
		
		//return SWATLib.convertTo2DVector(navX.getVelocityX(), navX.getVelocityY());
		//return navX.getVelocityX();
		
		// Old version, using encoders:
		return ((leftEncoder.getRate() + rightEncoder.getRate()) / 2);
	}

	public double getDriveVelocityFPS() {
		// TODO: This should convert to fps but fix it if it doesn't
		
		//FOR NAVX WHICH SUCKS
		//return getDriveVelocity() * 3.28083989501;
		
		 return getDriveVelocity() / 12;
	}

	public double getDriveSpeed() {
		return Math.abs(getDriveVelocity());
	}

	public double getDriveSpeedFPS() {
		return Math.abs(getDriveVelocityFPS());
	}

	public double getDriveAccelFPSPS() {		
		
		//return navX.getRawAccelY();
		// Old version:
		return (currentSpeed - lastSpeed) / period;
	}

	public boolean isSpeedingUp() {
		return getDriveAccelFPSPS() > Constants.drivetrainAccelerationThreshold;
	}

	public boolean isSlowingDown() {
		return getDriveAccelFPSPS() < -Constants.drivetrainAccelerationThreshold;
	}

	// Methods for getting and setting user input
	public void arcadeDrive(double newPower, double newTurn) {
		power = newPower;
		turn = newTurn;
	}

	public double getPower() {
		return power;
	}

	public double getTurn() {
		return turn;
	}
	
	public double getAngle(){
		return navX.getAngle();
	}

	// methods for getting diagnostic data
	public double getAmperage() {
		double driveCurrent =leftDrive0.getCurrent() + leftDrive1.getCurrent() + leftDrive2.getCurrent();
		driveCurrent += rightDrive0.getCurrent() + rightDrive1.getCurrent() + rightDrive2.getCurrent();
		return driveCurrent;
	}

	public int getCimsRunningPerSide() {
		return cimsRunningPerSide;
	}

	// methods regarding shifting
	public void shiftLow() {
		power = 0;
		turn = 0;
		shifter.set(DoubleSolenoid.Value.kReverse);
	}

	public void shiftHigh() {
		power = 0;
		turn = 0;
		shifter.set(DoubleSolenoid.Value.kForward);
	}

	public boolean isInLowGear() {
		return shifter.get() == DoubleSolenoid.Value.kReverse;
	}

	public boolean isInHighGear() {
		return shifter.get() == DoubleSolenoid.Value.kForward;
	}

	public void lowGearLockDisable() {
		lowGearLock = false;
	}

	public void lowGearLockEnable() {
		lowGearLock = true;
	}

	public void disableAutoShift() {
		autoShift = false;
	}

	public void enableAutoShift() {
		autoShift = true;
	}

	public boolean isAutoShiftActive() {
		return autoShift;
	}

	public boolean canAutoShiftAgain() {
		// sets a minimum time to allow the drivetrain to begin accel or
		// continue to decel after a gear change
		// such that the shifting logic doesn't end up shifting back and forth
		// forever.
		return timeSinceLastAutoShift > Constants.drivetrainMinTimeBetweenShifts;
	}

	private void runXCims() {
		if (Robot.PDP.getTotalCurrent() > Constants.max1CimAmps || Robot.PDP.getVoltage() < Constants.min1CimVoltage) {
			// Stop drivetrain to avoid brown out
			cimsRunningPerSide = 0;

		} /// end stop drivetrain to avoid brown out
		else if (Robot.PDP.getTotalCurrent() > Constants.max2CimAmps || Robot.PDP.getVoltage() < Constants.min2CimVoltage) {
			// Run only 1 cim to avoid brown out
			cimsRunningPerSide = 1;
		} // end run 1 cim
		else if (Robot.PDP.getTotalCurrent() > Constants.max3CimAmps || Robot.PDP.getVoltage() < Constants.min3CimVoltage) {
			// Run 2 cims to avoid brown out
			cimsRunningPerSide = 2;
		} // end run 2 cims
		else {
			// ALL SYSTEMS, FULL POWER
			cimsRunningPerSide = 3;
		} // end 3 cims, also end deciding how many CIMs to run
	}

	public void integrityTestingDisable() {
		integrityTesting = false;
	}

	public void integrityTestingEnable() {
		integrityTesting = true;
	}

	private void motorIntegrityTest() {
		
		leftsideDeadTalons = 0;
		rightsideDeadTalons = 0;
		
		//Run integrity testing on all motors
		for(int i = 0; i <= 5; i++){
			if(!talonArray.get(i).isDead()){
			talonArray.get(i).integrityTest();
			}
		}
		
		//test left side talons only
		for(int i = 0; i <= 2; i++){
			if(talonArray.get(i).isDead()){
				leftsideDeadTalons++;
			}
		}

		//test right side talons only
		for(int i = 3; i >= 3 && i<=5; i++){
			if(talonArray.get(i).isDead()){
				rightsideDeadTalons++;
			}
		}
		
	}

	public void execute(double pPower, double pTurn) {
		
		lastTurn = turn;
		lastPower = power;
		power = pPower;
		turn = pTurn;

		// dampens power change to TRY to prevent brownouts
		if (Math.abs(power - lastPower) > Constants.drivetrainMaxPowerChange) {
			if (power > lastPower) {
				power = lastPower + Constants.drivetrainMaxPowerChange;
			} else {
				power = lastPower - Constants.drivetrainMaxPowerChange;
			}
		}

		// dampens power change to TRY to prevent brownouts
		if (Math.abs(turn - lastTurn) > Constants.drivetrainMaxTurnChange) {
			if (turn > lastTurn) {
				turn = lastTurn + Constants.drivetrainMaxTurnChange;
			} else {
				turn = lastTurn - Constants.drivetrainMaxTurnChange;
			}
		}
		
		// Warn console if loop hasn't been run recently.
		period = timer.get() - lastTrackedTime;
		if ((period) > Constants.drivetrainExecuteWarnTime) {
			System.out
					.println("SixCimTwoSpeed execute() hasn't run in:" + (timer.get() - lastTrackedTime) + " seconds.");
		}
		lastTrackedTime = timer.get();
		timeSinceLastAutoShift = timer.get() - lastAutoShift;

		lastSpeed = currentSpeed;
		currentSpeed = getDriveSpeedFPS();

		runXCims();

		// shiftAutomatically and set power
		if (autoShift) {
			shiftAutomatically();
		}
		
		if (integrityTesting) {
			motorIntegrityTest();
		}
		
		//compensation code: turn one side's power down if the other side has dead motors
		leftpower = power * (1 - (rightsideDeadTalons * Constants.deadMotorCompensation));
		rightpower = power * (1 - (leftsideDeadTalons * Constants.deadMotorCompensation));

		//FIXME: If a side has dead cims, you don't want to kill the live ones if too much current is drawn
		setLeftDrive(leftpower + turn, cimsRunningPerSide);
		setRightDrive(rightpower - turn, cimsRunningPerSide);

		

	}
}
