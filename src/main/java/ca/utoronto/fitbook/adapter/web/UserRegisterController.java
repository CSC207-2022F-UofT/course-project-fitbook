package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.UserRegisterUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserRegisterController {
    private final UserRegisterUseCase userRegisterUseCase;

    @PostMapping(path = "/register", consumes = "application/x-www-form-urlencoded")
    String postRegisterUser(Model model, HttpSession session, UserRegisterCommand command) {
        UserRegisterResponse response = userRegisterUseCase.createUser(command);
        session.setAttribute("userId", response.getId());
        model.addAttribute("id", response.getId());

        // TODO: Show a register success view
        return "home";
    }

    @GetMapping(path = "/register")
    String getRegisterUser(){
        return "register";
    }
}
