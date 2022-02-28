package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;

public class EnableExtendedMessages
extends AbstractAntMessage
implements AntBlockingMessage {
    private byte[] bytes = {0x00, 0x00};

    public EnableExtendedMessages() {
    }

    public EnableExtendedMessages(boolean enable) {
        this.bytes[1] = (byte)(enable ? 0x01 : 0x00);
    }

    @Override
    public byte getMessageId() {
        return 0x66;
    }

    @Override
    public byte[] getMessageContent() {
        return this.bytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.bytes = messageContentBytes;
    }

    @Override
    public byte getResponseMessageId() {
        return 0x40;
    }
}
