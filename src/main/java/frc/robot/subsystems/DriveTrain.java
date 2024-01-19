// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.sql.Blob;

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

  private double backRightSpeed = 0;
  private double backLeftSpeed = 0;
  private double frontRightSpeed = 0;
  private double frontLeftSpeed = 0;  
  
  private double backRightAngle = 0;
  private double backLeftAngle = 0;
  private double frontRightAngle = 0;
  private double frontLeftAngle = 0;  
  
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

    x0 = -leftY * Math.sin(getRobotAngle()) + leftX * Math.cos(getRobotAngle()); //sin() and cos() need radians 
    y0 = leftY * Math.cos(getRobotAngle()) + leftX * Math.sin(getRobotAngle()); // field centric?

    double a = x0 - rightX * (L / radius); // 0
    double b = x0 + rightX * (L / radius); // 0
    double c = y0 - rightX * (W / radius); // 1
    double d = y0 + rightX * (W / radius); // 1

    backRightSpeed = Math.sqrt((a*a) + (d*d)); //1
    backLeftSpeed = Math.sqrt((a*a) + (c*c)); // 1
    frontRightSpeed = Math.sqrt((b*b) + (d*d)); //1
    frontLeftSpeed = Math.sqrt((b*b) + (c*c)); //1

    double maxBackSpeed = Math.max(backLeftSpeed, backRightSpeed);
    double maxFrontSpeed = Math.max(frontLeftSpeed, frontRightSpeed);
    double maxSpeed = Math.max(maxBackSpeed, maxFrontSpeed);

    if (maxSpeed > 1) {
        backRightSpeed = backRightSpeed / maxSpeed;
        backLeftSpeed = backLeftSpeed / maxSpeed;
        frontRightSpeed = frontRightSpeed / maxSpeed;
        frontLeftSpeed = frontLeftSpeed / maxSpeed;
    }   
    ////Math.atan returns -pi to pi, have to convert to degrees
    backRightAngle = Math.atan2(a, d) * (180 / Math.PI) ;  //multiply by -1 with wrong ids
    backLeftAngle = Math.atan2(a, c) * (180 / Math.PI); // multiply by -1 with wrong ides
    frontRightAngle = Math.atan2(b, d) * (180 / Math.PI);
    frontLeftAngle = Math.atan2(b, c) * (180 / Math.PI);
    
    // System.out.println(
    // "*****************\n" +
    // "BackRightSpeed: " + backRightSpeed + "\n" + 
    // "FrontRightSpeed: " + frontRightSpeed + "\n" +
    // "BackLeftSpeed: " + backLeftSpeed + "\n" + 
    // "FrontLeftSpeed: " + frontLeftSpeed
    // );
    // System.out.println(

    // "BackRightAngle: " + backRightAngle + "\n" + 
    // "FrontRightAngle " + frontRightAngle + "\n" +
    // "BackLeftAngle: " + backLeftAngle + "\n" + 
    // "FrontLeftAngle: " + frontLeftAngle + "\n" +
    // "*****************\n"    
    // ); 
    
    // System.out.println("A:" + a);
    // System.out.println("B:" + b);
    // System.out.println("C:" + c);
    // System.out.println("D:" + d);

    BR.drive(backRightSpeed, backRightAngle );
    BL.drive(backLeftSpeed, backLeftAngle);
    FR.drive(frontRightSpeed, frontRightAngle );
    FL.drive(frontLeftSpeed, frontLeftAngle);   
  }

  
  public double getRobotAngle(){
    double angle = navx.getYaw();

    if (angle < 0){
      angle = (360 - Math.abs(angle));
    }
    
    return angle * (Math.PI / 180); // -180 to 180 degrees
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

  //GETTERS FOR ENCODERS (8)
  public double getBRTurn(){
    return backRightAngle;
  }

  public double getBRDrive(){
    return backRightSpeed;
  }

  public double getBLTurn(){
    return backLeftAngle;
  }

  public double getBLDrive(){
    return backLeftSpeed;
  }

  public double getFRTurn(){
    return frontRightAngle;
  }

  public double getFRDrive(){
    return frontRightSpeed;
  }

  public double getFLTurn(){
    return frontLeftAngle;
  }

  public double getFLDrive(){
    return frontLeftSpeed;
  }

  public double getFLOpAngle(){
    return FL.getOptimizedAngle();
  }
 
  public double getBLOpAngle(){
    return BL.getOptimizedAngle();
  }  
  
  public double getFROpAngle(){
    return FR.getOptimizedAngle();
  }  
  
  public double getBROpAngle(){
    return BR.getOptimizedAngle();
  }  

  public double getXVelocity(){
    return x0;
  }

  public double getYVelocity(){
    return y0;
  }

  public void stopAllMods(){ //SHOULD JUST STOP PALL DRIVE MOTORS MAKE THAT CHANGE
    FL.stopMod();
    BL.stopMod();
    BR.stopMod();
    BL.stopMod();

  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
