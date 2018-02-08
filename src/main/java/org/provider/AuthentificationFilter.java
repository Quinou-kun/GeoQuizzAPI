package org.provider;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.boundary.UtilisateurRessource;
import org.control.KeyManagement;
import org.entity.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.Key;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthentificationFilter implements ContainerRequestFilter
{
    @Inject 
    private KeyManagement keyManagement;

    @Inject
    UtilisateurRessource utilisateurResource;

    @Override 
    public void filter(ContainerRequestContext requestContext)
    {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            throw new NotAuthorizedException("Probleme header autorisation");
        } 

        String token = authHeader.substring("Bearer".length()).trim();

        try
        {
            Key key = keyManagement.generateKey();
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            String email = (String) claims.getBody().get("sub");

            Utilisateur user = utilisateurResource.findByEmail(email);

            if(user == null) throw new Exception("User doesn't exist");

            System.out.println(">>>> token valide : " + token);
        }
        catch(Exception e)
        {
            System.out.println(">>>> token invalide : " + token);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}