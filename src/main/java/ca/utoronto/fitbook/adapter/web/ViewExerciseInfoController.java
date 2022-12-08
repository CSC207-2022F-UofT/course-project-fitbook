package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.ViewExerciseInfoUseCase;
import ca.utoronto.fitbook.application.port.out.response.ViewExerciseInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ViewExerciseInfoController {

    private final ViewExerciseInfoUseCase viewExerciseInfoUseCase;

    @GetMapping(path = "/post")
    String viewInfo(Model model, HttpSession session){
        // Get session userId, throw error if null
//        String userId = (String) session.getAttribute("userId");
//        if (userId == null)
//            throw new UnauthorizedUserException();

        // Create output data using service method
        ViewExerciseInfoResponse outputData = viewExerciseInfoUseCase.viewInfo();
        model.addAttribute("tempExerciseList", outputData.getTemporalExercises());
        model.addAttribute("repExerciseList", outputData.getRepetitiveExercises());

        return "infoPage";
    }
}
