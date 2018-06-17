package com.jason.trade.service;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.ContractBaseInfo;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TradeService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;

    @Transactional
    public void saveContract(ContractBaseInfo contractBaseInfo, String cargoId){
        contractBaseInfo.setStatus(GlobalConst.ENABLE);
        contractRepository.save(contractBaseInfo);
        if(StringUtils.isNotBlank(cargoId)) {
            String[] arr = cargoId.split(",");
            List<String> cargoIdList = Arrays.asList(arr);
            cargoRepository.updateStatus(cargoIdList,GlobalConst.ENABLE);
        }
    }

    @Transactional
    public void updateCargoStatus(String cargoId,String status){
        if(StringUtils.isNotBlank(cargoId)) {
            String[] arr = cargoId.split(",");
            List<String> cargoIdList = Arrays.asList(arr);
            cargoRepository.updateStatus(cargoIdList,status);
        }
    }

    public List<ContractBaseInfo> queryContractList(ContractParam contractParam){
        /**root ：我们要查询的类型
         * query：添加查询条件
         * cb: 构建条件
         * specification为一个匿名内部类
         */
        Specification<ContractBaseInfo> specification = new Specification<ContractBaseInfo>() {
            @Override
            public Predicate toPredicate(Root<ContractBaseInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(cb.like(root.get("status"),GlobalConst.ENABLE));
                if(StringUtils.isNotBlank(contractParam.getExternalContract())){
                    /** cb.equal（）相当于判断后面两个参数是否一致
                     *root相当于我们的实体类的一个路径，使用get可以获取到我们的字段，因为我的cityid为Long类型
                     * 所以是as(Long.class)
                     *如果为Int,就是as(Integer.class) 第二个参数为前台传过来的参数，这句话就相当于
                     * 数据库字段的值cityid = 前台传过来的值schoolParam.getCityId()
                     */
                    predicates.add(cb.like(root.get("externalContract"),"%"+contractParam.getExternalContract()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getInsideContract())){
                    predicates.add(cb.like(root.get("insideContract"),"%"+contractParam.getInsideContract()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getBusinessMode())){
                    predicates.add(cb.equal(root.get("businessMode"),contractParam.getBusinessMode()));
                }
                if(StringUtils.isNotBlank(contractParam.getExternalCompany())){
                    predicates.add(cb.like(root.get("externalCompany"),contractParam.getExternalCompany()));
                }
                if(StringUtils.isNotBlank(contractParam.getContractStartDate())){
                    predicates.add(cb.greaterThan(root.get("contractDate"),contractParam.getContractStartDate()));
                }
                if(StringUtils.isNotBlank(contractParam.getContractEndDate())){
                    predicates.add(cb.lessThan(root.get("contractDate"),contractParam.getContractEndDate()));
                }
                //创建一个条件的集合，长度为上面满足条件的个数
                Predicate[] pre = new Predicate[predicates.size()];
                //这句大概意思就是将上面拼接好的条件返回去
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
        List<ContractBaseInfo> list= contractRepository.findAll(specification);
        return list;
    }
}
