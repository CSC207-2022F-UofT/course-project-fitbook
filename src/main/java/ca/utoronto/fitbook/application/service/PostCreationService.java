package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.exceptions.*;
import ca.utoronto.fitbook.application.port.in.*;
import ca.utoronto.fitbook.application.port.in.command.PostCreationCommand;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.application.port.out.SaveUserPort;
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
    private final SaveUserPort saveUserPort;
    private final LoadUserPort loadUserPort;
    private final CheckUserExistsPort checkUserExistsPort;
    private final LoadExerciseListPort loadExerciseListPort;

    @Override
    public PostCreationResponse createPost(PostCreationCommand command) {
        // Certify user existence
        String userId = command.getUserId();
        if (!checkUserExistsPort.checkUserExists(userId))
            throw new UserNotFoundException(userId);

        // Check if exercise list is empty, throw exception if so
        List<String> exerciseIdList = command.getExerciseIdList();
        if (exerciseIdList.size() == 0)
            throw new EmptyExerciseListException();

        // Certify exercise existence
        try {
            loadExerciseListPort.loadExerciseList(exerciseIdList);
        } catch (EntityNotFoundException e) {
            throw new ExerciseInListNotFoundException();
        }

        // Certify proper description length
        String description = command.getDescription();
        if (description.length() > 100)
            throw new DescriptionTooLongException();

        Date date = new Date();

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

        // Save new user information to database
        saveUserPort.saveUser(user);

        // Create OutputData type and return it with needed information (post id)
        return new PostCreationResponse(newPost.getId());
    }
}
