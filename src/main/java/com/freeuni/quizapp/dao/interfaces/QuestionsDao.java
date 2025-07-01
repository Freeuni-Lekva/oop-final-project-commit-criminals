package com.freeuni.quizapp.dao.interfaces;

import com.freeuni.quizapp.model.Question;

import java.util.List;

public interface QuestionsDao {

    Question getQuestionById(int id);

    void addQuestion(Question q);

    void removeQuestion(int q_id);

    List<Question> getAllQuestions();

    List<Question> getQuizAllQuestions(int quiz_id);

    void updateQuestion(int update_q_id, Question q);
}
