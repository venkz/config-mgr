package configmgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConfigTest {

	private DPManager dpManager;
	private Devices device;
	private Domains domain;
	private DeploymentPolicy deploymentPolicy;
	private ServiceEndPoint serviceEndPoint;

	HashMap<String, Devices> devicesCollection = new HashMap<String, Devices>();
	// HashMap<String, Domains> allDomainsCollection = new HashMap<String,
	// Domains>();
	HashMap<String, DeploymentPolicy> deploymentPoliciesCollection = new HashMap<String, DeploymentPolicy>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		ConfigTest configTest = new ConfigTest();
		configTest.parseConfigXML();

		configTest.printDPDeviceCount();
		configTest.printDomainsCount("DPDevice_0");
		configTest.printDeploymentPolicyCount("DPDevice_3", "DPDomain_3_0");
		configTest.printServiceEndPointsCount("DPPolicy_3");
		configTest.printServiceEndPointAttributes("DPPolicy_0","SvcEndpoint_0_2");
		
		*/
		
		String split_args[];
		// TODO Auto-generated method stub
		ConfigTest tmain = new ConfigTest();
		
		String read_command = tmain.getCmdLine();
		while(!read_command.equalsIgnoreCase("end"))
		{
			
			split_args = read_command.split(" ");
			//System.out.println(split_args[0]);
			
			if(split_args[0].equalsIgnoreCase("Configuration") )
			{
				tmain.parseConfigXML(split_args[1]);				
			}
			else if (split_args[0].equalsIgnoreCase("DPDevice") && split_args.length==1)
			{
				tmain.printDPDeviceCount();
				
			}
			else if(split_args[0].equalsIgnoreCase("DPDevice") && split_args[2].equalsIgnoreCase("DPDomain") && split_args.length==3)
			{
				
			tmain.printDomainsCount(split_args[1]);	
				
				
			}
			else if(split_args[0].equalsIgnoreCase("DPDevice") && split_args[2].equalsIgnoreCase("DPDomain") && split_args[4].equalsIgnoreCase("DeploymentPolicy") && split_args.length==5)
			{
				
				
				tmain.printDeploymentPolicyCount(split_args[1], split_args[3]);
			}
			else if(split_args[0].equalsIgnoreCase("DeploymentPolicy") && split_args[2].equalsIgnoreCase("Serviceendpoint") && split_args.length==3)
			{
				
				tmain.printServiceEndPointsCount(split_args[1]);
			}
			else if(split_args[0].equalsIgnoreCase("DeploymentPolicy") && split_args[2].equalsIgnoreCase("Serviceendpoint") && split_args.length==4)
			{
				
				tmain.printServiceEndPointAttributes(split_args[1],split_args[3]);
				
			}
			else
			{
				System.out.println("Wrong arguments...Exiting");
				break;
				
			}
			
			read_command = tmain.getCmdLine();
		}
		
		
		
		
	}

	public String getCmdLine()
	{
		
		InputStreamReader istr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(istr);
		try {
			String cmdString = br.readLine();
			//System.out.println(cmdString);
			return cmdString;
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void parseConfigXML(String xml) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		ConfigXMLParser configParser = new ConfigXMLParser();

		try {
			SAXParser sp = saxParserFactory.newSAXParser();
			sp.parse(xml, configParser);
			dpManager = configParser.getDPManager();
			//System.out.println("XML Parse complete");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * / Prints number of Devices in DPmanager
	 */
	public void printDPDeviceCount() {
		//System.out.println("No. of DP Devices:");
		dpManager.printDPDeviceCount();
	}

	/*
	 * / Prints the number of domains in DPDevice
	 */
	public void printDomainsCount(String deviceId) {
		// First get the required device from DPManager
		device = dpManager.getDevice(deviceId);

		if (device != null) {
			//System.out.println("No. of Domains in " + deviceId);
			device.printDomainsCount();
		}
	}

	public void printDeploymentPolicyCount(String deviceId, String domainId) {
		// First get the required device from DPManager
		device = dpManager.getDevice(deviceId);

		if (device != null) {
			domain = device.getDomain(domainId);
			if (domain != null) {
				//System.out.println("No. of Deployment Policies in " + deviceId
				//		+ "," + domainId);
				domain.printDeploymentPoliciesCount();
			}
		}
	}

	public void printServiceEndPointsCount(String deploymentPolicyId) {
		// Check if the local copy of all deployment policy is populated or not
		// In case not then scan all nodes again
		if (this.deploymentPoliciesCollection.size() == 0) {
			this.fetchAllDeploymentPolicies();
		}

		deploymentPolicy = deploymentPoliciesCollection.get(deploymentPolicyId);

		if (deploymentPolicy != null) {
			//System.out.println("No. of Service Endpoints ");
			deploymentPolicy.printServiceEndPointCount();
		}
	}

	/*
	 * / Prints all attributes of a ServicePoint with serviceEndPointId of
	 * DeploymentPolicy - deploymentPolicyId
	 */
	public void printServiceEndPointAttributes(String deploymentPolicyId,
			String serviceEndPointId) {

		// Check if the local copy of all deployment policy is populated or not
		// In case not then scan all nodes again
		if (this.deploymentPoliciesCollection.size() == 0) {
			this.fetchAllDeploymentPolicies();
		}

		// First get the required Deployment Policy from all deployment policies
		// collection
		deploymentPolicy = this.deploymentPoliciesCollection
				.get(deploymentPolicyId);

		if (deploymentPolicy != null) {
			this.serviceEndPoint = this.deploymentPolicy
					.getServiceEndPoint(serviceEndPointId);

			if (this.serviceEndPoint != null) {
				// Print all attributes of Service End Point.
				//System.out.print(this.serviceEndPoint.toString());
				this.serviceEndPoint.printAllAttributes();
			}
		}
	}

	/*
	 * / Scans all devices and domains under those devices. Then iterates all
	 * domains to add all deployment policy under them to a class variable
	 */
	private void fetchAllDeploymentPolicies() {
		// For getting the required Deployment Policy we first need to
		// loop through all devices then through all domains under those devices
		// till the time we did not find the required policy
		// Loop through all domains and add them to all domain collection

		devicesCollection = dpManager.getDevices();

		for (Devices device : devicesCollection.values()) {
			for (Domains domain : device.getDomains().values()) {
				for (DeploymentPolicy deploymendPolicy : domain
						.getDeploymentPolicies().values()) {
					this.deploymentPoliciesCollection.put(
							deploymendPolicy.getId(), deploymendPolicy);
				}
			}
		}
	}	
}
