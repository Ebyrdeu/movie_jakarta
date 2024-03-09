package dev.ebyrdeu.movie_jakarta.resource;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.dto.Movies;
import dev.ebyrdeu.movie_jakarta.entity.Movie;
import dev.ebyrdeu.movie_jakarta.repository.MovieRepository;
import dev.ebyrdeu.movie_jakarta.service.MovieService;
import jakarta.el.CompositeELResolver;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Path("/movies")
public class MovieResource {

    MovieService movieService;

    public MovieResource(){
    }
    @Inject
    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Movies all() {
        return movieService.all();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response one(@PathParam("id") @NotNull UUID id) {
            return movieService.one(id);
        }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@Valid MovieDto movieDto) {
        Movie movie = movieService.add(movieDto);
        return Response.created(
                URI.create("http://localhost:8080/app/api/movies/" + movie.getId()))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") @NotNull UUID id, @Valid MovieDto movieDetails) {
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