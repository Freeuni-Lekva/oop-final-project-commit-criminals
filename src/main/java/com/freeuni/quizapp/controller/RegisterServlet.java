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
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || password == null || confirmPassword == null) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnector.getConnection()) {
            UserDaoImpl userDao = new UserDaoImpl(con);
            if (userDao.isUsernameOccupied(username)) {
                request.setAttribute("errorMessage", "Username already taken.");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
                return;
            }

            int nextId = 1;
            try (PreparedStatement ps = con.prepareStatement("SELECT MAX(user_id) FROM users");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nextId = rs.getInt(1) + 1;
                }
            }

            String hashedPassword;
            try {
                hashedPassword = PasswordHasher.hashPassword(password);
            } catch (NoSuchAlgorithmException e) {
                throw new ServletException("Failed to hash password", e);
            }

            User newUser = new User(nextId, username, hashedPassword, false, Timestamp.from(Instant.now()), "", "");
            userDao.createUser(newUser);

            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } catch (SQLException e) {
            throw new ServletException("Database error during registration", e);
        }
    }
} 