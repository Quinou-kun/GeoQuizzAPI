package org.boundary;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
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

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

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

    @GET
    public Response getPosts() 
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
            .add("distance", s.getDistance())
            .build();

		return json;
	}
}
