package be.glever.ant.channel;

import be.glever.ant.channel.SharedAddressIndicator;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AntChannelTransmissionType {
    private byte value;

    /**
     * Use this one for searching devices. It is the Master device that decides the TransmissionType, which slaves must use when opening a channel.
     */
    public static final AntChannelTransmissionType PAIRING_TRANSMISSION_TYPE = new AntChannelTransmissionType((byte) 0, (byte) 0, (byte) 0);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AntChannelTransmissionType)) {
            return false;
        }
        AntChannelTransmissionType other = (AntChannelTransmissionType)obj;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
          append(value).
          toHashCode();
    }

    public AntChannelTransmissionType(byte value) {
        this.value = value;
    }


    public AntChannelTransmissionType(byte sharedAddressIndicator, byte globalDataPagesUsage, byte deviceNumberExtension) {
        sharedAddressIndicator = (byte) (0b11 & sharedAddressIndicator);
        globalDataPagesUsage = (byte) (0b100 & (globalDataPagesUsage << 2));
        // 4th bit unused
        deviceNumberExtension = (byte) (deviceNumberExtension << 4);

        this.value = (byte) (sharedAddressIndicator | globalDataPagesUsage | deviceNumberExtension);
    }

    public SharedAddressIndicator getSharedAddressIndicator() {
        return SharedAddressIndicator.valueOf((byte)(this.value & 0b11)).get();
    }

    public byte getGlobalDataPagesUsage() {
        return (byte) ((value & 0b100) >> 2);
    }

    public byte getOptionalDeviceNumberExtension() {
        return (byte) (value >> 4);
    }

    public byte getValue() {
        return value;
    }
}
