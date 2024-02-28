package dev.ebyrdeu.movie_jakarta.dto;

import dev.ebyrdeu.movie_jakarta.entity.Movie;

public record MovieDto(String title, int releaseYear, String director) {

    public static MovieDto map(Movie movie){
        return new MovieDto(movie.getTitle(), movie.getReleaseYear(), movie.getDirector());
    }

    public static Movie map(MovieDto movieDto){
        var movie = new Movie();
        movie.setTitle(movieDto.title);
        movie.setReleaseYear(movieDto.releaseYear);
        movie.setDirector(movieDto.director);
        return movie;
    }
}