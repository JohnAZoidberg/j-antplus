package be.glever.antplus.power;

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
import be.glever.ant.util.ByteUtils;
import be.glever.util.logging.Log;
import java.time.Duration;
import java.util.Arrays;
import reactor.core.publisher.Flux;

public class PowerChannel
extends AntChannel {
        public static final byte CHANNEL_FREQUENCY = 0x39;  // RF Channel 57 (2457MHz)
        public static final byte[] DEVICE_NUMBER_WILDCARD = new byte[]{0, 0};
        public static final byte DEFAULT_PUBLIC_NETWORK = 0;
        private static final Log LOG = Log.getLogger(PowerChannel.class);
        private final AntUsbDevice device;
        public static final byte[] CHANNEL_PERIOD = ByteUtils.toUShort(8182);
        private Flux<AntMessage> eventFlux;

        public PowerChannel(AntUsbDevice device) {
                this(device, DEVICE_NUMBER_WILDCARD);
        }

        public PowerChannel(AntUsbDevice device, byte[] deviceNumber) {
                this.device = device;
                this.setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
                this.setNetwork(new AntChannelNetwork((byte) 0, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
                this.setRfFrequency(CHANNEL_FREQUENCY);
                this.setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, AntPlusDeviceType.Power, deviceNumber));
                this.setChannelPeriod(CHANNEL_PERIOD);
                this.device.openChannel(this).block(Duration.ofSeconds(10L));
        }

        @Override
        public void subscribeTo(Flux<AntMessage> messageFlux) {
                this.eventFlux = messageFlux.filter(this::isMatchingAntMessage).distinctUntilChanged(AntMessage::toByteArray, Arrays::equals);
        }

        @Override
        public Flux<AntMessage> getEvents() {
                return this.eventFlux;
        }

        private boolean isMatchingAntMessage(AntMessage message2) {
                if (message2.getMessageId() == BroadcastDataMessage.MSG_ID) {
                        return ((BroadcastDataMessage)message2).getChannelNumber() == super.getChannelNumber();
                }
                if (message2.getMessageId() == ChannelEventOrResponseMessage.MSG_ID) {
                        return ((ChannelEventOrResponseMessage)message2).getChannelNumber() == super.getChannelNumber();
                }
                return false;
        }
}
