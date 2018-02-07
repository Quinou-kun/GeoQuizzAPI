package org.boundary;


import org.control.KeyManagement;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.net.URI;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.entity.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class UtilisateurRepresentation {

 
    
    @EJB
    UtilisateurRessource UserRessource;
  
    @Inject
    private KeyManagement keyManagement;
    
    
    @Context
    private UriInfo uriInfo;
    

    /**
     * 
     *  ajouter un user 
     * 
     */
    @POST
    public Response addUser(Utilisateur user, @Context UriInfo uriInfo) {
        
        try
        {
    
            Utilisateur ut = this.UserRessource.save(user);
            URI uri = uriInfo.getAbsolutePathBuilder().path(ut.getId()).build();
            String fullname = ut.getfullName();
            String email = ut.getEmail();
            
            Map fname = Collections.singletonMap("nom", fullname );
            Map map = Collections.singletonMap("email", email );
            List<Map> list = new ArrayList<>();
            list.add(map);
            list.add(fname);

            return Response.created(uri)
                    .entity(list).header("Access-Control-Allow-Origin", "*")
                    .build();
        }
         catch (Exception e) {
            
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    

    @POST
    @Path("/login")
   
      public Response login(Utilisateur ut) throws Exception {
        
        try
        {
            String email = ut.getEmail();
            String mdp = ut.getPassword();
            
            Utilisateur user = this.UserRessource.findByEmail(email) ;   
            String mdpBD = user.getPassword();
            if(!BCrypt.checkpw(mdp,mdpBD)) 
            {     
                throw new NotAuthorizedException ("Erreur d'authentification");
            }
            
            String token = issueToken(email);
            JsonObject jsonResult = Json.createObjectBuilder()
                    .add("token",  token).add("email", email).build();
            return Response.ok().entity(jsonResult).build();
        }
        
         catch (Exception e) {
            
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
       
    }
    
     private String issueToken(String email) {
        Key key = keyManagement.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(5L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println(">>>> token/key : " + jwtToken + " - " + key);
        return jwtToken;
    }
 
    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}
