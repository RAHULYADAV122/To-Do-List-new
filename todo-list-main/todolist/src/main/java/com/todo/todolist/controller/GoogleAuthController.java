package com.todo.todolist.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.todo.todolist.model.User;
import com.todo.todolist.repository.UserRepository;

@Controller
public class GoogleAuthController {

    private final UserRepository userRepository;

    public GoogleAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login-success")
    public String googleSuccess(OAuth2AuthenticationToken token) {

        String email = token.getPrincipal().getAttribute("email");
        String name = token.getPrincipal().getAttribute("name");

        // 🔥 DB me user ensure karo
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name);
                    u.setPassword("GOOGLE_LOGIN"); // dummy
                    return userRepository.save(u);
                });

        // 🔥 userId frontend ko bhejna
        return "redirect:/index.html?userId=" + user.getId() + "&name=" + name;
    }
}
