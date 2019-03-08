/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
 
	// driver joystick
	private Joystick driver;
  private Joystick operator;
  
  private DoubleSolenoid firstSolenoid;
  private DoubleSolenoid secondSolenoid;

   
 
	// speed controller for drive
	private VictorSP leftDrive1;
	private VictorSP leftDrive2;
	private VictorSP rightDrive1;
	private VictorSP rightDrive2;
 
  private VictorSP intake;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
  this.driver = new Joystick(0);
  this.operator = new Joystick(1);
 
	// try {
  // 	this.camera = new UsbCamera("usb cam", "/dev/video0");
  //     CameraServer.getInstance().addCamera(this.camera);
  // 	this.camera.setResolution(320, 240);
  // 	this.camera.setFPS(30);
	// } catch (Exception e) {
  // 	e.printStackTrace();
  // }

  // CameraServer.getInstance().startAutomaticCapture();

  UsbCamera camera1Camera = CameraServer.getInstance().startAutomaticCapture(0);
  UsbCamera camera2CUsbCamera = CameraServer.getInstance().startAutomaticCapture(1);
  camera1Camera.setResolution(640, 480);
  camera2CUsbCamera.setResolution(640, 480);

  this.firstSolenoid = new DoubleSolenoid(0,1);
  this.secondSolenoid = new DoubleSolenoid(2,3);

  this.leftDrive1 = new VictorSP(2);  //Left Front
	this.leftDrive2 = new VictorSP(3);  //Left Back
	this.rightDrive1 = new VictorSP(0); //right Front
	this.rightDrive2 = new VictorSP(1); //right Back 
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
	m_chooser.addOption("My Auto", kCustomAuto);
  SmartDashboard.putData("Auto choices", m_chooser);

  //Intake
  this.intake = new VictorSP(4);


 
  }
 
  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }
 

  @Override
  public void autonomousInit() {
  }
 
  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
	this.teleopPeriodic();
    }


  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
      //tank drive
      
  double left =  
  driver.getRawAxis(1);
  double right =
  driver.getRawAxis(5);

    if(left > 0) {
        left = 0.5 * left * left;
    } else {
      left = -0.5 * left * left;
    }
    if(right > 0) {
      right = 0.5 * right * right;
  } else {
    right = -0.5 * right * right;

    }
 
	// // calculations of output
	// double leftOut = driverY + driverX;
	// double rightOut = driverY - driverX;
 
	//outputs to speed controller
  this.leftDrive1.set(-left);
	this.leftDrive2.set(-left);
	this.rightDrive1.set(right);
  this.rightDrive2.set(right);
  
  if(this.operator.getRawButton(4)) { 
  this.firstSolenoid.set(DoubleSolenoid.Value.kForward);                                    //Port 0
  }
  if(this.operator.getRawButton(3)){                                                        //Port 1
    this.firstSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  if(this.operator.getRawButton(2)){                                                        //Port 2 
      this.secondSolenoid.set(DoubleSolenoid.Value.kForward);
  }
  if(this.operator.getRawButton(1)){                                                        //Port 3 
    this.secondSolenoid.set(DoubleSolenoid.Value.kReverse);
  }


    //Intake
    if(this.operator.getRawButton(11)) { // in
      this.intake.set(1);
    } else if(this.operator.getRawButton(12)) {  // out
      this.intake.set(-0.8);
    } else { // off
      this.intake.set(0);  
    }

}


 
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  
   // tank style drive control
  
  
  
	// // getting our inputs
	// double driverX = this.driver.getRawAxis(0)
	// double driverY = -this.driver.getRawAxis(1)
 
	// // calculating our outputs
 
 
	
 
	// // setting speed controllers
  
	// this.leftDrive1.set(leftStick);
	// this.leftDrive2.set(leftStick);
	
	// this.rightDrive1.set(right);
    // this.rightDrive2.set(driverY);
 
 
	//arcade drive
	double driverX = this.driver.getRawAxis(0);
	double driverY = this.driver.getRawAxis(1);
	
 
	// calculations of output
	double leftOut = driverY + driverX;
	double rightOut = driverY - driverX;
 
	//outputs to speed controller
	this.leftDrive1.set(-leftOut);
	this.leftDrive2.set(-leftOut);
	this.rightDrive1.set(rightOut);
	this.rightDrive2.set(rightOut);
 
  }
}
