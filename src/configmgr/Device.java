package configmgr;

import java.util.HashMap;

public class Devices {

	private String id;

	private String deviceType;

	private int guiPort;

	private int hlmPort;

	private double currentAMPVersion;

	private int quiesceTimeout;

	private String featureLicenses;

	private HashMap<String, Domains> domains = new HashMap<String, Domains>();
	
	public void addDomain(Domains domain){
		domains.put(domain.getId(), domain);
	}
	
	public HashMap<String, Domains> getDomains(){
		return domains;
	}
	
	public Domains getDomain(String domainId){
		return domains.get(domainId);
	}
	
	public void printDomainsCount(){
		System.out.println(domains.size());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getGuiPort() {
		return guiPort;
	}

	public void setGuiPort(int guiPort) {
		this.guiPort = guiPort;
	}

	public int getHlmPort() {
		return hlmPort;
	}

	public void setHlmPort(int hlmPort) {
		this.hlmPort = hlmPort;
	}

	public double getCurrentAMPVersion() {
		return currentAMPVersion;
	}

	public void setCurrentAMPVersion(double currentAMPVersion) {
		this.currentAMPVersion = currentAMPVersion;
	}

	public int getQuiesceTimeout() {
		return quiesceTimeout;
	}

	public void setQuiesceTimeout(int quiesceTimeout) {
		this.quiesceTimeout = quiesceTimeout;
	}

	public String getFeatureLicenses() {
		return featureLicenses;
	}

	public void setFeatureLicenses(String featureLicenses) {
		this.featureLicenses = featureLicenses;
	}
}
