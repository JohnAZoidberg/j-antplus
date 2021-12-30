package be.glever.antplus.common.datapage.registry;

import be.glever.antplus.common.datapage.registry.AbstractDataPageRegistry;
import be.glever.antplus.common.datapage.registry.CommonDataPageRegistry;
import be.glever.antplus.fec.datapage.FecDataPageRegistry;
import be.glever.antplus.hrm.datapage.HrmDataPageRegistry;
import be.glever.antplus.power.datapage.PowerDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.SpeedAndCadenceDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.SpeedCadenceDataPageRegistry;

public class ComprehensiveDataPageRegistry
extends AbstractDataPageRegistry {
    public ComprehensiveDataPageRegistry() {
        super(new CommonDataPageRegistry(), new FecDataPageRegistry(), new HrmDataPageRegistry(), new PowerDataPageRegistry(), new SpeedAndCadenceDataPageRegistry(), new SpeedCadenceDataPageRegistry());
    }
}
