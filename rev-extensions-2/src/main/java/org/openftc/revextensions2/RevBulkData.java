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

import com.qualcomm.hardware.lynx.LynxAnalogInputController;
import com.qualcomm.hardware.lynx.LynxController;
import com.qualcomm.hardware.lynx.LynxDcMotorController;
import com.qualcomm.hardware.lynx.LynxDigitalChannelController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;

import java.lang.reflect.Field;

/**
 * This class is a wrapper over LynxGetBulkInputDataResponse that provides
 * easier-to-use methods to access the underlying data
 */

public class RevBulkData
{
    private LynxGetBulkInputDataResponse response;
    private LynxModule module;

    RevBulkData(LynxGetBulkInputDataResponse response, LynxModule module)
    {
        this.response = response;
        this.module = module;
    }

    public int getMotorCurrentPosition(int motorNum)
    {
        return response.getEncoder(motorNum);
    }

    public int getMotorCurrentPosition(DcMotor motor)
    {
        validateMotor(motor);
        return getMotorCurrentPosition(motor.getPortNumber());
    }

    public int getMotorVelocity(int motorNum)
    {
        return response.getVelocity(motorNum);
    }

    public int getMotorVelocity(DcMotor motor)
    {
        validateMotor(motor);
        return getMotorVelocity(motor.getPortNumber());
    }

    public boolean isMotorAtTargetPosition(int motorNum)
    {
        return response.isAtTarget(motorNum);
    }

    public boolean isMotorAtTargetPosition(DcMotor motor)
    {
        validateMotor(motor);
        return isMotorAtTargetPosition(motor.getPortNumber());
    }

    public int getAnalogInputValue(int pin)
    {
        return response.getAnalogInput(pin);
    }

    public int getAnalogInputValue(AnalogInput input)
    {
        AnalogInputController controller = null;
        int port = -1;

        try
        {
            Field field = AnalogInput.class.getDeclaredField("controller");
            field.setAccessible(true);
            controller = (AnalogInputController) field.get(input);

            Field field2 = AnalogInput.class.getDeclaredField("channel");
            field2.setAccessible(true);
            port = (int) field2.get(input);
        }
        catch (Exception ignored){}

        if(!(controller instanceof LynxAnalogInputController))
        {
            throw new RevBulkDataException(String.format("AnalogInput %s is not attached to a Lynx module!", Utils.getHwMapName(input)));
        }

        if(!validateLynxModule((LynxController) controller))
        {
            throw new RevBulkDataException(String.format("AnalogInput %s is attached to a different Lynx module than the one that this bulk command was issued to!", Utils.getHwMapName(input)));
        }

        return getAnalogInputValue(port);
    }

    public boolean getDigitalInputState(int pin)
    {
        return response.getDigitalInput(pin);
    }

    public boolean getDigitalInputState(DigitalChannel digitalChannel)
    {
        DigitalChannelController controller = null;
        int port = -1;

        try
        {
            Field field = DigitalChannelImpl.class.getDeclaredField("controller");
            field.setAccessible(true);
            controller = (DigitalChannelController) field.get(digitalChannel);

            Field field2 = DigitalChannelImpl.class.getDeclaredField("channel");
            field2.setAccessible(true);
            port = (int) field2.get(digitalChannel);
        }
        catch (Exception ignored){}

        if(!(controller instanceof LynxDigitalChannelController))
        {
            throw new RevBulkDataException(String.format("DigitalChannel %s is not attached to a Lynx module!", Utils.getHwMapName(digitalChannel)));
        }

        if(!validateLynxModule((LynxController) controller))
        {
            throw new RevBulkDataException(String.format("DigitalChannel %s is attached to a different Lynx module than the one that this bulk command was issued to!", Utils.getHwMapName(digitalChannel)));
        }

        return getDigitalInputState(port);
    }

    private void validateMotor(DcMotor motor)
    {
        if(!(motor.getController() instanceof LynxDcMotorController))
        {
            throw new RevBulkDataException(String.format("Motor %s is not attached to a Lynx module!", Utils.getHwMapName(motor)));
        }

        if(!validateLynxModule((LynxController) motor.getController()))
        {
            throw new RevBulkDataException(String.format("Motor %s is attached to a different Lynx module than the one that this bulk command was issued to!", Utils.getHwMapName(motor)));
        }
    }

    private boolean validateLynxModule(LynxController controller)
    {
        return validateLynxModule(new OpenLynxController(controller).getLynxModule());
    }

    private boolean validateLynxModule(LynxModule moduleToValidate)
    {
        return moduleToValidate.getModuleAddress() == module.getModuleAddress();
    }
}
