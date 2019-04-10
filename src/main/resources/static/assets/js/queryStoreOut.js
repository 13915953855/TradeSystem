$(function () {

    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
$("select").select2({
        tags: true
    });
    $(".form_datetime").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        minView: 2,
        maxView: 4,
        todayHighlight: true,
        language: 'zh-CN'
    });

    $("select").on("change",function(){
        $("#btn_query").click();
    });
    initLevel();
    initBusinessMode();
    initExternalCompany();
    initCargoList();

    getTotalInfo();
});

var toFloat = function (value) {
    value = Math.round(parseFloat(value) * 100) / 100;
    if (value.toString().indexOf(".") < 0) {
        value = value.toString() + ".00";
    }
    return value;
}
var toFloat4 = function (value) {
    value = Math.round(parseFloat(value) * 10000) / 10000;
    if (value.toString().indexOf(".") < 0) {
        value = value.toString() + ".0000";
    }
    return value;
}

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_contract').bootstrapTable({
            url: '/trade/queryCargoSellInfo',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "id",                     //排序字段名
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                field: 'inside_contract',
                title: '内合同编号'
            }, {
                field: 'external_company',
                title: '外商',
                                           visible: false
            }, {
                field: 'company_no',
                title: '厂号'
            }, {
                field: 'cargo_name',
                title: '商品'
            }, {
                field: 'level',
                title: '级别'
            }, {
                field: 'warehouse',
                title: '仓库'
            }, {
                field: 'unit_price',
                title: '单价',
                                           visible: false
            }, {
                field: 'cost_price',
                title: '成本单价'
            }, {
                field: 'real_store_weight',
                title: '库存重量',
                                             visible: false,
              formatter: function(value, row, index){
                  return toFloat4(value);
              }
            }, {
                field: 'real_store_money',
                title: '库存成本',
                                             visible: false
            }, {
                field: 'container_no',
                title: '柜号'
            }, {
                field: 'ladingbill_no',
                title: '提单号',
                                            visible: false
            }, {
                field: 'real_sale_date',
                title: '出库时间'
            }, {
                field: 'customer_name',
                title: '客户名称'
            }, {
                field: 'real_sale_weight',
                title: '实售重量'
            }, {
                field: 'real_sale_boxes',
                title: '实售箱数'
            }, {
                field: 'real_sale_unit_price',
                title: '实售单价'
            }, {
                field: 'real_sale_money',
                title: '实售金额'
            }, {
                field: 'deposit',
                title: '定金',
                                           visible: false
            }, {
                field: 'customer_pay_money',
                title: '客户来款金额',
                                               visible: false
            }, {
                field: 'profit',
                title: '利润'
            }, {
                 field: 'kaifapiao',
                 title: '发票',
                 //visible: false,
                 formatter: function(value, row, index){
                    if(value == 1) return '是';
                    else if(value == 0) return '否';
                    else return value;
                 }
             }]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            contractNo: $("#contractNo").val(),
            warehouse: $("#warehouse").val() == "全部"?"":$("#warehouse").val(),
            storeStartDate: $("#storeStartDate").val(),
            storeEndDate: $("#storeEndDate").val(),
            pickupDateStart: $("#pickupDateStart").val(),
            pickupDateEnd: $("#pickupDateEnd").val(),
            realSaleDateStart: $("#realSaleDateStart").val(),
            realSaleDateEnd: $("#realSaleDateEnd").val(),
            insideContract: $("#insideContract").val(),
            level: $("#level").val() == "全部"?"":$("#level").val(),
            cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
            cargoNo: $("#cargoNo").val(),
            customerName: $("#customerName").val(),
            containerNo: $("#containerNo").val(),
            storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
            ladingbillNo: $("#ladingbillNo").val(),
            status: $("#status").val() == "全部"?"":$("#status").val(),
            kaifapiao: $("#kaifapiao").val() == "全部"?"":$("#kaifapiao").val(),
            businessMode: $("#businessMode").val() == "全部"?"":$("#businessMode").val(),
            minBox: $("#minBox").val(),
            maxBox: $("#maxBox").val()
        };
        return temp;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        $("#btn_query").click(function(){
            $('#tb_contract').bootstrapTable("refresh",{pageNumber:1});
            getTotalInfo();
        });
        $("#btn_reset").click(function(){
            resetQuery();
            getTotalInfo();
        });
    };

    return oInit;
};

function resetQuery(){
    $("#contractNo").val("");
    $("#insideContract").val("");
    $("#cargoNo").val("");
    $("#warehouse").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
    $("#ownerCompany").val("全部").trigger("change");
    $("#level").val("全部").trigger("change");
    $("#status").val("全部").trigger("change");
    $("#businessMode").val("全部").trigger("change");
    $("#storageCondition").val("全部").trigger("change");
    $("#storeStartDate").val("");
    $("#storeEndDate").val("");
    $("#containerNo").val("");
    $("#ladingbillNo").val("");
    $("#customerName").val("");
    $("#pickupDateStart").val("");
    $("#pickupDateEnd").val("");
    $("#realSaleDateStart").val("");
    $("#realSaleDateEnd").val("");
    $("#totalStoreWeight").html("");
    $("#totalStoreBoxes").html("");
}

function getTotalInfo(){
     var queryParams = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
         contractNo: $("#contractNo").val(),
         warehouse: $("#warehouse").val() == "全部"?"":$("#warehouse").val(),
         storeStartDate: $("#storeStartDate").val(),
         storeEndDate: $("#storeEndDate").val(),
         insideContract: $("#insideContract").val(),
         level: $("#level").val() == "全部"?"":$("#level").val(),
         cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
         cargoNo: $("#cargoNo").val(),
         customerName: $("#customerName").val(),
         storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
         containerNo: $("#containerNo").val(),
         ladingbillNo: $("#ladingbillNo").val(),
         pickupDateStart: $("#pickupDateStart").val(),
         pickupDateEnd: $("#pickupDateEnd").val(),
         realSaleDateStart: $("#realSaleDateStart").val(),
         realSaleDateEnd: $("#realSaleDateEnd").val(),
         status: $("#status").val() == "全部"?"":$("#status").val(),
         kaifapiao: $("#kaifapiao").val() == "全部"?"":$("#kaifapiao").val(),
         businessMode: $("#businessMode").val() == "全部"?"":$("#businessMode").val(),
         ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
         minBox: $("#minBox").val(),
         maxBox: $("#maxBox").val()
     };

    $.ajax({
        url:"/trade/query/getTotalStoreOut",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            $("#totalStoreWeight").html("0");
            $("#totalStoreMoney").html("0");
            if(res.status == "1"){
                $("#totalStoreWeight").html(res.totalStoreWeight);
                $("#totalStoreMoney").html(res.totalStoreMoney);
            }
        }
    });
}

