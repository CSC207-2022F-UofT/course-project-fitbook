package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.adapter.web.model.UserProfileModel;
import ca.utoronto.fitbook.application.port.in.UserProfileUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserProfileCommand;
import ca.utoronto.fitbook.application.port.out.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserProfileController
{
    private final UserProfileUseCase userProfileUseCase;

    @GetMapping(path = "/profile/{userId}")
    String userProfile(Model model, @PathVariable UserProfileCommand userId) {
        UserProfileResponse response = userProfileUseCase.createProfile(userId);
        UserProfileModel profileModel = new UserProfileModel(
                userId.getUserId(),
                response.getName(),
                response.getFollowingSize(),
                response.getFollowerSize(),
                response.getJoinDate(),
                response.getPostList(),
                response.getLikedPostList(),
                response.getUserExercises(),
                response.getTotalLikes()
        );
        model.addAttribute("profile", profileModel);
        return "profile";
    }
}
