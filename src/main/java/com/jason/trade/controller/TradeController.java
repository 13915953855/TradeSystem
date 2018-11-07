package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.CargoParam;
import com.jason.trade.entity.ContractForCharts;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.entity.InternalContractParam;
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
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/trade")
public class TradeController {
    private static final Logger log = LoggerFactory.getLogger(TradeController.class.getName());
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
        String type = contractParam.getType();
        JSONObject result = new JSONObject();
        if(StringUtils.isBlank(type)){
            result = tradeService.queryContractListByMapper(contractParam);
        }else{
            switch (type){
                case "n1":
                    Date before14Date = DateUtil.getDateBefore(new Date(),14);
                    String date1 = DateUtil.DateToString(before14Date);
                    result.put("total",contractRepository.countByExpectSailingDateLessThanAndStatus(date1,GlobalConst.ENABLE));
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
    @RequestMapping(value = "/internal/list")
    public String getInternalTradeList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, InternalContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        JSONObject result = tradeService.queryInternalContractListByMapper(contractParam);
        return result.toString();
    }
    @RequestMapping(value = "/cargo/list")
    public String getCargoList(@RequestParam("contractId") String contractId) throws JSONException {
        List<CargoInfo> list = cargoRepository.findByContractId(contractId);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }
    @RequestMapping(value = "/internal/cargo/list")
    public String getInternalCargoList(@RequestParam("contractId") String contractId) throws JSONException {
        List<CargoInfo> list = cargoRepository.findByContractId(contractId);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

    @RequestMapping(value = "/attachment/getAll")
    public String getAllFile(@RequestParam("contractId") String contractId) throws JSONException {
        List<Attachment> list = attachmentRepository.findByContractId(contractId);
        JSONObject result = new JSONObject();
        result.put("status","1");
        result.put("data",list);
        return result.toString();
    }

    @RequestMapping(value = "/attachment/delete")
    public String delete(@RequestParam("contractId") String contractId,@RequestParam("id") Integer id) throws JSONException {
        AttachmentKey key = new AttachmentKey();
        key.setId(id);
        key.setContractId(contractId);
        Attachment attachment = attachmentMapper.selectByPrimaryKey(key);
        String filePath = attachment.getFilePath();
        attachmentMapper.deleteByPrimaryKey(key);
        //删除文件
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        JSONObject result = new JSONObject();
        result.put("status","1");
        return result.toString();
    }

    @RequestMapping(value = "/cargo/all")
    public String getCargoAllList(@RequestParam("limit") int limit, @RequestParam("offset") int offset,CargoParam cargoParam) throws JSONException {
        cargoParam.setStart(offset);
        cargoParam.setLimit(limit);
        JSONObject result = tradeService.queryAllCargoList(cargoParam);
        return result.toString();
    }

    @RequestMapping(value = "/contract/getTotalInfo")
    public String getTotalInfo(ContractParam contractParam) throws JSONException {
        JSONObject result = tradeService.getTotalInfo(contractParam);
        return result.toString();
    }
    @RequestMapping(value = "/query/getTotalInfo")
    public String getTotalInfoForQuery(ContractParam contractParam) throws JSONException {
        JSONObject result = tradeService.getTotalInfoForQuery(contractParam);
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
        contractBaseInfo.setCreateUser(userInfo.getAccount());
        contractBaseInfo.setCreateDateTime(now);
        contractBaseInfo.setVersion(1);
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
            cargoInfo.setStatus(GlobalConst.ENABLE);
            cargoInfo.setCreateUser(userInfo.getAccount());
            cargoInfo.setCreateDateTime(now);
            cargoInfo.setExpectStoreBoxes(cargoInfo.getBoxes());
            cargoInfo.setExpectStoreWeight(cargoInfo.getInvoiceAmount());
            log.info("保存商品开始");
            cargoRepository.save(cargoInfo);
            log.info("保存商品完毕");
        }else{
            log.info("编辑商品cargoId="+cargoInfo.getCargoId());
            cargoId = cargoInfo.getCargoId();
            CargoInfo cargo = cargoRepository.findByCargoId(cargoId);
            cargoInfo.setRealStoreBoxes(cargo.getRealStoreBoxes());
            cargoInfo.setRealStoreWeight(cargo.getRealStoreWeight());
            cargoInfo.setCreateUser(userInfo.getAccount());
            cargoInfo.setCreateDateTime(now);
            cargoInfo.setExpectStoreBoxes(cargoInfo.getBoxes());
            cargoInfo.setExpectStoreWeight(cargoInfo.getInvoiceAmount());
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

    @PostMapping(value="/sale/checkStore")
    public String checkStore(@RequestParam("cargoId") String cargoId,@RequestParam("boxes") String boxes,@RequestParam("weight") String weight){
        cargoRepository.updateWeightAndBoxes(cargoId,weight,boxes);
        if(boxes.equals("0") || weight.startsWith("-") || weight.equals("0")){
            cargoInfoMapper.selloutByCargoId(cargoId);
            CargoInfo cargoInfo = cargoRepository.findByCargoId(cargoId);
            List<String> id = new ArrayList<>();
            id.add(cargoInfo.getContractId());
            List<CargoInfo> cargoList = cargoRepository.findByContractId(cargoInfo.getContractId());
            int flag = 0;
            for (CargoInfo cargo : cargoList) {
                if(cargo.getRealStoreWeight() > 0){
                    flag = 1;
                }
            }
            if(flag == 0) {
                contractRepository.updateStatusByContractId(id, GlobalConst.SELLOUT);
            }
        }else{
            cargoInfoMapper.storeByCargoId(cargoId);
        }
        return RespUtil.respSuccess("");
    }

    @PostMapping(value="/sale/add")
    public String saleAdd(SaleInfo saleInfo, HttpSession session){
        saleInfo.setStatus(GlobalConst.ENABLE);
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        if(saleInfo.getSaleId() == null) {//新增
            String now = DateUtil.DateTimeToString(new Date());
            saleInfo.setCreateUser(userInfo.getAccount());
            saleInfo.setCreateDateTime(now);
        }
        SaleInfo data = tradeService.saveSale(saleInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增销售记录"+data.getSaleId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/internal/contract/add")
    public String internalcontractAdd(InternalContractInfo contractBaseInfo,@RequestParam("cargoId") String cargoId, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String now = DateUtil.DateTimeToString(new Date());
        contractBaseInfo.setCreateUser(userInfo.getAccount());
        contractBaseInfo.setCreateDateTime(now);
        contractBaseInfo.setVersion(1);
        InternalContractInfo record = tradeService.saveInternalContract(contractBaseInfo,cargoId);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增合同"+record.getContractId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return GlobalConst.SUCCESS;
    }

    @PostMapping(value="/internal/cargo/add")
    public String internalcargoAdd(InternalCargoInfo cargoInfo, HttpSession session){
        log.info("开始处理商品新增或修改的请求");
        cargoInfo.setStatus(GlobalConst.EDITING);
        if(StringUtils.isBlank(cargoInfo.getCargoId())) {
            cargoInfo.setCargoId(UUID.randomUUID().toString());
            log.info("新增商品cargoId="+cargoInfo.getCargoId());
        }else{
            log.info("编辑商品cargoId="+cargoInfo.getCargoId());
        }

        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String now = DateUtil.DateTimeToString(new Date());
        cargoInfo.setCreateUser(userInfo.getAccount());
        cargoInfo.setCreateDateTime(now);
        log.info("保存商品开始");
        InternalCargoInfo data = internalCargoRepository.save(cargoInfo);
        log.info("保存商品完毕");

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增商品"+data.getCargoId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/contract/update")
    public String contractUpdate(ContractBaseInfo contractBaseInfo,@RequestParam("cargoId") String cargoId, HttpSession session){
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
            //对应商品的状态也设为已入库
            cargoInfoMapper.storeByContractId(contractBaseInfo.getContractId());
        }
        contractBaseInfo.setVersion(contractBaseInfo.getVersion()+1);
        contractBaseInfoMapper.updateByPrimaryKeySelective(contractBaseInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("更新合同"+contractBaseInfo.getContractId());
        sysLog.setOperation("更新");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);

        return GlobalConst.SUCCESS;
    }
    @PostMapping(value="/internal/contract/update")
    public String internalContractUpdate(InternalContractInfo contractBaseInfo,@RequestParam("cargoId") String cargoId, HttpSession session){
        Integer currentVersion = internalContractRepository.findOne(contractBaseInfo.getId()).getVersion();
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        if(currentVersion > contractBaseInfo.getVersion()){
            return GlobalConst.MODIFIED;
        }
        if(StringUtils.isNotBlank(contractBaseInfo.getStoreDate())){
            contractBaseInfo.setStatus(GlobalConst.STORED);
            //对应商品的状态也设为已入库
            cargoInfoMapper.storeByContractId(contractBaseInfo.getContractId());
        }
        contractBaseInfo.setVersion(contractBaseInfo.getVersion()+1);
        internalContractInfoMapper.updateByPrimaryKeySelective(contractBaseInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("更新合同"+contractBaseInfo.getContractId());
        sysLog.setOperation("更新");
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
    @PostMapping(value="/internal/contract/delete")
    public String intercontractDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        tradeService.deleteInternalContract(ids);

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

    @PostMapping(value="/sale/delete")
    public String saleDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        tradeService.deleteSaleInfo(ids);
        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除销售记录"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

    @RequestMapping(value = "/charts")
    public String getCharts(@RequestParam("type") String type) throws JSONException {
        JSONObject result = new JSONObject();
        if(type.equals("contractAdd")){
            List<ContractForCharts> data = contractBaseInfoMapper.getTotalNumPerDay();
            result.put("status","1");
            result.put("data",data);
        }

        return result.toString();
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file")MultipartFile files,@RequestParam("contractId") String contractId,@RequestParam("fileRef") String fileRef, @RequestParam("size") Integer size) {
        JSONObject json=new JSONObject();
        String msg = "添加成功";
        log.info("-------------------开始调用上传文件upload接口-------------------");
        try{
            String name = files.getOriginalFilename();
            String type = name.substring(name.lastIndexOf(".") + 1);// 文件类型
            String path = GlobalConst.FILE_PATH + File.separator + contractId;
            //String path = "D:"+ File.separator + contractId;
            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }
            String pathWithName = path + File.separator + name;
            File uploadFile = new File(pathWithName);
            files.transferTo(uploadFile);

            //保存记录到数据库
            Attachment attachment = new Attachment();
            attachment.setContractId(contractId);
            attachment.setFileName(name);
            attachment.setFilePath(pathWithName);
            attachment.setFileType(type);
            attachment.setFileRef(fileRef);
            attachment.setFileSize(size);
            attachment.setStatus(GlobalConst.ENABLE);
            attachment.setCreatetime(new Date());
            attachmentMapper.insertSelective(attachment);
        }catch(Exception e){
            msg="添加失败";
        }
        log.info("-------------------结束调用上传文件upload接口-------------------");
        json.put("msg", msg);
        return json.toString();
    }

    @RequestMapping(value = "/queryContractList")
    public String queryContractList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        JSONObject result = tradeService.queryContractListForQuery(contractParam);
        return result.toString();
    }

    @PostMapping(value="/notice")
    public String noticeAll(HttpSession session){
        //UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        Date before14Date = DateUtil.getDateBefore(new Date(),14);
        String date1 = DateUtil.DateToString(before14Date);
        JSONObject result = new JSONObject();
        result.put("status","1");
        int n1 = contractRepository.countByExpectSailingDateLessThanAndStatus(date1,GlobalConst.ENABLE);
        result.put("n1",n1);
        Date before60Date = DateUtil.getDateBefore(new Date(),60);
        String date2 = DateUtil.DateToString(before60Date);
        int n2 = contractRepository.countByStoreDateLessThanAndStatus(date2,GlobalConst.STORED);
        result.put("n2",n2);
        Date before15Date = DateUtil.getDateBefore(new Date(),15);
        String date3 = DateUtil.DateToString(before15Date);
        int n3 = contractRepository.countByEtaLessThanAndStatus(date3,GlobalConst.ARRIVED);
        result.put("n3",n3);
        int n4 = cargoRepository.countByRealStoreBoxesBetween(1,5);
        result.put("n4",n4);
        int n5 = saleRepository.countByProfitLessThan(0.001);
        result.put("n5",n5);
        result.put("total",n1+n2+n3+n4+n5);
        return result.toString();
    }

    @RequestMapping(value = "/queryDutyList")
    public String queryDutyList(ContractParam contractParam) throws JSONException {
        JSONObject result = new JSONObject();
        String start = contractParam.getTaxPayDateStart();
        String end = contractParam.getTaxPayDateEnd();
        if(StringUtils.isBlank(start)){
            start = "2000-01-01";
        }
        if(StringUtils.isBlank(end)){
            end = "2099-12-31";
        }
        result.put("total",contractRepository.countByTaxPayDateBetween(start,end));
        List<ContractBaseInfo> data = contractRepository.findByTaxPayDateBetweenOrderByTaxPayDateDesc(start,end);
        Double totalTariff = 0.0;
        Double totalAddedValueTax = 0.0;
        for (ContractBaseInfo contractBaseInfo : data) {
            totalTariff += contractBaseInfo.getTariff();
            totalAddedValueTax += contractBaseInfo.getAddedValueTax();
        }
        result.put("rows",data);
        result.put("totalTariff",new BigDecimal(totalTariff+""));
        result.put("totalAddedValueTax",new BigDecimal(totalAddedValueTax+""));
        return result.toString();
    }

    @RequestMapping(value = "/queryDutyTotal")
    public String queryDutyTotal(ContractParam contractParam) throws JSONException {
        JSONObject result = new JSONObject();
        String start = contractParam.getTaxPayDateStart();
        String end = contractParam.getTaxPayDateEnd();
        if(StringUtils.isBlank(start)){
            start = "2000-01-01";
        }
        if(StringUtils.isBlank(end)){
            end = "2099-12-31";
        }
        List<ContractBaseInfo> data = contractRepository.findByTaxPayDateBetweenOrderByTaxPayDateDesc(start,end);
        Double totalTariff = 0.0;
        Double totalAddedValueTax = 0.0;
        for (ContractBaseInfo contractBaseInfo : data) {
            totalTariff += contractBaseInfo.getTariff();
            totalAddedValueTax += contractBaseInfo.getAddedValueTax();
        }
        result.put("status","1");
        result.put("totalTariff",new BigDecimal(totalTariff+""));
        result.put("totalAddedValueTax",new BigDecimal(totalAddedValueTax+""));
        return result.toString();
    }
}
