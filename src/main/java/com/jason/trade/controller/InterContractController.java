package com.jason.trade.controller;

import com.jason.trade.entity.InternalContractParam;
import com.jason.trade.mapper.CargoInfoMapper;
import com.jason.trade.model.CargoInfo;
import com.jason.trade.repository.CargoRepository;
import com.jason.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/trade")
@Slf4j
public class InterContractController {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private CargoInfoMapper cargoInfoMapper;
    @Autowired
    private CargoRepository cargoRepository;

    @RequestMapping(value = "/internal/list")
    public String getInternalTradeList(@RequestParam("limit") int limit, @RequestParam("offset") int offset, InternalContractParam contractParam) throws JSONException {
        contractParam.setStart(offset);
        contractParam.setLimit(limit);
        JSONObject result = tradeService.queryInternalContractListByMapper(contractParam);
        return result.toString();
    }
    @RequestMapping(value = "/internal/cargo/list")
    public String getInternalCargoList(@RequestParam("contractId") String contractId) throws JSONException {
        List<CargoInfo> list = cargoRepository.findByContractIdOrderByIdAsc(contractId);
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }

}
