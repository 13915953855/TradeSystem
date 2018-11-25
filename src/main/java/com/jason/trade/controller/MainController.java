package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.entity.QueryContractInfo;
import com.jason.trade.mapper.AttachmentMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.model.*;
import com.jason.trade.repository.*;
import com.jason.trade.service.ExcelService;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.DateUtil;
import com.jason.trade.util.WebSecurityConfig;
import net.sf.json.JSONObject;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
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
        model.addAttribute("type","");
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
        model.addAttribute("contractId",uuid);
        return "trade/contractadd";
    }
    @GetMapping("/trade/internal/contract/add")
    public String internalcontractadd(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        String uuid = "in_"+UUID.randomUUID().toString();
        model.addAttribute("contractId",uuid);
        return "trade/internaladd";
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
    @GetMapping("/trade/internal/contract/update")
    public String internalcontractupdate(@RequestParam(value="id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        InternalContractInfo contract = internalContractRepository.findOne(id);
        model.addAttribute("contract",contract);
        model.addAttribute("action","update");
        return "trade/internalupdate";
    }
    @GetMapping("/trade/internal/contract/view")
    public String internalcontractview(@RequestParam(value="id") Integer id, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        InternalContractInfo contract = internalContractRepository.findOne(id);
        model.addAttribute("contract",contract);
        model.addAttribute("action","view");
        return "trade/internalupdate";
    }

    @GetMapping("/trade/contract/viewByEC")
    public String contractview(@RequestParam(value="externalContract") String externalContract, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        ContractBaseInfo contract = contractRepository.findByExternalContractAndStatusNot(externalContract, GlobalConst.DISABLE);
        if(contract != null){
            model.addAttribute("contract", contract);
            model.addAttribute("action","view");
            return "trade/contractupdate";
        }else{
            InternalContractInfo contract1 = internalContractRepository.findByContractNo(externalContract);
            model.addAttribute("contract", contract1);
            model.addAttribute("action","view");
            return "trade/internalupdate";
        }

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
        if(contractBaseInfo != null) {
            model.addAttribute("externalContract", contractBaseInfo.getExternalContract());
            model.addAttribute("ownerCompany", contractBaseInfo.getOwnerCompany());
            model.addAttribute("from","jinkou");
            model.addAttribute("type","进口台账");
        }else{
            InternalContractInfo internalContractInfo = internalContractRepository.findByContractId(cargoInfo.getContractId());
            model.addAttribute("externalContract", internalContractInfo.getContractNo());
            model.addAttribute("ownerCompany", internalContractInfo.getOwnerCompany());
            model.addAttribute("from","neimao");
            model.addAttribute("type","内贸台账");
        }
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
            sysLog.setUser(userInfo.getAccount());
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

   /* @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping(value="/register")
    public @ResponseBody String register(UserInfo userInfo){
        userRepository.save(userInfo);
        return GlobalConst.SUCCESS;
    }*/

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
        contractParam.setSortName("contract_date");
        contractParam.setSortOrder("desc");
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

    @GetMapping(value="/trade/attachment/download")
    public void downloadAttachment(HttpServletResponse res,HttpSession session,@RequestParam(value="id") Integer id,@RequestParam(value="contractId") String contractId){
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

    /*@PostMapping("/trade/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        String name = request.getParameter("name");// 文件名称
        String type = name.substring(name.lastIndexOf(".") + 1);// 文件类型
        name = DateUtil.DateToString(new Date(),"yyyyMMddHHmmss") + "." + type;// 重新定义图片名称，DateTools.getDateAndTime() 工具类，产生时间戳短码，可以自己根据需求重新定义

        //String path = GlobalConst.FILE_PATH;//这里保存图片路径 D:\ 这样
        String path = "D:";
        path = path + File.separator + name;// 拼接路径
        File uploadFile = new File(path);// 路径文件，一定要有文件夹，没有则创建，mkdir
        ServletInputStream inputStream = null;// ***获取字节流，图片保存在这里,切记这里只可以获取一次。***
        FileOutputStream outputStream = null;
        try {
            inputStream = request.getInputStream();
            outputStream = new FileOutputStream(uploadFile);
            FileCopyUtils.copy(inputStream, outputStream);// 复制图片
        } catch (IOException e) {
           log.error("获取字节流失败",e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭字节流失败",e);
            }
        }

        String realPath= uploadFile.getPath();//realPath 为图片真路径
        // 格式 ： http://192.1.1.1/xxxx/name 类似这样在公网显示
        JSONObject json = new JSONObject();
        json.put("path", realPath);// 引用路径
        json.put("flag", "success");// 标识
        //response(response, json);// 保存图片完成，返回前台进行回显
    }*/

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

    @GetMapping(value="/trade/queryCargo/output")
    public ResponseEntity<Resource> queryCargoOutput(HttpSession session,@RequestParam(value="externalCompany") String externalCompany,
                                           @RequestParam(value="businessMode") String businessMode,@RequestParam(value="companyNo") String companyNo,
                                           @RequestParam(value="level") String level,@RequestParam(value="cargoName") String cargoName,
                                           @RequestParam(value="contractEndDate") String contractEndDate,@RequestParam(value="contractStartDate") String contractStartDate,
                                           @RequestParam(value="endDate") String endDate,@RequestParam(value="startDate") String startDate,
                                           @RequestParam(value="etdStartDate") String etdStartDate,@RequestParam(value="etdEndDate") String etdEndDate,
                                           @RequestParam(value="etaStartDate") String etaStartDate,@RequestParam(value="etaEndDate") String etaEndDate){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        ContractParam contractParam = new ContractParam();
        contractParam.setContractStartDate(contractStartDate);
        contractParam.setContractEndDate(contractEndDate);
        contractParam.setBusinessMode(businessMode);
        contractParam.setExternalCompany(externalCompany);
        contractParam.setCompanyNo(companyNo);
        contractParam.setCargoName(cargoName);
        contractParam.setLevel(level);
        contractParam.setEtaStartDate(etaStartDate);
        contractParam.setEtaEndDate(etaEndDate);
        contractParam.setEtdStartDate(etdStartDate);
        contractParam.setEtdEndDate(etdEndDate);
        contractParam.setStartDate(startDate);
        contractParam.setEndDate(endDate);

        ByteArrayOutputStream bos = null;
        List<QueryContractInfo> data = contractBaseInfoMapper.queryCargoListByExample(contractParam);
        String fileName = "订单数据统计商品"+ DateUtil.DateToString(new Date(),"yyyyMMddHHmmss")+".xlsx";
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
}
