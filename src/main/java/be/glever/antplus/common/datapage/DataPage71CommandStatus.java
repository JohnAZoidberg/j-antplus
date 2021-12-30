package be.glever.antplus.common.datapage;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import java.util.Arrays;

public class DataPage71CommandStatus extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 71;

    public DataPage71CommandStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    public byte getLastReceivedCommandId() {
        return getDataPageBytes()[1];
    }

    public byte getSequenceNumber() {
        return getDataPageBytes()[2];
    }

    public CommandStatus getCommandStatus() {
        switch (ByteUtils.toInt(getDataPageBytes()[3])) {
            case 0: {
                return CommandStatus.PASS;
            }
            case 1: {
                return CommandStatus.FAIL;
            }
            case 2: {
                return CommandStatus.NOT_SUPPORTED;
            }
            case 3: {
                return CommandStatus.REJECTED;
            }
            case 4: {
                return CommandStatus.PENDING;
            }
            case 255: {
                return CommandStatus.UNINITIALIZED;
            }
        }
        throw new IllegalStateException("Device sent reserved command status");
    }

    public byte[] getData() {
        return Arrays.copyOfRange(getDataPageBytes(), 4, 8);
    }

    public static enum CommandStatus {
        PASS,
        FAIL,
        NOT_SUPPORTED,
        REJECTED,
        PENDING,
        UNINITIALIZED;

    }
}
