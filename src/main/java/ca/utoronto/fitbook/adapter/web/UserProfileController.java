package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.persistence.UnauthorizedUserException;
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

    /**
     * @param model profile display information
     * @param session current user's web session
     * @param profileId id of the requested user's profile
     * @return  user profile view page
     */
    @GetMapping(path = "/profile/{profileId}")
    String userProfile(Model model, HttpSession session, @PathVariable String profileId) {
        // Get session's userId, throw exception if null
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            throw new UnauthorizedUserException();

        // Create input data with session's userId
        UserProfileCommand command = new UserProfileCommand(profileId, userId);

        // Receive output data with use case method
        UserProfileResponse response = userProfileUseCase.findProfile(command);

        // Add profile information to display to model
        model.addAttribute("profile", response);
        model.addAttribute("currUser", userId);
        return "profile";
    }
}
