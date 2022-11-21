package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.command.PostCreationInputData;
import ca.utoronto.fitbook.application.port.in.PostCreationInputBoundary;
import ca.utoronto.fitbook.application.port.out.response.PostCreationOutputData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PostCreationController {

    private final PostCreationInputBoundary interactor;

    @PostMapping(path = "/post")
    PostCreationOutputData create(Model model, PostCreationInputData inputData){
        PostCreationOutputData outputData = interactor.createPost(inputData);
        model.addAttribute("id", outputData.getPostId());

        return outputData;
    }
}