package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.UserLoginUseCase;
import ca.utoronto.fitbook.application.port.in.UserRegisterUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserLoginCommand;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserLoginResponse;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserLoginController {
    private final UserLoginUseCase userLoginUseCase;

    @PostMapping(path = "/login")
    String UserLogin(Model model, @RequestBody UserLoginCommand command) {
        UserLoginResponse response = userLoginUseCase.loginUser(command);
        model.addAttribute("id", response.getId());
        return "index";
    }
}
