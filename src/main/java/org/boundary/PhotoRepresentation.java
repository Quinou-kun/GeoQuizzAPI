package org.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.entity.Photo;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("photos")
public class PhotoRepresentation 
{
    @Inject
    PhotoResource photoResource;
    
    @Context
    UriInfo uriInfo;

    /**
     * 
     *  Route permettant de récupérer la liste des photos
     * 
     */

    @GET
    public Response getPhotos() 
    {
        JsonObject json = Json.createObjectBuilder()
                .add("photos", buildPhotosArray())
                .build();

        return Response.ok(json).build();
    }

    private JsonValue buildPhotosArray() 
    {
        List<Photo> photos = photoResource.findAll();
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Photo p : photos)
        {   
            jab.add(buildPhotoJSON(p));
        }

		return jab.build();
	}

    private JsonValue buildPhotoJSON(Photo p) 
    {
        JsonValue json = Json.createObjectBuilder()
            .add("id", p.getId())
            .add("desc", p.getDescription())
            .add("position", p.getPosition())
            .add("url", p.getUrl())
            .build();

		return json;
	}
}
