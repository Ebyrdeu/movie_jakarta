package dev.ebyrdeu.movie_jakarta.repository;

import dev.ebyrdeu.movie_jakarta.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovieRepository {
    private EntityManager entityManager;

    public MovieRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Movie saveMovie(Movie movie) {
        try {
            entityManager.getTransaction().begin();
            if (movie.getId() == null) {
                entityManager.persist(movie);
            }else{
                movie = entityManager.merge(movie);
            }
            entityManager.getTransaction().commit();
            return movie;
        }catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    public Optional<Movie> findById(UUID id) {
        Movie movie = entityManager.find(Movie.class, id);
        return movie != null ? Optional.of(movie) : Optional.empty();
    }

    public List<Movie> findAll() {
        TypedQuery<Movie> query = entityManager.createQuery("SELECT m FROM Movie m", Movie.class);
        return query.getResultList();
    }

    public void deleteMovie(Movie movie){
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(movie);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e){
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
}
