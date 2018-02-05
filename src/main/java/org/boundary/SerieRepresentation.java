package org.boundary;

import java.util.List;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.entity.Serie;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("series")
public class SerieRepresentation {

    @Inject
    SerieResource serieResource;
    
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

        if(s == null) return Response.status(Response.Status.BAD_REQUEST).build();

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
        serieResource.save(serie);
        System.out.println("uuid = " + serie.getId());
        System.out.println("mapInfos = " + serie.getMapOptions());

        return Response.ok().build();
    }
     
}
