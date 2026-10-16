package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

/**
 * Mecanum Drivetrain Subsystem for FTC Team 26256 (SWERVO)
 * Integrated with Pinpoint Odometry for Field-Centric Vector Mapping and Toggleable Hit Correction.
 */
public class MecanumDrive {

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;

    // Toggle state for the hit auto-correction feature (Left Bumper ON, Right Bumper OFF)
    private boolean autoCorrectionEnabled = true;

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
     * Field-centric driving math for mecanum wheels with Pinpoint vector rotation and hit auto-correction toggles.
     * 
     * @param axial               Forward/backward stick input (-1.0 to 1.0)
     * @param lateral             Left/right strafe stick input (-1.0 to 1.0)
     * @param yaw                 Rotation stick input (-1.0 to 1.0)
     * @param leftBumperPressed   State of gamepad1.left_bumper (turns auto-correction ON)
     * @param rightBumperPressed  State of gamepad1.right_bumper (turns auto-correction OFF)
     * @param pinpoint            The initialized PinpointSubsystem instance
     */
    public void driveFieldCentric(double axial, double lateral, double yaw, 
                                  boolean leftBumperPressed, boolean rightBumperPressed, 
                                  PinpointSubsystem pinpoint) {
        
        // --- BUMPER TOGGLE LOGIC ---
        if (leftBumperPressed) {
            autoCorrectionEnabled = true;
        } else if (rightBumperPressed) {
            autoCorrectionEnabled = false;
        }

        // 1. Pull latest position and heading from the Pinpoint coprocessor
        pinpoint.update();
        Pose2D pose = pinpoint.getPose();
        double currentHeadingDeg = pose.getHeading(AngleUnit.DEGREES);

        // 2. Conditionally apply auto-correction to yaw if the robot takes a defensive hit
        double correctedYaw;
        if (autoCorrectionEnabled) {
            correctedYaw = pinpoint.calculateHeadingCorrection(currentHeadingDeg, yaw);
        } else {
            correctedYaw = yaw; // Pass raw driver rotation straight through
        }

        // 3. Convert robot heading to radians for field-centric vector rotation math
        double botHeadingRad = Math.toRadians(currentHeadingDeg);

        // Rotate movement vectors relative to the field instead of the robot chassis
        double rotatedLateral = lateral * Math.cos(-botHeadingRad) - axial * Math.sin(-botHeadingRad);
        double rotatedAxial   = lateral * Math.sin(-botHeadingRad) + axial * Math.cos(-botHeadingRad);

        // Denominator logic to ensure no motor power exceeds 1.0 while maintaining vector ratios
        double max;

        double frontLeftPower  = rotatedAxial + rotatedLateral + correctedYaw;
        double frontRightPower = rotatedAxial - rotatedLateral - correctedYaw;
        double backLeftPower   = rotatedAxial - rotatedLateral + correctedYaw;
        double backRightPower  = rotatedAxial + rotatedLateral - correctedYaw;

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

    /**
     * Standard robot-centric drive method (kept intact as backup or uncorrected usage).
     */
    public void drive(double axial, double lateral, double yaw) {
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

    public boolean isAutoCorrectionEnabled() {
        return autoCorrectionEnabled;
    }
}