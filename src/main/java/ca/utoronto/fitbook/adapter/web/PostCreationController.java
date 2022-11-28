package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.persistence.UnauthorizedUserException;
import ca.utoronto.fitbook.application.port.in.PostCreationUseCase;
import ca.utoronto.fitbook.application.port.in.command.PostCreationCommand;
import ca.utoronto.fitbook.application.port.out.response.PostCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class PostCreationController {

    private final PostCreationUseCase postCreationUseCase;

    @PostMapping(path = "/post")
    String createPost(Model model, HttpSession session, @RequestBody PostCreationRequestBody body){

        String userId = (String) session.getAttribute("userId");
        if (userId == null){
            throw new UnauthorizedUserException();
        }

        PostCreationCommand command = new PostCreationCommand(userId,
                body.getExerciseIdList(),
                body.getDescription());

        PostCreationResponse outputData = postCreationUseCase.createPost(command);
        model.addAttribute("id", outputData.getPostId());

        return "index";
    }
}