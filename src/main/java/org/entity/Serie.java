package org.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Serie implements Serializable {

    @Id
    @GeneratedValue
    private String id;
    @NotNull
    private String ville;
    @NotNull
    private String mapOptions;
    @NotNull
    private String distance;

    public Serie() {}

    public String getDistance() {
        return distance;
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

    public void setDistance(String distance) {
        this.distance = distance;
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
}
