package be.glever.antplus.power.datapage.main;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractPowerDataPage}
 */
public class PowerDataPage11WheelTorque extends AbstractPowerDataPage {


    public static final byte PAGE_NR = 0x11;

    public PowerDataPage11WheelTorque(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    public int getUpdateEventCount() {
        return getPageSpecificBytes()[0];
    }

    /**
     * Rollover: 256
     */
    public int getWheelRevolutions() {
        return this.getPageSpecificBytes()[1] & 0xFF;
    }

    /**
     * Instantaneous wheel cadence. 255 if unavailable
     * Unit: RPM
     * Range: 0 - 254 RPM
     */
    public int getInstantaneousCadence() {
        return this.getPageSpecificBytes()[2] & 0xFF;
    }

    /**
     * Rollower: 32s
     * Unit: 1/2048s
     */
    public double getWheelPeriod() {
        byte[] pageBytes = this.getPageSpecificBytes();
        return (double)ByteUtils.fromUShort(pageBytes[3], pageBytes[4]) / 2048.0;
    }

    /**
     * Rollover: 2048Nm
     * Unit: 1/32Nm
     */
    public double getAccumulatedTorque() {
        byte[] pageBytes = this.getPageSpecificBytes();
        return (double)ByteUtils.fromUShort(pageBytes[5], pageBytes[6]) / 32.0;
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
