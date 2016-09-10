package edu.usc.snapworld.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by karanjeetsingh on 9/9/16.
 */
@Path("/status")
public class JerseyService {

    @GET
    public String getMessage() {
        return "Jersey 2 is running fine...";
    }
}
