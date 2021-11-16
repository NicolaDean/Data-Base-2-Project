
drop trigger if exists INSOLVENT_USER;
drop trigger if exists INSOLVENT_USER_REMOVAL;
DELIMITER $$
-- IF USER DO A FAILED ORDER THEN UPDATE USER TABLE FLAGGING IT AS INSOLVENT
create trigger INSOLVENT_USER
    after insert on Orders
    for each row
begin
    if ( new.status = false) then
    update Users set Users.insolvent = true where Users.id = new.userId;
    insert into FailedPayments (userId,orderId,faildate) values (new.userId,new.id,CURRENT_TIMESTAMP);
end if;
END $$

create trigger INSOLVENT_USER_REMOVAL
    after update on Orders
    for each row
begin
    if (new.status = true) AND -- user payed a suspended order i check if all his pending order are payed (if yes remove flag)
			(select count(*) from Orders as o where o.userId=new.userId and o.status = false) = 0
		then
    update Users set Users.insolvent = false where Users.id = new.userId;
end if;
if (new.status = false AND old.status = new.status)  then
			insert into FailedPayments (userId,orderId,faildate) values (new.userId,new.id,CURRENT_TIMESTAMP);
            SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Warning: c > 100!';
	
end if;
END $$
DELIMITER ;
select * from FailedPayments;
update Orders set status = false where id = 2;
select * from Orders;
select * from Users;
