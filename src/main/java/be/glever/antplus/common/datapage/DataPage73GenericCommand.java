package be.glever.antplus.common.datapage;

import be.glever.ant.util.ByteUtils;
import java.util.Arrays;

public class DataPage73GenericCommand extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 73;

    public DataPage73GenericCommand(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    public byte[] getSlaveSerialNumber() {
        return Arrays.copyOfRange(getDataPageBytes(), 1, 3);
    }

    public byte[] getSlaveManufacturerId() {
        return super.dataPageSubArray(3, 5);

    }

    public int getSequenceNr() {
        return ByteUtils.toInt(getDataPageBytes()[5]);
    }

    public byte[] getCommandNumber() {
        return super.dataPageSubArray(6, 8);
    }

}
