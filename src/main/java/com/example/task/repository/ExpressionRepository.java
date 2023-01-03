package com.example.task.repository;

import com.example.task.model.Expression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, Integer> {

    Optional<Expression> findByTerm(String term);

    List<Expression> findAllByAnswer(String answer);

    List<Expression> findAllByAnswerLessThan(String answer);

    List<Expression> findAllByAnswerGreaterThan(String answer);
}
