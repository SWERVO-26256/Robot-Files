@TeleOp(name="Swervo TeleOp Main", group="TeleOp")
public class SwervoTeleOpMain extends LinearOpMode {

    private final MecaniumDrive drive = new MecaniumDrive();

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