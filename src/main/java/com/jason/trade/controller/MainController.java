package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.model.CargoInfo;
import com.jason.trade.model.ContractBaseInfo;
import com.jason.trade.model.SysLog;
import com.jason.trade.model.UserInfo;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.repository.ContractRepository;
import com.jason.trade.repository.SysLogRepository;
import com.jason.trade.repository.UserRepository;
import com.jason.trade.service.ExcelService;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.WebSecurityConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private SysLogRepository sysLogRepository;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private ExcelService excelService;

    @GetMapping("/")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "index";
    }

    @GetMapping(value="/index")
    public String index(Model model, HttpSession session){
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
        return "trade/contract";
    }
    @GetMapping("/trade/contract/add")
    public String contractadd(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        String uuid = UUID.randomUUID().toString();
        model.addAttribute("contractId",uuid);
        return "trade/contractadd";
    }
    @GetMapping("/trade/contract/update")
    public String contractupdate(@RequestParam(value="id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findOne(id);
        model.addAttribute("contract",contract);
        model.addAttribute("action","update");
        return "trade/contractupdate";
    }
    @GetMapping("/trade/contract/view")
    public String contractview(@RequestParam(value="id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findOne(id);
        model.addAttribute("contract",contract);
        model.addAttribute("action","view");
        return "trade/contractupdate";
    }
    @GetMapping("/trade/contract/viewByEC")
    public String contractview(@RequestParam(value="externalContract") String externalContract, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findByExternalContractAndStatusNot(externalContract,GlobalConst.DISABLE);
        model.addAttribute("contract",contract);
        model.addAttribute("action","view");
        return "trade/contractupdate";
    }
    @GetMapping("/trade/cargomanage")
    public String inout(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/cargomanage";
    }
    @GetMapping("/trade/cargo/view")
    public String cargoview(@RequestParam(value="id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        CargoInfo cargoInfo = cargoRepository.findOne(id);
        model.addAttribute("cargo",cargoInfo);
        model.addAttribute("action","view");
        ContractBaseInfo contractBaseInfo = contractRepository.findByContractId(cargoInfo.getContractId());
        model.addAttribute("externalContract",contractBaseInfo.getExternalContract());
        return "trade/cargosaleview";
    }
    @GetMapping("/trade/agent")
    public String agent(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "trade/agent";
    }
    @PostMapping("/login")
    public @ResponseBody Map<String, Object> loginPost(String username, String password, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = userRepository.findByAccount(username);
        if(userInfo != null && userInfo.getPasswd().equals(password)){
            // 设置session
            session.setAttribute(WebSecurityConfig.SESSION_KEY, userInfo);
            map.put("status", "1");
            map.put("message", "登录成功");

            SysLog sysLog = new SysLog();
            sysLog.setDetail("");
            sysLog.setOperation("登录");
            sysLog.setUser(userInfo.getName());
            sysLog.setCreateDate(DateUtil.DateTimeToString(new Date()));
            sysLogRepository.save(sysLog);
            return map;
        }else{
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

    @PostMapping(value="/register")
    public @ResponseBody String register(UserInfo userInfo){
        userRepository.save(userInfo);
        return GlobalConst.SUCCESS;
    }

    @GetMapping(value="/trade/contract/output")
    public ResponseEntity<Resource> output(HttpSession session,@RequestParam(value="externalContract") String externalContract,
               @RequestParam(value="insideContract") String insideContract,@RequestParam(value="contractStartDate") String contractStartDate,
               @RequestParam(value="contractEndDate") String contractEndDate,@RequestParam(value="agent") String agent,
               @RequestParam(value="ladingbillNo") String ladingbillNo,@RequestParam(value="destinationPort") String destinationPort,
               @RequestParam(value="businessMode") String businessMode,@RequestParam(value="externalCompany") String externalCompany,
               @RequestParam(value="status") String status,@RequestParam(value="cargoName") String cargoName,@RequestParam(value="level") String level,
               @RequestParam(value="containerNo") String containerNo,@RequestParam(value="companyNo") String companyNo,@RequestParam(value="chk") String[] chk,
               @RequestParam(value="etaStartDate") String etaStartDate,@RequestParam(value="etaEndDate") String etaEndDate){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setExternalContract(externalContract);
        contractParam.setInsideContract(insideContract);
        contractParam.setContractStartDate(contractStartDate);
        contractParam.setContractEndDate(contractEndDate);
        contractParam.setLadingbillNo(ladingbillNo);
        contractParam.setDestinationPort(destinationPort);
        contractParam.setBusinessMode(businessMode);
        contractParam.setExternalCompany(externalCompany);
        contractParam.setStatus(status);
        contractParam.setContainerNo(containerNo);
        contractParam.setCompanyNo(companyNo);
        contractParam.setCargoName(cargoName);
        contractParam.setLevel(level);
        contractParam.setAgent(agent);
        contractParam.setLadingbillNo(ladingbillNo);
        contractParam.setEtaStartDate(etaStartDate);
        contractParam.setEtaEndDate(etaEndDate);

        ByteArrayOutputStream bos = null;
        List<ContractBaseInfo> data = tradeService.getContractBaseInfoList(contractParam);
        String fileName = "业务台账"+ DateUtil.DateToString(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            Workbook workbook = excelService.writeExcel(data,chk);
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
            sysLog.setUser(userInfo.getName());
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

}
