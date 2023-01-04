package com.example.task.util;

import com.example.task.model.Expression;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class ExpressionSolver {

    public void solveExpression(Expression expression) {
        String term = expression.getTerm();

        String prepared = prepareException(term);
        String polishNotation = expressionToPolishNotation(prepared);
        double answer = polishNotationToAnswer(polishNotation);
        expression.setAnswer(answer);
    }

    private String prepareException(String term) {

        StringBuilder preparedExpression = new StringBuilder();

        for (int i = 0; i < term.length(); i++) {
            char ch = term.charAt(i);

            if (ch == '-' || ch == '+') {
                if (i == 0 || term.charAt(i - 1) == '(') preparedExpression.append('0');
                else if (term.charAt(i - 1) == '+' || term.charAt(i - 1) == '-') {
                    char tempCh = term.charAt(i - 1);
                    preparedExpression.append(tempCh).append('0');
                } else if (term.charAt(i - 1) == '/' || term.charAt(i - 1) == '*') {
                    preparedExpression.append('(');
                    preparedExpression.append('0').append(ch);
                    int j = i + 1;
                    for (; j < term.length(); j++) {
                        if (getPriority(term.charAt(j)) == 0)
                            preparedExpression.append(term.charAt(j));
                        else break;

                    }
                    i = j;
                    preparedExpression.append(')');
                    continue;
                }
            }
            preparedExpression.append(ch);
        }

        return preparedExpression.toString();
    }

    private String expressionToPolishNotation(String term) {
        StringBuilder current = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        int priority;
        for (int i = 0; i < term.length(); i++) {
            priority = getPriority(term.charAt(i));

            if (priority == 0) current.append(term.charAt(i));
            if (priority == 1) stack.push(term.charAt(i));

            if (priority > 1) {
                current.append(' ');
                while (!stack.empty()) {
                    if (getPriority(stack.peek()) >= priority) current.append(stack.pop());
                    else break;
                }
                stack.push(term.charAt(i));
            }

            if (priority == -1) {
                current.append(' ');
                while (getPriority(stack.peek()) != 1)
                    current.append(stack.pop());
                stack.pop();
            }
        }

        while (!stack.empty()) current.append(stack.pop());

        return current.toString();
    }

    private double polishNotationToAnswer(String polishNotation) {
        StringBuilder operand;
        Stack<Double> stack = new Stack<>();

        for (int i = 0; i < polishNotation.length(); i++) {

            if (polishNotation.charAt(i) == ' ') continue;
            if (getPriority(polishNotation.charAt(i)) == 0) {
                operand = new StringBuilder();
                while (polishNotation.charAt(i) != ' ' && getPriority(polishNotation.charAt(i)) == 0) {
                    operand.append(polishNotation.charAt(i++));
                    if (i == polishNotation.length()) break;
                }
                stack.push(Double.parseDouble(operand.toString()));
            }
            if (getPriority(polishNotation.charAt(i)) > 1) {
                double firstVar = stack.pop();
                double secondVar = stack.pop();

                if (polishNotation.charAt(i) == '+') stack.push(secondVar + firstVar);
                if (polishNotation.charAt(i) == '-') stack.push(secondVar - firstVar);
                if (polishNotation.charAt(i) == '*') stack.push(secondVar * firstVar);
                if (polishNotation.charAt(i) == '/') stack.push(secondVar / firstVar);
            }
        }
        return stack.pop();
    }

    private int getPriority(char ch) {
        return switch (ch) {
            case '*', '/' -> 3;
            case '+', '-' -> 2;
            case '(' -> 1;
            case ')' -> -1;
            default -> 0;
        };
    }
}
