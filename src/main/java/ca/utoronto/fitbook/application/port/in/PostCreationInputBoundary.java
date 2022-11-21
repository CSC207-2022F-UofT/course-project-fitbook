package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.PostCreationInputData;
import ca.utoronto.fitbook.application.port.out.response.PostCreationOutputData;

public interface PostCreationInputBoundary {
    PostCreationOutputData createPost(PostCreationInputData inputData);
}
