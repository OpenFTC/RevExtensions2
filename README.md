# RevExtensions2

**NOTE: SDK v4.x/5.x is required to use this**

**NOTE: an OpenRC-based SDK is NOT required to use this**

**NOTE: scroll down for installation instructions**

### DISCLAIMER:

Use this at your own risk. **You accept the possibility that using these extra features may cause the SDK to implode and cause you lose F-3 at worlds.**
That being said, I used a few of these features in competition this past season without any issues.

## What extra functionality does this add to the SDK?

 - Setting the LED color on the Hub
 - Setting the speed of the I2C buses on the Hub
 - Querying for total module current draw
 - Querying for servo bus current draw [ **broken:** REV firmware bug ]
 - Querying for GPIO bus current draw
 - Querying for I2C bus current draw
 - Querying for individual motor channel current draw
 - Reading the 5v monitor
 - Reading the 12v monitor
 - Queying for the internal temperature inside the Hub [ **broken:** unknown units ]
 - Query whether the Expansion Hub is in an over-temp condition
 - Query whether each individual motor H-bridge is over-temp
 - Query whether phone charging is enabled [ **broken:** REV firmware bug ]
 - Enable or disable phone charging
 - Setting servo pulse width directly in microseconds
 - Reading certain data in bulk from the Hub (can increase your control loop speed)
     
     The following data can be read in one go:

     - All 4 encoder counts
     - All 4 encoder velocities
     - All digital pins
     - All all analog pins
     - Is motor at target position for all 4 motors

 - Query whether a motor has "lost counts" [ **broken:** no idea what this actually does ]
 - Query for which firmware version the Hub has installed
 - Query for which hardware revision the Hub is
 - Convenient `VexMC29` class to prevent the need to manually scale the servo range for the Vex MC29.

## How do I install this?

1. Open your FTC SDK Android Studio project
2. Open the `build.common.gradle` file:

    ![img-her](doc/images/build-common-gradle.png)

3. Add `jcenter()` to the `repositories` block at the bottom:

    ![img-her](doc/images/jcenter.png)

4. Open the `build.gradle` file for the TeamCode module:

    ![img-her](doc/images/teamcode-gradle.png)

5. At the bottom, add this:

        dependencies {
            //NOTE: this is the correct version, the below screenshot is outdated
            implementation 'org.openftc:rev-extensions-2:1.2'
         }

    When you've done that, the bottom of the file should look like this:

    ![img-her](doc/images/gradledepend.png)

6. Now perform a Gradle Sync:

    ![img-her](doc/images/gradle-sync.png)

7. Wait for Gradle to finish gradling

8. Congratulations! You are now ready to use the new features provided by REV Extensions 2!


## Ok now that I've installed it, how do I use this?

Please see the [examples](examples/src/main/java/org/openftc/revextensions2/examples) folder. More detailed documentation coming soon.

## Changelog

### v1.2

 - Fix compatibility with SDK v5.x (should still be backwards compatible with v4.x, as the difference was not a compile-time issue)
 - **Bugfix:** (Issue #6) Prevent possibility of exception when doing bulk reads during disconnect event
 - Add `VexMC29` class, which scales the normal -1 to 1 CRServo PWM range to the range expected by the MC29.
 - **API change:** The hardware map is now hotswapped transparently - you no longer need to call `RevExtensions2.init()`
 - **API change:** Require units as a parameter to get voltage/current/temperature methods

### v1.1

 - Fix compatibility with SDK v4.x (due to API breaking changes, older versions of the SDK are no longer supported)
 - Fix `RevBulkData.getMotorCurrentPosition()` and `RevBulkData.getMotorVelocity()` to return the value signed to be consistent with `DcMotor.getMotorCurrentPosition()` and `DcMotorEx.getVelocity()`
 - Correct typo in `CurrentMonitorsExample.java`
 - Don't ignore reflection exceptions
 - Add JavaDoc comments to `RevBulkData.java`
 - Refactor hotswapping slightly

### v1.0

 - Initial release
