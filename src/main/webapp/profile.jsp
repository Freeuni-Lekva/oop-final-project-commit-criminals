<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.freeuni.quizapp.model.User" %>
<%@ page import="java.util.List" %>
<%
    // Clear quiz session data when user navigates to profile
    session.removeAttribute("currentQuiz");
    session.removeAttribute("quizAnswers");
    session.removeAttribute("quizStartTime");
    User currentUser = (User) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #EAE7DC 0%, #D8C3A5 100%);
            --accent-gradient: linear-gradient(135deg, #E85A4F 0%, #E9704F 100%);
            --card-bg: rgba(255, 255, 255, 0.9);
            --card-border: rgba(0, 0, 0, 0.05);
            --text-primary: #8E8D8A;
        }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            color: var(--text-primary);
            background: var(--primary-gradient);
            min-height: 100vh;
        }
        .navbar {
            position: sticky;
            top: 0;
            background: #fff;
            display: flex;
            align-items: center;
            padding: 1rem 4.5%;
            box-shadow: 0 2px 8px rgba(0,0,0,.05);
            z-index: 100;
        }
        .brand {
            font-size: 1.6rem;
            font-weight: 700;
            background: var(--accent-gradient);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-decoration: none;
        }
        .nav-links {
            list-style: none;
            display: flex;
            gap: 2rem;
            margin-left: auto;
        }
        .nav-links a {
            text-decoration: none;
            color: var(--text-primary);
            font-weight: 500;
        }
        .nav-links a:hover {
            color: #E85A4F;
        }
        .profile {
            position: relative;
        }
        .profile .dropdown {
            display: none;
            position: absolute;
            left: 50%;
            top: 100%;
            transform: translateX(-50%);
            background: #fff;
            list-style: none;
            margin: 0;
            padding: 0.4rem 0;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,.08);
            min-width: 160px;
        }
        .profile:hover .dropdown {
            display: block;
        }
        .profile .dropdown li a {
            display: block;
            padding: 0.6rem 1rem;
            color: var(--text-primary);
            white-space: nowrap;
        }
        .profile .dropdown li a:hover {
            background: #f7f7f7;
            color: #E85A4F;
        }
        .profile a {
            display: flex;
            align-items: center;
            gap: 0.35rem;
        }
        .search-bar {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            width: 42%;
            min-width: 260px;
        }
        .search-bar input[type="text"] {
            padding: 0.45rem 1rem 0.45rem 2rem;
            border: 1px solid #e0e0e0;
            border-radius: 50px;
            font-size: 0.9rem;
            width: 60%;
            max-width: 420px;
        }
        .profile-container {
            max-width: 800px;
            margin: 4.5% auto 0 auto;
            padding: 0 1.5rem;
        }
        .profile-card {
            background: var(--card-bg);
            border: 1px solid var(--card-border);
            border-radius: 18px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.13);
            padding: 2.5rem 2.25rem;
            text-align: center;
            margin-bottom: 2rem;
        }
        .profile-card h2 {
            margin-bottom: 0.5rem;
            font-weight: 700;
            font-size: 2rem;
            background: var(--accent-gradient);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .profile-card .username {
            font-size: 1.2rem;
            font-weight: 500;
            margin-bottom: 1.2rem;
            color: #E85A4F;
        }
        .profile-card .info {
            margin-bottom: 1.2rem;
            font-size: 1rem;
        }
        .profile-card .stats {
            display: flex;
            justify-content: space-around;
            margin: 1.5rem 0 1.2rem 0;
        }
        .profile-card .stat {
            text-align: center;
        }
        .profile-card .stat .number {
            font-size: 1.3rem;
            font-weight: 700;
            background: var(--accent-gradient);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .profile-card .stat .label {
            font-size: 0.85rem;
            color: var(--text-primary);
        }
        .profile-card .btn {
            margin-top: 1.2rem;
            padding: 0.7rem 1.5rem;
            border: none;
            border-radius: 50px;
            background: var(--accent-gradient);
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: transform 0.2s, box-shadow 0.2s;
            display: inline-block;
        }
        .profile-card .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(232, 90, 79, 0.2);
        }
        .history-section {
            background: var(--card-bg);
            border: 1px solid var(--card-border);
            border-radius: 18px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.13);
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .history-section h3 {
            font-size: 1.4rem;
            margin-bottom: 1rem;
            color: #E85A4F;
            text-align: center;
        }
        .history-list {
            list-style: none;
            padding: 0;
        }
        .history-item {
            padding: 1rem;
            margin-bottom: 0.8rem;
            background: rgba(255, 255, 255, 0.6);
            border-radius: 10px;
            border: 1px solid rgba(0, 0, 0, 0.05);
            font-size: 0.9rem;
            line-height: 1.4;
        }
        .no-history {
            text-align: center;
            color: var(--text-primary);
            font-style: italic;
            padding: 2rem;
        }
        @media (max-width: 768px) {
            .profile-container {
                margin: 2rem auto 0;
                padding: 0 1rem;
            }
            .profile-card {
                padding: 2rem 1.5rem;
            }
            .profile-card .stats {
                flex-direction: column;
                gap: 1rem;
            }
        }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="index.jsp" class="brand">QuizMaster</a>
    <form class="search-bar" action="search.jsp" method="get">
        <input type="text" name="q" placeholder="Search">
    </form>
    <ul class="nav-links">
        <li><a href="quizzes.jsp">Browse Quizzes</a></li>
        <li><a href="#">Leaderboard</a></li>
        <li class="profile">
            <a href="#"><%= currentUser.getUsername() %></a>
            <ul class="dropdown">
                <li><a href="profile">View Profile</a></li>
                <li><a href="logout">Sign Out</a></li>
            </ul>
        </li>
    </ul>
</nav>

<div class="profile-container">
    <div class="profile-card">
        <h2>Profile</h2>
        <div class="username"><%= currentUser.getUsername() %></div>
        <div class="info">
            <% if (currentUser.getBio() != null && !currentUser.getBio().trim().isEmpty()) { %>
                <%= currentUser.getBio() %><br>
            <% } %>
            Member since: <%= currentUser.getCreatedAt() != null ? currentUser.getCreatedAt().toString().substring(0, 10) : "Unknown" %>
        </div>
        <div class="stats">
            <div class="stat">
                <div class="number"><%= request.getAttribute("quizzesCreated") != null ? request.getAttribute("quizzesCreated") : "0" %></div>
                <div class="label">Created</div>
            </div>
            <div class="stat">
                <div class="number"><%= request.getAttribute("quizzesTaken") != null ? request.getAttribute("quizzesTaken") : "0" %></div>
                <div class="label">Taken</div>
            </div>
        </div>
        <a class="btn" href="quizzes.jsp">Browse Quizzes</a>
    </div>

    <% if (currentUser.isAdmin()) { %>
    <div class="history-section">
        <h3>My Created Quizzes</h3>
        <%
            @SuppressWarnings("unchecked")
            List<com.freeuni.quizapp.model.Quiz> createdQuizzes = (List<com.freeuni.quizapp.model.Quiz>) request.getAttribute("createdQuizzes");
            @SuppressWarnings("unchecked")
            java.util.Map<Integer, Integer> questionCounts = (java.util.Map<Integer, Integer>) request.getAttribute("questionCounts");

            if (createdQuizzes != null && !createdQuizzes.isEmpty()) {
        %>
            <ul class="history-list">
                <% for (com.freeuni.quizapp.model.Quiz quiz : createdQuizzes) { %>
                    <li class="history-item">
                        <strong><%= quiz.getTitle() %></strong><br>
                        <% if (quiz.getDescription() != null && !quiz.getDescription().trim().isEmpty()) { %>
                            Description: <%= quiz.getDescription() %><br>
                        <% } %>
                        Questions: <%= questionCounts != null && questionCounts.get(quiz.getId()) != null ? questionCounts.get(quiz.getId()) : "0" %><br>
                        Created: <%= quiz.getCreatedAt() != null ? quiz.getCreatedAt().toString().substring(0, 16) : "Unknown" %>
                    </li>
                <% } %>
            </ul>
        <% } else { %>
            <div class="no-history">
                You haven't created any quizzes yet.
                <a href="quizzes.jsp" style="color: #E85A4F;">Create your first quiz!</a>
            </div>
        <% } %>
    </div>
    <% } %>

    <div class="history-section">
        <h3>Recent Activity</h3>
        <%
            @SuppressWarnings("unchecked")
            List<String> history = (List<String>) request.getAttribute("history");
            if (history != null && !history.isEmpty()) {
        %>
            <ul class="history-list">
                <% for (String item : history) { %>
                    <li class="history-item"><%= item %></li>
                <% } %>
            </ul>
        <% } else { %>
            <div class="no-history">No recent activity to display.</div>
        <% } %>
    </div>
</div>
</body>
</html>