package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.datapage.background.PowerDataPage2GetSetParameter;
import be.glever.util.logging.Log;

public class PowerDataPage2ParameterFdAdvancedCapabilities1
extends PowerDataPage2GetSetParameter {
    public static final byte SUBPAGE_NR = -3;
    private static final Log LOG = Log.getLogger(PowerDataPage2ParameterFdAdvancedCapabilities1.class);

    public PowerDataPage2ParameterFdAdvancedCapabilities1(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getSubPageNumber() {
        return -3;
    }

    public boolean isDefaultCrankLength() {
        return !ByteUtils.hasBitSet(this.getInteroperableProperties(), 0);
    }

    public boolean requiresCrankLength() {
        return !ByteUtils.hasBitSet(this.getInteroperableProperties(), 1);
    }

    private byte getInteroperableProperties() {
        return this.getSubPageSpecificBytes()[0];
    }

    public byte getCustomProperties() {
        return this.getSubPageSpecificBytes()[1];
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

    public boolean supportsAutoZero() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 4);
    }

    public boolean supportsAutoCrankLength() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 5);
    }

    public boolean supportsTeAndPs() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilitiesMask(), 6);
    }

    public byte getCustomCapabilitiesMask() {
        return this.getSubPageSpecificBytes()[3];
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

    public boolean isAutoZeroEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 4);
    }

    public boolean isAutoCrankLengthEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 5);
    }

    public boolean isTeAndPsEnabled() {
        return !ByteUtils.hasBitSet(this.getInteroperableCapabilities(), 6);
    }

    public byte getCustomCapabilities() {
        return this.getSubPageSpecificBytes()[5];
    }
}
