package it.polimi.db2.controllers;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.entitys.RateCost;
import it.polimi.db2.entitys.Service;
import it.polimi.db2.entitys.ServiceTypes.MobileInternetServices;
import it.polimi.db2.entitys.ServiceTypes.MobilePhoneServices;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.services.OptionalProductService;
import it.polimi.db2.services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "create-package", value = "/create-package")
public class CreatePackage extends BasicServerlet {

    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @EJB(name="it.polimi.db2.services/OptionalProductService")
    private OptionalProductService optionalProductService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //RETRIVE DATA FROM FORMS AND PUT THEM IN STRINGS ARRAY

        String name           = request.getParameter("name");
        //Optional Product data
        String[] optionalData = request.getParameterValues("optionals");

        //Rates Data
        String[] rate_validity = request.getParameterValues("validity");
        String[] rate_price    = request.getParameterValues("price");

        //Mobile Phone Services Data

        String[] MPS_minutes    = request.getParameterValues("Min");
        String[] MPS_sms        = request.getParameterValues("Sms");
        String[] MPS_extraMin   = request.getParameterValues("ExtraMin");
        String[] MPS_extraSms   = request.getParameterValues("ExtraSms");

        //Mobile Internet Serivces Data

        String[] MIS_Gb  = request.getParameterValues("Gb");
        String[] MIS_extraGb   = request.getParameterValues("extraGb");

        //Fixed Phone Services Data

        //Fixed Internet Service Data


        //NOW CONVERT STRINGS ARRAYS INTO ACTUAL OBJ LISTS

        //Optional Products
        List<OptionalProduct>optionals = null;
        try {
            optionals = optionalProductService.convertOptionalProducts(optionalData);
        } catch (ElementNotFound e) {
            e.printStackTrace();
            //TODO add error
        }

        //Rate Costs
        List<RateCost> rateCosts = this.createRatesFromForms(rate_validity,rate_price);


        List<Service> services = new ArrayList<>();

        services = this.createMobilePhoneServicesFromForm(services,MPS_minutes,MPS_sms,MPS_extraMin,MPS_extraSms);
        services = this.createMobileInternetServicesFromForm(services,MIS_Gb,MIS_extraGb);



        packageService.persistPackage("Test",optionals,rateCosts,services);
        System.out.println("");
        //Services
    }



    /**
     * Convert the forms data of rates into actual rates
     * check also data validity (if not valid exception)
     * @param rate_validity
     * @param rate_price
     * @return list of rates from form
     */
    public List<RateCost> createRatesFromForms(String[] rate_validity, String[] rate_price)
    {
        if(rate_price.length != rate_validity.length) return null;


        List<RateCost> rates = new ArrayList<>();
        for(int i=0;i<rate_price.length;i++)
        {
            RateCost tmp = new RateCost();
            tmp.setCost(Integer.parseInt(rate_price[i]));
            tmp.setMonthValidity(Integer.parseInt(rate_validity[i]));
            rates.add(tmp);
        }

        return rates;
    }

    /**
     * create a list of mobile phone services from form datas
     * check if missing data
     * @param services
     * @param MPS_minutes
     * @param MPS_sms
     * @param MPS_extraMin
     * @param MPS_extraSms
     * @return
     */
    public List<Service> createMobilePhoneServicesFromForm(List<Service> services,String[] MPS_minutes,String[] MPS_sms,String[] MPS_extraMin,String[] MPS_extraSms)
    {

        if((MPS_extraMin.length!=MPS_extraSms.length) ||
                (MPS_extraMin.length!=MPS_minutes.length) ||
                (MPS_minutes.length!=MPS_sms.length) ||
                (MPS_extraMin.length!=MPS_sms.length)) return null;

        for(int i=0;i<MPS_minutes.length;i++)
        {
            MobilePhoneServices tmp = new MobilePhoneServices();

            tmp.setMinutes(Integer.parseInt(MPS_minutes[i]));
            tmp.setExtraMinutesFee(Float.parseFloat(MPS_extraMin[i]));
            tmp.setSms(Integer.parseInt(MPS_sms[i]));
            tmp.setExtraSMSFee(Float.parseFloat(MPS_extraSms[i]));

            services.add(tmp);
        }

        return services;
    }

    private List<Service> createMobileInternetServicesFromForm(List<Service> services, String[] mis_gb, String[] mis_extraGb) {

        for(int i=0;i<mis_gb.length;i++)
        {
            MobileInternetServices tmp = new MobileInternetServices();

            tmp.setGigabyte(Integer.parseInt(mis_gb[i]));
            tmp.setExtraFee(Float.parseFloat(mis_extraGb[i]));

            services.add(tmp);
        }

        return services;
    }

}
