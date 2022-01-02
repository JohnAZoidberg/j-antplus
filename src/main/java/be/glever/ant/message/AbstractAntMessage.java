package be.glever.ant.message;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.Rssi;
import be.glever.ant.util.ByteArrayBuilder;
import be.glever.ant.util.ByteUtils;

import java.util.Arrays;

public abstract class AbstractAntMessage implements AntMessage {
    private byte[] bytes;

    public abstract byte[] getMessageContent();

    public abstract void setMessageBytes(byte[] messageContentBytes) throws AntException;

    @Override
    public byte[] toByteArray() {
        byte[] messageContent = getMessageContent();

        ByteArrayBuilder bab = new ByteArrayBuilder();
        // TODO synchronous mode (0xa5 used for WRITE mode) is currently unsupported
        byte sync = (byte) 0xa4;
        bab.write(sync);
        bab.write((byte) messageContent.length);
        bab.write(getMessageId());
        bab.write(messageContent);
        bab.write(getCheckSum(bab.toByteArray()));
        bab.write((byte) 0x00, (byte) 0x00); // recommendation

        return bab.toByteArray();
    }

    @Override
    public void parse(byte[] bytes) throws AntException {
        this.bytes = bytes;
        validateNumberDataBytes(bytes);
        validateChecksum(bytes);
        setMessageBytes(Arrays.copyOfRange(bytes, 3, bytes.length - 1));
    }

    private byte getCheckSum(byte[] bytes) {
        byte checksum = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            checksum ^= bytes[i];
        }
        return checksum;
    }

    private void validateChecksum(byte[] bytes) throws AntException {
        byte calculatedChecksum = bytes[0];
        for (int i = 1; i < bytes.length - 1; i++) {
            calculatedChecksum ^= bytes[i];
        }

        byte checkSum = bytes[bytes.length - 1];
        if (calculatedChecksum != checkSum) {
            throw new AntException("Checksum doesnt match. Given: [" + checkSum + "]. Calculated: ["
                    + calculatedChecksum + "]. Message: " + ByteUtils.hexString(bytes));
        }
    }

    private void validateNumberDataBytes(byte[] bytes) throws AntException {
        byte nrDataBytes = bytes[1];
        if (nrDataBytes + 4 != bytes.length) {
            throw new AntException("Incorrect message length given [" + nrDataBytes + "] for byte array "
                    + ByteUtils.hexString(bytes));
        }
    }

    public Object getExtendedData() {
        byte contentByteSize = this.bytes[1];
        if (contentByteSize < 10) {
            return null;
        }

        int flagByte = this.bytes[12] & 0xFF;
        switch (flagByte) {
            case 0x80:
                return this.bytesToChannelId(Arrays.copyOfRange(this.bytes, 13, 17)); // 4 bytes
            case 0x40:
                return this.bytesToRssi(Arrays.copyOfRange(this.bytes, 13, 16)); // 3 bytes
            case 0x20:
                return ByteUtils.toInt(this.bytes[13], this.bytes[14]);
            case 0xE0:
                return null;
            case 0xA0:
                return null;
            case 0x60:
                return null;
            default:
                return null;
        }
    }

    private AntChannelId bytesToChannelId(byte[] bytes) throws AntException {
        AntPlusDeviceType deviceType = AntPlusDeviceType.valueOf(bytes[2]).orElseThrow(() -> new AntException("Standard message does not have extended data"));
        return new AntChannelId(new AntChannelTransmissionType(bytes[3]), deviceType, Arrays.copyOfRange(bytes, 0, 2));
    }

    private Rssi bytesToRssi(byte[] bytes) {
        if (bytes[0] != 32) {
            return null;
        }
        return new Rssi(bytes[1], bytes[2]);
    }
}
