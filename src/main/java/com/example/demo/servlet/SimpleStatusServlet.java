package com.example.demo.controller;

import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

public class SimpleStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(
            "SaaS User Role Permission Manager is running"
        );
    }
}
