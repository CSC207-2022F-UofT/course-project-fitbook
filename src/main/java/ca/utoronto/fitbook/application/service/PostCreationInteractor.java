package ca.utoronto.fitbook.application.service;

import ca.utoronto.fitbook.application.port.in.EmptyListException;
import ca.utoronto.fitbook.application.port.in.command.PostCreationInputData;
import ca.utoronto.fitbook.application.port.in.PostCreationInputBoundary;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.application.port.in.LoadUserPort;
import ca.utoronto.fitbook.application.port.out.response.PostCreationOutputData;
import ca.utoronto.fitbook.entity.Post;
import ca.utoronto.fitbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCreationInteractor implements PostCreationInputBoundary{

    private final SavePostPort savePostPort;

    private final LoadUserPort loadUserPort;

    @Override
    public PostCreationOutputData createPost(PostCreationInputData inputData) throws EmptyListException {
        String UserID = inputData.getUserID();
        String description = inputData.getDescription();
        List<String> exrLst = inputData.getExerciseIdList();
        Date date = new Date();

        // Check if exercise list is empty, throw exception if so
        if (exrLst.size() == 0) {
            throw new EmptyListException();
        }

        // Create new post using given information
        Post newPost = Post.builder()
                .postDate(date)
                .authorId(UserID)
                .description(description)
                .exerciseIdList(exrLst)
                .build();

        // Save post to database
        savePostPort.savePost(newPost);

        // Fetch user from database using UserID
        User user = loadUserPort.loadUser(UserID);

        // Add post to user post list
        user.getPostIdList().add(newPost.getId());

        // Create OutputData type and return it with needed information
        return new PostCreationOutputData(newPost.getId());
    }
}
