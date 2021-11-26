
drop trigger if exists INSOLVENT_USER;
drop trigger if exists INSOLVENT_USER_REMOVAL;

drop trigger if exists PurchasesCount_Population;
drop trigger if exists PurchasesCount_Population_Update;

drop trigger if exists OptionalProdAvgOnUpdate;
drop trigger if exists AddNoOptionalOrderToAVG;
drop trigger if exists OptionalProdAvg;

drop trigger if exists TotalPackageRevenue;
drop trigger if exists TotalPackageRevenue_Update;
drop trigger if exists PackageRevenueNoOptional;

drop trigger if exists UpdateInsolventUser;

drop trigger if exists UpdateBestOptional;

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
    if new.status = true then
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
    set orderVal  := (select monthValidity from Rate_costs where id=new.rateId);
		 -- If this is the first order on package then set count = 1 and insert new line
		if(  (select count(*) from PurchasesCount where 
			 name = pkgname and validity = orderVal  group by name,validity)>0) then
			
            SET SQL_SAFE_UPDATES=0;
			update PurchasesCount set PurchasesCount.count = PurchasesCount.count + 1
					where name = pkgname and validity = orderVal;
            SET SQL_SAFE_UPDATES=1;
		else
            insert into PurchasesCount (name,validity,count) values (pkgname,
																	 orderVal,
																	 1
																	 );
		end if;
        end if;
	END $$
    
create trigger  PurchasesCount_Population_Update
    after update on Orders
    for each row
	begin
		declare pkgname Varchar(20);
    declare orderVal int;
    if new.status = true then
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
    set orderVal  := (select monthValidity from Rate_costs where id=new.rateId);
		 -- If this is the first order on package then set count = 1 and insert new line
		if(  (select count(*) from PurchasesCount where 
			 name = pkgname and validity = orderVal  group by name,validity)>0) then
			
            SET SQL_SAFE_UPDATES=0;
			update PurchasesCount set PurchasesCount.count = PurchasesCount.count + 1
					where name = pkgname and validity = orderVal;
            SET SQL_SAFE_UPDATES=1;
		else
            insert into PurchasesCount (name,validity,count) values (pkgname,
																	 orderVal,
																	 1
																	 );
		end if;
        end if;
    END $$

create trigger AddNoOptionalOrderToAVG
	after insert on Orders
    for each row
    begin
		-- trigger OptionalProdAvg trigger
		insert into Orders_OptionalProducts (orderId,productId) values (new.id,null);
		delete from Orders_OptionalProducts where orderId = new.id;
    END $$
-- for each new order add the number of optional product purchased and packaged id to this view
create trigger OptionalProdAvg
	after insert on Orders_OptionalProducts
	for each row
    begin
    declare pkgcount INT;
    declare pkgname Varchar(20);
    declare stat  bool;
    set stat    := (select status from Orders where id=new.orderId);

    if stat = 1 then
    set pkgname   := (select name from Packages where Packages.id=(select packageId from Orders where id=new.orderId));
	set pkgcount  := (select count(*) from PurchasesCountGrouped group by name having name = pkgname) - 1;
		if (select count(name) from OptionalProductsAverage where name=pkgname) = 0 then
			if(new.productId is  null) then
				insert into OptionalProductsAverage (name,avg) values (pkgname ,0);
			else
				insert into OptionalProductsAverage (name,avg) values (pkgname ,1/(pkgcount+1));
			end if;
		else
			SET SQL_SAFE_UPDATES=0;
            if(new.productId is null) then
				update OptionalProductsAverage set avg = ((avg*pkgcount))/(pkgcount+1) where name=pkgname;
            else 
				update OptionalProductsAverage set avg = ((avg*pkgcount)+1)/(pkgcount+1) where name=pkgname;
            end if;
            SET SQL_SAFE_UPDATES=1;
        end if;
        end if;
    END $$
    
