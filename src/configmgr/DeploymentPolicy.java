package configmgr;

import java.util.HashMap;

public class DeploymentPolicy {

	private String id;

	private int highestVersion;

	private int synchDate;

	private String policyType;

	private HashMap<String, ServiceEndPoint> serviceEndPoints = new HashMap<String, ServiceEndPoint>();

	public void addServiceEndPoint(ServiceEndPoint serviceEndPoint) {
		this.serviceEndPoints.put(serviceEndPoint.getId(), serviceEndPoint);
	}
	
	public HashMap<String, ServiceEndPoint> getServiceEndPoints(){
		return serviceEndPoints;
	}

	public ServiceEndPoint getServiceEndPoint(String serviceEndPointId){
		return this.serviceEndPoints.get(serviceEndPointId);
		
		/*for(String key : serviceEndPoints.keySet())
		{
			ServiceEndPoint serviceEndPoint = this.serviceEndPoints.get(key);
			if(serviceEndPoint.getId().equalsIgnoreCase(serviceEndPointId)){
				return serviceEndPoint;
			}
		}
		return null;*/
		//return serviceEndPoints.get(serviceEndPointId);
	}
	
	public void printServiceEndPointCount(){
		System.out.println(serviceEndPoints.size());
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

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

}
