package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Intake subsystem controlled via a Spark Mini (PWM / Continuous Rotation Servo).
 */
public class IntakeModule {

    private Servo intakeServo;

    // Power constants for intake states
    private static final double INTAKE_IN_POWER = 1.0;   // Pull game pieces in
    private static final double INTAKE_OUT_POWER = -1.0; // Spit game pieces out
    private static final double INTAKE_STOP_POWER = 0.5; // Neutral / Stop for continuous rotation servos

    public void init(HardwareMap hwMap) {
        // Note: Spark Minis connected to PWM ports are mapped as Servos in FTC
        intakeServo = hwMap.get(Servo.class, RobotHardware.INTAKE_SERVO);
    }

    /**
     * Spin the intake inward to grab elements.
     */
    public void intakeIn() {
        intakeServo.setPower(INTAKE_IN_POWER); // Note: Use setPosition if it's a standard servo, or setPower if configured as a CRServo
    }

    /**
     * Spit elements outward.
     */
    public void intakeOut() {
        intakeServo.setPower(INTAKE_OUT_POWER);
    }

    /**
     * Stop the intake.
     */
    public void stop() {
        intakeServo.setPower(INTAKE_STOP_POWER); // 0.5 is typically neutral for continuous rotation PWM devices
    }
}