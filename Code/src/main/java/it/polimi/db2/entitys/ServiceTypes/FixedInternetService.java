package it.polimi.db2.entitys.ServiceTypes;

import it.polimi.db2.entitys.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "fixed_internet_services", schema = "test")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue(value = "FIS")
public class FixedInternetService extends Service {
    int   gigabyte;
    float extraFee;

    public String getExtraFee() {
        return extraFee + " $/GB Extra Traffic";
    }

    public String getGiga() {
        return gigabyte + " GB";
    }

    public void setGigabyte(int gigabyte) {
        this.gigabyte = gigabyte;
    }

    public void setExtraFee(float extraFee) {
        this.extraFee = extraFee;
    }
}
