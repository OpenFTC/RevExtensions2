package org.openftc.revextensions2.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.revextensions2.ExpansionHubServo;
import org.openftc.revextensions2.RevExtensions2;

@TeleOp(group = "RevExtensions2Examples")
public class ServoPulseWidthExample extends OpMode
{
    ExpansionHubServo servo;

    @Override
    public void init()
    {
        /*
         * Call this ONCE as the first thing in each of your OpModes
         */
        RevExtensions2.init();

        /*
         * Now that RevExtensions2.init() has been called, there are new objects in the
         * hardwareMap :)
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
