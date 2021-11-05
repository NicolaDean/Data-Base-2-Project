package it.polimi.db2.entitys.ServiceTypes;

import it.polimi.db2.entitys.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_internet_services", schema = "test")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue(value = "MIS")
public class InternetServices extends Service {
    int   gigabyte;
    float extraFee;

    public String getExtraFee() {
        return extraFee + " $/GB Extra Traffic";
    }

    public String getGiga() {
        return gigabyte + " GB";
    }
}
