package com.jason.trade.service;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.CargoSellInfo;
import com.jason.trade.entity.CargoStoreInfo;
import com.jason.trade.entity.QueryContractInfo;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.model.CargoInfo;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.SaleInfo;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SaleRepository;
import com.jason.trade.util.CommonUtil;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.SetStyleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ExcelService {
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private SaleRepository saleRepository;

    private int firstSize = 0;
    private int secondSize = 0;
    private int thirdSize = 0;

    public XSSFWorkbook writeDutyExcel(List<ContractBaseInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        List<List<Object>> result = convertDutyList(data);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int j = 0; j < GlobalConst.HEAD_DUTY_ARRAY.length; j++) {
            cell = row.createCell(j, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_DUTY_ARRAY[j]);
            cell.setCellStyle(styleNoColor);
        }

        for (int i = 0; i < result.size(); i++) {
            //创建行
            row = sheet.createRow(i+1);
            List<Object> rowData = result.get(i);
            for (int k = 0; k < GlobalConst.HEAD_DUTY_ARRAY.length; k++) {
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
    private List<List<Object>> convertDutyList(List<ContractBaseInfo> data) {
        List<List<Object>> result = new ArrayList<>();
        for (ContractBaseInfo dutyInfo : data) {
            List<Object> list = new ArrayList<>();
            list.add(dutyInfo.getExternalContract());// "外合同编号",
            list.add(dutyInfo.getInsideContract());// "内合同编号",
            list.add(dutyInfo.getExternalCompany());//"外商",
            list.add(dutyInfo.getTariffNo());// "报关单号",
            list.add(dutyInfo.getTaxPayDate());// "付税日期",
            list.add(dutyInfo.getTariff());// "关税",
            list.add(dutyInfo.getAddedValueTax());// "增值税",
            list.add(dutyInfo.getAgent());// "货代",
            list.add(dutyInfo.getAgentPassDate());// "放行日期"
            list.add(dutyInfo.getWarehouse());// "仓库",
            list.add(dutyInfo.getStoreDate());// "入库时间",

            result.add(list);
        }
        return result;
    }
    public XSSFWorkbook writeStoreOutExcel(List<CargoSellInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);
        XSSFCellStyle blueColor = createXssfCellStyleWithBlueColor(workBook);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        List<List<Object>> result = convertQueryStoreOutList(data);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int j = 0; j < GlobalConst.HEAD_STOREOUT_QUERY_ARRAY.length; j++) {
            cell = row.createCell(j, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_STOREOUT_QUERY_ARRAY[j]);
            cell.setCellStyle(styleNoColor);
        }

        for (int i = 0; i < result.size(); i++) {
            //创建行
            row = sheet.createRow(i+1);
            List<Object> rowData = result.get(i);
            for (int k = 0; k < GlobalConst.HEAD_STOREOUT_QUERY_ARRAY.length; k++) {
                cell = row.createCell(k, CellType.STRING);
                String val = "";
                if(rowData.get(k) == null){
                    cell.setCellValue("");
                }else {
                    val = rowData.get(k).toString();
                    cell.setCellValue(val);
                }
                cell.setCellStyle(styleNoColor);

                //设置入库时间、ETA\ETD，七天内显示蓝色
                String key = GlobalConst.HEAD_STOREOUT_QUERY_ARRAY[k];
                if(key.equals("入库时间") || key.equals("ETA")) {
                    Date dateBeforeSevenDay = DateUtil.getDateBefore(new Date(), 7);
                    String beforeSevenDay = DateUtil.DateToString(dateBeforeSevenDay);
                    if(val.compareTo(beforeSevenDay) >= 0){
                        cell.setCellStyle(blueColor);
                    }
                }

            }
        }
        return workBook;
    }
    private List<List<Object>> convertQueryStoreOutList(List<CargoSellInfo> data) {
        List<List<Object>> result = new ArrayList<>();
        for (CargoSellInfo storeInfo : data) {
            //"内合同号","外商","库号","厂号","商品","级别",
             //       "仓库","单价","成本单价","库存重量","库存成本","柜号","提单号",
            //       "出库时间","客户属性","客户名称","实售重量","实售箱数","实售单价",
              //      "实售金额","定金","客户来款金额","利润","发票"
            List<Object> list = new ArrayList<>();
            list.add(storeInfo.getInside_contract());// "内合同编号",
            list.add(storeInfo.getExternal_company());//外商
            list.add(storeInfo.getCargo_no());
            list.add(storeInfo.getCompany_no());// "厂号",
            list.add(storeInfo.getCargo_name());//"商品",
            list.add(storeInfo.getLevel());// "级别",
            list.add(storeInfo.getWarehouse());// "仓库",
            list.add(storeInfo.getUnit_price());//单价
            list.add(storeInfo.getCost_price());//成本单价
            list.add(storeInfo.getReal_store_weight());
            list.add(storeInfo.getReal_store_money());
            list.add(storeInfo.getContainer_no());// "柜号",
            list.add(storeInfo.getLadingbill_no());// "提单号",
            list.add(storeInfo.getReal_sale_date());
            list.add(storeInfo.getCustomer_type());
            list.add(storeInfo.getCustomer_name());
            list.add(storeInfo.getReal_sale_weight());
            list.add(storeInfo.getReal_sale_boxes());
            list.add(storeInfo.getReal_sale_unit_price());
            list.add(storeInfo.getReal_sale_money());
            list.add(storeInfo.getDeposit());
            list.add(storeInfo.getCustomer_pay_money());
            list.add(storeInfo.getProfit());
            String a = storeInfo.getKaifapiao();
            if(StringUtils.isNotBlank(a) && a.equals("1")){
                list.add("是");// "是",
            }else{
                list.add("否");// "否",
            }

            result.add(list);
        }
        return result;
    }
    public XSSFWorkbook writeStoreInExcel(List<CargoStoreInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);
        XSSFCellStyle blueColor = createXssfCellStyleWithBlueColor(workBook);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        List<List<Object>> result = convertQueryStoreInList(data);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int j = 0; j < GlobalConst.HEAD_STOREIN_QUERY_ARRAY.length; j++) {
            cell = row.createCell(j, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_STOREIN_QUERY_ARRAY[j]);
            cell.setCellStyle(styleNoColor);
        }

        for (int i = 0; i < result.size(); i++) {
            //创建行
            row = sheet.createRow(i+1);
            List<Object> rowData = result.get(i);
            for (int k = 0; k < GlobalConst.HEAD_STOREIN_QUERY_ARRAY.length; k++) {
                cell = row.createCell(k, CellType.STRING);
                String val = "";
                if(rowData.get(k) == null){
                    cell.setCellValue("");
                }else {
                    val = rowData.get(k).toString();
                    cell.setCellValue(val);
                }
                cell.setCellStyle(styleNoColor);

                //设置入库时间、ETA\ETD，七天内显示蓝色
                String key = GlobalConst.HEAD_STOREIN_QUERY_ARRAY[k];
                if(key.equals("入库时间") || key.equals("ETA")) {
                    Date dateBeforeSevenDay = DateUtil.getDateBefore(new Date(), 7);
                    String beforeSevenDay = DateUtil.DateToString(dateBeforeSevenDay);
                    if(val.compareTo(beforeSevenDay) >= 0){
                        cell.setCellStyle(blueColor);
                    }
                }

            }
        }
        return workBook;
    }

    public XSSFWorkbook writeStoreInfoExcel(List<QueryContractInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);
        XSSFCellStyle blueColor = createXssfCellStyleWithBlueColor(workBook);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        List<List<Object>> result = convertQueryStoreInfoList(data);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int j = 0; j < GlobalConst.HEAD_STOREINFO_QUERY_ARRAY.length; j++) {
            cell = row.createCell(j, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_STOREINFO_QUERY_ARRAY[j]);
            cell.setCellStyle(styleNoColor);
        }

        for (int i = 0; i < result.size(); i++) {
            //创建行
            row = sheet.createRow(i+1);
            List<Object> rowData = result.get(i);
            for (int k = 0; k < GlobalConst.HEAD_STOREINFO_QUERY_ARRAY.length; k++) {
                cell = row.createCell(k, CellType.STRING);
                String val = "";
                if(rowData.get(k) == null){
                    cell.setCellValue("");
                }else {
                    val = rowData.get(k).toString();
                    cell.setCellValue(val);
                }
                cell.setCellStyle(styleNoColor);

                //设置入库时间、ETA\ETD，七天内显示蓝色
                String key = GlobalConst.HEAD_STOREINFO_QUERY_ARRAY[k];
                if(key.equals("入库时间") || key.equals("ETA") || key.equals("ETD")) {
                    Date dateBeforeSevenDay = DateUtil.getDateBefore(new Date(), 7);
                    String beforeSevenDay = DateUtil.DateToString(dateBeforeSevenDay);
                    if(val.compareTo(beforeSevenDay) >= 0){
                        cell.setCellStyle(blueColor);
                    }
                }
            }
        }
        return workBook;
    }

    private XSSFCellStyle createXssfCellStyleWithBlueColor(XSSFWorkbook workBook) {
        XSSFCellStyle styleNoColor = workBook.createCellStyle();
        styleNoColor.setAlignment(HorizontalAlignment.CENTER);
        styleNoColor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNoColor.setBorderBottom(BorderStyle.THIN);
        styleNoColor.setBorderLeft(BorderStyle.THIN);
        styleNoColor.setBorderRight(BorderStyle.THIN);
        styleNoColor.setBorderTop(BorderStyle.THIN);
        styleNoColor.setWrapText(true);
        styleNoColor.setFillForegroundColor((short)40);
        styleNoColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return styleNoColor;
    }
    private XSSFCellStyle createXssfCellStyleWithBlueFont(XSSFWorkbook workBook) {
        XSSFCellStyle styleNoColor = workBook.createCellStyle();
        styleNoColor.setAlignment(HorizontalAlignment.CENTER);
        styleNoColor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNoColor.setBorderBottom(BorderStyle.THIN);
        styleNoColor.setBorderLeft(BorderStyle.THIN);
        styleNoColor.setBorderRight(BorderStyle.THIN);
        styleNoColor.setBorderTop(BorderStyle.THIN);
        styleNoColor.setWrapText(true);
        Font monthFont = workBook.createFont();
        monthFont.setColor((short)40);
        styleNoColor.setFont(monthFont);
        return styleNoColor;
    }
    private XSSFCellStyle createXssfCellStyle(XSSFWorkbook workBook) {
        XSSFCellStyle styleNoColor = workBook.createCellStyle();
        styleNoColor.setAlignment(HorizontalAlignment.CENTER);
        styleNoColor.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNoColor.setBorderBottom(BorderStyle.THIN);
        styleNoColor.setBorderLeft(BorderStyle.THIN);
        styleNoColor.setBorderRight(BorderStyle.THIN);
        styleNoColor.setBorderTop(BorderStyle.THIN);
        styleNoColor.setWrapText(true);
        return styleNoColor;
    }
    private List<List<Object>> convertQueryStoreInfoList(List<QueryContractInfo> data) {
        List<List<Object>> result = new ArrayList<>();
        for (QueryContractInfo storeInfo : data) {
            List<Object> list = new ArrayList<>();
            list.add(storeInfo.getCargoName());//"商品",
            list.add(storeInfo.getLevel());// "级别",
            list.add(storeInfo.getCompanyNo());// "厂号",
            list.add(storeInfo.getCargoNo());// "库号",
            list.add(storeInfo.getExternalContract());// "外合同编号",
            list.add(storeInfo.getInsideContract());// "内合同编号",
            list.add(storeInfo.getContainerNo());// "柜号",
            list.add(storeInfo.getLadingbillNo());// "提单号",
            list.add(storeInfo.getStoreDate());// "入库时间",
            list.add(storeInfo.getWarehouse());// "仓库",
            list.add(storeInfo.getInvoiceAmount());// "发票重量",
            list.add(storeInfo.getBoxes());// "发票箱数",
            list.add(storeInfo.getRealStoreWeight());// "现库存重量",
            list.add(storeInfo.getRealStoreBoxes());// "现库存箱数",
            list.add(storeInfo.getRealStoreMoney());// "库存成本"

            result.add(list);
        }
        return result;
    }
    private List<List<Object>> convertQueryStoreInList(List<CargoStoreInfo> data) {
        List<List<Object>> result = new ArrayList<>();
        for (CargoStoreInfo storeInfo : data) {
            //商品，级别，存储条件，厂号，库号，外合同编号，内合同编号，柜号，提单号，入库时间，
            // 冷库，发票数量，发票箱数，检疫证签发日期，是否上传检疫证，ETA,平均箱重
            List<Object> list = new ArrayList<>();
            list.add(storeInfo.getCargo_name());//"商品",
            list.add(storeInfo.getLevel());// "级别",
            list.add(storeInfo.getStorage_condition());// "存储条件",
            list.add(storeInfo.getCompany_no());// "厂号",
            list.add(storeInfo.getCargo_no());// "库号",
            list.add(storeInfo.getExternal_contract());// "外合同编号",
            list.add(storeInfo.getInside_contract());// "内合同编号",
            list.add(storeInfo.getContainer_no());// "柜号",
            list.add(storeInfo.getLadingbill_no());// "提单号",
            list.add(storeInfo.getStore_date());// "入库时间",
            list.add(storeInfo.getWarehouse());// "冷库",
            list.add(storeInfo.getInvoice_amount());// "发票数量",
            list.add(storeInfo.getBoxes());// "发票箱数",
            list.add(storeInfo.getJyzqfrq());// "检疫证签发日期",
            String a = storeInfo.getQacertificate();
            if(StringUtils.isNotBlank(a) && a.equals("1")){
                list.add("是");// "是否上传检疫证",
            }else{
                list.add("否");// "是否上传检疫证",
            }
            list.add(storeInfo.getEta());// "ETA"
            BigDecimal ave = BigDecimal.ZERO;
            if(Integer.valueOf(storeInfo.getBoxes()) > 0){
                ave = new BigDecimal(storeInfo.getInvoice_amount()).divide(new BigDecimal(storeInfo.getBoxes()),2,BigDecimal.ROUND_HALF_UP);
            }
            list.add(ave);
            result.add(list);
        }
        return result;
    }
    public XSSFWorkbook writeContractExcel(List<ContractBaseInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);
        XSSFCellStyle blueColor = createXssfCellStyleWithBlueColor(workBook);

        //set date format
        CellStyle dateCellStyle = workBook.createCellStyle();
        CreationHelper createHelper = workBook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/m/d"));

        List<List<Object>> result = convertQueryContractList(data);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        //创建单元格
        for (int j = 0; j < GlobalConst.HEAD_CONTRACT_QUERY_ARRAY.length; j++) {
            cell = row.createCell(j, CellType.STRING);
            cell.setCellValue(GlobalConst.HEAD_CONTRACT_QUERY_ARRAY[j]);
            cell.setCellStyle(styleNoColor);
        }


        for (int i = 0; i < result.size(); i++) {
            //创建行
            row = sheet.createRow(i+1);
            List<Object> rowData = result.get(i);
            for (int k = 0; k < GlobalConst.HEAD_CONTRACT_QUERY_ARRAY.length; k++) {
                cell = row.createCell(k, CellType.STRING);
                String val = "";
                if(rowData.get(k) == null){
                    cell.setCellValue("");
                }else {
                    val = rowData.get(k).toString();
                    cell.setCellValue(val);
                }
                cell.setCellStyle(styleNoColor);
                //设置入库时间、ETA\ETD，七天内显示蓝色
                String key = GlobalConst.HEAD_CONTRACT_QUERY_ARRAY[k];
                if(key.equals("入库时间") || key.equals("ETA") || key.equals("ETD")) {
                    Date dateBeforeSevenDay = DateUtil.getDateBefore(new Date(), 7);
                    String beforeSevenDay = DateUtil.DateToString(dateBeforeSevenDay);
                    if(val.compareTo(beforeSevenDay) >= 0){
                        cell.setCellStyle(blueColor);
                    }
                }
            }
        }
        return workBook;
    }
    public XSSFWorkbook writeCargoExcel(List<QueryContractInfo> data){
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //创建样式
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);
        XSSFCellStyle blueColor = createXssfCellStyleWithBlueColor(workBook);

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
                String val = "";
                if(rowData.get(k) == null){
                    cell.setCellValue("");
                }else {
                    val = rowData.get(k).toString();
                    cell.setCellValue(val);
                }
                cell.setCellStyle(styleNoColor);
                //设置入库时间、ETA\ETD，七天内显示蓝色
                String key = GlobalConst.HEAD_CARGO_QUERY_ARRAY[k];
                if(key.equals("入库时间") || key.equals("ETA") || key.equals("ETD")) {
                    Date dateBeforeSevenDay = DateUtil.getDateBefore(new Date(), 7);
                    String beforeSevenDay = DateUtil.DateToString(dateBeforeSevenDay);
                    if(val.compareTo(beforeSevenDay) >= 0){
                        cell.setCellStyle(blueColor);
                    }
                }
            }
        }
        return workBook;
    }

    private List<List<Object>> convertQueryContractList(List<ContractBaseInfo> data) {
        List<List<Object>> result = new ArrayList<>();
        for (ContractBaseInfo contractBaseInfo : data) {
            List<Object> list = new ArrayList<>();
            list.add(contractBaseInfo.getExternalContract());//"外合同编号",
            list.add(contractBaseInfo.getInsideContract());// "内合同编号",
            list.add(contractBaseInfo.getExternalCompany());// "外商",
            list.add(contractBaseInfo.getContractDate());// "合同日期",
            list.add(contractBaseInfo.getTotalContractAmount());// "合同总重量",
            list.add(contractBaseInfo.getTotalContractMoney());// "合同总金额",
            list.add(contractBaseInfo.getTotalInvoiceAmount());// "发票总重量",
            list.add(contractBaseInfo.getTotalInvoiceMoney());// "发票总金额",
            list.add(contractBaseInfo.getTotalBoxes());//总箱数
            list.add(contractBaseInfo.getOriginCountry());// "原产地",
            list.add(contractBaseInfo.getCurrency());// "币种",
            list.add(contractBaseInfo.getEtd());// "ETD",
            list.add(contractBaseInfo.getEta());// "ETA",
            list.add(contractBaseInfo.getExpectSailingDate());// "预计船期",
            list.add(contractBaseInfo.getDestinationPort());// "目的港",
            String status = contractBaseInfo.getStatus();
            switch (status){
                case "1":status = "已下单";break;
                case "2":status = "已装船";break;
                case "3":status = "已到港";break;
                case "4":status = "已入库";break;
                case "5":status = "已售完";break;
            }
            list.add(status);// "状态"
            result.add(list);
        }
        return result;
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
            list.add(cargoInfo.getContainerNo());//柜号
            list.add(cargoInfo.getCurrency());//币种
            list.add(cargoInfo.getCargoName());//商品
            list.add(cargoInfo.getLevel());//级别
            list.add(cargoInfo.getRealStoreBoxes());//库存箱数
            list.add(cargoInfo.getUnitPrice());//单价
            list.add(cargoInfo.getContractAmount());//合同重量
            list.add(cargoInfo.getContractMoney());//合同金额
            list.add(cargoInfo.getInvoiceAmount());//发票重量
            list.add(cargoInfo.getInvoiceMoney());//发票金额
            list.add(cargoInfo.getEtd());//ETD
            list.add(cargoInfo.getEta());//ETA
            list.add(cargoInfo.getExpectSailingDate());//预计船期
            list.add(cargoInfo.getDestinationPort());//目的港
            list.add(CommonUtil.revertStatusToCHN(cargoInfo.getStatus()));//状态
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
        XSSFCellStyle styleNoColor = createXssfCellStyle(workBook);
        XSSFCellStyle blueColor = createXssfCellStyleWithBlueColor(workBook);

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
            List<CargoInfo> cargoData = cargoRepository.findByContractIdOrderByIdAsc(baseInfo.getContractId());
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
                            if(saleEnd == start+1) {
                                for (int k = 0; k < GlobalConst.HEAD_CARGO_ARRAY.length; k++) {
                                    if (chkArr[GlobalConst.HEAD_CONTRACT_ARRAY.length + k].equals("1")) {
                                        //重复写入单条商品信息
                                        Object value = cargoList.get(k);
                                        if (value instanceof String) {
                                            cell = row.createCell(firstSize + cargoSize++, CellType.STRING);//从商品的单元格开始写，下标是50
                                            cell.setCellValue(String.valueOf(cargoList.get(k)));
                                            if (String.valueOf(value).matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}")) {//日期
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
                //创建行
                XSSFRow row = sheet.getRow(saleStart);
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
                            //设置入库时间、ETA\ETD，七天内显示蓝色
                            String key = GlobalConst.HEAD_CONTRACT_ARRAY[k];
                            if(key.equals("入库日期") || key.equals("ETA") || key.equals("ETD")) {
                                Date dateBeforeSevenDay = DateUtil.getDateBefore(new Date(), 7);
                                String beforeSevenDay = DateUtil.DateToString(dateBeforeSevenDay);
                                if(String.valueOf(value).compareTo(beforeSevenDay) >= 0){
                                    cell.setCellStyle(blueColor);
                                }
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
        list.add(baseInfo.getOwnerCompany());//合同代理公司
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

        list.add(baseInfo.getCustomerName());//客户名称
        list.add("");//预售时间
        list.add(baseInfo.getExpectSaleUnitPrice());//预售单价(元/KG)
        list.add(baseInfo.getExpectSaleWeight());//预售重量(kg)

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
        list.add(baseInfo.getAgentMoney());//货代费
        list.add(baseInfo.getDzdjsdrq());//电子单据收到日期
        list.add(baseInfo.getJyzzbsdrq());//检疫证正本收到日期
        list.add(baseInfo.getJyzqfrq());//检疫证签发日期
        list.add(baseInfo.getBgdcjrq());//报关单出具日期
        list.add(baseInfo.getAgent());//货代
        list.add(baseInfo.getAgentSendDate());//正本单据收到日期
        list.add(baseInfo.getTariff());//关税
        list.add(baseInfo.getAddedValueTax());//增值税
        list.add(baseInfo.getTaxPayDate());//付税日期
        list.add(baseInfo.getTariffNo());//报关单号
        list.add(baseInfo.getAgentPassDate());//放行日期
        list.add(baseInfo.getWarehouse());//仓库
        list.add(baseInfo.getStoreDate());//入库日期
        list.add(baseInfo.getCaiyangcangku());//采样仓库
        list.add(baseInfo.getCaiyangdate());//采样日期
        list.add(baseInfo.getZhixiangfei());//滞箱费
        list.add(baseInfo.getZhigangfei());//滞港费
        list.add(baseInfo.getRemark());//备注
        list.add(baseInfo.getCargoType());//商品属性
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

        if(cargoInfo.getStatus().equals(GlobalConst.STORED)){
            list.add(cargoInfo.getRealStoreWeight());//当前库存重量
            list.add(cargoInfo.getRealStoreBoxes());//当前库存箱数
        }else{
            list.add(0);//当前库存重量
            list.add(0);//当前库存箱数
        }

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


    public XSSFWorkbook hesuan(ContractBaseInfo contractBaseInfo){
        List<CargoInfo> cargoInfos = cargoRepository.findByContractIdOrderByIdAsc(contractBaseInfo.getContractId());
        Set<String> businessModeSet = new HashSet<>();
        for (CargoInfo cargoInfo : cargoInfos) {
            businessModeSet.add(cargoInfo.getBusinessMode());
        }
        String businessMode = "";
        for (String bs : businessModeSet) {
            businessMode += bs + ",";
        }
        if(businessMode.endsWith(",")){
            businessMode = businessMode.substring(0,businessMode.length()-1);
        }
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //横向A4纸
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        printSetup.setLandscape(true); // 打印方向，true：横向，false：纵向(默认) 

        //创建样式
        XSSFCellStyle style = workBook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        //新建font实体
        XSSFFont hssfFont = workBook.createFont();
        hssfFont.setFontHeightInPoints((short)20);
        hssfFont.setFontName("宋体");
        hssfFont.setBold(true);
        style.setFont(hssfFont);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        for (int i = 0; i < 10; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue("进 口 核 算 表");
            cell.setCellStyle(style);
        }
        row = sheet.createRow(1);
        for (int i = 0; i < 10; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue("进 口 核 算 表");
            cell.setCellStyle(style);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 9));

        XSSFFont textFont = workBook.createFont();
        textFont.setFontHeightInPoints((short)12);
        textFont.setFontName("宋体");

        XSSFCellStyle textStyle = workBook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true);
        textStyle.setFont(textFont);

        int two = 2;
        row = sheet.createRow(two);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("制表日期：");
        cell.setCellStyle(textStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("制表日期：");
        cell.setCellStyle(textStyle);
        sheet.addMergedRegion(new CellRangeAddress(two, two, 0, 1));

        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(DateUtil.DateToString(new Date()));
        cell.setCellStyle(textStyle);
        sheet.autoSizeColumn(2);

        XSSFCellStyle blackStyle = createXssfCellStyle(workBook);
        blackStyle.setFont(textFont);

        int three = 4;
        row = sheet.createRow(three);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("外合同编号");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("外合同编号");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getExternalContract());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getExternalContract());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("内合同编号");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("内合同编号");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getInsideContract());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getInsideContract());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("外商");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getExternalCompany());
        cell.setCellStyle(blackStyle);

        int four = 5;
        row = sheet.createRow(four);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("合同总重量(KG)");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("合同总重量(KG)");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getTotalContractAmount());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getTotalContractAmount());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("合同总金额");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("合同总金额");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getTotalContractMoney());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getTotalContractMoney());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("币种");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getCurrency());
        cell.setCellStyle(blackStyle);

        int five = 6;
        row = sheet.createRow(five);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("价格条件");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("价格条件");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getPriceCondition());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getPriceCondition());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("付款方式");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("付款方式");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getPayType());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getPayType());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("汇率");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getExchangeRate());
        cell.setCellStyle(blackStyle);

        int six = 7;
        row = sheet.createRow(six);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("储存条件");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("储存条件");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getStorageCondition());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getStorageCondition());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("原产地");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("原产地");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getOriginCountry());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getOriginCountry());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("业务模式");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue(businessMode);
        cell.setCellStyle(blackStyle);

        int seven = 8;
        row = sheet.createRow(seven);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("ETD");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("ETD");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(seven, seven, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getEtd());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getEtd());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(seven, seven, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("ETA");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("ETA");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(seven, seven, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getEta());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getEta());
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(seven, seven, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("拟存货仓库");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue("");
        cell.setCellStyle(blackStyle);

        int eight = 9;
        row = sheet.createRow(eight);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("关税");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("关税");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(eight, eight, 0, 1));
        double tariff = 0;
        double zzs = 0;
        String originCountry = contractBaseInfo.getOriginCountry();
        if(originCountry.equals("澳大利亚")){
            tariff = contractBaseInfo.getTotalContractMoney()*contractBaseInfo.getExchangeRate()*0.06;
            zzs = contractBaseInfo.getTotalContractMoney()*contractBaseInfo.getExchangeRate()*1.06*0.09;
        }else if(originCountry.equals("新西兰") || originCountry.equals("哥斯达黎加")){
            tariff = 0;
            zzs = contractBaseInfo.getTotalContractMoney()*contractBaseInfo.getExchangeRate()*0.09;
        }else{
            tariff = contractBaseInfo.getTotalContractMoney()*contractBaseInfo.getExchangeRate()*0.12;
            zzs = contractBaseInfo.getTotalContractMoney()*contractBaseInfo.getExchangeRate()*1.12*0.09;
        }
        tariff = (double)Math.round(tariff*100)/100;
        zzs = (double)Math.round(zzs*100)/100;
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(tariff);
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(tariff);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(eight, eight, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("增值税");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("增值税");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(eight, eight, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(zzs);
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(zzs);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(eight, eight, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("目的港");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getDestinationPort());
        cell.setCellStyle(blackStyle);

        int nine = 10;
        row = sheet.createRow(nine);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("仓储费");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("仓储费");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(nine, nine, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue((double)Math.round(0.09*contractBaseInfo.getTotalContractAmount()));
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue((double)Math.round(0.09*contractBaseInfo.getTotalContractAmount()));
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(nine, nine, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("报关费");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("报关费");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(nine, nine, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue((double)Math.round(0.3*contractBaseInfo.getTotalContractAmount()));
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue((double)Math.round(0.3*contractBaseInfo.getTotalContractAmount()));
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(nine, nine, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("资金占用利息");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue((double)Math.round(contractBaseInfo.getTotalContractMoney()*contractBaseInfo.getExchangeRate()*0.007));
        cell.setCellStyle(blackStyle);

        row = sheet.createRow(13);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("产品名称");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("级别");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue("库号");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue("采购单价\n(/KG)");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("合同数量\n(小计)");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("合同金额\n(小计)");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue("含税\n成本单价");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue("预计含税\n销售单价");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("除税利润");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue("利润率(%)");
        cell.setCellStyle(blackStyle);

        double total1 = 0;
        double total2 = 0;
        for (int i = 0; i < cargoInfos.size(); i++) {
            row = sheet.createRow(14 + i);
            CargoInfo cargoInfo = cargoInfos.get(i);
            cell = row.createCell(0,CellType.STRING);
            cell.setCellValue(cargoInfo.getCargoName());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(1,CellType.STRING);
            cell.setCellValue(cargoInfo.getLevel());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(2,CellType.STRING);
            cell.setCellValue(cargoInfo.getCargoNo());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(3,CellType.STRING);
            double unitPrice = cargoInfo.getUnitPrice();
            cell.setCellValue(unitPrice);
            cell.setCellStyle(blackStyle);
            cell = row.createCell(4,CellType.STRING);
            cell.setCellValue(cargoInfo.getContractAmount());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(5,CellType.STRING);
            cell.setCellValue(cargoInfo.getContractMoney());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(6,CellType.STRING);
            double a = 0;
            if(originCountry.equals("澳大利亚")){
                a = unitPrice*contractBaseInfo.getExchangeRate()*1.06*1.09+0.6;
            }else if(originCountry.equals("新西兰") || originCountry.equals("哥斯达黎加")){
                a = unitPrice*contractBaseInfo.getExchangeRate()*1.09+0.6;
            }else{
                a = unitPrice*contractBaseInfo.getExchangeRate()*1.12*1.09+0.6;
            }
            a = Math.round(a*100)/100;
            double b = (double)Math.round(a*1.03*100)/100;
            double c = (b-a)/1.1*cargoInfo.getContractAmount();
            c = (double)Math.round(c*100)/100;
            double d = (b-a)/a*100;
            d = (double)Math.round(d*100)/100;

            total1 += cargoInfo.getContractAmount()*a;
            total2 += cargoInfo.getContractAmount()*b;
            total1 = Math.round(total1*100)/100;
            total2 = Math.round(total2*100)/100;

            cell.setCellValue(a);
            cell.setCellStyle(blackStyle);
            cell = row.createCell(7,CellType.STRING);
            cell.setCellValue(b);
            cell.setCellStyle(blackStyle);
            cell = row.createCell(8,CellType.STRING);
            cell.setCellValue(c);
            cell.setCellStyle(blackStyle);
            cell = row.createCell(9,CellType.STRING);
            cell.setCellValue(d);
            cell.setCellStyle(blackStyle);
        }

        int ten = 11;
        row = sheet.createRow(ten);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("含税成本总计");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("含税成本总计");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(total1);
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue(total1);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("预计销售额总计");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("预计销售额总计");
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(total2);
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue(total2);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 6, 7));

        XSSFCellStyle leftTextStyle = workBook.createCellStyle();
        leftTextStyle.setAlignment(HorizontalAlignment.LEFT);
        leftTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        leftTextStyle.setWrapText(true);
        leftTextStyle.setFont(textFont);

        int begin = cargoInfos.size()+16;
        row = sheet.createRow(begin);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("经办人:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("经办人:");
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin, begin, 0, 1));

        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue("部门经理:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("部门经理:");
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin, begin, 3, 4));

        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue("资金额度审批:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("资金额度审批:");
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin, begin, 7, 8));

        row = sheet.createRow(begin+2);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("贸管库管审核:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellValue("贸管库管审核:");
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin+2, begin+2, 0, 1));

        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue("财务审批:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("财务审批:");
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin+2, begin+2, 3, 4));

        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue("业务审批:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("业务审批:");
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin+2, begin+2, 7, 8));

        for (int j = 0; j < 10; j++) {
            sheet.autoSizeColumn(j);
        }
        return workBook;
    }

    public XSSFWorkbook ruku(ContractBaseInfo contractBaseInfo){
        List<CargoInfo> cargoInfos = cargoRepository.findByContractIdOrderByIdAsc(contractBaseInfo.getContractId());
        Set<String> businessModeSet = new HashSet<>();
        for (CargoInfo cargoInfo : cargoInfos) {
            businessModeSet.add(cargoInfo.getBusinessMode());
        }
        String businessMode = "";
        for (String bs : businessModeSet) {
            businessMode += bs + ",";
        }
        if(businessMode.endsWith(",")){
            businessMode = businessMode.substring(0,businessMode.length()-1);
        }
        //创建工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet();
        //横向A4纸
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        printSetup.setLandscape(true); // 打印方向，true：横向，false：纵向(默认) 

        //创建样式
        XSSFCellStyle style = workBook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        //新建font实体
        XSSFFont hssfFont = workBook.createFont();
        hssfFont.setFontHeightInPoints((short)20);
        hssfFont.setFontName("宋体");
        hssfFont.setBold(true);
        style.setFont(hssfFont);

        //创建头
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = null;
        for (int i = 0; i < 12; i++) {
            cell = row.createCell(i, CellType.STRING);
            cell.setCellValue("进口商品入库申请单");
            cell.setCellStyle(style);
        }
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 11));

        XSSFFont textFont = workBook.createFont();
        textFont.setFontHeightInPoints((short)12);
        textFont.setFontName("宋体");

        XSSFCellStyle textStyle = workBook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true);
        textStyle.setFont(textFont);
        XSSFCellStyle leftTextStyle = workBook.createCellStyle();
        leftTextStyle.setAlignment(HorizontalAlignment.LEFT);
        leftTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        leftTextStyle.setWrapText(true);
        leftTextStyle.setFont(textFont);
        XSSFCellStyle blackStyle = createXssfCellStyle(workBook);
        blackStyle.setFont(textFont);
        XSSFCellStyle blackLeftStyle = createXssfCellStyle(workBook);
        blackLeftStyle.setAlignment(HorizontalAlignment.LEFT);
        blackLeftStyle.setFont(textFont);

        int two = 3;
        row = sheet.createRow(two);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("申请日期：");
        cell.setCellStyle(leftTextStyle);
        row.createCell(1,CellType.STRING);
        sheet.addMergedRegion(new CellRangeAddress(two, two, 0, 1));

        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(DateUtil.DateToString(new Date()));
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(3,CellType.STRING);
        sheet.addMergedRegion(new CellRangeAddress(two, two, 2, 3));
        //sheet.autoSizeColumn(2);

        int three = 4;
        row = sheet.createRow(three);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("合同代理公司");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getOwnerCompany());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("商品属性");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getCargoType());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("储存条件");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 8, 9));
        cell = row.createCell(10,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getStorageCondition());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(11,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(three, three, 10, 11));

        int four = 5;
        row = sheet.createRow(four);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("外合同号");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getExternalContract());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("内合同号");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getInsideContract());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("柜号");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 8, 9));
        cell = row.createCell(10,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getContainerNo());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(11,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(four, four, 10, 11));

        int five = 6;
        row = sheet.createRow(five);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("发票总重量(KG）");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getTotalInvoiceAmount());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("箱数合计");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getTotalBoxes());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("提单号");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 8, 9));
        cell = row.createCell(10,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getLadingbillNo());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(11,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(five, five, 10, 11));

        int six = 7;
        row = sheet.createRow(six);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("拟入仓库名称");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getOwnerCompany());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 2, 3));
        cell = row.createCell(4,CellType.STRING);
        cell.setCellValue("预计入库时间");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(5,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 4, 5));
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getCargoType());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 6, 7));
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("是否分拣");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 8, 9));
        cell = row.createCell(10,CellType.STRING);
        cell.setCellStyle(blackStyle);
        cell = row.createCell(11,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(six, six, 10, 11));

        int seven = 8;
        row = sheet.createRow(seven);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("拟委托货代");
        cell.setCellStyle(blackLeftStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(blackLeftStyle);
        sheet.addMergedRegion(new CellRangeAddress(seven, seven, 0, 1));
        cell = row.createCell(2,CellType.STRING);
        cell.setCellValue(contractBaseInfo.getAgent());
        cell.setCellStyle(blackStyle);
        cell = row.createCell(3,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(seven, seven, 2, 3));

        int ten = 10;
        row = sheet.createRow(ten);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("库号");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(blackStyle);
        cell = row.createCell(2,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 0, 2));
        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue("产品名称");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(4,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 3, 4));
        cell = row.createCell(5,CellType.STRING);
        cell.setCellValue("级别");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(6,CellType.STRING);
        cell.setCellValue("箱数");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue("重量(KG)");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(8,CellType.STRING);
        cell.setCellValue("平均箱重");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(9,CellType.STRING);
        cell.setCellValue("厂号");
        cell.setCellStyle(blackStyle);
        cell = row.createCell(10,CellType.STRING);
        cell.setCellStyle(blackStyle);
        sheet.addMergedRegion(new CellRangeAddress(ten, ten, 9, 10));


        for (int i = 0; i < cargoInfos.size(); i++) {
            CargoInfo cargoInfo = cargoInfos.get(i);
            int index = ten+i+1;
            row = sheet.createRow(index);
            cell = row.createCell(0,CellType.STRING);
            cell.setCellValue(cargoInfo.getCargoNo());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(1,CellType.STRING);
            cell.setCellStyle(blackStyle);
            cell = row.createCell(2,CellType.STRING);
            cell.setCellStyle(blackStyle);
            sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
            cell = row.createCell(3,CellType.STRING);
            cell.setCellValue(cargoInfo.getCargoName());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(4,CellType.STRING);
            cell.setCellStyle(blackStyle);
            sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 4));
            cell = row.createCell(5,CellType.STRING);
            cell.setCellValue(cargoInfo.getLevel());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(6,CellType.STRING);
            cell.setCellValue(cargoInfo.getBoxes());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(7,CellType.STRING);
            cell.setCellValue(cargoInfo.getInvoiceAmount());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(8,CellType.STRING);

            BigDecimal ave = BigDecimal.ZERO;
            if(Integer.valueOf(cargoInfo.getBoxes()) > 0){
                ave = new BigDecimal(cargoInfo.getInvoiceAmount()).divide(new BigDecimal(cargoInfo.getBoxes()),2,BigDecimal.ROUND_HALF_UP);
            }
            cell.setCellValue(ave.toString());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(9,CellType.STRING);
            cell.setCellValue(cargoInfo.getCompanyNo());
            cell.setCellStyle(blackStyle);
            cell = row.createCell(10,CellType.STRING);
            cell.setCellStyle(blackStyle);
            sheet.addMergedRegion(new CellRangeAddress(index, index, 9, 10));
        }

        int begin = cargoInfos.size()+13;
        row = sheet.createRow(begin);
        cell = row.createCell(0,CellType.STRING);
        cell.setCellValue("经办人:");


        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(1,CellType.STRING);
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin, begin, 0, 1));

        cell = row.createCell(3,CellType.STRING);
        cell.setCellValue("业务审批人:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4,CellType.STRING);
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin, begin, 3, 4));

        cell = row.createCell(7,CellType.STRING);
        cell.setCellValue("库管员审核:");
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(8,CellType.STRING);
        cell.setCellStyle(leftTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(begin, begin, 7, 8));

        for (int j = 0; j < 10; j++) {
            sheet.autoSizeColumn(j);
        }
        return workBook;
    }
}
