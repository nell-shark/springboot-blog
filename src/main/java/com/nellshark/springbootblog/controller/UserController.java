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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final String USER_TEMPLATES = "/users";

    @GetMapping("/sign-up")
    public String getSignUpPage(Model model) {
        model.addAttribute("newUser", new User());
        return USER_TEMPLATES + "/sign-up";
    }

    @PostMapping("/sign-up")
    public String createNewUser(@ModelAttribute(name = "newUser") User user,
                                HttpServletRequest request) {
        User usr = userService.saveUser(user);
        authenticateUserAndSetSession(usr.getUsername(), request);
        return "redirect:/";
    }

    @GetMapping("/sign-in")
    public String getSignInPage() {
        return USER_TEMPLATES + "/sign-in";
    }

    @GetMapping("/{id}")
    public String getUserPage(@PathVariable("id") Long id, Model model) {
        User userById = userService.getUserById(id);
        model.addAttribute("userById", userById);
        model.addAttribute("comments", userById.getComments());
        return USER_TEMPLATES + "/id";
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getListOfUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", UserRole.values());
        return USER_TEMPLATES + "/list";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String getEditPage(@PathVariable("id") Long id, Model model) {
        return USER_TEMPLATES + "/edit";
    }

    @PatchMapping("/{id}/edit")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        userService.updateUser(id, email, password, avatar);
        return "redirect:/";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/";
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") Long id,
                             @AuthenticationPrincipal User user,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        // TODO: check admin

        userService.deleteUserById(id);

        if (user.getRole().equals(UserRole.ROLE_ADMIN)) return "redirect:/users/list";
        return "redirect:/";
    }

    private void authenticateUserAndSetSession(String username, HttpServletRequest request) {
        request.getSession();
        UserDetails user = userService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
