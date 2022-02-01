# j-antplus
Java Library for speaking with ANT / ANT+ devices through a USB dongle.

I set up this project trying to talk to my personal ANT+ devices.
Goal is to implement needed ant messages and ANT+ datapages in order to talk to following set of devices:

* Heart rate monitor:
	* Basic support. Tested with Garmin HRM3SS. Check out `HrmTest_Main` under `src/test`
	* No support for legacy format (yet, because I don't own a legacy device). If you need it, check out HrmChannel to sniff the datapage 1st bit alternating every 4 packages.
* Speed sensor
	* Basic support. Tested with Garmin Speed Sensor.
	* The sensor only measures the number of wheel revolutions, the actual speed has to be calculated by a consumer of this library in conjunction with the wheel's diameter.
		Check out `SpeedTest_Main` under `src/test`
* Cadence sensor
	* Basic support. Tested with Garmin Cadence Sensor.
	* The sensor only measures the number of crank revolutions, the actual cadence has to be calculated by a consumer of this library from two different measurements.
		Check out `CadenceTest_Main` under `src/test`
* Combined speed and cadence sensor
	* Basic support. Not tested with a physical device yet. Check out `SpeedAndCadenceTest_Main` under `src/test`
* Power meter
	* Basic support. Tested with Garmin Vector 3. Check out `PowerTest_Main` under `src/test`
	* Not all data pages implemented, yet.
* Ant FE-C trainer
	* Basic support. Tested with Elite Suito and Tacx. Check out `FecTest_Main` under `src/test`
	* Not all data pages implemented, yet.
	* TODO: Send command to adjust resistance


# Current status: 
Codebase still unstable but foundation is shaping up. Expect a few heavy refactors until things stabilize.
Check out the Hrm/SpeedTest/Cadence/Fec examples to get started. Many thanks to https://github.com/JohnAZoidberg for help with implementing most of the devices.

Then you can add a maven dependency to `be.glever:j-antplus:0.0.1-SNAPSHOT` in order to use it.

# Compatibility notes
## Windows
This project uses usb4java-javax, which underneath uses libusb.
There seems to be a mismatch between the default windows10 WHQL driver (libusb 1.2.40.201, provided by dynastream) and libusb.
I resolved this by using the "Zadig" tool, select the ant-m usb stick (you may need to go to "Options" -> "list all devices")
and *downgrade* the driver to 1.2.6.0 (which is actually the latest libusb driver I could find on the net).

## Linux
Tested on Ubuntu: you need to give access to the usb interface for current user.
I followed this approach (which may give too wide permissions to your taste): https://askubuntu.com/a/930338  
```
echo 'SUBSYSTEM=="usb", MODE="0660", GROUP="plugdev"' > /etc/udev/rules.d/00-usb-permissions.rules
udevadm control --reload-rules
```

update: on manjaro, above didn't work (permission denied on the file), but this works
```
echo 'ATTRS{idVendor}=="fcf", ATTRS{idProduct}=="1009", MODE="0666"' > /etc/udev/rules.d/99-my-user-permissions.rules
udevadm control --reload-rules
```
After this, unplug and re-plug the dongle.

## Building

Currently Maven and Bazel build files are there but active work is done only on
the Bazel build system. Therefore that is preferred.

Install:

- Bazel 4.2
- OpenJDK11

If you have Nix installed, you can get a shell with the dependencies like this:

```
nix-shell -p gnumake openjdk bazel_4
```

Build:

```sh
# Commandline
bazel build bazel build //:j_antplus
ls -l bazel-bin/libj_antplus.jar
```

#### Building JavaDoc

```sh
bazel build //:j_antplus_-docs

# The result is here:
ls -l bazel-bin/j_antplus_docs.jar
```

You can extract the JavaDoc JAR and open the index.html.

### Running tests

Automated unit tests with JUnit:

```sh
bazel test //:tests
```

### Example Programs

Manual "tests" with actual hardare, or rather example program.

```sh
# Depending on your available sensor hardware, choose one of:
bazel run //:CadenceTest
bazel run //:FecTest
bazel run //:HrmTest
bazel run //:PowerTest
bazel run //:SpeedAndCadenceTest
bazel run //:SpeedCadenceTest
bazel run //:SpeedTest
```

To see what capabilities your USB ANT dongle has, you can run the capability test:

```sh
bazel run //:CapabilityTest
```

My "USB ANT+ Stick" from Magene, reported as  `ID 0fcf:1008 Dynastream
Innovations, Inc. ANTUSB2 Stick` by `lsusb` supports these capabilities:

```
Transceiver supports ANT version: AP2USB1.05
Transceiver has serial number:    REDACTED
Standard capabilities:
  Total number of ANT Channels:   8
  Receive Channels:               true
  Transmit Channels:              true
  Receive Messages:               true
  Transmit Messages:              true
  Ackd messages:                  true
  Burst messages:                 true
Advanced capabilities:
  Network Enabled:                true
  Serial Number Enabled:          true
  Per Channel TX Power Enabled:   true
  Low-Priority Serarch Enabled:   true
  Scipt Enabled:                  false
  Search List Enabled:            true
Advanced capabilities2:
  LED Enabled:                    false
  Ext Message Enabled:            true
  Scan Mode Enabled:              true
  Proximity Search Enabled:       true
  Extended Assign Enabled:        true
  FS ANTFS Enabled:               false
  FIT 1 Enabled:                  false
Total SensRcore channels:         0
Advanced capabilities3:
  Advanced Burst Enabled:         false
  Event Buffering Enabled:        false
  Event Filtering Enabled:        false
  High Duty Search Enabled:       false
  Search Sharing Enabled:         false
  Selective Data Updates Enabled: false
  Encrypted Channel Enabled:      false
Advanced capabilities4:
  RF Active Notification Enabled: false
```
