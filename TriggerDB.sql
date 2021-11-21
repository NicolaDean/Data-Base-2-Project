
drop trigger if exists INSOLVENT_USER;
drop trigger if exists INSOLVENT_USER_REMOVAL;
drop trigger if exists PurchasesCount_Population;
drop trigger if exists OptionalProdInOrdersCount;
drop trigger if exists OptionalProdAvg;
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
	end if;
	END $$

-- populate view for counting Purchases of packages
create trigger PurchasesCount_Population
	after insert on Orders 
    for each row
	begin
	declare pkgname Varchar(20);
    declare orderVal int;
    set pkgname   := (select name from packages where packages.id=new.packageId);
    set orderVal  := (select monthValidity from rate_costs where id=new.rateId);
		 -- If this is the first order on package then set count = 1 and insert new line
		if( (select count(distinct(name)) from PurchasesCount where 
			 name = pkgname = 0 )) then
			insert into purchasescount (name,validity,count) values (pkgname,
																	 orderVal,
																	 1
																	 );
		else
			SET SQL_SAFE_UPDATES=0;
			update purchasescount set count = purchasescount.count + 1 where name = pkgname;
            SET SQL_SAFE_UPDATES=1;
		end if;
	END $$

-- for each new order add the number of optional product purchased and packaged id to this view
create trigger OptionalProdInOrdersCount
	after insert on Orders
    for each row
    begin
		insert into OptionalProductsCount (packageId,optcount) 
			select new.packageId, count(*) as optcount
                                    from Orders as o left join Orders_OptionalProducts as opt
                                                               on o.id=opt.orderId
                                    group by o.id
                                    having o.id = new.id;
    END $$
    
create trigger OptionalProdAvg
	after insert on OptionalProductsCount
	for each row
    begin
    declare pkgcount INT;
    declare pkgname Varchar(20);
    set pkgname   := (select name from packages where packages.id=new.packageId);
	set pkgcount  := (select count from PurchasesCountGrouped where name = pkgname) - 1;
		if (select count(name) from OptionalProductsAverage where name=pkgname) = 0 then
			insert into OptionalProductsAverage (name,avg) values (pkgname ,new.optcount);
		else
			SET SQL_SAFE_UPDATES=0;
			update OptionalProductsAverage set avg = ((avg*pkgcount)+new.optcount)/(pkgcount+1) where name=pkgname;
            SET SQL_SAFE_UPDATES=1;
        end if;
    END $$
DELIMITER ;


select * from PurchasesCount;
select count(distinct(name)) from PurchasesCount where name = (select name from packages where id=5);
insert into orders (userId,packageId,rateId) values (1,4,2);
select * from FailedPayments;
update Orders set status = false where id = 2;
select * from Orders;
select * from packages;
select * from Orders_OptionalProducts;
select * from OptionalProductsCount;
select * from OptionalProductsAverage;

select o.packageId as packageId, count(opt.productId) as optcount
                                    from Orders as o left join Orders_OptionalProducts as opt
                                                               on o.id=opt.orderId
                                    group by o.id
                                    having o.id = 7;
