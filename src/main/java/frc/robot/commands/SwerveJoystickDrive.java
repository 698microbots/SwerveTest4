// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveTrain;

public class SwerveJoystickDrive extends Command {
  /** Creates a new SwerveJoystickDrive. */
  private final DriveTrain driveTrain;
  private final Supplier<Double> leftXSupplier, leftYSupplier, rightXSupplier;
  private double tempLeftX = 0;
  private double tempLeftY = 0;
  private double tempRightX = 0;
  public SwerveJoystickDrive(DriveTrain driveTrain, Supplier<Double> leftXSupplier, Supplier<Double> leftYSupplier, Supplier<Double> rightXSupplier) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveTrain = driveTrain;
    this.leftXSupplier = leftXSupplier;
    this.leftYSupplier = leftYSupplier;
    this.rightXSupplier = rightXSupplier;
    addRequirements(driveTrain);

  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double leftX = leftXSupplier.get() * .2;
    double leftY = leftYSupplier.get() * .2;
    double rightX = rightXSupplier.get() * .2;

    // if (!(tempLeftX == leftX) || !(tempLeftY == leftY) || !(tempRightX == rightX)){
    //   if ((Math.abs(leftX) > 0.12) || (Math.abs(leftY) > 0.12) || (Math.abs(rightX) > 0.12)){ //deadzone
    //     driveTrain.swerveDrive(leftX, leftY * -1, rightX);
    //     tempLeftX = leftX;
    //     tempLeftY = leftY;
    //     tempRightX = rightX;
    //     System.out.println("IT IS RUNNING!!!");
    //   } 
    // } else {
    //     leftX = 0;
    //     leftY = 0;
    //     rightX = 0;
    //     driveTrain.swerveDrive(leftX, leftY * -1, rightX); 
    //     System.out.println("IT IS STOPPED!!!");
    // }

    if (Math.abs(leftX) < .12){
      leftX = 0;
    }
    if (Math.abs(leftY) < .12){
      leftY = 0;
    }
    if (Math.abs(rightX) < .12){
      rightX = 0;
    }
  
  
    driveTrain.swerveDrive(leftX, leftY, rightX);
  
  
  
    // if ((Math.abs(leftX) > 0.12) || (Math.abs(leftY * -1) > 0.12) || (Math.abs(rightX) > 0.12)){ //deadzone
    //   driveTrain.swerveDrive(leftX, leftY * -1, rightX);
    //   tempLeftX = leftX;
    //   tempLeftY = leftY;
    //   tempRightX = rightX;
    //   System.out.println("IT IS RUNNING!!!");
    // } else {
    //   leftX = 0;
    //   leftY = 0;
    //   rightX = 0;
    //   driveTrain.swerveDrive(leftX, leftY * -1, rightX);
    //   System.out.println("IT IS STOPPED!!!");

    // }



  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {




  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
