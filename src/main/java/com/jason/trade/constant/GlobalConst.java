package com.jason.trade.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobalConst {
    public static Map<String,Object> globalElement = new HashMap<>();
    public static final String SUCCESS = "{\"status\":1}";
    public static final String FAILURE = "{\"status\":-1}";
    public static final String MODIFIED = "{\"status\":-2}";
    //0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
    public static final String ENABLE = "1";
    public static final String SHIPPED = "2";
    public static final String ARRIVED = "3";
    public static final String STORED = "4";
    public static final String SELLOUT = "5";
    public static final String DISABLE = "0";
    public static final String EDITING = "9";
    public static final Integer UNPRESALED = 0;
    public static final Integer PRESALED = 1;

    public static final String FILE_PATH = "/home/uploadFile";
    //public static final String FILE_PATH = "D:";

    //size
    public static final String[] HEAD_CONTRACT_ARRAY = new String[]{"序号","外合同编号","内合同编号","合同日期","外商","原产地","储存条件","价格条件","目的港","币种","付款方式","预计船期","汇率","箱数总计","合同总重量(KG)","合同总金额(元)","发票总重量(KG)","发票总金额(元)","客户名称","预售时间","预售单价(元/KG)","预售重量(kg)","开证行","开证日期","LC NO.","付汇日","付汇汇率","已办理押汇","押汇金额","押汇年利率","押汇到期日","到期日汇率","预付款金额","尾款金额","预付款银行","尾款银行","预付款日期","尾款日期","预付款汇率","尾款汇率","已办理融资","融资银行","融资金额","融资年利率","融资到期日","到期日汇率","柜号","提单号","货柜尺寸","需要购买保险","保险公司","保险购买日期","保费","ETD","ETA","已核对电子版","已出检疫证","已出报关单","货代","单据寄给货代日期","关税","增值税","付税日期","报关单号","放行日期","仓库","入库日期","滞箱费","滞港费","备注"};
    //size
    public static final String[] HEAD_CARGO_ARRAY = new String[]{"产品名称","级别","库号","业务模式","厂号","采购单价(/KG)","箱数(小计)","合同数量(小计)","合同金额(小计:元)","发票数量(小计)","发票金额(小计:元)","成本单价(元/KG)","库存金额(元)","当前库存重量","当前库存箱数"};
    //size
    public static final String[] HEAD_SALE_ARRAY = new String[]{"提货时间","销售经理","销售合同编号","客户名称","实售单价(元/KG)","实售重量(kg)","实售箱数","实售金额(元)","出库单时间","定金时间","定金","客户来款时间","客户来款金额(元)","货款差额","是否已结清","备注"};

    public static final String[] HEAD_CONTRACT_QUERY_ARRAY = new String[]{"外合同编号","内合同编号","外商","合同日期","合同总重量","合同总金额","发票总重量","发票总金额","原产地","币种","ETD","ETA","预计船期","目的港","状态"};
    public static final String[] HEAD_CARGO_QUERY_ARRAY = new String[]{"外合同编号","内合同编号","外商","原产地","合同日期","厂号","柜号","币种","商品","级别","单价","合同重量","合同金额","发票重量","发票金额","ETD","ETA","预计船期","目的港","状态"};
    public static final String[] HEAD_DUTY_ARRAY = new String[]{"外合同编号","内合同编号","外商","报关单号","付税日期","关税","增值税","货代","放行日期","仓库","入库时间"};
    public static final String[] HEAD_STOREINFO_QUERY_ARRAY = new String[]{"商品","级别","厂号","库号","外合同编号","内合同编号","柜号","提单号","入库时间","仓库","发票重量","发票箱数","现库存重量","现库存箱数","库存成本"};
}
