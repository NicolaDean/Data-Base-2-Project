package it.polimi.db2.controllers;


import it.polimi.db2.entitys.Package;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.swing.text.html.CSS.getAttribute;

@WebServlet(name = "contract", value = "/contract")
public class Contract extends BasicServerlet{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rate;
        rate = StringEscapeUtils.escapeJava(request.getParameter("rates"));
        String id ;
        id= StringEscapeUtils.escapeJava(request.getParameter("packId"));

       // rate = (int) request.StringEscapeUtils.escapeJava(getAttribute("rates"));
        System.out.println("puchasing");

    }
}
