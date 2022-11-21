package ca.utoronto.fitbook.adapter.persistence;

public interface GenericRepository<T>
{
    T getById(String id);
    void save(T entity);
}
