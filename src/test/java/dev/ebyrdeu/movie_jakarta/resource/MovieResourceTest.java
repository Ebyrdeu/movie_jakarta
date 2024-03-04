package dev.ebyrdeu.movie_jakarta.resource;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.entity.Movie;
import dev.ebyrdeu.movie_jakarta.repository.MovieRepository;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieResourceTest {

    @Mock
    MovieRepository movieRepository;
    Dispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        var resource = new MovieResource(movieRepository);
        dispatcher.getRegistry().addSingletonResource(resource);
    }

    @Test
    @DisplayName("Return empty list when get is called")
    void returnEmptyListWhenGetIsCalled() throws URISyntaxException, UnsupportedEncodingException {
        when(movieRepository.findAll()).thenReturn(List.of());
        MockHttpRequest request = MockHttpRequest.get("/movies");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals("{\"movieDto\":[]}", response.getContentAsString());
    }

    @Test
    @DisplayName("Return status 201 when created")
    void returnStatus201WhenCreated() throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.post("/movies");
        when(movieRepository.saveMovie(any())).thenReturn(MovieDto.map(new MovieDto(
                "Skyfall",
                2012,
                "Sam Mendes")));
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(("{\n" +
                "  \"title\": \"Skyfall\",\n" +
                "  \"releaseYear\": 2012,\n" +
                "  \"director\": \"Sam Mendes\"\n" +
                "}").getBytes());
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("Return status 200 ok when updated")
    void returnStatus200OkWhenUpdated() throws URISyntaxException {
        UUID uuid = UUID.randomUUID();
        MockHttpRequest request = MockHttpRequest.put("/movies/" + uuid);
        when(movieRepository.updateMovie(any(), any())).thenReturn(MovieDto.map(new MovieDto(
                "Skyfall",
                2012,
                "Sam Mendes")));
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(("{\n" +
                "  \"title\": \"Skyfall\",\n" +
                "  \"releaseYear\": 2012,\n" +
                "  \"director\": \"Sam Mendes\"\n" +
                "}").getBytes());
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Return 404 when updating entity that does not exist")
    void return404WhenUpdatingEntityThatDoesNotExist() throws URISyntaxException {
        UUID uuid = UUID.randomUUID();
        MockHttpRequest request = MockHttpRequest.put("/movies/" + uuid);
        when(movieRepository.updateMovie(any(), any())).thenThrow(NotFoundException.class);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(("{\n" +
                "  \"title\": \"Skyfall\",\n" +
                "  \"releaseYear\": 2012,\n" +
                "  \"director\": \"Sam Mendes\"\n" +
                "}").getBytes());
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Return status 404 when id was not found")
    void returnStatus404WhenIdWasNotFound() throws URISyntaxException {
        UUID id = UUID.randomUUID();
        when(movieRepository.findById(id)).thenReturn(Optional.empty());
        MockHttpRequest request = MockHttpRequest.get("/movies/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Return status 200 when id is found in get")
    void returnsStatus200WhenIdIsFoundInGet() throws URISyntaxException {
        UUID id = UUID.randomUUID();
        when(movieRepository.findById(id)).thenReturn(Optional.of(new Movie()));
        MockHttpRequest request = MockHttpRequest.get("/movies/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Return status 404 when delete cannot find id")
    void returnStatus404WhenDeleteCannotFindId() throws URISyntaxException {
        UUID id = UUID.randomUUID();
        doThrow(NotFoundException.class).when(movieRepository).deleteMovie(id);
        MockHttpRequest request = MockHttpRequest.delete("/movies/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }


    @Test
    @DisplayName("Return status 204 when object is deleted")
    void returnStatus204WhenObjectIsDeleted() throws URISyntaxException {
        UUID id = UUID.randomUUID();
        MockHttpRequest request = MockHttpRequest.delete("/movies/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(204, response.getStatus());
    }

    @Test
    @DisplayName("Constructor creates objects of MovieResource class")
    void constructorCreatesObjectsOfMovieResourceClass(){
        MovieResource movieResource = new MovieResource();
        assertEquals(MovieResource.class,movieResource.getClass());
    }

}