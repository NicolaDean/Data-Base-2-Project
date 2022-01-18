package it.polimi.db2.controllers;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.services.OptionalProductService;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.utils.TemplatePathManager;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "create-product", value = "/create-product")
public class CreateProduct extends BasicServerlet{


    @EJB(name="it.polimi.db2.services/OptionalProductService")
    private OptionalProductService optionalProductService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        String name = null;
        int    fee  = 0;
         //A NEW OPTIONAL PRODUCT CAN BE ASSIGNED ONLY TO NEW PACKAGES
        try {
            name = StringEscapeUtils.escapeJava(request.getParameter("name"));
            fee = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("monthFee")));

            if(name == null || fee == 0){
                this.setError(request,response,"Fill all fields",TemplatePathManager.creation);
            }
        }catch (Exception e)
        {
            this.setError(request,response,"Fill all fields",TemplatePathManager.creation);
        }

        this.optionalProductService.createProduct(name,fee);

        this.templateRenderer(request,response, TemplatePathManager.admin);
    }
}
