package it.polimi.db2.controllers;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.services.OptionalProductService;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@WebServlet(name = "go-creation", value = "/go-creation")
public class GoCreationPage extends BasicServerlet{


    @EJB(name="it.polimi.db2.services/OptionalProductService")
    private OptionalProductService optionalProductService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String error = request.getParameter("error");

       List<OptionalProduct> products = optionalProductService.getOptionalProducts();

       request.setAttribute("errorMsg",error);
       request.setAttribute("optionals",products);
       this.templateRenderer(request,response, TemplatePathManager.creation);
    }
}
