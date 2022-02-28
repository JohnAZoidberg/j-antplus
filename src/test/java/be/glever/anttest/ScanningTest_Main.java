package be.glever.anttest;

import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntScanResult;
import be.glever.ant.channel.BackgroundScanningChannel;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.ant.util.ByteUtils;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.common.datapage.DataPage80ManufacturersInformation;
import be.glever.antplus.common.datapage.registry.ComprehensiveDataPageRegistry;
import be.glever.antplus.hrm.datapage.background.HrmDataPage2ManufacturerInformation;
import be.glever.antplus.hrm.datapage.background.HrmDataPage3ProductInformation;
import be.glever.antplus.speedcadence.datapage.background.SpeedCadenceDataPage2ManufacturerInformation;
import be.glever.antplus.speedcadence.datapage.background.SpeedCadenceDataPage3ProductInformation;
import be.glever.util.logging.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 */
public class ScanningTest_Main {
    private static final Log LOG = Log.getLogger(ScanningTest_Main.class);
    private ComprehensiveDataPageRegistry registry = new ComprehensiveDataPageRegistry();
    private ArrayList<AntScanResult> scanResults = new ArrayList<>();

    private ScanningTest_Main() throws IOException {

        try (AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.

            // Timeout as a minute. Because it takes so long to receive all pages about device information
            // At leat on an HRM
            BackgroundScanningChannel scanChannel = new BackgroundScanningChannel(device, AntPlusDeviceType.Any, 60);
            scanChannel.getEvents().doOnNext(this::handle).subscribe();

            System.in.read();
        }
    }

    public static void main(String[] args) throws Exception {
        new ScanningTest_Main();
    }

    private void handle(AntMessage antMessage) {
        AntChannelId foundChannelId = null;
        Integer foundModelNumber = null;
        Integer foundManufacturerId = null;

        if (antMessage instanceof ChannelEventOrResponseMessage) {
            ChannelEventOrResponseMessage msg = (ChannelEventOrResponseMessage) antMessage;
            if (msg.getResponseCode() == ChannelEventResponseCode.EVENT_RX_SEARCH_TIMEOUT) {
                LOG.info(() -> "Reached search timeout. Stopping search");
                logScanResult();
                System.exit(0);
            }
        } else if (antMessage instanceof BroadcastDataMessage) {
            Object ext = ((AbstractAntMessage)antMessage).getExtendedData();
            if (ext != null) {
                if (ext instanceof AntChannelId) {
                    foundChannelId = (AntChannelId) ext;
                    //LOG.info(() -> "Found device at Channel: " + ((AntChannelId)ext));
                }
            }

            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            // TODO: Determine when exactly the toggle bit needs to be removed
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);

            if (dataPage == null) {
                return;
            }
            LOG.debug(() -> "Received datapage " + dataPage.toString());
            if (dataPage instanceof DataPage80ManufacturersInformation) {
                DataPage80ManufacturersInformation dp = (DataPage80ManufacturersInformation) dataPage;
                foundModelNumber = dp.getModelNumber();
                foundManufacturerId = dp.getManufacturerId();
            }

            if (dataPage instanceof HrmDataPage3ProductInformation) {
                HrmDataPage3ProductInformation dp = (HrmDataPage3ProductInformation) dataPage;
                foundModelNumber = ByteUtils.toInt(dp.getModelNumber());
                LOG.warn(()-> "Found modelNumber");
            }
            // TODO: We never seem to receive this datapage
            if (dataPage instanceof HrmDataPage2ManufacturerInformation) {
                HrmDataPage2ManufacturerInformation dp = (HrmDataPage2ManufacturerInformation) dataPage;
                foundManufacturerId = dp.getManufacturerId();
                LOG.warn(()-> "Found manufacterId");
            }

            if (dataPage instanceof SpeedCadenceDataPage3ProductInformation) {
                SpeedCadenceDataPage3ProductInformation dp = (SpeedCadenceDataPage3ProductInformation) dataPage;
                foundModelNumber = ByteUtils.toInt(dp.getModelNumber());
            }
            if (dataPage instanceof SpeedCadenceDataPage2ManufacturerInformation) {
                SpeedCadenceDataPage2ManufacturerInformation dp = (SpeedCadenceDataPage2ManufacturerInformation) dataPage;
                foundManufacturerId = dp.getManufacturerId();
            }

            saveNewScanResults(foundChannelId, foundModelNumber, foundManufacturerId);
        } else {
            LOG.warn(()-> String.format("Ignoring message  %s", antMessage));
        }
    }

    private void saveNewScanResults(AntChannelId channelId, Integer modelNumber, Integer manufacturerId) {
        AntScanResult previouslyFound = null;
        for (AntScanResult scanResult : this.scanResults) {
            if (scanResult.getChannelId().equals(channelId)) {
                previouslyFound = scanResult;
            } else {
                LOG.trace(()-> String.format(
                    "Previous channel id: %s it not same as new one: %s",
                    scanResult.getChannelId().toString(),
                    channelId.toString()
                ));
            }
        }

        if (previouslyFound == null) {
            this.scanResults.add(new AntScanResult(channelId, modelNumber, manufacturerId));
        } else {
            boolean changed = false;

            if (previouslyFound.getManufacturerId() == null && manufacturerId != null) {
                LOG.warn(()-> String.format("Updating manufacterId: %d", manufacturerId));
                previouslyFound.setManufacturerId(manufacturerId);
                changed = true;
            }

            if (previouslyFound.getModelNumber() == null && modelNumber != null) {
                LOG.warn(()-> String.format("Updating modelNumber: %d", modelNumber));
                previouslyFound.setModelNumber(modelNumber);
                changed = true;
            }

            // Don't update all of the list if nothing has changed
            if (!changed) {
                return;
            }

            // TODO: Do we need this code?
            scanResults.remove(previouslyFound);
            scanResults.add(previouslyFound);
            //scanResults.setAll(scanResults.sorted());
        }
    }

    private void logScanResult() {
        LOG.info(()-> String.format("Found %d devices:", this.scanResults.size()));
        for (AntScanResult scanResult : this.scanResults) {
            LOG.info(()-> String.format("%s", scanResult));
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
