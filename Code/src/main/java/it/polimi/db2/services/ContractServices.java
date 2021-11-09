package it.polimi.db2.services;

import it.polimi.db2.entitys.*;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.exception.NoPackageFound;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

//TODO VERIFICARE CHE NON POSSA ESSERE STATEFULL
@Stateless
public class ContractServices extends BasicService{

    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;

    /**
     *
     * @param id id of product
     * @return optional product with id from db
     * @throws ElementNotFound if db dosnt find product
     */
    public OptionalProduct getProductById(int id) throws ElementNotFound {
        OptionalProduct op = this.em.find(OptionalProduct.class,id);

        if(op == null) throw new ElementNotFound("OptionalProduct Not Founded");

        return op;
    }

    public RateCost getRateById(int id) throws ElementNotFound {
        RateCost rate = this.em.find(RateCost.class,id);

        if(rate == null) throw new ElementNotFound("Rate not Founded");
        return rate;
    }

    public List<OptionalProduct> convertOptionalProducts(String[] optionalProducts) throws ElementNotFound {
        //FIND SELECTED PRODUCTS
        List<OptionalProduct> products = new ArrayList<OptionalProduct>();
        for (String x : optionalProducts) {
            int productId = Integer.parseInt(x);

            try {
                //Add to the order all the selected optional products
                OptionalProduct op = this.getProductById(productId);
                products.add(op);

            } catch (ElementNotFound e) {
                throw new ElementNotFound("No Optional product");
            }
        }
        return products;
    }

    public Order createContract( int packageId, int rateId, String[] optionalProducts) throws ElementNotFound {
        Order order = new Order();

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
        List<OptionalProduct> products = this.convertOptionalProducts(optionalProducts);

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