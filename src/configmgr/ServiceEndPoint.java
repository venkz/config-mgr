package configmgr;

public class ServiceEndPoint {

	private String id;

	private String type;

	private String operation;

	private int port;

	private String targetServer;

	public ServiceEndPoint() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTargetServer() {
		return targetServer;
	}

	public void setTargetServer(String targetServer) {
		this.targetServer = targetServer;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Service Endpoint:");
		sb.append(" xmi:id=" + this.getId());
		sb.append(" type=" + this.getType());
		sb.append(" operation="+ this.getOperation());
		sb.append(" port=" + this.getPort());
		sb.append(" targetserver" + this.getTargetServer());
		
		return sb.toString();
	}
}
