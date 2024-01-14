// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */
  private final SwerveMod FL = new SwerveMod(
  Constants.frontLeftDrive,
  Constants.frontLeftTurn,
  Constants.frontLeftCanCoder, 
  new PIDController(Constants.kp, Constants.ki, Constants.kd)); // will pid constants be different for each module?

  private final SwerveMod BL = new SwerveMod(
  Constants.backLeftDrive,
  Constants.backLeftTurn,
  Constants.backLeftCanCoder, 
  new PIDController(Constants.kp, Constants.ki, Constants.kd)); 
 
  private final SwerveMod FR = new SwerveMod(
  Constants.frontRightDrive,
  Constants.frontRightTurn,
  Constants.frontRightCanCoder, 
  new PIDController(Constants.kp, Constants.ki, Constants.kd)); 
  
  private final SwerveMod BR = new SwerveMod(
  Constants.backRightDrive,
  Constants.backRightTurn,
  Constants.backRightCanCoder, 
  new PIDController(Constants.kp, Constants.ki, Constants.kd));  
  
  public double x0 = 0; // x velocity
  public double y0 = 0; // y velocity
  //********************** */
  //HOW TO CALCULATE OFFSETS?
  //********************** */
  private double brOffset = 0; //are these angle offsets?
  private double blOffset = 0;
  private double frOffset = 0;
  private double flOffset = 0;
  //get the encoders to display the degree that the motors are at, move the motors to the straight position and use the level on phone to make sure it makes it straight facing forward, got the offset, absulte can coders DO NOT RESET
  private final AHRS navx = new AHRS(I2C.Port.kMXP); // roborio port
  
  public DriveTrain() {

  }

  public void swerveDrive(double leftX, double leftY, double rightX){
    double L = 23; // inches but should not matter as long as all units stay the same
    double W = 23; //can do without a square robot? YES
    double radius = Math.sqrt((L*L) + (W*W));

    x0 = -leftY * Math.sin(getRobotAngle()) + leftX * Math.cos(getRobotAngle()); // 
    y0 = leftY * Math.cos(getRobotAngle()) + leftX * Math.sin(getRobotAngle()); // field centric?

    double a = x0 - rightX * (L / radius);
    double b = x0 + rightX * (L / radius);
    double c = y0 - rightX * (W / radius);
    double d = y0 + rightX * (W / radius);

    double backRightSpeed = Math.sqrt((a*a) + (d*d));
    double backLeftSpeed = Math.sqrt((a*a) + (c*c));
    double frontRightSpeed = Math.sqrt((b*b) + (d*d));
    double frontLeftSpeed = Math.sqrt((b*b) + (c*c));

    double maxBackSpeed = Math.max(backLeftSpeed, backRightSpeed);
    double maxFrontSpeed = Math.max(frontLeftSpeed, frontRightSpeed);
    double maxSpeed = Math.max(maxBackSpeed, maxFrontSpeed);

    if (maxSpeed > 1) {
        backRightSpeed = backRightSpeed / maxSpeed;
        backLeftSpeed = backLeftSpeed / maxSpeed;
        frontRightSpeed = frontRightSpeed / maxSpeed;
        frontLeftSpeed = frontLeftSpeed / maxSpeed;
    }   
    double backRightAngle = Math.atan2(a, d);
    double backLeftAngle = Math.atan2(a, c);
    double frontRightAngle = Math.atan2(b, d);
    double frontLeftAngle = Math.atan2(b, c);
    
    System.out.println(
    "*****************\n" +
    "BackRightSpeed: " + backRightSpeed + "\n" + 
    "FrontRightSpeed: " + frontRightSpeed + "\n" +
    "BackLeftSpeed: " + backLeftSpeed + "\n" + 
    "FrontLeftSpeed: " + frontLeftSpeed
    );
    System.out.println(

    "BackRightAngle: " + backRightAngle + "\n" + 
    "FrontRightAngle " + frontRightAngle + "\n" +
    "BackLeftAngle: " + backLeftAngle + "\n" + 
    "FrontLeftAngle: " + frontLeftAngle +
    "*****************\n"    
    ); 
    
    BR.drive(backRightSpeed, (backRightAngle + brOffset));
    BL.drive(backLeftSpeed, (backLeftAngle + blOffset));
    FR.drive(frontRightSpeed, (frontRightAngle + frOffset));
    FL.drive(frontLeftSpeed, (frontLeftAngle + flOffset));   
  }

  public double getRobotAngle(){
    return navx.getYaw(); // -180 to 180 degrees
  }

  public double getFLCanCoders(){
    return FL.getCanCoders();
  }

  public double getBLCanCoders(){
    return BL.getCanCoders();
  }

  public double getFRCanCoders(){
    return FR.getCanCoders();
  }

  public double getBRCanCoders(){
    return BR.getCanCoders();
  }










  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
