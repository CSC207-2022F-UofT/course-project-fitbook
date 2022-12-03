package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.persistence.UnauthorizedUserException;
import ca.utoronto.fitbook.application.port.in.SearchPostsUseCase;
import ca.utoronto.fitbook.application.port.in.command.SearchCommand;
import ca.utoronto.fitbook.application.port.out.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchPostsUseCase searchUseCase;

    @GetMapping(path = "/search")
    String search(Model model, HttpSession httpSession, @RequestParam SearchCommand searchCommand) {
        if(httpSession.getId().isEmpty())
            throw new UnauthorizedUserException();
        SearchResponse searchResponse = searchUseCase.search(searchCommand);

        return "index";
    }

}
