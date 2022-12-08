package ca.utoronto.fitbook.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController
{
    @GetMapping(path = "/")
    String index(HttpSession session) {
        // Redirect user to login page if they are not logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            return "redirect:login";

        // Redirect user to their feed if they are logged in
        return "redirect:feed";
    }
}
