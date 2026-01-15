/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public final class Constants
{
    /**
     * Motor Constants
     */
    public static final int TITAN_ID    = 42;
    public static final int LEFT_FRONT  = 0;
    public static final int LEFT_BACK   = 1;
    public static final int RIGHT_FRONT = 2;
    public static final int RIGHT_BACK  = 3;

    /**
     * Encoder Constants
     */

    //Radius of Wheel
    private static final double wheelRadius  = 125.0 / 2.0; //mm

    //Encoder pulses per revolution
    private static final double pulsePerRevolution = 1464;

    //Gear ratio between encoder and wheel
    private static final double gearRatio            = 1/1; //Wheel on motor shaft

    //Pulse per revolution of the wheel
    private static final double encoderPulseRatio    = pulsePerRevolution * gearRatio;

    //Distance per tick
    public static final double DIST_PER_TICK        = (Math.PI * 2 * wheelRadius) / encoderPulseRatio;
}