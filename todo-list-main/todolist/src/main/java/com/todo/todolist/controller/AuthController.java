// package com.todo.todolist.controller;

// import com.todo.todolist.model.User;
// import com.todo.todolist.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.web.bind.annotation.*;

// import java.util.Optional;

// @RestController
// @RequestMapping("/api/auth")
// @CrossOrigin(origins = "*")
// public class AuthController {

//     @Autowired
//     private UserRepository userRepository;

//     @PostMapping("/signup")
//     public String signup(@RequestBody User user) {
//         if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
//                 user.getPassword() == null || user.getPassword().trim().isEmpty()) {
//             return "Error: Username and Password cannot be empty!";
//         }
//         if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//             return "Username already exists!";
//         }
//         userRepository.save(user);
//         return "User registered successfully!";
//     }

//     @PostMapping("/login")
//     public String login(@RequestBody User user) {
//         Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
//         if (foundUser.isPresent() && foundUser.get().getPassword().equals(user.getPassword())) {
//             return foundUser.get().getId().toString();
//         }
//         return "Invalid username or password!";
//     }

//     @GetMapping("/google-success")
//     public String googleLogin(@AuthenticationPrincipal OAuth2User principal) {
//         if (principal == null)
//             return "Login Failed";

//         String email = principal.getAttribute("email");
//         String name = principal.getAttribute("name");

//         Optional<User> existingUser = userRepository.findByUsername(email);
//         User user;
//         if (existingUser.isEmpty()) {
//             user = new User();
//             user.setUsername(email);
//             user.setPassword("GOOGLE_AUTH_USER");
//             user = userRepository.save(user);
//         } else {
//             user = existingUser.get();
//         }

//         // Dashboard par bhejte waqt localStorage set karna
//         return "<html><body><script>" +
//                 "localStorage.setItem('userId', '" + user.getId() + "');" +
//                 "localStorage.setItem('userName', '" + name + "');" +
//                 "window.location.href = '/index.html';" +
//                 "</script></body></html>";
//     }
// }/

package com.todo.todolist.controller;

import com.todo.todolist.model.User;
import com.todo.todolist.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User already exists";
        }
        userRepository.save(user);
        return user.getId().toString();
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userRepository
                .findByEmailAndPassword(user.getEmail(), user.getPassword())
                .map(u -> u.getId().toString())
                .orElse("Invalid credentials");
    }
}
