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
	private Device device;
	private Domain domain;
	private DeploymentPolicy deploymentPolicy;
	private ServiceEndPoint serviceEndPoint;
	private ResultSet rs;
	private Statement stmt;
	private Connection con;

	HashMap<String, Device> devicesCollection = new HashMap<String, Device>();
	HashMap<String, DeploymentPolicy> deploymentPoliciesCollection = new HashMap<String, DeploymentPolicy>();
	HashMap<String, DPManager> dpManagers = new HashMap<String, DPManager>();
	Set<String> devicesUniqueCheck = new HashSet<String>();
	Set<String> policyUniqueCheck = new HashSet<String>();
	Set<String> managedUniqueCheck = new HashSet<String>();

	public ConfigTest() throws SQLException {
		DBConnManager connectionManager = new DBConnManager();
		con = connectionManager.getConnection();
		con.setAutoCommit(true);
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);

		this.deleteAllFromDatabase();
	}

	private void deleteAllFromDatabase() {
		try {
			stmt.executeUpdate("delete from SERVICEENDPOINTTABLE");
			stmt.executeUpdate("delete from DOMAINDEPLOYMENTTABLE");
			stmt.executeUpdate("delete from DEPLOYMENTPOLICY");
			stmt.executeUpdate("delete from DOMAINTABLE");
			stmt.executeUpdate("delete from ManagedSetDevices");
			stmt.executeUpdate("delete from DEVICETABLE");
			stmt.executeUpdate("delete from MANAGEDSETS");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {

		ConfigTest configTest = new ConfigTest();
		configTest.deliverable2();
	}

	public void deliverable2() throws SQLException {

		String cmdString;
		String line;
		String split_args[];
		String fileName = "edge_config3.xml";

		InputStreamReader istr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(istr);

		try {
			while ((line = br.readLine()) != null) {
				cmdString = line;

				split_args = cmdString.split(" ");

				if (split_args.length == 1) {
					fileName = split_args[0];
				} else if (split_args[0].equalsIgnoreCase("end")) {
					break;
				} else {
					System.out.println("Please enter XML file name.");
				}

				if (fileName != null) {
					String xmlFile = fileName;
					// edgeconfig_001U.xml, edge_config3.xml

					this.parseConfigXML(xmlFile);

					this.storeDevices();
					this.storeDomains();
					this.storeDeploymentPolicy();
					this.storeDomainDeploymentTable();
					this.storeManagedSets();
					this.storeManagedSetDevices();

					// configTest.storeServiceEndPoints();

					this.printDevicesCount();
					this.printDeploymentPoliciesCount();
				}

			}
		} catch (IOException e) {

			System.out.println("Error in reading command.");
		}

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
				Device tmpDev = devicesCollection.get(tmpkey);
				tmpDev = null;
			}

			deploymentPoliciesCollection.clear();
			devicesCollection.clear();
			dpManager = new DPManager();
			device = new Device();
			domain = new Domain();
			deploymentPolicy = new DeploymentPolicy();
			serviceEndPoint = new ServiceEndPoint();

			Runtime.getRuntime().gc();

			SAXParser sp = saxParserFactory.newSAXParser();
			sp.parse(xml, configParser);

			dpManager = configParser.getDPManager();

		} catch (Exception ex) {
			System.out
					.println("Error reading the XML file. Please provide valid file.");
			// ex.printStackTrace();
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
		Set<String> domainIdSet = new HashSet<String>();

		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			for (String domain_id : device.getDomains().keySet()) {
				domain = device.getDomain(domain_id);
				try {
					if (domainIdSet.add(domain.getId())) {
						stmt.addBatch("insert into DOMAINTABLE (XMIID,HIGHESTVERSION,SYNCHDATE,OUTOFSYNCH,QUISECETIMEOUT,SYNCMODE,DEVICEID)"
								+ "values('"
								+ domain.getId()
								+ "',"
								+ domain.getHighestVersion()
								+ ","
								+ domain.getSynchDate()
								+ ",'"
								+ domain.isOutOfSynch()
								+ "','"
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
		Set<String> domainIdSet = new HashSet<String>();

		for (String device_id : dpManager.getDevices().keySet()) {
			device = dpManager.getDevice(device_id);
			for (String domain_id : device.getDomains().keySet()) {
				domain = device.getDomain(domain_id);
				for (String policy_id : domain.getDeploymentPolicies().keySet()) {
					deploymentPolicy = domain.getDeploymentPolicy(policy_id);
					try {
						if (domainIdSet.add(domain.getId())) {
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

		for (Device device : devicesCollection.values()) {
			for (Domain domain : device.getDomains().values()) {
				for (DeploymentPolicy deploymendPolicy : domain
						.getDeploymentPolicies().values()) {
					this.deploymentPoliciesCollection.put(
							deploymendPolicy.getId(), deploymendPolicy);
				}
			}
		}
	}

	// Stores the list of Devices in DPmanager
	public void storeDevices() {
		try {
			for (String device_id : dpManager.getDevices().keySet()) {
				device = dpManager.getDevice(device_id);
				try {
					if (devicesUniqueCheck.add(device.getId())) {
						stmt.addBatch("insert into DEVICETABLE (XMIID,DEVICETYPE,GUIPORT,HLMPORT,CURRENTAMPVERSION,QUISECETIMEOUT,FEATURELICENSES)"
								+ "values('"
								+ device.getId()
								+ "','"
								+ device.getDeviceType()
								+ "','"
								+ device.getGuiPort()
								+ "','"
								+ device.getHlmPort()
								+ "','"
								+ device.getCurrentAMPVersion()
								+ "','"
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
		} catch (SQLException e) {
			System.out.println("Error: store Devices.");
			// e.printStackTrace();
		}

	}

	public void storeManagedSets() {
		try {
			ManagedSet managedSet = new ManagedSet();
			for (String managedSetId : dpManager.getManagedSets().keySet()) {
				managedSet = dpManager.getManagedSet(managedSetId);

				stmt.addBatch("insert into MANAGEDSETS (XMIID)" + "values('"
						+ managedSet.getId() + "')");
			}
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e) {
			System.out.println("Error: store Managed Sets.");
			// e.printStackTrace();
		}
	}

	public void storeManagedSetDevices() {
		try {
			ManagedSet managedSet = new ManagedSet();
			for (String managedSetId : dpManager.getManagedSets().keySet()) {
				managedSet = dpManager.getManagedSet(managedSetId);

				for (String deviceMemberId : managedSet.getDeviceMembers()) {
					stmt.addBatch("insert into MANAGEDSETDEVICES (MANAGESETID, DEVICEID)"
							+ "values('"
							+ managedSet.getId()
							+ "','"
							+ deviceMemberId + "')");
				}
			}
			stmt.executeBatch();
			stmt.clearBatch();
		} catch (SQLException e) {
			System.out.println("Error: store Managed Set Devices.");
		}
	}

	public void printDeploymentPoliciesCount() {
		try {
			rs = stmt
					.executeQuery("select count(*) AS number_policies from DeploymentPolicy");
			if (rs.next()) {
				System.out.println(rs.getInt("number_policies"));
			}
		} catch (SQLException e) {
			System.out.println("Error: print Deployment Policies count.");
			// e.printStackTrace();
		}
	}

	public void printDevicesCount() {
		try {
			rs = stmt
					.executeQuery("select count(*) AS number_nodes from DEVICETABLE");
			if (rs.next()) {
				System.out.println(rs.getInt("number_nodes"));
			}
		} catch (SQLException e) {
			System.out.println("Error: print Devices Count.");
			// e.printStackTrace();
		}
	}

}
