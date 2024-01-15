// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
// test for commit
public class SwerveMod extends SubsystemBase {
  /** Creates a new SwerveMod. */
  private final TalonFX driveMotor;
  private final TalonFX turnMotor;
  private final CANcoder encoder;
  private double optimizedAngle = 0;
  private final PIDController pidController;
  public SwerveMod(int driveID, int turnID, int encoderID, PIDController pidController) {
    driveMotor = new TalonFX(driveID);
    turnMotor = new TalonFX(turnID);
    encoder = new CANcoder(encoderID);
    this.pidController = pidController;
    pidController.enableContinuousInput(-180, 180); //testing if this works here move to beginning of drive if not work
  }

  public void setWheels(){

  }
  
  public double nearestAngle(double currentAngle, double targetAngle){
    double direction = (targetAngle % 360) - (currentAngle % 360); // mod is to elimiate the rotations/angles greater than 180

    if (Math.abs(direction) > 180){
      direction = -(Math.signum(direction)) * 360 + direction; // if the angle is > 180, then take the direction and either add or subtract 360 to get a angle movement < 180 
    } //tests whther or not it is 180 signum tests to see if it is negative 180 or not

    return direction;
  }
  
  public void drive(double speed, double angle){
    double currentAngle = encoder.getAbsolutePosition().getValueAsDouble() * 360 ; //why from -0.5 to 1? encoder.getAbsolutePosition() should just give ngle
    double setPoint = 0;

    double setPointAngle = nearestAngle(currentAngle, angle);
    double setPointAngleOpposite = nearestAngle(currentAngle, angle + 180); //if it can go a shorter angle in the opposite 

    if (Math.abs(setPointAngle) <= Math.abs(setPointAngleOpposite)){ // if the nearest angle is closer to or the same angle distance away from the opposite side, the setpoint is the current + the angle to travel
      setPoint = currentAngle + setPointAngle;
    } else { 
      setPoint = currentAngle + setPointAngleOpposite; // if the opposite angle is closer, that will be the angle to travel from the current angle and set the speed of the angled motor backwards so that it turns to a negative angle instead of a positive one
      speed *= -1; //this will require set inverts later to the motors
    }

    optimizedAngle = pidController.calculate(currentAngle, setPoint); //when is this called regularly? Possibly in execute function in commands?
    //returns the value from the current to setpoint in a way for motor to read
    // driveMotor.set(speed);
    // turnMotor.set(optimizedAngle);
    
  }

  public double getCanCoders(){
    return encoder.getAbsolutePosition().getValueAsDouble() * 360;
  }

  public double getTurnEncoders(){
    return turnMotor.getPosition().getValueAsDouble();
  }

  public double getDriveEncoders(){
    return driveMotor.getPosition().getValueAsDouble();
  } 

  public double getOptimizedAngle(){
    return optimizedAngle;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
