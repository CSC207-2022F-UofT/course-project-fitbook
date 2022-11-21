package ca.utoronto.fitbook.application.port.out;

import ca.utoronto.fitbook.entity.User;

public interface SaveUserPort {
    void save(User User);
}
