package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.UserLoginUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserLoginController {
    private final UserLoginUseCase userLoginUseCase;

    @PostMapping(path = "/login", consumes = "application/x-www-form-urlencoded")
    String postLoginUser(Model model, HttpSession session, UserLoginCommand command) {
        UserLoginResponse response = userLoginUseCase.loginUser(command);
        session.setAttribute("userId", response.getId());
        model.addAttribute("id", response.getId());

        return "home";
    }

    @GetMapping(path = "/login")
    String getLoginUser(){
        return "login";
    }
}
