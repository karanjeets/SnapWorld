package edu.usc.snapworld.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by karanjeetsingh on 9/9/16.
 */
@Path("/data")
public class JerseyService {
	@Path("/status")
    @GET
    public String getMessage() {
        return "Jersey 2 is running fine...";
    }
	
	@Path("/getdata")
    @GET
    public String getDetails() {
    	GetDbData conn = new GetDbData();
    	String dbData = conn.getData();
        return dbData;
    }
	@Path("/getcategory")
    @GET
    public String getCategoryValues() {
    	GetCategoryData conn = new GetCategoryData();
    	String dbData = conn.getData();
        return dbData;
    }
	@Path("/putdata/{image}/{username}/{latitude}/{longitude}/{category}/{description}/{timestamp}")
    @GET
    public void putDetails(@PathParam("image") String image, @PathParam("username") String username, @PathParam("latitude") String latitude, @PathParam("longitude") String longitude, @PathParam("category") String category, @PathParam("description") String description, @PathParam("timestamp") String timestamp) {
    	PutDbData conn = new PutDbData();
    	conn.putData(image.getBytes(), username,latitude,longitude,category,description,timestamp);
        //return dbData;
    }
	
}
