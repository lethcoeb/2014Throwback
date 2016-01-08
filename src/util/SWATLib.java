package util;

import org.usfirst.frc.team1806.robot.Robot;

public class SWATLib {

	private static double m_angle1;
	private static double m_angle2;
	
	public static double convertTo2DVector(double angle1, double angle2){
		m_angle1 = angle1;
		m_angle2 = angle2;
		
		return Math.sqrt(Math.pow(m_angle1, 2)) + (Math.pow(m_angle2, 2));
	}
	
	public static double average(double num1, double num2){
		return (num1 + num2)/2;
	}
	
	private static double m_xAxis;
	private static double m_yAxis;
	private static double m_angle;
	
	public static double joystickAngle(double xAxis, double yAxis){
		
		m_xAxis = xAxis;
		m_yAxis = yAxis;
		
		m_angle = Math.atan(Robot.oi.rsY/Robot.oi.rsX) * (180/Math.PI);
		
		if(m_xAxis > 0 && m_yAxis > 0){
			//quadrant 1
			m_angle = -m_angle;
		}else if(m_xAxis < 0 && m_yAxis > 0){
			//quadrant 2
			m_angle = m_angle + 90;
		}else if(m_xAxis < 0 && m_yAxis < 0){
			//quadrant 3
			
		}else{
			//quadrant 4
		}
		
		return m_angle;
		
	}
	
	
}
