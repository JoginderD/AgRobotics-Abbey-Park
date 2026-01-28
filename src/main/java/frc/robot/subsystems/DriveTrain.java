
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

    public AHRS navX;
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

    // guess.
    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    // simple driving, y moves forward backwards, x turns left right
    public void driveArcade(double x, double y) {
        x = clamp(x, -1.0, 1.0);
        y = clamp(y, -1.0, 1.0);
        
        leftBack.set(y + x);
        leftFront.set(y + x);
        rightBack.set(y - x);
        rightFront.set(y - x);
    }


    // gets the average distance traveled across all four wheels
    public double getAverageEncoderDistance() {
        return (leftBackEncoder.getEncoderDistance() + leftFrontEncoder.getEncoderDistance() + rightBackEncoder.getEncoderDistance() + rightFrontEncoder.getEncoderDistance()) / 4.0;
    }

    // keeps moving the robot forward until the encoders say they have reached the distance. power needs to be tuned!
    public void moveForward(double distance, double power) {
        resetEncoders();        
        
        while (getAverageEncoderDistance() < distance) {
            driveArcade(0, power);
        }
        
        driveArcade(0, 0); // stop the robot!!
    }
    
    // continuously updates path to target 10 times per second
    public void driveToUWB(UWB uwb, double targetX, double targetY, double power) {
        long lastUpdate = System.currentTimeMillis();
        
        while (true) {
            // recalculate distance
            double deltaX = targetX - uwb.xPosition;
            double deltaY = targetY - uwb.yPosition;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            
            if (distance <= 100) break; // tolerance for ending the loop - make it larger and the robot will stop further
            
            // recalculate every 100ms (10 times per second)
            if (System.currentTimeMillis() - lastUpdate >= 100) {
                // oo finally interestingm ath  - calcualtes angle to target
                double targetAngle = Math.toDegrees(Math.atan2(targetX - uwb.xPosition, targetY - uwb.yPosition));
                double angleDiff = targetAngle - navX.getYaw();
                while (angleDiff > 180) angleDiff -= 360;
                while (angleDiff < -180) angleDiff += 360;
                
                // adjusting da heading while driving
                double turnAmount = Math.signum(angleDiff) * Math.min(Math.abs(angleDiff) / 90.0, 1.0);
                driveArcade(turnAmount * Constants.TURN_GAIN, power);
                
                lastUpdate = System.currentTimeMillis();
            }
        }
        
        driveArcade(0, 0);
    }


    // rotates robot to target angle
    public void turnToAngle(double targetAngle, double power) {
        double currentAngle = navX.getYaw();
        double angleDiff = targetAngle - currentAngle;
        while (angleDiff > 180) angleDiff -= 360;
        while (angleDiff < -180) angleDiff += 360;
        
        while (Math.abs(angleDiff) > 5) { // 5 degree tolerance
            double turnPower = Math.signum(angleDiff) * power * Constants.TURN_GAIN;
            driveArcade(turnPower, 0);
            currentAngle = navX.getYaw();
            angleDiff = targetAngle - currentAngle;
            while (angleDiff > 180) angleDiff -= 360;
            while (angleDiff < -180) angleDiff += 360;
        }
        
        driveArcade(0, 0);
    }


    @Override
    public void periodic() {
    }
}