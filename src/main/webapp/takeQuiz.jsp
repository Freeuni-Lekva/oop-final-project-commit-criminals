<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freeuni.quizapp.model.Quiz" %>
<%@ page import="com.freeuni.quizapp.model.Question" %>
<%@ page import="com.freeuni.quizapp.model.Answer" %>
<%@ page import="com.freeuni.quizapp.model.User" %>
<%@ page import="com.freeuni.quizapp.enums.QuestionType" %>
<%@ page import="java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Quiz currentQuiz = (Quiz) session.getAttribute("currentQuiz");
    if (currentQuiz == null) {
        response.sendRedirect("quizzes.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Take Quiz - <%= currentQuiz.getTitle() %></title>
    <style>
        :root {
            --gradient-accent: linear-gradient(135deg, #E85A4F 0%, #E9704F 100%);
            --text-secondary: #8E8D8A;
            --card-shadow: 0 16px 32px rgba(0, 0, 0, 0.06);
        }
        
        * { box-sizing: border-box; margin: 0; padding: 0; }
        
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            color: var(--text-secondary);
            background: #fafafa;
            line-height: 1.6;
        }
        
        .quiz-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 1.5rem;
        }
        
        .quiz-header {
            background: #ffffff;
            padding: 2rem;
            border-radius: 14px;
            box-shadow: var(--card-shadow);
            margin-bottom: 2rem;
            text-align: center;
        }
        
        .quiz-title {
            font-size: 2rem;
            font-weight: 700;
            background: var(--gradient-accent);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 0.5rem;
        }
        
        .quiz-description {
            font-size: 1rem;
            opacity: 0.8;
        }
        
        .question-card {
            background: #ffffff;
            padding: 2rem;
            border-radius: 14px;
            box-shadow: var(--card-shadow);
            margin-bottom: 1.5rem;
        }
        
        .question-number {
            font-size: 0.9rem;
            color: #E85A4F;
            font-weight: 600;
            margin-bottom: 0.5rem;
        }
        
        .question-text {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 1.5rem;
            color: #333;
        }
        
        .answer-option {
            margin-bottom: 1rem;
        }
        
        .answer-option input[type="radio"] {
            margin-right: 0.8rem;
            transform: scale(1.2);
        }
        
        .answer-option label {
            font-size: 1rem;
            cursor: pointer;
            padding: 0.5rem;
            border-radius: 8px;
            transition: background-color 0.2s ease;
        }
        
        .answer-option label:hover {
            background-color: #f0f0f0;
        }
        
        .text-input {
            width: 100%;
            padding: 0.8rem;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
            margin-top: 0.5rem;
        }
        
        .text-input:focus {
            outline: none;
            border-color: #E85A4F;
        }
        
        .submit-container {
            text-align: center;
            margin-top: 2rem;
        }
        
        .btn-submit {
            padding: 1rem 2rem;
            border: none;
            border-radius: 50px;
            background: var(--gradient-accent);
            color: #ffffff;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            box-shadow: 0 4px 12px rgba(232, 90, 79, 0.25);
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(232, 90, 79, 0.35);
        }
        
        .progress-bar {
            width: 100%;
            height: 8px;
            background: #e0e0e0;
            border-radius: 4px;
            margin-bottom: 1rem;
            overflow: hidden;
        }
        
        .progress-fill {
            height: 100%;
            background: var(--gradient-accent);
            transition: width 0.3s ease;
        }
        
        .navbar {
            background: #ffffff;
            padding: 1rem 2rem;
            box-shadow: 0 2px 8px rgba(0,0,0,.05);
            margin-bottom: 2rem;
        }
        
        .brand {
            font-size: 1.6rem;
            font-weight: 700;
            background: var(--gradient-accent);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <a href="index.jsp" class="brand">QuizMaster</a>
    </nav>
    
    <div class="quiz-container">
        <div class="quiz-header">
            <h1 class="quiz-title"><%= currentQuiz.getTitle() %></h1>
            <% if (currentQuiz.getDescription() != null) { %>
                <p class="quiz-description"><%= currentQuiz.getDescription() %></p>
            <% } %>
        </div>
        
        <% 
            List<Question> questions = currentQuiz.getQuestions();
            int totalQuestions = questions != null ? questions.size() : 0;
        %>
        
        <div class="progress-bar">
            <div class="progress-fill" style="width: 0%"></div>
        </div>
        
        <form action="submitQuiz" method="post" id="quizForm">
            <% if (questions != null) {
                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
            %>
                <div class="question-card">
                    <div class="question-number">Question <%= (i + 1) %> of <%= totalQuestions %></div>
                    <div class="question-text"><%= question.getText() %></div>
                    
                    <% if (question.getType() == QuestionType.multiple_choice && question.getAnswers() != null) { %>
                        <% for (Answer answer : question.getAnswers()) { %>
                            <div class="answer-option">
                                <input type="radio" 
                                       name="question_<%= question.getId() %>" 
                                       value="<%= answer.getAnswerText() %>" 
                                       id="answer_<%= question.getId() %>_<%= answer.getId() %>">
                                <label for="answer_<%= question.getId() %>_<%= answer.getId() %>">
                                    <%= answer.getAnswerText() %>
                                </label>
                            </div>
                        <% } %>
                    <% } else { %>
                        <input type="text" 
                               name="question_<%= question.getId() %>" 
                               class="text-input" 
                               placeholder="Enter your answer here...">
                    <% } %>
                </div>
            <% }
            } %>
            
            <div class="submit-container">
                <button type="submit" class="btn-submit">Submit Quiz</button>
            </div>
        </form>
    </div>
    
    <script>
        // Update progress bar based on answered questions
        function updateProgress() {
            const form = document.getElementById('quizForm');
            const questions = form.querySelectorAll('.question-card');
            let answered = 0;
            
            questions.forEach(question => {
                const inputs = question.querySelectorAll('input[type="radio"]:checked, input[type="text"]');
                let hasAnswer = false;
                
                inputs.forEach(input => {
                    if ((input.type === 'radio' && input.checked) || 
                        (input.type === 'text' && input.value.trim() !== '')) {
                        hasAnswer = true;
                    }
                });
                
                if (hasAnswer) answered++;
            });
            
            const percentage = (answered / questions.length) * 100;
            document.querySelector('.progress-fill').style.width = percentage + '%';
        }
        
        // Add event listeners to update progress
        document.addEventListener('DOMContentLoaded', function() {
            const inputs = document.querySelectorAll('input[type="radio"], input[type="text"]');
            inputs.forEach(input => {
                input.addEventListener('change', updateProgress);
                input.addEventListener('input', updateProgress);
            });
            
            // Confirm before leaving page
            window.addEventListener('beforeunload', function(e) {
                e.preventDefault();
                e.returnValue = 'Are you sure you want to leave? Your progress will be lost.';
            });
            
            // Remove confirmation when submitting
            document.getElementById('quizForm').addEventListener('submit', function() {
                window.removeEventListener('beforeunload', function() {});
            });
        });
    </script>
</body>
</html> 