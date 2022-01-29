package be.glever.antplus.hrm.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.BatteryStatus;
import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class HrmDataPage7BatteryStatus extends AbstractHRMDataPage {
    public static final byte PAGE_NR = 7;

    public HrmDataPage7BatteryStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    /**
     * @return Battery level in percentage or -1 if not used.
     */
    public int getBatteryLevelPercentage() {
        byte batteryLevelByte = getPageSpecificBytes()[0];

        return batteryLevelByte == (byte) 0xFF ? -1 : ByteUtils.toInt(batteryLevelByte);
    }

    /**
     * Decimal place of the battery voltage as a fraction.
     *
     * Unit: 1/256V
     * Range: 0-255
     *
     * Must be added to getCoarseBatteryVoltage(), to get the absolute voltage.
     *
     * @return battery voltage
     */
    private int getFractionalBatteryVoltage() {
        return ByteUtils.toInt(getPageSpecificBytes()[1]);
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
        return getPageSpecificBytes()[2] & 0xF;
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

    public BatteryStatus getBatteryVoltageDescription() {
        // Consider only bits 4 through 6
        int b = (getPageSpecificBytes()[2] >> 4) & 0x7;
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
