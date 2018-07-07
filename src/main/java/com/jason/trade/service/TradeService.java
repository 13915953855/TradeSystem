package com.jason.trade.service;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
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

    @Transactional
    public ContractBaseInfo saveContract(ContractBaseInfo contractBaseInfo, String cargoId){
        contractBaseInfo.setStatus(GlobalConst.ENABLE);
        if(StringUtils.isNotBlank(contractBaseInfo.getContainerNo())){
            contractBaseInfo.setStatus(GlobalConst.SHIPPED);
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getETA())){
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

        cargoRepository.updateSaleInfo(cargoId,expectSaleWeight,expectSaleBoxes,realSaleWeight,realSaleBoxes);

        SaleInfo data = saleRepository.save(saleInfo);
        return data;
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

    public JSONObject queryContractList(ContractParam contractParam, int limit, int offset){
        /**root ：我们要查询的类型
         * query：添加查询条件
         * cb: 构建条件
         * specification为一个匿名内部类
         */
        Specification<ContractBaseInfo> specification = new Specification<ContractBaseInfo>() {
            @Override
            public Predicate toPredicate(Root<ContractBaseInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(StringUtils.isNotBlank(contractParam.getStatus())){
                    String status = contractParam.getStatus();
                    switch (status){//0-作废，1-已下单，2-已装船，3-已到港，4-已入库
                        case "已下单":predicates.add(cb.equal(root.get("status"),GlobalConst.ENABLE));break;
                        case "已装船":predicates.add(cb.equal(root.get("status"),GlobalConst.SHIPPED));break;
                        case "已到港":predicates.add(cb.equal(root.get("status"),GlobalConst.ARRIVED));break;
                        case "已入库":predicates.add(cb.equal(root.get("status"),GlobalConst.STORED));break;
                    }
                }
                predicates.add(cb.notEqual(root.get("status"),GlobalConst.DISABLE));
                if(StringUtils.isNotBlank(contractParam.getExternalContract())){
                    /** cb.equal（）相当于判断后面两个参数是否一致
                     *root相当于我们的实体类的一个路径，使用get可以获取到我们的字段，因为我的cityid为Long类型
                     * 所以是as(Long.class)
                     *如果为Int,就是as(Integer.class) 第二个参数为前台传过来的参数，这句话就相当于
                     * 数据库字段的值cityid = 前台传过来的值schoolParam.getCityId()
                     */
                    predicates.add(cb.like(root.get("externalContract"),contractParam.getExternalContract()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getInsideContract())){
                    predicates.add(cb.like(root.get("insideContract"),contractParam.getInsideContract()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getBusinessMode())){
                    predicates.add(cb.equal(root.get("businessMode"),contractParam.getBusinessMode()));
                }
                if(StringUtils.isNotBlank(contractParam.getExternalCompany())){
                    predicates.add(cb.equal(root.get("externalCompany"),contractParam.getExternalCompany()));
                }
                if(StringUtils.isNotBlank(contractParam.getDestinationPort())){
                    predicates.add(cb.equal(root.get("destinationPort"),contractParam.getDestinationPort()));
                }
                if(StringUtils.isNotBlank(contractParam.getAgent())){
                    predicates.add(cb.like(root.get("agent"),contractParam.getAgent()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getContainerNo())){
                    predicates.add(cb.like(root.get("containerNo"),contractParam.getContainerNo()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getCompanyNo())){
                    predicates.add(cb.like(root.get("companyNo"),contractParam.getCompanyNo()+"%"));
                }
                if(StringUtils.isNotBlank(contractParam.getLadingbillNo())){
                    predicates.add(cb.like(root.get("ladingbillNo"),contractParam.getLadingbillNo()+"%"));
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
        JSONObject result = new JSONObject();
        Pageable pageable = new PageRequest(offset/limit, limit);
        Page<ContractBaseInfo> pages = contractRepository.findAll(specification,pageable);
        Iterator<ContractBaseInfo> it = pages.iterator();
        List<ContractBaseInfo> list = new ArrayList<>();
        while(it.hasNext()){
            list.add(it.next());
        }
        result.put("total",pages.getTotalElements());
        result.put("rows",list);
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
                return query.where(predicates.toArray(pre)).getRestriction();
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

    public XSSFWorkbook writeExcel(List<ContractBaseInfo> data) {
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建头
        writeExcelHeadZiYing(workBook,sheet);
        int saleStart = 1;
        int saleEnd = 1;
        for (int i = 0,len = data.size(); i < len; i++) {
            saleStart = saleEnd;
            ContractBaseInfo baseInfo = data.get(i);
            List<String> contractList = convertContractList(baseInfo,i);
            //获取单个合同的多个商品
            List<CargoInfo> cargoData = cargoRepository.findByContractIdAndStatus(baseInfo.getContractId(),GlobalConst.ENABLE);
            if(cargoData.size()>0) {
                //获取单个商品的多个销售记录
                for (int j = 0; j < cargoData.size(); j++) {
                    CargoInfo cargoInfo = cargoData.get(j);
                    List<String> cargoList = convertCargoList(cargoInfo);
                    List<SaleInfo> saleData = saleRepository.findByCargoIdAndStatus(cargoInfo.getCargoId(), GlobalConst.ENABLE);
                    if (saleData.size() > 0) {
                        List<List<String>> saleList = convertCargoSaleList(saleData);
                        int start = saleEnd;
                        //写入销售数据
                        for (List<String> saleRowData : saleList) {
                            //创建行
                            XSSFRow row = sheet.createRow(saleEnd++);
                            //创建单元格
                            XSSFCell cell = null;
                            //写入多条销售记录
                            for (int k = 0; k < GlobalConst.HEAD_SALE_ARRAY.length; k++) {
                                cell = row.createCell(GlobalConst.HEAD_CONTRACT_ARRAY.length + GlobalConst.HEAD_CARGO_ARRAY.length + k, CellType.STRING);//从销售的单元格开始写，下标是65
                                cell.setCellValue(saleRowData.get(k));
                            }
                            for (int k = 0; k < GlobalConst.HEAD_CARGO_ARRAY.length; k++) {
                                //重复写入单条商品信息
                                cell = row.createCell(GlobalConst.HEAD_CONTRACT_ARRAY.length + k, CellType.STRING);//从商品的单元格开始写，下标是50
                                cell.setCellValue(cargoList.get(k));
                            }
                        }
                        //合并单个商品的单元行
                        if(saleEnd-1 > saleStart) {
                            mergeCargoRow(sheet, start, saleEnd-1);
                        }
                    } else {
                        //创建行
                        XSSFRow row = sheet.createRow(saleEnd++);
                        //创建单元格
                        XSSFCell cell = null;
                        for (int k = 0; k < GlobalConst.HEAD_CARGO_ARRAY.length; k++) {
                            //重复写入单条商品信息
                            cell = row.createCell(GlobalConst.HEAD_CONTRACT_ARRAY.length + k, CellType.STRING);//从商品的单元格开始写，下标是50
                            cell.setCellValue(cargoList.get(k));
                        }
                    }
                }
                //重复写入单个合同信息
                for (int j = saleStart; j < saleEnd; j++) {
                    //创建行
                    XSSFRow row = sheet.getRow(j);
                    //获取单元格
                    XSSFCell cell = null;
                    for (int k = 0; k < GlobalConst.HEAD_CONTRACT_ARRAY.length; k++) {
                        //重复写入单条商品信息
                        cell = row.createCell(k,CellType.STRING);
                        cell.setCellValue(contractList.get(k));
                    }
                }
            }else{
                //创建行
                XSSFRow row = sheet.createRow(saleEnd++);
                //获取单元格
                XSSFCell cell = null;
                for (int k = 0; k < GlobalConst.HEAD_CONTRACT_ARRAY.length; k++) {
                    //重复写入单条商品信息
                    cell = row.createCell(k,CellType.STRING);
                    cell.setCellValue(contractList.get(k));
                }
            }

            //合并单个合同的单元行
            if(saleEnd-1 > saleStart) {
                mergeContractRow(sheet, saleStart, saleEnd - 1);
            }
        }

        for (int i = 0; i < saleEnd; i++) {
            //创建行
            XSSFRow row = sheet.getRow(i);
            //创建单元格
            XSSFCell cell = null;
            int total = GlobalConst.HEAD_CONTRACT_ARRAY.length + GlobalConst.HEAD_CARGO_ARRAY.length + GlobalConst.HEAD_SALE_ARRAY.length;
            for (int k = 0; k < total; k++) {
                cell = row.getCell(k);
                if(cell == null){
                    cell = row.createCell(k);
                    cell.setCellValue("");
                }
                cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
            }
        }

        return workBook;
    }

    private void mergeContractRow(XSSFSheet sheet, int contractMergeStart, int contractMergeEnd) {
        for (int i = 0; i < GlobalConst.HEAD_CONTRACT_ARRAY.length; i++) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(contractMergeStart, contractMergeEnd, i, i);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    private void mergeCargoRow(XSSFSheet sheet, int saleStart, int saleEnd) {
        for (int i = GlobalConst.HEAD_CONTRACT_ARRAY.length; i < GlobalConst.HEAD_CONTRACT_ARRAY.length+GlobalConst.HEAD_CARGO_ARRAY.length; i++) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(saleStart, saleEnd, i, i);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    private List<String> convertContractList(ContractBaseInfo baseInfo,Integer index) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(index+1));//序号
        list.add(baseInfo.getExternalContract());//外合同编号
        list.add(baseInfo.getInsideContract());//内合同编号
        list.add(baseInfo.getContractDate());//合同日期
        list.add(baseInfo.getExternalCompany());//外商
        list.add(baseInfo.getOriginCountry());//原产地
        list.add(baseInfo.getCompanyNo());//厂号
        list.add(baseInfo.getDestinationPort());//目的港
        list.add(baseInfo.getPriceCondition());//价格条件
        list.add(baseInfo.getPayType());//付款方式
        list.add(baseInfo.getCurrency());//币种
        list.add(baseInfo.getExpectSailingDate());//预计船期
        list.add(baseInfo.getBusinessMode());//业务模式
        list.add(baseInfo.getTotalBoxes()+"");//箱数总计
        list.add(baseInfo.getTotalContractAmount()+"");//合同总数量
        list.add(baseInfo.getTotalContractMoney()+"");//合同总金额(元)
        list.add(baseInfo.getTotalInvoiceAmount()+"");//发票总数量
        list.add(baseInfo.getTotalInvoiceMoney()+"");//发票总金额(元)
        list.add(baseInfo.getIssuingBank());//开证行
        list.add(baseInfo.getIssuingDate());//开证日期
        list.add(baseInfo.getLCNo());//LC NO.
        list.add(baseInfo.getBankDaodanDate());//银行到单日
        list.add(baseInfo.getRemittanceDate());//付汇日
        list.add(baseInfo.getYahuidaoqiDate());//押汇到期日
        list.add(baseInfo.getRemittanceRate()+"");//付汇汇率(%)
        list.add(baseInfo.getPrePayment()+"");//付款金额
        list.add(baseInfo.getPrePaymentDate()+"");//付款日期
        list.add(baseInfo.getPreRate()+"");//汇率
        list.add(baseInfo.getFinalPayment()+"");//付款金额
        list.add(baseInfo.getFinalPaymentDate()+"");//付款日期
        list.add(baseInfo.getFinalRate()+"");//汇率
        list.add(baseInfo.getContainerNo());//柜号
        list.add(baseInfo.getLadingbillNo());//提单号
        list.add(baseInfo.getContainerSize());//货柜尺寸
        list.add(baseInfo.getIsNeedInsurance() == 1?"是":"否");//需要购买保险
        list.add(baseInfo.getInsuranceBuyDate());//保险购买日期
        list.add(baseInfo.getInsuranceMoney()+"");//保险费用
        list.add(baseInfo.getInsuranceCompany());//保险公司
        list.add(baseInfo.getETD());//ETD
        list.add(baseInfo.getETA());//ETA
        list.add(baseInfo.getIsCheckElec()==1?"是":"否");//已核对电子版
        list.add(baseInfo.getQACertificate()==1?"是":"否");//检疫证
        list.add(baseInfo.getAgent());//货代
        list.add(baseInfo.getAgentSendDate());//单据寄给货代日期
        list.add(baseInfo.getTariff()+"");//关税
        list.add(baseInfo.getAddedValueTax()+"");//增值税
        list.add(baseInfo.getTaxPayDate());//付税日期
        list.add(baseInfo.getAgentPassDate());//放行日期
        list.add(baseInfo.getWarehouse());//仓库
        list.add(baseInfo.getStoreDate());//入库日期
        list.add(baseInfo.getRemark());//备注
        return list;
    }

    private List<String> convertCargoList(CargoInfo cargoInfo) {
        List<String> list = new ArrayList<>();
        list.add(cargoInfo.getCargoName());//产品名称
        list.add(cargoInfo.getLevel());//级别
        list.add(cargoInfo.getCargoNo());//库号
        list.add(cargoInfo.getUnitPrice()+"");//采购单价(/KG)
        list.add(cargoInfo.getBoxes()+"");//箱数(小计)
        list.add(cargoInfo.getContractAmount()+"");//合同数量(小计)
        list.add(cargoInfo.getContractMoney()+"");//合同金额(小计:元)
        list.add(cargoInfo.getInvoiceAmount()+"");//发票数量(小计)
        list.add(cargoInfo.getInvoiceMoney()+"");//发票金额(小计:元)
        list.add(cargoInfo.getCostPrice()+"");//成本单价(/KG)
        list.add(cargoInfo.getRealStoreMoney()+"");//库存成本
        return list;
    }

    private List<List<String>> convertCargoSaleList(List<SaleInfo> saleData) {
        List<List<String>> result = new ArrayList<>();
        for (SaleInfo saleInfo : saleData) {
            List<String> list = new ArrayList<>();
            list.add(saleInfo.getPickupDate());//提货时间
            list.add(saleInfo.getPickupUser());//销售经理
            list.add(saleInfo.getSaleContractNo());//销售合同编号
            list.add(saleInfo.getCustomerName());//客户名称
            list.add(saleInfo.getExpectSaleUnitPrice()+"");//预售单价(元/kg)
            list.add(saleInfo.getExpectSaleWeight()+"");//预售重量(kg)
            list.add(saleInfo.getExpectSaleMoney()+"");//预售金额(元)
            list.add(saleInfo.getExpectSaleBoxes()+"");//预售箱数
            list.add(saleInfo.getExpectSaleDate()+"");//预出库时间
            list.add(saleInfo.getRealSaleUnitPrice()+"");//实售单价(元/kg)
            list.add(saleInfo.getRealSaleWeight()+"");//实售重量(kg)
            list.add(saleInfo.getRealSaleBoxes()+"");//实售箱数
            list.add(saleInfo.getRealSaleMoney()+"");//实售金额(元)
            list.add(saleInfo.getRealSaleDate());//出库单时间
            list.add(saleInfo.getCustomerPayDate());//客户来款时间
            list.add(saleInfo.getCustomerPayMoney()+"");//客户来款金额(元)
            list.add(saleInfo.getPaymentDiff()+"");//货款差额
            list.add(saleInfo.getMoneyClear()==1?"是":"否");//是否已结清
            list.add(saleInfo.getRemark());//备注
            result.add(list);
        }
        return result;
    }

    private void writeExcelHeadZiYing(XSSFWorkbook workBook,XSSFSheet sheet){
        //合并单元格
        //MergeCell(sheet);
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int i = 0; i < GlobalConst.HEAD_CONTRACT_ARRAY.length; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_CONTRACT_ARRAY[i]);
            cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
        }
        for (int i = 0; i < GlobalConst.HEAD_CARGO_ARRAY.length; i++) {
            cell = row.createCell(i+GlobalConst.HEAD_CONTRACT_ARRAY.length, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_CARGO_ARRAY[i]);
            cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
        }
        for (int i = 0; i < GlobalConst.HEAD_SALE_ARRAY.length; i++) {
            cell = row.createCell(i+GlobalConst.HEAD_CONTRACT_ARRAY.length+GlobalConst.HEAD_CARGO_ARRAY.length, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_SALE_ARRAY[i]);
            cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
        }
    }
}
