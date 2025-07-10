package com.freeuni.quizapp.service.interfaces;

import java.sql.SQLException;

public interface QuizResultsService {

    void storePopularQuizzes() throws SQLException;

}
