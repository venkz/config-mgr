package configmgr;

import java.util.HashMap;

public class DPManager {

	private String id;

	private String dat;

	private String xmi;

	private double version;

	private int versionsStoredLimit;

	private HashMap<String, Device> devices = new HashMap<String, Device>();

	private HashMap<String, ManagedSet> managedSets = new HashMap<String, ManagedSet>();

	public HashMap<String, ManagedSet> getManagedSets() {
		return this.managedSets;
	}
	
	public void addManagedSet(ManagedSet managedSet) {
		managedSets.put(managedSet.getId(), managedSet);
	}

	public ManagedSet getManagedSet(String managedSetId){
		return this.managedSets.get(managedSetId);
	}

	public void addDevice(Device device) {
		devices.put(device.getId(), device);
	}

	/*
	 * / Returns all devices in DPManager
	 */
	public HashMap<String, Device> getDevices() {
		return devices;
	}

	public Device getDevice(String deviceId) {
		return devices.get(deviceId);
	}

	public void printDPDeviceCount() {
		System.out.println(devices.size());
	}

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
		return versionsStoredLimit;
	}

	public void setVersionsStoredLimit(int versionsStoredLimit) {
		this.versionsStoredLimit = versionsStoredLimit;
	}
}
