package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.dao.impl.UserDaoImpl;
import com.freeuni.quizapp.model.User;
import com.freeuni.quizapp.util.DBConnector;
import com.freeuni.quizapp.util.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            request.setAttribute("errorMessage", "Username and password are required");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnector.getConnection()) {
            UserDaoImpl userDao = new UserDaoImpl(con);
            User user = userDao.getByUsername(username, true);

            if (user == null) {
                request.setAttribute("errorMessage", "Invalid Username");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String hashedInput;
            try {
                hashedInput = PasswordHasher.hashPassword(password);
            } catch (NoSuchAlgorithmException e) {
                throw new ServletException("Failed to hash password", e);
            }

            if (!hashedInput.equals(user.getHashedPassword())) {
                request.setAttribute("errorMessage", "Invalid password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }
} 