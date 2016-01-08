package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.usfirst.frc.team1806.robot.Robot;
import org.usfirst.frc.team1806.robot.States;

import edu.wpi.first.wpilibj.Timer;

public class Logger {

	// lumberjack.
	//From here on the logger class will be referred to as the lumberjack

	
	//TODO: Make this write to a local file on the roboRIO, or better yet give the user an option on the dashboard.
	Timer timer;
	Calendar c;
	String fileName;
	FileWriter fileWriter;
	BufferedWriter bufferedWriter;

	public Logger() {
		
		timer = new Timer();
		c = Calendar.getInstance();
		fileName = "/logging/" + String.valueOf(c.getTime() + ".csv");
		
		
		// replace spaces and colons to make filename POLITICALLY CORRECT
		//#trump 2016
		fileName = fileName.replaceAll("\\s", "_");
		fileName = fileName.replace(":", "-");

		try {

			//Instantiate objects
			fileWriter = new FileWriter(fileName, true);
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException ex) {
			System.out.println("Failed to instantiate writers. Killing datalogging.");
			Robot.robotStates.dataLoggerTracker = States.dataLogger.OFF;
		}

	}

	public void writeNewTeleopCycle() {
		
		if(Robot.robotStates.dataLoggerTracker == States.dataLogger.ON){
			
			newLine();
			writeData("New Teleop Cycle Started");
			//Specify what you're writing here; helps in interpreting the CSV
			writeData("Time,TotalCurrent");
			newLine();

			//Old stuff from 2015 robot code for reference.
			/*bufferedWriter.write("\r\n" + "New Teleop Cycle Started" + "\r\n");
			bufferedWriter.write("P: " + Constants.elevatorP + "," + "I: " + Constants.elevatorI + "," + "D: "
					+ Constants.elevatorD + "\r\n");
			bufferedWriter.write(Constants.secondStagePIDEngage + " = PID Engage Height" + ","
					+ Constants.secondStageHeight + " = PID Target Height" + "\r\n" + "\r\n");
			bufferedWriter.write("Height,Time,Power" + "\r\n");*/
			
		}
	}
	
	//currently only logs total current and time.
	//you NEED to separate your values with commas. That's what a CSV (comma separated values) file is fam.
	public void writeDataCycle(){
		if(Robot.robotStates.dataLoggerTracker == States.dataLogger.ON){
			newLine();
			writeData(timer.get() + "," + String.valueOf(Robot.PDP.getTotalCurrent()));
		}
	}
	
	private void newLine(){
		try{
			bufferedWriter.write("\r\n");
			bufferedWriter.close();
		}catch(IOException ex){
			System.out.println("Killing datalogger - failed to write newline: " + ex);
			Robot.robotStates.dataLoggerTracker = States.dataLogger.OFF;
		}
	}
	
	private void writeData(String data){
		try{
			bufferedWriter.write(data);
			bufferedWriter.close();
		}catch(IOException ex){
			System.out.println("Killing datalogger - failed to write data " + ex);
			Robot.robotStates.dataLoggerTracker = States.dataLogger.OFF;
		}
	}
	
	public void startDataTimer(){
		timer.start();
	}

}
