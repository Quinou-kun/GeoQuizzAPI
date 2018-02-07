package org.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity  
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class Utilisateur implements Serializable {
    
    
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    
    private String password;

   
    private String fullName;
    
    
    @Column(unique = true)
    private String email;
    
    // 0 pour les utilisateurs et 1 pour les admins
    private int user_rol = 0 ;

   
    /*
    @XmlElement(name="_links")
    @Transient 
    private List<Link>  links = new ArrayList<>();
    
    public List<Link> getLinks() {
        
        return links;
    }
    
    
    public void addLink(String uri, String rel) {
        
        this.links.add(new Link(rel, uri));
    }
    */

    //constructeur vide  
    public Utilisateur() {
    }
    
    public Utilisateur(String email , String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        //this.password = password;
        
    }

    public int getUserType()
    {
        return user_rol;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    //Getters et setters 
    public String getfullName()
    {
        return fullName;
    }
    
    public void setfullName(String fullName)
    {
        this.fullName = fullName;
    }
   
    
    public void setPassword(String password)
    {
        this.password = password;
    } 
    public String getPassword()
    {
        return password;
    }
    
    public String getEmail()
    {
        return email;
    }

}

