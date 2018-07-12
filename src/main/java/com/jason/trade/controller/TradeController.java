package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.model.*;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SaleRepository;
import com.jason.trade.repository.SysLogRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private SysLogRepository sysLogRepository;
    @Autowired
    private ContractBaseInfoMapper contractBaseInfoMapper;

    @RequestMapping(value = "/list")
    public String getTradeList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        JSONObject result = tradeService.queryContractListByMapper(contractParam);
        return result.toString();
    }

    @RequestMapping(value = "/cargo/list")
    public String getCargoList(@RequestParam("contractId") String contractId) throws JSONException {
        List<CargoInfo> list = cargoRepository.findByContractIdAndStatusNot(contractId,GlobalConst.DISABLE);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

    @RequestMapping(value = "/cargo/all")
    public String getCargoAllList(@RequestParam("limit") int limit, @RequestParam("offset") int offset,CargoParam cargoParam) throws JSONException {
        cargoParam.setStart(offset);
        cargoParam.setLimit(limit);
        JSONObject result = tradeService.queryAllCargoList(cargoParam);
        return result.toString();
    }

    @RequestMapping(value = "/cargo/getTotalStore")
    public String getTotalStore(CargoParam cargoParam) throws JSONException {
        JSONObject result = tradeService.getTotalStore(cargoParam);
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
        contractBaseInfo.setVersion(1);
        ContractBaseInfo record = tradeService.saveContract(contractBaseInfo,cargoId);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增合同"+record.getContractId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/add")
    public String cargoAdd(CargoInfo cargoInfo, HttpSession session){
        cargoInfo.setStatus(GlobalConst.EDITING);
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
        CargoInfo data = cargoRepository.save(cargoInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增商品"+data.getCargoId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/sale/add")
    public String saleAdd(SaleInfo saleInfo, HttpSession session){
        saleInfo.setStatus(GlobalConst.ENABLE);
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        if(saleInfo.getSaleId() == null) {//新增
            String now = DateUtil.DateTimeToString(new Date());
            saleInfo.setCreateUser(userInfo.getName());
            saleInfo.setCreateDateTime(now);
        }

        SaleInfo data = tradeService.saveSale(saleInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增销售记录"+data.getSaleId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/contract/update")
    public String contractUpdate(ContractBaseInfo contractBaseInfo, HttpSession session){
        Integer currentVersion = contractRepository.findOne(contractBaseInfo.getId()).getVersion();
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        if(currentVersion > contractBaseInfo.getVersion()){
            return GlobalConst.MODIFIED;
        }

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
            contractBaseInfo.setStatus(GlobalConst.STORED);
        }
        contractBaseInfo.setVersion(contractBaseInfo.getVersion()+1);
        //contractRepository.save(contractBaseInfo);
        contractBaseInfoMapper.updateByPrimaryKeySelective(contractBaseInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("更新合同"+contractBaseInfo.getContractId());
        sysLog.setOperation("更新");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/contract/delete")
    public String contractDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        tradeService.updateContractStatus(ids,GlobalConst.DISABLE);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除合同"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/delete")
    public String cargoDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);

        tradeService.updateCargoStatus(ids,GlobalConst.DISABLE);
        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除商品"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/sale/delete")
    public String saleDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);

        tradeService.updateSaleStatus(ids,GlobalConst.DISABLE);
        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除销售记录"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getName());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

}
