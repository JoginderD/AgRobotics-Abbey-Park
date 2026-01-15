package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class BuzzRover extends OpMode {

    private DcMotor leftFront, leftRear, rightFront, rightRear;

    private static final double DEADZONE = 0.06;    // joystick drift filter
    private static final double MAX_POWER = 0.75;   // cap speed
    private static final double TURN_GAIN = 0.85;   // reduce twitchy turning

    @Override
    public void init() {
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear   = hardwareMap.get(DcMotor.class, "leftRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightRear  = hardwareMap.get(DcMotor.class, "rightRear");

        // Reverse the right side so forward stick makes rover go forward
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);

        // Brake when joystick released (safer, more controlled)
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("BuzzRover ready (4-motor tank drive).");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Controls:
        // Left stick Y = forward/back
        // Right stick X = turning
        double forward = -gamepad1.left_stick_y;
        double turn    =  gamepad1.right_stick_x * TURN_GAIN;

        forward = applyDeadzone(forward, DEADZONE);
        turn    = applyDeadzone(turn, DEADZONE);

        // Tank drive mixing
        double leftPower  = forward + turn;
        double rightPower = forward - turn;

        // Normalize so values stay within [-1, 1]
        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1.0) {
            leftPower  /= max;
            rightPower /= max;
        }

        // Limit power for safety and current control
        leftPower  = Range.clip(leftPower,  -MAX_POWER, MAX_POWER);
        rightPower = Range.clip(rightPower, -MAX_POWER, MAX_POWER);

        // Apply power
        leftFront.setPower(leftPower);
        leftRear.setPower(leftPower);
        rightFront.setPower(rightPower);
        rightRear.setPower(rightPower);

        telemetry.addData("Forward", forward);
        telemetry.addData("Turn", turn);
        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);
        telemetry.update();
    }

    private double applyDeadzone(double value, double dz) {
        return (Math.abs(value) < dz) ? 0.0 : value;
    }
}
