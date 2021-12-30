package be.glever.ant.message.configuration;

import java.util.Arrays;
import java.util.Optional;

public enum ExtendedAssignment {
    BACKGROUND_SCANNING_ENABLE((byte) 1),
    FREQUENCY_AGILITY_ENABLE((byte) 4),
    FASTCHANNEL_INITIATION_ENABLE((byte) 16),
    ASYNCHRONOUS_TRANSMISSION_ENABLE((byte) 32);

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
