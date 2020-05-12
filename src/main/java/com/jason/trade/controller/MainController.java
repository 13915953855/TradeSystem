package com.jason.trade.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.*;
import com.jason.trade.mapper.AttachmentMapper;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.model.*;
import com.jason.trade.repository.*;
import com.jason.trade.service.ExcelService;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.CommonUtil;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.WebSecurityConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class MainController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private InternalContractRepository internalContractRepository;
    @Autowired
    private InternalCargoRepository internalCargoRepository;
    @Autowired
    private SysLogRepository sysLogRepository;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private ContractBaseInfoMapper contractBaseInfoMapper;

    @GetMapping("/")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "index";
    }

    @GetMapping(value = "/index")
    public String index(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/trade/contract")
    public String contract(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        model.addAttribute("type", "");
        return "trade/contract";
    }

    @GetMapping("/trade/internal")
    public String internal(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/internal";
    }

    @GetMapping("/trade/contract/add")
    public String contractadd(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        String uuid = UUID.randomUUID().toString();
        model.addAttribute("contractId", uuid);
        return "trade/contractadd";
    }

    @GetMapping("/trade/internal/contract/add")
    public String internalcontractadd(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        String uuid = "in_" + UUID.randomUUID().toString();
        model.addAttribute("contractId", uuid);
        return "trade/internaladd";
    }

    @GetMapping("/trade/contract/update")
    public String contractupdate(@RequestParam(value = "id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findOne(id);
        model.addAttribute("contract", contract);
        model.addAttribute("action", "update");
        return "trade/contractupdate";
    }

    @GetMapping("/trade/contract/view")
    public String contractview(@RequestParam(value = "id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findOne(id);
        model.addAttribute("contract", contract);
        model.addAttribute("action", "view");
        return "trade/contractupdate";
    }

    @GetMapping("/trade/internal/contract/update")
    public String internalcontractupdate(@RequestParam(value = "id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        InternalContractInfo contract = internalContractRepository.findOne(id);
        model.addAttribute("contract", contract);
        model.addAttribute("action", "update");
        return "trade/internalupdate";
    }

    @GetMapping("/trade/internal/contract/view")
    public String internalcontractview(@RequestParam(value = "id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        InternalContractInfo contract = internalContractRepository.findOne(id);
        model.addAttribute("contract", contract);
        model.addAttribute("action", "view");
        return "trade/internalupdate";
    }

    @GetMapping("/trade/contract/viewByEC")
    public String contractview(@RequestParam(value = "externalContract") String externalContract, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findByExternalContractAndStatusNot(externalContract, GlobalConst.DISABLE);
        if (contract != null) {
            model.addAttribute("contract", contract);
            model.addAttribute("action", "view");
            return "trade/contractupdate";
        } else {
            InternalContractInfo contract1 = internalContractRepository.findByContractNo(externalContract);
            model.addAttribute("contract", contract1);
            model.addAttribute("action", "view");
            return "trade/internalupdate";
        }

    }

    @GetMapping("/trade/cargomanage")
    public String inout(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/cargomanage";
    }

    @GetMapping("/trade/precargomanage")
    public String precargomanage(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/precargomanage";
    }

    @GetMapping("/trade/cargo/view")
    public String cargoview(@RequestParam(value = "id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        CargoInfo cargoInfo = cargoRepository.findOne(id);
        model.addAttribute("cargo", cargoInfo);
        model.addAttribute("action", "view");
        ContractBaseInfo contractBaseInfo = contractRepository.findByContractId(cargoInfo.getContractId());
        if (contractBaseInfo != null) {
            model.addAttribute("externalContract", contractBaseInfo.getExternalContract());
            model.addAttribute("ownerCompany", contractBaseInfo.getOwnerCompany());
            model.addAttribute("storageCondition", contractBaseInfo.getStorageCondition());
            model.addAttribute("from", "jinkou");
            model.addAttribute("type", "进口台账");
            model.addAttribute("containerNo", contractBaseInfo.getContainerNo());
            model.addAttribute("warehouse", contractBaseInfo.getWarehouse());
        } else {
            InternalContractInfo internalContractInfo = internalContractRepository.findByContractId(cargoInfo.getContractId());
            model.addAttribute("externalContract", internalContractInfo.getContractNo());
            model.addAttribute("ownerCompany", internalContractInfo.getOwnerCompany());
            model.addAttribute("storageCondition", "");
            model.addAttribute("from", "neimao");
            model.addAttribute("type", "内贸台账");
            model.addAttribute("containerNo", contractBaseInfo.getContainerNo());
            model.addAttribute("warehouse", contractBaseInfo.getWarehouse());
        }
        return "trade/cargosaleview";
    }

    @GetMapping("/trade/cargo/presale")
    public String cargopresale(@RequestParam(value = "id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        CargoInfo cargoInfo = cargoRepository.findOne(id);
        model.addAttribute("cargo", cargoInfo);
        model.addAttribute("action", "view");
        Double contractAmount = cargoInfo.getContractAmount();
        Double expectStoreWeight = cargoInfo.getExpectStoreWeight();
        model.addAttribute("yysWeight", contractAmount - expectStoreWeight);
        model.addAttribute("wysWeight", expectStoreWeight);
        ContractBaseInfo contractBaseInfo = contractRepository.findByContractId(cargoInfo.getContractId());
        if (contractBaseInfo != null) {
            model.addAttribute("externalContract", contractBaseInfo.getExternalContract());
            model.addAttribute("ownerCompany", contractBaseInfo.getOwnerCompany());
            model.addAttribute("from", "jinkou");
            model.addAttribute("type", "进口台账");
            model.addAttribute("containerNo", contractBaseInfo.getContainerNo());
            model.addAttribute("warehouse", contractBaseInfo.getWarehouse());
        } else {
            InternalContractInfo internalContractInfo = internalContractRepository.findByContractId(cargoInfo.getContractId());
            model.addAttribute("externalContract", internalContractInfo.getContractNo());
            model.addAttribute("ownerCompany", internalContractInfo.getOwnerCompany());
            model.addAttribute("from", "neimao");
            model.addAttribute("type", "内贸台账");
            model.addAttribute("containerNo", contractBaseInfo.getContainerNo());
            model.addAttribute("warehouse", contractBaseInfo.getWarehouse());
        }
        return "trade/cargopresaleview";
    }

    @GetMapping("/trade/agent")
    public String agent(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/agent";
    }

    @PostMapping("/login")
    public @ResponseBody
    Map<String, Object> loginPost(String username, String password, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = userRepository.findByAccountAndStatus(username, "1");
        if (userInfo != null && userInfo.getPasswd().equals(password)) {
            // 设置session
            session.setAttribute(WebSecurityConfig.SESSION_KEY, userInfo);
            map.put("status", "1");
            map.put("message", "登录成功");

            SysLog sysLog = new SysLog();
            sysLog.setDetail("");
            sysLog.setOperation("登录");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);
            return map;
        } else {
            map.put("status", "-1");
            map.put("message", "密码错误");
            return map;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping(value = "/register")
    public @ResponseBody
    String register(UserInfo userInfo) {
        userRepository.save(userInfo);
        return GlobalConst.SUCCESS;
    }

    @GetMapping(value = "/trade/contract/output")
    public ResponseEntity<Resource> output(HttpSession session, @RequestParam(value = "externalContract") String externalContract,
                                           @RequestParam(value = "insideContract") String insideContract, @RequestParam(value = "contractStartDate") String contractStartDate,
                                           @RequestParam(value = "caiyangdateStart") String caiyangdateStart, @RequestParam(value = "caiyangdateEnd") String caiyangdateEnd, @RequestParam(value = "caiyangcangku") String caiyangcangku,
                                           @RequestParam(value = "contractEndDate") String contractEndDate, @RequestParam(value = "agent") String agent, @RequestParam(value = "ownerCompany") String ownerCompany,
                                           @RequestParam(value = "ladingbillNo") String ladingbillNo, @RequestParam(value = "destinationPort") String destinationPort,
                                           @RequestParam(value = "businessMode") String businessMode, @RequestParam(value = "externalCompany") String externalCompany,
                                           @RequestParam(value = "status") String status, @RequestParam(value = "cargoName") String cargoName, @RequestParam(value = "level") String level, @RequestParam(value = "cmpRel") String cmpRel,
                                           @RequestParam(value = "containerNo") String containerNo, @RequestParam(value = "companyNo") String companyNo, @RequestParam(value = "chk") String[] chk,
                                           @RequestParam(value = "etdStartDate") String etdStartDate, @RequestParam(value = "etdEndDate") String etdEndDate,
                                           @RequestParam(value = "etaStartDate") String etaStartDate, @RequestParam(value = "etaEndDate") String etaEndDate, @RequestParam(value = "originCountry") String originCountry,
                                           @RequestParam(value = "storeStartDate") String storeStartDate, @RequestParam(value = "storeEndDate") String storeEndDate,
                                           @RequestParam(value = "taxPayDateStart") String taxPayDateStart, @RequestParam(value = "taxPayDateEnd") String taxPayDateEnd,
                                           @RequestParam(value = "cargoType") String cargoType, @RequestParam(value = "storageCondition") String storageCondition) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setExternalContract(externalContract);
        contractParam.setCmpRel(cmpRel);
        contractParam.setCaiyangcangku(caiyangcangku);
        contractParam.setCaiyangdateStart(caiyangdateStart);
        contractParam.setCaiyangdateEnd(caiyangdateEnd);
        contractParam.setInsideContract(insideContract);
        contractParam.setContractStartDate(contractStartDate);
        contractParam.setContractEndDate(contractEndDate);
        contractParam.setCargoType(URLDecoder.decode(cargoType, "UTF-8"));
        contractParam.setLadingbillNo(ladingbillNo);
        contractParam.setDestinationPort(URLDecoder.decode(destinationPort, "UTF-8"));
        contractParam.setBusinessMode(URLDecoder.decode(businessMode, "UTF-8"));
        contractParam.setTaxPayDateStart(taxPayDateStart);
        contractParam.setOriginCountry(originCountry);
        contractParam.setTaxPayDateEnd(taxPayDateEnd);
        contractParam.setStoreStartDate(storeStartDate);
        contractParam.setStoreEndDate(storeEndDate);
        contractParam.setExternalCompany(URLDecoder.decode(externalCompany, "UTF-8"));
        contractParam.setStatus(CommonUtil.revertStatus(URLDecoder.decode(status, "UTF-8")));
        contractParam.setStorageCondition(URLDecoder.decode(storageCondition, "UTF-8"));
        contractParam.setContainerNo(containerNo);
        contractParam.setCompanyNo(URLDecoder.decode(companyNo, "UTF-8"));
        contractParam.setCargoName(URLDecoder.decode(cargoName, "UTF-8"));
        contractParam.setLevel(URLDecoder.decode(level, "UTF-8"));
        contractParam.setAgent(URLDecoder.decode(agent, "UTF-8"));
        contractParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        contractParam.setEtaStartDate(etaStartDate);
        contractParam.setEtaEndDate(etaEndDate);
        contractParam.setEtdStartDate(etdStartDate);
        contractParam.setEtdEndDate(etdEndDate);
        contractParam.setSortName("contract_date");
        contractParam.setSortOrder("desc");
        ByteArrayOutputStream bos = null;
        List<ContractBaseInfo> data = contractBaseInfoMapper.selectByExample(contractParam);
        String fileName = "业务台账" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeExcel(data, chk);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    @GetMapping(value = "/trade/attachment/download")
    public void downloadAttachment(HttpServletResponse res, HttpSession session, @RequestParam(value = "id") Integer id, @RequestParam(value = "contractId") String contractId) {
        AttachmentKey key = new AttachmentKey();
        key.setContractId(contractId);
        key.setId(id);
        Attachment attachment = attachmentMapper.selectByPrimaryKey(key);
        String fileName = attachment.getFileName();
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setCharacterEncoding("utf-8");
        res.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(attachment.getFilePath())));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/trade/query/contract")
    public String queryContract(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryContract";
    }

    @GetMapping("/trade/query/cargo")
    public String queryCargo(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryCargo";
    }

    @GetMapping("/trade/query/duty")
    public String queryDuty(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryduty";
    }

    @GetMapping("/trade/query/storeInfo")
    public String queryStoreInfo(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryStoreInfo";
    }

    @GetMapping("/trade/query/storeIn")
    public String queryStoreIn(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryStoreIn";
    }

    @GetMapping("/trade/query/storeOut")
    public String queryStoreOut(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryStoreOut";
    }

    @GetMapping("/trade/query/baoguan")
    public String queryBaoguan(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryBaoguan";
    }

    @GetMapping("/trade/query/fuhui")
    public String queryFuhui(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryFuhui";
    }

    @GetMapping("/trade/query/fuhuiRZ")
    public String queryFuhuiRZ(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryFuhuiRZ";
    }

    @GetMapping("/trade/query/fuhuiKZ")
    public String queryFuhuiKZ(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/queryFuhuiKZ";
    }

    @GetMapping(value = "/trade/queryCargo/output")
    public ResponseEntity<Resource> queryCargoOutput(HttpSession session, @RequestParam(value = "externalCompany") String externalCompany, @RequestParam(value = "originCountry") String originCountry,
                                                     @RequestParam(value = "businessMode") String businessMode, @RequestParam(value = "companyNo") String companyNo, @RequestParam(value = "currency") String currency,
                                                     @RequestParam(value = "level") String level, @RequestParam(value = "cargoName") String cargoName, @RequestParam(value = "ownerCompany") String ownerCompany,
                                                     @RequestParam(value = "contractEndDate") String contractEndDate, @RequestParam(value = "contractStartDate") String contractStartDate, @RequestParam(value = "cargoType") String cargoType,
                                                     @RequestParam(value = "endDate") String endDate, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "status") String status,
                                                     @RequestParam(value = "etdStartDate") String etdStartDate, @RequestParam(value = "etdEndDate") String etdEndDate, @RequestParam(value = "destinationPort") String destinationPort,
                                                     @RequestParam(value = "etaStartDate") String etaStartDate, @RequestParam(value = "etaEndDate") String etaEndDate,
                                                     @RequestParam(value = "externalContract") String externalContract, @RequestParam(value = "insideContract") String insideContract,
                                                     @RequestParam(value = "minBox") Double minBox, @RequestParam(value = "maxBox") Double maxBox, @RequestParam(value = "cmpRel") String cmpRel,
                                                     @RequestParam(value = "storageCondition") String storageCondition) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setOriginCountry(URLDecoder.decode(originCountry, "UTF-8"));
        contractParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        contractParam.setDestinationPort(URLDecoder.decode(destinationPort, "UTF-8"));
        contractParam.setCurrency(currency);
        contractParam.setMinBox(minBox);
        contractParam.setCargoType(cargoType);
        contractParam.setMaxBox(maxBox);
        contractParam.setExternalContract(externalContract);
        contractParam.setInsideContract(insideContract);
        contractParam.setStorageCondition(storageCondition);
        contractParam.setContractStartDate(contractStartDate);
        contractParam.setContractEndDate(contractEndDate);
        contractParam.setBusinessMode(URLDecoder.decode(businessMode, "UTF-8"));
        contractParam.setExternalCompany(URLDecoder.decode(externalCompany, "UTF-8"));
        contractParam.setCompanyNo(companyNo);
        contractParam.setCargoName(URLDecoder.decode(cargoName, "UTF-8"));
        contractParam.setLevel(URLDecoder.decode(level, "UTF-8"));
        contractParam.setStatus(CommonUtil.revertStatus(URLDecoder.decode(status, "UTF-8")));
        contractParam.setEtaStartDate(etaStartDate);
        contractParam.setEtaEndDate(etaEndDate);
        contractParam.setEtdStartDate(etdStartDate);
        contractParam.setEtdEndDate(etdEndDate);
        contractParam.setStartDate(startDate);
        contractParam.setEndDate(endDate);
        contractParam.setCmpRel(cmpRel);

        ByteArrayOutputStream bos = null;
        List<QueryContractInfo> data = contractBaseInfoMapper.queryCargoList(contractParam);
        String fileName = "订单数据统计商品" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeCargoExcel(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 库存信息
     *
     * @param session
     * @param externalCompany
     * @param businessMode
     * @param companyNo
     * @param level
     * @param cargoName
     * @param status
     * @return
     */
    @GetMapping(value = "/trade/queryStoreInfo/output")
    public ResponseEntity<Resource> queryStoreInfoOutput(HttpSession session, @RequestParam(value = "externalCompany") String externalCompany, @RequestParam(value = "ownerCompany") String ownerCompany,
                                                         @RequestParam(value = "businessMode") String businessMode, @RequestParam(value = "companyNo") String companyNo,@RequestParam(value = "cmpRel") String cmpRel,
                                                         @RequestParam(value = "level") String level, @RequestParam(value = "cargoName") String cargoName, @RequestParam(value = "storageCondition") String storageCondition,
                                                         @RequestParam(value = "storeStartDate") String storeStartDate, @RequestParam(value = "storeEndDate") String storeEndDate,
                                                         @RequestParam(value = "status") String status, @RequestParam(value = "originCountry") String originCountry,
                                                         @RequestParam(value = "cargoType") String cargoType, @RequestParam(value = "warehouse") String warehouse, @RequestParam(value = "containerNo") String containerNo,
                                                         @RequestParam(value = "minBox") Double minBox, @RequestParam(value = "maxBox") Double maxBox,
                                                         @RequestParam(value = "minWeight") Double minWeight, @RequestParam(value = "maxWeight") Double maxWeight) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setStoreStartDate(storeStartDate);
        contractParam.setCargoType(cargoType);
        contractParam.setStorageCondition(URLDecoder.decode(storageCondition, "UTF-8"));
        contractParam.setStoreEndDate(storeEndDate);
        contractParam.setBusinessMode(URLDecoder.decode(businessMode, "UTF-8"));
        contractParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        contractParam.setExternalCompany(URLDecoder.decode(externalCompany, "UTF-8"));
        contractParam.setCompanyNo(companyNo);
        contractParam.setCargoName(URLDecoder.decode(cargoName, "UTF-8"));
        contractParam.setOriginCountry(URLDecoder.decode(originCountry, "UTF-8"));
        contractParam.setWarehouse(URLDecoder.decode(warehouse, "UTF-8"));
        contractParam.setContainerNo(containerNo);
        contractParam.setMinBox(minBox);
        contractParam.setMaxBox(maxBox);
        contractParam.setMinWeight(minWeight);
        contractParam.setCmpRel(cmpRel);
        contractParam.setMaxWeight(maxWeight);
        contractParam.setLevel(level);
        contractParam.setStatus(CommonUtil.revertStatus(URLDecoder.decode(status, "UTF-8")));

        ByteArrayOutputStream bos = null;
        List<QueryContractInfo> data = contractBaseInfoMapper.queryStoreInfoListByExample(contractParam);
        String fileName = "库存信息" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeStoreInfoExcel(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    @Autowired
    private ApplicationContext applicationContext;


    @ResponseBody
    @RequestMapping("/testListener")
    public String testListener(){
        applicationContext.publishEvent(new AddDateEvent("aaa"));
        return "success";
    }

    /**
     * 入库信息导出
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping(value = "/trade/queryStoreIn/output")
    public ResponseEntity<Resource> queryStoreIn(HttpSession session, @RequestParam(value = "contractNo") String contractNo,
                                                 @RequestParam(value = "storeStartDate") String storeStartDate,
                                                 @RequestParam(value = "storeEndDate") String storeEndDate,
                                                 @RequestParam(value = "etaEndDate") String etaEndDate,
                                                 @RequestParam(value = "etaStartDate") String etaStartDate,
                                                 @RequestParam(value = "insideContract") String insideContract,
                                                 @RequestParam(value = "cargoNo") String cargoNo,
                                                 @RequestParam(value = "cargoType") String cargoType,
                                                 @RequestParam(value = "containerNo") String containerNo,
                                                 @RequestParam(value = "ladingbillNo") String ladingbillNo,
                                                 @RequestParam(value = "warehouse") String warehouse,
                                                 @RequestParam(value = "ownerCompany") String ownerCompany,
                                                 @RequestParam(value = "level") String level,
                                                 @RequestParam(value = "cargoName") String cargoName,
                                                 @RequestParam(value = "storageCondition") String storageCondition,
                                                 @RequestParam(value = "status") String status) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        CargoParam cargoParam = new CargoParam();
        cargoParam.setContractNo(contractNo);
        cargoParam.setInsideContract(insideContract);
        cargoParam.setLevel(URLDecoder.decode(level, "UTF-8"));
        cargoParam.setCargoName(URLDecoder.decode(cargoName, "UTF-8"));
        cargoParam.setCargoNo(cargoNo);
        cargoParam.setCargoType(cargoType);
        cargoParam.setWarehouse(URLDecoder.decode(warehouse, "UTF-8"));
        cargoParam.setStoreStartDate(storeStartDate);
        cargoParam.setStoreEndDate(storeEndDate);
        cargoParam.setEtaEndDate(etaEndDate);
        cargoParam.setEtdStartDate(etaStartDate);
        cargoParam.setContainerNo(containerNo);
        cargoParam.setLadingbillNo(ladingbillNo);
        cargoParam.setStatus(CommonUtil.revertStatus(URLDecoder.decode(status, "UTF-8")));
        cargoParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        cargoParam.setStorageCondition(storageCondition);

        ByteArrayOutputStream bos = null;
        List<CargoStoreInfo> data = cargoInfoMapper.getStoreList2(cargoParam);
        String fileName = "入库信息" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeStoreInExcel(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    @GetMapping(value = "/trade/queryStoreOut/output")
    public ResponseEntity<Resource> queryStoreOut(HttpSession session, @RequestParam(value = "contractNo") String contractNo,
                                                 @RequestParam(value = "realSaleDateStart") String realSaleDateStart,
                                                 @RequestParam(value = "realSaleDateEnd") String realSaleDateEnd,
                                                 @RequestParam(value = "pickupDateStart") String pickupDateStart,
                                                 @RequestParam(value = "pickupDateEnd") String pickupDateEnd,
                                                 @RequestParam(value = "insideContract") String insideContract,
                                                 @RequestParam(value = "cargoNo") String cargoNo,
                                                 @RequestParam(value = "cargoName") String cargoName,
                                                 @RequestParam(value = "cargoType") String cargoType,
                                                 @RequestParam(value = "containerNo") String containerNo,
                                                 @RequestParam(value = "ladingbillNo") String ladingbillNo,
                                                 @RequestParam(value = "customerName") String customerName,
                                                 @RequestParam(value = "customerType") String customerType,
                                                 @RequestParam(value = "maxBox") String maxBox,
                                                 @RequestParam(value = "minBox") String minBox,
                                                 @RequestParam(value = "warehouse") String warehouse,
                                                 @RequestParam(value = "ownerCompany") String ownerCompany,
                                                 @RequestParam(value = "externalCompany") String externalCompany,
                                                 @RequestParam(value = "companyNo") String companyNo,
                                                 @RequestParam(value = "cmpRel") String cmpRel,
                                                 @RequestParam(value = "level") String level,
                                                 @RequestParam(value = "storageCondition") String storageCondition,
                                                 @RequestParam(value = "kaifapiao") String kaifapiao,
                                                 @RequestParam(value = "businessMode") String businessMode,
                                                 @RequestParam(value = "status") String status) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        CargoParam cargoParam = new CargoParam();
        cargoParam.setContractNo(contractNo);
        cargoParam.setInsideContract(insideContract);
        cargoParam.setKaifapiao(kaifapiao);
        cargoParam.setBusinessMode(businessMode);
        cargoParam.setMinBox(minBox);
        cargoParam.setExternalCompany(externalCompany);
        cargoParam.setCmpRel(cmpRel);
        cargoParam.setCompanyNo(companyNo);
        cargoParam.setMaxBox(maxBox);
        cargoParam.setRealSaleDateEnd(realSaleDateEnd);
        cargoParam.setRealSaleDateStart(realSaleDateStart);
        cargoParam.setPickupDateEnd(pickupDateEnd);
        cargoParam.setPickupDateStart(pickupDateStart);
        cargoParam.setCustomerName(customerName);
        cargoParam.setLevel(URLDecoder.decode(level, "UTF-8"));
        cargoParam.setCargoName(URLDecoder.decode(cargoName, "UTF-8"));
        cargoParam.setCargoNo(cargoNo);
        cargoParam.setCargoType(cargoType);
        cargoParam.setCustomerType(customerType);
        cargoParam.setWarehouse(URLDecoder.decode(warehouse, "UTF-8"));
        cargoParam.setContainerNo(containerNo);
        cargoParam.setLadingbillNo(ladingbillNo);
        cargoParam.setStatus(CommonUtil.revertStatus(URLDecoder.decode(status, "UTF-8")));
        cargoParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        cargoParam.setStorageCondition(storageCondition);

        ByteArrayOutputStream bos = null;
        List<CargoSellInfo> data = cargoInfoMapper.getSellList(cargoParam);
        String fileName = "出库信息" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeStoreOutExcel(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }


    @Autowired
    private CargoInfoMapper cargoInfoMapper;

    @GetMapping(value = "/trade/queryDuty/output")
    public ResponseEntity<Resource> queryDutyOutput(HttpSession session, @RequestParam(value = "externalCompany") String externalCompany, @RequestParam(value = "ownerCompany") String ownerCompany,
                                                    @RequestParam(value = "etaStartDate") String etaStartDate, @RequestParam(value = "etaEndDate") String etaEndDate,
                                                    @RequestParam(value = "cargoType") String cargoType, @RequestParam(value = "agent") String agent,
                                                    @RequestParam(value = "originCountry") String originCountry, @RequestParam(value = "cmpRel") String cmpRel,
                                                    @RequestParam(value = "taxPayDateStart") String taxPayDateStart, @RequestParam(value = "taxPayDateEnd") String taxPayDateEnd) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setTaxPayDateStart(taxPayDateStart);
        contractParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        contractParam.setTaxPayDateEnd(taxPayDateEnd);
        contractParam.setEtaStartDate(etaStartDate);
        contractParam.setEtaEndDate(etaEndDate);
        contractParam.setAgent(agent);
        contractParam.setCmpRel(cmpRel);
        contractParam.setOriginCountry(originCountry);
        contractParam.setCargoType(cargoType);
        contractParam.setExternalCompany(URLDecoder.decode(externalCompany, "UTF-8"));

        ByteArrayOutputStream bos = null;
        List<ContractBaseInfo> data = contractBaseInfoMapper.selectByExample(contractParam);
        String fileName = "付税信息" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeDutyExcel(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    @GetMapping(value = "/trade/queryContract/output")
    public ResponseEntity<Resource> queryContractOutput(HttpSession session, @RequestParam(value = "externalCompany") String externalCompany, @RequestParam(value = "originCountry") String originCountry,
                                                        @RequestParam(value = "ownerCompany") String ownerCompany, @RequestParam(value = "status") String status, @RequestParam(value = "currency") String currency,
                                                        @RequestParam(value = "contractEndDate") String contractEndDate, @RequestParam(value = "contractStartDate") String contractStartDate, @RequestParam(value = "cargoType") String cargoType,
                                                        @RequestParam(value = "insideContract") String insideContract, @RequestParam(value = "externalContract") String externalContract, @RequestParam(value = "cmpRel") String cmpRel,
                                                        @RequestParam(value = "expectSailingDateEnd") String expectSailingDateEnd, @RequestParam(value = "expectSailingDateStart") String expectSailingDateStart, @RequestParam(value = "storageCondition") String storageCondition,
                                                        @RequestParam(value = "etdStartDate") String etdStartDate, @RequestParam(value = "etdEndDate") String etdEndDate, @RequestParam(value = "destinationPort") String destinationPort,
                                                        @RequestParam(value = "etaStartDate") String etaStartDate, @RequestParam(value = "etaEndDate") String etaEndDate) throws UnsupportedEncodingException {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setStatus(CommonUtil.revertStatus(URLDecoder.decode(status, "UTF-8")));
        contractParam.setOriginCountry(URLDecoder.decode(originCountry, "UTF-8"));
        contractParam.setCurrency(currency);
        contractParam.setCmpRel(cmpRel);
        contractParam.setCargoType(cargoType);
        contractParam.setExternalContract(externalContract);
        contractParam.setInsideContract(insideContract);
        contractParam.setDestinationPort(URLDecoder.decode(destinationPort, "UTF-8"));
        contractParam.setOwnerCompany(URLDecoder.decode(ownerCompany, "UTF-8"));
        contractParam.setStorageCondition(URLDecoder.decode(storageCondition, "UTF-8"));
        contractParam.setContractStartDate(contractStartDate);
        contractParam.setContractEndDate(contractEndDate);
        contractParam.setExternalCompany(URLDecoder.decode(externalCompany, "UTF-8"));
        contractParam.setEtaStartDate(etaStartDate);
        contractParam.setEtaEndDate(etaEndDate);
        contractParam.setEtdStartDate(etdStartDate);
        contractParam.setEtdEndDate(etdEndDate);
        contractParam.setExpectSailingDateStart(expectSailingDateStart);
        contractParam.setExpectSailingDateEnd(expectSailingDateEnd);

        ByteArrayOutputStream bos = null;
        List<ContractBaseInfo> data = contractBaseInfoMapper.selectByExample(contractParam);
        String fileName = "订单数据统计台账" + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            Workbook workbook = excelService.writeContractExcel(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");


            SysLog sysLog = new SysLog();
            sysLog.setDetail("导出Excel记录");
            sysLog.setOperation("导出");
            sysLog.setUser(userInfo.getAccount());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);


            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    //进口核算表
    @GetMapping(value = "/trade/contract/hesuan")
    public ResponseEntity<Resource> hesuan(@RequestParam(value = "id") Integer id) {
        ByteArrayOutputStream bos = null;
        ContractBaseInfo data = contractRepository.findById(id);
        String fileName = "进口核算表.xlsx";
        try {
            Workbook workbook = excelService.hesuan(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);
        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    //入库单
    @GetMapping(value = "/trade/contract/rukudan")
    public ResponseEntity<Resource> rukudan(@RequestParam(value = "id") Integer id) {
        ByteArrayOutputStream bos = null;
        ContractBaseInfo data = contractRepository.findById(id);
        String fileName = "入库申请单.xlsx";
        try {
            Workbook workbook = excelService.ruku(data);
            bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            fileName = URLEncoder.encode(fileName, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);
        } catch (IOException e) {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }
}
