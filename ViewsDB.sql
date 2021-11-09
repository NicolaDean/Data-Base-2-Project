
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
drop view if exists OptionalProductsAverage;
drop view if exists OptionalProductsCount;

create view OptionalProductsCount as(
                                    select p.name as name,p.id as id,count(*) as optcount
                                    from Orders_OptionalProducts as opt join Orders as o join Packages as p
                                    where opt.orderId=o.id and p.id=o.packageId
                                    group by o.packageId,o.id
                                        );

create view OptionalProductsAverage as(
                                      select name,avg(OptionalProductsCount.optcount) as avg
                                      from OptionalProductsCount
                                      group by id
                                          );

select * from OptionalProductsAverage;

drop view if exists ValueOfTotalSales;

create view ValueOfTotalSales as(
                                select p.name,sum(o.totalPayment)
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
                                   from ValueOfTotalSales as vot join OptionalProductsSales as ops
                                       );

select * from ValueOfSalesDetailed;
