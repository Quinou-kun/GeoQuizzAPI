package org.boundary;


import org.control.KeyManagement;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.net.URI;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.entity.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class UtilisateurRepresentation 
{    
    @EJB
    UtilisateurRessource UserRessource;
  
    @Inject
    private KeyManagement keyManagement;
    
    
    @Context
    private UriInfo uriInfo;
    

    /**
     * 
     *  Ajouter un user 
     * 
     */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(@Valid Utilisateur user) 
    {    
        try
        {
            Utilisateur ut = this.UserRessource.save(user);
            
            JsonValue json = Json.createObjectBuilder()
                .add("fullname", ut.getfullName())
                .add("email", ut.getEmail())
                .add("token", issueToken(ut.getEmail()))
                .build();

            URI uri = uriInfo.getAbsolutePathBuilder().path(ut.getId()).build();

            return Response.created(uri)
                    .entity(json).header("Access-Control-Allow-Origin", "*")
                    .build();
        }
        catch (Exception e) 
        {    
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * 
     *  Connecter un user 
     * 
     */
    
    @GET
    public Response login(@DefaultValue("") @QueryParam("email") String email, 
                            @DefaultValue("") @QueryParam("password") String pwd) throws Exception 
    {
        Utilisateur user = UserRessource.findByEmail(email);

        if(user == null) return Response.status(Response.Status.NOT_FOUND).build();
            
        String mdpBD = user.getPassword();

        if(!BCrypt.checkpw(pwd, mdpBD)) 
        {     
            throw new NotAuthorizedException ("Erreur d'authentification");
        }
            
        String token = issueToken(email);
        JsonObject jsonResult = Json.createObjectBuilder()
            .add("fullname", user.getfullName())
            .add("email", email)        
            .add("token",  token)
            .build();

        return Response.ok(jsonResult).build();
    }
    
     private String issueToken(String email) {
        Key key = keyManagement.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(30L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return jwtToken;
    }
 
    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
