package be.glever.ant.message;

public class Rssi {
    private byte value;
    private byte thresholdConfiguration;

    public Rssi(byte value, byte thresholdConfiguration) {
        this.value = value;
        this.thresholdConfiguration = thresholdConfiguration;
    }

    public byte getValue() {
        return this.value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public byte getThresholdConfiguration() {
        return this.thresholdConfiguration;
    }

    public void setThresholdConfiguration(byte thresholdConfiguration) {
        this.thresholdConfiguration = thresholdConfiguration;
    }
}

