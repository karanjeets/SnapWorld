package edu.usc.snapworld.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import edu.usc.snapworld.util.CommonUtil;
import edu.usc.snapworld.util.Constants;

public class PutDbData {

	private Properties configProperties;
	
	public PutDbData() {
		configProperties = CommonUtil.configProperties;
	}
	
	public void putData(byte[] image, String username,String latitude,String longitude,
			String category,String description,String timestamp){
		Connection c = null;
	      //Statement stmt = null;
	      PreparedStatement pst = null;

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
	       
	        String imgpath = CommonUtil.saveImage(image, username, timestamp);
	        		
	        //stmt = c.createStatement(); 
	        String stm = "INSERT INTO snapdata(username,coordinate, imgpath, category, description, timestamp, latitude, longitude ) VALUES(?, ST_GeomFromText(?, 4326), ?, ?, ?, ?, ?, ?)";
	        pst = c.prepareStatement(stm);
	        
	        pst.setString(2,"POINT("+Double.parseDouble(latitude)+" "+Double.parseDouble(longitude)+")" );
	        
           // pst.setInt(1, id);
            pst.setString(1, username);
            //pst.setDouble(3, Double.parseDouble(longitude));
            //pst.setDouble(4, Double.parseDouble(latitude));
            pst.setString(3, imgpath);
            pst.setString(4, category);
            pst.setString(5, description);
            pst.setTimestamp(6, CommonUtil.toSqlTimestamp(timestamp));
            pst.setString(7, latitude);
            pst.setString(8, longitude);
            pst.executeUpdate();
          c.commit();
         
	      
	        pst.close();
	        
	        c.close();
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        System.exit(0);
	      }
	      System.out.println("Operation done successfully");

	}
	
}
