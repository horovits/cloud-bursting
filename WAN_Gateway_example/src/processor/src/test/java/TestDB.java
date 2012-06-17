import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestDB {

	
//	@Test
//	public void testDisconnection() throws Exception {
//		
//		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		Connection conn = createJdbcConnection();
//		
//		System.out.println("READY");
//		
//		try { 
//			Statement stmt = conn.createStatement();
//			stmt.executeQuery("select * from test");
//		} catch (Exception e) {
//			System.out.println(e.getClass().getName());
//			System.out.println(e.getMessage());
//			System.out.println(conn.isValid(5));
//		}
//		
//		
//	}

	private Connection createJdbcConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/idan", "root", "");
	}

	
}
