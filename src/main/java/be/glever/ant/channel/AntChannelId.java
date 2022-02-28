package be.glever.ant.channel;

import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.util.ByteUtils;
import java.util.Arrays;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AntChannelId {

    private AntChannelTransmissionType transmissionType;
    private byte deviceType; // set by master device, 0 for slave devices. TODO MSB is a pairing bit. split device type from pairing bit?
    private byte[] deviceNumber; // TODO remember this is little endian

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AntChannelId)) {
            return false;
        }
        AntChannelId other = (AntChannelId)obj;
        return transmissionType.equals(other.transmissionType) && deviceType == other.deviceType && Arrays.equals(deviceNumber, other.deviceNumber);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
          append(transmissionType).
          append(deviceType).
          append(deviceNumber).
          toHashCode();
    }

    public AntChannelId(AntChannelTransmissionType transmissionType, AntPlusDeviceType deviceType, byte[] deviceNumber) {
        this.transmissionType = transmissionType;
        this.deviceType = deviceType.value();
        this.deviceNumber = deviceNumber;
    }

    public AntChannelId(AntChannelTransmissionType transmissionType, byte deviceTypeId, byte[] deviceNumber) {
        this.transmissionType = transmissionType;
        this.deviceType = AntPlusDeviceType.valueOf(deviceTypeId).get().value();
        this.deviceNumber = deviceNumber;
    }

    public AntChannelTransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(AntChannelTransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    public AntPlusDeviceType getDeviceType() {
        return AntPlusDeviceType.valueOf(deviceType).get();
    }

    public byte getDeviceTypeId() {
        return deviceType;
    }

    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    public byte[] getDeviceNumber() {
        return deviceNumber;
    }

    public int getIntDeviceNumber() {
        return ByteUtils.toInt(deviceNumber[1], deviceNumber[0]);
    }

    public void setDeviceNumber(byte[] deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String toString() {
        return "AntChannelId{transmissionType=" + transmissionType + ", deviceType=" + deviceType + ", deviceNumber=" + Arrays.toString(deviceNumber) + "}";
    }
}
