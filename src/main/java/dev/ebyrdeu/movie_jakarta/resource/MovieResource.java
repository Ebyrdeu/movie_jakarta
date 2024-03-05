package dev.ebyrdeu.movie_jakarta.resource;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.dto.Movies;
import dev.ebyrdeu.movie_jakarta.entity.Movie;
import dev.ebyrdeu.movie_jakarta.repository.MovieRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/movies")
public class MovieResource {

    private MovieRepository movieRepository;

    public MovieResource() {
    }

    @Inject
    public MovieResource(MovieRepository movieRepository) {
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
    public Response one(@PathParam("id") @NotNull UUID id) {
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
    public Response add(@Valid MovieDto movieDto) {
        Movie movie = movieRepository.saveMovie(MovieDto.map(movieDto));
        return Response.created(URI.create("http://localhost:8080/app/api/movies/" + movie.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") @NotNull UUID id, @NotNull MovieDto movieDetails) {
        try{
            movieRepository.updateMovie(id, MovieDto.map(movieDetails));
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") @NotNull UUID id) {
        try {
            movieRepository.deleteMovie(id);
        } catch (NotFoundException notFound) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}