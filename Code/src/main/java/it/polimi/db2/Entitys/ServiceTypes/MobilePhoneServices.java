package it.polimi.db2.Entitys.ServiceTypes;

import it.polimi.db2.Entitys.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_phone_services", schema = "test")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue(value = "MPS")
public class MobilePhoneServices extends Service {

    int   minutes;
    int   sms;
    double extraMinutesFee;
    double extraSMSFee;


    public MobilePhoneServices() {

    }

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }

    public void setExtraMinutesFee(double extraMinutesFee) {
        this.extraMinutesFee = extraMinutesFee;
    }

    public void setSms(int sms) {
        this.sms = sms;
    }

    public void setExtraSMSFee(double extraSMSFee) {
        this.extraSMSFee = extraSMSFee;
    }
}
