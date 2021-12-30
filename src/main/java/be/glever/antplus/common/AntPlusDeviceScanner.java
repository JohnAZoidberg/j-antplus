package be.glever.antplus.common;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelNetwork;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntNetworkKeys;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.AssignChannelMessage;
import be.glever.ant.message.configuration.ChannelIdMessage;
import be.glever.ant.message.configuration.ExtendedAssignment;
import be.glever.ant.message.configuration.SearchTimeoutMessage;
import be.glever.ant.message.configuration.SetLowPrioritySearchTimeoutMessage;
import be.glever.ant.message.control.OpenRxScanModeMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceCapabilities;
import be.glever.ant.usb.RequestMatcher;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.hrm.HRMChannel;
import be.glever.antplus.hrm.datapage.HrmDataPageRegistry;
import be.glever.util.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import reactor.core.publisher.Flux;

public class AntPlusDeviceScanner {
    private static final Log LOG = Log.getLogger(AntPlusDeviceScanner.class);

    private AntUsbDevice antUsbDevice;
    private AntChannelType CHANNEL_TYPE = AntChannelType.BIDIRECTIONAL_SLAVE;
    public byte NETWORK_NUMBER = 0;
    private AntChannelNetwork NETWORK = new AntChannelNetwork(this.NETWORK_NUMBER, AntNetworkKeys.ANT_PLUS_NETWORK_KEY);
    private Flux<AntMessage> eventFlux;
    private HrmDataPageRegistry registry = new HrmDataPageRegistry();

    public AntPlusDeviceScanner(AntUsbDevice antUsbDevice) {
        this.antUsbDevice = antUsbDevice;
    }

    /**
     * Scans the Ant+ Network for all Ant+ devices.
     * For each found device, an AntChannel will be  assigned, until no more devices found or the max number of channels reached (see {@link AntUsbDevice#getCapabilities()}.
     * If you need to support more devices than {@link AntUsbDeviceCapabilities#getMaxChannels()}, use continuous scanning (unsupported at time of writing).
     * Downside is that all RF bandwith is spent scanning and no channels can be formed so you are limited to receiving only..
     *
     * @return List of assigned {@link AntChannel}s. Client should open the channel if he wishes to receive all messages, or unassign if he is not interested.
     * @throws AntException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<AntChannelId> scanAvailableDevices() throws AntException, ExecutionException, InterruptedException {
        ArrayList<AntChannelId> availableDevices = new ArrayList<AntChannelId>();
        AntUsbDeviceCapabilities capabilities = this.antUsbDevice.getCapabilities();
        short maxChannels = capabilities.getMaxChannels();
        for (int channelNumber = 0; channelNumber < maxChannels; channelNumber++) {
            RequestMessage requestMessage = new RequestMessage((byte) channelNumber, ChannelStatusMessage.MSG_ID);
            ChannelStatusMessage responseMessage = (ChannelStatusMessage) antUsbDevice.sendBlocking(requestMessage);

            if (responseMessage.getChannelStatus() == ChannelStatusMessage.CHANNEL_STATUS.UnAssigned) {
                // scan channel for ant device
            } else {
                // TODO
                LOG.debug(() -> String.format("Channel %s is in status %s. Not scanning.", responseMessage.getChannelNumber(), responseMessage.getChannelStatus()));
            }
            BiFunction<AntMessage, AntMessage, Boolean> responseMatcher = RequestMatcher::isMatchingResponse;
            this.antUsbDevice.sendBlocking(new AssignChannelMessage((byte) channelNumber, this.CHANNEL_TYPE.getValue(), this.NETWORK_NUMBER, ExtendedAssignment.BACKGROUND_SCANNING_ENABLE), responseMatcher);
            this.antUsbDevice.sendBlocking(new ChannelIdMessage((byte) channelNumber, new byte[]{0, 0}, (byte) 0, AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE.getValue()), responseMatcher);
            this.antUsbDevice.sendBlocking(new SearchTimeoutMessage((byte) channelNumber, 0), responseMatcher);
            this.antUsbDevice.sendBlocking(new SetLowPrioritySearchTimeoutMessage((byte) channelNumber, 2), responseMatcher);
        }
        return availableDevices;
    }

    private void handle(AntMessage antMessage) {
        BroadcastDataMessage msg;
        byte[] payLoad;
        AbstractAntPlusDataPage dataPage;
        if (antMessage instanceof BroadcastDataMessage && (dataPage = this.registry.constructDataPage(payLoad = (msg = (BroadcastDataMessage)antMessage).getPayLoad())) != null) {
            System.out.print(dataPage.toString());
        }
    }

    public void openRxScanMode() throws AntException, ExecutionException, InterruptedException {
        HRMChannel channel = new HRMChannel(antUsbDevice);
        AssignChannelMessage assignChannelMessage = new AssignChannelMessage((byte) 0x00, channel.getChannelType().getValue(), channel.getNetwork().getNetworkNumber());
        if (((ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(assignChannelMessage)).getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not assign channel");
        }

        OpenRxScanModeMessage rxScanModeMessage = new OpenRxScanModeMessage(false);
        antUsbDevice.send(rxScanModeMessage);
    }
}
