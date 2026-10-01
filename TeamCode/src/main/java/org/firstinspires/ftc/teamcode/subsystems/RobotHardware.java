package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

/**
 * Centralized Hardware Class for FTC Team 26256 (SWERVO)
 * Defines all hardware configuration names and holds device instances.
 */
public class RobotHardware {

    // ==========================================
    // REV CONFIGURATION NAME CONSTANTS
    // (Must match names configured on the Driver Station)
    // ==========================================
    
    // Drive Motors (Single Control Hub Setup)
    public static final String FLmotor  = "frontLeft";
    public static final String FRmotor = "frontRight";
    public static final String BLmotor   = "backLeft";
    public static final String BRmotor  = "backRight";

    // Subsystem Motors
    public static final String INTAKE_MOTOR_NAME      = "intakeMotor";
    public static final String FLYWHEEL_MOTOR_NAME    = "flywheelMotor";

    // Servos
    public static final String INDEX_GATE_SERVO_NAME  = "indexGate";
    public static final String HOOD_ANGLE_SERVO_NAME  = "hoodServo";

    // Vision / Cameras
    public static final String WEBCAM_NAME            = "Webcam 1";

    // ==========================================
    // HARDWARE DEVICE INSTANCES
    // ==========================================
    
    public DcMotorEx frontLeft  = null;
    public DcMotorEx frontRight = null;
    public DcMotorEx backLeft   = null;
    public DcMotorEx backRight  = null;

    public DcMotorEx intakeMotor   = null;
    public DcMotorEx flywheelMotor = null;

    public Servo indexGate  = null;
    public Servo hoodServo  = null;

    public WebcamName webcam = null;

    // Local HardwareMap reference
    private HardwareMap hwMap = null;

    /**
     * Initialize all hardware components using the provided Driver Station HardwareMap.
     * @param ahwMap The active HardwareMap from the running OpMode.
     */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        // Initialize Motors
        frontLeft  = hwMap.get(DcMotorEx.class, FLmotor);
        frontRight = hwMap.get(DcMotorEx.class, FRmotor);
        backLeft   = hwMap.get(DcMotorEx.class, BLmotor);
        backRight  = hwMap.get(DcMotorEx.class, BRmotor);

        intakeMotor   = hwMap.get(DcMotorEx.class, INTAKE_MOTOR_NAME);
        flywheelMotor = hwMap.get(DcMotorEx.class, FLYWHEEL_MOTOR_NAME);

        // Configure Default Motor Behaviors
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Flywheel & Intake setup
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize Servos
        indexGate = hwMap.get(Servo.class, INDEX_GATE_SERVO_NAME);
        hoodServo = hwMap.get(Servo.class, HOOD_ANGLE_SERVO_NAME);

        // Initialize Vision Hardware
        webcam = hwMap.get(WebcamName.class, WEBCAM_NAME);
    }
}