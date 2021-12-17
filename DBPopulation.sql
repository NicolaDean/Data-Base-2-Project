
insert into Users (username,password,email,type) values ("nico","1234","nico@gmail.com","user");
insert into Users (username,password,email,type) values ("fasa","1234","fasa@gmail.com","user");
insert into Users (username,password,email,type) values ("babbano","1234","babbano@gmail.com","user");
insert into Users (username,password,email,type) values ("admin","1234","admin@gmail.com","admin");

insert into Packages (name) values ("Basic");
insert into Packages (name) values ("Family");
insert into Packages (name) values ("Businnes");
insert into Packages (name) values ("All Inclusive");

insert into OptionalProducts (name,monthlyFee) values ("Iphone 8",20);
insert into OptionalProducts (name,monthlyFee) values ("TIM vision",5);
insert into OptionalProducts (name,monthlyFee) values ("Samsung Galaxy S20",20);
insert into OptionalProducts (name,monthlyFee) values ("SkyCalcio",30);
insert into OptionalProducts (name,monthlyFee) values ("Dzan",3);
insert into OptionalProducts (name,monthlyFee) values ("Netflix",5);



-- NOW GENERATE THE VALIDITY PERIOD TABLE


insert into Rate_costs (monthValidity,cost,packageId) values (12,20,1);
insert into Rate_costs (monthValidity,cost,packageId) values (24,18,1);
insert into Rate_costs (monthValidity,cost,packageId) values (36,15,1);

insert into Rate_costs (monthValidity,cost,packageId) values (12,25,2);
insert into Rate_costs (monthValidity,cost,packageId) values (24,20,2);
insert into Rate_costs (monthValidity,cost,packageId) values (36,16,2);

insert into Rate_costs (monthValidity,cost,packageId) values (12,27,3);
insert into Rate_costs (monthValidity,cost,packageId) values (24,22,3);
insert into Rate_costs (monthValidity,cost,packageId) values (36,18,3);

insert into Rate_costs (monthValidity,cost,packageId) values (12,30,4);
insert into Rate_costs (monthValidity,cost,packageId) values (24,25,4);
insert into Rate_costs (monthValidity,cost,packageId) values (36,20,4);


insert into services (packageID,DTYPE) values (1,"MIS");-- 1
insert into services (packageID,DTYPE) values (2,"MIS");-- 2
insert into services (packageID,DTYPE) values (2,"MPS");-- 3
insert into services (packageID,DTYPE) values (3,"MPS");-- 4
insert into services (packageID,DTYPE) values (4,"FIS");-- 5
insert into services (packageID,DTYPE) values (1,"FPS");-- 6

insert into mobile_internet_services (id,gigabyte,extraFee) values (1,5,0.5);
insert into mobile_internet_services (id,gigabyte,extraFee) values (2,10,0.3);
insert into mobile_phone_services (id,minutes,sms,extraMinutesFee,extraSMSFee) values (3,500,100,0.1,0.1);
insert into mobile_phone_services (id,minutes,sms,extraMinutesFee,extraSMSFee) values (4,1000,300,0.07,0.1);
insert into fixed_internet_services  (id,gigabyte,extraFee) values (5,300,0.3);


insert into Packages_OptionalProducts (packageId,productId) values (2,2);
insert into Packages_OptionalProducts (packageId,productId) values (3,2);
insert into Packages_OptionalProducts (packageId,productId) values (3,4);
insert into Packages_OptionalProducts (packageId,productId) values (4,2);
insert into Packages_OptionalProducts (packageId,productId) values (4,4);
insert into Packages_OptionalProducts (packageId,productId) values (4,3);
