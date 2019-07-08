package org.openftc.revextensions2.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.revextensions2.ExpansionHubServo;

@TeleOp(group = "RevExtensions2Examples")
public class ServoPulseWidthExample extends OpMode
{
    ExpansionHubServo servo;

    @Override
    public void init()
    {
        /*
         * Before init() was called on this user code, REV Extensions 2
         * was notified via OpModeManagerNotifier.Notifications and
         * it automatically took care of initializing the new objects
         * in the hardwaremap for you. Historically, you would have
         * needed to call RevExtensions2.init()
         */
        servo = (ExpansionHubServo) hardwareMap.servo.get("servo");
    }

    @Override
    public void loop()
    {
        String header =
                        "**********************************\n" +
                        "SERVO PULSE WIDTH EXAMPLE         \n" +
                        "**********************************\n";
        telemetry.addLine(header);

        servo.setPwmEnable(); // <-- IMPORTANT: make sure the servo is enabled, otherwise setPulseWidthUs() will appear to have no effect
        servo.setPulseWidthUs(1000);

        telemetry.addLine("Setting servo pulse width directly...");
        telemetry.update();
    }
}
