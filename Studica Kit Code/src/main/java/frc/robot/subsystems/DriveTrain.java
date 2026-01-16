package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase {
    
    private TitanQuad leftBack;
    private TitanQuad leftFront;
    private TitanQuad rightBack;
    private TitanQuad rightFront;

    private AHRS navX;
    private TitanQuadEncoder leftBackEncoder;
    private TitanQuadEncoder leftFrontEncoder;
    private TitanQuadEncoder rightBackEncoder;
    private TitanQuadEncoder rightFrontEncoder;

    public DriveTrain() {
        leftBack = new TitanQuad(Constants.TITAN_ID, Constants.LEFT_BACK);
        leftFront = new TitanQuad(Constants.TITAN_ID, Constants.LEFT_FRONT);
        rightBack = new TitanQuad(Constants.TITAN_ID, Constants.RIGHT_BACK);
        rightFront = new TitanQuad(Constants.TITAN_ID, Constants.RIGHT_FRONT);

        Timer.delay(1);

        System.out.println("Titan Serial number: " + leftBack.getSerialNumber());
        System.out.println("Titan ID: " + leftBack.getID());
        System.out.println("Titan " + leftBack.getFirmwareVersion());
        System.out.println("Titan " + leftBack.getHardwareVersion());

        leftBackEncoder = new TitanQuadEncoder(leftBack, Constants.LEFT_BACK, Constants.DIST_PER_TICK);
        leftFrontEncoder = new TitanQuadEncoder(leftFront, Constants.LEFT_FRONT, Constants.DIST_PER_TICK);
        rightBackEncoder = new TitanQuadEncoder(rightBack, Constants.RIGHT_BACK, Constants.DIST_PER_TICK);
        rightFrontEncoder = new TitanQuadEncoder(rightFront, Constants.RIGHT_FRONT, Constants.DIST_PER_TICK);
        navX = new AHRS(SPI.Port.kMXP);

        leftBackEncoder.setReverseDirection();
        leftFrontEncoder.setReverseDirection();

        rightBack.invertRPM();
        rightFront.invertRPM();

        leftFront.setInverted(false);
        leftBack.setInverted(false);
        rightFront.setInverted(true);
        rightBack.setInverted(true);

        resetEncoders();
    }

    public void resetEncoders() {
        leftBackEncoder.reset();
        leftFrontEncoder.reset();
        rightBackEncoder.reset();
        rightFrontEncoder.reset();
    }

    public void resetYaw() {
        navX.zeroYaw();
    }

    public float getYaw() {
        return navX.getYaw();
    }

    public void driveArcade(double x, double y) {
        x = clamp(x, -1.0, 1.0);
        y = clamp(y, -1.0, 1.0);
        
        leftBack.set(y + x);
        leftFront.set(y + x);
        rightBack.set(y - x);
        rightFront.set(y - x);
    }

    public void driveDifferential(double left, double right) {
        left = clamp(left, -1.0, 1.0);
        right = clamp(right, -1.0, 1.0);
        
        leftBack.set(left);
        leftFront.set(left);
        rightBack.set(right);
        rightFront.set(right);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public double getLeftBackEncoderDistance() {
        return leftBackEncoder.getEncoderDistance();
    }

    public double getLeftFrontEncoderDistance() {
        return leftFrontEncoder.getEncoderDistance();
    }

    public double getRightBackEncoderDistance() {
        return rightBackEncoder.getEncoderDistance();
    }

    public double getRightFrontEncoderDistance() {
        return rightFrontEncoder.getEncoderDistance();
    }

    public double getLeftBackRPM() {
        return leftBack.getRPM();
    }

    public double getLeftFrontRPM() {
        return leftFront.getRPM();
    }

    public double getRightBackRPM() {
        return rightBack.getRPM();
    }

    public double getRightFrontRPM() {
        return rightFront.getRPM();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("NavX Yaw", getYaw());
        SmartDashboard.putNumber("LB Encoder", getLeftBackEncoderDistance());
        SmartDashboard.putNumber("LB RPM", getLeftBackRPM());
        SmartDashboard.putNumber("LF Encoder", getLeftFrontEncoderDistance());
        SmartDashboard.putNumber("LF RPM", getLeftFrontRPM());
        SmartDashboard.putNumber("RB Encoder", getRightBackEncoderDistance());
        SmartDashboard.putNumber("RB RPM", getRightBackRPM());
        SmartDashboard.putNumber("RF Encoder", getRightFrontEncoderDistance());
        SmartDashboard.putNumber("RF RPM", getRightFrontRPM());
    }
}