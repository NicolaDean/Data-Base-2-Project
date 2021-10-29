
drop table if exists OptionalProducts;

create table OptionalProducts(
	id 			INT AUTO_INCREMENT,
    name 		VARCHAR(30) NOT NULL,
    monthlyFee  INT NOT NULL,
    PRIMARY KEY(id)
);

insert into OptionalProducts (name,monthlyFee) values ("Iphone 8",20);
insert into OptionalProducts (name,monthlyFee) values ("TIM vision",5);
insert into OptionalProducts (name,monthlyFee) values ("Samsung Galaxy S20",20);
insert into OptionalProducts (name,monthlyFee) values ("SkyCalcio",30);
insert into OptionalProducts (name,monthlyFee) values ("Dzan",3);
insert into OptionalProducts (name,monthlyFee) values ("Netflix",5);

select * from OptionalProducts;


-- NOW Create the table to store the Packages
drop table if exists Rate_costs;
drop table if exists Packages;

create table Packages(
	id 				INT AUTO_INCREMENT,
    name			VARCHAR(30) not null,		
    PRIMARY KEY(id)
);

insert into Packages (name) values ("Basic");
insert into Packages (name) values ("Family");
insert into Packages (name) values ("Businnes");
insert into Packages (name) values ("All Inclusive");

select * from Packages;

-- NOW GENERATE THE VALIDITY PERIOD TABLE



create table Rate_costs(
	id 				INT AUTO_INCREMENT,
    monthValidity   INT not null,
    cost			INT not null,
    packageId       INT,
    PRIMARY KEY(id),
    FOREIGN KEY(packageId) REFERENCES Packages(id)
);

insert into Rate_costs (monthValidity,cost,packageId) values (12,20,1);
insert into Rate_costs (monthValidity,cost,packageId) values (24,18,1);
insert into Rate_costs (monthValidity,cost,packageId) values (36,15,1);

select * from Rate_costs;

-- Now We Should Create all the services Tables

-- Inhinerance tables with specific services types

-- Basic service
drop table if exists mobile_phone_services;
drop table if exists fixed_phone_services;
drop table if exists mobile_internet_services;
drop table if exists fixed_internet_services;
drop table if exists services;

create table services(
	id				INT AUTO_INCREMENT,
    PRIMARY KEY(id)
);

create table mobile_phone_services(
	id 				INT AUTO_INCREMENT,
    minutes         INT not null,
    sms				INT not null,
    extraMinutesFee FLOAT not null,
    extraSMSFee		FLOAT not null,
    serviceId		INT not null,
    PRIMARY KEY(id),
    FOREIGN KEY (serviceId) REFERENCES services (id)
);

-- and for fixed_internet???? is the same table, do 2 table or keep them united?
create table mobile_internet_services(
	id 				INT AUTO_INCREMENT,
    gigabyte		INT not null,
    extraFee        FLOAT not null,
    serviceId		INT not null,
    PRIMARY KEY(id),
    FOREIGN KEY (serviceId) REFERENCES services (id)
);


insert into services () values ();-- 1
insert into services () values ();-- 2
insert into services () values ();-- 3
insert into services () values ();-- 4
insert into services () values ();-- 5

insert into mobile_internet_services (gigabyte,extraFee,serviceId) values (5,0.5,1);
insert into mobile_internet_services (gigabyte,extraFee,serviceId) values (10,0.3,2);

insert into mobile_phone_services (minutes,sms,extraMinutesFee,extraSMSFee,serviceId) values (500,100,0.1,0.1,3);
insert into mobile_phone_services (minutes,sms,extraMinutesFee,extraSMSFee,serviceId) values (1000,300,0.07,0.1,4);

