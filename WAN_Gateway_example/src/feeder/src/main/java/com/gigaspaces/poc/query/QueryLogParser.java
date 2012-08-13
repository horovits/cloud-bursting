package com.gigaspaces.poc.query;

import java.util.List;

public interface QueryLogParser {

	List<String> getQueries(List<String> queryLog);

	boolean isStartTransaction(String query);

	boolean isCommitTransaction(String query);

	boolean isRollbackTransaction(String query);

	
}
