package com.example.task.controller;

import com.example.task.model.Expression;
import com.example.task.service.ExpressionService;
import com.example.task.util.ExpressionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/expressions")
public class CalculatorController {

    private final ExpressionValidator expressionValidator;
    private final ExpressionService expressionService;

    @Autowired
    public CalculatorController(ExpressionValidator expressionValidator,
                                ExpressionService expressionService) {
        this.expressionValidator = expressionValidator;
        this.expressionService = expressionService;
    }

    @GetMapping
    public String expressionList(Model model) {
        model.addAttribute("expressionList", expressionService.findAll());

        return "expressions/expression-list";
    }

    @GetMapping("/new")
    public String newExpression(Model model) {
        model.addAttribute("newExpression", new Expression());
        return "expressions/expression-new";
    }

    @PostMapping
    public String addExpressionForm(Expression expression, BindingResult bindingResult) {
        expressionValidator.validate(expression, bindingResult);

        if (bindingResult.hasErrors()) return "expressions/expression-new";
        expressionService.save(expression);

        return "redirect:/expressions";
    }

    @GetMapping("/{id}/edit")
    public String editExpression(@PathVariable int id, Model model) {
        model.addAttribute("expressionToEdit", expressionService.findOne(id));

        return "expressions/expression-edit";
    }

    @PatchMapping("/{id}")
    public String editExpressionForm(Expression expression,
                                     BindingResult bindingResult,
                                     @PathVariable int id) {

        expressionValidator.validate(expression, bindingResult);
        if (bindingResult.hasErrors()) return "expressions/expression-edit";

        expressionService.update(id, expression);
        return "redirect:/expressions";
    }

    @DeleteMapping("/{id}")
    public String deleteExpression(@PathVariable("id") int id, Model model) {
        model.addAttribute("deletedExpression", expressionService.findOne(id));
        expressionService.delete(id);
        return "redirect:/expressions";
    }

    @GetMapping("/search")
    public String searchExpressions() {
        return "expressions/expression-search";
    }

    @PostMapping("/search")
    public String doSearch(@RequestParam double answer,
                           @RequestParam String searchType,
                           Model model) {

        List<Expression> expressionList = expressionService.findByAnswer(answer, searchType);
        model.addAttribute("expressionsFounded", expressionList);
        return "expressions/expression-search";
    }
}
