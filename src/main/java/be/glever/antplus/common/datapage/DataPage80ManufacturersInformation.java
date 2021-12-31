package be.glever.antplus.common.datapage;

import be.glever.ant.util.ByteUtils;

public class DataPage80ManufacturersInformation extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 80;

    public DataPage80ManufacturersInformation(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    public byte getHwRevision() {
        return getDataPageBytes()[3];
    }

    public int getManufacturerId() {
        return ByteUtils.fromUShort(getDataPageBytes()[4], getDataPageBytes()[5]);
    }

    public int getModelNumber() {
        return ByteUtils.fromUShort(getDataPageBytes()[6], getDataPageBytes()[7]);
    }
}
