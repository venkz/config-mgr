package configmgr;

import java.util.HashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigXMLParser extends DefaultHandler {

	private final Stack<String> tagsStack = new Stack<String>();
	private final StringBuilder tempVal = new StringBuilder();

	private HashMap<String, DPManager> dpManagers;
	private HashMap<String, ServiceEndPoint> serviceEndPointCollection = new HashMap<String, ServiceEndPoint>();

	public HashMap<String, DPManager> getDpManagers() {
		return dpManagers;
	}

	public HashMap<String, ServiceEndPoint> getServiceEndPointCollection() {
		return this.serviceEndPointCollection;
	}

	private DPManager dbManager;
	private Device device;
	private Domain domain;
	private DeploymentPolicy deploymentPolicy;
	private ServiceEndPoint serviceEndPoint;
	private ManagedSet managedSet;

	final String TAG_DP_MANAGER = "dat:DPManager";
	final String TAG_DEVICES = "devices";
	final String TAG_DOMAINS = "domains";
	final String TAG_DEPLOYMENT_POLICY = "deploymentPolicy";
	final String TAG_SERVICE_END_POINT = "serviceend-point";
	final String TAG_MANAGED_SET = "managedSets";

	final String TAG_ID = "xmi:id";

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		// Push the start element of XML on stack which is further popped in end 
		// element in order to verify an valid XML
		pushTag(qName);

		if (TAG_DP_MANAGER.equalsIgnoreCase(qName)) {

			dpManagers = new HashMap<String, DPManager>();
			dbManager = new DPManager();

			dbManager.setDat(attributes.getValue("xmlns:dat"));
			dbManager.setXmi(attributes.getValue("xmlns:xmi"));
			dbManager.setVersion(Double.parseDouble(attributes
					.getValue("xmi:version")));
			dbManager.setVersionsStoredLimit(Integer.parseInt(attributes
					.getValue("VersionsStoredLimit")));
			dbManager.setId(attributes.getValue("xmi:id"));

		} else if (TAG_DEVICES.equalsIgnoreCase(qName)) {
			
			device = new Device();

			device.setId(attributes.getValue("xmi:id"));
			device.setDeviceType(attributes.getValue("deviceType"));
			device.setGuiPort(Integer.parseInt(attributes.getValue("GUIPort")));
			device.setHlmPort(Integer.parseInt(attributes.getValue("HLMPort")));
			device.setCurrentAMPVersion(Double.parseDouble(attributes
					.getValue("currentAMPVersion")));
			device.setQuiesceTimeout(Integer.parseInt(attributes
					.getValue("quiesceTimeout")));
			device.setFeatureLicenses(attributes.getValue("featureLicenses"));
			
		} else if (TAG_DOMAINS.equalsIgnoreCase(qName)) {
			
			domain = new Domain();

			domain.setId(attributes.getValue(TAG_ID));
			domain.setHighestVersion(Integer.parseInt(attributes
					.getValue("highestVersion")));
			domain.setSynchDate(Integer.parseInt(attributes
					.getValue("SynchDate")));
			domain.setOutOfSynch(Boolean.parseBoolean(attributes
					.getValue("OutOfSynch")));
			domain.setQuiesceTimeout(Integer.parseInt(attributes
					.getValue("quiesceTimeout")));
			domain.setSyncMode(attributes.getValue("SyncMode"));

		} else if (TAG_DEPLOYMENT_POLICY.equalsIgnoreCase(qName)) {
			
			deploymentPolicy = new DeploymentPolicy();

			deploymentPolicy.setId(attributes.getValue(TAG_ID));
			deploymentPolicy.setHighestVersion(Integer.parseInt(attributes
					.getValue("highestVersion")));
			deploymentPolicy.setSynchDate(Integer.parseInt(attributes
					.getValue("SynchDate")));
			deploymentPolicy.setPolicyType(attributes.getValue("policyType"));

		} else if (TAG_SERVICE_END_POINT.equalsIgnoreCase(qName)) {
			
			serviceEndPoint = new ServiceEndPoint();

			serviceEndPoint.setId(attributes.getValue(TAG_ID));
			serviceEndPoint.setType(attributes.getValue("type"));
			serviceEndPoint.setOperation(attributes.getValue("operation"));
			serviceEndPoint.setPort(Integer.parseInt(attributes
					.getValue("port")));
			serviceEndPoint
					.setTargetServer(attributes.getValue("targetserver"));

		} else if (TAG_MANAGED_SET.equalsIgnoreCase(qName)) {
			
			managedSet = new ManagedSet();

			managedSet.setId(attributes.getValue(TAG_ID));
			String tempDeviceMembers = attributes.getValue("devicemembers");

			for (String tempDeviceId : tempDeviceMembers.split(",")) {
				managedSet.addDeviceMember(tempDeviceId);
			}
		} else {
			// Error Case
			System.out.println("Unknown XML element found");
		}

	}

	// Read element value
	public void characters(char ch[], int start, int length) {
		tempVal.append(ch, start, length);
	}
	

	public void endElement(String uri, String localName, String qName) {
		String tag = peekTag();

		// Pop from stack to verify an Valid XML
		if (!qName.equals(tag)) {
			throw new InternalError();
		}

		popTag();

		if (TAG_MANAGED_SET.equalsIgnoreCase(qName)) {
			dbManager.addManagedSet(managedSet);
		} else if (TAG_SERVICE_END_POINT.equalsIgnoreCase(qName)) {
			
			serviceEndPoint.setDepPolicyId(deploymentPolicy.getId());
			
			if(!serviceEndPointCollection.containsKey(serviceEndPoint.getId())){
				serviceEndPointCollection.put(serviceEndPoint.getId(), serviceEndPoint);
			}			
			
			deploymentPolicy.addServiceEndPoint(serviceEndPoint);
		} else if (TAG_DEPLOYMENT_POLICY.equalsIgnoreCase(qName)) {
			domain.addDeploymentPolicy(deploymentPolicy);
		} else if (TAG_DOMAINS.equalsIgnoreCase(qName)) {
			device.addDomain(domain);
		} else if (TAG_DEVICES.equalsIgnoreCase(tag)) {
			dbManager.addDevice(device);
		} else if (TAG_DP_MANAGER.equalsIgnoreCase(tag)) {
			dpManagers.put(dbManager.getId(), dbManager);
		}
	}

	public void startDocument() {
		pushTag("");
	}

	private void pushTag(String tag) {
		tagsStack.push(tag);
	}

	private String popTag() {
		return tagsStack.pop();
	}

	private String peekTag() {
		return tagsStack.peek();
	}

	public DPManager getDPManager() {
		return dbManager;
	}
}
