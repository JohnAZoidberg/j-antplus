package be.glever.ant.message;

import be.glever.ant.AntException;

public interface AntMessage {
    public byte getMessageId();

    public void parse(byte[] value) throws AntException;

    public byte[] toByteArray();
}
