package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveTrain;
import main.java.frc.robot.subsystems.UWB;

public class Teleop extends CommandBase {
    
    private static final DriveTrain driveTrain = RobotContainer.driveTrain;
    private static final UWB uwb = RobotContainer.uwb;
    private static final OI oi = RobotContainer.oi;
    
    private boolean uwbDriving = false;
    private Thread uwbThread;

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
        // A button starts uwb driving to fixed target
        if (oi.getDriveAButton() && !uwbDriving) {
            uwbDriving = true;
            uwbThread = new Thread(() -> {
                driveTrain.driveToUWB(uwb, Constants.UWB_TARGET_X, Constants.UWB_TARGET_Y, Constants.UWB_DRIVE_POWER);
                uwbDriving = false;
            });
            uwbThread.start();
        }
        
        // B button stops uwb driving immediately
        if (oi.getDriveBButton() && uwbDriving) {
            uwbDriving = false;
            if (uwbThread != null) {
                uwbThread.interrupt();
            }
            driveTrain.driveDifferential(0.0, 0.0);
        }
        
        // normal joystick driving when not uwb driving
        if (!uwbDriving) {
            double forward = -oi.getLeftDriveY();
            double turn = oi.getLeftDriveX() * Constants.TURN_GAIN;

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
        
        SmartDashboard.putBoolean("UWB Driving", uwbDriving);
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