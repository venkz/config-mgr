package configmgr;

import java.util.HashSet;
import java.util.Set;

public class ManagedSet {

	private String id;

	private Set<String> deviceMembers = new HashSet<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getDeviceMembers() {
		return this.deviceMembers;
	}

	public void addDeviceMember(String deviceId) {
		this.deviceMembers.add(deviceId);
	}

}
