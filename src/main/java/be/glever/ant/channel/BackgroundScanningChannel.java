package be.glever.ant.channel;

import be.glever.ant.channel.AntChannel;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelNetwork;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntNetworkKeys;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.antplus.hrm.HRMChannel;
import be.glever.util.logging.Log;
import java.time.Duration;
import java.util.Arrays;
import reactor.core.publisher.Flux;
import reactor.util.annotation.NonNull;

public class BackgroundScanningChannel
extends AntChannel {
    public static final byte CHANNEL_FREQUENCY = 0x39;  // RF Channel 57 (2457MHz)
    public static final byte[] DEVICE_NUMBER_WILDCARD = new byte[]{0, 0};
    public static final byte DEVICE_TYPE_ID_WILDCARD = 0;
    public static final byte DEFAULT_PUBLIC_NETWORK = 0;
    private static final Log LOG = Log.getLogger(BackgroundScanningChannel.class);
    private final AntUsbDevice device;
    private Flux<AntMessage> eventFlux;

    public BackgroundScanningChannel(@NonNull AntUsbDevice device) {
        this(device, AntPlusDeviceType.Any);
    }

    public BackgroundScanningChannel(@NonNull AntUsbDevice device, AntPlusDeviceType deviceType) {
        this.device = device;
        this.setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
        this.setNetwork(new AntChannelNetwork(DEFAULT_PUBLIC_NETWORK, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
        this.setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, deviceType.value(), DEVICE_NUMBER_WILDCARD));
        this.setRfFrequency(CHANNEL_FREQUENCY);
        this.setChannelPeriod(HRMChannel.CHANNEL_PERIOD);
        this.device.openBackgroundScanningChannel(this).block(Duration.ofSeconds(10));
    }

    @Override
    public void subscribeTo(Flux<AntMessage> messageFlux) {
        this.eventFlux = messageFlux.filter(this::isMatchingAntMessage).distinctUntilChanged(AntMessage::toByteArray, Arrays::equals);
    }

    @Override
    public Flux<AntMessage> getEvents() {
        return this.eventFlux;
    }

    private boolean isMatchingAntMessage(AntMessage message) {
        if (message.getMessageId() == BroadcastDataMessage.MSG_ID) {
            return ((BroadcastDataMessage)message).getChannelNumber() == super.getChannelNumber();
        }
        if (message.getMessageId() == ChannelEventOrResponseMessage.MSG_ID) {
            return ((ChannelEventOrResponseMessage)message).getChannelNumber() == super.getChannelNumber();
        }
        return false;
    }
}
