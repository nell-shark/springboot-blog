package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("contact-us")
    public ModelAndView getContactUsPage() {
        return new ModelAndView("users/contact-us")
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


    @GetMapping("sign-out")
    public ModelAndView signOut(HttpServletRequest request, HttpServletResponse response) {
        userService.signOut(request, response);
        return new ModelAndView("redirect:/");
    }


    @GetMapping("edit/{id}")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public ModelAndView getUserEditPage(@PathVariable("id") Long id, Model model) {
        return new ModelAndView("users/edit");
    }

    @PostMapping
    public ModelAndView createNewUser(@ModelAttribute(name = "newUser") @Valid User user,
                                      BindingResult bindingResult,
                                      HttpServletRequest request) {
        if (bindingResult.hasErrors()) return new ModelAndView("redirect:/users/sign-up");
        userService.saveAndAuthenticate(user, request);
        return new ModelAndView("redirect:/users/" + user.getId());
    }

    // TODO: ModelAttribute
    @PatchMapping("{id}")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public ModelAndView updateUser(@PathVariable("id") Long id,
                                   @RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        userService.updateUser(id, email, password, avatar);
        return new ModelAndView("redirect:/");
    }


    @DeleteMapping("{id}")
    @PreAuthorize("#id.equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ModelAndView("redirect:/");
    }
}
