package com.jason.trade.service;

import com.github.pagehelper.PageHelper;
import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.model.CargoInfo;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.SaleInfo;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SaleRepository;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.SetStyleUtils;
import com.jason.trade.util.WebSecurityConfig;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class TradeService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CargoInfoMapper cargoInfoMapper;
    @Autowired
    private ContractBaseInfoMapper contractBaseInfoMapper;

    @Transactional
    public void updateCargoStatus(List<String> cargoIdList){
        cargoRepository.updateStatus(cargoIdList,GlobalConst.ENABLE);
    }
    @Transactional
    public ContractBaseInfo saveContract(ContractBaseInfo contractBaseInfo, String cargoId){
        contractBaseInfo.setStatus(GlobalConst.ENABLE);
        if(StringUtils.isNotBlank(contractBaseInfo.getContainerNo())){
            contractBaseInfo.setStatus(GlobalConst.SHIPPED);
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getEta())){
            contractBaseInfo.setStatus(GlobalConst.ARRIVED);
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getStoreDate())){
            contractBaseInfo.setStatus(GlobalConst.STORED);
        }

        ContractBaseInfo record = contractRepository.save(contractBaseInfo);
        if(StringUtils.isNotBlank(cargoId)) {
            String[] arr = cargoId.split(",");
            List<String> cargoIdList = Arrays.asList(arr);
            cargoRepository.updateStatus(cargoIdList,GlobalConst.ENABLE);
        }
        return record;
    }

    @Transactional
    public SaleInfo saveSale(SaleInfo saleInfo){
        //商品的库存进行减操作
        String cargoId = saleInfo.getCargoId();
        CargoInfo cargoInfo = cargoRepository.findByCargoId(cargoId);
        double expectSaleWeight = 0;
        Integer expectSaleBoxes = 0;
        double realSaleWeight =0;
        Integer realSaleBoxes = 0;
        if(saleInfo.getSaleId() != null){
            SaleInfo saleInfoOld = saleRepository.findOne(saleInfo.getSaleId());
            expectSaleWeight = cargoInfo.getExpectStoreWeight() - saleInfo.getExpectSaleWeight() + saleInfoOld.getExpectSaleWeight();
            expectSaleBoxes = cargoInfo.getExpectStoreBoxes() - saleInfo.getExpectSaleBoxes() + saleInfoOld.getExpectSaleBoxes();
            realSaleWeight = cargoInfo.getRealStoreWeight() - saleInfo.getRealSaleWeight() + saleInfoOld.getRealSaleWeight();
            realSaleBoxes = cargoInfo.getRealStoreBoxes() - saleInfo.getRealSaleBoxes() + saleInfoOld.getRealSaleBoxes();
        }else {
            expectSaleWeight = cargoInfo.getExpectStoreWeight() - saleInfo.getExpectSaleWeight();
            expectSaleBoxes = cargoInfo.getExpectStoreBoxes() - saleInfo.getExpectSaleBoxes();
            realSaleWeight = cargoInfo.getRealStoreWeight() - saleInfo.getRealSaleWeight();
            realSaleBoxes = cargoInfo.getRealStoreBoxes() - saleInfo.getRealSaleBoxes();
        }
        String status = GlobalConst.ENABLE;
        if(realSaleWeight <= 0){
            status = GlobalConst.SELLOUT;
        }
        cargoRepository.updateSaleInfo(cargoId,expectSaleWeight,expectSaleBoxes,realSaleWeight,realSaleBoxes,status);
        autoUpdateStatus(saleInfo.getCargoId(),status);
        SaleInfo data = saleRepository.save(saleInfo);
        return data;
    }

    public void autoUpdateStatus(String cargoId,String status){
        //商品售完，合同的状态同步变更
        CargoInfo cargoInfo = cargoRepository.findByCargoId(cargoId);
        String contractId = cargoInfo.getContractId();
        //查询该合同对应的所有商品的库存。
        CargoParam cargoParam = new CargoParam();
        cargoParam.setContractId(contractId);
        Integer totalRealStoreWeight = cargoInfoMapper.getTotalStoreWeightByExample(cargoParam);
        if(totalRealStoreWeight == null || totalRealStoreWeight <= 0) {
            List<String> id = new ArrayList<>();
            id.add(contractId);
            contractRepository.updateStatusByContractId(id, status);
        }
    }

    @Transactional
    public void updateContractStatus(String ids,String status){
        if(StringUtils.isNotBlank(ids)) {
            String[] arr = ids.split(",");
            List<String> idList = Arrays.asList(arr);
            contractRepository.updateStatus(idList,status);
            cargoRepository.deleteByContract(idList);
            saleRepository.deleteByContract(idList);
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

    @Transactional
    public void updateSaleStatus(String saleId,String status){
        if(StringUtils.isNotBlank(saleId)) {
            String[] arr = saleId.split(",");
            List<String> saleIdList = Arrays.asList(arr);
            saleRepository.updateStatus(saleIdList,status);
            double expectSaleWeight = 0;
            Integer expectSaleBoxes = 0;
            double realSaleWeight = 0;
            Integer realSaleBoxes = 0;
            String cargoId = "";
            for(String id:saleIdList){
                SaleInfo saleInfo = saleRepository.findOne(Integer.valueOf(id));
                cargoId = saleInfo.getCargoId();
                expectSaleWeight += saleInfo.getExpectSaleWeight();
                expectSaleBoxes += saleInfo.getExpectSaleBoxes();
                realSaleWeight += saleInfo.getRealSaleWeight();
                realSaleBoxes += saleInfo.getRealSaleBoxes();
            }
            CargoInfo cargoInfo = cargoRepository.findByCargoId(cargoId);
            cargoInfo.setExpectStoreWeight(cargoInfo.getExpectStoreWeight() + expectSaleWeight);
            cargoInfo.setExpectStoreBoxes(cargoInfo.getExpectStoreBoxes() + expectSaleBoxes);
            cargoInfo.setRealStoreWeight(cargoInfo.getRealStoreWeight() + realSaleWeight);
            cargoInfo.setRealStoreBoxes(cargoInfo.getRealStoreBoxes() + realSaleBoxes);
            cargoRepository.save(cargoInfo);
        }
    }

    public JSONObject queryContractListByMapper(ContractParam contractParam){
        String status = "";//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        switch (contractParam.getStatus()){
            case "全部":  status = "";break;
            case "已下单": status = "1";break;
            case "已装船": status = "2";break;
            case "已到港": status = "3";break;
            case "已入库": status = "4";break;
            case "已售完": status = "5";break;
            default: break;
        }
        contractParam.setStatus(status);
        Integer count = contractBaseInfoMapper.selectCountByExample(contractParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",getContractBaseInfoList(contractParam));
        return result;
    }

    public List<ContractBaseInfo> getContractBaseInfoList(ContractParam contractParam) {
        return contractBaseInfoMapper.selectByExample(contractParam);
    }

    public JSONObject queryAllCargoList(CargoParam cargoParam){
        String status = "";//0-已删除，1-已保存,5-已售完，9-编辑中
        switch (cargoParam.getStatus()){
            case "全部":  status = "";break;
            case "已保存": status = "1";break;
            case "已售完": status = "5";break;
            case "编辑中": status = "9";break;
            case "已删除": status = "0";break;
            default: break;
        }
        cargoParam.setStatus(status);
        List<CargoManageInfo> list = cargoInfoMapper.selectByExample(cargoParam);
        Integer count = cargoInfoMapper.selectCountByExample(cargoParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",list);
        return result;
    }
    public JSONObject getTotalStore(CargoParam cargoParam){
        JSONObject result = new JSONObject();
        result.put("totalStoreWeight",cargoInfoMapper.getTotalStoreWeightByExample(cargoParam));
        result.put("totalStoreBoxes",cargoInfoMapper.getTotalStoreBoxesByExample(cargoParam));
        result.put("status","1");
        return result;
    }
    public JSONObject getTotalInfo(ContractParam contractParam){
        JSONObject result = new JSONObject();
        //处理status
        String status = "";//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        switch (contractParam.getStatus()){
            case "全部":  status = "";break;
            case "已下单": status = "1";break;
            case "已装船": status = "2";break;
            case "已到港": status = "3";break;
            case "已入库": status = "4";break;
            case "已售完": status = "5";break;
            default: break;
        }
        contractParam.setStatus(status);
        ContractTotalInfo record = contractBaseInfoMapper.getTotalInfo(contractParam);
        if(record != null) {
            result.put("totalContractMoney", record.getTotalContractMoney());
            result.put("totalContractAmount", record.getTotalContractAmount());
            result.put("totalInvoiceMoney", record.getTotalInvoiceMoney());
            result.put("totalInvoiceAmount", record.getTotalInvoiceAmount());
        }else{
            result.put("totalContractMoney", "0");
            result.put("totalContractAmount", "0");
            result.put("totalInvoiceMoney", "0");
            result.put("totalInvoiceAmount", "0");
        }
        result.put("status","1");
        return result;
    }

    public JSONObject queryCargoList(CargoParam cargoParam, int limit, int offset){
        /**root ：我们要查询的类型
         * query：添加查询条件
         * cb: 构建条件
         * specification为一个匿名内部类
         */
        Specification<CargoInfo> specification = new Specification<CargoInfo>() {
            @Override
            public Predicate toPredicate(Root<CargoInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(cb.notEqual(root.get("status"),GlobalConst.DISABLE));

                if(StringUtils.isNotBlank(cargoParam.getContractNo())){
                    /** cb.equal（）相当于判断后面两个参数是否一致
                     *root相当于我们的实体类的一个路径，使用get可以获取到我们的字段，因为我的cityid为Long类型
                     * 所以是as(Long.class)
                     *如果为Int,就是as(Integer.class) 第二个参数为前台传过来的参数，这句话就相当于
                     * 数据库字段的值cityid = 前台传过来的值schoolParam.getCityId()
                     */
                    predicates.add(cb.like(root.get("contractNo"),cargoParam.getContractNo()+"%"));
                }
                if(StringUtils.isNotBlank(cargoParam.getInsideContract())){
                    predicates.add(cb.like(root.get("insideContract"),cargoParam.getInsideContract()+"%"));
                }
                if(StringUtils.isNotBlank(cargoParam.getLevel())){
                    predicates.add(cb.equal(root.get("level"),cargoParam.getLevel()));
                }
                if(StringUtils.isNotBlank(cargoParam.getCargoName())){
                    predicates.add(cb.equal(root.get("cargoName"),cargoParam.getCargoName()));
                }
                if(StringUtils.isNotBlank(cargoParam.getCargoNo())){
                    predicates.add(cb.like(root.get("cargoNo"),cargoParam.getCargoNo()+"%"));
                }
                //创建一个条件的集合，长度为上面满足条件的个数
                Predicate[] pre = new Predicate[predicates.size()];
                //这句大概意思就是将上面拼接好的条件返回去
                query.where(predicates.toArray(pre));
                //添加排序的功能
                query.orderBy(cb.desc(root.get("id").as(Integer.class)));
                return query.getRestriction();
            }
        };
        JSONObject result = new JSONObject();
        Pageable pageable = new PageRequest(offset/limit, limit);
        Page<CargoInfo> pages = cargoRepository.findAll(specification,pageable);
        Iterator<CargoInfo> it = pages.iterator();
        List<CargoInfo> list = new ArrayList<>();
        while(it.hasNext()){
            list.add(it.next());
        }
        result.put("total",pages.getTotalElements());
        result.put("rows",list);
        return result;
    }

    //0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
    public void autoUpdateContractStatus(){
        String today = DateUtil.DateToString(new Date());
        ContractParam contractParam = new ContractParam();
        //2-已装船 （填写了ETD，且真实日期到了ETD的日期）
        contractParam.setStatus(GlobalConst.SHIPPED);
        contractParam.setFieldName("etd");
        contractParam.setToday(today);
        contractBaseInfoMapper.updateStatusByField(contractParam);
        //3已到港（填写了ETA，且真实日期到了ETA所填的日期
        contractParam.setStatus(GlobalConst.ARRIVED);
        contractParam.setFieldName("eta");
        contractParam.setToday(today);
        contractBaseInfoMapper.updateStatusByField(contractParam);
        //4已入库 （填写了入库日期，且真实日期到了所填的入库日期）
        contractParam.setStatus(GlobalConst.STORED);
        contractParam.setFieldName("store_date");
        contractParam.setToday(today);
        contractBaseInfoMapper.updateStatusByField(contractParam);
    }
}
