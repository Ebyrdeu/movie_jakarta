package dev.ebyrdeu.movie_jakarta.resource;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.dto.Movies;
import dev.ebyrdeu.movie_jakarta.entity.Movie;
import dev.ebyrdeu.movie_jakarta.repository.MovieRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/movies")
public class MovieResource {

    private MovieRepository movieRepository;

    public MovieResource(){
    }

    @Inject
    public MovieResource(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Movies all() {
        return new Movies(movieRepository.findAll().stream().map(MovieDto::map).collect(Collectors.toList()));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response one(@PathParam("id") UUID id){
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            MovieDto movieDto = MovieDto.map(optionalMovie.get());
            return Response.ok(movieDto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(MovieDto movieDto){
        Movie movie = movieRepository.saveMovie(MovieDto.map(movieDto));
        return Response.created(URI.create("http://localhost:8080/app/api/movies" + movie.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id")UUID id,MovieDto movieDetails){
        Movie movie = movieRepository.updateMovie(id, MovieDto.map(movieDetails));
        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id){
        movieRepository.deleteMovie(id);
        return Response.noContent().build();
    }
}