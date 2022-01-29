package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.CalibrationStatus;
import be.glever.antplus.power.CrankLengthStatus;
import be.glever.antplus.power.SensorAvailability;
import be.glever.antplus.power.SensorSwStatus;
import be.glever.antplus.power.datapage.background.PowerDataPage2GetSetParameter;
import be.glever.util.logging.Log;

public class PowerDataPage2Parameter1Crank extends PowerDataPage2GetSetParameter {
    public static final byte SUBPAGE_NR = 0x01;
    private static final Log LOG = Log.getLogger(PowerDataPage2Parameter1Crank.class);

    public PowerDataPage2Parameter1Crank(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getSubPageNumber() {
        return PowerDataPage2Parameter1Crank.SUBPAGE_NR;
    }

    public byte[] createRequestSubPageData(byte crankLength) {
        byte[] subPageBytes = new byte[]{-1, -1, crankLength, 0, 0, -1};
        return subPageBytes;
    }

    public double getCrankLength() {
        int unsigned = this.getSubPageSpecificBytes()[1] & 0xFF;
        if (unsigned == 255) {
            return -1.0;
        }
        return (double)unsigned / 2.0 + 110.0;
    }

    private byte getSensorStatusByte() {
        return this.getSubPageSpecificBytes()[2];
    }

    public CrankLengthStatus getCrankLengthStatus() {
        byte statusByte = this.getSensorStatusByte();
        if (ByteUtils.hasMask(statusByte, 0)) {
            return CrankLengthStatus.INVALID;
        }
        if (ByteUtils.hasMask(statusByte, 1)) {
            return CrankLengthStatus.DEFAULT;
        }
        if (ByteUtils.hasMask(statusByte, 2)) {
            return CrankLengthStatus.MANUAL;
        }
        if (ByteUtils.hasMask(statusByte, 3)) {
            return CrankLengthStatus.AUTO_OR_FIXED;
        }
        throw new IllegalStateException("Can never occur");
    }

    public SensorSwStatus getSensorSwMismatchStatus() {
        byte statusByte = this.getSensorStatusByte();
        if (ByteUtils.hasMask(statusByte, 0)) {
            return SensorSwStatus.UNDEFINED;
        }
        if (ByteUtils.hasMask(statusByte, 4)) {
            return SensorSwStatus.RIGHT_OLDER;
        }
        if (ByteUtils.hasMask(statusByte, 8)) {
            return SensorSwStatus.LEFT_OLDER;
        }
        if (ByteUtils.hasMask(statusByte, 12)) {
            return SensorSwStatus.SAME_SW;
        }
        throw new IllegalStateException("Can never occur");
    }

    public SensorAvailability getSensorAvailabilityStatus() {
        byte statusByte = this.getSensorStatusByte();
        if (ByteUtils.hasMask(statusByte, 0)) {
            return SensorAvailability.UNDEFINED;
        }
        if (ByteUtils.hasMask(statusByte, 16)) {
            return SensorAvailability.ONLY_LEFT_PRESENT;
        }
        if (ByteUtils.hasMask(statusByte, 32)) {
            return SensorAvailability.ONLY_RIGHT_PRESENT;
        }
        if (ByteUtils.hasMask(statusByte, 48)) {
            return SensorAvailability.BOTH_PRESENT;
        }
        throw new IllegalStateException("Can never occur");
    }

    public CalibrationStatus getCustomCalibrationStatus() {
        byte statusByte = this.getSensorStatusByte();
        if (ByteUtils.hasMask(statusByte, 0)) {
            return CalibrationStatus.UNSUPPORTED;
        }
        if (ByteUtils.hasMask(statusByte, 64)) {
            return CalibrationStatus.NOT_REQUIRED;
        }
        if (ByteUtils.hasMask(statusByte, 128)) {
            return CalibrationStatus.REQUIRED;
        }
        if (ByteUtils.hasMask(statusByte, 192)) {
            LOG.warn(() -> "Custom Calibration Status is set to 0x11 which is reserved");
            return CalibrationStatus.UNSUPPORTED;
        }
        throw new IllegalStateException("Can never occur");
    }

    public boolean getAutoCrankLengthCapability() {
        return ByteUtils.hasBitSet(this.getSubPageSpecificBytes()[3], 0);
    }
}
