package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.persistence.UnauthorizedUserException;
import ca.utoronto.fitbook.adapter.web.model.UserProfileModel;
import ca.utoronto.fitbook.application.port.in.UserProfileUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserProfileController
{
    private final UserProfileUseCase userProfileUseCase;

    @GetMapping(path = "/profile/{profileId}")
    String userProfile(Model model, HttpSession session, @PathVariable String profileId) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null){
            throw new UnauthorizedUserException();
        }
        UserProfileCommand command = new UserProfileCommand(profileId, userId);
        UserProfileResponse response = userProfileUseCase.findProfile(command);
        UserProfileModel profileModel = UserProfileModel.fromResponseToModel(response);
        model.addAttribute("profile", profileModel);
        return "profile";
    }
}
