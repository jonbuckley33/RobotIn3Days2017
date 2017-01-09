package org.usfirst.frc.team1014.robot.subsystems;

import org.usfirst.frc.team1014.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.DigitalInput;


public class SwerveDriveTrain extends BadSubsystem {
    
	private static SwerveDriveTrain instance;
	
	private static double swerveDegreeMargin = 3.0;
	
	Talon gatherer;
	CANTalon frontLeft, backLeft, frontRight, backRight;
	SpeedController frontLeftRotation, backLeftRotation, frontRightRotation, backRightRotation;
	Encoder frontLeftEncoder, backLeftEncoder, frontRightEncoder, backRightEncoder;
	Boolean frontLeftReversed, frontRightReversed, backLeftReversed, backRightReversed;
	
	public static SwerveDriveTrain getInstance()
    {
        if (instance == null)
        {
            instance = new SwerveDriveTrain();
        }
        return instance;
    }
	
    private SwerveDriveTrain()
    {
    	
    }
	
	@Override
	protected void initialize() {
		frontLeftReversed = false;
		frontRightReversed = false;
		backLeftReversed = false;
		backRightReversed = false;
		
		gatherer = new Talon(1);
		
        frontLeft = new CANTalon(RobotMap.frontLeftController);
        frontLeft.setInverted(true);
        backLeft = new CANTalon(RobotMap.backLeftController);
        backLeft.setInverted(true);
        frontRight = new CANTalon(RobotMap.frontRightController);
        frontRight.setInverted(true);
        backRight = new CANTalon(RobotMap.backRightController); 
        backRight.setInverted(true);
        
        frontLeftEncoder = new Encoder(RobotMap.frontLeftRotationEncoderChannelA, RobotMap.frontLeftRotationEncoderChannelB);
        frontRightEncoder = new Encoder(RobotMap.frontRightRotationEncoderChannelA, RobotMap.frontRightRotationEncoderChannelB);
        backLeftEncoder = new Encoder(RobotMap.backLeftRotationEncoderChannelA, RobotMap.backLeftRotationEncoderChannelB);
        backRightEncoder = new Encoder(RobotMap.backRightRotationEncoderChannelA, RobotMap.backRightRotationEncoderChannelB);
        
        frontLeftEncoder.reset();
        frontRightEncoder.reset();
        backLeftEncoder.reset();
        backRightEncoder.reset();

        frontLeftRotation = new Talon(RobotMap.frontLeftRotationController);
        backLeftRotation = new Talon(RobotMap.backLeftRotationController);
        frontRightRotation = new Talon(RobotMap.frontRightRotationController);
        backRightRotation = new Talon(RobotMap.backRightRotationController);
	}
	
	private float getRotationOfWheel(Encoder encoder) {		
		int raw = encoder.get();
		return mod(-raw, 360);
	}
	
	private int mod(int x, int y)
	{
	    int result = x % y;
	    if (result < 0)
	    {
	        result += y;
	    }
	    return result;
	}
	
	public void foo() {
		gatherer.set(1.0);
	}
	
	public void bar() {
		gatherer.set(-1.0);
	}
	
	public void baz() {
		gatherer.set(0.0);
	}
	
	public static double minDifferenceBetweenAngles(double alpha, double theta) {
		double delta1 = theta - alpha;
        double delta2 = theta > alpha ? (theta - 360) - alpha : theta - (alpha - 360);
        double minDelta = Math.abs(delta1) > Math.abs(delta2) ? delta2 : delta1;
		System.out.println("Current angle: " + alpha + " target angle: " + theta + " deltas: " + delta1 + " " + delta2); 

        return minDelta;
	}
	
	static double rotationSpeedScalar = 1.0 / 40.0;
	private void setRotationOnWheel(SpeedController controller, Encoder encoder, double targetAngle, Boolean reversed) {
		double currentAngle = getRotationOfWheel(encoder);
		double inverseTargetAngle = mod((int) targetAngle + 180, 360);
        double angleDiff1 = minDifferenceBetweenAngles(currentAngle, targetAngle);
        double angleDiff2 = minDifferenceBetweenAngles(currentAngle, inverseTargetAngle);
        
        double minDiff = angleDiff1;
        if (Math.abs(angleDiff2) < Math.abs(angleDiff1)) {
        	minDiff = angleDiff2;
        	System.out.println("Flipping motor");
        	reversed = true;
        } else {
        	System.out.println("Not flipping motor");
        	reversed = false;
        }
 
        if (Math.abs(minDiff) > swerveDegreeMargin)
		{
        	double rawSpeed = Math.abs(minDiff * rotationSpeedScalar);
        	double clampedSpeed = rawSpeed > 1.0 ? 1.0 : rawSpeed;
            if (minDiff > 0)
            {
            	controller.set(-clampedSpeed);
            }
            else	
            {
            	controller.set(clampedSpeed);
            }
        } else {
        	controller.set(0.0);
        }
	}
	
