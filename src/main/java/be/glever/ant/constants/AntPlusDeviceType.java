package be.glever.ant.constants;

import java.util.Arrays;
import java.util.Optional;

public enum AntPlusDeviceType {
    Any((byte) 0x0),
    Fec((byte) 0x11),
    Power((byte) 0x0B),
    HRM((byte) 0x78),
    SpeedCadence((byte) 0x79),
    Cadence((byte) 0x7A),
    Speed((byte) 0x7B);

    private byte value;

    private AntPlusDeviceType(byte value) {
        this.value = value;
    }

    public static Optional<AntPlusDeviceType> valueOf(int value) {
        return Arrays.stream(AntPlusDeviceType.values()).filter(legNo -> legNo.value == value).findFirst();
    }

    public byte value() {
        return value;
    }
}
