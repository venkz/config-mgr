package configmgr;

import java.util.HashMap;

public class Domain {

	private String id;
	
	private int highestVersion;
	
	private int synchDate;
	
	private boolean outOfSynch;
	
	private int quiesceTimeout;
	
	private String syncMode;
	
	private HashMap<String, DeploymentPolicy> deploymentPolicies = new HashMap<String, DeploymentPolicy>();
	
	public void addDeploymentPolicy(DeploymentPolicy deploymentPolicy){
		deploymentPolicies.put(deploymentPolicy.getId(), deploymentPolicy);
	}
	
	public HashMap<String, DeploymentPolicy> getDeploymentPolicies(){
		return deploymentPolicies;
	}

	public DeploymentPolicy getDeploymentPolicy(String deploymentPolicyId){
		return deploymentPolicies.get(deploymentPolicyId);
	}
	
	public void printDeploymentPoliciesCount(){
		System.out.println(deploymentPolicies.size());
	}
	
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

	public boolean isOutOfSynch() {
		return outOfSynch;
	}

	public void setOutOfSynch(boolean outOfSynch) {
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
