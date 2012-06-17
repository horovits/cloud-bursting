package com.gigaspaces.poc.query;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySQLQueryLogParser implements QueryLogParser {

	private Pattern queryPattern;
	private Pattern executeQueryPattern;
	
    public MySQLQueryLogParser() {
    	this.executeQueryPattern = Pattern.compile(".*(?i)(query|execute).*");
		this.queryPattern = Pattern.compile("(?i)(insert|update|delete).*");
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

}
