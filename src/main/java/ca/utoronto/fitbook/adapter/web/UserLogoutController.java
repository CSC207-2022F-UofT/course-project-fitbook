package ca.utoronto.fitbook.adapter.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserLogoutController {
    @GetMapping(path = "/logout")
    String logoutUser(Model model, HttpSession session) {
        session.invalidate();

        // TODO: Show a logout view
        return "login";
    }
}
