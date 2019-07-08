/*
 * Copyright (c) 2018 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.openftc.revextensions2.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;

@TeleOp(group = "RevExtensions2Examples")
public class CurrentMonitorsExample extends OpMode
{
    ExpansionHubMotor motor0, motor1, motor2, motor3;
    ExpansionHubEx expansionHub;

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
        expansionHub = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 2");
        motor0 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motor0");
        motor1 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motor1");
        motor2 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motor2");
        motor3 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motor3");
    }

    @Override
    public void loop()
    {
        /*
         * ------------------------------------------------------------------------------------------------
         * Current monitors - NOTE: units are milliamps
         * ------------------------------------------------------------------------------------------------
         */

        String header =
                        "**********************************\n" +
                        "CURRENT MONITORING EXAMPLE        \n" +
                        "NOTE: UNITS ARE AMPS              \n" +
                        "**********************************\n";
        telemetry.addLine(header);

        telemetry.addData("Total current", expansionHub.getTotalModuleCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
        telemetry.addData("I2C current", expansionHub.getI2cBusCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
        telemetry.addData("GPIO current", expansionHub.getGpioBusCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
        telemetry.addData("M0 current", motor0.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
        telemetry.addData("M1 current", motor1.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
        telemetry.addData("M2 current", motor2.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));
        telemetry.addData("M3 current", motor3.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.AMPS));

        telemetry.update();
    }
}
