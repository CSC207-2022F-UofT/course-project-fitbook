package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.User;

public interface LoadUserPort
{
    User loadUser(String id) throws EntityNotFoundException;
}
