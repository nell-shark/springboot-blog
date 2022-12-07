package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    private final String TEMPLATES_FOLDER = "users/";


    @GetMapping("/sign-up")
    public String signUp() {
        return TEMPLATES_FOLDER + "sign-up";
    }

    @PostMapping("/sign-up")
    public String createNewUser(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        User user = userService.saveUser(new User(email, password));
        authenticateUserAndSetSession(user.getUsername(), request);
        return "redirect:/";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return TEMPLATES_FOLDER + "sign-in";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/";
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getListUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", UserRole.values());
        return TEMPLATES_FOLDER + "list";
    }

    @GetMapping("/{id}")
    public String getUserPage(@PathVariable("id") Long id,
                              Model model,
                              @AuthenticationPrincipal User user) {
        if (user != null) model.addAttribute("user", user);
        model.addAttribute("user", userService.getUserById(id));
        return TEMPLATES_FOLDER + "id";
    }


    public void authenticateUserAndSetSession(String username, HttpServletRequest request) {
        request.getSession();
        UserDetails user = userService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
