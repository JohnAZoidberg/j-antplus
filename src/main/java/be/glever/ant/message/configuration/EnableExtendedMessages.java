package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;

public class EnableExtendedMessages
extends AbstractAntMessage
implements AntBlockingMessage {
    private byte[] bytes = new byte[]{0, 0};

    public EnableExtendedMessages() {
    }

    public EnableExtendedMessages(boolean enable) {
        this.bytes[1] = (byte)(enable ? 1 : 0);
    }

    @Override
    public byte getMessageId() {
        return 102;
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
        return 64;
    }
}
