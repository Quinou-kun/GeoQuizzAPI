package org.boundary;
import org.control.PasswordManagement;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.entity.Utilisateur;

@Stateless
public class UtilisateurRessource 
{    
    @PersistenceContext
    EntityManager em;
    
    public Utilisateur findById(String id) 
    {
        return this.em.find(Utilisateur.class, id);
    }
    
    public Utilisateur save(Utilisateur user) 
    {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(PasswordManagement.digestPassword(user.getPassword()));
    
        return this.em.merge(user);
    }

    public Utilisateur findByEmail(String mail) 
    {
        Query query;
        query = em.createQuery("SELECT c FROM Utilisateur c WHERE c.email LIKE :monmail");
        query.setParameter("monmail", mail); 
        Utilisateur user = (Utilisateur) query.getSingleResult();
        
        return user;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

