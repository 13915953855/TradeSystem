package com.jason.trade.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobalConst {
    public static Map<String,Object> globalElement = new HashMap<>();
    public static final String SUCCESS = "{\"status\":1}";
    public static final String FAILURE = "{\"status\":-1}";
    public static final String ENABLE = "1";
    public static final String DISABLE = "0";
    public static final String[] FIRST_HEAD_ARRAY  = new String[]{"","采购信息","","","","","","","","","","","","","","","","预付款","","","","尾款","","","","","","","","","","","船期","","外商单据","","","","","柜号","提单号","报关报检","","","","税款","","","","","入库","","备注"};
    public static final short[] FIRST_HEAD_COLOR  = new short[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,44,44,44,44,44,44,44,44,44,44,44,44,44,47,47,47,47,47,47,47,47,47,47,47,47,47,47,48,44,44,44,44,44,44,44,1};
    public static final String[] SECOND_HEAD_ARRAY = new String[]{"序号","合同日期","外商","外合同","内合同","业务模式","厂号","库号","产品","规格","原产地","价格条件","起运港","目的港","数量(kg)","单价(USD)","合同金额(USD)","付款金额(USD)","付款日期","汇率","小计(CNY)","付款金额","付款日期","汇率","小计(CNY)","销售客户","来款金额","来款日期","尾款金额","来款日期","发票数量(kg)","发票金额(USD)","ETD","ETA","电子版发送日期","是否已核对电子版","保险购买日期","寄出日期","签收日期","柜号","提单号","货代","单据寄给货代日期","放行日期","税票抵扣方","关税","增值税","滞报费","付税日期","税票签收日期","仓库","入库日期"};
    public static final short[] SECOND_HEAD_COLOR  = new short[]{1,1,1,1,1,1,1,1,1,31,31,31,31,31,31,31,31,44,44,44,44,44,44,44,44,44,44,44,44,44,47,47,47,47,47,47,47,47,47,47,47,47,47,47,48,44,44,44,44,44,44,44,1};
    public static final short[] BODY_COLOR  = new short[]{1,1,1,1,1,1,1,1,1,31,31,31,31,31,31,31,31,44,44,44,44,44,44,44,44,44,44,44,44,44,55,55,55,55,55,55,55,55,55,55,55,55,55,55,48,44,44,44,44,44,44,44,1};
    public static final Integer[] NEED_MERGE_CELL = new Integer[]{1,3,4,5,6,9,10,11,12,13,17,18,19,20,21,22,23,24,32,33,35,36,37,38,39,40,41,42,43,44,45,46,48,49,50,51};
}
