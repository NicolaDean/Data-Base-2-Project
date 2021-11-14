package it.polimi.db2.entitys.ServiceTypes;

import it.polimi.db2.entitys.Service;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "services", schema = "test")
@DiscriminatorValue("FPS")
public class FixedPhoneService extends Service {

}
