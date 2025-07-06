<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QuizMaster – Challenge Your Mind</title>
    <style>
        :root {
            --gradient-accent: linear-gradient(135deg, #E85A4F 0%, #E9704F 100%);
            --beige-gradient: linear-gradient(135deg, #EAE7DC 0%, #D8C3A5 100%);
            --text-secondary: #8E8D8A;
            --card-shadow: 0 18px 40px rgba(0, 0, 0, 0.08);
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            color: var(--text-secondary);
            background-color: #ffffff;
            line-height: 1.6;
        }

        .navbar {
            position: sticky;
            top: 0;
            width: 100%;
            background: #ffffff;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem 4.5%;
            z-index: 100;
            box-shadow: 0 2px 8px rgba(0,0,0,.05);
            animation: slideDown 0.8s ease-out both;

        }

        .brand {
            font-size: 1.6rem;
            font-weight: 700;
            background: var(--gradient-accent);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .nav-links {
            display: flex;
            gap: 2rem;
            list-style: none;
        }

        .nav-links a {
            text-decoration: none;
            color: var(--text-secondary);
            font-weight: 500;
            transition: color .2s ease;
        }

        .nav-links a:hover {
            color: #E85A4F;
        }

        .hero {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            padding: 4.5% 4.5% 7%;
            background: var(--beige-gradient);
        }

        .hero-content {
            flex: 1 1 420px;
        }

        .hero-title {
            font-size: 3.6rem;
            font-weight: 800;
            line-height: 1.15;
            background: var(--gradient-accent);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: fadeInUp 1s ease-out 0.3s both;
        }

        .hero-title span {
            color: #8E8D8A;
        }

        .hero-description {
            margin-top: 1.2rem;
            max-width: 480px;
            font-size: 1.05rem;
            animation: fadeIn 1s ease-out 0.6s both;
        }

        .hero-btn-group {
            margin-top: 2.2rem;
            display: flex;
            gap: 1.2rem;
        }

        .btn-primary,
        .btn-outline {
            padding: 0.95rem 1.85rem;
            border-radius: 50px;
            font-size: 1.05rem;
            font-weight: 600;
            cursor: pointer;
            border: none;
            transition: all .25s ease;
        }

        .btn-primary {
            background-image: var(--gradient-accent);
            color: #ffffff;
            box-shadow: 0 4px 12px rgba(232, 90, 79, 0.25);
        }

        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(232, 90, 79, 0.35);
        }

        .btn-outline {
            background: transparent;
            color: #E85A4F;
            border: 2px solid #E85A4F;
        }

        .btn-outline:hover {
            background-image: var(--gradient-accent);
            color: #ffffff;
            border-color: transparent;
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(232, 90, 79, 0.35);
        }

        .stats {
            display: flex;
            gap: 2.5rem;
            margin-top: 3.5rem;
            flex-wrap: wrap;
        }

        .stat {
            text-align: center;
        }

        .stat .number {
            font-size: 2.1rem;
            font-weight: 700;
            background: var(--gradient-accent);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .stat .label {
            font-size: 0.8rem;
            letter-spacing: 1.2px;
            text-transform: uppercase;
        }

        .hero-card-wrapper {
            flex: 1 1 360px;
            perspective: 1000px;
            display: flex;
            justify-content: center;
            margin-top: 2rem;
        }

        .quiz-card {
            background: #ffffff;
            border-radius: 14px;
            width: 340px;
            padding: 2rem 1.75rem 2.4rem;
            box-shadow: var(--card-shadow);
            transform: rotate(6deg);
            animation: rotateIn 1s ease-out 1s both;
        }

        .quiz-label {
            display: inline-block;
            padding: 0.35rem 0.85rem;
            font-size: 0.85rem;
            font-weight: 600;
            border-radius: 8px;
            background-image: var(--gradient-accent);
            color: #ffffff;
            margin-bottom: 1.1rem;
        }

        .quiz-question {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 1.4rem;
            color: var(--text-secondary);
        }

        .answer {
            background: #f7f7f7;
            border-radius: 8px;
            padding: 0.65rem 0.85rem;
            margin-bottom: 0.75rem;
            border: 1px solid #e5e5e5;
            font-size: 0.95rem;
            color: var(--text-secondary);
        }

        .answer.correct {
            background-image: var(--gradient-accent);
            color: #ffffff;
            border: none;
        }

        .categories-section {
            padding: 6% 4.5% 7%;
        }

        .categories-title {
            text-align: center;
            font-size: 2.4rem;
            font-weight: 700;
            margin-bottom: 3rem;
            background: var(--gradient-accent);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .categories-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 2rem;
        }

        .category-card {
            background: #ffffff;
            border-radius: 14px;
            padding: 2rem 1.6rem;
            box-shadow: var(--card-shadow);
            text-align: center;
            transition: transform .25s ease, box-shadow .25s ease;
        }

        .category-card:hover {
            transform: translateY(-6px);
            box-shadow: 0 12px 28px rgba(0,0,0,.1);
        }

        .category-icon {
            font-size: 2.4rem;
            margin-bottom: 1rem;
            color: #E85A4F;
        }

        .category-name {
            font-size: 1.1rem;
            font-weight: 600;
        }

        footer {
            margin-top: 8%;
            padding: 2.5rem 4.5%;
            text-align: center;
            background: #f7f7f7;
            font-size: 0.9rem;
        }

        @media (max-width: 900px) {
            .hero {
                flex-direction: column;
                text-align: center;
            }
            .hero-card-wrapper {
                margin-top: 3.5rem;
            }
        }

        @keyframes slideDown {
            from { transform: translateY(-100%); opacity: 0; }
            to   { transform: translateY(0); opacity: 1; }
        }

        @keyframes fadeInUp {
            from { transform: translateY(20px); opacity: 0; }
            to   { transform: translateY(0); opacity: 1; }
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to   { opacity: 1; }
        }

        @keyframes rotateIn {
            from { transform: rotate(-12deg) scale(0.8); opacity: 0; }
            to   { transform: rotate(6deg) scale(1); opacity: 1; }
        }
    </style>
</head>
<body>

<nav class="navbar">
    <div class="brand">QuizMaster</div>
    <ul class="nav-links">
        <li><a href="#">Home</a></li>
        <li><a href="#">Quizzes</a></li>
        <li><a href="#">Leaderboard</a></li>
        <li><a href="login.jsp">Login</a></li>
    </ul>
</nav>

<section class="hero">
    <div class="hero-content">
        <h1 class="hero-title">Challenge <br/>Your <span>Mind</span></h1>
        <p class="hero-description">Discover thousands of engaging quizzes across multiple categories. Test your knowledge, learn something new, and compete with friends!</p>
        <div class="hero-btn-group">
            <button class="btn-primary">Start Random Quiz Now</button>
        </div>

        <div class="stats">
            <div class="stat">
                <div class="number">50+</div>
                <div class="label">Quizzes</div>
            </div>
        </div>
    </div>

    <div class="hero-card-wrapper">
        <div class="quiz-card">
            <span class="quiz-label">Geography Quiz</span>
            <p class="quiz-question">What is the capital of Japan?</p>
            <div class="answer">Seoul</div>
            <div class="answer correct">Tokyo</div>
            <div class="answer">Beijing</div>
            <div class="answer">Bangkok</div>
        </div>
    </div>
</section>

</body>
</html>
