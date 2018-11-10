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

import com.qualcomm.hardware.lynx.LynxDcMotorController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class does Blackmagic
 */

class Utils
{
    static OpModeManagerImpl getOpModeManager()
    {
        return OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getRootActivity());
    }

    static HardwareMap getHardwareMap()
    {
        return getOpModeManager().getHardwareMap();
    }

    static void hotswapHardwareMap()
    {
        HardwareMap map = getHardwareMap();

        //-----------------------------------------------------------------------------------
        // Motors
        //-----------------------------------------------------------------------------------
        {   //An extra set of brackets so that it's impossible to screw things up for other swaps with a copy & paste error

            ArrayList<Map.Entry<String, DcMotor>> motorsToRecreateAsLynx = new ArrayList<>();

            // TODO: Process both map.dcMotor and the root map.
            for(Map.Entry<String, DcMotor> entry : map.dcMotor.entrySet())
            {
                if(!(entry.getValue() instanceof ExpansionHubMotor)
                        && entry.getValue() instanceof DcMotorEx
                        && entry.getValue().getController() instanceof LynxDcMotorController)
                {
                    motorsToRecreateAsLynx.add(entry);
                }
            }

            if(!motorsToRecreateAsLynx.isEmpty())
            {
                for(Map.Entry<String, DcMotor> entry : motorsToRecreateAsLynx)
                {
                    map.dcMotor.remove(entry.getKey());
                }

                for(Map.Entry<String, DcMotor> entry : motorsToRecreateAsLynx)
                {
                    map.dcMotor.put(entry.getKey(), new ExpansionHubMotor(entry.getValue()));
                }
            }
        }

        //-----------------------------------------------------------------------------------
        // Servos
        //-----------------------------------------------------------------------------------
        {   //An extra set of brackets so that it's impossible to screw things up for other swaps with a copy & paste error

            ArrayList<Map.Entry<String, Servo>> servosToRecreateAsLynx = new ArrayList<>();

            // TODO: Process both map.servo and the root map.
            for(Map.Entry<String, Servo> entry : map.servo.entrySet())
            {
                if(!(entry.getValue() instanceof ExpansionHubServo)
                        && entry.getValue() instanceof ServoImplEx
                        && entry.getValue().getController() instanceof LynxServoController)
                {
                    servosToRecreateAsLynx.add(entry);
                }
            }

            if(!servosToRecreateAsLynx.isEmpty())
            {
                for(Map.Entry<String, Servo> entry : servosToRecreateAsLynx)
                {
                    map.servo.remove(entry.getKey());
                }

                for(Map.Entry<String, Servo> entry : servosToRecreateAsLynx)
                {
                    map.servo.put(entry.getKey(), new ExpansionHubServo(entry.getValue()));
                }
            }
        }

        //-----------------------------------------------------------------------------------
        // LynxModules
        //-----------------------------------------------------------------------------------
        {   //An extra set of brackets so that it's impossible to screw things up for other swaps with a copy & paste error

            HashMap<String, ExpansionHubEx> enhancedLynxModulesToInject = new HashMap<>();

            for(LynxModule module : map.getAll(LynxModule.class))
            {
                if(!hwMapContainsEnhancedModule(module))
                {
                    enhancedLynxModulesToInject.put(getHwMapName(module), new ExpansionHubEx(module));
                }
            }

            for(Map.Entry<String, ExpansionHubEx> entry : enhancedLynxModulesToInject.entrySet())
            {
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }

    static void deswapHardwareMap()
    {
        HardwareMap map = getHardwareMap();

        //-----------------------------------------------------------------------------------
        // Motors
        //-----------------------------------------------------------------------------------
        {   //An extra set of brackets so that it's impossible to screw things up for other swaps with a copy & paste error

            ArrayList<Map.Entry<String, DcMotor>> lynxMotorsToRecreateAsStandard = new ArrayList<>();

            for(Map.Entry<String, DcMotor> entry : map.dcMotor.entrySet())
            {
                if(entry.getValue() instanceof ExpansionHubMotor)
                {
                    lynxMotorsToRecreateAsStandard.add(entry);
                }
            }

            if(!lynxMotorsToRecreateAsStandard.isEmpty())
            {
                for(Map.Entry<String, DcMotor> entry : lynxMotorsToRecreateAsStandard)
                {
                    map.dcMotor.remove(entry.getKey());
                }

                for(Map.Entry<String, DcMotor> entry : lynxMotorsToRecreateAsStandard)
                {
                    map.dcMotor.put(entry.getKey(),
                            new DcMotorImplEx(
                                    entry.getValue().getController(),
                                    entry.getValue().getPortNumber(),
                                    entry.getValue().getDirection(),
                                    entry.getValue().getMotorType()));
                }
            }
        }

        //-----------------------------------------------------------------------------------
        // Servos
        //-----------------------------------------------------------------------------------
        {   //An extra set of brackets so that it's impossible to screw things up for other swaps with a copy & paste error

            ArrayList<Map.Entry<String, Servo>> lynxServosToRecreateAsStandard = new ArrayList<>();

            for(Map.Entry<String, Servo> entry : map.servo.entrySet())
            {
                if(entry.getValue() instanceof ExpansionHubServo)
                {
                    lynxServosToRecreateAsStandard.add(entry);
                }
            }

            if(!lynxServosToRecreateAsStandard.isEmpty())
            {
                for(Map.Entry<String, Servo> entry : lynxServosToRecreateAsStandard)
                {
                    map.servo.remove(entry.getKey());
                }

                for(Map.Entry<String, Servo> entry : lynxServosToRecreateAsStandard)
                {
                    map.servo.put(entry.getKey(),
                            new ServoImplEx(
                                    (ServoControllerEx)entry.getValue().getController(),
                                    entry.getValue().getPortNumber(),
                                    entry.getValue().getDirection(),
                                    ServoConfigurationType.getStandardServoType()));
                }
            }
        }



        //-----------------------------------------------------------------------------------
        // LynxModules
        //-----------------------------------------------------------------------------------
        {   //An extra set of brackets so that it's impossible to screw things up for other swaps with a copy & paste error

            HashMap<String, ExpansionHubEx> enhancedLynxModulesToRemove = new HashMap<>();

            for(ExpansionHubEx module : map.getAll(ExpansionHubEx.class))
            {
                enhancedLynxModulesToRemove.put(getHwMapName(module), module);
            }

            if(!enhancedLynxModulesToRemove.isEmpty())
            {
                for(Map.Entry<String, ExpansionHubEx> entry : enhancedLynxModulesToRemove.entrySet())
                {
                    map.remove(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    static String getHwMapName(HardwareDevice device)
    {
        return getHardwareMap().getNamesOf(device).iterator().next();
    }

    private static boolean hwMapContainsEnhancedModule(LynxModule module)
    {
        for(ExpansionHubEx enhancedModule : getHardwareMap().getAll(ExpansionHubEx.class))
        {
            if(module.getModuleAddress() == enhancedModule.getStandardModule().getModuleAddress())
            {
                return true;
            }
        }

        return false;
    }
}
