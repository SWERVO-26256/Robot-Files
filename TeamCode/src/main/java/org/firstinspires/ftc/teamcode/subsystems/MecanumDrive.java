package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Mecanum Drivetrain Subsystem for FTC Team 26256 (SWERVO)
 */
public class MecaniumDrive {

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;

    public void init(HardwareMap hwMap) {
        // Pull motor instances using configuration names from RobotHardware
        frontLeft  = hwMap.get(DcMotorEx.class, RobotHardware.FLmotor);
        frontRight = hwMap.get(DcMotorEx.class, RobotHardware.FRmotor);
        backLeft   = hwMap.get(DcMotorEx.class, RobotHardware.BLmotor);
        backRight  = hwMap.get(DcMotorEx.class, RobotHardware.BRmotor);

        // Set motor directions for standard mecanum layout
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Set zero power behavior to brake for crisp stops
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // Run without encoders for open-loop joystick control
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Field-centric or robot-centric driving math for mecanum wheels.
     * @param axial   Forward/backward stick input (-1.0 to 1.0)
     * @param lateral Left/right strafe stick input (-1.0 to 1.0)
     * @param yaw     Rotation stick input (-1.0 to 1.0)
     */
    public void drive(double axial, double lateral, double yaw) {
        // Denominator is the largest motor power (absolute value) to ensure 
        // no individual power exceeds 1.0 while maintaining vector ratios.
        double max;

        double frontLeftPower  = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower   = axial - lateral + yaw;
        double backRightPower  = axial + lateral - yaw;

        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        frontLeft.setZeroPowerBehavior(behavior);
        frontRight.setZeroPowerBehavior(behavior);
        backLeft.setZeroPowerBehavior(behavior);
        backRight.setZeroPowerBehavior(behavior);
    }

    public void setMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
        backLeft.setMode(mode);
        backRight.setMode(mode);
    }
}