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

    //size
    public static final int HEAD_CONTRACT_CELL_SIZE = 50;
    public static final String[] HEAD_CONTRACT_ARRAY = new String[]{"序号","外合同编号","内合同编号","合同日期","外商","原产地","厂号","目的港","价格条件","付款方式","币种","预计船期","业务模式","箱数总计","合同总数量(KG)","合同总金额(元)","发票总数量(KG)","发票总金额(元)","开证行","开证日期","LC NO.","银行到单日","付汇日","押汇到期日","付汇汇率(%)","付款金额","付款日期","汇率","付款金额","付款日期","汇率","柜号","提单号","货柜尺寸","需要购买保险","保险购买日期","保险费用","保险公司","ETD","ETA","已核对电子版","货代","单据寄给货代日期","关税","增值税","付税日期","放行日期","仓库","入库日期","备注"};
    //size
    public static final int HEAD_CARGO_CELL_SIZE = 11;
    public static final String[] HEAD_CARGO_ARRAY = new String[]{"产品名称","级别","库号","采购单价(/KG)","箱数(小计)","合同数量(小计)","合同金额(小计:元)","发票数量(小计)","发票金额(小计:元)","成本单价(元/KG)","库存金额(元)"};
    //size
    public static final int HEAD_SALE_CELL_SIZE = 19;
    public static final String[] HEAD_SALE_ARRAY = new String[]{"提货时间","销售经理","销售合同编号","客户名称","预售单价(元/KG)","预售重量(kg)","预售金额(元)","预售箱数","预出库时间","实售单价(元/KG)","实售重量(kg)","实售箱数","实售金额(元)","出库单时间","客户来款时间","客户来款金额(元)","货款差额","是否已结清","备注"};
}
