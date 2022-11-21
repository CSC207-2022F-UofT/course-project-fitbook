package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.PostCreationUseCase;
import ca.utoronto.fitbook.application.port.in.command.PostCreationCommand;
import ca.utoronto.fitbook.application.port.out.response.PostCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostCreationController {

    private final PostCreationUseCase interactor;

    @PostMapping(path = "/post")
    String createPost(Model model, PostCreationCommand command){
        PostCreationResponse outputData = interactor.createPost(command);
        model.addAttribute("id", outputData.getPostId());

        return "index";
    }
}