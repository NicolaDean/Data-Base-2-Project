package it.polimi.db2.controllers;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.services.OptionalProductService;
import it.polimi.db2.services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "create-package", value = "/create-package")
public class CreatePackage extends BasicServerlet {

    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @EJB(name="it.polimi.db2.services/OptionalProductService")
    private OptionalProductService optionalProductService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String[] optionalData = request.getParameterValues("optionals");

        List<OptionalProduct>optionals = null;
        try {
            optionals = optionalProductService.convertOptionalProducts(optionalData);
        } catch (ElementNotFound e) {
            e.printStackTrace();
            //TODO add error
        }




    }
}
