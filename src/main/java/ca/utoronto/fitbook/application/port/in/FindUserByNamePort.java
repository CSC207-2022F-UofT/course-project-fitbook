package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.User;

public interface FindUserByNamePort {
    boolean findByName(String userName);
}
