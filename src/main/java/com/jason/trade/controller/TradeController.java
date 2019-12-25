package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.CargoParam;
import com.jason.trade.entity.ContractForCharts;
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
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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



    @RequestMapping(value = "/cargo/list")
    public String getCargoList(@RequestParam("contractId") String contractId) throws JSONException {
        List<CargoInfo> list = cargoRepository.findByContractIdOrderByIdAsc(contractId);
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

    @RequestMapping(value = "/cargo/preall")
    public String getCargoPREAllList(@RequestParam("limit") int limit, @RequestParam("offset") int offset,CargoParam cargoParam) throws JSONException {
        cargoParam.setStart(offset);
        cargoParam.setLimit(limit);
        JSONObject result = tradeService.queryAllPreCargoList(cargoParam);
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
    @RequestMapping(value = "/cargo/getPreTotal")
    public String getPreTotal(CargoParam cargoParam) throws JSONException {
        JSONObject result = tradeService.getPreTotal(cargoParam);
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
    @RequestMapping(value = "/presale/list")
    public String getPreSaleList(@RequestParam("cargoId") String cargoId) throws JSONException {
        List<PreSaleInfo> list = preSaleRepository.findByCargoId(cargoId);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

    @PostMapping(value="/sale/checkStore")
    public String checkStore(@RequestParam("cargoId") String cargoId,@RequestParam("boxes") String boxes,@RequestParam("weight") String weight){
        cargoRepository.updateWeightAndBoxes(cargoId,weight,boxes);
        if(boxes.equals("0") || weight.startsWith("-") || weight.equals("0")){
            cargoInfoMapper.selloutByCargoId(cargoId);
            CargoInfo cargoInfo = cargoRepository.findByCargoId(cargoId);
            List<String> id = new ArrayList<>();
            id.add(cargoInfo.getContractId());
            List<CargoInfo> cargoList = cargoRepository.findByContractIdOrderByIdAsc(cargoInfo.getContractId());
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

    @RequestMapping(value = "/queryCargoList")
    public String queryCargoList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        contractParam.setSortName("contract_date");
        contractParam.setSortOrder("desc");
        JSONObject result = tradeService.queryCargoListForQuery(contractParam);
        return result.toString();
    }
    @RequestMapping(value = "/queryContractList")
    public String queryContractList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        contractParam.setSortName("contract_date");
        contractParam.setSortOrder("desc");
        JSONObject result = tradeService.queryContractListByMapper(contractParam);
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
        result.put("total",contractBaseInfoMapper.selectCountByExample(contractParam));
        List<ContractBaseInfo> data = contractBaseInfoMapper.selectByExample(contractParam);

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
        List<ContractBaseInfo> data = contractBaseInfoMapper.selectByExample(contractParam);
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

    @RequestMapping(value = "/queryStoreInfoList")
    public String queryStoreInfoList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, ContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        JSONObject result = tradeService.queryStoreInfoList(contractParam);
        return result.toString();
    }

    @RequestMapping(value = "/query/getTotalStoreInfo")
    public String getTotalStoreInfo(ContractParam contractParam) throws JSONException {
        JSONObject result = tradeService.getTotalStoreInfoForQuery(contractParam);
        return result.toString();
    }

    @RequestMapping(value = "/queryCargoSellInfo")
    public String queryCargoSellInfo(@RequestParam("limit") int limit, @RequestParam("offset") int offset, CargoParam cargoParam) throws JSONException {
        cargoParam.setStart(offset);
        cargoParam.setLimit(limit);
        JSONObject result = tradeService.queryCargoSellInfo(cargoParam);
        return result.toString();
    }

    @RequestMapping(value = "/query/getTotalStoreOut")
    public String getTotalStoreOut(CargoParam cargoParam) throws JSONException {
        JSONObject result = tradeService.getTotalStoreOutForQuery(cargoParam);
        return result.toString();
    }

    @RequestMapping(value = "/queryCargoStoreInfo")
    public String queryCargoStoreInfo(@RequestParam("limit") int limit, @RequestParam("offset") int offset, CargoParam cargoParam) throws JSONException {
        cargoParam.setStart(offset);
        cargoParam.setLimit(limit);
        JSONObject result = tradeService.queryCargoStoreInfo(cargoParam);
        return result.toString();
    }

    @RequestMapping("/common/getOption")
    public String getCommonOption(@RequestParam("group") String group, @RequestParam("field") String field) throws JSONException {
        List<OptionInfo> data = cargoInfoMapper.findByGroupEqualsAndFieldEqualsOrderByNameAsc(group,field);
        JSONObject result = new JSONObject();
        result.put("data",data);
        return result.toString();
    }

    @RequestMapping("/common/getLevel")
    public String getLevel() throws JSONException {
        List<String> data = cargoInfoMapper.getLevelList();
        JSONObject result = new JSONObject();
        result.put("data",data);
        return result.toString();
    }

    @PostMapping(value="/presale/add")
    public String presaleAdd(PreSaleInfo saleInfo, HttpSession session){
        saleInfo.setStatus(GlobalConst.ENABLE);
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        if(saleInfo.getSaleId() == null) {//新增
            String now = DateUtil.DateTimeToString(new Date());
            saleInfo.setCreateUser(userInfo.getAccount());
            saleInfo.setCreateDateTime(now);
        }
        PreSaleInfo data = preSaleRepository.save(saleInfo);
        CargoInfo cargoInfo = new CargoInfo();
        String cargoId = saleInfo.getCargoId();
        CargoInfo cargoInfoTmp = cargoRepository.findByCargoId(cargoId);
        cargoInfo.setCargoId(cargoId);
        cargoInfo.setExpectStoreBoxes(GlobalConst.PRESALED);//1-已预售，0-未预售

        //获取已预售的重量
        List<PreSaleInfo> record = preSaleRepository.findByCargoId(cargoId);
        Double totalPreSaleWeight = record.stream().map(PreSaleInfo::getExpectSaleWeight).reduce(Double::sum).get();
        Double expectStoreWeight = cargoInfoTmp.getExpectStoreWeight() - totalPreSaleWeight;
        cargoInfo.setExpectStoreWeight(expectStoreWeight);//未预售重量
        cargoInfoMapper.updateByCargoId(cargoInfo);

        SysLog sysLog = new SysLog();
        sysLog.setDetail("新增预售记录"+data.getSaleId());
        sysLog.setOperation("新增");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return RespUtil.respSuccess(data);
    }

    @PostMapping(value="/presale/delete")
    @Transactional
    public String presaleDel(@RequestParam("ids") String ids, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        if(StringUtils.isNotBlank(ids)) {
            String[] arr = ids.split(",");
            List<String> saleIdList = Arrays.asList(arr);
            List<PreSaleInfo> record = preSaleRepository.findBySaleId(saleIdList);
            Double totalExpectSaleWeight = record.stream().map(PreSaleInfo::getExpectSaleWeight).reduce(Double::sum).get();

            //预售状态变更
            String cargoId = record.get(0).getCargoId();
            CargoInfo cargoInfo = cargoRepository.findByCargoId(cargoId);
            Double expectStoreWeight = cargoInfo.getExpectStoreWeight();
            Double contractAmount = cargoInfo.getContractAmount();

            cargoInfo.setExpectStoreWeight(expectStoreWeight + totalExpectSaleWeight);
            if(contractAmount == cargoInfo.getExpectStoreWeight()){
                cargoInfo.setExpectStoreBoxes(GlobalConst.UNPRESALED);//1-已预售，0-未预售
            }
            cargoInfoMapper.updateByCargoId(cargoInfo);

            preSaleRepository.deleteSaleInfo(saleIdList);
        }

        SysLog sysLog = new SysLog();
        sysLog.setDetail("删除预售记录"+ids);
        sysLog.setOperation("删除");
        sysLog.setUser(userInfo.getAccount());
        sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
        sysLogRepository.save(sysLog);
        return GlobalConst.SUCCESS;
    }

    @PostMapping(value = "/cargo/count")
    public String getCargoCount(CargoParam cargoParam) throws JSONException {
        Integer count = cargoInfoMapper.countCargoByContractId(cargoParam);
        JSONObject result = new JSONObject();
        result.put("count",count);
        return result.toString();
    }
}
