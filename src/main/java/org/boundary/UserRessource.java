package org.lpro.boundary;
import control.PasswordManagement;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.lpro.entity.Utilisateur;


@Stateless
public class UserRessource {
    
    @PersistenceContext
    EntityManager em;
    
    /**
     * cherche user by  ID
     * @param id
     * @return user
     */
    public User findById(String id) {
        return this.em.find(User.class, id);
    }
    
    /**
    * Save user 
     * @param user for save
     * @return user
     */
    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(PasswordManagement.digestPassword(user.getPassword()));
        return this.em.merge(user);
    }

    /**
     * recherche par email
     * @param adresse mail user
     * @return user or exception
     */
     public User findByEmail(String mail) {
        Query query;
        query = em.createQuery("SELECT c FROM User c WHERE c.email LIKE :monmail");
        query.setParameter("monmail", mail); 
        User user = (User) query.getSingleResult();
        return user;
    }

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

