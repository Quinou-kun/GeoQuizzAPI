package org.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.entity.Photo;
import org.entity.Serie;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("series")
public class SerieRepresentation {

    @Inject
    SerieResource serieResource;

    @Inject
    PhotoResource photoResource;
    
    @Context
    UriInfo uriInfo;

    /**
     * 
     *  Route permettant de récupérer la liste des séries
     * 
     */

    @GET
    public Response getSeries() 
    {
        JsonObject json = Json.createObjectBuilder()
                .add("series", buildSeriesArray())
                .build();
        return Response.ok(json).build();
    }

    private JsonValue buildSeriesArray() 
    {
        List<Serie> series = serieResource.findAll();

        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Serie s : series)
        {
            jab.add(buildSerieJson(s));
        }

		return jab.build();
	}

    private JsonValue buildSerieJson(Serie s) 
    {
        JsonValue json = Json.createObjectBuilder()
            .add("id", s.getId())
            .add("ville", s.getVille())
            .add("mapOptions", s.getMapOptions())
            .add("photos", buildJsonForPhotos(s))
            .build();

		return json;
    }
    
    private JsonValue buildJsonForPhotos(Serie s) 
    {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(Photo p : s.getPhotos())
        {
            jab.add(buildJsonForPhoto(p));
        }

        return jab.build();
	}

    private JsonValue buildJsonForPhoto(Photo p) 
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
     *  Route permettant de récupérer une série
     * 
     */

    @GET
    @Path("{id}")
    public Response getSerie(@PathParam("id") String id) 
    {
        Serie s = serieResource.findById(id);

        if(s == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(buildSerieJson(s)).build();
    }

     /**
     * 
     *  Route permettant d'ajouter une série
     * 
     */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSerie(@Valid Serie serie)
    {
        serie.setId(UUID.randomUUID().toString());
        serieResource.save(serie);

        URI uri = uriInfo.getBaseUriBuilder().path("series/" + serie.getId()).build();

        return Response.created(uri).build();
    }

     /**
     * 
     *  Route permettant d'ajouter une photo à une série
     * 
     */
     
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPhotoToSerie(@PathParam("id") String id, @Valid Photo photo)
    {
        Serie serie = serieResource.findById(id);
        if(serie == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        photo.setId(UUID.randomUUID().toString());
        photo.setSerie(serie);
        Photo newOne = photoResource.save(photo);

        URI uri = uriInfo.getBaseUriBuilder().path("series/" + serie.getId()).build();        

        return Response.ok().location(uri).build();
    }
}
