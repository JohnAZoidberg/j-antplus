package be.glever.antplus.common.datapage;

public class DataPage70Request extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 70;

    public DataPage70Request(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * DataPage to request specific datapage from device so as not to have to wait until device sends it itself.
     * Meant for faster display of info to user.
     *
     * @param timesToTransmitRequestedPage     (1-128)
     * @param transmitUntilAcknowledgeReceived If true, timesToTransmitRequestedPage and useAcknowledgementMessages are ignored.
     * @param requestedDataPageNr              The datapage to request
     */
    public DataPage70Request(int timesToTransmitRequestedPage, boolean useAcknowledgementMessages, boolean transmitUntilAcknowledgeReceived, int requestedDataPageNr) {
        super(createDataPageBytes(timesToTransmitRequestedPage, useAcknowledgementMessages, transmitUntilAcknowledgeReceived, (byte) requestedDataPageNr));
    }

    private static byte[] createDataPageBytes(int timesToTransmitRequestedPage, boolean useAcknowledgementMessages, boolean transmitUntilAcknowledgeReceived, byte requestedDataPage) {
        byte[] dataPageBytes = new byte[8];
        dataPageBytes[0] = PAGE_NR;
        dataPageBytes[1] = (byte) 0xFF;
        dataPageBytes[2] = (byte) 0xFF;
        dataPageBytes[3] = (byte) 0xFF;
        dataPageBytes[4] = (byte) 0xFF;

        if (transmitUntilAcknowledgeReceived) {
            dataPageBytes[5] = (byte) 0x80;
        } else {
            byte timesToTransmitMask = 0b01111111;
            dataPageBytes[5] = (byte) (0b01111111 & timesToTransmitRequestedPage);

            byte useAckMsgMask = useAcknowledgementMessages ? (byte) ~timesToTransmitMask : 0;
            dataPageBytes[5] = (byte) (dataPageBytes[5] | useAckMsgMask);
        }

        dataPageBytes[6] = requestedDataPage;

        dataPageBytes[7] = 1;
        return dataPageBytes;
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public static enum CommandType {
        REQUEST_DATA_PAGE((byte) 1),
        REQUEST_ANTFS_SESSION((byte) 2),
        REQUEST_DATA_PAGE_FROM_SLAVE((byte) 3),
        REQUEST_DATA_PAGE_SET((byte) 4);

        private byte value;

        private CommandType(byte value) {
            this.value = value;
        }

        public byte value() {
            return this.value;
        }
    }
}
