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

        String term = expression.getTerm();
        if (expressionService.findByTerm(term).isPresent())
            errors.rejectValue("term", "", "Such expression already exists");

        if (!validateBrackets(term))
            errors.rejectValue("term", "", "Incorrect brackets input");

        if (!validateMathOperators(term))
            errors.rejectValue("term", "", "Incorrect math operators input");

        if (validateStringRegex("[^\\d*/+\\-()]", term))
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
        if (validateStringRegex("[-+/*]{2}", expression)) {
            return validateStringRegex("(/[-+])|(\\*[-+])|([-+]{2})", expression);
        }
        return true;
    }

    private boolean validateStringRegex(String regex, String string) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
}