create trigger OptionalProdAvgOnUpdate
	after update on Orders
    for each row
    begin
	declare pkgcount INT;
    declare pkgname Varchar(20);
    declare prodCount INT;
    if new.status then
    set pkgname   := (select name from Packages where Packages.id=(select packageId from Orders where id=new.id));
	set pkgcount  := (select count(*) from PurchasesCountGrouped group by name having name = pkgname);
    set prodCount := (select count(*) from Orders_OptionalProducts group by orderId having  orderId= new.id);
		if (select count(name) from OptionalProductsAverage where name=pkgname) = 0 then
			if(prodCount is  null) then
				insert into OptionalProductsAverage (name,avg) values (pkgname ,0);
			else
				insert into OptionalProductsAverage (name,avg) values (pkgname ,prodCount);
			end if;
		else
			SET SQL_SAFE_UPDATES=0;
            if(prodCount is null) then
				update OptionalProductsAverage set avg = ((avg*pkgcount))/(pkgcount+1) where name=pkgname;
            else 
				update OptionalProductsAverage set avg = ((avg*pkgcount)+prodCount)/(pkgcount+1) where name=pkgname;
            end if;
            SET SQL_SAFE_UPDATES=1;
        end if;
        end if;
    END $$
    
create trigger TotalPackageRevenue
	after insert on Orders
    for each row
    begin
    declare pkgname Varchar(20);
    declare validity int;
	declare totalOfProducts int;
    if new.status = 1 then
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
    set validity  := (select monthValidity from Rate_costs where id=new.rateId);

		if (select count(name) from ValueOfSalesDetailed where name=pkgname) = 0 then
			insert into ValueOfSalesDetailed  (name,totalPayment,totalPaymentWithoutOP) values (pkgname,new.totalPayment,new.totalPayment);
            -- SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Warning: c > 99!';
        else
			begin
				SET SQL_SAFE_UPDATES=0;
				update ValueOfSalesDetailed set totalPayment =  totalPayment + new.totalPayment ,
                                                    totalPaymentWithoutOP = totalPaymentWithoutOP + new.totalPayment
													where name=pkgname;
				SET SQL_SAFE_UPDATES=1;                     
			end;
        end if;
        end if;
    END $$
-- remove optional revenue for each optional
create trigger PackageRevenueNoOptional 
	after insert on Orders_OptionalProducts
    for each row
    begin
    declare pkgId     INT;
	declare pkgname Varchar(20);
	declare price     INT;
    declare validity  INT;
	declare stat  bool;
    set stat    := (select status from Orders where id=new.orderId);

    if stat = 1 then
		 
         set pkgId     := (select packageId from Orders where id=new.orderId);
		 set pkgname   := (select name from Packages where Packages.id=pkgId);
         set validity  := (select monthValidity from Rate_costs where id=(select rateId from Orders where id=new.orderId));
		 set price     := (select monthlyFee from OptionalProducts where id=new.productId)*validity;
         
         if(price is null)then
			set price := 0;
         end if;
        SET SQL_SAFE_UPDATES=0;    
        update ValueOfSalesDetailed set 
				totalPaymentWithoutOP = totalPaymentWithoutOP - price
				where name=pkgname;
		SET SQL_SAFE_UPDATES=1; 
        
	end if;
    END $$
    
create trigger TotalPackageRevenue_Update
	after update on Orders
	for each row
    begin
    declare pkgname Varchar(20);
	declare revenueOptional   INT;
    declare validity  INT;
		if new.status = 1 then
		set pkgname   := (select name from Packages where Packages.id=new.packageId);
        set validity  := (select monthValidity from Rate_costs where id=(select rateId from Orders where id=new.id));
		set revenueOptional := (select sum(monthlyFee) from Orders_OptionalProducts join OptionalProducts on productId = id group by orderId having orderId=new.id)*validity;
        
        if(revenueOptional is null) then
			set revenueOptional := 0;
        end if;
        if (select count(name) from ValueOfSalesDetailed where name=pkgname) = 0 then
			insert into ValueOfSalesDetailed  (name,totalPayment,totalPaymentWithoutOP) values (pkgname,new.totalPayment,new.totalPayment-revenueOptional);
            -- SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Warning: c > 99!';
        else
			begin
				SET SQL_SAFE_UPDATES=0;
				update ValueOfSalesDetailed set totalPayment =  totalPayment + new.totalPayment ,
                                                    totalPaymentWithoutOP = totalPaymentWithoutOP + (new.totalPayment -revenueOptional)
													where name=pkgname;
				SET SQL_SAFE_UPDATES=1;                     
			end;
        end if;
        
        end if;
    END$$
    
