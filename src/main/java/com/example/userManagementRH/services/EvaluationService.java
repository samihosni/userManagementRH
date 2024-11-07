package com.example.userManagementRH.services;

import com.example.userManagementRH.entities.Evaluation;
import com.example.userManagementRH.repositories.EvaluationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepo evaluationRepo;

    public Evaluation createEvaluation(Evaluation evaluation) {
        return evaluationRepo.save(evaluation);
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepo.findAll();
    }

    public Evaluation updateEvaluation(Long id, Evaluation updatedEvaluation) {
        return evaluationRepo.findById(id).map(evaluation -> {
            evaluation.setScore(updatedEvaluation.getScore());
            evaluation.setFeedback(updatedEvaluation.getFeedback());
            return evaluationRepo.save(evaluation);
        }).orElseThrow(() -> new RuntimeException("Evaluation not found"));
    }

    public void deleteEvaluation(Long id) {
        evaluationRepo.deleteById(id);
    }
}
