package it.polimi.db2.controllers;

import it.polimi.db2.entitys.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BasicServerlet extends HttpServlet {

    protected TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        super.init();
        //Generate Context
        ServletContext context = getServletContext();

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    /**
     *
     * @return the thymeleaf initialized template engine
     */
    public TemplateEngine getTemplateEngine()
    {
        return this.templateEngine;
    }

    /**
     * Generate an error on a "standard" error template $errorMsg
     * @param request
     * @param response
     * @param error
     */
    public void setError(HttpServletRequest request, HttpServletResponse response,String error,String path)
    {
        request.setAttribute("errorMsg",error);
        try {
            templateRenderer(request,response,path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given the path of a template it generate it
     * @param request http request
     * @param response http response
     * @param path template path
     * @throws IOException template not exist
     */
    public void templateRenderer(HttpServletRequest request, HttpServletResponse response,String path) throws IOException {
        ServletContext context = getServletContext();
        genericTemplateRenderer(context,this.templateEngine,request,response,path);
    }

    public static void genericTemplateRenderer(ServletContext context,TemplateEngine engine,HttpServletRequest request, HttpServletResponse response,String path) throws IOException {
        WebContext ctx = new WebContext(request, response, context, request.getLocale());
        engine.process(path,ctx,response.getWriter());
    }


    protected void errorRedirect(HttpServletRequest request, HttpServletResponse response, String page, String error) throws IOException {

        request.setAttribute("errorMsg",error);
        response.sendRedirect(page);
    }
}
