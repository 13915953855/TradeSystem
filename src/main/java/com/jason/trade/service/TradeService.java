package com.jason.trade.service;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SaleRepository;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.SetStyleUtils;
import com.jason.trade.util.WebSecurityConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TradeService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private SaleRepository saleRepository;

    @Transactional
    public void saveContract(ContractBaseInfo contractBaseInfo, String cargoId){
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

        contractRepository.save(contractBaseInfo);
        if(StringUtils.isNotBlank(cargoId)) {
            String[] arr = cargoId.split(",");
            List<String> cargoIdList = Arrays.asList(arr);
            cargoRepository.updateStatus(cargoIdList,GlobalConst.ENABLE);
        }
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

    public void writeExcel(List<ContractBaseInfo> data, String fileName) {
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet("自营");
        //创建头
        writeExcelHeadZiYing(workBook,sheet);
        int rowNum = 2;
        for (int i = 0,len = data.size(); i < len; i++) {
            ContractBaseInfo baseInfo = data.get(i);
            List<CargoInfo> cargoData = cargoRepository.findByContractIdAndStatus(baseInfo.getContractId(),GlobalConst.ENABLE);
            //拼接商品详情，组成多条记录
            List<List<String>> list = convertBeanToList(baseInfo,cargoData);
            int start = rowNum;
            for (List<String> rowData : list) {
                //创建行
                XSSFRow row = sheet.createRow(rowNum++);
                //创建单元格
                XSSFCell cell = null;
                for (int j = 0; j < GlobalConst.BODY_COLOR.length - 1; j++) {//-1是因为有个备注
                    cell = row.createCell(j, CellType.STRING);
                    cell.setCellValue(rowData.get(j));
                    cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
                }
            }
            int end = rowNum;
            MergeRow(sheet,start,end - 1);
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(fileName));
            workBook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                workBook.close();//记得关闭工作簿
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeExcelHeadZiYing(XSSFWorkbook workBook,XSSFSheet sheet){
        //合并单元格
        MergeCell(sheet);
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int i = 0; i < GlobalConst.FIRST_HEAD_ARRAY.length; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(GlobalConst.FIRST_HEAD_ARRAY[i]);
            cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
        }
        //创建第二行
        row = sheet.createRow(1);
        //创建单元格
        for (int i = 0; i < GlobalConst.SECOND_HEAD_ARRAY.length; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(GlobalConst.SECOND_HEAD_ARRAY[i]);
            cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
        }
    }

    private void MergeRow(XSSFSheet sheet,Integer start,Integer end) {
        CellRangeAddress cellRangeAddress = null;
        for (int i = 0; i < GlobalConst.NEED_MERGE_CELL.length; i++) {
            cellRangeAddress = new CellRangeAddress(start, end, GlobalConst.NEED_MERGE_CELL[i], GlobalConst.NEED_MERGE_CELL[i]);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }
    /**
     * 合并单元格--头部信息
     * @param sheet
     */
    private void MergeCell(XSSFSheet sheet) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 1, 16);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 17, 20);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 21, 24);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 32, 33);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 34, 38);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 1, 39, 39);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 1, 40, 40);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 41, 43);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 45, 49);
        sheet.addMergedRegion(cellRangeAddress);
        cellRangeAddress = new CellRangeAddress(0, 0, 50, 51);
        sheet.addMergedRegion(cellRangeAddress);
    }

    private List<List<String>> convertBeanToList(ContractBaseInfo baseInfo,List<CargoInfo> cargoData){
        List<List<String>> result = new ArrayList<>();
        /*for (CargoInfo cargoInfo : cargoData) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(cargoInfo.getId()));//序号//
            list.add(baseInfo.getContractDate());//合同日期//
            list.add(cargoInfo.getExternalCompany());//外商//
            list.add(baseInfo.getExternalContract());//外合同//
            list.add(baseInfo.getInsideContract());//内合同//
            list.add(baseInfo.getBusinessMode());//业务模式//
            list.add(baseInfo.getCompanyNo());//厂号//
            list.add(cargoInfo.getCargoNo());//库号//
            list.add(cargoInfo.getCargoName());//产品//
            list.add(baseInfo.getSpecification());//规格//
            list.add(baseInfo.getOriginCountry());//原产地//
            list.add(baseInfo.getPriceCondition());//价格条件//
            list.add(baseInfo.getShipmentPort());//起运港//
            list.add(baseInfo.getDestinationPort());//目的港//
            list.add(String.valueOf(cargoInfo.getAmount()));//数量(kg)//
            list.add(String.valueOf(cargoInfo.getUnitPrice()));//单价(USD)//
            list.add(String.valueOf(cargoInfo.getContractValue()));//合同金额(USD)//
            list.add(String.valueOf(baseInfo.getPrePayment()));//付款金额(USD)//
            list.add(baseInfo.getPrePaymentDate());//付款日期//
            list.add(String.valueOf(baseInfo.getPreRate()));//汇率//
            list.add(String.valueOf(baseInfo.getPrePaymentRMB()));//小计(CNY)//
            list.add(String.valueOf(baseInfo.getFinalPayment()));//付款金额//
            list.add(baseInfo.getFinalPaymentDate());//付款日期//
            list.add(String.valueOf(baseInfo.getFinalRate()));//汇率//
            list.add(String.valueOf(baseInfo.getFinalPaymentRMB()));//小计(CNY)//
            list.add(cargoInfo.getSaleCustomer());//销售客户//
            list.add(String.valueOf(cargoInfo.getUnitPrePayAmount()));//来款金额//
            list.add(cargoInfo.getUnitPrePayDate());//来款日期//
            list.add(String.valueOf(cargoInfo.getUnitFinalPayAmount()));//尾款金额//
            list.add(cargoInfo.getUnitFinalPayDate());//来款日期//
            list.add(String.valueOf(cargoInfo.getInvoiceNumber()));//发票数量(kg)//
            list.add(String.valueOf(cargoInfo.getInvoiceValue()));//发票金额(USD)//
            list.add(baseInfo.getEtd());//ETD//
            list.add(baseInfo.getEta());//ETA//
            list.add(cargoInfo.getElecSendDate());//电子版发送日期//
            list.add(String.valueOf(baseInfo.getIsCheckElec()));//是否已核对电子版//
            list.add(baseInfo.getInsuranceBuyDate());//保险购买日期//
            list.add(baseInfo.getInsuranceSendDate());//寄出日期//
            list.add(baseInfo.getInsuranceSignDate());//签收日期//
            list.add(baseInfo.getContainerNo());//柜号//
            list.add(baseInfo.getLadingbillNo());//提单号//
            list.add(baseInfo.getAgent());//货代//
            list.add(baseInfo.getAgentSendDate());//单据寄给货代日期//
            list.add(baseInfo.getAgentPassDate());//放行日期//
            list.add(baseInfo.getTaxDeductibleParty());//税票抵扣方//
            list.add(String.valueOf(baseInfo.getTariff()));//关税//
            list.add(String.valueOf(baseInfo.getAddedValueTax()));//增值税//
            list.add(String.valueOf(cargoInfo.getHystereticFee()));//滞报费//
            list.add(baseInfo.getTaxPayDate());//付税日期//
            list.add(baseInfo.getTaxSignDate());//税票签收日期//
            list.add(baseInfo.getWarehouse());//仓库//
            list.add(baseInfo.getStoreDate());//入库日期//
            result.add(list);
        }*/
        return result;
    }
}
