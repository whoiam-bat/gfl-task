package com.example.task.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expression")
public class Expression {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "term")
    private String term;

    @Column(name = "answer")
    private BigDecimal answer;

    public Expression() {
    }

    public Expression(String term, BigDecimal answer) {
        this.term = term;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public BigDecimal getAnswer() {
        return answer;
    }

    public void setAnswer(BigDecimal answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "id=" + id +
                ", term='" + term + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
