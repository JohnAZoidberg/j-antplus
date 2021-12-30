package be.glever.ant.channel;

import java.util.Arrays;
import java.util.Optional;

public enum SharedAddressIndicator {
    RESERVED((byte) 0),
    INDEPENDENT((byte) 1),
    SHARED_WITH_1_BYTE_ADDRESS((byte) 2),
    SHARED_WITH_2_BYTE_ADDRESS((byte) 3);

    private byte value;

    private SharedAddressIndicator(byte value) {
        this.value = value;
    }

    public static Optional<SharedAddressIndicator> valueOf(byte value) {
        return Arrays.stream(SharedAddressIndicator.values()).filter(legNo -> legNo.value == value).findFirst();
    }

    public byte value() {
        return this.value;
    }
}
