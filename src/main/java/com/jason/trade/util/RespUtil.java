package com.jason.trade.util;

import net.sf.json.JSONObject;

public class RespUtil {
    public static final String respSuccess(Object data){
        JSONObject result = new JSONObject();
        result.put("status","1");
        result.put("data",data);
        return result.toString();
    }
    public static final String respFailure(Object data){
        JSONObject result = new JSONObject();
        result.put("status","-1");
        result.put("data",data);
        return result.toString();
    }
}
