package ca.utoronto.fitbook.adapter.web;

import ca.utoronto.fitbook.application.port.in.UserRegisterUseCase;
import ca.utoronto.fitbook.application.port.in.command.UserRegisterCommand;
import ca.utoronto.fitbook.application.port.out.response.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserRegisterController {
    private final UserRegisterUseCase userRegisterUseCase;

    @PostMapping(path = "/register")
    String UserRegister(Model model, @RequestBody UserRegisterCommand command) {
        UserRegisterResponse response = userRegisterUseCase.createUser(command);
        model.addAttribute("id", response.getId());
        return "index";
    }
}
