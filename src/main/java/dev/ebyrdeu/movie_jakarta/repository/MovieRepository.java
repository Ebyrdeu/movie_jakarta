package dev.ebyrdeu.movie_jakarta.repository;

import dev.ebyrdeu.movie_jakarta.entity.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MovieRepository {
    @PersistenceContext(unitName = "mysql")
    EntityManager em;

    @Transactional
    public Movie saveMovie(Movie movie) {
        em.persist(movie);
        return movie;
    }

    public Optional<Movie> findById(UUID id) {
        Movie movie = em.find(Movie.class, id);
        return Optional.ofNullable(movie);
    }

    public List<Movie> findAll() {
        return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    @Transactional
    public void deleteMovie(UUID id) {
        var existingMovie = em.find(Movie.class, id);

        if (existingMovie == null) {
            throw new NotFoundException("Movie with id: " + id + " not found");
        }

        em.remove(existingMovie);
    }
    @Transactional
    public Movie updateMovie(UUID id, Movie movieDetails) {
        Movie movieToUpdate = em.find(Movie.class, id);
        if (movieToUpdate == null) {
            throw new NotFoundException("Movie with id: " + id + " not found");
        }
        movieToUpdate.setTitle(movieDetails.getTitle());
        movieToUpdate.setReleaseYear(movieDetails.getReleaseYear());
        movieToUpdate.setDirector(movieDetails.getDirector());

        return em.merge(movieToUpdate);
    }
}
