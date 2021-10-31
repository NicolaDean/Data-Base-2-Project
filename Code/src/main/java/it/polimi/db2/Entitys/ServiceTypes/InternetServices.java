package it.polimi.db2.Entitys.ServiceTypes;

import it.polimi.db2.Entitys.Service;

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
}
