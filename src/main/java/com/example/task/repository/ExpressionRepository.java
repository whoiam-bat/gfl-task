package com.example.task.repository;

import com.example.task.model.Expression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, Integer> {

    Optional<Expression> findByTerm(String term);

    List<Expression> findAllByAnswer(double answer);

    List<Expression> findAllByAnswerLessThan(double answer);

    List<Expression> findAllByAnswerGreaterThan(double answer);
}
