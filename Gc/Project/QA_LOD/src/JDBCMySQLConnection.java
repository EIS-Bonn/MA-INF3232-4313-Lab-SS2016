import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBCMySQLConnection {
	private static JDBCMySQLConnection instance = new JDBCMySQLConnection();
    public static final String URL = "jdbc:mysql://localhost/gdus?characterEncoding=UTF-8&useSSL=false";
    public static final String USER = "root";
    public static final String PASSWORD = "LOD123";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
    public static final String Db_Name="gdus";
    //private constructor
    public JDBCMySQLConnection() {
        try {
            //Step 2: Load MySQL Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
 
        Connection connection = null;
        try {
            //Step 3: Establish Java MySQL connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }   
     
    
	public static float checkIfResExist(String Res)
	{
		float ID=0;
    	ResultSet rs = null;
    	Connection connection = null;
    	Statement statement = null; 
		String query="Select ID from "+Db_Name+".dbpediadata where Resource='"+Res+"'";
		//System.out.println(query);
		   try{
		  //  Statement st=con.createStatement();
	      //  ResultSet re=st.executeQuery(query);
			   connection = JDBCMySQLConnection.getConnection();
	              statement = connection.createStatement();
	              rs = statement.executeQuery(query);
				if(rs.next())
				{
					ID=Float.parseFloat(rs.getString(1));
			
				}
		   }
		   catch (SQLException e) {
	              e.printStackTrace();
	          }
		return ID;	
	}
    
    public static float calcualteTheQuestionRank(ArrayList<String> words)
	{
    	ResultSet rs = null;
    	Connection connection = null;
    	Statement statement = null; 
		float rank=0;
		for (String object: words) {
		   // System.out.println(object);
		    String query="SELECT Rank FROM "+Db_Name+".word WHERE Word='"+object+"'";
		   try{
			   connection = JDBCMySQLConnection.getConnection();
	           statement = connection.createStatement();
	           rs = statement.executeQuery(query);
		    //Statement st=con.createStatement();
			//ResultSet re=st.executeQuery(query);
				if(rs.next())
				{
					rank=rank+Float.parseFloat(rs.getString(1));
				}
		   }
		   catch (SQLException e) {
	              e.printStackTrace();
	          }
		}
		return rank;	
	}
    
    public static void mysqlTest(){
	    	ResultSet rs = null;
	    	Connection connection = null;
	    	Statement statement = null; 
    	  String query = "SELECT * FROM gdus.category";
    	  try {           
              connection = JDBCMySQLConnection.getConnection();
              statement = connection.createStatement();
              rs = statement.executeQuery(query);
               
              while (rs.next()) {
            	  System.out.println("RANK IS: "+rs.getString("Category"));
              //    employee = new Employee();
               //   employee.setEmpId(rs.getInt("emp_id"));
                //  employee.setEmpName(rs.getString("emp_name"));
                //  employee.setDob(rs.getDate("dob"));
                //  employee.setSalary(rs.getDouble("salary"));
                //  employee.setDeptId((rs.getInt("dept_id")));
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
    }
    
    public static Connection getConnection() {
        return instance.createConnection();
    }
}
