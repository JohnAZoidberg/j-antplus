package be.glever.antplus.common.datapage.registry;

import be.glever.antplus.fec.datapage.FecDataPageRegistry;
import be.glever.antplus.hrm.datapage.HrmDataPageRegistry;
import be.glever.antplus.power.datapage.PowerDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.SpeedAndCadenceDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.SpeedCadenceDataPageRegistry;

/**
 * Registry of all datapages
 * 
 * Necessary when scanning for devices. Because for example HRM transmits
 * device information, such as the device type and manufacturer not in a common
 * data page but a device specific one.
 */
public class ComprehensiveDataPageRegistry extends AbstractDataPageRegistry {
    public ComprehensiveDataPageRegistry() {
        super(new CommonDataPageRegistry(), new FecDataPageRegistry(), new HrmDataPageRegistry(), new PowerDataPageRegistry(), new SpeedAndCadenceDataPageRegistry(), new SpeedCadenceDataPageRegistry());
    }
}
