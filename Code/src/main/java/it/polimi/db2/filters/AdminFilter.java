package it.polimi.db2.filters;

import it.polimi.db2.entitys.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request  = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String indexPath = request.getServletContext().getContextPath() + "/index.html";


        HttpSession s = request.getSession();
        User u = (User) s.getAttribute("user");
        if (s.isNew() ||  u == null || !u.isAdmin()) {
            response.sendRedirect(indexPath);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
