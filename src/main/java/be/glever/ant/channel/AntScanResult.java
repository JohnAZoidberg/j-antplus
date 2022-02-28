package be.glever.ant.channel;

import be.glever.ant.channel.AntChannelId;

public class AntScanResult {

    private AntChannelId channelId;
    private Integer manufacturerId;
    private Integer modelNumber;

    public AntScanResult(AntChannelId channelId) {
        this(channelId, null, null);
    }

    public AntScanResult(AntChannelId channelId, Integer manufacturerId, Integer modelNumber) {
        this.channelId = channelId;
        this.manufacturerId = manufacturerId;
        this.modelNumber = modelNumber;
    }

    public String getDeviceTypeName() {
        return channelId.getDeviceType().toString();
    }

    @Override
    public String toString() {
        if (manufacturerId == null && modelNumber != null) {
            return String.format(
                "%s (ID: %d) - Model Number: %d)",
                getDeviceTypeName(),
                channelId.getIntDeviceNumber(),
                modelNumber
            );
        } else if (manufacturerId != null && modelNumber == null) {
            return String.format(
                "%s (ID: %d) - by %d)",
                getDeviceTypeName(),
                channelId.getIntDeviceNumber(),
                manufacturerId
            );
        } else {
            return String.format("%s(ID: %d)", getDeviceTypeName(), channelId.getIntDeviceNumber());
        }
    }

    public AntChannelId getChannelId() {
        return channelId;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public Integer getModelNumber() {
        return modelNumber;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public void setModelNumber(Integer modelNumber) {
        this.modelNumber = modelNumber;
    }
}
