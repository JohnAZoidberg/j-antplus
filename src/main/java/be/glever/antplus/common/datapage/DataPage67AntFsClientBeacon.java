package be.glever.antplus.common.datapage;

import java.util.Arrays;

public class DataPage67AntFsClientBeacon extends AbstractAntPlusDataPage {


	public DataPage67AntFsClientBeacon(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 67;
	}

	public byte getStatusByte1() {
		return super.getDataPageBytes()[1];
	}

	public byte getStatusByte2() {
		return super.getDataPageBytes()[2];
	}

	public byte getAuthenticationType() {
		return super.getDataPageBytes()[3];
	}

	public byte[] getAntFsDeviceDescriptorOrHostSerialNumber() {
		return Arrays.copyOfRange(getDataPageBytes(), 4, 8);
	}
}
