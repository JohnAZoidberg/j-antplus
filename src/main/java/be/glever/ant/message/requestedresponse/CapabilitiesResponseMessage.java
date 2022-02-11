package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.util.logging.Log;

import java.util.Arrays;

import static be.glever.ant.util.ByteUtils.hasBitSet;
import be.glever.ant.util.ByteUtils;

/**
 * Response message describing the device's (transceiver) capabilities
 *
 * Document: ANT Message Protocol and Usage Rev 5.1
 * Section:  9.5.7.4 Capabilities (0x54)
 */
public class CapabilitiesResponseMessage extends AbstractAntMessage {
    private static final Log LOG = Log.getLogger(CapabilitiesResponseMessage.class);

    public static final byte MSG_ID = 0x54;
    private byte[] messageBytes;

    public CapabilitiesResponseMessage() {
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    @Override
    public byte[] getMessageContent() {
        return messageBytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        // The spec doesn't really say how many bytes are required. Probably at
        // least the number of channels, number of networks and the standard
        // options. But probably not any of the advances options. Nor the
        // number of SensRcore channels.
        // Let's be conservative and require the first 6 bytes.
        // I've encountered transceivers that provide 6 and some that provide 8 bytes.
        if (messageContentBytes.length < 6 || messageContentBytes.length > 8) {
            LOG.error(() -> String.format("Mesage content is %s", ByteUtils.hexString(messageContentBytes)));
            throw new AntException(
                    String.format("Incorrect message content length. Given: %s, expected: 6-8", messageContentBytes.length));
        }
        this.messageBytes = messageContentBytes;
    }

    /**
     * Number of ANT channels supported by the transceiver
     */
    public byte getMaxChannels() {
        return messageBytes[0];
    }

    /**
     * Number of ANT networks supported by the transceiver
     */
    public short getMaxNetworks() {
        return messageBytes[1];
    }

    private short getStandardOptionsByte() {
        return messageBytes[2];
    }

    /**
     * Can receive channels
     */
    public boolean getReceiveChannels() {
        return !hasBitSet(getStandardOptionsByte(), 0);
    }

    /**
     * Can transmit channels
     */
    public boolean getTransmitChannels() {
        return !hasBitSet(getStandardOptionsByte(), 1);
    }

    /**
     * Can receive messages
     */
    public boolean getReceiveMessages() {
        return !hasBitSet(getStandardOptionsByte(), 2);
    }

    /**
     * Can transmit messages
     */
    public boolean getTransmitMessages() {
        return !hasBitSet(getStandardOptionsByte(), 3);
    }

    /**
     * Supports acknowledged messages
     */
    public boolean getAckdMessages() {
        return !hasBitSet(getStandardOptionsByte(), 4);
    }

    /**
     * Supports burst messages
     */
    public boolean getBurstMessages() {
        return !hasBitSet(getStandardOptionsByte(), 5);
    }

    private byte getAdvancedOptionsByte() {
        // Optional byte. Default to all zero if not present.
        if (messageBytes.length < 4) {
            return 0x00;
        }
        return messageBytes[3];
    }

    public boolean getNetworkEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 1);
    }

    public boolean getSerialNumberEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 3);
    }

    public boolean getPerChannelTxPowerEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 4);
    }

    public boolean getLowPrioritySearchEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 5);
    }

    public boolean getScriptEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 6);
    }

    public boolean getSearchListEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 7);
    }

    private byte getAdvancedOptions2Byte() {
        // Optional byte. Default to all zero if not present.
        if (messageBytes.length < 5) {
            return 0x00;
        }
        return messageBytes[4];
    }

    public boolean getLedEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 0);
    }

    public boolean getExtMessageEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 1);
    }

    public boolean getScanModeEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 2);
    }

    public boolean getProxSearchEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 4);
    }

    public boolean getExtAssignEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 5);
    }

    public boolean getFsAntFsEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 6);
    }

    public boolean getFit1Enabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 7);
    }

    public short getMaxSensRcoreChannels() {
        // Optional byte. Default to 0 channels if not present.
        if (messageBytes.length < 6) {
            return 0;
        }
        return messageBytes[5];
    }

    private byte getAdvancedOptions3Byte() {
        // Optional byte. Default to all zero if not present.
        if (messageBytes.length < 7) {
            return 0x00;
        }
        return messageBytes[6];
    }

    public boolean getAdvancedBurstEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 0);
    }

    public boolean getEventBufferingEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 1);
    }

    public boolean getEventFilteringEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 2);
    }

    public boolean getHighDutySearchEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 3);
    }

    public boolean getSearchSharingEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 4);
    }

    public boolean getSelectiveDataUpdatesEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 6);
    }

    public boolean getEncryptedChannelEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 7);
    }

    private byte getAdvancedOptions4Byte() {
        // Optional byte. Default to all zero if not present.
        if (messageBytes.length < 8) {
            return 0x00;
        }
        return messageBytes[7];
    }

    public boolean getRfActiveNotificationEnabled() {
        return hasBitSet(getAdvancedOptions4Byte(), 0);
    }

    @Override
    public String toString() {
        return "CapabilitiesResponseMessage [getMessageId()=" + getMessageId() + ", getMessageContent()="
                + Arrays.toString(getMessageContent()) + ", getMaxChannels()=" + getMaxChannels()
                + ", getMaxNetworks()=" + getMaxNetworks() + ", getReceiveChannels()=" + getReceiveChannels()
                + ", getTransmitChannels()=" + getTransmitChannels() + ", getReceiveMessages()=" + getReceiveMessages()
                + ", getTransmitMessages()=" + getTransmitMessages() + ", getAckdMessages()=" + getAckdMessages()
                + ", getBurstMessages()=" + getBurstMessages() + ", getNetworkEnabled()=" + getNetworkEnabled()
                + ", getSerialNumberEnabled()=" + getSerialNumberEnabled() + ", getPerChannelTxPowerEnabled()="
                + getPerChannelTxPowerEnabled() + ", getLowPrioritySearchEnabled()=" + getLowPrioritySearchEnabled()
                + ", getScriptEnabled()=" + getScriptEnabled() + ", getSearchListEnabled()=" + getSearchListEnabled()
                + ", getLedEnabled()=" + getLedEnabled() + ", getExtMessageEnabled()=" + getExtMessageEnabled()
                + ", getScanModeEnabled()=" + getScanModeEnabled() + ", getProxSearchEnabled()="
                + getProxSearchEnabled() + ", getExtAssignEnabled()=" + getExtAssignEnabled() + ", getFsAntFsEnabled()="
                + getFsAntFsEnabled() + ", getFit1Enabled()=" + getFit1Enabled() + ", getMaxSensRcoreChannels()="
                + getMaxSensRcoreChannels() + ", getAdvancedBurstEnabled()=" + getAdvancedBurstEnabled()
                + ", getEventBufferingEnabled()=" + getEventBufferingEnabled() + ", getEventFilteringEnabled()="
                + getEventFilteringEnabled() + ", getHighDutySearchEnabled()=" + getHighDutySearchEnabled()
                + ", getSearchSharingEnabled()=" + getSearchSharingEnabled() + ", getSelectiveDataUpdatesEnabled()="
                + getSelectiveDataUpdatesEnabled() + ", getEncryptedChannelEnabled()=" + getEncryptedChannelEnabled()
                + ", getRfActiveNotificationEnabled()=" + getRfActiveNotificationEnabled() + "]";
    }

}
