package org.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Serie implements Serializable 
{
    @Id
    private String id;
    @NotNull
    private String ville;
    @NotNull
    private String mapOptions;
    @OneToMany(mappedBy="serie")
    private List<Photo> photos = new ArrayList<Photo>();
    @OneToMany(mappedBy="serie")
    private List<Game> games = new ArrayList<Game>();

    public Serie() {}

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getId() {
        return id;
    }

    public String getMapOptions() {
        return mapOptions;
    }

    public String getVille() {
        return ville;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMapOptions(String mapOptions) {
        this.mapOptions = mapOptions;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
