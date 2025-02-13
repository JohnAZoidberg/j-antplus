package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class SetLowPrioritySearchTimeoutMessage extends AbstractAntMessage implements AntBlockingMessage {
    private byte channelNumber;
    private int searchTimeout;

    public SetLowPrioritySearchTimeoutMessage(byte channelNumber, int searchTimeout) {
        this.channelNumber = channelNumber;
        this.searchTimeout = searchTimeout;
    }

    @Override
    public byte getMessageId() {
        return 0x63;
    }

    @Override
    public byte[] getMessageContent() {
        return new ByteArrayBuilder()
                .write(channelNumber)
                .write((byte) searchTimeout)
                .toByteArray();
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        // TODO Auto-generated method stub
    }

    @Override
    public byte getResponseMessageId() {
        return ChannelEventOrResponseMessage.MSG_ID;
    }
}
