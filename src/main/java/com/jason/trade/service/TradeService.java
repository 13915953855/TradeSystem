package com.jason.trade.service;

import com.github.pagehelper.PageHelper;
import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.mapper.InternalContractInfoMapper;
import com.jason.trade.mapper.SaleInfoMapper;
import com.jason.trade.model.CargoInfo;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.InternalContractInfo;
import com.jason.trade.model.SaleInfo;
import com.jason.trade.repository.*;
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
    private InternalContractRepository internalContractRepository;
    @Autowired
    private InternalCargoRepository internalCargoRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CargoInfoMapper cargoInfoMapper;
    @Autowired
    private SaleInfoMapper saleInfoMapper;
    @Autowired
    private ContractBaseInfoMapper contractBaseInfoMapper;
    @Autowired
    private InternalContractInfoMapper internalContractInfoMapper;

    @Transactional
    public ContractBaseInfo saveContract(ContractBaseInfo contractBaseInfo, String cargoId){
        contractBaseInfo.setStatus(GlobalConst.ENABLE);
        /*if(StringUtils.isNotBlank(contractBaseInfo.getContainerNo())){
            contractBaseInfo.setStatus(GlobalConst.SHIPPED);
        }*/
        if(StringUtils.isNotBlank(contractBaseInfo.getEtd())){
            if(contractBaseInfo.getEtd().compareTo(DateUtil.DateToString(new Date())) <= 0){
                contractBaseInfo.setStatus(GlobalConst.SHIPPED);
            }
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getEta())){
            if(contractBaseInfo.getEta().compareTo(DateUtil.DateToString(new Date())) <= 0){
                contractBaseInfo.setStatus(GlobalConst.ARRIVED);
            }
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getStoreDate())){
            if(contractBaseInfo.getStoreDate().compareTo(DateUtil.DateToString(new Date())) <= 0){
                contractBaseInfo.setStatus(GlobalConst.STORED);
                //对应商品的状态也设为已入库
                cargoInfoMapper.storeByContractId(contractBaseInfo.getContractId());
            }
        }

        ContractBaseInfo record = contractRepository.save(contractBaseInfo);
        return record;
    }
    @Transactional
    public InternalContractInfo saveInternalContract(InternalContractInfo contractBaseInfo, String cargoId){
        contractBaseInfo.setStatus(GlobalConst.ENABLE);

        if(StringUtils.isNotBlank(contractBaseInfo.getStoreDate())){
            if(contractBaseInfo.getStoreDate().compareTo(DateUtil.DateToString(new Date())) <= 0){
                contractBaseInfo.setStatus(GlobalConst.STORED);
                //对应商品的状态也设为已入库
                cargoInfoMapper.storeByContractId(contractBaseInfo.getContractId());
            }
        }

        InternalContractInfo record = internalContractRepository.save(contractBaseInfo);
        if(StringUtils.isNotBlank(cargoId)) {
            String[] arr = cargoId.split(",");
            List<String> cargoIdList = Arrays.asList(arr);
            internalCargoRepository.updateStatus(cargoIdList,GlobalConst.ENABLE);
        }
        return record;
    }
    @Transactional
    public SaleInfo saveSale(SaleInfo saleInfo){
        //商品的库存进行减操作
        String cargoId = saleInfo.getCargoId();
        CargoInfo cargoInfo = cargoInfoMapper.selectByCargoId(cargoId);
        //double expectSaleWeight = 0;
        //Integer expectSaleBoxes = 0;
        double realSaleWeight =0;
        Integer realSaleBoxes = 0;
        if(saleInfo.getSaleId() != null){
            SaleInfo saleInfoOld = saleRepository.findOne(saleInfo.getSaleId());
            //expectSaleWeight = cargoInfo.getExpectStoreWeight() - saleInfo.getExpectSaleWeight() + saleInfoOld.getExpectSaleWeight();
            //expectSaleBoxes = cargoInfo.getExpectStoreBoxes() - saleInfo.getExpectSaleBoxes() + saleInfoOld.getExpectSaleBoxes();
            realSaleWeight = cargoInfo.getRealStoreWeight() - saleInfo.getRealSaleWeight() + saleInfoOld.getRealSaleWeight();
            realSaleBoxes = cargoInfo.getRealStoreBoxes() - saleInfo.getRealSaleBoxes() + saleInfoOld.getRealSaleBoxes();
        }else {
            //expectSaleWeight = cargoInfo.getExpectStoreWeight() - saleInfo.getExpectSaleWeight();
            //expectSaleBoxes = cargoInfo.getExpectStoreBoxes() - saleInfo.getExpectSaleBoxes();
            realSaleWeight = cargoInfo.getRealStoreWeight() - saleInfo.getRealSaleWeight();
            realSaleBoxes = cargoInfo.getRealStoreBoxes() - saleInfo.getRealSaleBoxes();
        }
        String status = GlobalConst.ENABLE;
        if(realSaleBoxes <= 0){
            status = GlobalConst.SELLOUT;
        }
        cargoInfo.setStatus(status);
        //cargoInfo.setExpectStoreWeight(expectSaleWeight);
        //cargoInfo.setExpectStoreBoxes(expectSaleBoxes);
        cargoInfo.setRealStoreWeight(realSaleWeight);
        cargoInfo.setRealStoreBoxes(realSaleBoxes);
        cargoInfo.setRealStoreMoney(realSaleWeight*cargoInfo.getCostPrice());
        cargoInfoMapper.updateByCargoId(cargoInfo);
        if(realSaleBoxes <= 0) {
            autoUpdateStatus(saleInfo.getCargoId(), status);
        }
        SaleInfo data = saleRepository.save(saleInfo);
        return data;
    }

    public void autoUpdateStatus(String cargoId,String status){
        //商品售完，合同的状态同步变更
        CargoInfo cargoInfo = cargoInfoMapper.selectByCargoId(cargoId);
        String contractId = cargoInfo.getContractId();
        //查询该合同对应的所有商品的库存。
        CargoParam cargoParam = new CargoParam();
        cargoParam.setContractId(contractId);
        Integer totalRealStoreBoxes = 0;
        if(contractId.startsWith("in_")){
            totalRealStoreBoxes = cargoInfoMapper.getInternalTotalStoreBoxesByExample(cargoParam);
        }else{
            totalRealStoreBoxes = cargoInfoMapper.getTotalStoreBoxesByExample(cargoParam);
        }
        if(totalRealStoreBoxes == null || totalRealStoreBoxes <= 0) {
            List<String> id = new ArrayList<>();
            id.add(contractId);
            List<CargoInfo> cargoList = cargoRepository.findByContractId(cargoInfo.getContractId());
            if(status.equals(GlobalConst.SELLOUT)) {
                int flag = 0;
                for (CargoInfo cargo : cargoList) {
                    if (cargo.getRealStoreBoxes() > 0) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    if(contractId.startsWith("in_")){
                        internalContractRepository.updateStatusByContractId(id,status);
                    }else {
                        contractRepository.updateStatusByContractId(id, status);
                    }
                }
            }else {
                if(contractId.startsWith("in_")){
                    internalContractRepository.updateStatusByContractId(id,status);
                }else {
                    contractRepository.updateStatusByContractId(id, status);
                }
            }
        }
    }

    @Transactional
    public void deleteContract(String ids){
        if(StringUtils.isNotBlank(ids)) {
            String[] arr = ids.split(",");
            List<String> idList = Arrays.asList(arr);
            saleInfoMapper.deleteByContract(idList);
            cargoInfoMapper.deleteByContract(idList);
            contractRepository.deleteContract(idList);
        }
    }
    @Transactional
    public void deleteInternalContract(String ids){
        if(StringUtils.isNotBlank(ids)) {
            String[] arr = ids.split(",");
            List<String> idList = Arrays.asList(arr);
            saleInfoMapper.deleteByContract(idList);
            cargoInfoMapper.deleteByContract(idList);
            internalContractRepository.deleteContract(idList);
        }
    }
    @Transactional
    public void deleteCargo(String cargoId){
        if(StringUtils.isNotBlank(cargoId)) {
            String[] arr = cargoId.split(",");
            List<String> cargoIdList = Arrays.asList(arr);
            cargoRepository.deleteCargo(cargoIdList);
        }
    }

    @Transactional
    public void deleteSaleInfo(String saleId){
        if(StringUtils.isNotBlank(saleId)) {
            String[] arr = saleId.split(",");
            List<String> saleIdList = Arrays.asList(arr);

            //double expectSaleWeight = 0;
            //Integer expectSaleBoxes = 0;
            double realSaleWeight = 0;
            Integer realSaleBoxes = 0;
            String cargoId = "";
            for(String id:saleIdList){
                SaleInfo saleInfo = saleRepository.findOne(Integer.valueOf(id));
                cargoId = saleInfo.getCargoId();
                //expectSaleWeight += saleInfo.getExpectSaleWeight();
                //expectSaleBoxes += saleInfo.getExpectSaleBoxes();
                realSaleWeight += saleInfo.getRealSaleWeight();
                realSaleBoxes += saleInfo.getRealSaleBoxes();
            }
            CargoInfo cargoInfo = cargoInfoMapper.selectByCargoId(cargoId);
            //cargoInfo.setExpectStoreWeight(cargoInfo.getExpectStoreWeight() + expectSaleWeight);
            //cargoInfo.setExpectStoreBoxes(cargoInfo.getExpectStoreBoxes() + expectSaleBoxes);
            cargoInfo.setRealStoreWeight(cargoInfo.getRealStoreWeight() + realSaleWeight);
            cargoInfo.setRealStoreBoxes(cargoInfo.getRealStoreBoxes() + realSaleBoxes);
            if((cargoInfo.getRealStoreBoxes() + realSaleBoxes) > 0){
                cargoInfo.setStatus(GlobalConst.STORED);
            }
            cargoRepository.save(cargoInfo);

            saleRepository.deleteSaleInfo(saleIdList);
        }
    }

    public JSONObject queryContractListByMapper(ContractParam contractParam){
        String status = contractParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        contractParam.setStatus(status);
        Integer count = contractBaseInfoMapper.selectCountByExample(contractParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",getContractBaseInfoList(contractParam));
        return result;
    }

    public JSONObject queryCargoListForQuery(ContractParam contractParam){
        Integer count = contractBaseInfoMapper.countCargoList(contractParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",contractBaseInfoMapper.queryCargoList(contractParam));
        return result;
    }

    public JSONObject queryStoreInfoList(ContractParam contractParam){
        String status = contractParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        contractParam.setStatus(status);
        Integer count = contractBaseInfoMapper.countStoreInfoListByExample(contractParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",contractBaseInfoMapper.queryStoreInfoListByExample(contractParam));
        return result;
    }

    public JSONObject queryCargoSellInfo(CargoParam cargoParam){
        String status = cargoParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        cargoParam.setStatus(status);
        Integer count = cargoInfoMapper.countSellList(cargoParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",cargoInfoMapper.getSellList(cargoParam));
        return result;
    }

    public JSONObject queryInternalContractListByMapper(InternalContractParam contractParam){
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
        Integer count = internalContractInfoMapper.selectCountByExample(contractParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",internalContractInfoMapper.selectByExample(contractParam));
        return result;
    }

    public List<ContractBaseInfo> getContractBaseInfoList(ContractParam contractParam) {
        return contractBaseInfoMapper.selectByExample(contractParam);
    }

    public JSONObject queryAllCargoList(CargoParam cargoParam){
        String status = "";
        switch (cargoParam.getStatus()){
            case "全部":  status = "";break;
            case "已下单": status = "1";break;
            case "已装船": status = "2";break;
            case "已到港": status = "3";break;
            case "已入库": status = "4";break;
            case "已售完": status = "5";break;
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
        /*String status = "";//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        switch (contractParam.getStatus()){
            case "全部":  status = "";break;
            case "已下单": status = "1";break;
            case "已装船": status = "2";break;
            case "已到港": status = "3";break;
            case "已入库": status = "4";break;
            case "已售完": status = "5";break;
            default: break;
        }*/
        String status = contractParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        contractParam.setStatus(status);
        List<ContractTotalInfo> record = contractBaseInfoMapper.getTotalInfo(contractParam);
        if(record != null && record.size() > 0) {
            Float totalCNYContractMoney = 0F;
            Float totalCNYInvoiceMoney = 0F;
            Float totalUSDContractMoney = 0F;
            Float totalUSDInvoiceMoney = 0F;
            Float totalAUDContractMoney = 0F;
            Float totalAUDInvoiceMoney = 0F;
            Float totalContractAmount = 0F;
            Float totalInvoiceAmount = 0F;
            Float totalFinancingMoney = 0F;
            Float totalYahuiMoney = 0F;
            Float totalCNYPrePaymentMoney = 0F;
            Float totalUSDPrePaymentMoney = 0F;
            Float totalAUDPrePaymentMoney = 0F;
            Float totalCNYFinalPaymentMoney = 0F;
            Float totalUSDFinalPaymentMoney = 0F;
            Float totalAUDFinalPaymentMoney = 0F;
            for (ContractTotalInfo totalInfo : record) {
                totalFinancingMoney += totalInfo.getTotalFinancingMoney();
                totalYahuiMoney += totalInfo.getTotalYahuiMoney();
                switch(totalInfo.getCurrency()){
                    case "CNY":
                        totalCNYContractMoney = totalInfo.getTotalContractMoney();
                        totalCNYInvoiceMoney = totalInfo.getTotalInvoiceMoney();
                        totalContractAmount += totalInfo.getTotalContractAmount();
                        totalInvoiceAmount += totalInfo.getTotalInvoiceAmount();
                        totalCNYPrePaymentMoney += totalInfo.getTotalPrePayment();
                        totalCNYFinalPaymentMoney += totalInfo.getTotalFinalPayment();
                        break;
                    case "USD":
                        totalUSDContractMoney = totalInfo.getTotalContractMoney();
                        totalUSDInvoiceMoney = totalInfo.getTotalInvoiceMoney();
                        totalContractAmount += totalInfo.getTotalContractAmount();
                        totalInvoiceAmount += totalInfo.getTotalInvoiceAmount();
                        totalUSDPrePaymentMoney += totalInfo.getTotalPrePayment();
                        totalUSDFinalPaymentMoney += totalInfo.getTotalFinalPayment();
                        break;
                    case "AUD":
                        totalAUDContractMoney = totalInfo.getTotalContractMoney();
                        totalAUDInvoiceMoney = totalInfo.getTotalInvoiceMoney();
                        totalContractAmount += totalInfo.getTotalContractAmount();
                        totalInvoiceAmount += totalInfo.getTotalInvoiceAmount();
                        totalAUDPrePaymentMoney += totalInfo.getTotalPrePayment();
                        totalAUDFinalPaymentMoney += totalInfo.getTotalFinalPayment();
                        break;
                    default:break;
                }
            }
            result.put("totalCNYContractMoney", totalCNYContractMoney);
            result.put("totalCNYInvoiceMoney", totalCNYInvoiceMoney);
            result.put("totalUSDContractMoney", totalUSDContractMoney);
            result.put("totalUSDInvoiceMoney", totalUSDInvoiceMoney);
            result.put("totalAUDContractMoney", totalAUDContractMoney);
            result.put("totalAUDInvoiceMoney", totalAUDInvoiceMoney);
            result.put("totalContractAmount", totalContractAmount);
            result.put("totalInvoiceAmount", totalInvoiceAmount);
            result.put("totalFinancingMoney", totalFinancingMoney);
            result.put("totalYahuiMoney", totalYahuiMoney);
            result.put("totalCNYPrePaymentMoney", totalCNYPrePaymentMoney);
            result.put("totalUSDPrePaymentMoney", totalUSDPrePaymentMoney);
            result.put("totalAUDPrePaymentMoney", totalAUDPrePaymentMoney);
            result.put("totalCNYFinalPaymentMoney", totalCNYFinalPaymentMoney);
            result.put("totalUSDFinalPaymentMoney", totalUSDFinalPaymentMoney);
            result.put("totalAUDFinalPaymentMoney", totalAUDFinalPaymentMoney);
        }else{
            result.put("totalCNYContractMoney", "0");
            result.put("totalCNYInvoiceMoney", "0");
            result.put("totalUSDContractMoney", "0");
            result.put("totalUSDInvoiceMoney", "0");
            result.put("totalAUDContractMoney", "0");
            result.put("totalAUDInvoiceMoney", "0");
            result.put("totalContractAmount", "0");
            result.put("totalInvoiceAmount", "0");
            result.put("totalFinancingMoney", "0");
            result.put("totalYahuiMoney", "0");
            result.put("totalCNYPrePaymentMoney", "0");
            result.put("totalUSDPrePaymentMoney", "0");
            result.put("totalAUDPrePaymentMoney", "0");
            result.put("totalCNYFinalPaymentMoney", "0");
            result.put("totalUSDFinalPaymentMoney", "0");
            result.put("totalAUDFinalPaymentMoney", "0");
        }
        result.put("status","1");
        return result;
    }
    public JSONObject getTotalInfoForQuery(ContractParam contractParam){
        JSONObject result = new JSONObject();

        List<ContractTotalInfo> record = contractBaseInfoMapper.getTotalInfoForQueryContract(contractParam);
        if(record != null && record.size() > 0) {
            Float totalCNYContractMoney = 0F;
            Float totalCNYInvoiceMoney = 0F;
            Float totalUSDContractMoney = 0F;
            Float totalUSDInvoiceMoney = 0F;
            Float totalAUDContractMoney = 0F;
            Float totalAUDInvoiceMoney = 0F;
            Float totalContractAmount = 0F;
            Float totalInvoiceAmount = 0F;
            for (ContractTotalInfo totalInfo : record) {
                switch(totalInfo.getCurrency()){
                    case "CNY":
                        totalCNYContractMoney = totalInfo.getTotalContractMoney();
                        totalCNYInvoiceMoney = totalInfo.getTotalInvoiceMoney();
                        totalContractAmount += totalInfo.getTotalContractAmount();
                        totalInvoiceAmount += totalInfo.getTotalInvoiceAmount();
                        break;
                    case "USD":
                        totalUSDContractMoney = totalInfo.getTotalContractMoney();
                        totalUSDInvoiceMoney = totalInfo.getTotalInvoiceMoney();
                        totalContractAmount += totalInfo.getTotalContractAmount();
                        totalInvoiceAmount += totalInfo.getTotalInvoiceAmount();
                        break;
                    case "AUD":
                        totalAUDContractMoney = totalInfo.getTotalContractMoney();
                        totalAUDInvoiceMoney = totalInfo.getTotalInvoiceMoney();
                        totalContractAmount += totalInfo.getTotalContractAmount();
                        totalInvoiceAmount += totalInfo.getTotalInvoiceAmount();
                        break;
                    default:break;
                }
            }
            result.put("totalCNYContractMoney", totalCNYContractMoney);
            result.put("totalCNYInvoiceMoney", totalCNYInvoiceMoney);
            result.put("totalUSDContractMoney", totalUSDContractMoney);
            result.put("totalUSDInvoiceMoney", totalUSDInvoiceMoney);
            result.put("totalAUDContractMoney", totalAUDContractMoney);
            result.put("totalAUDInvoiceMoney", totalAUDInvoiceMoney);
            result.put("totalContractAmount", totalContractAmount);
            result.put("totalInvoiceAmount", totalInvoiceAmount);

        }else{
            result.put("totalCNYContractMoney", "0");
            result.put("totalCNYInvoiceMoney", "0");
            result.put("totalUSDContractMoney", "0");
            result.put("totalUSDInvoiceMoney", "0");
            result.put("totalAUDContractMoney", "0");
            result.put("totalAUDInvoiceMoney", "0");
            result.put("totalContractAmount", "0");
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

    public JSONObject getTotalStoreInfoForQuery(ContractParam contractParam){
        String status = contractParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        contractParam.setStatus(status);
        JSONObject result = new JSONObject();
        CargoTotalInfo record = contractBaseInfoMapper.getTotalStoreInfoForQuery(contractParam);
        if(record != null) {
            result.put("totalInvoiceMoney", record.getTotalInvoiceMoney());
            result.put("totalInvoiceWeight", record.getTotalInvoiceWeight());
            result.put("totalInvoiceBoxes", record.getTotalInvoiceBoxes());
            result.put("expectStoreWeight", record.getExpectStoreWeight());
            result.put("realStoreWeight", record.getRealStoreWeight());
            result.put("expectStoreBoxes", record.getExpectStoreBoxes());
            result.put("realStoreBoxes", record.getRealStoreBoxes());
            result.put("realStoreMoney", record.getRealStoreMoney());
        }else{
            result.put("totalInvoiceMoney", "0");
            result.put("totalInvoiceWeight", "0");
            result.put("totalInvoiceBoxes", "0");
            result.put("expectStoreWeight", "0");
            result.put("realStoreWeight", "0");
            result.put("expectStoreBoxes", "0");
            result.put("realStoreBoxes", "0");
            result.put("realStoreMoney", "0");
        }
        result.put("status","1");
        return result;
    }

    public JSONObject getTotalStoreOutForQuery(CargoParam cargoParam){
        String status = cargoParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        cargoParam.setStatus(status);
        JSONObject result = new JSONObject();
        CargoTotalInfo record = cargoInfoMapper.getTotalStoreInfoForQuery(cargoParam);
        if(record != null) {
            result.put("totalStoreWeight", record.getRealStoreWeight());
            result.put("totalStoreMoney", record.getRealStoreMoney());
        }else{
            result.put("totalStoreWeight", "0");
            result.put("totalStoreMoney", "0");
        }
        result.put("status","1");
        return result;
    }

    public JSONObject queryCargoStoreInfo(CargoParam cargoParam){
        String status = cargoParam.getStatus();//0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if(status.indexOf("全部") >= 0){
            status = "";
        }else{
            status = status.replaceAll("已下单","1");
            status = status.replaceAll("已装船","2");
            status = status.replaceAll("已到港","3");
            status = status.replaceAll("已入库","4");
            status = status.replaceAll("已售完","5");
        }
        cargoParam.setStatus(status);
        String baoguandan = cargoParam.getBaoguandan();
        if(baoguandan.equals("是")){
            baoguandan = "1";
        }else if(baoguandan.equals("否")){
            baoguandan = "0";
        }
        cargoParam.setBaoguandan(baoguandan);
        String qacertificate = cargoParam.getQacertificate();
        if(qacertificate.equals("是")){
            qacertificate = "1";
        }else if(qacertificate.equals("否")){
            qacertificate = "0";
        }
        cargoParam.setQacertificate(qacertificate);
        Integer count = cargoInfoMapper.countStoreList(cargoParam);
        JSONObject result = new JSONObject();
        result.put("total",count);
        result.put("rows",cargoInfoMapper.getStoreList(cargoParam));
        return result;
    }
}
