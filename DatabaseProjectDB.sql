
drop table if exists Users;
drop table if exists mobile_phone_services;
drop table if exists fixed_phone_services;
drop table if exists mobile_internet_services;
drop table if exists fixed_internet_services;
drop table if exists services;

drop table if exists Packages_OptionalProducts;
create table Users(
                      id 			INT AUTO_INCREMENT,
                      username    VARCHAR(30) not null,
                      password	VARCHAR(30) not null,
                      email		VARCHAR(30) not null,
                      PRIMARY KEY(id)
);

insert into Users (username,password,email) values ("nico","1234","nico@gmail.com");
insert into Users (username,password,email) values ("fasa","1234","fasa@gmail.com");
insert into Users (username,password,email) values ("babbano","1234","babbano@gmail.com");
select * from Users;
-- Now create the optionals products
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

-- MANY TO MANY TABLE FOR PACKAGES AND OPTIONAL PRODUCTS

create table Packages_OptionalProducts(
                                          packageId INT,
                                          productId INT,

                                          FOREIGN KEY (packageId) REFERENCES Packages (id),
                                          FOREIGN KEY (productId) REFERENCES OptionalProducts(id)
);

insert into Packages_OptionalProducts (packageId,productId) values (2,2);
insert into Packages_OptionalProducts (packageId,productId) values (3,2);
insert into Packages_OptionalProducts (packageId,productId) values (3,4);
insert into Packages_OptionalProducts (packageId,productId) values (4,2);
insert into Packages_OptionalProducts (packageId,productId) values (4,4);
insert into Packages_OptionalProducts (packageId,productId) values (4,3);
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


create table services(
                         id				INT AUTO_INCREMENT,
                         packageId       INT not null,
                         DTYPE	        VARCHAR(3),
                         PRIMARY KEY(id),
                         FOREIGN KEY(packageId) REFERENCES Packages (id)
);

create table mobile_phone_services(
                                      id 				INT,
                                      minutes         INT not null,
                                      sms				INT not null,
                                      extraMinutesFee FLOAT not null,
                                      extraSMSFee		FLOAT not null,
                                      PRIMARY KEY(id),
                                      FOREIGN KEY (id) REFERENCES services (id)
);

-- and for fixed_internet???? is the same table, do 2 table or keep them united?
create table mobile_internet_services(
                                         id 				INT AUTO_INCREMENT,
                                         gigabyte		INT not null,
                                         extraFee        FLOAT not null,
                                         PRIMARY KEY(id),
                                         FOREIGN KEY (id) REFERENCES services (id)
);


insert into services (packageID,DTYPE) values (1,"MIS");-- 1
insert into services (packageID,DTYPE) values (2,"MIS");-- 2
insert into services (packageID,DTYPE) values (2,"MPS");-- 3
insert into services (packageID,DTYPE) values (3,"MPS");-- 4

insert into mobile_internet_services (id,gigabyte,extraFee) values (1,5,0.5);
insert into mobile_internet_services (id,gigabyte,extraFee) values (2,10,0.3);

insert into mobile_phone_services (id,minutes,sms,extraMinutesFee,extraSMSFee) values (3,500,100,0.1,0.1);
insert into mobile_phone_services (id,minutes,sms,extraMinutesFee,extraSMSFee) values (4,1000,300,0.07,0.1);

select * from services;
select * from services natural join mobile_phone_services;