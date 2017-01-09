package org.usfirst.frc.team1014.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
	
	//Analogs
	public static int backLeftController = 3; // CAN
	public static int frontLeftController = 0; // CAN
	public static int frontRightController = 1; // CAN
	public static int backRightController = 2;// CAN
	
	public static int backLeftRotationController = 3; // PWM
	public static int frontLeftRotationController = 8; // PWM
	public static int frontRightRotationController = 6; // PWM
	public static int backRightRotationController = 0;// PWM
	
	public static int frontLeftRotationEncoderChannelA = 0; // PWM
	public static int frontLeftRotationEncoderChannelB= 1; // PWM
	
	public static int backLeftRotationEncoderChannelA = 2; // PWM
	public static int backLeftRotationEncoderChannelB = 3; // PWM

	public static int frontRightRotationEncoderChannelA = 4; // PWM
	public static int frontRightRotationEncoderChannelB = 5; // PWM

	public static int backRightRotationEncoderChannelA = 6;// PWM
	public static int backRightRotationEncoderChannelB = 7;// PWM
	
	public static int lift1 = 7; //PWM
	public static int lift2 = 8; //PWM
	public static int lift3 = 9; //PWM
	
	public static int opticalSensorPing = 0;
	public static int opticalSensorCatch = 1;
	

    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
}
