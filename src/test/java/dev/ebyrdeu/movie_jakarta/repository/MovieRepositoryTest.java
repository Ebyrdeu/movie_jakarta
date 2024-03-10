package dev.ebyrdeu.movie_jakarta.repository;

import dev.ebyrdeu.movie_jakarta.entity.Movie;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {

    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private MovieRepository movieRepository;
    private UUID movieId;
    private Movie movieDetails;
    @Mock
    private TypedQuery<Movie> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    @Test
    void whenSaveMovie_thenPersistMovie() {
        Movie movie = new Movie();

        Movie savedMovie = movieRepository.saveMovie(movie);

        verify(entityManager, times(1)).persist(movie);
        assertSame(movie, savedMovie, "The saved movie should be the same as the one returned");
    }
    @Test
    void whenFindById_thenReturnMovie() {
        UUID movieId = UUID.randomUUID();
        Movie movie = new Movie();
        movie.setId(movieId);
        when(entityManager.find(Movie.class, movieId)).thenReturn(movie);

        Optional<Movie> foundMovie = movieRepository.findById(movieId);

        assertTrue(foundMovie.isPresent(), "Movie should be found");
        assertEquals(movieId, foundMovie.get().getId(), "The movie ID should match");

        verify(entityManager, times(1)).find(Movie.class, movieId);
    }

    @Test
    void whenFindAll_thenReturnListOfMovies() {
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        List<Movie> expectedMovies = Arrays.asList(movie1, movie2);

        when(entityManager.createQuery("SELECT m FROM Movie m", Movie.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieRepository.findAll();

        assertEquals(expectedMovies.size(), actualMovies.size(), "The size of the returned movie list should match");
        assertEquals(expectedMovies, actualMovies, "The returned movie list should match the expected list");

        verify(entityManager, times(1)).createQuery("SELECT m FROM Movie m", Movie.class);
        verify(query, times(1)).getResultList();
    }
    @Test
    void deleteExistingMovie() {
        UUID movieId = UUID.randomUUID();
        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);

        when(entityManager.find(Movie.class, movieId)).thenReturn(existingMovie);

        movieRepository.deleteMovie(movieId);

        verify(entityManager).remove(existingMovie);
    }

    @Test
    void deleteNonExistingMovieThrowsNotFoundException() {
        UUID movieId = UUID.randomUUID();
        Movie nonExistingMovie = new Movie();
        nonExistingMovie.setId(movieId);

        when(entityManager.find(Movie.class, movieId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> movieRepository.deleteMovie(movieId));
    }
    @Test
    void updateExistingMovieSuccessfully() {
        movieId = UUID.randomUUID();
        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);
        existingMovie.setTitle("Original Title");
        existingMovie.setReleaseYear(2000);
        existingMovie.setDirector("Original Director");

        movieDetails = new Movie();
        movieDetails.setTitle("Updated Title");
        movieDetails.setReleaseYear(2001);
        movieDetails.setDirector("Updated Director");

        // Given
        when(entityManager.find(Movie.class, movieId)).thenReturn(existingMovie);
        when(entityManager.merge(any(Movie.class))).thenReturn(movieDetails);

        // When
        Movie updatedMovie = movieRepository.updateMovie(movieId, movieDetails);

        // Then
        verify(entityManager).merge(any(Movie.class));
        assertAll(
                () -> assertEquals("Updated Title", updatedMovie.getTitle()),
                () -> assertEquals(2001, updatedMovie.getReleaseYear()),
                () -> assertEquals("Updated Director", updatedMovie.getDirector())
        );
    }

    @Test
    void updateNonExistingMovieThrowsNotFoundException() {
        // Given
        when(entityManager.find(Movie.class, movieId)).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> movieRepository.updateMovie(movieId, movieDetails));
    }
}