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
    private String fullname;
    @Column(unique = true)
    private String email;

    public Utilisateur() {}
    
    public Utilisateur(String email , String fullName, String password) {
        this.email = email;
        this.fullname = fullName;
        this.password = password;        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getfullName()
    {
        return fullname;
    }
    
    public void setfullName(String fullName)
    {
        this.fullname = fullName;
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

