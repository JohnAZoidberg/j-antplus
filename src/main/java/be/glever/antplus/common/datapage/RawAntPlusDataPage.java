package be.glever.antplus.common.datapage;

public class RawAntPlusDataPage extends AbstractAntPlusDataPage {
    public RawAntPlusDataPage(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return getDataPageBytes()[0];
    }
}
