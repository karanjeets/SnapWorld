package edu.usc.snapworld.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.usc.snapworld.util.CommonUtil;
import edu.usc.snapworld.util.Constants;

public class GetDbData {
	
	private Properties configProperties;
	
	public GetDbData() {
		configProperties = CommonUtil.configProperties;
	}
	
	public String getData()
	{
		Connection c = null;
	      Statement stmt = null;
	      String dbData = null;
	      try {
	    	  Class.forName(configProperties.getProperty(Constants.SQL_DRIVER));
		    	String connectionString = "jdbc:" + configProperties.getProperty(Constants.SQL_DATABASE) + 
		    			"://" + configProperties.getProperty(Constants.SQL_HOST) + ":" + 
		    			configProperties.getProperty(Constants.SQL_PORT) + "/" + 
		    			configProperties.getProperty(Constants.SQL_DBNAME);
		        c = DriverManager.getConnection(connectionString,
		        		configProperties.getProperty(Constants.SQL_USERNAME), 
		        		configProperties.getProperty(Constants.SQL_PASSWORD));	        
		    c.setAutoCommit(false);
	        System.out.println("Opened database successfully");

	        stmt = c.createStatement(); 
	        ResultSet rs = stmt.executeQuery( "SELECT * FROM snapdata;" );
	       dbData = convertToJSON(rs);
	       /* while ( rs.next() ) {
	           int id = rs.getInt("id");
	           String  name = rs.getString("names");
	           System.out.println( "ID = " + id );
	           System.out.println( "NAME = " + name );
	           System.out.println();
	        }*/
	        rs.close();
	        stmt.close();
	        c.close();
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
	      System.out.println("Operation done successfully");
		return dbData;
	
	}
	
	public static String convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
                
            }
            jsonArray.put(obj);
        }
        return jsonArray.toString();
    }
	
	
}

