package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

@Entity
@NamedQuery(name = "Report.InsolventReport", query = "select i from InsolventReport i")
@Table(name="InsolventReport", schema="test")
public class InsolventReport {
    @Id
    private int id;
    private String username;
    private String email;
    private Date lastDate;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
