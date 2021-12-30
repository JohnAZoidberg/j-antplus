package be.glever.antplus.common.datapage;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.BatteryStatus;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;

public class DataPage82BatteryStatus extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 82;

    public DataPage82BatteryStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public int getBatteryIdentifier() {
        return this.getDataPageBytes()[2] >> 3;
    }

    public int getNumberOfBatteries() {
        return this.getDataPageBytes()[2] & 7;
    }

    public int getCumulativeOperatingTime() {
        return ByteUtils.fromUnsignedBytes(super.dataPageSubArray(3, 6)) / this.getCumulativeOperatingTimeResolution();
    }

    public double getBatteryVoltage() {
        int coarseVoltage = this.getCoarseBatteryVoltage();
        int fractionalVoltage = this.getFractionalBatteryVoltage();
        if (coarseVoltage == 15 || fractionalVoltage == 255) {
            return -1.0;
        }
        return (double)coarseVoltage + (double)fractionalVoltage / 256.0;
    }

    public int getFractionalBatteryVoltage() {
        return ByteUtils.toInt(this.getDataPageBytes()[6]);
    }

    private byte getDescriptiveBitField() {
        return this.getDataPageBytes()[7];
    }

    public int getCoarseBatteryVoltage() {
        return ByteUtils.toInt(this.getDescriptiveBitField());
    }

    public BatteryStatus getBatteryStatus() {
        switch (this.getDescriptiveBitField() >> 4 & 7) {
            case 0:
            case 6:
                return BatteryStatus.RESERVED;
            case 1:
                return BatteryStatus.NEW;
            case 2:
                return BatteryStatus.GOOD;
            case 3:
                return BatteryStatus.OK;
            case 4:
                return BatteryStatus.LOW;
            case 5:
                return BatteryStatus.CRITICAL;
            default:
                return BatteryStatus.INVALID;
        }
    }

    private int getCumulativeOperatingTimeResolution() {
        return ByteUtils.hasBitSet(this.getDescriptiveBitField(), 7) ? 2 : 16;
    }
}
