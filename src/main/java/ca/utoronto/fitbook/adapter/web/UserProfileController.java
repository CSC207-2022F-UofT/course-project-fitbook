package ca.utoronto.fitbook.adapter.web;

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

        model.addAttribute("id", userId.getUserId());
        model.addAttribute("name", response.getName());
        model.addAttribute("followingSize", response.getFollowingSize());
        model.addAttribute("followerSize", response.getFollowerSize());
        model.addAttribute("joinedDate", response.getJoinDate());
        model.addAttribute("likedPosts", response.getLikedPostList());
        model.addAttribute("userPosts", response.getPostList());
        model.addAttribute("userExercises", response.getUserExercises());
        return "profile";

    }
}
