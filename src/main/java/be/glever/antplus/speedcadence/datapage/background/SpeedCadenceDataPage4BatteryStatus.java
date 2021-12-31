package be.glever.antplus.speedcadence.datapage.background;

import be.glever.antplus.BatteryStatus;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;

public class SpeedCadenceDataPage4BatteryStatus extends AbstractSpeedCadenceDataPage {
    public static final byte PAGE_NR = 4;

    public SpeedCadenceDataPage4BatteryStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    /**
     * Range: 0-255
     * Unit: 1/256 V
     * @return fractional battery voltage.
     */
    public double getFractionalBatteryVoltage() {
        int batteryLevelByte = this.getPageSpecificBytes()[0] & 0xFF;
        if (batteryLevelByte == 255) {
            return -1.0;
        }
        return (double)batteryLevelByte / 256.0;
    }

    public BatteryStatus getBatteryVoltageDescription() {
        switch (this.getPageSpecificBytes()[2]) {
            case 0:
            case 6: {
                return BatteryStatus.RESERVED;
            }
            case 1: {
                return BatteryStatus.NEW;
            }
            case 2: {
                return BatteryStatus.GOOD;
            }
            case 3: {
                return BatteryStatus.OK;
            }
            case 4: {
                return BatteryStatus.LOW;
            }
            case 5: {
                return BatteryStatus.CRITICAL;
            }
            default:
                return BatteryStatus.INVALID;
        }
    }
}
