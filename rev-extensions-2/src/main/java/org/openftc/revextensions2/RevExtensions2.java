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

package org.openftc.revextensions2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;

/***
 * This class manages hotswapping of the hardwareMap
 */

public class RevExtensions2
{

    /*@OpModeRegistrar
    public static void init(OpModeManager manager)
    {
        ((OpModeManagerImpl)manager).registerListener(new OpModeNotifications());
    }*/

    public static void init()
    {
        Utils.hotswapHardwareMap();
        Utils.getOpModeManager().registerListener(new OpModeNotifications());
    }

    private static class OpModeNotifications implements OpModeManagerNotifier.Notifications
    {
        @Override
        public void onOpModePreInit(OpMode opMode)
        {
            /*
             * I'd LOVE to find a way to register this lister automatically so that we
             * can hotswap the hardware map here instead of making the user call init(),
             * but so far I've been unable to find a way to do that.
             */

            //Utils.hotswapHardwareMap();
        }

        @Override
        public void onOpModePreStart(OpMode opMode)
        {

        }

        @Override
        public void onOpModePostStop(OpMode opMode)
        {
            Utils.deswapHardwareMap();
            Utils.getOpModeManager().unregisterListener(this);
        }
    }
}
