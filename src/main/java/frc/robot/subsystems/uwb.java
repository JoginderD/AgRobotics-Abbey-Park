package main.java.frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import main.java.frc.robot.Constants;

// to future us: heres how to connect the uwb
// Step one. plug the uwb module into a usb port on the roborio
// Step dos. verify UWB_BAUD_RATE in constants.java matches your module - by default its 115200
// Step 3. if multiple usb devices are connected, change SerialPort.Port.kUSB to kUSB1 or kUSB2 in the constructor
// 4. check glass for "connected?" to verify connection (optional, useful)
// Number five. if data format is wrong, update the parsing logic in updatePosition() because i didnt know any better im sorry

public class UWB extends SubsystemBase {
    
    private SerialPort serialPort;
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public boolean isConnected;

    //connects the UWB. what else did you think it would do.
    public UWB() {
        // putting this try here because i have no clue if this is actually going to work why am i writing code for hardware i dont have and cant test kill me now also just  change everything in the constants file please dont change this directly
        try {
            serialPort = new SerialPort(Constants.UWB_BAUD_RATE, SerialPort.Port.kUSB);
            serialPort.setWriteBufferSize(Constants.UWB_BUFFER_SIZE);
            serialPort.setReadBufferSize(Constants.UWB_BUFFER_SIZE);
            isConnected = true;
            System.out.println("Connected yay");
        } catch (Exception e) {
            isConnected = false;
            System.err.println("Uh oh: " + e.getMessage());
        }

        resetPosition();
    }

    public void resetPosition() {
        xPosition = 0.0;
        yPosition = 0.0;
        zPosition = 0.0;
    }

    private void updatePosition() {
        //in case its not connected
        if (!isConnected || serialPort.getBytesReceived() == 0) {
            return;
        }

        // please just work first try
        try {   
            String data = serialPort.readString();
            if (data != null && !data.isEmpty()) {
                // ok so this might not work based on how the data is formatted, but im expecting it to look like "X:123.45,Y:67.89,Z:10.11" 
                String[] parts = data.trim().split(",");
                for (String part : parts) {
                    //i dont care that this is not elegant
                    if (part.startsWith("X:")) {
                        xPosition = Double.parseDouble(part.substring(2));
                    } else if (part.startsWith("Y:")) {
                        yPosition = Double.parseDouble(part.substring(2));
                    } else if (part.startsWith("Z:")) {
                        zPosition = Double.parseDouble(part.substring(2));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("uh oh: " + e.getMessage());
        }
    }

    @Override
    public void periodic() {
        updatePosition();
        //for testing - you can see the values on glass
        SmartDashboard.putBoolean("connected?", isConnected);
        SmartDashboard.putNumber("uwb x", xPosition);
        SmartDashboard.putNumber("uwb y", yPosition);
        SmartDashboard.putNumber("uwb z", zPosition);
    }
}
