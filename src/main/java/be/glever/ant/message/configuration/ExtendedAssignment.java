package be.glever.ant.message.configuration;

import java.util.Arrays;
import java.util.Optional;

// Extended Assignment is not supported by all devices
public enum ExtendedAssignment {
    NO_EXTENDED_ASSIGNMENT((byte) 0x00),
    BACKGROUND_SCANNING_ENABLE((byte) 0x01),
    FREQUENCY_AGILITY_ENABLE((byte) 0x04),
    FASTCHANNEL_INITIATION_ENABLE((byte) 0x10),
    ASYNCHRONOUS_TRANSMISSION_ENABLE((byte) 0x20);

    private byte value;

    private ExtendedAssignment(byte value) {
        this.value = value;
    }

    public static Optional<ExtendedAssignment> valueOf(int value) {
        return Arrays.stream(ExtendedAssignment.values()).filter(legNo -> legNo.value == value).findFirst();
    }

    public byte value() {
        return this.value;
    }
}
