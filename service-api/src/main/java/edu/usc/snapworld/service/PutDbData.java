package edu.usc.snapworld.service;

import edu.usc.snapworld.util.CommonUtil;
import edu.usc.snapworld.util.Constants;

import java.sql.*;
import java.util.Properties;

public class PutDbData {

	private Properties configProperties;
	
	public PutDbData() {
		configProperties = CommonUtil.configProperties;
	}
	
	public void putData(byte[] image, String username,String latitude,String longitude,
			String category,String description,String timestamp){
		Connection c = null;
	      Statement stmt = null;
	      PreparedStatement pst = null;

	      try {
			  System.out.println("Username, Latitude, Longitude, Category, Description, Timestamp: "
					  + username + ", " + latitude + ", " + longitude + ", " + category + ", " + description + ", "
					  + timestamp);
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
	        		
	        stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select source from hh_2po_4pgr order by st_distance(geom_way, " +
                    "st_setsrid(st_makepoint(" + longitude + ", " + latitude + "), 4326)) limit 1");
            rs.next();
            Integer osmid = rs.getInt("source");

	        String stm = "INSERT INTO snapdata(username,coordinate, imgpath, category, description, timestamp, latitude, longitude, osmid ) VALUES(?, ST_GeomFromText(?, 4326), ?, ?, ?, ?, ?, ?, ?)";
	        pst = c.prepareStatement(stm);
	        
	        pst.setString(2,"POINT("+Double.parseDouble(latitude)+" "+Double.parseDouble(longitude)+")" );
	        
           // pst.setInt(1, id);
            pst.setString(1, username);
            //pst.setDouble(3, Double.parseDouble(longitude));
            //pst.setDouble(4, Double.parseDouble(latitude));
            pst.setString(3, imgpath);
            pst.setInt(4, Integer.parseInt(category));
            pst.setString(5, description);
            pst.setTimestamp(6, CommonUtil.toSqlTimestamp(timestamp));
            pst.setString(7, latitude);
            pst.setString(8, longitude);
            pst.setInt(9, osmid);
            pst.executeUpdate();
          c.commit();
         
	      
	        pst.close();
	        
	        c.close();
			  System.out.println("Operation done successfully");
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			  e.printStackTrace();
	        //System.exit(0);
	      }
		System.out.println("Operation failed");

	}

    public static void main(String[] args) {
        PutDbData put = new PutDbData();
        put.putData("".getBytes(), "user", "34.02637224", "-118.27701804", "4", "Test", "2016-11-02-12-12-12");
    }


}
