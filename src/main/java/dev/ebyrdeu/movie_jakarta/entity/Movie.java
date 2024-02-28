package dev.ebyrdeu.movie_jakarta.entity;

import java.util.UUID;


/*
    This class is used temporarily until real entity class is merged to master.
 */
public class Movie {
    private UUID id;
    private String title;
    private int releaseYear;
    private String director;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
