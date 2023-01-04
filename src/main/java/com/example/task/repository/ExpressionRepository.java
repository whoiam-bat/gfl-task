package com.example.task.repository;

import com.example.task.model.Expression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, Integer> {

    Optional<Expression> findByTerm(String term);

    List<Expression> findAllByAnswer(BigDecimal answer);

    List<Expression> findAllByAnswerLessThan(BigDecimal answer);

    List<Expression> findAllByAnswerGreaterThan(BigDecimal answer);
}
