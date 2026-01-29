package frc.robot;

import frc.robot.commands.Teleop;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveTrain;
import main.java.frc.robot.subsystems.UWB;

public class RobotContainer {
  
  public static DriveTrain driveTrain;
  public static UWB uwb;
  public static OI oi;

  public RobotContainer() {
    driveTrain = new DriveTrain();
    uwb = new UWB();
    oi = new OI();

    driveTrain.setDefaultCommand(new Teleop());
  }
}
