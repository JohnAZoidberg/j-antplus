package be.glever.antplus.common.datapage;

import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;

public class RawAntPlusDataPage
extends AbstractAntPlusDataPage {
    public RawAntPlusDataPage(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return getDataPageBytes()[0];
    }
}
