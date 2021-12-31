package be.glever.antplus.common.datapage;

import be.glever.ant.util.ByteUtils;
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
            case 0x00: {
                return CommandStatus.PASS;
            }
            case 0x01: {
                return CommandStatus.FAIL;
            }
            case 0x02: {
                return CommandStatus.NOT_SUPPORTED;
            }
            case 0x03: {
                return CommandStatus.REJECTED;
            }
            case 0x04: {
                return CommandStatus.PENDING;
            }
            case 0xFF: {
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
