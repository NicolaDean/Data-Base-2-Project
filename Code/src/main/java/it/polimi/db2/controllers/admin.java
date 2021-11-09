package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Package;
import it.polimi.db2.entitys.User;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.services.UserServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "admin", value = "/admin")

public class admin extends BasicServerlet{
    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;
    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Package> packages = this.packageService.getAllPackages();

        request.setAttribute("packages",packages);

        this.templateRenderer(request,response, TemplatePathManager.admin);
    }
}
