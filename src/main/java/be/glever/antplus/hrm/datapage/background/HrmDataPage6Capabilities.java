package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class HrmDataPage6Capabilities
extends AbstractHRMDataPage {
    public static final byte PAGE_NR = 6;

    public HrmDataPage6Capabilities(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public boolean isExtendedRunningFeatureSupported() {
        return isBitSet(0b1, getSupportedFeaturesByte());
    }

    public boolean isExtendedCyclingFeaturesSupported() {
        return isBitSet(0b01, getSupportedFeaturesByte());
    }

    public boolean isExtendedSwimmingFeaturesSupported() {
        return isBitSet(0b001, getSupportedFeaturesByte());
    }


    public boolean isExtendedRunningFeaturesEnabled() {
        return isBitSet(0b1, getEnabledFeaturesByte());
    }

    public boolean isExtendedCyclingFeaturesEnabled() {
        return isBitSet(0b01, getEnabledFeaturesByte());
    }

    public boolean isExtendedSwimmingFeaturesEnabled() {
        return isBitSet(0b001, getEnabledFeaturesByte());
    }

    private boolean isBitSet(int mask, byte b) {
        return 1 == (mask & b);
    }

    private byte getSupportedFeaturesByte() {
        return getPageSpecificBytes()[1];
    }

    private byte getEnabledFeaturesByte() {
        return getPageSpecificBytes()[2];
    }
}
