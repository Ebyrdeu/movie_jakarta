package dev.ebyrdeu.movie_jakarta.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
    }

    @Test
    @DisplayName("getId return correct Id assigned with setId method")
    void getIdReturnCorrectIdAssignedWithSetIdMethod() {
        UUID id = UUID.randomUUID();
        movie.setId(id);

        assertEquals(id, movie.getId());
    }

    @Test
    @DisplayName("getTitle return correct title assigned with setTitle method")
    void getTitleReturnCorrectTitleAssignedWithSetTitleMethod() {
        movie.setTitle("Roma");

        assertEquals("Roma", movie.getTitle());
    }

    @Test
    @DisplayName("getReleaseYear method return correct Year assigned with setRelese method")
    void getReleaseYearMethodReturnCorrectYearAssignedWithSetReleseMethod() {
        movie.setReleaseYear(1972);

        assertEquals(1972, movie.getReleaseYear());
    }

    @Test
    @DisplayName("getDirector method return correct Director assigned with setDirector Method")
    void getDirectorMethodReturnCorrectDirectorAssignedWithSetDirectorMethod() {
        movie.setDirector("Federico Fellini");

        assertEquals("Federico Fellini", movie.getDirector());
    }

    @Test
    @DisplayName("Movies with same id return Equals")
    void MoviesWithSameIdReturnEqual() {
        UUID id1 = UUID.randomUUID();

        Movie movie1 = new Movie();
        Movie movie2 = new Movie();

        movie1.setId(id1);
        movie2.setId(id1); // same id as movie1

        assertEquals(movie1, movie2);
    }

    @Test
    @DisplayName("Movies with different id return NotEquals")
    void MoviesWithDifferentIdReturnNotEqual() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Movie movie1 = new Movie();
        Movie movie3 = new Movie();

        movie1.setId(id1);
        movie3.setId(id2); // id different from movie1

        assertNotEquals(movie1, movie3);
    }

    @Test
    void testHashCode() {
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();

        assertEquals(movie1.hashCode(), movie2.hashCode());
    }

}