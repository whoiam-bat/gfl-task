package com.example.task.util;

import com.example.task.model.Expression;
import com.example.task.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExpressionValidator implements Validator {

    private final ExpressionService expressionService;

    @Autowired
    public ExpressionValidator(ExpressionService expressionService) {
        this.expressionService = expressionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Expression.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Expression expression = (Expression) target;

        if (expressionService.findByTerm(expression.getTerm()).isPresent())
            errors.rejectValue("term", "", "Such expression already exists");

        if (!validateBrackets(expression.getTerm()))
            errors.rejectValue("term", "", "Incorrect brackets input");

        if (!validateMathOperators(expression.getTerm()))
            errors.rejectValue("term", "", "Incorrect math operators input");

        if (validateThirdPartyCharacters(expression.getTerm()))
            errors.rejectValue("term", "", "Incorrect character in expression");
    }

    private boolean validateBrackets(String expression) {
        int bracketsCount = 0;

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') bracketsCount++;
            else if (expression.charAt(i) == ')') bracketsCount--;
            if (bracketsCount < 0) return false;
        }
        return bracketsCount == 0;
    }

    private boolean validateMathOperators(String expression) {
        Pattern incorrectOperators = Pattern.compile("[-+/*]{2}");
        Pattern correctOperators = Pattern.compile("(/[-+])|(\\*[-+])|([-+]{2})");

        Matcher incorrectMatcher = incorrectOperators.matcher(expression);
        Matcher correctMatcher = correctOperators.matcher(expression);

        boolean isValid = false;

        if (incorrectMatcher.find()) {
            if (correctMatcher.find()) {
                isValid = true;
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

    private boolean validateThirdPartyCharacters(String expression) {
        Pattern pattern = Pattern.compile("[^\\d*/+\\-()]");
        Matcher matcher = pattern.matcher(expression);

        return matcher.find();
    }
}
