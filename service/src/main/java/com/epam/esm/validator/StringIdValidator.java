package com.epam.esm.validator;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StringIdValidator {
    private static final String ID_REGEX = "\\d+";
    private static final String ID_IS_INVALIDATE = "Id must be an integer";

    public static List<String> idValidate(String id) {
        List<String> errors = new java.util.ArrayList<>(Collections.emptyList());
        if(!id.matches(ID_REGEX)) {
            errors.add(ID_IS_INVALIDATE);
        }
        return errors;
    }
}
