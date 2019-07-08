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
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

@TeleOp(group = "RevExtensions2Examples")
public class BulkDataExample extends OpMode
{
    RevBulkData bulkData;
    AnalogInput a0, a1, a2, a3;
    DigitalChannel d0, d1, d2, d3, d4, d5, d6, d7;
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
        a0 = hardwareMap.analogInput.get("a0");
        a1 = hardwareMap.analogInput.get("a1");
        a2 = hardwareMap.analogInput.get("a2");
        a3 = hardwareMap.analogInput.get("a3");
        d0 = hardwareMap.digitalChannel.get("d0");
        d1 = hardwareMap.digitalChannel.get("d1");
        d2 = hardwareMap.digitalChannel.get("d2");
        d3 = hardwareMap.digitalChannel.get("d3");
        d4 = hardwareMap.digitalChannel.get("d4");
        d5 = hardwareMap.digitalChannel.get("d5");
        d6 = hardwareMap.digitalChannel.get("d6");
        d7 = hardwareMap.digitalChannel.get("d7");
    }

    @Override
    public void loop()
    {
        /*
         * ------------------------------------------------------------------------------------------------
         * Bulk data
         *
         * NOTE: While you could get all of this information by issuing many separate commands,
         * the amount of time required to fetch the information would increase drastically. By
         * reading all of this information in one command, we can loop at over 300Hz (!!!)
         * ------------------------------------------------------------------------------------------------
         */

        bulkData = expansionHub.getBulkInputData();

        String header =
                        "**********************************\n" +
                        "BULK DATA EXAMPLE                 \n" +
                        "**********************************\n";
        telemetry.addLine(header);

        /*
         * Encoders
         */
        telemetry.addData("M0 enocder", bulkData.getMotorCurrentPosition(motor0));
        telemetry.addData("M1 encoder", bulkData.getMotorCurrentPosition(motor1));
        telemetry.addData("M2 encoder", bulkData.getMotorCurrentPosition(motor2));
        telemetry.addData("M3 encoder", bulkData.getMotorCurrentPosition(motor3));

        /*
         * Encoder velocities
         */
        telemetry.addData("M0 velocity", bulkData.getMotorVelocity(motor0));
        telemetry.addData("M1 velocity", bulkData.getMotorVelocity(motor1));
        telemetry.addData("M2 velocity", bulkData.getMotorVelocity(motor2));
        telemetry.addData("M3 velocity", bulkData.getMotorVelocity(motor3));

        /*
         * Is motor at target position?
         */
        telemetry.addData("M0 at target pos", bulkData.isMotorAtTargetPosition(motor0));
        telemetry.addData("M1 at target pos", bulkData.isMotorAtTargetPosition(motor1));
        telemetry.addData("M2 at target pos", bulkData.isMotorAtTargetPosition(motor2));
        telemetry.addData("M3 at target pos", bulkData.isMotorAtTargetPosition(motor3));

        /*
         * Analog voltages
         */
        telemetry.addData("A0", bulkData.getAnalogInputValue(a0));
        telemetry.addData("A1", bulkData.getAnalogInputValue(a1));
        telemetry.addData("A2", bulkData.getAnalogInputValue(a2));
        telemetry.addData("A3", bulkData.getAnalogInputValue(a3));

        /*
         * Digital states
         */
        telemetry.addData("D0", bulkData.getDigitalInputState(d0));
        telemetry.addData("D1", bulkData.getDigitalInputState(d1));
        telemetry.addData("D2", bulkData.getDigitalInputState(d2));
        telemetry.addData("D3", bulkData.getDigitalInputState(d3));
        telemetry.addData("D4", bulkData.getDigitalInputState(d4));
        telemetry.addData("D5", bulkData.getDigitalInputState(d5));
        telemetry.addData("D6", bulkData.getDigitalInputState(d6));
        telemetry.addData("D7", bulkData.getDigitalInputState(d7));

        telemetry.update();
    }
}
