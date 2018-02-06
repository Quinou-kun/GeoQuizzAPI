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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.control.RandomToken;
import org.entity.Game;
import org.entity.Photo;
import org.entity.Serie;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("games")
public class GameRepresentation {

    @Inject
    GameResource gameResource;
    
    @Inject
    SerieResource serieResource;

    @Context
    UriInfo uriInfo;

    /**
     * 
     *  Route permettant de créer une nouvelle partie
     * 
     */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeries(@DefaultValue("") @QueryParam("idSerie") String idSerie, 
                                @DefaultValue("Anonyme") @QueryParam("playerName") String playerName) 
    {
        if(idSerie.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();

        Serie s = serieResource.findById(idSerie);

        Game newGame = new Game();
        newGame.setId(UUID.randomUUID().toString());
        newGame.setNbPhotos(s.getPhotos().size());
        newGame.setPlayer(playerName);
        newGame.setScore(0);
        newGame.setStatus("en cours");
        newGame.setToken(new RandomToken().randomString(20));
        newGame.setSerie(s);

        gameResource.save(newGame);

        URI uri = uriInfo.getAbsolutePathBuilder().path(newGame.getId()).build();

        JsonValue json = Json.createObjectBuilder()
            .add("id", newGame.getId())
            .add("nb_photos",newGame.getNbPhotos())
            .add("player", newGame.getPlayer())
            .add("score", newGame.getScore())
            .add("status", newGame.getStatus())
            .add("token", newGame.getToken())
            .add("serie", buildJsonForSerie(s))
            .build();

        return Response.ok(json).header("Location", uri).build();
    }

    private JsonValue buildJsonForSerie(Serie s) 
    {
        JsonObject json = Json.createObjectBuilder()
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
            JsonObject json = Json.createObjectBuilder()
                .add("id", p.getId())
                .add("desc", p.getDescription())
                .add("position", p.getPosition())
                .add("url", p.getUrl())
                .build();
            
            jab.add(json);
        }

		return jab.build();
	}
}
