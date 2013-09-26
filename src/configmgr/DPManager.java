package configmgr;

public class DPManager {

	private String id;

	private String dat;
	
	private String xmi;
	
	private double version;
	
	private int VersionsStoredLimit;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDat() {
		return dat;
	}

	public void setDat(String dat) {
		this.dat = dat;
	}

	public String getXmi() {
		return xmi;
	}

	public void setXmi(String xmi) {
		this.xmi = xmi;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public int getVersionsStoredLimit() {
		return VersionsStoredLimit;
	}

	public void setVersionsStoredLimit(int versionsStoredLimit) {
		VersionsStoredLimit = versionsStoredLimit;
	}
}
