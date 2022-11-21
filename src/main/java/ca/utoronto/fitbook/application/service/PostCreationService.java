package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.PostCreationCommand;
import ca.utoronto.fitbook.application.port.in.exception.EmptyListException;
import ca.utoronto.fitbook.application.port.in.exception.UserNotFoundException;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.application.port.out.response.PostCreationResponse;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCreationService implements PostCreationUseCase{
    private final SavePostPort savePostPort;

    private final LoadUserPort loadUserPort;

    private final CheckUserExistsPort checkUserExistsPort;

    @Override
    public PostCreationResponse createPost(PostCreationCommand command) {

        // Certify user existence
        String userId = command.getUserID();
        if (!checkUserExistsPort.checkExists(userId)) {
            throw new UserNotFoundException("User does not exist");
        }

        // TODO Certify exercise existence
        List<String> exerciseIdList = command.getExerciseIdList();

        String description = command.getDescription();
        Date date = new Date();

        // Check if exercise list is empty, throw exception if so
        if (exerciseIdList.size() == 0) {
            throw new EmptyListException();
        }

        // Create new post using given information
        Post newPost = Post.builder()
                .postDate(date)
                .authorId(userId)
                .description(description)
                .exerciseIdList(exerciseIdList)
                .build();

        // Save post to database
        savePostPort.savePost(newPost);

        // Fetch user from database using UserID
        User user = loadUserPort.loadUser(userId);

        // Add post to user post list
        user.getPostIdList().add(newPost.getId());

        // Create OutputData type and return it with needed information (post id)
        return new PostCreationResponse(newPost.getId());
    }
}
