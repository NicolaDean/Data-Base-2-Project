package it.polimi.db2.services;

import it.polimi.db2.entitys.*;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.exception.NoPackageFound;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//TODO VERIFICARE CHE NON POSSA ESSERE STATEFULL
@Stateless
public class ContractServices extends BasicService{

    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;

    @EJB(name="it.polimi.db2.services/OptionalProductService")
    private OptionalProductService optionalProductService;


    public RateCost getRateById(int id) throws ElementNotFound {
        RateCost rate = this.em.find(RateCost.class,id);

        if(rate == null) throw new ElementNotFound("Rate not Founded");
        return rate;
    }



    public Order createContract(int packageId, int rateId, String[] optionalProducts, Date startDate) throws ElementNotFound {
        Order order = new Order();

        order.setStartingDate(startDate);
        //FIND SELECTED PACKAGE
        try {
            Package p = this.packageService.getPackageById(packageId);
            order.setPackage(p);
        } catch (NoPackageFound e) {
            throw new ElementNotFound("No Package Found");
        }
        //FIND SELECTED RATE
        try {
            RateCost rate =this.getRateById(rateId);
            order.setRate(rate);
        } catch (ElementNotFound e) {
            throw new ElementNotFound("No Rate Found");
        }
        //FIND SELECTED PRODUCTS
        List<OptionalProduct> products = optionalProductService.convertOptionalProducts(optionalProducts);

        order.addOptionalProducts(products);

        order.calculateTotalSum();

        return order;
    }

    public List<Order> getAllOrders(){
        return this.em.createNamedQuery("Orders.All",Order.class).getResultList();
    }

    public void persist(Order order) throws ElementNotFound {

        this.em.persist(order);
    }

    public Order getOrderById(int id) throws ElementNotFound {
        List<Order> orders = this.em.createNamedQuery("Orders.Id",Order.class).setParameter("orderId",id).getResultList();

        if(orders != null) return orders.get(0);
        else throw new ElementNotFound("Order not found");
    }


    public void removeSuspendFromOrder(int id) {
        this.em.createNamedQuery("Orders.RemoveSuspend",Order.class).setParameter("orderId",id).executeUpdate();
    }

}