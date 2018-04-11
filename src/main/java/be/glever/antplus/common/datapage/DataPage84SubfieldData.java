package be.glever.antplus.common.datapage;

public class DataPage84SubfieldData extends AbstractAntPlusDataPage {
	public DataPage84SubfieldData(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 84;
	}


	public byte getSubPage1() {
		return getDataPageBytes()[2];
	}

	public byte getSubPage2() {
		return getDataPageBytes()[3];
	}

	public byte[] getDataField1() {
		return super.dataPageSubArray(4, 6);
	}

	public byte[] getDataField2() {
		return super.dataPageSubArray(6, 8);
	}

	// TODO implement subpages as per spec 6-17
}
