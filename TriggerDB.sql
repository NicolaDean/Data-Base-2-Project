
drop trigger if exists INSOLVENT_USER;
drop trigger if exists INSOLVENT_USER_REMOVAL;

drop trigger if exists NewPackage;

drop trigger if exists PurchaseCountInitialize;
drop trigger if exists PurchasesCount_Population;
drop trigger if exists PurchasesCount_Population_Update;

drop trigger if exists OptionalProdAvgOnUpdate;
drop trigger if exists AddNoOptionalOrderToAVG;
drop trigger if exists OptionalProdAvg;

drop trigger if exists TotalPackageRevenue;
drop trigger if exists TotalPackageRevenue_Update;
drop trigger if exists PackageRevenueNoOptional;

drop trigger if exists UpdateInsolventUser;

drop trigger if exists InitializeBestOptional;
drop trigger if exists UpdateBestOptional;
drop trigger if exists UpdateBestOptional_OnUpdate;

drop trigger if exists ActivationScheduleServices;
drop trigger if exists ActivationScheduleOptional;
drop trigger if exists ActivationSchedule_OnUpdate;
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


    create trigger NewPackage
		after insert on Packages
        for each row
        begin
			-- Optional Average per package
			insert into OptionalProductsAverage (name,avg) values (new.name ,0);
            -- Revenue per package
            insert into ValueOfSalesDetailed  (name,totalPayment,totalPaymentWithoutOP) values (new.name,0,0);
        END $$

create trigger PurchaseCountInitialize 
		after insert on Rate_costs
        for each row
        begin
			declare pkgname Varchar(30);
			set pkgname   := (select name from Packages where Packages.id=new.packageId);
			insert into PurchasesCount (name,validity,count) values (pkgname,new.monthValidity,0);
        END$$
        
-- populate view for counting Purchases of Packages
create trigger PurchasesCount_Population
	after insert on Orders 
    for each row
	begin
	declare pkgname Varchar(30);
    declare orderVal int;
    if new.status = true then
    set pkgname   := (select name from Packages where Packages.id=new.packageId);
    set orderVal  := (select monthValidity from Rate_costs where id=new.rateId);
		 -- If this is the first order on package then set count = 1 and insert new line
            SET SQL_SAFE_UPDATES=0;
			update PurchasesCount set PurchasesCount.count = PurchasesCount.count + 1
					where name = pkgname and validity = orderVal;
            SET SQL_SAFE_UPDATES=1;
        end if;
	END $$
    
create trigger  PurchasesCount_Population_Update
    after update on Orders
    for each row
	begin
	declare pkgname Varchar(30);
    declare orderVal int;
    if new.status = true then
		set pkgname   := (select name from Packages where Packages.id=new.packageId);
		set orderVal  := (select monthValidity from Rate_costs where id=new.rateId);

            SET SQL_SAFE_UPDATES=0;
			update PurchasesCount set PurchasesCount.count = PurchasesCount.count + 1
					where name = pkgname and validity = orderVal;
            SET SQL_SAFE_UPDATES=1;
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
    declare pkgname Varchar(30);
    declare stat  bool;
    set stat    := (select status from Orders where id=new.orderId);

    if stat = 1 then
    set pkgname   := (select name from Packages where Packages.id=(select packageId from Orders where id=new.orderId));
	set pkgcount  := (select count(*) from PurchasesCountGrouped group by name having name = pkgname) - 1;
			SET SQL_SAFE_UPDATES=0;
            if(new.productId is null) then
				update OptionalProductsAverage set avg = ((avg*pkgcount))/(pkgcount+1) where name=pkgname;
            else 
				update OptionalProductsAverage set avg = ((avg*pkgcount)+1)/(pkgcount+1) where name=pkgname;
            end if;
            SET SQL_SAFE_UPDATES=1;
        end if;
    END $$
    
create trigger OptionalProdAvgOnUpdate
	after update on Orders
    for each row
    begin
	declare pkgcount INT;
    declare pkgname Varchar(30);
    declare prodCount INT;
    if new.status then
    set pkgname   := (select name from Packages where Packages.id=(select packageId from Orders where id=new.id));
	set pkgcount  := (select count(*) from PurchasesCountGrouped group by name having name = pkgname);
    set prodCount := (select count(*) from Orders_OptionalProducts group by orderId having  orderId= new.id);
			SET SQL_SAFE_UPDATES=0;
            if(prodCount is null) then
				update OptionalProductsAverage set avg = ((avg*pkgcount))/(pkgcount+1) where name=pkgname;
            else 
				update OptionalProductsAverage set avg = ((avg*pkgcount)+prodCount)/(pkgcount+1) where name=pkgname;
            end if;
            SET SQL_SAFE_UPDATES=1;
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
			SET SQL_SAFE_UPDATES=0;
				update ValueOfSalesDetailed set totalPayment =  totalPayment + new.totalPayment ,
                                                    totalPaymentWithoutOP = totalPaymentWithoutOP + new.totalPayment
													where name=pkgname;
			SET SQL_SAFE_UPDATES=1;                     
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
    declare pkgname Varchar(30);
	declare revenueOptional   INT;
    declare validity  INT;
		if new.status = 1 then
			set pkgname   := (select name from Packages where Packages.id=new.packageId);
			set validity  := (select monthValidity from Rate_costs where id=(select rateId from Orders where id=new.id));
			set revenueOptional := (select sum(monthlyFee) from Orders_OptionalProducts join OptionalProducts on productId = id group by orderId having orderId=new.id)*validity;
        
			if(revenueOptional is null) then
				set revenueOptional := 0;
			end if;
        
			SET SQL_SAFE_UPDATES=0;
				update ValueOfSalesDetailed set totalPayment =  totalPayment + new.totalPayment ,
                                                    totalPaymentWithoutOP = totalPaymentWithoutOP + (new.totalPayment -revenueOptional)
													where name=pkgname;
			SET SQL_SAFE_UPDATES=1;                     
        end if;
    END$$
    
