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
import java.util.UUID;

@Path("/movies")
public class MovieResource {

    private MovieRepository movieRepository;

    public MovieResource(){

    }

    @Inject
    public MovieResource(MovieRepository movieRepository){
        this.movieRepository = new MovieRepository();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(UUID uuid, MovieDto changesToBeMade){

        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(MovieDto movieDto){
        //MovieRepository.add()
        return Response.created(URI.create("http://localhost:8080")).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(MovieDto movieDto){
        Movie movie = MovieDto.map(movieDto);
        //movieRepository.delete();
        return Response.noContent().build();
    }

}