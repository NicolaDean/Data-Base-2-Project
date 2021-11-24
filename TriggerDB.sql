
drop trigger if exists INSOLVENT_USER;
drop trigger if exists INSOLVENT_USER_REMOVAL;
drop trigger if exists PurchasesCount_Population;
drop trigger if exists OptionalProdInOrdersCount;
drop trigger if exists OptionalProdAvg;
drop trigger if exists ValueOfSalesDetailedUpdate;
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

-- populate view for counting Purchases of Packages
create trigger PurchasesCount_Population
	after insert on Orders 
    for each row
	begin
	declare pkgname Varchar(20);
    declare orderVal int;
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
    set orderVal  := (select monthValidity from Rate_costs where id=new.rateId);
		 -- If this is the first order on package then set count = 1 and insert new line
		if(  (select count(*) from PurchasesCount where 
			 name = pkgname and validity = orderVal  group by name,validity)>0) then
			
            SET SQL_SAFE_UPDATES=0;
			update PurchasesCount set PurchasesCount.count = PurchasesCount.count + 1 where name = pkgname and validity = orderVal;
            SET SQL_SAFE_UPDATES=1;
		else
            insert into PurchasesCount (name,validity,count) values (pkgname,
																	 orderVal,
																	 1
																	 );
		end if;
	END $$

-- for each new order add the number of optional product purchased and packaged id to this view
create trigger OptionalProdInOrdersCount
	after insert on Orders
    for each row
    begin
    declare prodCount INT default null;
    
    set prodCount := (select  count(*) as optcount
                                    from Orders as o left outer join Orders_OptionalProducts as opt
                                                               on o.id=opt.orderId
                                    group by o.id
                                    having o.id = new.id);
		-- TODO CHECK WHY IF prodCount = vuoto mette 1 in optcount
		insert into OptionalProductsCount (packageId,optcount)  values (new.packageId,prodCount);
    END $$
    
create trigger OptionalProdAvg
	after insert on OptionalProductsCount
	for each row
    begin
    declare pkgcount INT;
    declare pkgname Varchar(20);
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
	set pkgcount  := (select count from PurchasesCountGrouped where name = pkgname) - 1;
		if (select count(name) from OptionalProductsAverage where name=pkgname) = 0 then
			insert into OptionalProductsAverage (name,avg) values (pkgname ,new.optcount);
		else
			SET SQL_SAFE_UPDATES=0;
			update OptionalProductsAverage set avg = ((avg*pkgcount)+new.optcount)/(pkgcount+1) where name=pkgname;
            SET SQL_SAFE_UPDATES=1;
        end if;
    END $$
    
create trigger ValueOfSalesDetailedUpdate
	after insert on Orders 
    for each row
    begin
    declare pkgname Varchar(20);
    declare validity int;
	declare totalOfProducts int default 0;
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
    set validity  := (select monthValidity from Rate_costs where id=new.rateId);
    set totalOfProducts := (select sum(validity*monthlyFee) 
												from OptionalProducts join Orders_OptionalProducts 
                                                on  OptionalProducts.id = productId 
                                                group by orderId
												having orderId = new.id); -- revenue of optional prod for insereted order;
		if (select count(name) from ValueOfSalesDetailed where name=pkgname) = 0 then
			insert into ValueOfSalesDetailed  
						select p.name as name,sum(o.totalPayment) as totalPayment,1
                                from Orders as o join Packages as p
                                where p.id=o.packageId
                                group by o.packageId;
		else
			begin
					SET SQL_SAFE_UPDATES=0;
					update ValueOfSalesDetailed set totalPayment =  totalPayment + new.totalPayment ,
                                                    totalPaymentWithoutOP = totalPaymentWithoutOP + (new.totalPayment - totalOfProducts)
                                                    where name=pkgname;
					SET SQL_SAFE_UPDATES=1;
			end;
        end if;
    END $$;
DELIMITER ;


select * from PurchasesCount;
select count(distinct(name)) from PurchasesCount where name = (select name from Packages where id=5);
insert into Orders (userId,packageId,rateId) values (1,4,2);
select * from FailedPayments;
update Orders set status = false where id = 2;
select * from Orders;
select * from Packages;
select * from Orders_OptionalProducts;
select * from OptionalProductsCount;
select * from OptionalProductsAverage;
select * from ValueOfSalesDetailed;
select o.packageId as packageId, count(opt.productId) as optcount
                                    from Orders as o left join Orders_OptionalProducts as opt
                                                               on o.id=opt.orderId
                                    group by o.id
                                    having o.id = 7;
(select sum(12*monthlyFee) 
												from optionalproducts join orders_optionalproducts 
                                                on  optionalproducts.id = productId 
                                                group by orderId
												having orderId = 8);
                                                
                                                
select count(*) from PurchasesCount group by name,validity having
 name = "Family" and validity = 12;
 
 select 1,count(*) as optcount
                                    from Orders as o left join Orders_OptionalProducts as opt
                                                               on o.id=opt.orderId
                                    group by o.id
                                    having o.id = 1;