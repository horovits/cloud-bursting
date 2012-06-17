package com.gigaspaces.poc.common.data;

import java.util.List;

import com.gigaspaces.annotation.pojo.FifoSupport;
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass(fifoSupport = FifoSupport.ALL)
public class DataBaseQueries {

	private List<String> queries;
	private String id;
	private String siteName;

	public DataBaseQueries() {
	}
	
	public DataBaseQueries(List<String> queries, String siteName) {
		this.queries = queries;
		this.siteName = siteName;
	}

	public List<String> getQueries() {
		return queries;
	}

	public void setQueries(List<String> queries) {
		this.queries = queries;
	}

	@SpaceId(autoGenerate = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	
	
}
