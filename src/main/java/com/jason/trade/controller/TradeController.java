package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SaleRepository;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.RespUtil;
import com.jason.trade.util.SetStyleUtils;
import com.jason.trade.util.WebSecurityConfig;
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
import java.util.*;

@RestController
@RequestMapping(value = "/trade")
public class TradeController {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private TradeService tradeService;

    @RequestMapping(value = "/list")
    public String getTradeList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        List<ContractBaseInfo> list = tradeService.queryContractList(contractParam);
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

    @RequestMapping(value = "/cargo/all")
    public String getCargoAllList(@RequestParam("contractNo") String contractNo) throws JSONException {
        ContractBaseInfo contract = contractRepository.findByExternalContractAndStatus(contractNo,GlobalConst.ENABLE);
        JSONObject result = new JSONObject();
        if(contract != null) {
            List<CargoInfo> list = cargoRepository.findByContractIdAndStatus(contract.getContractId(), GlobalConst.ENABLE);
            result.put("total",list.size());
            result.put("rows",list);
        }else{
            List<CargoInfo> list = cargoRepository.findByStatus(GlobalConst.ENABLE);
            result.put("total",list.size());
            result.put("rows",list);
        }
        return result.toString();
    }

    @RequestMapping(value = "/sale/list")
    public String getSaleList(@RequestParam("cargoId") String cargoId) throws JSONException {
        List<SaleInfo> list = saleRepository.findByCargoIdAndStatus(cargoId,GlobalConst.ENABLE);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

    @PostMapping(value="/contract/add")
    public String contractAdd(ContractBaseInfo contractBaseInfo,@RequestParam("cargoId") String cargoId, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String now = DateUtil.DateTimeToString(new Date());
        contractBaseInfo.setCreateUser(userInfo.getName());
        contractBaseInfo.setCreateDateTime(now);
        tradeService.saveContract(contractBaseInfo,cargoId);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/add")
    public String cargoAdd(CargoInfo cargoInfo, HttpSession session){
        cargoInfo.setStatus(GlobalConst.ENABLE);
        if(StringUtils.isBlank(cargoInfo.getCargoId())) {
            cargoInfo.setCargoId(UUID.randomUUID().toString());
        }
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String now = DateUtil.DateTimeToString(new Date());
        cargoInfo.setCreateUser(userInfo.getName());
        cargoInfo.setCreateDateTime(now);
        cargoInfo.setExpectStoreBoxes(cargoInfo.getBoxes());
        cargoInfo.setRealStoreBoxes(cargoInfo.getBoxes());
        cargoInfo.setExpectStoreWeight(cargoInfo.getInvoiceAmount());
        cargoInfo.setRealStoreWeight(cargoInfo.getInvoiceAmount());
        //成本单价=采购单价*汇率*（1+关税税率）*（1+增值税率）+运费+冷藏+货代费
        String contractId = cargoInfo.getContractId();
        ContractBaseInfo contract = contractRepository.findByContractId(contractId);
        double tariffRate = contract.getTariffRate();//关税税率
        double exchangeRate = contract.getExchangeRate();//汇率
        double taxRate = contract.getTaxRate();//增值税率
        double costPrice = cargoInfo.getUnitPrice()*exchangeRate*(1+tariffRate)*(1+taxRate);

        cargoInfo.setCostPrice(costPrice);

        //库存成本=库存*成本单价
        double realStoreMoney = costPrice * cargoInfo.getInvoiceAmount();
        cargoInfo.setRealStoreMoney(realStoreMoney);
        CargoInfo data = cargoRepository.save(cargoInfo);
        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/sale/add")
    public String saleAdd(SaleInfo saleInfo, HttpSession session){
        saleInfo.setStatus(GlobalConst.ENABLE);
        if(saleInfo.getSaleId() == null) {//新增
            UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
            String now = DateUtil.DateTimeToString(new Date());
            saleInfo.setCreateUser(userInfo.getName());
            saleInfo.setCreateDateTime(now);
        }

        SaleInfo data = tradeService.saveSale(saleInfo);
        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/contract/update")
    public String contractUpdate(ContractBaseInfo contractBaseInfo, HttpSession session){
        Integer currentVersion = contractRepository.findOne(contractBaseInfo.getId()).getVersion();
        if(currentVersion > contractBaseInfo.getVersion()){
            return GlobalConst.MODIFIED;
        }

        if(StringUtils.isNotBlank(contractBaseInfo.getContainerNo())){
            contractBaseInfo.setStatus(GlobalConst.SHIPPED);
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getETA())){
            contractBaseInfo.setStatus(GlobalConst.ARRIVED);
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getStoreDate())){
            contractBaseInfo.setStatus(GlobalConst.STORED);
        }
        contractBaseInfo.setVersion(contractBaseInfo.getVersion()+1);
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

    @PostMapping(value="/sale/delete")
    public String saleDel(@RequestParam("ids") String ids, HttpSession session){
        tradeService.updateSaleStatus(ids,GlobalConst.DISABLE);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/contract/output")
    public String output(){
        List<ContractBaseInfo> data = contractRepository.findAll();
        String fileName = "业务台账"+DateUtil.DateToString(new Date(),"yyyyMMddHHmmss")+".xlsx";
        tradeService.writeExcel(data,fileName);
        return GlobalConst.SUCCESS;
    }
}
