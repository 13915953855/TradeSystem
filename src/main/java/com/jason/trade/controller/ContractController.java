package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.mapper.AttachmentMapper;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.mapper.InternalContractInfoMapper;
import com.jason.trade.model.*;
import com.jason.trade.repository.*;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.RespUtil;
import com.jason.trade.util.WebSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/trade")
@Slf4j
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private InternalContractRepository internalContractRepository;
    @Autowired
    private InternalCargoRepository internalCargoRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private PreSaleRepository preSaleRepository;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private SysLogRepository sysLogRepository;
    @Autowired
    private ContractBaseInfoMapper contractBaseInfoMapper;
    @Autowired
    private InternalContractInfoMapper internalContractInfoMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private CargoInfoMapper cargoInfoMapper;

    @RequestMapping(value = "/list")
    public String getTradeList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        contractParam.setSortName("contract_date");
        contractParam.setSortOrder("desc");
        String type = contractParam.getType();
        JSONObject result = new JSONObject();
        if(StringUtils.isBlank(type)){
            result = tradeService.queryContractList(contractParam);
        }else{
            switch (type){
                case "n1":
                    Date before14Date = DateUtil.getDateBefore(new Date(),14);
                    String date1 = DateUtil.DateToString(before14Date);
                    result.put("total",contractRepository.countByExpectSailingDateLessThanAndStatus(date1, GlobalConst.ENABLE));
                    result.put("rows",contractRepository.findByExpectSailingDateLessThanAndStatus(date1,GlobalConst.ENABLE));
                    break;
                case "n2":
                    Date before60Date = DateUtil.getDateBefore(new Date(),60);
                    String date2 = DateUtil.DateToString(before60Date);
                    result.put("total",contractRepository.countByStoreDateLessThanAndStatus(date2,GlobalConst.STORED));
                    result.put("rows",contractRepository.findByStoreDateLessThanAndStatus(date2,GlobalConst.STORED));
                    break;
                case "n3":
                    Date before15Date = DateUtil.getDateBefore(new Date(),15);
                    String date3 = DateUtil.DateToString(before15Date);
                    result.put("total",contractRepository.countByEtaLessThanAndStatus(date3,GlobalConst.ARRIVED));
                    result.put("rows",contractRepository.findByEtaLessThanAndStatus(date3,GlobalConst.ARRIVED));
                    break;
                default:break;
            }
        }
        return result.toString();
    }

    @PostMapping(value="/contract/add")
    public String contractAdd(ContractBaseInfo contractBaseInfo, @RequestParam("cargoId") String cargoId, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String now = DateUtil.DateTimeToString(new Date());
        contractBaseInfo.setCreateUser(userInfo.getAccount());
        contractBaseInfo.setCreateDateTime(now);
        contractBaseInfo.setVersion(1);
        contractBaseInfo.setTariff(contractBaseInfo.getTariff1()+contractBaseInfo.getTariff2()+contractBaseInfo.getTariff3()+contractBaseInfo.getTariff4()+contractBaseInfo.getTariff5()+contractBaseInfo.getTariff6());
        contractBaseInfo.setAddedValueTax(contractBaseInfo.getAddedValueTax1()+contractBaseInfo.getAddedValueTax2()+contractBaseInfo.getAddedValueTax3()+contractBaseInfo.getAddedValueTax4()+contractBaseInfo.getAddedValueTax5()+contractBaseInfo.getAddedValueTax6());
        ContractBaseInfo record = tradeService.saveContract(contractBaseInfo,cargoId);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增合同"+record.getContractId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/add")
    public String cargoAdd(CargoInfo cargoInfo, HttpSession session){
        log.info("开始处理商品新增或修改的请求");
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String now = DateUtil.DateTimeToString(new Date());
        String cargoId = "";
        if(StringUtils.isBlank(cargoInfo.getCargoId())) {
            cargoId = UUID.randomUUID().toString();
            cargoInfo.setCargoId(cargoId);
            log.info("新增商品cargoId="+cargoInfo.getCargoId());
            cargoInfo.setRealStoreBoxes(cargoInfo.getBoxes());
            cargoInfo.setRealStoreWeight(cargoInfo.getInvoiceAmount());
            String status = GlobalConst.ENABLE;
            //查询台账的状态
            String contractId = cargoInfo.getContractId();
            if(StringUtils.isNotBlank(contractId)){
                ContractBaseInfo contract = contractRepository.findByContractId(contractId);
                if(contract != null){
                    String contractStatus = contract.getStatus();
                    status = contractStatus;
                }
            }
            cargoInfo.setStatus(status);
            cargoInfo.setCreateUser(userInfo.getAccount());
            cargoInfo.setCreateDateTime(now);
            cargoInfo.setExpectStoreBoxes(GlobalConst.UNPRESALED);
            cargoInfo.setExpectStoreWeight(cargoInfo.getContractAmount());
            log.info("保存商品开始");
            cargoRepository.save(cargoInfo);
            log.info("保存商品完毕");
        }else{
            log.info("编辑商品cargoId="+cargoInfo.getCargoId());
            cargoId = cargoInfo.getCargoId();
            List<SaleInfo> saleInfoList = saleRepository.findByCargoIdAndStatus(cargoId,GlobalConst.ENABLE);
            Integer saleBoxes = 0;
            Double saleWeight = 0.0;
            for (SaleInfo saleInfo : saleInfoList) {
                saleBoxes += saleInfo.getRealSaleBoxes();
                saleWeight += saleInfo.getRealSaleWeight();
            }
            Integer realStoreBoxes = cargoInfo.getBoxes() - saleBoxes;
            cargoInfo.setRealStoreBoxes(realStoreBoxes);
            cargoInfo.setRealStoreWeight(cargoInfo.getInvoiceAmount() - saleWeight);
            cargoInfo.setCreateUser(userInfo.getAccount());
            cargoInfo.setCreateDateTime(now);
            List<PreSaleInfo> preSaleInfoList = preSaleRepository.findByCargoId(cargoId);
            Double expectStoreWeight = cargoInfo.getContractAmount();
            for (PreSaleInfo preSaleInfo : preSaleInfoList) {
                expectStoreWeight = expectStoreWeight - preSaleInfo.getExpectSaleWeight();
            }
            cargoInfo.setExpectStoreWeight(expectStoreWeight);
            if(expectStoreWeight.equals(cargoInfo.getContractAmount())){
                cargoInfo.setExpectStoreBoxes(GlobalConst.UNPRESALED);
            }else{
                cargoInfo.setExpectStoreBoxes(GlobalConst.PRESALED);
            }
            log.info("保存商品开始");
            cargoInfoMapper.updateByCargoId(cargoInfo);
            log.info("保存商品完毕");
        }

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增商品"+cargoId);
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return RespUtil.respSuccess("");
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
                cargoInfoMapper.updateStatusByContractId(contractBaseInfo.getContractId(),GlobalConst.SHIPPED);
            }
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getEta())){
            if(contractBaseInfo.getEta().compareTo(DateUtil.DateToString(new Date())) <= 0){
                contractBaseInfo.setStatus(GlobalConst.ARRIVED);
                cargoInfoMapper.updateStatusByContractId(contractBaseInfo.getContractId(),GlobalConst.ARRIVED);
            }
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getStoreDate())){
            contractBaseInfo.setStatus(GlobalConst.STORED);
            //对应商品的状态也设为已入库
            cargoInfoMapper.storeByContractId(contractBaseInfo.getContractId());
            //判断所有商品是否已售完
            int avgStatus = cargoInfoMapper.getAvgStatusByContractId(contractBaseInfo.getContractId());
            if(avgStatus == 5){
                contractBaseInfo.setStatus(GlobalConst.SELLOUT);
            }
        }
        contractBaseInfo.setVersion(contractBaseInfo.getVersion()+1);

        contractBaseInfo.setTariff(contractBaseInfo.getTariff1()+contractBaseInfo.getTariff2()+contractBaseInfo.getTariff3()+contractBaseInfo.getTariff4()+contractBaseInfo.getTariff5()+contractBaseInfo.getTariff6());
        contractBaseInfo.setAddedValueTax(contractBaseInfo.getAddedValueTax1()+contractBaseInfo.getAddedValueTax2()+contractBaseInfo.getAddedValueTax3()+contractBaseInfo.getAddedValueTax4()+contractBaseInfo.getAddedValueTax5()+contractBaseInfo.getAddedValueTax6());

        contractBaseInfoMapper.updateByPrimaryKeySelective(contractBaseInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("更新合同"+contractBaseInfo.getContractId());
        sysLog.setOperation("更新");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/contract/copy")
    public String contractCopy(@RequestParam("id") Integer id, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        tradeService.copyContract(id);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("复制合同"+id);
        sysLog.setOperation("复制");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/contract/delete")
    public String contractDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        tradeService.deleteContract(ids);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除合同"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/cargo/delete")
    public String cargoDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);

        tradeService.deleteCargo(ids);
        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除商品"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

}
