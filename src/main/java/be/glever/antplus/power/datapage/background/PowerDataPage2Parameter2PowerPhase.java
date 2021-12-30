package be.glever.antplus.power.datapage.background;

import be.glever.antplus.power.datapage.background.PowerDataPage2GetSetParameter;
import be.glever.util.logging.Log;

public class PowerDataPage2Parameter2PowerPhase
extends PowerDataPage2GetSetParameter {
    public static final byte SUBPAGE_NR = 2;
    private static final Log LOG = Log.getLogger(PowerDataPage2Parameter2PowerPhase.class);

    public PowerDataPage2Parameter2PowerPhase(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    byte getSubPageNumber() {
        return 2;
    }

    byte[] createRequestSubPageData(int peakTorqueThreshold) throws IllegalAccessException {
        if (peakTorqueThreshold < 0 || peakTorqueThreshold > 200) {
            throw new IllegalAccessException("peakTorqueThreshold must be between 0 and 200");
        }
        byte[] subPageBytes = new byte[]{(byte)peakTorqueThreshold, -1, -1, -1, -1, -1};
        return subPageBytes;
    }

    int getAutoCrankLengthCapability() {
        return this.getSubPageSpecificBytes()[0];
    }
}
