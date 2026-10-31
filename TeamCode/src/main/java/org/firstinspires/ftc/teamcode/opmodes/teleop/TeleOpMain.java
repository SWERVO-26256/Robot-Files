package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.RobotHardware;

@TeleOp(name = "TeleOpMain", group = "TeleOp")
public class TeleOpMain extends LinearOpMode {

    private final RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addLine("Mecanum drivetrain ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            robot.driveSubsystem.drive(axial, lateral, yaw);

            telemetry.addData("Axial", axial);
            telemetry.addData("Lateral", lateral);
            telemetry.addData("Yaw", yaw);
            telemetry.update();
        }
    }
}