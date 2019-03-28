update cargo_info set expect_store_weight = contract_amount,expect_store_boxes=0;

update cargo_info a
inner join (select sum(expect_sale_weight) as weight,cargo_id from presale_info group by cargo_id) b
on a.cargo_id = b.cargo_id
set a.expect_store_weight = a.contract_amount-b.weight,a.expect_store_boxes=1;
