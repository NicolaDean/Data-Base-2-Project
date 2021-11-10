
drop view if exists PurchasesCount;
drop view if exists PurchasesCountGrouped;

create view PurchasesCount as (
                              Select Packages.name as name,Rate_costs.monthValidity as validity,count(*) as count
                              from Orders as o join Packages  join Rate_costs
                              on o.packageId=Packages.id and Rate_costs.packageId=o.packageId and o.rateId=Rate_costs.id
                              group by o.packageId, Rate_costs.id
                                  );

create view PurchasesCountGrouped as (
                                     select p.name as name,sum(p.count) as count
                                     from PurchasesCount as p
                                     group by p.name
                                         );

select * from PurchasesCount;
select * from PurchasesCountGrouped;

select distinct o.id,o.packageId from Orders_OptionalProducts as opt join Orders as o where opt.orderId=o.id;

drop view if exists OptionalProductsCount;
create view OptionalProductsCount as(
                                    select o.packageId as packageId, count(opt.productId) as optcount
                                    from Orders as o left join Orders_OptionalProducts as opt
                                                               on o.id=opt.orderId
                                    group by o.id
                                        );
select * from OptionalProductsCount;

drop view if exists OptionalProductsAverage;
create view OptionalProductsAverage as(
                                      select p.name as name,avg(opc.optcount) as avg
                                      from OptionalProductsCount as opc join Packages as p
                                      where opc.packageId=p.id
                                      group by id
                                          );
select * from OptionalProductsAverage;


drop view if exists ValueOfTotalSales;
create view ValueOfTotalSales as(
                                select p.name as name,sum(o.totalPayment) as totalPayment
                                from Orders as o join Packages as p
                                where p.id=o.packageId
                                group by o.packageId
                                    );
select * from ValueOfTotalSales;


drop view if exists OptionalProductsSales;
create view OptionalProductsSales as(
                                    select p.name as name,sum(op.monthlyFee*r.monthValidity) as totalOptionalProductsSales
                                    from Orders_OptionalProducts as orderop join Orders as o join OptionalProducts as op join Packages as p join Rate_costs as r
                                    where o.id=orderop.orderId and orderop.productId=op.id and o.packageId=p.id and o.rateId=r.id
                                    group by p.id
                                        );
select * from OptionalProductsSales;

drop view if exists ValueOfSalesDetailed ;
create view ValueOfSalesDetailed as(
                                   select vot.name as name,vot.totalPayment as totalPayment, (vot.totalPayment-ops.totalOptionalProductsSales) as totalPaymentWithoutOP
                                   from ValueOfTotalSales as vot left join OptionalProductsSales as ops
                                                                           on vot.name=ops.name
                                       );

select * from ValueOfSalesDetailed;


drop view if exists InsolventReport;
create view InsolventReport as(
                              select u.id as id,u.username as username,u.email as email,(select max(fp1.faildate) from FailedPayments as fp1 where fp1.userId=u.id) as lastdate,(select o.totalPayment from Orders as o join FailedPayments as fp2 where o.id=fp2.orderId and lastdate=fp2.faildate) as amount
                              from FailedPayments as fp join Users as u
                              where fp.userId=u.id
                              group by u.id
                              having count(*)>=3
                                  );
select * from InsolventReport;