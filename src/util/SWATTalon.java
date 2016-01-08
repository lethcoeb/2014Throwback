package util;

import org.usfirst.frc.team1806.robot.Constants;
import org.usfirst.frc.team1806.robot.Robot;

import edu.wpi.first.wpilibj.Talon;

public class SWATTalon extends Talon{

	private int pdpChannel;
	//TODO make this private
	public int deadCycles = 0;
	public boolean dead = false;
	
	public SWATTalon(int channel, int PDPChannel) {
		super(channel);
		this.pdpChannel = PDPChannel;
	}
	
	public void integrityTest(){
		if(this.get() > Constants.motorMinPower && Robot.PDP.getCurrent(pdpChannel) < Constants.motorMinCurrent){
			//Sending power but not drawing current
			deadCycles++;
		}else if (this.get() > Constants.motorMinPower && Robot.PDP.getCurrent(pdpChannel) > Constants.motorMinCurrent){
			//Sending power and drawing current
			deadCycles = 0;
		}
	}
	
	public boolean isDead(){
		
		if(deadCycles >= Constants.cyclesUntilDead){
			dead = true;
		}else{
			dead = false;
		}
		
		return dead;
	}
	
	public double getCurrent(){
		return Robot.PDP.getCurrent(pdpChannel);
	}

}
