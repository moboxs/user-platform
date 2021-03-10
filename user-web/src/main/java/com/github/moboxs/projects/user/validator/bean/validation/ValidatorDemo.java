package com.github.moboxs.projects.user.validator.bean.validation;

import com.github.moboxs.projects.user.domain.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidatorDemo {
    public static void main(String[] args) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        User user = new User();
        //user.setPassword("***");
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        validate.forEach(e->{
            System.out.println(e.getMessage());
        });
    }
}
