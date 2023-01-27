package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("bosses")
    public ModelAndView getBossesPage() {
        return new ModelAndView("users/bosses")
                .addObject("bosses", userService.getBosses());
    }

    @GetMapping("sign-up")
    public ModelAndView getSignUpPage() {
        return new ModelAndView("users/sign-up")
                .addObject("newUser", new User());
    }

    @GetMapping("sign-in")
    public ModelAndView getSignInPage() {
        return new ModelAndView("users/sign-in");
    }

    @GetMapping("{id}")
    public ModelAndView getUserPage(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return new ModelAndView("users/id")
                .addObject("userById", user);
    }

    @GetMapping("edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "OR #id.equals(authentication.principal.id)")
    public ModelAndView getUserEditPage(@PathVariable("id") Long id) {
        return new ModelAndView("users/edit")
                .addObject("userById", userService.getUserById(id));
    }

    @PostMapping
    public ModelAndView createNewUser(@ModelAttribute(name = "newUser") @Valid User user,
                                      BindingResult bindingResult,
                                      HttpServletRequest request) throws IOException, ServletException {
        if (bindingResult.hasErrors()) return new ModelAndView("redirect:/users/sign-up");
        userService.save(user);
        userService.authenticateUserAndSetSession(user, request);
        return new ModelAndView("redirect:/users/" + user.getId());
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "OR #id.equals(authentication.principal.id)")
    public ModelAndView updateUser(@PathVariable("id") Long id,
                                   @ModelAttribute("userById") @Valid User updatedUser,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "file", required = false) MultipartFile avatar) throws IOException {
        if (bindingResult.hasErrors()) return new ModelAndView("redirect:/users/edit/" + id);
        userService.save(updatedUser, avatar);
        return new ModelAndView("redirect:/users/" + id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "OR #id.equals(authentication.principal.id)")
    public ModelAndView deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ModelAndView("redirect:/");
    }
}
