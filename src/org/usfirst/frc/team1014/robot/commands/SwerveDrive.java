package org.usfirst.frc.team1014.robot.commands;

import org.usfirst.frc.team1014.robot.OI;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SwerveDrive extends CommandBase {

	public SwerveDrive()
	{
		requires((Subsystem) driveTrain);
	}
	@Override
	protected void initialize() {
		driveTrain.swerveDrive(0, 0, 0);
	}

	@Override
	protected void execute() {
		driveTrain.swerveDrive(OI.xboxController.getLeftStickY(), OI.xboxController.getLeftStickX(), OI.xboxController.getRightStickX());	  
		if (OI.xboxController.isBButtonPressed()) { driveTrain.foo(); }
		else if (OI.xboxController.isAButtonPressed()) { driveTrain.bar(); }
		else { driveTrain.baz(); }
	}
	
	@Override
	public String getConsoleIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
