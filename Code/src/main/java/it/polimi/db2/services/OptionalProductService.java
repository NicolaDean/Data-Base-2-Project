package it.polimi.db2.services;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.exception.ElementNotFound;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OptionalProductService extends BasicService{

    public void createProduct(String name,int monthFee)
    {
        OptionalProduct product = new OptionalProduct();

        product.setName(name);
        product.setMonthlyFee(monthFee);

        this.em.persist(product);

    }

    public List<OptionalProduct> getOptionalProducts()
    {
        return this.em.createNamedQuery("OptionalProd.findAll",OptionalProduct.class).getResultList();
    }

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
}
