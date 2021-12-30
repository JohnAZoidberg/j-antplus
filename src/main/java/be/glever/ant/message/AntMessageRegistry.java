package be.glever.ant.message;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.configuration.AssignChannelMessage;
import be.glever.ant.message.configuration.ChannelIdMessage;
import be.glever.ant.message.configuration.ChannelPeriodMessage;
import be.glever.ant.message.configuration.ChannelRfFrequencyMessage;
import be.glever.ant.message.configuration.NetworkKeyMessage;
import be.glever.ant.message.configuration.SearchTimeoutMessage;
import be.glever.ant.message.configuration.UnassignChannelMessage;
import be.glever.ant.message.control.CloseChannelMessage;
import be.glever.ant.message.control.OpenChannelMessage;
import be.glever.ant.message.control.OpenRxScanModeMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.control.ResetSystemMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.message.notification.SerialErrorMessage;
import be.glever.ant.message.notification.StartupNotificationMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.message.requestedresponse.SerialNumberMessage;
import be.glever.ant.util.ByteUtils;
import be.glever.util.logging.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AntMessageRegistry {
    private static final Log LOG = Log.getLogger(AntMessageRegistry.class);
    private static Map<Byte, Class<? extends AbstractAntMessage>> registry = new HashMap<Byte, Class<? extends AbstractAntMessage>>();

    public static AntMessage from(byte[] bytes) throws AntException {
        byte msgLength = (byte)(bytes[1] + 4);
        byte msgId = bytes[2];
        byte[] msgBytes = Arrays.copyOf(bytes, (int)msgLength);
        Class<? extends AbstractAntMessage> messageClass = registry.get(msgId);
        AntMessage messageInstance = AntMessageRegistry.getMessageInstance(msgBytes, msgId, messageClass);
        LOG.debug(() -> String.format("Converted %s to %s", ByteUtils.hexString(msgBytes), messageInstance.getClass().getSimpleName()));
        return messageInstance;
    }

    private static AntMessage getMessageInstance(byte[] msgBytes, byte msgId, Class<? extends AbstractAntMessage> messageClass) {
        AntMessage messageInstance;
        if (messageClass == null) {
            LOG.error(() -> String.format("Could not convert %s to an AntMessage. Bytes received were %s", ByteUtils.hexString(msgId), ByteUtils.hexString(msgBytes)));
            messageInstance = new UnknownMessage(msgBytes);
        } else {
            messageInstance = AntMessageRegistry.instantiate(messageClass);
            messageInstance.parse(msgBytes);
        }
        return messageInstance;
    }

    private static void add(Class<? extends AbstractAntMessage> antMessageImplClass) {
        byte messageId = AntMessageRegistry.instantiate(antMessageImplClass).getMessageId();
        if (registry.containsKey(messageId)) {
            Class<? extends AbstractAntMessage> existingClass = registry.get(messageId);
            throw new IllegalStateException(String.format("Could not add class %s to registry due to duplicate id %s. Conflicting class: %s", antMessageImplClass, messageId, existingClass));
        }
        registry.put(messageId, antMessageImplClass);
    }

    private static AntMessage instantiate(Class<? extends AbstractAntMessage> msgImplClass) {
        try {
            return msgImplClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("Initialization Error. Could not call default constructor on class [" + msgImplClass.getName() + "]");
        }
    }

    static {
        AntMessageRegistry.add(AssignChannelMessage.class);
        AntMessageRegistry.add(ChannelPeriodMessage.class);
        AntMessageRegistry.add(ChannelRfFrequencyMessage.class);
        AntMessageRegistry.add(SearchTimeoutMessage.class);
        AntMessageRegistry.add(UnassignChannelMessage.class);
        AntMessageRegistry.add(NetworkKeyMessage.class);
        AntMessageRegistry.add(ChannelIdMessage.class);
        AntMessageRegistry.add(CloseChannelMessage.class);
        AntMessageRegistry.add(OpenChannelMessage.class);
        AntMessageRegistry.add(OpenRxScanModeMessage.class);
        AntMessageRegistry.add(RequestMessage.class);
        AntMessageRegistry.add(ResetSystemMessage.class);
        AntMessageRegistry.add(StartupNotificationMessage.class);
        AntMessageRegistry.add(SerialErrorMessage.class);
        AntMessageRegistry.add(BroadcastDataMessage.class);
        AntMessageRegistry.add(AntVersionMessage.class);
        AntMessageRegistry.add(ChannelStatusMessage.class);
        AntMessageRegistry.add(SerialNumberMessage.class);
        AntMessageRegistry.add(CapabilitiesResponseMessage.class);
        AntMessageRegistry.add(ChannelEventOrResponseMessage.class);
    }

    public static class UnknownMessage
    extends AbstractAntMessage
    implements AntMessage {
        private byte[] bytes;

        private UnknownMessage(byte[] fullByteArray) {
            this.bytes = fullByteArray;
        }

        @Override
        public byte getMessageId() {
            return bytes[2];
        }

        @Override
        public byte[] getMessageContent() {
            return Arrays.copyOfRange(bytes, 4, 4 + bytes[1]);
        }

        @Override
        public void setMessageBytes(byte[] messageContentBytes) throws AntException {
            throw new UnsupportedOperationException("Unsupported, only constructor should be used");
        }

        @Override
        public String toString() {
            return "UnknownMessage [msgId=" + ByteUtils.hexString(getMessageId()) + ", bytes=" + ByteUtils.hexString(bytes) + "]";
        }

    }

}
