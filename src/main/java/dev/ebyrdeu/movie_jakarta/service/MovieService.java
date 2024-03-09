package dev.ebyrdeu.movie_jakarta.service;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.dto.Movies;
import dev.ebyrdeu.movie_jakarta.entity.Movie;
import dev.ebyrdeu.movie_jakarta.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MovieService {

    MovieRepository movieRepository;

    public MovieService(){
    }
    @Inject
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public Movies all(){
        return new Movies(
                movieRepository.findAll().stream().map(MovieDto::map).collect(Collectors.toList()));
    }

    public Response one (UUID id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            MovieDto movieDto = MovieDto.map(optionalMovie.get());
            return Response.ok(movieDto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }}

        public Movie add (MovieDto movieDto){
            return movieRepository.saveMovie(MovieDto.map(movieDto));
        }


    public Response update(UUID id, MovieDto movieDetails) {
        try {
            movieRepository.updateMovie(id, MovieDto.map(movieDetails));
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    public Response delete(UUID id) {
        try {
            movieRepository.deleteMovie(id);
        } catch (NotFoundException notFound) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
