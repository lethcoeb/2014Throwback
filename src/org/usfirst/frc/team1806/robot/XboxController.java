package org.usfirst.frc.team1806.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class XboxController extends Joystick {

    private DriverStation _ds;
    private final int _port;

    public XboxController(int port) {
    	super(port);
        _ds = DriverStation.getInstance();
        _port = port;
    }

    public double getRawAxis(final int axis) {
        return _ds.getStickAxis(_port, axis);
    }

    public boolean getRawButton(final int button) {
        return ((0x1 << (button - 1)) & _ds.getStickButtons(_port)) != 0;
    }
    
    public boolean isPressed(final int button){
    	return getRawButton(button);
    }
    
    public double getRightTrigger() {
        return getRawAxis(3);
    	//return Math.max(getRawAxis(3), 0);
    }

    public double getLeftTrigger() {
        return getRawAxis(2);
    	//return Math.max(getRawAxis(2), 0);
    }

    public double getRightJoyX() {
        return getRawAxis(4);
    }

    public double getRightJoyY() {
        return -getRawAxis(5);
    }

    public double getLeftJoyX() {
        return getRawAxis(0);
    }

    public double getLeftJoyY() {
        return -getRawAxis(1);
    }

    public boolean getButtonA() {
        return getRawButton(1);
    }

    public boolean getButtonB() {
        return getRawButton(2);
    }

    public boolean getButtonX() {
        return getRawButton(3);
    }

    public boolean getButtonY() {
        return getRawButton(4);
    }

    public boolean getButtonBack() {
        return getRawButton(7);
    }

    public boolean getButtonStart() {
        return getRawButton(8);
    }

    public boolean getButtonRB() {
        return getRawButton(6);
    }

    public boolean getButtonLB() {
        return getRawButton(5);
    }

    public boolean getButtonLS() {
        return getRawButton(9);
    }

    public boolean getButtonRS() {
        return getRawButton(10);
    }
    
    public boolean getPOVUp(){
    	return getPOV() > 45 && getPOV() < 135;
    }
    
    public boolean getPOVLeft(){
    	return getPOV() > 135 && getPOV() < 225;
    }
    
    public boolean getPOVDown(){
    	return getPOV() > 225 && getPOV() < 315;
    }
    
    public boolean getPOVRight(){
    	return getPOV() > 315 && getPOV() < 45;
    }
    
}