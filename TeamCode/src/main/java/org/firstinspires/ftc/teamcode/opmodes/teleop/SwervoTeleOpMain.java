package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

@TeleOp(name="Swervo TeleOp Main", group="TeleOp")
public class SwervoTeleOpMain extends LinearOpMode {

    private final MecanumDrive drive = new MecanumDrive();

    @Override
    public void runOpMode() {
        drive.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            // Note: FTC joysticks are inverted on the Y-axis, so we negate axial input (-gamepad1.left_stick_y)
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            drive.drive(axial, lateral, yaw);

            telemetry.addData("Status", "Running Drivetrain");
            telemetry.update();
        }
    }
}





// Drivetrain control logic (inputs on controller):
// Gamepad1:
// Left Stick X: Strafe Left/Right
// Left Stick Y: Forward/Backward
// Left Stick X