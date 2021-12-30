package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.RiderPosition;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;
import be.glever.util.logging.Log;

public class PowerDataPageE2PedalPosition
extends AbstractPowerDataPage {
    public static final byte PAGE_NR = -30;
    private static final Log LOG = Log.getLogger(PowerDataPageE2PedalPosition.class);

    public PowerDataPageE2PedalPosition(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return -30;
    }

    public int getEventCount() {
        return this.getPageSpecificBytes()[0];
    }

    public RiderPosition getRiderPosition() {
        byte riderPosByte = this.getPageSpecificBytes()[1];
        if (ByteUtils.hasMask(riderPosByte, 0)) {
            return RiderPosition.SEATED_OR_UNKNOWN;
        }
        if (ByteUtils.hasMask(riderPosByte, 64)) {
            return RiderPosition.TRANSITION_TO_SEATED;
        }
        if (ByteUtils.hasMask(riderPosByte, 128)) {
            return RiderPosition.STANDING;
        }
        if (ByteUtils.hasMask(riderPosByte, 192)) {
            return RiderPosition.TRANSITION_TO_STANDING;
        }
        throw new IllegalStateException("Cannot be reached");
    }

    public int getCadence() {
        return this.getPageSpecificBytes()[2] & 0xFF;
    }

    public int getRightPco() {
        return this.getPageSpecificBytes()[3];
    }

    public int getLeftPco() {
        return this.getPageSpecificBytes()[4];
    }
}
