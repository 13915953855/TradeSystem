package com.jason.trade.service;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.model.CargoInfo;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.SaleInfo;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SaleRepository;
import com.jason.trade.util.SetStyleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    public XSSFWorkbook writeExcel(List<ContractBaseInfo> data, String[] chkArr) {
        firstSize = secondSize = thirdSize = 0;
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建头
        writeExcelHeadZiYing(workBook,sheet,chkArr);
        int saleStart = 1;
        int saleEnd = 1;
        for (int i = 0,len = data.size(); i < len; i++) {
            saleStart = saleEnd;
            ContractBaseInfo baseInfo = data.get(i);
            List<Object> contractList = convertContractList(baseInfo,i);
            //获取单个合同的多个商品
            List<CargoInfo> cargoData = cargoRepository.findByContractIdAndStatus(baseInfo.getContractId(), GlobalConst.ENABLE);
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
                cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
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
        list.add(baseInfo.getDestinationPort());//目的港
        list.add(baseInfo.getPriceCondition());//价格条件
        list.add(baseInfo.getPayType());//付款方式
        list.add(baseInfo.getCurrency());//币种
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
        list.add(baseInfo.getBankDaodanDate());//银行到单日
        list.add(baseInfo.getRemittanceDate());//付汇日
        list.add(baseInfo.getYahuidaoqiDate());//押汇到期日
        list.add(baseInfo.getRemittanceRate());//付汇汇率(%)
        list.add(baseInfo.getYahuiRate());//押汇汇率(%)
        list.add(baseInfo.getPrePayment());//付款金额
        list.add(baseInfo.getPrePaymentDate());//付款日期
        list.add(baseInfo.getPreRate());//汇率
        list.add(baseInfo.getFinalPayment());//付款金额
        list.add(baseInfo.getFinalPaymentDate());//付款日期
        list.add(baseInfo.getFinalRate());//汇率
        list.add(baseInfo.getContainerNo());//柜号
        list.add(baseInfo.getLadingbillNo());//提单号
        list.add(baseInfo.getContainerSize());//货柜尺寸
        list.add(baseInfo.getIsNeedInsurance() == 1?"是":"否");//需要购买保险
        list.add(baseInfo.getInsuranceBuyDate());//保险购买日期
        list.add(baseInfo.getInsuranceMoney());//保险费用
        list.add(baseInfo.getInsuranceCompany());//保险公司
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
            list.add(saleInfo.getCustomerPayMoney());//客户来款金额(元)
            list.add(saleInfo.getPaymentDiff());//货款差额
            list.add(saleInfo.getMoneyClear()==1?"是":"否");//是否已结清
            list.add(saleInfo.getRemark());//备注
            result.add(list);
        }
        return result;
    }

    private void writeExcelHeadZiYing(XSSFWorkbook workBook,XSSFSheet sheet,String[] chkArr){
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
                cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
            }
        }
        for (int i = 0; i < GlobalConst.HEAD_CARGO_ARRAY.length; i++) {
            if(chkArr[i + GlobalConst.HEAD_CONTRACT_ARRAY.length].equals("1")) {
                cell = row.createCell(firstSize + secondSize++, CellType.STRING);
                cell.setCellValue(GlobalConst.HEAD_CARGO_ARRAY[i]);
                cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
            }
        }
        for (int i = 0; i < GlobalConst.HEAD_SALE_ARRAY.length; i++) {
            if(chkArr[i + GlobalConst.HEAD_CONTRACT_ARRAY.length + GlobalConst.HEAD_CARGO_ARRAY.length].equals("1")) {
                cell = row.createCell( firstSize + secondSize + thirdSize++, CellType.STRING);
                cell.setCellValue(GlobalConst.HEAD_SALE_ARRAY[i]);
                cell.setCellStyle(SetStyleUtils.setStyleNoColor(workBook));
            }
        }
    }
}
