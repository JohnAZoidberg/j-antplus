package be.glever.antplus.power;

import java.util.Arrays;
import java.util.Optional;

public enum Unit {
    COUNTDOWN_PROGRESS((byte) 0),
    COUNTDOWN_TIME((byte) 1),
    TORQUE_BOTH((byte) 8),
    TORQUE_LEFT((byte) 9),
    TORQUE_RIGHT((byte) 10),
    TORQUE_Y_AXIS((byte) 11),
    TORQUE_X_AXIS((byte) 12),
    FORCE_BOTH((byte) 16),
    FORCE_LEFT((byte) 17),
    FORCE_RIGHT((byte) 18),
    CRANK_ANGLE_BOTH((byte) 20),
    CRANK_ANGLE_LEFT((byte) 21),
    CRANK_ANGLE_RIGHT((byte) 22),
    ZERO_OFFSET((byte) 24),
    TEMPERATURE((byte) 25),
    VOLTAGE((byte) 26),
    FORCE_FORWARD_LEFT((byte) 32),
    FORCE_FORWARD_RIGHT((byte) 33),
    FORCE_DOWNWARD_LEFT((byte) 34),
    FORCE_DOWNWARD_RIGHT((byte) 35),
    PEDAL_ANGLE_LEFT((byte) 40),
    PEDAL_ANGLE_RIGHT((byte) 41);

    private byte value;

    private Unit(byte value) {
        this.value = value;
    }

    public static Optional<Unit> valueOf(int value) {
        return Arrays.stream(Unit.values()).filter(legNo -> legNo.value == value).findFirst();
    }

    public byte value() {
        return this.value;
    }
}
