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

import com.qualcomm.hardware.lynx.LynxCommExceptionHandler;
import com.qualcomm.hardware.lynx.LynxController;
import com.qualcomm.hardware.lynx.LynxModule;

import java.lang.reflect.Field;

/**
 * Wraps a LynxController to provide access to the underlying LynxModule.
 */
class OpenLynxController extends LynxCommExceptionHandler
{
    private ExpansionHubEx enhancedLynxModule;
    private LynxModule lynxModule;

    OpenLynxController(LynxController controller)
    {
        /*
         * NOTE: Historically we invoked the getModule() method
         * rather than grabbing the field directly. However, this
         * was reported to be causing problems in the wild because
         * getModule() actually returns a "PretendLynxModule" if
         * its internal "isHooked" boolean is false (as can happen
         * during a disconnect). This caused the cast to LynxModule
         * to fail and crashed the user code with an RE2Exception.
         *
         * See https://github.com/OpenFTC/RevExtensions2/issues/6
         */

        // We can get the underlying LynxModule object through a
        // LynxController object, but only through reflection.
        Field moduleField;

        try
        {
            // The "module" field is located within the LynxController class
            moduleField = LynxController.class.getDeclaredField("module");

            // Ensures the field is accessible for the next line. We still catch
            // the (impossible) IllegalAccessException just to be safe.
            moduleField.setAccessible(true);

            // Actually get the value from the controller that was passed in.
            lynxModule = (LynxModule) moduleField.get(controller);
            enhancedLynxModule = new ExpansionHubEx(lynxModule);
        }
        catch (Exception e)
        {
            throw new RE2Exception("Failed to reflect on LynxController!");
        }
    }

    LynxModule getLynxModule()
    {
        return lynxModule;
    }

    ExpansionHubEx getEnhancedLynxModule()
    {
        return enhancedLynxModule;
    }
}