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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevExtensions2;

@TeleOp(group = "RevExtensions2Examples")
public class HwSwInfoExample extends LinearOpMode
{
    ExpansionHubEx expansionHub;

    @Override
    public void runOpMode() throws InterruptedException
    {
        /*
         * Call this ONCE as the first thing in each of your OpModes
         */
        RevExtensions2.init();

        /*
         * Now that RevExtensions2.init() has been called, there are new objects in the
         * hardwareMap :)
         */
        expansionHub = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 2");

        /*
         * ------------------------------------------------------------------------------------------------
         * HW/SW info
         * ------------------------------------------------------------------------------------------------
         */

        String header =
                        "**********************************\n" +
                        "HW/SW INFO EXAMPLE                \n" +
                        "**********************************\n";
        telemetry.addLine(header);

        telemetry.addData("Firmware", expansionHub.getFirmwareVersion());
        telemetry.addData("Hardware revision", expansionHub.getHardwareRevision());
        telemetry.update();

        waitForStart();
        while (opModeIsActive());
    }
}
