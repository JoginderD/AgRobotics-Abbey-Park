package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase
{
    /**
     * Motors
     */
    private TitanQuad leftBack;     //M1
    private TitanQuad leftFront;    //M0
    private TitanQuad rightBack;    //M3
    private TitanQuad rightFront;   //M2

    /**
     * Sensors
     */
    private AHRS navX;
    private TitanQuadEncoder leftBackEncoder;
    private TitanQuadEncoder leftFrontEncoder;
    private TitanQuadEncoder rightBackEncoder;
    private TitanQuadEncoder rightFrontEncoder;

    public DriveTrain()
    {
        /**
         * Motor init
         */
        leftBack = new TitanQuad(Constants.TITAN_ID, Constants.LEFT_BACK);
        leftFront = new TitanQuad(Constants.TITAN_ID, Constants.LEFT_FRONT);
        rightBack = new TitanQuad(Constants.TITAN_ID, Constants.RIGHT_BACK);
        rightFront = new TitanQuad(Constants.TITAN_ID, Constants.RIGHT_FRONT);

        //Wait 1 sec for Titan to configure and startup comm process
        Timer.delay(1);

        //Printout Titan Diagnostic data
        System.out.println("Titan Serial number: " + leftBack.getSerialNumber());
        System.out.println("Titan ID: " + leftBack.getID());
        System.out.println("Titan " + leftBack.getFirmwareVersion());
        System.out.println("Titan " + leftBack.getHardwareVersion());

        /**
         * Sensor init
         */
        leftBackEncoder = new TitanQuadEncoder(leftBack, Constants.LEFT_BACK, Constants.DIST_PER_TICK);
        leftFrontEncoder = new TitanQuadEncoder(leftFront, Constants.LEFT_FRONT, Constants.DIST_PER_TICK);
        rightBackEncoder = new TitanQuadEncoder(rightBack, Constants.RIGHT_BACK, Constants.DIST_PER_TICK);
        rightFrontEncoder = new TitanQuadEncoder(rightFront, Constants.RIGHT_FRONT, Constants.DIST_PER_TICK);
        navX = new AHRS(SPI.Port.kMXP);

        /**
         * Set Flags, comment out if necessary 
         */
        //Encoder Direction
        leftBackEncoder.setReverseDirection();
        leftFrontEncoder.setReverseDirection();
        //rightBackEncoder.setReverseDirection();
        //rightFrontEncoder.setReverseDirection();

        //RPM Direction
        //leftBack.invertRPM();
        //leftFront.invertRPM();
        rightBack.invertRPM();
        rightFront.invertRPM();

        //Motor Direction
        leftFront.setInverted(false);
        leftBack.setInverted(false);
        rightFront.setInverted(true);
        rightBack.setInverted(true);



        /**
         * Reset Encoders
         */
        resetEncoders();
    }

    /**
     * Call to reset encoder counts to 0
     */
    public void resetEncoders()
    {
        leftBackEncoder.reset();
        leftFrontEncoder.reset();
        rightBackEncoder.reset();
        rightFrontEncoder.reset();
    }

    /**
     * Call to reset navX yaw angle to 0
     */
    public void resetYaw()
    {
        navX.zeroYaw();
    }

    /**
     * Call for the current angle from the internal NavX
     * <p>
     * @return yaw angle in degrees range -180° to 180°
     */
    public float getYaw()
    {
        return navX.getYaw();
    }

    /**
     * Call to drive the robot in an arcade way (one joystick) 
     * <p>
     * @param x axis, left right movement 
     * @param y axis, forward backwards movement
     */
    public void driveArcade(double x, double y)
    {
        leftBack.set(y + x);
        leftFront.set(y + x);
        rightBack.set(y - x);
        rightFront.set(y - x);
    }

    /**
     * Call to drive the robot in a differential way.
     * @param left - the speed of the left side of robot
     * @param right - the speed of the right side of robot
     */
    public void driveDifferential(double left, double right)
    {
        leftBack.set(left);
        leftFront.set(left);
        rightBack.set(right);
        rightFront.set(right);
    }

    /**
     * Call to get the distance travelled by the left back motor
     * <p>
     * @return distance travelled
     */
    public double getLeftBackEncoderDistance()
    {
        return leftBackEncoder.getEncoderDistance();
    }

    /**
     * Call to get the distance travelled by the left front motor
     * <p>
     * @return distance travelled
     */
    public double getLeftFrontEncoderDistance()
    {
        return leftFrontEncoder.getEncoderDistance();
    }

    /**
     * Call to get the distance travelled by the right back motor
     * <p>
     * @return distance travelled
     */
    public double getRightBackEncoderDistance()
    {
        return rightBackEncoder.getEncoderDistance();
    }

    /**
     * Call to get the distance travelled by the right front motor
     * <p>
     * @return distance travelled
     */
    public double getRightFrontEncoderDistance()
    {
        return rightFrontEncoder.getEncoderDistance();
    }

    /**
     * Call to get the RPM of the left back motor
     * <p>
     * @return the rpm of the motor
     */
    public double getLeftBackRPM()
    {
        return leftBack.getRPM();
    }

    /**
     * Call to get the RPM of the left front motor
     * <p>
     * @return the rpm of the motor
     */
    public double getLeftFrontRPM()
    {
        return leftFront.getRPM();
    }


    /**
     * Call to get the RPM of the right back motor
     * <p>
     * @return the rpm of the motor
     */
    public double getRightBackRPM()
    {
        return rightBack.getRPM();
    }

    /**
     * Call to get the RPM of the right front motor
     * <p>
     * @return the rpm of the motor
     */
    public double getRightFrontRPM()
    {
        return rightFront.getRPM();
    }

    /**
     * Code that runs once every robot loop
     */
    @Override
    public void periodic()
    {
        SmartDashboard.putNumber("NavX Yaw", getYaw());
        SmartDashboard.putNumber("Left Back Encoder", getLeftBackEncoderDistance());
        SmartDashboard.putNumber("Left Back RPM", getLeftBackRPM());
        SmartDashboard.putNumber("Left Front Encoder", getLeftFrontEncoderDistance());
        SmartDashboard.putNumber("Left Front RPM", getLeftFrontRPM());
        SmartDashboard.putNumber("Right Back Encoder", getRightBackEncoderDistance());
        SmartDashboard.putNumber("Right Back RPM", getRightBackRPM());
        SmartDashboard.putNumber("Right Front Encoder", getRightFrontEncoderDistance());
        SmartDashboard.putNumber("Right Front RPM", getRightFrontRPM());
    }
}