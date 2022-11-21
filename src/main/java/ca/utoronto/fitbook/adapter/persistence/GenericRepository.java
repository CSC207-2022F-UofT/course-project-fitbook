package ca.utoronto.fitbook.adapter.persistence;

import ca.utoronto.fitbook.application.port.in.EntityNotFoundException;

public interface GenericRepository<T>
{
    T getById(String id) throws EntityNotFoundException;
    void save(T entity);
}
