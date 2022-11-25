package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.User;

public interface LoadUserByNamePort {
    User loadUserByName(String name);
}
