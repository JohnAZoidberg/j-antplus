package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.datapage.background.PowerDataPage2GetSetParameter;
import be.glever.util.logging.Log;

public class PowerDataPage2Parameter4RiderPosition
extends PowerDataPage2GetSetParameter {
    public static final byte SUBPAGE_NR = 4;
    private static final Log LOG = Log.getLogger(PowerDataPage2Parameter4RiderPosition.class);

    public PowerDataPage2Parameter4RiderPosition(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    byte getSubPageNumber() {
        return 4;
    }

    byte[] createRequestSubPageData(int transitionTimeOffset) {
        byte[] subPageBytes = new byte[]{(byte)transitionTimeOffset, -1, -1, -1, -1, -1};
        return subPageBytes;
    }

    int getTransitionTimeOffset() {
        return ByteUtils.toInt(this.getSubPageSpecificBytes()[0]);
    }
}
