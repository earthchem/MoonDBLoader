package org.moondb.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;





public class DatabaseUtil {

    
    private static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
 
        Connection connection;
         
        Properties prop = new Properties();
        //System.out.println("test");
        prop.load(new FileInputStream("db.properties"));

        String host = prop.getProperty("url").toString().trim();
        String username = prop.getProperty("username").toString().trim();
        String password = prop.getProperty("password").toString().trim();
        String driver = prop.getProperty("driver").toString().trim();
 
        //System.out.println("host: " + host + "\\username: " + username + "\\password: " + password);
 
        Class.forName(driver);
        //System.out.println("--------------------------");
       // System.out.println("DRIVER: " + driver);
        connection = DriverManager.getConnection(host, username, password);
       // System.out.println("CONNECTION: " + connection);
 
        return connection;
    }
    
    public static List<Object[]> getRecords(String query) {

    	List<Object[]> records=new ArrayList<Object[]>();

    	Connection con = null;

    	Statement stmt = null;

    	ResultSet rs = null;

    	

    	try {

    		con = getConnection();

    		stmt = con.createStatement();

            rs = stmt.executeQuery(query);

            int cols = rs.getMetaData().getColumnCount();

            while(rs.next()){

            	 Object[] arr = new Object[cols]; 

            	 for(int i=0; i<cols; i++){ 

            		 arr[i] = rs.getObject(i+1); 

            	 } 

            	 records.add(arr); }

    	} catch (SQLException e) { 

       	 	System.err.println(e);

        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

        	try {

        		if(rs != null) rs.close();

        		if(stmt != null) stmt.close();

        		if(con != null) con.close();   

        	} catch (SQLException e) {

        		System.err.println(e);

        	}

        }

    	return records;

    }
    
    public static Object getUniqueResult(String query) {
    	Object record =null;
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		con = getConnection();
    		stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next())
            	 record = rs.getObject(1);
    	} catch (SQLException e) { 
       	 	System.err.println(e);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
        	try {
        		if(rs != null) rs.close();
        		if(stmt != null) stmt.close();
        		if(con != null) con.close();   
        	} catch (SQLException e) {
        		System.err.println(e);
        	}
        }
    	return record;
    }
    
    public static String update(List<String> queries) {
    	String error = null;
    	Connection con = null;
    	Statement stmt = null;
    	
    	try {
    		con = getConnection();
    		stmt = con.createStatement();
    		con.setAutoCommit(false);
    		for(String q: queries) stmt.executeUpdate(q);
    		con.commit();
    	} catch (SQLException e) { 
       	 	System.err.println(e);
       	 	error = e.getMessage();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
        	try {
        		if(stmt != null) stmt.close();
        		if(con != null) con.close();   	
        	} catch (SQLException e) {
        		System.err.println(e);
        	}
        }  
    	return error;       	
    }
    
    public static String update(String query) {
    	String error = null;
    	Connection con = null;
    	Statement stmt = null;
    	
    	try {
    		con = getConnection();
    		stmt = con.createStatement();
    		con.setAutoCommit(false);
    		stmt.executeUpdate(query);
    		con.commit();
    	} catch (SQLException e) { 
       	 	System.err.println(e);
       	 	error = e.getMessage();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
        	try {
        		if(stmt != null) stmt.close();
        		if(con != null) con.close();   	
        	} catch (SQLException e) {
        		System.err.println(e);
        	}
        }  
    	return error;       	
    }

    
   
}
