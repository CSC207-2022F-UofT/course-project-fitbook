package ca.utoronto.fitbook.adapter.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserLogoutController {
    @GetMapping(path = "/logout")
    String logoutUser(HttpSession session) {
        session.invalidate();
        // Redirect the user to the login page
        return "redirect:login";
    }
}
