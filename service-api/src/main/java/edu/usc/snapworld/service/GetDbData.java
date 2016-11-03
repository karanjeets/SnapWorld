package edu.usc.snapworld.service;

import edu.usc.snapworld.util.CommonUtil;
import edu.usc.snapworld.util.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class GetDbData {
	
	private Properties configProperties;
	
	public GetDbData() {
		configProperties = CommonUtil.configProperties;
	}
	
	public String getData(String latitude, String longitude)
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

	        ResultSet rs = stmt.executeQuery("SELECT T.* FROM (SELECT *, (SELECT SUM(di.cost)*69 FROM pgr_dijkstra('SELECT id, source, " +
                    "target, st_length(geom_way) as cost FROM hh_2po_4pgr', S.osmid::int, " +
                    "(select target from hh_2po_4pgr order by st_distance(geom_way, " +
                    "st_setsrid(st_makepoint(" + longitude + ", " + latitude + "), 4326)) limit 1), false, false) as di " +
                    "JOIN hh_2po_4pgr ON di.id2 = hh_2po_4pgr.id) as road_distance FROM snapdata S " +
                    "ORDER BY coordinate <-> st_setsrid(ST_MakePoint(" + longitude + "," + latitude + "),4326) LIMIT 15) " +
                    "AS T ORDER BY T.road_distance;" );

	       dbData = CommonUtil.convertToJSON(rs, Constants.JSON_SNAPDATA).toString();
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
	
	
}

