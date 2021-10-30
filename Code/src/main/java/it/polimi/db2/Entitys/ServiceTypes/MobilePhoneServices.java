package it.polimi.db2.Entitys.ServiceTypes;

import it.polimi.db2.Entitys.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_phone_services", schema = "test")
public class MobilePhoneServices extends Service {
    int   minutes;
    int   sms;
    float extraMinutesFee;
    float extraSMSFee;
}
