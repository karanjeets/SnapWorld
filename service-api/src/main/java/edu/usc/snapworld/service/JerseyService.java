package edu.usc.snapworld.service;

import edu.usc.snapworld.model.SnapData;
import org.json.JSONException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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


    @POST
    @Path("/putdata")
    @Consumes("application/json")
    public void postDetails(SnapData data) throws JSONException {
    	PutDbData conn = new PutDbData();
        System.out.println(data.getImage());
        System.out.println("Karan: " + data.getCategory());
    	conn.putData(data.getImage().getBytes(), data.getUsername(),data.getLatitude(),data.getLongitude(),
                data.getCategory(),data.getDescription(),data.getTimestamp());
        //return dbData;
        //return Response.status(201).entity(data).build();
    }
	
}
