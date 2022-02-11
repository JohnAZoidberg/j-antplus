package be.glever.anttest;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.hrm.HRMChannel;
import be.glever.antplus.hrm.datapage.HrmDataPageRegistry;
import be.glever.antplus.hrm.datapage.background.HrmDataPage1CumulativeOperatingTime;
import be.glever.antplus.hrm.datapage.background.HrmDataPage2ManufacturerInformation;
import be.glever.antplus.hrm.datapage.background.HrmDataPage3ProductInformation;
import be.glever.antplus.hrm.datapage.background.HrmDataPage7BatteryStatus;
import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;
import be.glever.anttest.stats.StatCalculator;
import be.glever.anttest.stats.StatSummary;
import be.glever.util.logging.Log;

import java.io.IOException;

public class HrmTest_Main {
    private static final Log LOG = Log.getLogger(HrmTest_Main.class);
    private int heartbeatCount;
    private HrmDataPageRegistry registry = new HrmDataPageRegistry();
    private StatCalculator statCalculator = new StatCalculator();

    private HrmTest_Main() throws IOException {

        try (AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.
            HRMChannel channel = new HRMChannel(device);
            channel.getEvents().doOnNext(this::handle).subscribe();
            System.in.read();
        }
    }

    public static void main(String[] args) throws Exception {
        new HrmTest_Main();
    }

    private void handle(AntMessage antMessage) {
        if (antMessage instanceof BroadcastDataMessage) {
            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);

            if (dataPage instanceof HrmDataPage4PreviousHeartBeatEvent) {
                calcStats((HrmDataPage4PreviousHeartBeatEvent) dataPage);
            } else if (dataPage instanceof HrmDataPage2ManufacturerInformation) {
                HrmDataPage2ManufacturerInformation dp = (HrmDataPage2ManufacturerInformation) dataPage;
                LOG.debug(() -> String.format(
                        "Manufacturer: %d, SerialNumber: %d",
                        dp.getManufacturerId(),
                        dp.getSerialNumber()
                ));
            } else if (dataPage instanceof HrmDataPage3ProductInformation) {
                HrmDataPage3ProductInformation dp = (HrmDataPage3ProductInformation) dataPage;
                LOG.debug(() -> String.format(
                        "HardwareVersion: %d, SoftwareVersion: %d, ModelNumber: %d",
                        dp.getHardwareVersion(),
                        dp.getSoftwareVersion(),
                        dp.getModelNumber()
                ));
            } else if (dataPage instanceof HrmDataPage7BatteryStatus) {
                HrmDataPage7BatteryStatus dp = (HrmDataPage7BatteryStatus) dataPage;
                LOG.debug(() -> String.format(
                        "Battery - Level: %d%%, Voltage: %.2f, Status: %s",
                        dp.getBatteryLevelPercentage(),
                        dp.getBatteryVoltage(),
                        dp.getBatteryVoltageDescription().name()
                ));
            } else if (dataPage instanceof HrmDataPage1CumulativeOperatingTime) {
                HrmDataPage1CumulativeOperatingTime dp = (HrmDataPage1CumulativeOperatingTime) dataPage;
                LOG.debug(() -> String.format(
                        "Cumulative Operating time: %ds / %.1fh",
                        dp.getCumulativeOperatingTime(),
                        dp.getCumulativeOperatingTime() / 3600.0
                ));
            } else {
                LOG.debug(() -> "Received " + dataPage.toString());
            }
        } else {
            if (antMessage instanceof ChannelEventOrResponseMessage) {
                ChannelEventOrResponseMessage channelEventOrResponseMessage = (ChannelEventOrResponseMessage) antMessage;
                if (channelEventOrResponseMessage.getResponseCode() == ChannelEventResponseCode.EVENT_RX_FAIL) {
                    // Not a useful message. Occurs when the channel is
                    // expecting a message at a fixed rate but didn't receive
                    // one.
                    return;
                }
            }
            LOG.warn(()-> String.format("Ignoring message  %s", antMessage));
        }
    }

    // TODO responsibility of client

    private void calcStats(HrmDataPage4PreviousHeartBeatEvent dataPage) {
        if (this.heartbeatCount != dataPage.getHeartBeatCount()) {
            this.heartbeatCount = dataPage.getHeartBeatCount();

            StatSummary statSummary = this.statCalculator.push(dataPage);

            LOG.info(statSummary::toString);
        }
    }


    /**
     * For the moment not taking the legacy hrm devices into account.
     * Non-legacy devices swap the first bit of the pageNumber every 4 messages.
     *
     * @param payLoad
     */
    private void removeToggleBit(byte[] payLoad) {
        payLoad[0] = (byte) (0b01111111 & payLoad[0]);
    }
}
