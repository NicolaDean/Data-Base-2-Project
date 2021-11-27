
drop table if exists FailedPayments;

drop table if exists mobile_phone_services;
drop table if exists fixed_phone_services;
drop table if exists mobile_internet_services;
drop table if exists fixed_internet_services;
drop table if exists services;
drop table if exists Orders_OptionalProducts;
drop table if exists Orders;
drop table if exists Packages_OptionalProducts;
drop table if exists Users;
create table Users(
                      id 			INT AUTO_INCREMENT,
                      username    VARCHAR(30) not null,
                      password	VARCHAR(30) not null,
                      email		VARCHAR(30) not null,
                      type      varchar(30) not null,
                      insolvent boolean,
                      PRIMARY KEY(id)
);

select * from Users;
-- Now create the optionals products
drop table if exists OptionalProducts;

create table OptionalProducts(
                                 id 			INT AUTO_INCREMENT,
                                 name 		VARCHAR(30) NOT NULL,
                                 monthlyFee  INT NOT NULL,
                                 PRIMARY KEY(id)
);

select * from OptionalProducts;


-- NOW Create the table to store the Packages
drop table if exists Rate_costs;
drop table if exists Packages;

create table Packages(
                         id 				INT AUTO_INCREMENT,
                         name			VARCHAR(30) not null,
                         PRIMARY KEY(id)
);

select * from Packages;

-- MANY TO MANY TABLE FOR PACKAGES AND OPTIONAL PRODUCTS

create table Packages_OptionalProducts(
                                          packageId INT,
                                          productId INT,

                                          FOREIGN KEY (packageId) REFERENCES Packages (id),
                                          FOREIGN KEY (productId) REFERENCES OptionalProducts(id)
);



create table Rate_costs(
                           id 				INT AUTO_INCREMENT,
                           monthValidity   INT not null,
                           cost			INT not null,
                           packageId       INT,
                           PRIMARY KEY(id),
                           FOREIGN KEY(packageId) REFERENCES Packages(id)
);

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

create table fixed_internet_services(
                                         id 			INT AUTO_INCREMENT,
                                         gigabyte		INT not null,
                                         extraFee       FLOAT not null,
                                         PRIMARY KEY(id),
                                         FOREIGN KEY (id) REFERENCES services (id)
);


select * from services;
select * from services natural join mobile_phone_services;

select * from Users;

create table Orders(
                       id 				INT AUTO_INCREMENT,
                       creationDate	    datetime,
                       userId			INT,
                       packageId        INT,
                       rateId			INT,
                       startDate		date,
                       totalPayment     FLOAT,
                       status		    bool,
                       PRIMARY KEY(id),
                       FOREIGN KEY (rateId) REFERENCES Rate_costs (id),
                       FOREIGN KEY (packageId) REFERENCES Packages (id),
                       FOREIGN KEY (userId) REFERENCES Users (id)

);

create table Orders_OptionalProducts(
                                        orderId INT,
                                        productId INT,

                                        FOREIGN KEY (orderId) REFERENCES Orders (id),
                                        FOREIGN KEY (productId) REFERENCES OptionalProducts(id)
);

select * from Orders;

select * from Orders where status = false;

-- ELENCO POSSIBILI TRIGGER
-- quando si aggiorna un ordine si controlla se per quell'utente sono spariti tutti i "pending payment" in tal caso rimuove il flag "insolvenza"
-- creare un trigger che in automatico cerca il numero di pagamenti falliti e in caso marchia lutente come insolvente
-- possiamo creare un trigger per aggiornare in automatico questa tabella
-- create table Activation_Schedule(
-- 	orderId 	INT,
--    DeactivationDate
-- );
-- populate with trigger

create table FailedPayments(
                               userId 	   INT,
                               orderId     INT,
                               faildate    datetime DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (userId) REFERENCES Users (id),
                               FOREIGN KEY (orderId) REFERENCES Orders (id)
);


