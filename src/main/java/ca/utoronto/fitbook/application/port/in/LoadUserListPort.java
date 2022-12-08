package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.entity.User;

import java.util.List;

public interface LoadUserListPort
{
    List<User> loadUserList(List<String> userIds) throws EntityNotFoundException;
}
