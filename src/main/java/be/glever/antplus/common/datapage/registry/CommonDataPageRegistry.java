package be.glever.antplus.common.datapage.registry;

import be.glever.antplus.common.datapage.DataPage67AntFsClientBeacon;
import be.glever.antplus.common.datapage.DataPage68AntFsHostCommandResponse;
import be.glever.antplus.common.datapage.DataPage70Request;
import be.glever.antplus.common.datapage.DataPage71CommandStatus;
import be.glever.antplus.common.datapage.DataPage73GenericCommand;
import be.glever.antplus.common.datapage.DataPage74OpenChannelCommand;
import be.glever.antplus.common.datapage.DataPage76ModeSettingsCommand;
import be.glever.antplus.common.datapage.DataPage78MultiComponentSystemManufacturersInformation;
import be.glever.antplus.common.datapage.DataPage79MultiComponentSystemProductInformation;
import be.glever.antplus.common.datapage.DataPage80ManufacturersInformation;
import be.glever.antplus.common.datapage.DataPage81ProductInformation;
import be.glever.antplus.common.datapage.DataPage82BatteryStatus;
import be.glever.antplus.common.datapage.DataPage83TimeAndDate;
import be.glever.antplus.common.datapage.DataPage84SubfieldData;
import be.glever.antplus.common.datapage.DataPage85MemoryLevel;
import be.glever.antplus.common.datapage.DataPage86GetPairedDevices;
import be.glever.antplus.common.datapage.DataPage87ErrorDescription;
import be.glever.antplus.common.datapage.registry.AbstractDataPageRegistry;

public class CommonDataPageRegistry
extends AbstractDataPageRegistry {
    public CommonDataPageRegistry() {
        this.add((byte)67, DataPage67AntFsClientBeacon::new);
        this.add((byte)68, DataPage68AntFsHostCommandResponse::new);
        this.add((byte)70, DataPage70Request::new);
        this.add((byte)71, DataPage71CommandStatus::new);
        this.add((byte)73, DataPage73GenericCommand::new);
        this.add((byte)74, DataPage74OpenChannelCommand::new);
        this.add((byte)76, DataPage76ModeSettingsCommand::new);
        this.add((byte)78, DataPage78MultiComponentSystemManufacturersInformation::new);
        this.add((byte)79, DataPage79MultiComponentSystemProductInformation::new);
        this.add((byte)80, DataPage80ManufacturersInformation::new);
        this.add((byte)81, DataPage81ProductInformation::new);
        this.add((byte)82, DataPage82BatteryStatus::new);
        this.add((byte)83, DataPage83TimeAndDate::new);
        this.add((byte)84, DataPage84SubfieldData::new);
        this.add((byte)85, DataPage85MemoryLevel::new);
        this.add((byte)86, DataPage86GetPairedDevices::new);
        this.add((byte)87, DataPage87ErrorDescription::new);
    }
}
