package be.glever.antplus.common.datapage;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.BatteryStatus;

public class DataPage82BatteryStatus extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 82;

    public DataPage82BatteryStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    /**
     * Range: 0-3
     *
     * @return The battery which the battery status info describes.
     */
    public int getBatteryIdentifier() {
        return this.getDataPageBytes()[2] >> 4;
    }

    /**
     * Range: 0-3
     * @return The number of batteries in the system.
     */
    public int getNumberOfBatteries() {
        // First 4 bits
        return this.getDataPageBytes()[2] & 7;
    }

    public int getCumulativeOperatingTime() {
        return ByteUtils.fromUnsignedBytes(super.dataPageSubArray(3, 6)) / this.getCumulativeOperatingTimeResolution();
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
        return ByteUtils.toInt(this.getDataPageBytes()[6]);
    }

    private byte getDescriptiveBitField() {
        return this.getDataPageBytes()[7];
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
        return ByteUtils.toInt(this.getDescriptiveBitField()) & 0xF;
    }

    public BatteryStatus getBatteryStatus() {
        // Consider only bits 4 through 6
        switch (this.getDescriptiveBitField() >> 4 & 0b111) {
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

    /**
     * Unit: seconds
     */
    private int getCumulativeOperatingTimeResolution() {
        return ByteUtils.hasBitSet(this.getDescriptiveBitField(), 7) ? 2 : 16;
    }
}
