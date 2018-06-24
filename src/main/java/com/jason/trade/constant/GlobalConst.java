package com.jason.trade.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobalConst {
    public static Map<String,Object> globalElement = new HashMap<>();
    public static final String SUCCESS = "{\"status\":1}";
    public static final String FAILURE = "{\"status\":-1}";
    public static final String MODIFIED = "{\"status\":-2}";
    //0-作废，1-已下单，2-已装船，3-已到港，4-已入库
    public static final String ENABLE = "1";
    public static final String SHIPPED = "2";
    public static final String ARRIVED = "3";
    public static final String STORED = "4";
    public static final String DISABLE = "0";
    public static final String[] FIRST_HEAD_ARRAY  = new String[]{"","采购信息","","","","","","","","","","","","","","","","预付款","","","","尾款","","","","","","","","","","","船期","","外商单据","","","","","柜号","提单号","报关报检","","","","税款","","","","","入库","","备注"};
    public static final short[] FIRST_HEAD_COLOR  = new short[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,44,44,44,44,44,44,44,44,44,44,44,44,44,47,47,47,47,47,47,47,47,47,47,47,47,47,47,48,44,44,44,44,44,44,44,1};
    public static final String[] SECOND_HEAD_ARRAY = new String[]{"序号","合同日期","外商","外合同","内合同","业务模式","厂号","库号","产品","规格","原产地","价格条件","起运港","目的港","数量(kg)","单价(USD)","合同金额(USD)","付款金额(USD)","付款日期","汇率","小计(CNY)","付款金额","付款日期","汇率","小计(CNY)","销售客户","来款金额","来款日期","尾款金额","来款日期","发票数量(kg)","发票金额(USD)","ETD","ETA","电子版发送日期","是否已核对电子版","保险购买日期","寄出日期","签收日期","柜号","提单号","货代","单据寄给货代日期","放行日期","税票抵扣方","关税","增值税","滞报费","付税日期","税票签收日期","仓库","入库日期"};
    public static final short[] SECOND_HEAD_COLOR  = new short[]{1,1,1,1,1,1,1,1,1,31,31,31,31,31,31,31,31,44,44,44,44,44,44,44,44,44,44,44,44,44,47,47,47,47,47,47,47,47,47,47,47,47,47,47,48,44,44,44,44,44,44,44,1};
    public static final short[] BODY_COLOR  = new short[]{1,1,1,1,1,1,1,1,1,31,31,31,31,31,31,31,31,44,44,44,44,44,44,44,44,44,44,44,44,44,55,55,55,55,55,55,55,55,55,55,55,55,55,55,48,44,44,44,44,44,44,44,1};
    //public static final Integer[] NEED_MERGE_CELL = new Integer[]{1,3,4,5,6,9,10,11,12,13,17,18,19,20,21,22,23,24,32,33,35,36,37,38,39,40,41,42,43,44,45,46,48,49,50,51};

    //size:61
    public static final int HEAD_CONTRACT_CELL_SIZE = 61;
    public static final String[] HEAD_CONTRACT_ARRAY = new String[]{"序号","外合同编号","内合同编号","合同日期","外商","原产地","厂号","起运港","目的港","价格条件","付款方式","币种","预计船期","业务模式","箱数总计","合同总数量","合同总金额(元)","发票总数量","发票总金额(元)","关税税率(%)","汇率(%)","增值税率(%)","开证行","开证日期","LC NO.","银行到单日","付汇日","押汇到期日","付汇汇率(%)","付款金额","付款日期","汇率","付款金额","付款日期","汇率","柜号","提单号","船公司名称","货柜尺寸","需要购买保险","保险购买日期","保险费用","保险公司","ETD","ETA","已核对电子版","电子版发送日期","外商邮寄正本单据日期","正本单据签收日期","货代","单据寄给货代日期","关税","增值税","付税日期","税票签收日期","税票抵扣方","放行日期","仓库","入库日期","滞箱滞报费","备注"};
    //size:13
    public static final int HEAD_CARGO_CELL_SIZE = 13;
    public static final String[] HEAD_CARGO_ARRAY = new String[]{"产品名称","级别","规格","库号","包装","采购单价(/KG)","箱数(小计)","合同数量(小计)","合同金额(小计:元)","发票数量(小计)","发票金额(小计:元)","成本单价(/KG)","库存成本"};
    //size:16
    public static final int HEAD_SALE_CELL_SIZE = 16;
    public static final String[] HEAD_SALE_ARRAY = new String[]{"提货重量(kg)","提货箱数","提货时间","提货人","销售合同编号","客户名称","预售重量(kg)","预售金额(元)","预售箱数","预出库时间","实售重量(kg)","实售箱数","实售金额(元)","出库单时间","客户来款时间","客户来款金额(元)"};
}
