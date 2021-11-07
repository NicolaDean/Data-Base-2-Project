package it.polimi.db2.entitys.custom;

public class ReportData {
    private Integer id;
    private Integer count;

    public ReportData(Integer id, Integer count) {
        this.id=id;
        this.count=count;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getId() {
        return id;
    }
}
