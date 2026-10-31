package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

/**
 * Optional odometry wrapper kept lightweight for later autonomous work.
 * For now, the drivetrain does not depend on it.
 */
public class PinpointSubsystem {

    private double targetHeading = 0.0;
    private boolean isHeadingLocked = false;

    public PinpointSubsystem(HardwareMap hardwareMap) {
        // Intentionally left as a lightweight stub until a Pinpoint device is needed.
    }

    public void update() {
        // No-op until odometry is configured.
    }

    public Pose2D getPose() {
        return null;
    }

    public double calculateHeadingCorrection(double currentHeading, double rotationInput) {
        if (Math.abs(rotationInput) < 0.05) {
            if (!isHeadingLocked) {
                targetHeading = currentHeading;
                isHeadingLocked = true;
            }

            double headingError = targetHeading - currentHeading;
            double kp = 0.02;
            return headingError * kp;
        }

        isHeadingLocked = false;
        return rotationInput;
    }
}