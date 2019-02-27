update contract_base_info set status = 5 where contract_id in (
select contract_id from(
select a.contract_id,a.external_contract,a.status,AVG(b.status) as bdd from contract_base_info a 
inner join cargo_info b on a.contract_id = b.contract_id
where a.status != 5 group by b.contract_id) t
where bdd = 5
);