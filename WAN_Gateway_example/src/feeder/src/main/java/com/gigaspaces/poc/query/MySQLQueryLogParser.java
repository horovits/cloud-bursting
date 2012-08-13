package com.gigaspaces.poc.query;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySQLQueryLogParser implements QueryLogParser {

	private Pattern queryPattern;
	private Pattern executeQueryPattern;
	private Pattern startTransactionQueryPattern;
	private Pattern commitTransactionQueryPattern;
	private Pattern rollbackTransactionQueryPattern;
	
    public MySQLQueryLogParser() {
    	this.executeQueryPattern = Pattern.compile(".*(?i)(query|execute).*"); //exclude 'prepare' statements etc.
		this.queryPattern = Pattern.compile("(?i)(insert|update|delete|START TRANSACTION|\\sCOMMIT|ROLLBACK).*");
		this.startTransactionQueryPattern = Pattern.compile("(START TRANSACTION).*", Pattern.CASE_INSENSITIVE);
		this.commitTransactionQueryPattern = Pattern.compile("(COMMIT).*", Pattern.CASE_INSENSITIVE);
		this.rollbackTransactionQueryPattern = Pattern.compile("(ROLLBACK).*", Pattern.CASE_INSENSITIVE);
	}
	
	@Override
	public List<String> getQueries(List<String> queryLog) {
		List<String> queries = new LinkedList<String>();
		
		for (String string : queryLog) {
			Matcher executeQueryMatcher = executeQueryPattern.matcher(string);
			if (executeQueryMatcher.matches()) {
				Matcher queryMatcher = queryPattern.matcher(executeQueryMatcher.group());            
				if (queryMatcher.find()) {
					queries.add(queryMatcher.group());
				}				
			}
        }
		
		return queries;
	}

	@Override
	public boolean isStartTransaction(String query) {
		return startTransactionQueryPattern.matcher(query).find();
	}

	@Override
	public boolean isCommitTransaction(String query) {
		return commitTransactionQueryPattern.matcher(query).find();
	}

	@Override
	public boolean isRollbackTransaction(String query) {
		return rollbackTransactionQueryPattern.matcher(query).find();
	}

}
