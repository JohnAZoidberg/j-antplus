package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;
import be.glever.util.logging.Log;

public class PowerDataPageE1LeftForceAngle
extends AbstractPowerDataPage {
    public static final byte PAGE_NR = -31;
    private static final Log LOG = Log.getLogger(PowerDataPageE1LeftForceAngle.class);

    public PowerDataPageE1LeftForceAngle(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return -31;
    }

    public int getEventCount() {
        return this.getPageSpecificBytes()[0];
    }

    public int getStartAngle() {
        return this.getPageSpecificBytes()[1];
    }

    public int getEndAngle() {
        return this.getPageSpecificBytes()[2];
    }

    public int getStartPeakAngle() {
        return this.getPageSpecificBytes()[3];
    }

    public int getEndPeakAngle() {
        return this.getPageSpecificBytes()[4];
    }

    public double getTorque() {
        return (double)ByteUtils.fromUShort(this.getPageSpecificBytes()[5], this.getPageSpecificBytes()[6]) / 32.0;
    }
}
