package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.datapage.background.PowerDataPage2GetSetParameter;
import be.glever.util.logging.Log;

public class PowerDataPage2ParameterFeAdvancedCapabilities2
extends PowerDataPage2GetSetParameter {
    public static final byte SUBPAGE_NR = -2;
    private static final Log LOG = Log.getLogger(PowerDataPage2ParameterFeAdvancedCapabilities2.class);

    public PowerDataPage2ParameterFeAdvancedCapabilities2(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getSubPageNumber() {
        return -2;
    }

    private byte getInteroperableCapabilitiesMask() {
        return this.getSubPageSpecificBytes()[2];
    }

    public boolean supports4Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 0);
    }

    public boolean supports8Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 1);
    }

    public boolean supportsPowerPhase8Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 3);
    }

    public boolean supportsPco8Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 4);
    }

    public boolean supportsRiderPosition8Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 5);
    }

    public boolean supportsTorqueBarycenter8Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 6);
    }

    private byte getInteroperableCapabilities() {
        return this.getSubPageSpecificBytes()[4];
    }

    public boolean isTransmittingAt4Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 0);
    }

    public boolean isTransmittingAt8Hz() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 1);
    }

    public boolean powerPhase8HzEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 3);
    }

    public boolean pco8HzEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 4);
    }

    public boolean riderPosition8HzEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 5);
    }

    public boolean torqueBarycenter8HzEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 6);
    }
}
