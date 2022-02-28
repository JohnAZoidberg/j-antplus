package be.glever.antplus.power.datapage.background;

import be.glever.antplus.power.datapage.AbstractPowerDataPage;
import be.glever.util.logging.Log;

public class PowerDataPage14TorqueBarycenter extends AbstractPowerDataPage {
    public static final byte PAGE_NR = 0x20;
    private static final Log LOG = Log.getLogger(PowerDataPage14TorqueBarycenter.class);

    public PowerDataPage14TorqueBarycenter(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PowerDataPage14TorqueBarycenter.PAGE_NR;
    }

    // TODO: Add explanation for the numbers
    public double getTorqueBarycenterAngle() {
        return (double)(this.getPageSpecificBytes()[0] & 0xFF) * 0.5 + 30.0;
    }
}
