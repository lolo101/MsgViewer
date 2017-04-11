package at.redeye.SqlDBInterface.SqlDBConnection.impl;

/**
 * @author Mario Mattl
 * 
 */

public class ConnectionDefinition {

	private String hostname_ = "";

	private String username_ = "";
	
	private int	port_ = 0;

	private String pwd_ = "";

	private SupportedDBMSTypes dbmstype_;
	
	private String instance_ = "";

	public ConnectionDefinition(String hostname, int port, String username,
			String pwd, String instance, SupportedDBMSTypes dbmstype) {
		super();
		this.hostname_ = hostname;
		this.username_ = username;
		this.pwd_ = pwd;
		this.instance_ = instance;
		this.dbmstype_ = dbmstype;
		this.port_ = port;
	}
	
	public ConnectionDefinition(String hostname, String username,
			String pwd, String instance, SupportedDBMSTypes dbmstype) {
		super();
		this.hostname_ = hostname;
		this.username_ = username;
		this.pwd_ = pwd;
		this.instance_ = instance;
		this.dbmstype_ = dbmstype;
	}

	/**
	 * @return the hostname_
	 */
	public String getHostname() {
		return hostname_;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname_ = hostname;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username_;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username_ = username;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port_;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port_ = port;
	}

	/**
	 * @return the pwd_
	 */
	public String getPwd() {
		return pwd_;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd_ = pwd;
	}

	/**
	 * @return the dbmstype_
	 */
	public SupportedDBMSTypes getDBMSType() {
		return dbmstype_;
	}

	/**
	 * @param dbmstype the dbmstype to set
	 */
	public void setDBMSType(SupportedDBMSTypes dbmstype) {
		this.dbmstype_ = dbmstype;
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
