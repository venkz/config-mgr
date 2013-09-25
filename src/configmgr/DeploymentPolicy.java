package configmgr;

public class DeploymentPolicy {

	private String id;

	private int highestVersion;

	private int synchDate;

	private Boolean outOfSynch;

	private int quiesceTimeout;

	private String syncMode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getHighestVersion() {
		return highestVersion;
	}

	public void setHighestVersion(int highestVersion) {
		this.highestVersion = highestVersion;
	}

	public int getSynchDate() {
		return synchDate;
	}

	public void setSynchDate(int synchDate) {
		this.synchDate = synchDate;
	}

	public Boolean getOutOfSynch() {
		return outOfSynch;
	}

	public void setOutOfSynch(Boolean outOfSynch) {
		this.outOfSynch = outOfSynch;
	}

	public int getQuiesceTimeout() {
		return quiesceTimeout;
	}

	public void setQuiesceTimeout(int quiesceTimeout) {
		this.quiesceTimeout = quiesceTimeout;
	}

	public String getSyncMode() {
		return syncMode;
	}

	public void setSyncMode(String syncMode) {
		this.syncMode = syncMode;
	}

}
