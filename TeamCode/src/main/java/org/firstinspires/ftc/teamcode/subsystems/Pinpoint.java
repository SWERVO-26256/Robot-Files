package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

/**
 * Subsystem wrapper for the goBILDA Pinpoint Odometry Computer.
 * Handles tracking initialization, updates, and impact auto-correction logic.
 */
public class PinpointSubsystem {

    public GoBildaPinpointDriver pinpoint;

    private double targetHeading = 0.0;
    private boolean isHeadingLocked = false;

    public PinpointSubsystem(HardwareMap hardwareMap) {
        // Initializes the Pinpoint device matching the configuration name in RobotHardware (PINPOINT_NAME = "pinpoint")
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, RobotHardware.PINPOINT_NAME);

        // --- PHYSICAL CONFIGURATION ---
        // Set X and Y pod offsets in millimeters relative to your robot's center of rotation.
        // Measure these precisely on your chassis and update the values below (currently 0.0 placeholders).
        pinpoint.setOffsets(0.0, 0.0);

        // Set your odometry pod type (Change to GoBildaOdometryPods.goBILDA_4_Bar_Pods, 
        // goBILDA_Swingarm_Pods, or use custom ticks per mm if using non-goBILDA pods like Studica)
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_Bar_Pods);

        // Set travel directions for the X (forward) and Y (strafe) pods
        pinpoint.setDirection(
            GoBildaPinpointDriver.LocalizationEncoderDirection.FORWARD,
            GoBildaPinpointDriver.LocalizationEncoderDirection.FORWARD
        );

        // Reset position and calibrate internal IMU heading
        pinpoint.resetPosAndIMU();
    }

    /**
     * Must be called once per control loop cycle to pull latest tracking data from the coprocessor.
     */
    public void update() {
        pinpoint.update();
    }

    /**
     * Returns the current absolute field pose (X, Y, Heading).
     */
    public Pose2D getPose() {
        return pinpoint.getPosition();
    }

    /**
     * Automatically counters defensive hits or external shoves when the driver isn't turning.
     * 
     * @param currentHeading The robot's current heading in degrees.
     * @param rotationInput  The raw right stick rotation input from Gamepad 1.
     * @return The modified rotation power with auto-correction applied if hit.
     */
    public double calculateHeadingCorrection(double currentHeading, double rotationInput) {
        // If the driver is not actively pushing the rotation stick
        if (Math.abs(rotationInput) < 0.05) {
            if (!isHeadingLocked) {
                // Lock onto the exact target heading the moment the driver stops turning
                targetHeading = currentHeading;
                isHeadingLocked = true;
            }
            
            // Proportional (P) controller loop to fight external shoves and hold heading steady
            double headingError = targetHeading - currentHeading;
            double kp = 0.02; // Tune this multiplier if correction is too weak or oscillates
            
            return headingError * kp;
        } else {
            // Driver is actively steering; disable lock and pass input straight through
            isHeadingLocked = false;
            return rotationInput;
        }
    }
}