package dev.ebyrdeu.movie_jakarta.resource;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.dto.Movies;
import dev.ebyrdeu.movie_jakarta.entity.Movie;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/movies")
public class MovieResource {
    @GET
    @Produces("text/plain")
    public Response all() {
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(MovieDto movieDto){
        Movie movie = MovieDto.map(movieDto);
        // movieRepository.update(movie)
        return Response.ok(movie).build();
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