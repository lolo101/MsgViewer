package at.redeye.SqlDBInterface.SqlDBConnection.impl;

public class ConnectionDefinition {

	private String instance_;

	public ConnectionDefinition(String instance) {
		this.instance_ = instance;
	}

	/**
	 * @return the instance_
	 */
	public String getInstance() {
		return instance_;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance_ = instance;
	}


}
