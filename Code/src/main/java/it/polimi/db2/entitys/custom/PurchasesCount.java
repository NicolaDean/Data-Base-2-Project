package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


public class PurchasesCount {

    private String pack;
    private Long count;

    public PurchasesCount(String pack, Long count) {
        this.pack=pack;
        this.count=count;
    }


    public Long getCount() {
        return count;
    }

    public String getPack() {
        return pack;
    }
}
