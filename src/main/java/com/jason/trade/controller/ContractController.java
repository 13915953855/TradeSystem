package com.jason.trade.controller;

import com.jason.trade.constant.GlobalConst;
import com.jason.trade.entity.ContractParam;
import com.jason.trade.entity.InternalContractParam;
import com.jason.trade.mapper.AttachmentMapper;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.mapper.ContractBaseInfoMapper;
import com.jason.trade.mapper.InternalContractInfoMapper;
import com.jason.trade.repository.*;
import com.jason.trade.service.TradeService;
import com.jason.trade.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/trade")
@Slf4j
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private OptionInfoRepository optionInfoRepository;
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


}
