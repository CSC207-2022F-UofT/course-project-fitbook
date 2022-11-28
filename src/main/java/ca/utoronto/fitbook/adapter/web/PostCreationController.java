package ca.utoronto.fitbook.adapter.web;

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
    String createPost(Model model, HttpSession session, @RequestBody PostCreationCommand command){
        PostCreationResponse outputData = postCreationUseCase.createPost(command);
        model.addAttribute("id", outputData.getPostId());

        return "index";
    }
}