package it.polimi.db2.Entitys.ServiceTypes;

import it.polimi.db2.Entitys.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_internet_services", schema = "test")
public class InternetServices extends Service {
    int   gigabyte;
    float extraFee;
}
