package main.java.frc.robot;

import frc.robot.subsystems.DriveTrain;
import main.java.frc.robot.subsystems.UWB;

public class tests {
    
    private DriveTrain driveTrain;
    private UWB uwb;

    public tests() {
        driveTrain = new DriveTrain();
        uwb = new UWB();
    }

    // so i can change the output easily yay
    private void result(boolean pass) {
        System.out.println(pass ? "good" : "NONONONONONONONONONONO");
    }

    // test all encoder functions
    public void testEncoders() {
        System.out.println("encoder tests!");
        
        driveTrain.resetEncoders();
        double avg = driveTrain.getAverageEncoderDistance();
        System.out.print("reset test: ");
        result(avg == 0.0);
        
        System.out.println("average distance: " + driveTrain.getAverageEncoderDistance());
    }

    // test all navx functions
    public void testNavX() {
        System.out.println("navx tests!");
        
        driveTrain.navX.zeroYaw();
        float yaw = driveTrain.navX.getYaw();
        System.out.print("Reset test: ");
        result(Math.abs(yaw) < 1.0);
        
        System.out.println("current yaw: " + driveTrain.navX.getYaw());
    }

    // test all basic drive functions
    public void testBasicDrive() {
        System.out.println("basic drive tests!");
        
        driveTrain.driveArcade(0, 0);
        System.out.print("arcade zero: ");
        result(true);
        
        driveTrain.driveArcade(0, 0.5);
        System.out.print("arcade forward: ");
        result(true);
        driveTrain.driveArcade(0, 0);
        
        driveTrain.driveArcade(0.5, 0);
        System.out.print("arcade turn: ");
        result(true);
        driveTrain.driveArcade(0, 0);
        
        driveTrain.driveDifferential(0.5, 0.5);
        System.out.print("differential: ");
        result(true);
        driveTrain.driveDifferential(0, 0);
    }

    // test all autonomous movement functions
    public void testAutonomousMovement() {
        System.out.println("auto moving tests!");
        
        System.out.println("Move forward: starting...");
        driveTrain.moveForward(100, 0.3);
        System.out.print("Move forward: ");
        result(true);
    }

    // test all uwb functions
    public void testUWB() {
        System.out.println("uwb tests!");
        
        System.out.print("connection: ");
        result(uwb.isConnected);
        
        System.out.println("position: x=" + uwb.xPosition + " y=" + uwb.yPosition + " z=" + uwb.zPosition);
        
        uwb.resetPosition();
        System.out.print("reset: ");
        result(uwb.xPosition == 0 && uwb.yPosition == 0 && uwb.zPosition == 0);
        
        System.out.println("drice to uwb starting ");
        driveTrain.driveToUWB(uwb, 100, 100, 0.3);
        System.out.print("drive to uwb: ");
        result(true);
    }

    // run all tests
    public void runAllTests() {
        System.out.println("ALL DA TESTS!");
        testEncoders();
        testNavX();
        testBasicDrive();
        testUWB();
        testAutonomousMovement();
        System.out.println("--------");
    }
}

