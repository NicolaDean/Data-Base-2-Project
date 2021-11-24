
package it.polimi.db2.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class NormalUserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest  request  = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String indexPath = request.getServletContext().getContextPath() + "/index.html";


        HttpSession s = request.getSession();
        if (s.isNew() || s.getAttribute("user") == null) {
            response.sendRedirect(indexPath);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
