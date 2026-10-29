package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.RobotHardware;

@TeleOp(name = "TeleOpMain", group = "TeleOp")
public class TeleOpMain extends LinearOpMode {

    // Instantiate your centralized hardware map class
    RobotHardware robot = new RobotHardware();

    // Toggle state tracking flags
    private boolean pollenToggled = false;
    private boolean nectarToggled = false;
    private boolean intakeToggled = false;
    
    // Debounce flags to prevent button spam/toggling too fast per press
    private boolean aButtonPrev = false;
    private boolean bButtonPrev = false;
    private boolean xButtonPrev = false; // Used for intake toggle or assign your preferred key

    @Override
    public void runOpMode() {
        // Initialize all subsystems and motors via RobotHardware
        robot.init(hardwareMap);

        telemetry.addData("Status", "SWERVO TeleOp Initialized & Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            
            // =========================================================
            // 1. DRIVETRAIN & PINPOINT HIT AUTO-CORRECTION CONTROL
            // =========================================================
            // Left stick controls field-centric translation (axial & lateral)
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            
            // Right stick controls rotation (yaw)
            double yaw     =  gamepad1.right_stick_x;

            // Bumpers manage the hit auto-correction feature state
            boolean correctionOn  = gamepad1.left_bumper;
            boolean correctionOff = gamepad1.right_bumper;

            // Execute field-centric driving with Pinpoint correction mapping
            robot.driveSubsystem.driveFieldCentric(
                axial, lateral, yaw, 
                correctionOn, correctionOff, 
                robot.pinpointSubsystem
            );


            // =========================================================
            // 2. FLYWHEEL CONTROLS (SEPARATE FROM INTAKE)
            // =========================================================
            // Pollen Wheel Toggle (X Button)
            boolean xButtonCurr = gamepad1.x;
            if (xButtonCurr && !xButtonPrev) {
                pollenToggled = !pollenToggled;
            }
            xButtonPrev = xButtonCurr;

            if (pollenToggled) {
                robot.pollenMotor.setPower(0.8); // Adjust target power as needed
            } else {
                robot.pollenMotor.setPower(0.0);
            }

            // Nectar Wheel Toggle (B Button)
            boolean bButtonCurr = gamepad1.b;
            if (bButtonCurr && !bButtonPrev) {
                nectarToggled = !nectarToggled;
            }
            bButtonPrev = bButtonCurr;

            if (nectarToggled) {
                robot.nectarMotor.setPower(0.8); // Adjust target power as needed
            } else {
                robot.nectarMotor.setPower(0.0);
            }


            // =========================================================
            // 3. INTAKE C-SERVO HOLD TO RUN (A BUTTON)
            // =========================================================
            // Separate section: A directly controls the intake servo while held.
            boolean aButtonCurr = gamepad1.a;
            intakeToggled = aButtonCurr;

            if (aButtonCurr) {
                robot.intakeServo.setPower(1.0);
            } else {
                robot.intakeServo.setPower(0.0);
            }


            // =========================================================
            // 4. AUTO-AIM & APRILTAG SEARCH WITH RUMBLE FEEDBACK
            // =========================================================
            // Left Trigger for general auto-aim mechanism execution
            if (gamepad1.left_trigger > 0.3) {
                // TODO: Insert your Vision / Auto-Aim tracking logic here
            }

            // Right Trigger pressed to search for AprilTags
            if (gamepad1.right_trigger > 0.3) {
                boolean aprilTagFound = false; 
                
                // TODO: Replace with your actual AprilTag pipeline detection check
                // e.g., aprilTagFound = robot.visionPortal.getDetections().size() > 0;
                
                if (aprilTagFound) {
                    // Rumble Driver 1 gamepad for 300 milliseconds to confirm target lock
                    gamepad1.rumble(1.0, 1.0, 300);
                    
                    // TODO: Insert snap-to-target alignment code here if desired
                }
            }


            // =========================================================
            // 5. TELEMETRY MONITORING
            // =========================================================
            Pose2D currentPose = robot.pinpointSubsystem.getPose();
            telemetry.addData("Correction Status", robot.driveSubsystem.isAutoCorrectionEnabled() ? "ACTIVE" : "OFF");
            telemetry.addData("X Position (in)", currentPose.getX(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH));
            telemetry.addData("Y Position (in)", currentPose.getY(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH));
            telemetry.addData("Heading (deg)", currentPose.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Pollen Wheel", pollenToggled ? "ON" : "OFF");
            telemetry.addData("Nectar Wheel", nectarToggled ? "ON" : "OFF");
            telemetry.addData("Intake Status", intakeToggled ? "ACTIVE" : "OFF");
            telemetry.update();
        }
    }
}