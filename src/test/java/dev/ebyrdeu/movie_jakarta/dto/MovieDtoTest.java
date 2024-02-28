package dev.ebyrdeu.movie_jakarta.dto;

import dev.ebyrdeu.movie_jakarta.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieDtoTest {
    private Movie movie;
    private MovieDto movieDto;

    @BeforeEach
    void setUp(){
        movie = new Movie();
        movie.setReleaseYear(2023);
        movie.setTitle("Oppenheimer");
        movie.setDirector("Christopher Nolan");

        movieDto = new MovieDto(
                "Oppenheimer",
                2023,
                "Christopher Nolan");

    }

    @Test
    @DisplayName("DTO should contain entity fields after mapping")
    void dtoShouldContainEntityFieldsAfterMapping(){
        MovieDto movieDto = MovieDto.map(movie);
        assertEquals("Oppenheimer", movieDto.title());
        assertEquals(2023, movieDto.releaseYear());
        assertEquals("Christopher Nolan", movieDto.director());
    }

    @Test
    @DisplayName("Entity should contain DTO fields after mapping")
    void entityShouldContainDtoFieldsAfterMapping(){
        Movie movie = MovieDto.map(movieDto);
        assertEquals("Oppenheimer", movie.getTitle());
        assertEquals(2023, movie.getReleaseYear());
        assertEquals("Christopher Nolan", movie.getDirector());
    }

    @Test
    @DisplayName("Mapping a DTO should return entity class")
    void mappingADtoShouldReturnEntityClass(){
        var movie = MovieDto.map(movieDto);
        assertEquals(movie.getClass(), Movie.class);
    }

    @Test
    @DisplayName("Mapping an entity should return DTO class")
    void mappingAnEntityShouldReturn(){
        var movieDto = MovieDto.map(movie);
        assertEquals(movieDto.getClass(), MovieDto.class);
    }

}