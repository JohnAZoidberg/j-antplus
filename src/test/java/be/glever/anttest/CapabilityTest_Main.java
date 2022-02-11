package be.glever.anttest;

import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.ant.usb.AntUsbDeviceCapabilities;
import be.glever.util.logging.Log;
import be.glever.ant.util.ByteUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CapabilityTest_Main {
    private static final Log LOG = Log.getLogger(CapabilityTest_Main.class);

    private CapabilityTest_Main() throws IOException {

        try (AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // Channels stay open on usb dongle even if program shuts down.

            AntUsbDeviceCapabilities caps = device.getCapabilities();
            byte[] antVersion = device.getAntVersion();
            LOG.info(() -> "Transceiver supports ANT version: " + new String(antVersion, StandardCharsets.UTF_8));
            LOG.info(() -> "Transceiver has serial number:    " + ByteUtils.hexString(device.getSerialNumber()));
            LOG.info(() -> "Standard capabilities:");
            LOG.info(() -> "  Total number of ANT Channels:   " + caps.getMaxChannels());
            LOG.info(() -> "  Receive Channels:               " + caps.getReceiveChannels());
            LOG.info(() -> "  Transmit Channels:              " + caps.getTransmitChannels());
            LOG.info(() -> "  Receive Messages:               " + caps.getReceiveMessages());
            LOG.info(() -> "  Transmit Messages:              " + caps.getTransmitMessages());
            LOG.info(() -> "  Ackd messages:                  " + caps.getAckdMessages());
            LOG.info(() -> "  Burst messages:                 " + caps.getBurstMessages());
            LOG.info(() -> "Advanced capabilities:");
            LOG.info(() -> "  Network Enabled:                " + caps.getNetworkEnabled());
            LOG.info(() -> "  Serial Number Enabled:          " + caps.getSerialNumberEnabled());
            LOG.info(() -> "  Per Channel TX Power Enabled:   " + caps.getPerChannelTxPowerEnabled());
            LOG.info(() -> "  Low-Priority Serarch Enabled:   " + caps.getLowPrioritySearchEnabled());
            LOG.info(() -> "  Scipt Enabled:                  " + caps.getScriptEnabled());
            LOG.info(() -> "  Search List Enabled:            " + caps.getSearchListEnabled());
            LOG.info(() -> "Advanced capabilities2:");
            LOG.info(() -> "  LED Enabled:                    " + caps.getLedEnabled());
            LOG.info(() -> "  Ext Message Enabled:            " + caps.getExtMessageEnabled());
            LOG.info(() -> "  Scan Mode Enabled:              " + caps.getScanModeEnabled());
            LOG.info(() -> "  Proximity Search Enabled:       " + caps.getProxSearchEnabled());
            LOG.info(() -> "  Extended Assign Enabled:        " + caps.getExtAssignEnabled());
            LOG.info(() -> "  FS ANTFS Enabled:               " + caps.getFsAntFsEnabled());
            LOG.info(() -> "  FIT 1 Enabled:                  " + caps.getFit1Enabled());
            LOG.info(() -> "Total SensRcore channels:         " + caps.getMaxSensRcoreChannels());
            LOG.info(() -> "Advanced capabilities3:");
            LOG.info(() -> "  Advanced Burst Enabled:         " + caps.getAdvancedBurstEnabled());
            LOG.info(() -> "  Event Buffering Enabled:        " + caps.getEventBufferingEnabled());
            LOG.info(() -> "  Event Filtering Enabled:        " + caps.getEventFilteringEnabled());
            LOG.info(() -> "  High Duty Search Enabled:       " + caps.getHighDutySearchEnabled());
            LOG.info(() -> "  Search Sharing Enabled:         " + caps.getSearchSharingEnabled());
            LOG.info(() -> "  Selective Data Updates Enabled: " + caps.getSelectiveDataUpdatesEnabled());
            LOG.info(() -> "  Encrypted Channel Enabled:      " + caps.getEncryptedChannelEnabled());
            LOG.info(() -> "Advanced capabilities4:");
            LOG.info(() -> "  RF Active Notification Enabled: " + caps.getRfActiveNotificationEnabled());
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
        new CapabilityTest_Main();
    }
}
