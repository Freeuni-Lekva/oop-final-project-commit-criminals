package com.freeuni.quizapp.controller;

import com.freeuni.quizapp.model.Announcement;
import com.freeuni.quizapp.service.impl.AnnouncementServiceImpl;
import com.freeuni.quizapp.service.interfaces.AnnouncementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/announcement")
public class ViewAnnouncementServlet extends HttpServlet {

    private AnnouncementService announcementService;

    @Override
    public void init() throws ServletException {
        announcementService = new AnnouncementServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing announcement ID");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Announcement announcement = announcementService.getAnnouncementById(id);
            if (announcement == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Announcement not found");
                return;
            }

            request.setAttribute("announcement", announcement);
            request.getRequestDispatcher("/announcement.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid announcement ID format");
        } catch (SQLException e) {
            throw new ServletException("Database error retrieving announcement", e);
        }
    }
}
