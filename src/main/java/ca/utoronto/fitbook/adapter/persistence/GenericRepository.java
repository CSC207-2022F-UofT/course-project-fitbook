package ca.utoronto.fitbook.adapter.persistence;

import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;

public interface GenericRepository<T>
{
    T getById(String id) throws EntityNotFoundException;
    void save(T entity);
}
