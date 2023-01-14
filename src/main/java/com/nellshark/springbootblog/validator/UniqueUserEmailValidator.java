package com.nellshark.springbootblog.validator;

import com.nellshark.springbootblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {
    private final UserService userService;

    @Override
    public void initialize(UniqueUserEmail unique) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.existsEmail(email);
    }
}