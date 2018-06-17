package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.CargoInfo;
import com.jason.trade.entity.ContractBaseInfo;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.RespUtil;
import com.jason.trade.util.SetStyleUtils;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/trade")
public class TradeController {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private TradeService tradeService;

    @RequestMapping(value = "/list")
    public String getTradeList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam
                               /*@RequestParam("externalContract") String externalContract,
                               @RequestParam("contractStartDate") String contractStartDate,
                               @RequestParam("contractEndDate") String contractEndDate,
                               @RequestParam("businessMode") String businessMode,
                               @RequestParam("externalCompany") String externalCompany,
                               @RequestParam("insideContract") String insideContract*/) throws JSONException {
        List<ContractBaseInfo> list = tradeService.queryContractList(contractParam);
        //List<ContractBaseInfo> list = contractRepository.queryContractList('%'+externalContract+'%');
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

    @RequestMapping(value = "/cargo/list")
    public String getCargoList(@RequestParam("contractId") String contractId) throws JSONException {
        List<CargoInfo> list = cargoRepository.findByContractIdAndStatus(contractId,GlobalConst.ENABLE);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

    @PostMapping(value="/contract/add")
    public String contractAdd(ContractBaseInfo contractBaseInfo,@RequestParam("cargoId") String cargoId, HttpSession session){
        tradeService.saveContract(contractBaseInfo,cargoId);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/add")
    public String cargoAdd(CargoInfo cargoInfo, HttpSession session){
        cargoInfo.setStatus(GlobalConst.ENABLE);
        if(StringUtils.isBlank(cargoInfo.getCargoId())) {
            cargoInfo.setCargoId(UUID.randomUUID().toString());
        }
        CargoInfo data = cargoRepository.save(cargoInfo);
        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/contract/update")
    public String contractUpdate(ContractBaseInfo contractBaseInfo, HttpSession session){
        contractRepository.save(contractBaseInfo);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/contract/delete")
    public String contractDel(@RequestParam("ids") String ids, HttpSession session){
        String idArr[] = ids.split(",");
        ContractBaseInfo contractBaseInfo = new ContractBaseInfo();
        for (int i = 0; i < idArr.length; i++) {
            contractBaseInfo.setId(Integer.valueOf(idArr[i]));
            contractBaseInfo.setStatus(GlobalConst.DISABLE);
            contractRepository.save(contractBaseInfo);
        }
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/delete")
    public String cargoDel(@RequestParam("ids") String ids, HttpSession session){
        tradeService.updateCargoStatus(ids,GlobalConst.DISABLE);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/contract/output")
    public String output(){
        List<ContractBaseInfo> data = contractRepository.findAll();
        String fileName = "E:\\业务台账"+DateUtil.DateToString(new Date(),"yyyyMMddHHmmss")+".xlsx";
        writeExcel(data,fileName);
        return GlobalConst.SUCCESS;
    }

    private void writeExcel(List<ContractBaseInfo> data, String fileName) {
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