    public void swerveDrive(double leftY, double leftX, double rightX) //analogs
    {
    	//System.out.printf("Encoder values (fl, bl, fr, br): %d %d %d %d\n", frontLeftEncoder.get(), backLeftEncoder.get(), frontRightEncoder.get(), backRightEncoder.get());
    	
        //convert cart to polar
        double radius = Math.sqrt( leftX * leftX + leftY * leftY );//speed      

        // Don't divide by 0.
        if (radius <= 0) {
        	this.frontLeftRotation.set(0);
        	this.frontRightRotation.set(0);
        	this.backLeftRotation.set(0);
        	this.backRightRotation.set(0);
        	
        	this.frontLeft.set(0);
        	this.frontRight.set(0);
        	this.backLeft.set(0);
        	this.backRight.set(0);
        } else {
        	double angleInDegrees = mod((int)-Math.toDegrees(Math.acos( leftX / radius )) + 90, 360);//desired angle
            
            System.out.println("\n\n");
            setRotationOnWheel(frontLeftRotation, frontLeftEncoder, angleInDegrees, frontLeftReversed);
//            setRotationOnWheel(frontRightRotation, frontRightEncoder, angleInDegrees, frontRightReversed);
//            setRotationOnWheel(backLeftRotation, backLeftEncoder, angleInDegrees, backLeftReversed);
//            setRotationOnWheel(backRightRotation, backRightEncoder, angleInDegrees, backRightReversed);
            
        	frontLeft.set(frontLeftReversed ? -radius : radius);
//            frontRight.set(frontRightReversed ? -radius : radius);
//            backLeft.set(backLeftReversed ? -radius : radius);
//            backRight.set(backRightReversed ? -radius : radius);
        }
    }
    
	/**
	 * Tank drives the robot
	 * 
	 * @param leftY
	 * @param rightY
	 */
	
    public void tankDrive(double leftY, double rightY) //analogs
    {
        //train.tankDrive(leftY, rightY);
    }
    /**
     * This drive the robot with in orienation with the field with mecanum wheels where the axels of the rollers form an X across the robot
     * 
     * @param leftX
     * @param leftY
     * @param rightX
     * @param gyro
     */
    
    public void mecanumDriveCartesian(double leftX, double leftY, double rightX, double gyro) 
    {
    	//train.mecanumDrive_Cartesian(leftX, leftY, rightX, gyro);
    }
    /**
     * Sets each motor speeds at a certain value
     * @param fl
     * @param bl
     * @param fr
     * @param br
     */
    public void setMotors(double fl, double bl, double fr, double br)
    {
    	frontLeft.set(fl);
    	backLeft.set(bl);
    	frontRight.set(fr);
    	backRight.set(br);
    }
    
    
    /**
     * rotates the robot at a given speed
     * 
     * if speed > 0, rotates counter clockwise right
     * @param speed
     */
    public void rotateRobotDifference(double speed) // works
    {
    	frontLeft.set(speed);
    	frontRight.set(-speed);
    	backLeft.set(-speed);
    	backRight.set(speed);
    }
    
    /**
     * This method, using the gyro and the dpad, lines up the robot in orientation with the field.  
     * 
     * Please don't look at it
     * @param dpadAngle
     * @param mxpAngle
     */
    
    public void lineUpWithField(int dpadAngle, double mxpAngle)
    {
    	if(mxpAngle < 0) // makes mxpAngle comparable to gyro, works
    	{
        	mxpAngle = mxpAngle + 360;
    	}
    	if(!isNear(dpadAngle, mxpAngle))
    	{
        	double angleDif = 0;
        	boolean turnLeft = false;

        	if(dpadAngle == 0 && mxpAngle > 180)
        	{
        		angleDif = mxpAngle - dpadAngle;
        		double motorSpeedToPut = convertToMotorSpeed(angleDif);
        		rotateRobotDifference(-motorSpeedToPut);
        	}
        	else
        	{
            	if(dpadAngle > mxpAngle) // rotate left
            	{
            		angleDif = dpadAngle - mxpAngle;
            		if(angleDif > 180)
            		{
            			angleDif = Math.abs(angleDif - 360);
            		}
            			
            		turnLeft = false; // yes it is redundant but I dont care
            	}
            	else
            	{
            		angleDif = mxpAngle - dpadAngle;
            		if(angleDif > 180)
            		{
            			angleDif = Math.abs(angleDif - 360);
            		}
            		
            		turnLeft = true; 
            	}
            	
            	
            	
        		double motorSpeedToPut = convertToMotorSpeed(angleDif);
        		
        		if(turnLeft)
        			rotateRobotDifference(motorSpeedToPut);
        		else
        			rotateRobotDifference(-motorSpeedToPut);
        	}
        	

        	
        	
    	}

    }
    
    /**
     * This method takes an angle difference and converts to a motor speed.  Basically to fake PID
     * 
     * Meant to really only be used with the mxp and dpad, but can be converted
     * 
     * If angleDif > 0, turn left or counter clockwise
     * 
     * @return yourmom
     */
    
    public double convertToMotorSpeed(double angleDifference) // this never works if given a negative value
    {    	
    	
    	if(angleDifference/90 < .01) // 360 would give a perfect linear speed but I want it faster with a little overshoot
    	{
    		return 0;
    	}
    	else
    	{
    		return clampMotorValues(Math.abs(angleDifference/90));
    	}
    }
    
    /**
     * this is for the dpadRotation.  The Gyro will never return a value exactly equal to the POV from the Controller so this just gets it close
     * You can make it closer by lowering the magic number .1 in the if statement
     * 
     * 
     * @param num1
     * @param num2
     * @return is the number near
     */
    public boolean isNear(double num1, double num2)
    {
    	if(Math.abs(num2-num1) < .01)
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * This method clamps down values to give to the motors
     * 
     * @param value
     * @return
     */
    
    
    private double clampMotorValues(double value)
    {

        if (value > 1)
        {
            value = 1;
        }
        if (value < -1)
        {
            value = -1;
        }
        return value;
    }
    

	@Override
	public String getConsoleIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
