
package org.usfirst.frc.team1806.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import org.usfirst.frc.team1806.robot.commands.utility.DataLoggingOff;
import org.usfirst.frc.team1806.robot.commands.utility.DataLoggingOn;
import org.usfirst.frc.team1806.robot.commands.utility.IntegrityTestingOff;
import org.usfirst.frc.team1806.robot.commands.utility.IntegrityTestingOn;
import org.usfirst.frc.team1806.robot.subsystems.SixCimTwoSpeed;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static SixCimTwoSpeed drivetrainSS;
	public static OI oi;
	public static SmartDashboardInterface dashboard;
	public static SendableChooser sc;
	public static SendableChooser sc_dataLogging;
	Command IntegrityCommand;
	Command dataLoggingCommand;
	public static PowerDistributionPanel PDP;
	public static States robotStates;
	public util.Logger lumberjack;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	drivetrainSS = new SixCimTwoSpeed();
    	PDP = new PowerDistributionPanel();
		oi = new OI();
		dashboard = new SmartDashboardInterface();
		sc = new SendableChooser();
		sc.addDefault("Integrity Testing: Off", new IntegrityTestingOff());
		sc.addObject("Integrity Testing: On", new IntegrityTestingOn());
		
		sc_dataLogging = new SendableChooser();
		sc_dataLogging.addDefault("Datalogging: On", new DataLoggingOn());
		sc_dataLogging.addObject("Datalogging: Off", new DataLoggingOff());
		
		SmartDashboard.putData("Integrity testing" , sc);
		SmartDashboard.putData("DataLogging", sc_dataLogging);
		dashboard.updateData();
		
		robotStates = new States();
		robotStates.reset();
		
		lumberjack = new util.Logger();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		dashboard.updateData();
	}

    public void autonomousInit() {
        //if (autonomousCommand != null) autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	
    	//For integrity testing feature.
    	IntegrityCommand = (Command) sc.getSelected();
    	IntegrityCommand.start();
    	
    	dataLoggingCommand = (Command) sc_dataLogging.getSelected();
    	dataLoggingCommand.start();
    	
    	lumberjack.writeNewTeleopCycle();
    	lumberjack.startDataTimer();
    	
    	
        //if (autonomousCommand != null) autonomousCommand.cancel();
    }

    public void disabledInit(){
    	oi.rumbleStop();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        oi.update();
        dashboard.updateData();
        lumberjack.writeDataCycle();
    }
    
    public void testPeriodic() {
        LiveWindow.run();
    }
}
