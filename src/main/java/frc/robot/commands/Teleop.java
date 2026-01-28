package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveTrain;

public class Teleop extends CommandBase {
    
    private static final DriveTrain driveTrain = RobotContainer.driveTrain;
    private static final OI oi = RobotContainer.oi;

    public Teleop() {
        addRequirements(driveTrain);
    }

    @Override
    public void initialize() {
        driveTrain.resetEncoders();
        driveTrain.navX.zeroYaw();
    }

    @Override
    public void execute() {
        double forward = -oi.getLeftDriveY();
        double turn = oi.getRightDriveX() * Constants.TURN_GAIN;

        double leftPower = forward + turn;
        double rightPower = forward - turn;

        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1.0) {
            leftPower /= max;
            rightPower /= max;
        }

        leftPower = clamp(leftPower, -Constants.MAX_POWER, Constants.MAX_POWER);
        rightPower = clamp(rightPower, -Constants.MAX_POWER, Constants.MAX_POWER);

        driveTrain.driveDifferential(leftPower, rightPower);

        SmartDashboard.putNumber("Forward", forward);
        SmartDashboard.putNumber("Turn", turn);
        SmartDashboard.putNumber("Left", leftPower);
        SmartDashboard.putNumber("Right", rightPower);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.driveDifferential(0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}