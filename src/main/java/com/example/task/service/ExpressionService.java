package com.example.task.service;

import com.example.task.model.Expression;
import com.example.task.repository.ExpressionRepository;
import com.example.task.util.ExpressionSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExpressionService {

    private final ExpressionRepository repository;

    private final ExpressionSolver expressionSolver;

    @Autowired
    public ExpressionService(ExpressionRepository repository, ExpressionSolver expressionSolver) {
        this.repository = repository;
        this.expressionSolver = expressionSolver;
    }

    public List<Expression> findAll() {
        return repository.findAll();
    }

    public Expression findOne(int id) {
        Optional<Expression> expression = repository.findById(id);
        return expression.orElse(null);
    }

    public Optional<Expression> findByTerm(String term) {
        return repository.findByTerm(term);
    }

    public List<Expression> findAllByAnswer(BigDecimal answer) {
        return repository.findAllByAnswer(answer);
    }

    public List<Expression> findAllByAnswerLessThan(BigDecimal answer) {
        return repository.findAllByAnswerLessThan(answer);
    }

    public List<Expression> findAllByAnswerGreaterThan(BigDecimal answer) {
        return repository.findAllByAnswerGreaterThan(answer);
    }

    @Transactional
    public void save(Expression expression) {
        expressionSolver.solveExpression(expression);

        repository.save(expression);
    }

    @Transactional
    public void update(int id, Expression updateExpression) {
        updateExpression.setId(id);
        expressionSolver.solveExpression(updateExpression);

        repository.save(updateExpression);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

}
