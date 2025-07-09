<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freeuni.quizapp.model.User" %>
<%
    // Clear quiz session data when user navigates to profile
    session.removeAttribute("currentQuiz");
    session.removeAttribute("quizAnswers");
    session.removeAttribute("quizStartTime");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <style>
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            padding: 2rem;
            background: #f5f5f5;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: #ffffff;
            border-radius: 10px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,.05);
        }
        h1 {
            margin-bottom: 1.5rem;
        }
        .label {
            font-weight: 600;
            margin-top: 1rem;
        }
    </style>
</head>
<body>
<%
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<div class="container">
    <h1>Profile</h1>
    <div>
        <span class="label">Username:</span> <%= user.getUsername() %>
    </div>
    <div>
        <span class="label">User ID:</span> <%= user.getId() %>
    </div>
</div>
</body>
</html> 