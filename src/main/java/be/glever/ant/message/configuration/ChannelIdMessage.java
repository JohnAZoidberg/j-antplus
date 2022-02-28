package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;

public class ChannelIdMessage extends AbstractAntMessage implements AntBlockingMessage {

    public static final int MSG_ID = 0x51;
    private byte[] bytes;

    public ChannelIdMessage() {
    }

    public ChannelIdMessage(byte channelNr, byte[] deviceNr, byte deviceTypeID, byte transmissionType) {
        this.bytes = new byte[]{channelNr, deviceNr[0], deviceNr[1], deviceTypeID, transmissionType};
    }

    public AntChannelId getChannelId() {
        return new AntChannelId(this.getTransmissionType(), this.getDeviceTypeId(), this.getDeviceNr());
    }

    public byte getChannelNr() {
        return bytes[0];
    }

    public byte[] getDeviceNr() {
        return new byte[]{bytes[1], bytes[2]};
    }

    public byte getDeviceTypeId() {
        return bytes[3];
    }

    public AntPlusDeviceType getDeviceType() {
        return AntPlusDeviceType.valueOf(this.getDeviceTypeId()).get();
    }

    public AntChannelTransmissionType getTransmissionType() {
        return new AntChannelTransmissionType(this.bytes[4]);
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    @Override
    public byte[] getMessageContent() {
        return bytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.bytes = messageContentBytes;
    }

    @Override
    public byte getResponseMessageId() {
        return ChannelEventOrResponseMessage.MSG_ID;
    }
}
