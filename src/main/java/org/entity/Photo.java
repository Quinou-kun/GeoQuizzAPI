package org.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Photo implements Serializable 
{
    @Id
    private String id;
    @NotNull
    private String description;
    @NotNull
    private String position;
    @NotNull
    private String url;
    @ManyToOne(fetch=FetchType.LAZY)
    private Serie serie;

    public Photo() {}

    public String getDescription() {
        return description;
    }
    
    public String getId() {
        return id;
    }
    
    public String getPosition() {
        return position;
    }
    
    public String getUrl() {
        return url;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }
}
