package be.glever.antplus.fec;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.AssignChannelMessage;
import be.glever.ant.message.configuration.ChannelIdMessage;
import be.glever.ant.message.configuration.ChannelPeriodMessage;
import be.glever.ant.message.configuration.ChannelRfFrequencyMessage;
import be.glever.ant.message.configuration.NetworkKeyMessage;
import be.glever.ant.message.control.OpenChannelMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.antplus.fec.FecChannel;
import java.util.concurrent.ExecutionException;

public class FecSlave {

    private static final byte ANTPLUS_FEC_FREQUENCY = (byte) 57; // 2457Mhz
    private AntUsbDevice antUsbDevice;

    public FecSlave(AntUsbDevice antUsbDevice) {
        this.antUsbDevice = antUsbDevice;
    }

    public AntChannel[] listDevices() throws AntException, ExecutionException, InterruptedException {
        byte channelNumber = 0;
        FecChannel channel = new FecChannel(this.antUsbDevice);
        NetworkKeyMessage networkKeyMessage = new NetworkKeyMessage(channelNumber, channel.getNetwork().getNetworkKey());
        ChannelEventOrResponseMessage setNetworkKeyResponse = (ChannelEventOrResponseMessage)this.antUsbDevice.sendBlocking(networkKeyMessage);
        if (setNetworkKeyResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not set network key");
        }
        AssignChannelMessage assignChannelMessage = new AssignChannelMessage(channelNumber, (byte) 64, (byte) 0);
        ChannelEventOrResponseMessage response = (ChannelEventOrResponseMessage)this.antUsbDevice.sendBlocking(assignChannelMessage);
        if (response.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not assign channel");
        }
        ChannelIdMessage channelIdMessage = new ChannelIdMessage(channelNumber, new byte[]{0, 0}, AntPlusDeviceType.Fec.value(), (byte) 0);
        ChannelEventOrResponseMessage channelIdResponse = (ChannelEventOrResponseMessage)this.antUsbDevice.sendBlocking(channelIdMessage);
        if (channelIdResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could set channelId");
        }
        ChannelPeriodMessage channelPeriodMessage = new ChannelPeriodMessage(channelNumber, FecChannel.CHANNEL_PERIOD);
        ChannelEventOrResponseMessage channelPeriodResponseMessage = (ChannelEventOrResponseMessage)this.antUsbDevice.sendBlocking(channelPeriodMessage);
        if (channelPeriodResponseMessage.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could set channel period");
        }
        ChannelRfFrequencyMessage channelRfFrequencyMessage = new ChannelRfFrequencyMessage(channelNumber, (byte) 57);
        ChannelEventOrResponseMessage channelRfFrequencyResponse = (ChannelEventOrResponseMessage)this.antUsbDevice.sendBlocking(channelRfFrequencyMessage);
        if (channelRfFrequencyResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not set rf frequency");
        }
        if ((response = (ChannelEventOrResponseMessage)this.antUsbDevice.sendBlocking(new OpenChannelMessage(response.getChannelNumber()))).getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not open channel");
        }
        return null;
    }
}