-- todo how can we do a trigger when 
    
-- when create a OptionalProduct add it to this list
create trigger InitializeBestOptional
	after insert on OptionalProducts
    for each row
    begin
		insert into OptionalProductBestSeller (name,amountSold,value) values (new.name,0,0);
    END$$
    
create trigger UpdateBestOptional
    after insert on Orders_OptionalProducts
	for each row
    begin
		 declare prodName Varchar(30);
         declare price     INT;
         declare validity  INT;
         
         declare stat  bool;
		set stat    := (select status from Orders where id=new.orderId);

		if stat = 1 then
		 set prodName  := (select name from OptionalProducts where id=new.productId);
         set validity  := (select monthValidity from Rate_costs where id=(select rateId from Orders where id=new.orderId));
		 set price     := (select monthlyFee from OptionalProducts where id=new.productId)*validity;
         
			if(prodName is not null) then
			 SET SQL_SAFE_UPDATES=0;    
				update OptionalProductBestSeller set value = value + price,
													 amountSold = amountSold + 1 
													 where name=prodName;
				SET SQL_SAFE_UPDATES=1;    
			 end if;
        end if;
    END$$



create trigger UpdateBestOptional_OnUpdate
	after update on Orders
    for each row
		begin
         declare revenue     INT;
         declare validity  INT;
		 declare count  INT;
		if new.status = 1 then
			 SET SQL_SAFE_UPDATES=0;    
				update OptionalProductBestSeller as best join OptionalProducts as op
									on best.name = op.name
									set amountSold = amountSold + 1,
									value = amountSold * op.monthlyFee
									where op.name in (select op1.name from OptionalProducts as op1 join Orders_OptionalProducts as ord
													on id = productId where ord.orderId =new.id);
				SET SQL_SAFE_UPDATES=1;    
		end if;
	END $$

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
    
create trigger ActivationScheduleServices
	after insert on Orders
	for each row
	begin
    declare validity int;
    if new.status = true then
        set validity := (select monthValidity from Rate_costs where id = new.rateId);
        
		insert into ActivationSchedule_Services (serviceId,userId,activationdate,deactivationDate)
				select id,new.userId,new.startDate,DATE_ADD(new.startDate, INTERVAL validity MONTH) from services as s where s.packageId = new.packageId;
    end if;
    END $$
    
create  trigger ActivationScheduleOptional
	after insert on Orders_OptionalProducts
    for each row
    begin
    declare usr INT;
    declare actDate timestamp;
    declare rateId int;
    declare validity int;
    
	declare stat  bool;
	set stat    := (select status from Orders where id=new.orderId);

	if stat = 1 then
		set usr      := (select userId  from Orders where id = new.orderId);
		set actDate  := (select startDate from Orders where id=new.orderId);
		set rateId   := (select rateId from Orders where id = new.orderId);
		set validity := (select monthValidity from Rate_costs where id = rateId);
			
        if(new.productId is not null) then    
		insert into ActivationSchedule_Optional (productId,userId,activationdate,deactivationDate)
				values (new.productId, usr , actDate, DATE_ADD(actDate,INTERVAL validity MONTH));
		end if;
    end if;
    END $$
    
create trigger ActivationSchedule_OnUpdate
	after update on Orders
    for each row
    begin
	declare validity int;
    if new.status = true then
        set validity := (select monthValidity from Rate_costs where id = new.rateId);
        
        -- update services activation
        insert into ActivationSchedule_Services (serviceId,userId,activationdate,deactivationDate)
				select id,new.userId,new.startDate,DATE_ADD(new.startDate, INTERVAL validity MONTH) from services as s where s.packageId = new.packageId;
		 -- update optional activation
         insert into ActivationSchedule_Optional (productId,userId,activationdate,deactivationDate)
				select id,new.userId,new.startDate,DATE_ADD(new.startDate, INTERVAL validity MONTH) 
						from OptionalProducts join Orders_OptionalProducts on id = productId
                        where orderId = new.id;
        end if;
    END$$
DELIMITER ;




select count(distinct(name)) from PurchasesCount where name = (select name from Packages where id=5);
select * from FailedPayments;
select * from Orders;
select * from Packages;
select * from PurchasesCount;
select * from Orders_OptionalProducts;
select * from OptionalProductsCount;
select * from OptionalProductsAverage;
select * from ValueOfSalesDetailed;
select * from OptionalProductBestSeller;
select * from InsolventReport;
select * from ActivationSchedule_Optional;
select * from ActivationSchedule_Services;


select 1,id,CURRENT_TIMESTAMP,DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 12 MONTH) from services as s where s.packageId = 2;
