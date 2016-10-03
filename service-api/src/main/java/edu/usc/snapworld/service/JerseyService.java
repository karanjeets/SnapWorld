package edu.usc.snapworld.service;

import edu.usc.snapworld.model.SnapData;
import edu.usc.snapworld.util.Constants;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

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
	
	@Path("/getdata/{latitude}/{longitude}")
    @GET
    public String getDetails(@PathParam("latitude") String latitude, @PathParam("longitude") String longitude) {
    	GetDbData conn = new GetDbData();
    	String dbData = conn.getData(latitude, longitude);
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
    @Path("/putdataold")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void postDetails(SnapData data) {
    	PutDbData conn = new PutDbData();
        System.out.println(data.getImage());
        System.out.println("Karan: " + data.getCategory());
    	conn.putData(data.getImage().getBytes(), data.getUsername(),data.getLatitude(),data.getLongitude(),
                data.getCategory(),data.getDescription(),data.getTimestamp());
        //return dbData;
        //return Response.status(201).entity(data).build();
    }


    @POST
    @Path("/putdata")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postDetailsNew(@FormDataParam(Constants.JSON_IMAGE) InputStream imageStream,
                                   @FormDataParam(Constants.JSON_IMAGE) FormDataContentDisposition imageDetail,
                                   @FormDataParam(Constants.JSON_USERNAME) String username,
                                   @FormDataParam(Constants.JSON_LATITUDE) String latitude,
                                   @FormDataParam(Constants.JSON_LONGITUDE) String longitude,
                                   @FormDataParam(Constants.JSON_CATEGORY) String category,
                                   @FormDataParam(Constants.JSON_DESCRIPTION) String description,
                                   @FormDataParam(Constants.JSON_TIMESTAMP) String timestamp) throws IOException {

        //System.out.print(imageDetail.getFileName());
        PutDbData conn = new PutDbData();
        System.out.println("Karan: " + category);
        conn.putData(IOUtils.toByteArray(imageStream), username, latitude, longitude,
                category, description, timestamp);

        return Response.status(200).entity("Successful").build();
    }

	
}
