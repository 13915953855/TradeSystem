update cargo_info b inner join contract_base_info a on a.contract_id = b.contract_id 
set b.status = 4 where a.status = 4 and b.status = 1;

update internal_contract_info set warehouse = '名联纪丰' where warehouse in ('上海名联','名联');

update cargo_info set status = 5 where status = 4 and real_store_boxes = 0;

update contract_base_info set status = 5 where contract_id in (
select t.contract_id from (
select a.contract_id,a.status,a.external_contract,AVG(b.status) as sb from contract_base_info a inner join cargo_info b on a.contract_id = b.contract_id
where a.status = 4 
group by a.contract_id) t
where t.sb = 5
);
