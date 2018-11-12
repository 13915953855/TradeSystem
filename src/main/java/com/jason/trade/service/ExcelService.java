package com.jason.trade.service;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.QueryContractInfo;
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
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private SaleRepository saleRepository;

    private int firstSize = 0;
    private int secondSize = 0;
    private int thirdSize = 0;

    public XSSFWorkbook writeCargoExcel(List<QueryContractInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = workBook.createCellStyle();
        styleNoColor.setAlignment(HorizontalAlignment.CENTER);
        styleNoColor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNoColor.setBorderBottom(BorderStyle.THIN);
        styleNoColor.setBorderLeft(BorderStyle.THIN);
        styleNoColor.setBorderRight(BorderStyle.THIN);
        styleNoColor.setBorderTop(BorderStyle.THIN);
        styleNoColor.setWrapText(true);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        List<List<Object>> result = convertQueryCargoList(data);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int j = 0; j < GlobalConst.HEAD_CARGO_QUERY_ARRAY.length; j++) {
            cell = row.createCell(j, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_CARGO_QUERY_ARRAY[j]);
            cell.setCellStyle(styleNoColor);
        }


        for (int i = 0; i < result.size(); i++) {
            //创建行
            row = sheet.createRow(i+1);
            List<Object> rowData = result.get(i);
            for (int k = 0; k < GlobalConst.HEAD_CARGO_QUERY_ARRAY.length; k++) {
                cell = row.createCell(k, CellType.STRING);
                if(rowData.get(k) == null){
                    cell.setCellValue("");
                }else {
                    cell.setCellValue(rowData.get(k).toString());
                }
                cell.setCellStyle(styleNoColor);
            }
        }
        return workBook;
    }

    private List<List<Object>> convertQueryCargoList(List<QueryContractInfo> data) {
        List<List<Object>> result = new ArrayList<>();
        for (QueryContractInfo cargoInfo : data) {
            List<Object> list = new ArrayList<>();
            list.add(cargoInfo.getExternalContract());//外合同编号
            list.add(cargoInfo.getInsideContract());//内合同编号
            list.add(cargoInfo.getExternalCompany());//外商
            list.add(cargoInfo.getOriginCountry());//原产地
            list.add(cargoInfo.getContractDate());//合同日期
            list.add(cargoInfo.getCompanyNo());//厂号
            list.add(cargoInfo.getCargoName());//商品
            list.add(cargoInfo.getLevel());//级别
            list.add(cargoInfo.getUnitPrice());//单价
            list.add(cargoInfo.getContractAmount());//合同重量
            list.add(cargoInfo.getContractMoney());//合同金额
            list.add(cargoInfo.getInvoiceAmount());//发票重量
            list.add(cargoInfo.getInvoiceMoney());//发票金额
            list.add(cargoInfo.getEtd());//ETD
            list.add(cargoInfo.getEta());//ETA
            list.add(cargoInfo.getExpectSailingDate());//预计船期
            result.add(list);
        }
        return result;
    }

    public XSSFWorkbook writeExcel(List<ContractBaseInfo> data, String[] chkArr) {

        firstSize = secondSize = thirdSize = 0;
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = workBook.createCellStyle();
        styleNoColor.setAlignment(HorizontalAlignment.CENTER);
        styleNoColor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNoColor.setBorderBottom(BorderStyle.THIN);
        styleNoColor.setBorderLeft(BorderStyle.THIN);
        styleNoColor.setBorderRight(BorderStyle.THIN);
        styleNoColor.setBorderTop(BorderStyle.THIN);
        styleNoColor.setWrapText(true);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        //创建头
        writeExcelHeadZiYing(sheet,chkArr,styleNoColor);
        int saleStart = 1;
        int saleEnd = 1;
        for (int i = 0,len = data.size(); i < len; i++) {
            saleStart = saleEnd;
            ContractBaseInfo baseInfo = data.get(i);
            List<Object> contractList = convertContractList(baseInfo,i);
            //获取单个合同的多个商品
            List<CargoInfo> cargoData = cargoRepository.findByContractId(baseInfo.getContractId());
            if(cargoData.size()>0) {
                //获取单个商品的多个销售记录
                for (int j = 0; j < cargoData.size(); j++) {
                    CargoInfo cargoInfo = cargoData.get(j);
                    List<Object> cargoList = convertCargoList(cargoInfo);
                    List<SaleInfo> saleData = saleRepository.findByCargoIdAndStatus(cargoInfo.getCargoId(), GlobalConst.ENABLE);
                    if (saleData.size() > 0) {
                        List<List<Object>> saleList = convertCargoSaleList(saleData);
                        int start = saleEnd;
                        //写入销售数据
                        for (List<Object> saleRowData : saleList) {
                            //创建行
                            XSSFRow row = sheet.createRow(saleEnd++);
                            //创建单元格
                            XSSFCell cell = null;
                            //写入多条销售记录
                            int saleSize = 0;
                            for (int k = 0; k < GlobalConst.HEAD_SALE_ARRAY.length; k++) {
                                if(chkArr[GlobalConst.HEAD_CONTRACT_ARRAY.length + GlobalConst.HEAD_CARGO_ARRAY.length + k].equals("1")) {
                                    Object value = saleRowData.get(k);
                                    if (value instanceof String) {
                                        cell = row.createCell(firstSize + secondSize + saleSize++, CellType.STRING);//从销售的单元格开始写，下标是65
                                        cell.setCellValue(String.valueOf(value));
                                        if(String.valueOf(value).matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}")){//日期
                                            //cell.setCellValue(DateUtil.stringToDate(String.valueOf(value)));
                                            cell.setCellStyle(dateCellStyle);
                                        }
                                    } else {
                                        cell = row.createCell(firstSize + secondSize + saleSize++, CellType.NUMERIC);//从销售的单元格开始写，下标是65
                                        if (value instanceof Integer) {
                                            cell.setCellValue((Integer) value);
                                        } else if (value instanceof Float) {
                                            cell.setCellValue((Float) value);
                                        } else {
                                            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                                                cell.setCellValue((Double) value);
                                            } else {
                                                cell.setCellValue(0.0);
                                            }
                                        }
                                    }
                                }
                            }
                            int cargoSize = 0;
                            for (int k = 0; k < GlobalConst.HEAD_CARGO_ARRAY.length; k++) {
                                if(chkArr[GlobalConst.HEAD_CONTRACT_ARRAY.length +k].equals("1")) {
                                    //重复写入单条商品信息
                                    Object value = cargoList.get(k);
                                    if (value instanceof String) {
                                        cell = row.createCell(firstSize + cargoSize++, CellType.STRING);//从商品的单元格开始写，下标是50
                                        cell.setCellValue(String.valueOf(cargoList.get(k)));
                                        if(String.valueOf(value).matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}")){//日期
                                            //cell.setCellValue(DateUtil.stringToDate(String.valueOf(value)));
                                            cell.setCellStyle(dateCellStyle);
                                        }
                                    } else {
                                        cell = row.createCell(firstSize + cargoSize++, CellType.NUMERIC);//从商品的单元格开始写，下标是50
                                        if (value instanceof Integer) {
                                            cell.setCellValue((Integer) value);
                                        } else if (value instanceof Float) {
                                            cell.setCellValue((Float) value);
                                        } else {
                                            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                                                cell.setCellValue((Double) value);
                                            } else {
                                                cell.setCellValue(0.0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //合并单个商品的单元行
                        if(saleEnd-1 > start) {
                            mergeCargoRow(sheet, start, saleEnd-1);
                        }
                    } else {
                        //创建行
                        XSSFRow row = sheet.createRow(saleEnd++);
                        //创建单元格
                        XSSFCell cell = null;
                        int cargoSize = 0;
                        for (int k = 0; k < GlobalConst.HEAD_CARGO_ARRAY.length; k++) {
                            if(chkArr[GlobalConst.HEAD_CONTRACT_ARRAY.length +k].equals("1")) {
                                //重复写入单条商品信息
                                Object value = cargoList.get(k);
                                if (value instanceof String) {
                                    cell = row.createCell(firstSize + cargoSize++, CellType.STRING);//从商品的单元格开始写，下标是50
                                    cell.setCellValue(String.valueOf(value));
                                    if(String.valueOf(value).matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}")){//日期
                                        //cell.setCellValue(DateUtil.stringToDate(String.valueOf(value)));
                                        cell.setCellStyle(dateCellStyle);
                                    }
                                } else {
                                    cell = row.createCell(firstSize + cargoSize++, CellType.NUMERIC);//从商品的单元格开始写，下标是50
                                    if (value instanceof Integer) {
                                        cell.setCellValue((Integer) value);
                                    } else if (value instanceof Float) {
                                        cell.setCellValue((Float) value);
                                    } else {
                                        if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                                            cell.setCellValue((Double) value);
                                        } else {
                                            cell.setCellValue(0.0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //重复写入单个合同信息
                for (int j = saleStart; j < saleEnd; j++) {
                    //创建行
                    XSSFRow row = sheet.getRow(j);
                    //获取单元格
                    XSSFCell cell = null;
                    int contractSize = 0;
                    for (int k = 0; k < GlobalConst.HEAD_CONTRACT_ARRAY.length; k++) {
                        if(chkArr[k].equals("1")) {
                            //重复写入单条合同信息
                            Object value = contractList.get(k);
                            if (value instanceof String) {
                                cell = row.createCell(contractSize++, CellType.STRING);
                                cell.setCellValue(String.valueOf(value));
                                if(String.valueOf(value).matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}")){//日期
                                    //cell.setCellValue(DateUtil.stringToDate(String.valueOf(value)));
                                    cell.setCellStyle(dateCellStyle);
                                }
                            } else {
                                cell = row.createCell(contractSize++, CellType.NUMERIC);
                                if (value instanceof Integer) {
                                    cell.setCellValue((Integer) value);
                                } else if (value instanceof Float) {
                                    cell.setCellValue((Float) value);
                                } else {
                                    if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                                        cell.setCellValue((Double) value);
                                    } else {
                                        cell.setCellValue(0.0);
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                //创建行
                XSSFRow row = sheet.createRow(saleEnd++);
                //获取单元格
                XSSFCell cell = null;
                int contractSize = 0;
                for (int k = 0; k < GlobalConst.HEAD_CONTRACT_ARRAY.length; k++) {
                    if(chkArr[k].equals("1")) {
                        //重复写入单条合同信息
                        Object value = contractList.get(k);
                        if (value instanceof String) {
                            cell = row.createCell(contractSize++, CellType.STRING);
                            cell.setCellValue(String.valueOf(value));
                            if(String.valueOf(value).matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}")){//日期
                                //cell.setCellValue(DateUtil.stringToDate(String.valueOf(value)));
                                cell.setCellStyle(dateCellStyle);
                            }
                        } else {
                            cell = row.createCell(contractSize++, CellType.NUMERIC);
                            if (value instanceof Integer) {
                                cell.setCellValue((Integer) value);
                            } else if (value instanceof Float) {
                                cell.setCellValue((Float) value);
                            } else {
                                if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                                    cell.setCellValue((Double) value);
                                } else {
                                    cell.setCellValue(0.0);
                                }
                            }
                        }
                    }
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
            //int total = GlobalConst.HEAD_CONTRACT_ARRAY.length + GlobalConst.HEAD_CARGO_ARRAY.length + GlobalConst.HEAD_SALE_ARRAY.length;
            int total = firstSize+secondSize+thirdSize;
            for (int k = 0; k < total; k++) {
                cell = row.getCell(k);
                if(cell == null){
                    cell = row.createCell(k);
                    cell.setCellValue("");
                }
                cell.setCellStyle(styleNoColor);
            }
        }
        return workBook;
    }

    private void mergeContractRow(XSSFSheet sheet, int contractMergeStart, int contractMergeEnd) {
        for (int i = 0; i < firstSize; i++) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(contractMergeStart, contractMergeEnd, i, i);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    private void mergeCargoRow(XSSFSheet sheet, int saleStart, int saleEnd) {
        for (int i = firstSize; i < firstSize+secondSize; i++) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(saleStart, saleEnd, i, i);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }

    private List<Object> convertContractList(ContractBaseInfo baseInfo,Integer index) {
        List<Object> list = new ArrayList<>();
        list.add(index+1);//序号
        list.add(baseInfo.getExternalContract());//外合同编号
        list.add(baseInfo.getInsideContract());//内合同编号
        list.add(baseInfo.getContractDate());//合同日期
        list.add(baseInfo.getExternalCompany());//外商
        list.add(baseInfo.getOriginCountry());//原产地
        list.add(baseInfo.getStorageCondition());//储存条件
        list.add(baseInfo.getPriceCondition());//价格条件
        list.add(baseInfo.getDestinationPort());//目的港
        list.add(baseInfo.getCurrency());//币种
        list.add(baseInfo.getPayType());//付款方式
        list.add(baseInfo.getExpectSailingDate());//预计船期
        list.add(baseInfo.getExchangeRate());//汇率
        list.add(baseInfo.getTotalBoxes());//箱数总计
        list.add(baseInfo.getTotalContractAmount());//合同总数量
        list.add(baseInfo.getTotalContractMoney());//合同总金额(元)
        list.add(baseInfo.getTotalInvoiceAmount());//发票总数量
        list.add(baseInfo.getTotalInvoiceMoney());//发票总金额(元)

        list.add(baseInfo.getIssuingBank());//开证行
        list.add(baseInfo.getIssuingDate());//开证日期
        list.add(baseInfo.getLcno());//LC NO.
        list.add(baseInfo.getRemittanceDate());//付汇日
        list.add(baseInfo.getRemittanceRate());//付汇汇率(%)
        list.add(baseInfo.getIsYahui() == 1?"是":"否");//已办理押汇
        list.add(baseInfo.getYahuiMoney());//押汇金额
        list.add(baseInfo.getYahuiYearRate());//押汇年汇率(%)
        list.add(baseInfo.getYahuidaoqiDate());//押汇到期日
        list.add(baseInfo.getYahuiDayRate());//到期日汇率(%)

        list.add(baseInfo.getPrePayment());//预付款金额
        list.add(baseInfo.getPrePaymentDate());//预付款日期
        list.add(baseInfo.getPrePayBank());//预付款银行
        list.add(baseInfo.getFinalPayBank());//尾款银行
        list.add(baseInfo.getPreRate());//预付款汇率
        list.add(baseInfo.getFinalPayment());//尾款金额
        list.add(baseInfo.getFinalPaymentDate());//尾款日期
        list.add(baseInfo.getFinalRate());//尾款汇率
        list.add(baseInfo.getIsFinancing() == 1?"是":"否");//已办理融资
        list.add(baseInfo.getFinancingBank());//融资银行
        list.add(baseInfo.getFinancingMoney());//融资金额
        list.add(baseInfo.getFinancingRate());//融资年利率
        list.add(baseInfo.getFinancingDaoqi());//融资到期日
        list.add(baseInfo.getDaoqiRate());//到期日汇率
        list.add(baseInfo.getContainerNo());//柜号
        list.add(baseInfo.getLadingbillNo());//提单号
        list.add(baseInfo.getContainerSize());//货柜尺寸
        list.add(baseInfo.getIsNeedInsurance() == 1?"是":"否");//需要购买保险
        list.add(baseInfo.getInsuranceCompany());//保险公司
        list.add(baseInfo.getInsuranceBuyDate());//保险购买日期
        list.add(baseInfo.getInsuranceMoney());//保险费用
        list.add(baseInfo.getEtd());//ETD
        list.add(baseInfo.getEta());//ETA
        list.add(baseInfo.getIsCheckElec()==1?"是":"否");//已核对电子版
        list.add(baseInfo.getQacertificate()==1?"是":"否");//已出检疫证
        list.add(baseInfo.getHasbaoguan()==1?"是":"否");//已出报关单
        list.add(baseInfo.getAgent());//货代
        list.add(baseInfo.getAgentSendDate());//单据寄给货代日期
        list.add(baseInfo.getTariff());//关税
        list.add(baseInfo.getAddedValueTax());//增值税
        list.add(baseInfo.getTaxPayDate());//付税日期
        list.add(baseInfo.getTariffNo());//报关单号
        list.add(baseInfo.getAgentPassDate());//放行日期
        list.add(baseInfo.getWarehouse());//仓库
        list.add(baseInfo.getStoreDate());//入库日期
        list.add(baseInfo.getZhixiangfei());//滞箱费
        list.add(baseInfo.getZhigangfei());//滞港费
        list.add(baseInfo.getRemark());//备注
        return list;
    }

    private List<Object> convertCargoList(CargoInfo cargoInfo) {
        List<Object> list = new ArrayList<>();
        list.add(cargoInfo.getCargoName());//产品名称
        list.add(cargoInfo.getLevel());//级别
        list.add(cargoInfo.getCargoNo());//库号
        list.add(cargoInfo.getBusinessMode());//业务模式
        list.add(cargoInfo.getCompanyNo());//厂号
        list.add(cargoInfo.getUnitPrice());//采购单价(/KG)
        list.add(cargoInfo.getBoxes());//箱数(小计)
        list.add(cargoInfo.getContractAmount());//合同数量(小计)
        list.add(cargoInfo.getContractMoney());//合同金额(小计:元)
        list.add(cargoInfo.getInvoiceAmount());//发票数量(小计)
        list.add(cargoInfo.getInvoiceMoney());//发票金额(小计:元)
        list.add(cargoInfo.getCostPrice());//成本单价(/KG)
        list.add(cargoInfo.getRealStoreMoney());//库存成本
        list.add(cargoInfo.getRealStoreWeight());//当前库存重量
        list.add(cargoInfo.getRealStoreBoxes());//当前库存箱数
        return list;
    }

    private List<List<Object>> convertCargoSaleList(List<SaleInfo> saleData) {
        List<List<Object>> result = new ArrayList<>();
        for (SaleInfo saleInfo : saleData) {
            List<Object> list = new ArrayList<>();
            list.add(saleInfo.getPickupDate());//提货时间
            list.add(saleInfo.getPickupUser());//销售经理
            list.add(saleInfo.getSaleContractNo());//销售合同编号
            list.add(saleInfo.getCustomerName());//客户名称
            list.add(saleInfo.getExpectSaleUnitPrice());//预售单价(元/kg)
            list.add(saleInfo.getExpectSaleWeight());//预售重量(kg)
            list.add(saleInfo.getExpectSaleMoney());//预售金额(元)
            list.add(saleInfo.getExpectSaleBoxes());//预售箱数
            list.add(saleInfo.getExpectSaleDate());//预出库时间
            list.add(saleInfo.getRealSaleUnitPrice());//实售单价(元/kg)
            list.add(saleInfo.getRealSaleWeight());//实售重量(kg)
            list.add(saleInfo.getRealSaleBoxes());//实售箱数
            list.add(saleInfo.getRealSaleMoney());//实售金额(元)
            list.add(saleInfo.getRealSaleDate());//出库单时间
            list.add(saleInfo.getDepositDate());//定金时间
            list.add(saleInfo.getDeposit());//定金(元)
            list.add(saleInfo.getCustomerPayDate());//客户来款时间
            Double totalPayMoney = 0.0;
            if(saleInfo.getCustomerPayMoney() != null) totalPayMoney += saleInfo.getCustomerPayMoney();
            if(saleInfo.getCustomerPayMoney2() != null) totalPayMoney += saleInfo.getCustomerPayMoney2();
            if(saleInfo.getCustomerPayMoney3() != null) totalPayMoney += saleInfo.getCustomerPayMoney3();
            if(saleInfo.getCustomerPayMoney4() != null) totalPayMoney += saleInfo.getCustomerPayMoney4();
            if(saleInfo.getCustomerPayMoney5() != null) totalPayMoney += saleInfo.getCustomerPayMoney5();
            list.add(totalPayMoney);//客户来款金额(元)
            list.add(saleInfo.getPaymentDiff());//货款差额
            list.add(saleInfo.getMoneyClear()==1?"是":"否");//是否已结清
            list.add(saleInfo.getRemark());//备注
            result.add(list);
        }
        return result;
    }

    private void writeExcelHeadZiYing(XSSFSheet sheet,String[] chkArr,XSSFCellStyle styleNoColor){
        //合并单元格
        //MergeCell(sheet);
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int i = 0; i < GlobalConst.HEAD_CONTRACT_ARRAY.length; i++) {
            if(chkArr[i].equals("1")) {
                cell = row.createCell(firstSize++, CellType.STRING);
                cell.setCellValue(GlobalConst.HEAD_CONTRACT_ARRAY[i]);
                cell.setCellStyle(styleNoColor);
            }
        }
        for (int i = 0; i < GlobalConst.HEAD_CARGO_ARRAY.length; i++) {
            if(chkArr[i + GlobalConst.HEAD_CONTRACT_ARRAY.length].equals("1")) {
                cell = row.createCell(firstSize + secondSize++, CellType.STRING);
                cell.setCellValue(GlobalConst.HEAD_CARGO_ARRAY[i]);
                cell.setCellStyle(styleNoColor);
            }
        }
        for (int i = 0; i < GlobalConst.HEAD_SALE_ARRAY.length; i++) {
            if(chkArr[i + GlobalConst.HEAD_CONTRACT_ARRAY.length + GlobalConst.HEAD_CARGO_ARRAY.length].equals("1")) {
                cell = row.createCell( firstSize + secondSize + thirdSize++, CellType.STRING);
                cell.setCellValue(GlobalConst.HEAD_SALE_ARRAY[i]);
                cell.setCellStyle(styleNoColor);
            }
        }
    }
}
