package com.gigaspaces.poc.query.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gigaspaces.poc.query.MySQLQueryLogParser;

public class MySQLQueryLogParserTest {

	@Test
	public void testQueries() {
		MySQLQueryLogParser parser = new MySQLQueryLogParser();
		
		List<String> queries = new ArrayList<String>();
		queries.add("120522 15:45:37	    5 Query	update test set text = 'hello' where id = 10"); // GOOD
		queries.add("120522 15:45:37	    5 Prepare	SELECT id, visit_date, description FROM visits WHERE pet_id=?");
		queries.add("120522 15:45:37	    5 Execute	SELECT id, visit_date, description FROM visits WHERE pet_id=5");
		queries.add("120522 15:45:37	    5 Query	SET autocommit=1");
		queries.add("120522 15:45:37	    5 Prepare	UPDATE owners SET first_name=?, last_name=?, address=?, city=?, telephone=? WHERE id=?");
		queries.add("120522 15:45:37	    5 Execute	UPDATE owners SET first_name='joe', last_name='dow', address='5', city='Herzeliya', telephone='5' WHERE id=11"); // GOOD
		queries.add("120522 15:45:37	    5 Query     update owners set address = 'holon' where id=11");
		queries.add("120522 15:45:37	    2 Query	start transaction");
		queries.add("120522 15:45:37	    5 Query     update owners set address = 'netanya' where id=11");
		queries.add("120522 15:45:37	    2 Query	commit");

		Assert.assertEquals(6, parser.getQueries(queries).size());
	}
	
	@Test
	public void testIsStartTransaction() {
		MySQLQueryLogParser parser = new MySQLQueryLogParser();
		String query = "start transaction";
		Assert.assertTrue(parser.isStartTransaction(query));
	}
	
//	@Test
//	public void testQueriesFromFile() throws IOException {
//		BufferedReader reader = new BufferedReader(new FileReader("D:\\temp\\queries.txt"));
//		MySQLQueryLogParser parser = new MySQLQueryLogParser();
//		
//		List<String> queries = new ArrayList<String>();
//		String line;
//		while ((line = reader.readLine()) != null) {
//			queries.add(line);			
//		}
//		
//		reader.close();
//		Assert.assertEquals(2, parser.getQueries(queries).size());
//	}
}