-- todo how can we do a trigger when 
    
create trigger UpdateBestOptional
    after insert on Orders_OptionalProducts
	for each row
    begin
		 declare prodName Varchar(20);
         declare price     INT;
         declare validity  INT;
         
         declare stat  bool;
		set stat    := (select status from Orders where id=new.orderId);

		if stat = 1 then
		 set prodName  := (select name from OptionalProducts where id=new.productId);
         set validity  := (select monthValidity from Rate_costs where id=(select rateId from Orders where id=new.orderId));
		 set price     := (select monthlyFee from OptionalProducts where id=new.productId)*validity;
         
		 if(prodName is not null) then
			 if (select count(name) from OptionalProductBestSeller where name=prodName) = 0 then
				insert into OptionalProductBestSeller (name,amountSold,value) values (prodName,1,price);
			 else
			 SET SQL_SAFE_UPDATES=0;    
				update OptionalProductBestSeller set value = value + price,
													 amountSold = amountSold + 1 
													 where name=prodName;
				SET SQL_SAFE_UPDATES=1;    
			 end if;
		end if;
        end if;
    END$$
 /*   
create trigger UpdateBestOptional_OnUpdate
	after update on Orders
    for each row
		begin
		 declare prodName Varchar(20);
         declare revenue     INT;
         declare validity  INT;
		 declare count  INT;
		if new.status = 1 then
			set prodName  := (select name from OptionalProducts where id=new.productId);
         set validity  := (select monthValidity from Rate_costs where id=(select rateId from Orders where id=new.orderId));
		 set revenue   := (select sum(monthlyFee) from Orders_OptionalProducts join OptionalProducts group by productId having orderId=new.id)*validity;
         set count     := (select count(*) from Orders_OptionalProducts join OptionalProducts group by p having orderId=new.id);
		 
         if(revenue is null) then
			set revenue := 0;
         end if;
         
          if(count is null) then
			set count := 0;
         end if;
         if(prodName is not null) then
			 if (select count(name) from OptionalProductBestSeller where name=prodName) = 0 then
				insert into OptionalProductBestSeller (name,amountSold,value) values (prodName,count,revenue);
			 else
			 SET SQL_SAFE_UPDATES=0;    
				update OptionalProductBestSeller set value = value + revenue,
													 amountSold = amountSold + count 
													 where name=prodName;
				SET SQL_SAFE_UPDATES=1;    
			 end if;
		end if;
        end if;
	END $$*/
    
create trigger UpdateInsolventUser
    after insert on FailedPayments
	for each row
    begin
		declare failedCount INT;
        declare failedImport INT;
        declare usr Varchar(30);
        declare mail	 Varchar(30);

        set failedCount := (select count(*) from FailedPayments  group by userId having userId = new.userId );
        set failedImport := (select totalPayment from Orders where id=new.orderId);
        set usr := (select username from Users where id=new.userId);
        set mail    := (select email from Users where id=new.userId);
        
         -- SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT =username ;
        if(failedCount >=3) then
			if( (select id from InsolventReport where id=new.userId) is  null) then
				insert into InsolventReport (id,username,email,lastDate,amount) values (new.userId,usr ,mail ,new.faildate,failedImport);
			else
				update InsolventReport set lastDate = new.faildate, amount = failedImport where id=new.userId;
			end if;
        end if;
    END$$
DELIMITER ;




select count(distinct(name)) from PurchasesCount where name = (select name from Packages where id=5);
insert into Orders (userId,packageId,rateId) values (1,3,2);
select * from FailedPayments;
update Orders set status = false where id = 2;
select * from Orders;
select * from Packages;
select * from PurchasesCount;
select * from Orders_OptionalProducts;
select * from OptionalProductsCount;
select * from OptionalProductsAverage;
select * from ValueOfSalesDetailed;
select * from OptionalProductBestSeller;
select * from InsolventReport;

(select username from Users where id=1)
