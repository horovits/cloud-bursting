package com.gigaspaces.poc.feeder;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.gigaspaces.poc.common.Constants;
import com.gigaspaces.poc.common.data.DataBaseQueries;
import com.gigaspaces.poc.query.MySQLQueryLogParser;
import com.gigaspaces.poc.query.QueryLogParser;
import com.gigaspaces.poc.utils.FileMonitor;
import com.gigaspaces.poc.utils.FileMonitorListener;

public class Feeder implements DisposableBean, InitializingBean {

	private static final Logger logger = Logger.getLogger("Feeder");
	
	private FileMonitor fileMonitor;
	private GigaSpace gigaSpace;
	private String queriesLogFilePath;
	private String siteName;

	
	public void setGigaSpace(GigaSpace gigaSpace) {
		this.gigaSpace = gigaSpace;
	}

	public void setQueriesLogFilePath(String queriesLogFilePath) {
		this.queriesLogFilePath = queriesLogFilePath;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final QueryLogParser parser = new MySQLQueryLogParser();		
		fileMonitor = new FileMonitor(queriesLogFilePath,	new FileMonitorListener() {
			@Override
			public void onFileModified(List<String> newContent) {
				List<String> queries = parser.getQueries(newContent);
				// Filter queries from WAN replication
				for (Iterator<String> iterator = queries.iterator(); iterator.hasNext();) {
					if (iterator.next().endsWith(Constants.QUERY_SUFFIX))
						iterator.remove();
				}
				if (queries.size() > 0) {
					for (String query : queries) {
						logger.info("Writing SQL query to space: " + query);
					}
					DataBaseQueries dbQueries = new DataBaseQueries(queries, siteName);
					gigaSpace.write(dbQueries);
				} else {
					logger.info("No queries to execute in queries log text");
				}
				
			}
		});
		logger.info("Feeder initialized [queriesLogFilePath=" + queriesLogFilePath + "]");
	}

	@Override
	public void destroy() throws Exception {
		if (fileMonitor != null)
			fileMonitor.close();	
	}

}
