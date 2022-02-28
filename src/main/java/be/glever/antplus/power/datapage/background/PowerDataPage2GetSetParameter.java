package be.glever.antplus.power.datapage.background;

import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.power.datapage.background.PowerDataPage2Parameter1Crank;
import be.glever.antplus.power.datapage.background.PowerDataPage2Parameter2PowerPhase;
import be.glever.antplus.power.datapage.background.PowerDataPage2Parameter4RiderPosition;
import be.glever.antplus.power.datapage.background.PowerDataPage2ParameterFdAdvancedCapabilities1;
import be.glever.antplus.power.datapage.background.PowerDataPage2ParameterFeAdvancedCapabilities2;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class PowerDataPage2GetSetParameter extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 0x02;
    private static Map<Byte, SubPageBuilder> registry = new HashMap<Byte, SubPageBuilder>();

    public PowerDataPage2GetSetParameter(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return 0x02;
    }

    protected byte[] getSubPageSpecificBytes() {
        return Arrays.copyOfRange(this.getDataPageBytes(), 2, 8);
    }

    abstract byte getSubPageNumber();

    public static PowerDataPage2GetSetParameter newSubPage(byte[] bytes) {
        if (registry.isEmpty()) {
            registry.put(PowerDataPage2Parameter1Crank.SUBPAGE_NR, PowerDataPage2Parameter1Crank::new);
            registry.put(PowerDataPage2Parameter2PowerPhase.SUBPAGE_NR, PowerDataPage2Parameter2PowerPhase::new);
            registry.put(PowerDataPage2Parameter4RiderPosition.SUBPAGE_NR, PowerDataPage2Parameter4RiderPosition::new);
            registry.put(PowerDataPage2ParameterFdAdvancedCapabilities1.SUBPAGE_NR, PowerDataPage2ParameterFdAdvancedCapabilities1::new);
            registry.put(PowerDataPage2ParameterFeAdvancedCapabilities2.SUBPAGE_NR, PowerDataPage2ParameterFeAdvancedCapabilities2::new);
        }
        return registry.get(bytes[1]).construct(bytes);
    }

    public static interface SubPageBuilder {
        public PowerDataPage2GetSetParameter construct(byte[] var1);
    }
}
