package org.firstinspires.ftc.teamcode.subsystems;

import.com.qualcomm.robotcore.hardware.CRservo
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Minimal robot hardware map for the drivetrain-only build.
 * Shooter and other mechanism wiring can be added later when needed.
 */
public class RobotHardware {

    // Drive motor configuration names
    public static final String FLmotor = "frontLeft";
    public static final String FRmotor = "frontRight";
    public static final String BLmotor = "backLeft";
    public static final String BRmotor = "backRight";

    // Optional odometry setup for later autonomous work
    public static final String PINPOINT = "pinpoint";
    public static final String PINPOINT_NAME = PINPOINT;

    // Intake Spark mini
    public static final String INTAKE = "intake"
    public CRServorvo intakeMotor = null;

    public DcMotorEx frontLeft = null;
    public DcMotorEx frontRight = null;
    public DcMotorEx backLeft = null;
    public DcMotorEx backRight = null;
    Intake_mo

    public MecanumDrive driveSubsystem = null;
    public PinpointSubsystem pinpointSubsystem = null;

    public void init(HardwareMap ahwMap) {
        frontLeft = ahwMap.get(DcMotorEx.class, FLmotor);
        frontRight = ahwMap.get(DcMotorEx.class, FRmotor);
        backLeft = ahwMap.get(DcMotorEx.class, BLmotor);
        backRight = ahwMap.get(DcMotorEx.class, BRmotor);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        driveSubsystem = new MecanumDrive();
        driveSubsystem.init(ahwMap);

        // Keep this optional for future autonomy; no shooter logic is required here.
        pinpointSubsystem = new PinpointSubsystem(ahwMap);
    }
}