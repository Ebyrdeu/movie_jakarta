package dev.ebyrdeu.movie_jakarta.resource;

import dev.ebyrdeu.movie_jakarta.dto.MovieDto;
import dev.ebyrdeu.movie_jakarta.dto.Movies;
import dev.ebyrdeu.movie_jakarta.service.MovieService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieResourceTest {

    @Mock
    MovieService movieService;
    Dispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        var resource = new MovieResource(movieService);
        dispatcher.getRegistry().addSingletonResource(resource);
    }

    @Test
    @DisplayName("Return empty list when get is called")
    void returnEmptyListWhenGetIsCalled() throws URISyntaxException, UnsupportedEncodingException {
        when(movieService.all()).thenReturn(new Movies(List.of()));
        MockHttpRequest request = MockHttpRequest.get("/movies");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals("{\"movieDto\":[]}", response.getContentAsString());
    }

    @Test
    @DisplayName("Return status 201 when created")
    void returnStatus201WhenCreated() throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.post("/movies");
        when(movieService.add(any())).thenReturn(MovieDto.map(new MovieDto(
                UUID.randomUUID(),
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

        when(movieService.update(any(), any())).thenReturn(Response.ok().build());
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(("{\n" +
                "  \"title\": \" Skyfall\",\n" +
                "  \"releaseYear\": 2012,\n" +
                "  \"director\": \" Sam Mendes\"\n" +
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
        when(movieService.update(any(), any())).thenThrow(NotFoundException.class);
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(("{\n" +
                "  \"title\": \" Skyfall\",\n" +
                "  \"releaseYear\": 2012,\n" +
                "  \"director\": \" Sam Mendes\"\n" +
                "}").getBytes());
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Return status 404 when id was not found")
    void returnStatus404WhenIdWasNotFound() throws URISyntaxException {
        UUID id = UUID.randomUUID();

        when(movieService.one(id)).thenReturn(Response.status(Response.Status.NOT_FOUND).build());
        MockHttpRequest request = MockHttpRequest.get("/movies/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Return status 200 when id is found in get")
    void returnsStatus200WhenIdIsFoundInGet() throws URISyntaxException {
        UUID id = UUID.randomUUID();
        MovieDto movieDto = new MovieDto (id, "skyfall", 2012, "Sam Mendes");

        when(movieService.one(id)).thenReturn(Response.ok(movieDto).build());
        MockHttpRequest request = MockHttpRequest.get("/movies/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Return status 404 when delete cannot find id")
    void returnStatus404WhenDeleteCannotFindId() throws URISyntaxException {
        UUID id = UUID.randomUUID();

        doThrow(NotFoundException.class).when(movieService).delete(id);
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
    void constructorCreatesObjectsOfMovieResourceClass() {
        MovieResource movieResource = new MovieResource();
        assertEquals(MovieResource.class, movieResource.getClass());
    }

}