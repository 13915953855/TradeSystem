package com.jason.trade.controller;

import com.jason.trade.entity.ContractBaseInfo;
import com.jason.trade.repository.ContractRepository;
import net.sf.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/trade")
public class TradeController {
    @Autowired
    private ContractRepository contractRepository;

    @RequestMapping(value = "/list")
    public String getTradeList(@RequestParam("limit") int limit,
                                   @RequestParam("offset") int offset,
                                   @RequestParam("tradeName") String tradeName) throws JSONException {
        List<ContractBaseInfo> list = contractRepository.findAll();
        JSONObject result = new JSONObject();
        result.put("total",list.size());
        result.put("rows",list);
        return result.toString();
    }
}
