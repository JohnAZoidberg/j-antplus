package be.glever.antplus.speedcadence.datapage.background;

import be.glever.antplus.BatteryStatus;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;
import be.glever.ant.util.ByteUtils;

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
     * Absolute battery voltage
     *
     * @return Voltage 0-14.99609375V, -1 means invalid. Precision: 1/256V
     */
    public double getBatteryVoltage() {
        int coarseVoltage = this.getCoarseBatteryVoltage();
        int fractionalVoltage = this.getFractionalBatteryVoltage();
        if (coarseVoltage == 15 && fractionalVoltage == 255) {
            return -1.0;
        }
        return coarseVoltage + fractionalVoltage / 256.0;
    }

    /**
     * Range: 0-255
     * Unit: 1/256 V
     * @return fractional battery voltage.
     */
    public int getFractionalBatteryVoltage() {
        return ByteUtils.toInt(getPageSpecificBytes()[2]);
    }

    /**
     * Battery voltage with 1V precision
     *
     * Range: 0-14
     * 15 means invalid (for example unable to measure)
     *
     * @return battery voltage
     */
    private int getCoarseBatteryVoltage() {
        // Consider first 4 bits
        return getPageSpecificBytes()[3] & 0xF;
    }

    public BatteryStatus getBatteryVoltageDescription() {
        // Consider only bits 4 through 6
        int b = (getPageSpecificBytes()[3] >> 4) & 0x7;
        switch (b) {
            case 0x00:
            case 0x06:
                return BatteryStatus.RESERVED;
            case 0x01:
                return BatteryStatus.NEW;
            case 0x02:
                return BatteryStatus.GOOD;
            case 0x03:
                return BatteryStatus.OK;
            case 0x04:
                return BatteryStatus.LOW;
            case 0x05:
                return BatteryStatus.CRITICAL;
            default:
                return BatteryStatus.INVALID;
        }
    }
}
