package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;

public class ReportData {

    private String pack;
    private Long count;

    public ReportData(String pack, Long count) {
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
