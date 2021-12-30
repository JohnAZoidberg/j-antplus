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
     * @return Fractional battery voltage. Not sure what this is, perhaps just a better granularity (1/256V) than percentage? At the moment just return this byte toInt
     */
    public int getFractionalBatteryVoltage() {
        return ByteUtils.toInt(getPageSpecificBytes()[1]);
    }

    public BatteryStatus getBatteryVoltageDescription() {
        switch (ByteUtils.toInt(getPageSpecificBytes()[2])) {
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
            default:
                return BatteryStatus.CRITICAL;
        }
    }


}
