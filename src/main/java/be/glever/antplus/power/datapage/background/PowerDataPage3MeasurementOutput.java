package be.glever.antplus.power.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.power.Unit;
import java.util.Optional;

public class PowerDataPage3MeasurementOutput extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 0x03;

    public PowerDataPage3MeasurementOutput(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PowerDataPage3MeasurementOutput.PAGE_NR;
    }

    public int getNumberOfDataTypes() {
        int numberOfDataTypes = this.getDataPageBytes()[0] & 0xF;
        if (numberOfDataTypes > 11) {
            throw new IllegalStateException("Sensor returned numberOfDataTypes not in range (1-11)");
        }
        return numberOfDataTypes;
    }

    public Optional<Unit> getDataType() {
        return Unit.valueOf(this.getDataPageBytes()[1]);
    }

    public int getScaleFactor() {
        return this.getDataPageBytes()[2];
    }

    public int getTimeStamp() {
        return ByteUtils.fromUShort(this.getDataPageBytes()[3], this.getDataPageBytes()[4]);
    }

    public int getMeasurement() {
        return ByteUtils.fromUShort(this.getDataPageBytes()[5], this.getDataPageBytes()[5]);
    }

    public double getScaledMeasurement() {
        return Math.pow(2.0, this.getScaleFactor()) * (double)this.getMeasurement();
    }
}
