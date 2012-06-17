package com.gigaspaces.poc.processor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.openspaces.events.adapter.SpaceDataEvent;
import org.springframework.beans.factory.InitializingBean;

import com.gigaspaces.poc.common.Constants;
import com.gigaspaces.poc.common.data.DataBaseQueries;


public class Processor implements InitializingBean {
	
	private static final Logger logger = Logger.getLogger("Processor");
	private static final int RETRIES_ON_FAILURE = 5;
	private static final int SLEEP_BETWEEN_RETRIES = 3000;
	
	private Connection connection;
	private String connectionUrl;
	private String username = "";
	private String password = "";
	private String siteName;

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@SpaceDataEvent
	public DataBaseQueries processMessage(DataBaseQueries databaseQueries) {		
		for (String query : databaseQueries.getQueries()) {			
			boolean done = false;
			int retries = 0;
			do {
				if (retries > 0)
					logger.info("Executing query - retry #" + retries + " [source=" + databaseQueries.getSiteName() + "]: " + query);					
				else
					logger.info("Executing query [source=" + databaseQueries.getSiteName() + "]: " + query);
				try {
					if (connection == null)
						throw new SQLException("No connection to SQL server");
					Statement statement = connection.createStatement();
					statement.executeUpdate(query + Constants.QUERY_SUFFIX);
					done = true;
				} catch (SQLException e) {						
					logger.severe("Error executing query: '" + query + "' - " + e.getMessage());
					try {
						if (connection != null && !connection.isValid(5))
							connection = null;
					} catch (SQLException ex) {
						// Should never happen...
					}
					logger.info("Sleeping for " + SLEEP_BETWEEN_RETRIES + " milliseconds before next attempt");
					try {
						Thread.sleep(SLEEP_BETWEEN_RETRIES);
					} catch (InterruptedException ex) {
					}
					retries++;
					if (connection == null) {
						logger.info("No connection to SQL server - attempting to recover connection [" + retries +"]");
						try {
							connection = createJdbcConnection();
						} catch (SQLException ex) {
							logger.severe("Cannot establish connection to SQL server: " + ex.getMessage());
						}
					}
				}
			} while (!done && retries <= RETRIES_ON_FAILURE);
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (connectionUrl == null) {
			logger.warning("Connection URL was not set");
			return;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = createJdbcConnection();			
			logger.info("Processor initialized");
		} catch (Exception e) {
			logger.severe("Error initializing processor: " + e.getMessage());			
		}		
	}

	private Connection createJdbcConnection() throws SQLException {
		return DriverManager.getConnection(connectionUrl, username, password);
	}

}
