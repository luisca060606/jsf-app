package com.ejemplo.filter;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.ejemplo.bean.AuthBean;

@WebFilter(urlPatterns = { "/*" })
public class AuthFilter implements Filter {

    @Inject
    private AuthBean authBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String loginURL = contextPath + "/login.xhtml";
        String dashboardURL = contextPath + "/views/dashboard.xhtml";

        boolean loggedIn = (authBean != null && authBean.getUserLogged() != null);
        boolean isLoginPage = req.getRequestURI().equals(loginURL);
        boolean isResourceRequest = req.getRequestURI().startsWith(contextPath + "/jakarta.faces.resource/");

        if (!isResourceRequest) {
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);
        }

        if (loggedIn) {
            if (isLoginPage) {
                res.sendRedirect(dashboardURL);
                return;
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (isLoginPage || isResourceRequest) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(loginURL);
                return;
            }
        }
    }

    @Override public void init(FilterConfig fConfig) {}
    @Override public void destroy() {}
}
