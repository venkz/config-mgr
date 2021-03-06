package configmgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ConfigTest {

	private DPManager dpManager;
	private Devices device;
	private Domains domain;
	private DeploymentPolicy deploymentPolicy;
	private ServiceEndPoint serviceEndPoint;
	private ResultSet rs;
	private Statement stmt;
	private Connection con;

	public ConfigTest() throws SQLException {
		DataLoadRun dataLoadRun = new DataLoadRun();
		con = dataLoadRun.init();
		con.setAutoCommit(true);
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		stmt.executeUpdate("delete from SERVICEENDPOINTTABLE");
		stmt.executeUpdate("delete from DOMAINDEPLOYMENTTABLE");
		stmt.executeUpdate("delete from DEPLOYMENTPOLICY");
		stmt.executeUpdate("delete from DOMAINTABLE");
		stmt.executeUpdate("delete from ManagedSetsDeviceMapping");
		stmt.executeUpdate("delete from DEVICETABLE");
		stmt.executeUpdate("delete from MANAGEDSETS");
	}

	HashMap<String, Devices> devicesCollection = new HashMap<String, Devices>();
	HashMap<String, DeploymentPolicy> deploymentPoliciesCollection = new HashMap<String, DeploymentPolicy>();
	HashMap<String, DPManager> dpManagers = new HashMap<String, DPManager>();
	Set<String> devicesUniqueCheck = new HashSet<String>();
	Set<String> policyUniqueCheck = new HashSet<String>();
	Set<String> managedUniqueCheck = new HashSet<String>();

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		ConfigTest configTest = new ConfigTest();
		String xmlFile = "edge_config3.xml"; // edgeconfig_001U.xml
												// //edge_config3.xml
		configTest.parseConfigXML(xmlFile);

		configTest.storeDPDevices();
		// configTest.storeDPManagedSets(); // NEEDS ATTENTION HERE!!!! DB
		// MODELLING NEEDED.
		// configTest.storeManagedSetsDeviceMapping();
		configTest.storeDomains();
		configTest.storeDeploymentPolicy();
		configTest.storeDomainDeploymentTable();
		//configTest.storeServiceEndPoints();
		configTest.printNumberOfDevices();
		configTest.printNumberDeploymentPolicies();

	}

	public String getCmdLine() {

		InputStreamReader istr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(istr);
		try {
			String cmdString = br.readLine();
			// System.out.println(cmdString);
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

			// Memory overflow bug: Free existing memory space
			for (String tmpkey : deploymentPoliciesCollection.keySet()) {
				DeploymentPolicy tmpDP = deploymentPoliciesCollection
						.get(tmpkey);
				tmpDP = null;
			}

			for (String tmpkey : devicesCollection.keySet()) {
				Devices tmpDev = devicesCollection.get(tmpkey);
				tmpDev = null;
			}

			deploymentPoliciesCollection.clear();
			devicesCollection.clear();
			dpManager = new DPManager();
			device = new Devices();
			domain = new Domains();
			deploymentPolicy = new DeploymentPolicy();
			serviceEndPoint = new ServiceEndPoint();

			Runtime.getRuntime().gc();

			SAXParser sp = saxParserFactory.newSAXParser();
			sp.parse(xml, configParser);

			dpManager = configParser.getDPManager();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		dpManagers = configParser.getDpManagers();
	}

	// Prints number of Devices in DPmanager

	public void printDPDeviceCount() {
		// System.out.println("No. of DP Devices:");
		dpManager.printDPDeviceCount();
	}

	// Stores the number of domains in DPDevice

	public void storeDomains() throws SQLException {
		Set<String> domainsUniqueCheck = new HashSet<String>();
		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			for (String domain_id : device.getDomains().keySet()) {
				domain = device.getDomain(domain_id);
				try {
					if (domainsUniqueCheck.add(domain.getId())) {
						stmt.addBatch("insert into DOMAINTABLE (XMIID,HIGHESTVERSION,SYNCHDATE,OUTOFSYNCH,QUISECETIMEOUT,SYNCMODE,DEVICEID)"
								+ "values('"
								+ domain.getId()
								+ "',"
								+ domain.getHighestVersion()
								+ ","
								+ domain.getSynchDate()
								+ ",'"
								+ domain.isOutOfSynch()
								+ "'"
								+ ",'"
								+ domain.getQuiesceTimeout()
								+ "','"
								+ domain.getSyncMode()
								+ "','"
								+ device_id
								+ "')");
					}

				} catch (SQLException e) {
					continue;
				}
			}
		}
		stmt.executeBatch();
		stmt.clearBatch();
	}

	public void storeDeploymentPolicy() {
		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			for (String domain_id : device.getDomains().keySet()) {
				domain = device.getDomain(domain_id);
				for (String policy_id : domain.getDeploymentPolicies().keySet()) {
					deploymentPolicy = domain.getDeploymentPolicy(policy_id);
					try {
						if (policyUniqueCheck.add(deploymentPolicy.getId())) {
							stmt.addBatch("insert into DEPLOYMENTPOLICY (XMIID,HIGHESTVERSION,SYNCHDATE,POLICYTYPE)"
									+ "values('"
									+ deploymentPolicy.getId()
									+ "',"
									+ deploymentPolicy.getHighestVersion()
									+ ","
									+ deploymentPolicy.getSynchDate()
									+ ",'"
									+ deploymentPolicy.getPolicyType()
									+ "')");
						}
					} catch (SQLException e) {
						continue;
					}
				}
			}

		}
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e) {
		}
	}

	public void storeDomainDeploymentTable() {
		Set<String> domainUniqueCheck = new HashSet<String>();
		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			for (String domain_id : device.getDomains().keySet()) {
				domain = device.getDomain(domain_id);
				for (String policy_id : domain.getDeploymentPolicies().keySet()) {
					deploymentPolicy = domain.getDeploymentPolicy(policy_id);
					try {
						if (domainUniqueCheck.add(domain.getId())) {
							stmt.addBatch("insert into DOMAINDEPLOYMENTTABLE (DOMAINID,DEPLOYMENTID)"
									+ "values('"
									+ domain.getId()
									+ "','"
									+ deploymentPolicy.getId() + "')");
						}
					} catch (SQLException e) {
						continue;
					}
				}

			}
		}
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (Exception e) {
		}
	}

	public void storeServiceEndPoints() {
		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			for (String domain_id : device.getDomains().keySet()) {
				domain = device.getDomain(domain_id);
				for (String policy_id : policyUniqueCheck) {
					deploymentPolicy = domain.getDeploymentPolicy(policy_id);
					if (deploymentPolicy != null) {
						for (String service_id : deploymentPolicy
								.getServiceEndPoints().keySet()) {
							serviceEndPoint = deploymentPolicy
									.getServiceEndPoint(service_id);
							try {
								stmt.addBatch("insert into ServiceEndpointTable (XMIID,TYPE,OPERATION,PORT,TARGETSERVER,DPPOLICYID)"
										+ "values('"
										+ serviceEndPoint.getId()
										+ "','"
										+ serviceEndPoint.getType()
										+ "','"
										+ serviceEndPoint.getOperation()
										+ "',"
										+ serviceEndPoint.getPort()
										+ ""
										+ ",'"
										+ serviceEndPoint.getTargetServer()
										+ "','" + policy_id + "')");
							} catch (SQLException e) {
								continue;
							}
						}
					}

				}
			}
		}
		try {
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Prints all attributes of a ServicePoint with serviceEndPointId of
	// DeploymentPolicy - deploymentPolicyId

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
				// System.out.print(this.serviceEndPoint.toString());
				this.serviceEndPoint.printAllAttributes();
			}
		}
	}

	// Scans all devices and domains under those devices. Then iterates all
	// domains to add all deployment policy under them to a class variable

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

	// Stores the list of Devices in DPmanager
	public void storeDPDevices() throws SQLException {
		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			try {
				if (devicesUniqueCheck.add(device.getId())) {
					stmt.addBatch("insert into DEVICETABLE (XMIID,DEVICETYPE,HLMPORT,CURRENTAMPVERSION,QUISECETIMEOUT,FEATURELICENSES)"
							+ "values('"
							+ device.getId()
							+ "','"
							+ device.getDeviceType()
							+ "','"
							+ device.getHlmPort()
							+ "','"
							+ device.getCurrentAMPVersion()
							+ "'"
							+ ",'"
							+ device.getQuiesceTimeout()
							+ "','"
							+ device.getFeatureLicenses() + "')");
				}
			} catch (SQLException e) {
				continue;
			}
		}
		stmt.executeBatch();
		stmt.clearBatch();
	}

	// NEEDS ATTENTION HERE!!!! DB MODELLING NEEDED.

	public void storeDPManagedSets() throws SQLException {
		for (String managedSet_id : dpManagers.keySet()) {
			dpManager = dpManagers.get(managedSet_id);
			if (managedUniqueCheck.add(managedSet_id)) {
				stmt.executeQuery("insert into MANAGEDSETS (XMIID)"
						+ "values('" + dpManager.getId() + "')");
			}
		}
	}

	public void storeManagedSetsDeviceMapping() throws SQLException {
		for (String managedSet_id : managedUniqueCheck) {
			dpManager = dpManagers.get(managedSet_id);
			for (String device_id : dpManager.getDevices().keySet()) {
				device = dpManager.getDevice(device_id);
				if (devicesUniqueCheck.contains(device_id)) {
					stmt.executeQuery("insert into ManagedSetsDeviceMapping (manageSetId,DEVICEMEMBERS)"
							+ "values('"
							+ managedSet_id
							+ "','"
							+ device.getId() + "')");
				}
			}

		}
	}

	public void printNumberDeploymentPolicies() throws SQLException {
		rs = stmt
				.executeQuery("select count(*) AS number_policies from DeploymentPolicy");
		if (rs.next()) {
			System.out.println(rs.getInt("number_policies"));
		}
	}

	public void printNumberOfDevices() throws SQLException {
		rs = stmt
				.executeQuery("select count(*) AS number_nodes from DEVICETABLE");
		if (rs.next()) {
			System.out.println(rs.getInt("number_nodes"));
		}
	}

}
