package usecase.profile;
import entity.User;
import java.util.Optional;

public class FindUser {
    private final UserRepository repository;

    public Optional<User> findById(final String id){
        // User IDs are accessible in a user's feed, allowing the lookup of other users.
        return repository.findById(id);
    }
}
