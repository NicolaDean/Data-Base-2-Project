package it.polimi.db2.controllers;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.entitys.RateCost;
import it.polimi.db2.entitys.Service;
import it.polimi.db2.entitys.ServiceTypes.FixedInternetService;
import it.polimi.db2.entitys.ServiceTypes.MobileInternetServices;
import it.polimi.db2.entitys.ServiceTypes.MobilePhoneServices;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.exception.MissingFormData;
import it.polimi.db2.exception.NotValidRates;
import it.polimi.db2.services.OptionalProductService;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.utils.TemplatePathManager;
import org.thymeleaf.TemplateEngine;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

        if(optionalData != null)
        {
            //Delete duplicates from products
            Arrays.stream(optionalData).distinct().collect(Collectors.toList()).toArray(optionalData);
        }

        //Rates Data
        String[] rate_validity = request.getParameterValues("validity");
        String[] rate_price    = request.getParameterValues("price");

        //Mobile Phone Services Data

        String[] MPS_minutes    = request.getParameterValues("Min");
        String[] MPS_sms        = request.getParameterValues("Sms");
        String[] MPS_extraMin   = request.getParameterValues("ExtraMin");
        String[] MPS_extraSms   = request.getParameterValues("ExtraSms");

        // Internet Serivces Data

        String[] MIS_type = request.getParameterValues("internet-type");
        String[] MIS_Gb  = request.getParameterValues("Gb");
        String[] MIS_extraGb   = request.getParameterValues("extraGb");

        //Fixed Phone Services Data

        String[] FPS     = request.getParameterValues("FPS");


        //NOW CONVERT STRINGS ARRAYS INTO ACTUAL OBJ LISTS

        //Optional Products
        List<OptionalProduct>optionals = null;
        try {
            optionals = optionalProductService.convertOptionalProducts(optionalData);
        } catch (ElementNotFound e) {
            e.printStackTrace();
            this.setError(request,response,"Invalid Optional Prod", TemplatePathManager.creation);
        }


        try {
            //Rate Costs
            List<RateCost> rateCosts = this.createRatesFromForms(rate_validity,rate_price);

            //Services
            List<Service> services = new ArrayList<>();
            services = this.createMobilePhoneServicesFromForm(services,MPS_minutes,MPS_sms,MPS_extraMin,MPS_extraSms);
            services = this.createMobileInternetServicesFromForm(services,MIS_Gb,MIS_extraGb,MIS_type);
            services = this.createFixedPhoneServiceFromForm(services,FPS);

            //Persist
            packageService.persistPackage(name,optionals,rateCosts,services);
            response.sendRedirect("admin");
        }catch (MissingFormData e)
        {
            response.sendRedirect("go-creation?error=\"Missing some Fields, check if all data inserted\"");
        }catch ( NotValidRates e)
        {
            response.sendRedirect("go-creation?error=\"Rate with higher validity should have lower price, check them please\"");
        }

        System.out.println("");
        //Services
    }

    private List<Service> createFixedPhoneServiceFromForm(List<Service> services, String[] FPS) {

        if(FPS==null) return services;

        for(int i=0;i<FPS.length;i++)
        {
            services.add(new Service());
        }

        return services;
    }


    /**
     * Convert the forms data of rates into actual rates
     * check also data validity (if not valid exception)
     * @param rate_validity
     * @param rate_price
     * @return list of rates from form
     */
    public List<RateCost> createRatesFromForms(String[] rate_validity, String[] rate_price) throws MissingFormData, NotValidRates {
        boolean flag = false;

        //TODO check if the rates are valid (eg higher validity-> lower price)
        if(rate_price == null) flag = true;
        if(rate_validity == null) flag = true;

        if(flag) throw new MissingFormData("Rate cost Form Error");

        if(rate_price.length != rate_validity.length) flag = true;

        if(flag) throw new MissingFormData("Rate cost Form Error");



        List<RateCost> rates = new ArrayList<>();
        for(int i=0;i<rate_price.length;i++)
        {
            RateCost tmp = new RateCost();

            try {
                tmp.setCost(Integer.parseInt(rate_price[i]));
                tmp.setMonthValidity(Integer.parseInt(rate_validity[i]));
                rates.add(tmp);
            }catch (Exception e)
            {
                throw new MissingFormData("Rate cost Form Error");
            }

        }


        for(int i=0;i<rates.size();i++)
        {
            for(int j=0;j<rates.size();j++)
            {
                if(i!=j)
                {
                    if(rates.get(i).getMonthValidity() <= rates.get(j).getMonthValidity())
                    {
                        if(rates.get(i).getPrice() <= rates.get(j).getPrice()) {
                            throw new NotValidRates();
                        }
                    }
                }

            }
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
    public List<Service> createMobilePhoneServicesFromForm(List<Service> services,String[] MPS_minutes,String[] MPS_sms,String[] MPS_extraMin,String[] MPS_extraSms) throws MissingFormData {

        boolean flag = false;

        if(MPS_extraMin == null) return services;
        if(MPS_extraSms == null) return services;
        if(MPS_minutes == null)  return services;
        if(MPS_sms == null)      return services;


        int size = MPS_extraMin.length;

        if(MPS_minutes.length != size)  flag = true;
        if(MPS_sms.length != size)      flag = true;
        if(MPS_extraSms.length != size) flag = true;


        if(flag) throw new MissingFormData("Mobile phone services form error");
        for(int i=0;i<MPS_minutes.length;i++)
        {
            MobilePhoneServices tmp = new MobilePhoneServices();

            try {
                tmp.setMinutes(Integer.parseInt(MPS_minutes[i]));
                tmp.setExtraMinutesFee(Float.parseFloat(MPS_extraMin[i]));
                tmp.setSms(Integer.parseInt(MPS_sms[i]));
                tmp.setExtraSMSFee(Float.parseFloat(MPS_extraSms[i]));

                services.add(tmp);
            } catch (Exception e)
            {
                throw new MissingFormData("Mobile phone services form error");
            }

        }

        return services;
    }


    private List<Service> createMobileInternetServicesFromForm(List<Service> services, String[] mis_gb, String[] mis_extraGb,String[] type) throws MissingFormData {

        boolean flag = false;

        if(mis_extraGb == null) return services;
        if(mis_gb      == null) return services;

        if(mis_extraGb.length != mis_gb.length) flag = true;

        if(flag) throw new MissingFormData("Internet Form error");

        for(int i=0;i<mis_gb.length;i++)
        {

            try {
                if(type[i].equals("FIS"))
                {
                    FixedInternetService tmp = new FixedInternetService();
                    tmp.setGigabyte(Integer.parseInt(mis_gb[i]));
                    tmp.setExtraFee(Float.parseFloat(mis_extraGb[i]));
                    services.add(tmp);
                }
                else
                {
                    MobileInternetServices tmp = new MobileInternetServices();
                    tmp.setGigabyte(Integer.parseInt(mis_gb[i]));
                    tmp.setExtraFee(Float.parseFloat(mis_extraGb[i]));
                    services.add(tmp);
                }
            }catch (Exception e)
            {
                throw new MissingFormData("Internet Form error");
            }

        }

        return services;
    }

}
