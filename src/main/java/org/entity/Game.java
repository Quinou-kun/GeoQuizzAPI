package org.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Game implements Serializable 
{
    @Id
    private String id;
    @NotNull
    private String token;
    @NotNull
    private int nbPhotos;
    @NotNull
    private String status;
    private int score; 
    @NotNull
    private String player;

    public Game() {}

    public String getId() {
        return id;
    }
    
    public int getNbPhotos() {
        return nbPhotos;
    }
    
    public String getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNbPhotos(int nbPhotos) {
        this.nbPhotos = nbPhotos;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
