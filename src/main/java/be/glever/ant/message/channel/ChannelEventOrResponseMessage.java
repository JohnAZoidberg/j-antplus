package be.glever.ant.message.channel;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.util.ByteUtils;


/**
 * todo Having 2 types of messages (Channel Event / Channel Response) bound to the same message id (0x40) may impact message generation logic as currently this is done directly through constructor (change to factory?)
 */
public class ChannelEventOrResponseMessage extends AbstractAntMessage {
    public static final byte MSG_ID = (byte) 0x40;
    private byte[] messageContentBytes;

    @Override
    public byte[] getMessageContent() {
        return messageContentBytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.messageContentBytes = messageContentBytes;
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    public byte getChannelNumber() {
        return messageContentBytes[0];
    }

    public byte getRespondToMessageId() {
        return messageContentBytes[1];
    }

    public ChannelEventResponseCode getResponseCode() {
        return ChannelEventResponseCode.fromValue(messageContentBytes[2]);
    }

    @Override
    public String toString() {
        return "ChannelEventOrResponseMessage{" +
                "messageId=" + ByteUtils.hexString(getMessageId()) +
                ",respondToMessageId=" + ByteUtils.hexString(getRespondToMessageId()) +
                ",channelNumber=" + ByteUtils.hexString(getChannelNumber()) +
                ",responseCode=" + getResponseCode().name() +
                '}';
    }
}
