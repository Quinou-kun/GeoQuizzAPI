package org.boundary;

import java.io.File;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.entity.Photo;

@Stateless
@Path("photos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
    
    /**
     * 
     *  Route permettant de récupérer l'image d'une photo
     * 
     */

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"image/jpeg", "image/png"})
    public Response getImgPhoto(@PathParam("id") String uid)
    {
        if(uid == null || uid.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();

        Photo photo = photoResource.findById(uid);

        if(photo == null) return Response.status(Response.Status.NOT_FOUND).build();

        File file = new File(photo.getUrl());
 
        String filename = file.getName();

        ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        
        return responseBuilder.build();
    }
}
