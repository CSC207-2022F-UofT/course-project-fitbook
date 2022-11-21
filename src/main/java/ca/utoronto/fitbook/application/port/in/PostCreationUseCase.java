package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.port.in.command.PostCreationCommand;
import ca.utoronto.fitbook.application.port.out.response.PostCreationResponse;

public interface PostCreationUseCase {
    PostCreationResponse createPost(PostCreationCommand command);
}
