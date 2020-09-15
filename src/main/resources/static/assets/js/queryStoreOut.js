$(function () {

    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    $("#level").select2({
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
    $("#cargoType").change(function(){
        $("#cargoName").empty();
        $("#cargoName").append("<option>全部</option>");
        initCargoList();
    });
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
                field: 'contract_no',
                title: '内合同编号'
            }, {
                field: 'external_company',
                title: '外商',
                visible: false
            }, {
                field: 'cargo_no',
                title: '库号'
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
                visible: false,
                formatter: function(value, row, index){
                                  var r = auth($("#user").val());
                                  if(r == "1"){
                                      return value;
                                  }else{
                                      return '--';
                                  }
                                }
            }, {
                field: 'cost_price',
                title: '成本单价',
                formatter: function(value, row, index){
                                  var r = auth($("#user").val());
                                  if(r == "1"){
                                      return value;
                                  }else{
                                      return '--';
                                  }
                                }
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
                title: '实售箱数',
                visible: false
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
                field: 'customer_type',
                title: '客户属性',
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
        var externalCompanyArr = $("#externalCompany").val();
            var externalCompany = "";
            if(externalCompanyArr != null){
                for(var i=0;i<externalCompanyArr.length;i++){
                    if(externalCompanyArr[i] != '全部'){
                        externalCompany += "'"+externalCompanyArr[i] + "',";
                    }else{
                        externalCompany = "";break;
                    }
                }
            }
            if(externalCompany.length > 1){
                externalCompany = externalCompany.substring(0,externalCompany.length-1);
            }
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            externalCompany: externalCompany,
            cmpRel: $("#cmpRel").val(),
            companyNo: $("#companyNo").val(),
            importContractNo: $("#importContractNo").val(),
            contractNo: $("#contractNo").val(),
            warehouse: $("#warehouse").val() == "全部"?"":$("#warehouse").val(),
            pickupDateStart: $("#pickupDateStart").val(),
            pickupDateEnd: $("#pickupDateEnd").val(),
            realSaleDateStart: $("#realSaleDateStart").val(),
            realSaleDateEnd: $("#realSaleDateEnd").val(),
            level: $("#level").val() == "全部"?"":$("#level").val(),
            cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
            cargoNo: $("#cargoNo").val(),
            cargoType: $("#cargoType").val(),
            customerName: $("#customerName").val(),
            containerNo: $("#containerNo").val(),
            storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
            status: $("#status").val() == "全部"?"":$("#status").val(),
            kaifapiao: $("#kaifapiao").val() == "全部"?"":$("#kaifapiao").val(),
            businessMode: $("#businessMode").val() == "全部"?"":$("#businessMode").val(),
            minBox: $("#minBox").val(),
            customerType: $("#customerType").val(),
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
        $("#btn_output").click(function(){
        var externalCompanyArr = $("#externalCompany").val();
                    var externalCompany = "";
                    if(externalCompanyArr != null){
                        for(var i=0;i<externalCompanyArr.length;i++){
                            if(externalCompanyArr[i] != '全部'){
                                externalCompany += "'"+externalCompanyArr[i] + "',";
                            }else{
                                externalCompany = "";break;
                            }
                        }
                    }
                    if(externalCompany.length > 1){
                        externalCompany = externalCompany.substring(0,externalCompany.length-1);
                    }
            var params = "?contractNo="+$("#contractNo").val();
            params += "&realSaleDateStart="+$("#realSaleDateStart").val();
            params += "&realSaleDateEnd="+$("#realSaleDateEnd").val();
            params += "&pickupDateStart="+$("#pickupDateStart").val();
            params += "&pickupDateEnd="+$("#pickupDateEnd").val();
            params += "&importContractNo="+$("#importContractNo").val();
            params += "&cargoNo="+$("#cargoNo").val();
            params += "&externalCompany="+externalCompany;
            params += "&cmpRel="+$("#cmpRel").val();
            params += "&companyNo="+$("#companyNo").val();
            params += "&cargoType="+$("#cargoType").val();
            params += "&containerNo="+$("#containerNo").val();
            params += "&customerName="+$("#customerName").val();
            params += "&maxBox="+$("#maxBox").val();
            params += "&customerType="+$("#customerType").val();
            params += "&minBox="+$("#minBox").val();
            var warehouse = $("#warehouse").val() == '全部'?"":$("#warehouse").val();
            params += "&warehouse="+warehouse;
            var ownerCompany = $("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val();
            params += "&ownerCompany="+ownerCompany;
            var level = $("#level").val() == "全部"?"":$("#level").val();
            params += "&level="+level;
            var cargoName = $("#cargoName").val() == "全部"?"":$("#cargoName").val();
            params += "&cargoName="+cargoName;
            var storageCondition = $("#storageCondition").val() == "全部"?"":$("#storageCondition").val();
            params += "&storageCondition="+storageCondition;
            var status = $("#status").val() == "全部"?"":$("#status").val();
            params += "&status="+status;
            var kaifapiao = $("#kaifapiao").val() == "全部"?"":$("#kaifapiao").val();
            params += "&kaifapiao="+kaifapiao;
            var businessMode = $("#businessMode").val() == "全部"?"":$("#businessMode").val();
            params += "&businessMode="+businessMode;
            var url = "/trade/queryStoreOut/output"+params;
            window.open(url);
        });
    };

    return oInit;
};

function resetQuery(){
    $("#contractNo").val("");
    $("#importContractNo").val("");
    $("#cargoNo").val("");
    $("#companyNo").val("");
    $("#warehouse").val("全部").trigger("change");
    $("#externalCompany").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
    $("#cargoType").val("全部").trigger("change");
    $("#ownerCompany").val("全部").trigger("change");
    $("#level").val("全部").trigger("change");
    $("#status").val("全部").trigger("change");
    $("#businessMode").val("全部").trigger("change");
    $("#storageCondition").val("全部").trigger("change");
    $("#customerType").val("").trigger("change");
    $("#containerNo").val("");
    $("#customerName").val("");
    $("#pickupDateStart").val("");
    $("#pickupDateEnd").val("");
    $("#realSaleDateStart").val("");
    $("#realSaleDateEnd").val("");
    $("#totalStoreWeight").html("");
    $("#totalStoreBoxes").html("");
}

function getTotalInfo(){
    var externalCompanyArr = $("#externalCompany").val();
    var externalCompany = "";
    if(externalCompanyArr != null){
        for(var i=0;i<externalCompanyArr.length;i++){
            if(externalCompanyArr[i] != '全部'){
                externalCompany += "'"+externalCompanyArr[i] + "',";
            }else{
                externalCompany = "";break;
            }
        }
    }
    if(externalCompany.length > 1){
        externalCompany = externalCompany.substring(0,externalCompany.length-1);
    }
     var queryParams = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
         contractNo: $("#contractNo").val(),
         warehouse: $("#warehouse").val() == "全部"?"":$("#warehouse").val(),
         importContractNo: $("#importContractNo").val(),
         level: $("#level").val() == "全部"?"":$("#level").val(),
         cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
         cargoNo: $("#cargoNo").val(),
         externalCompany: externalCompany,
         cmpRel: $("#cmpRel").val(),
         companyNo: $("#companyNo").val(),
         cargoType: $("#cargoType").val(),
         customerName: $("#customerName").val(),
         storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
         containerNo: $("#containerNo").val(),
         pickupDateStart: $("#pickupDateStart").val(),
         pickupDateEnd: $("#pickupDateEnd").val(),
         realSaleDateStart: $("#realSaleDateStart").val(),
         realSaleDateEnd: $("#realSaleDateEnd").val(),
         status: $("#status").val() == "全部"?"":$("#status").val(),
         kaifapiao: $("#kaifapiao").val() == "全部"?"":$("#kaifapiao").val(),
         businessMode: $("#businessMode").val() == "全部"?"":$("#businessMode").val(),
         ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
         minBox: $("#minBox").val(),
         customerType: $("#customerType").val(),
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
            $("#totalSaleMoney").html("0");
            $("#totalSaleWeight").html("0");
            $("#totalSaleBoxes").html("0");
            $("#totalLirun").html("0");
            if(res.status == "1"){
                $("#totalStoreWeight").html(res.totalStoreWeight);
                $("#totalStoreMoney").html(res.totalStoreMoney);
                $("#totalSaleMoney").html(toFloat(res.totalSaleMoney));
                $("#totalSaleWeight").html(toFloat(res.totalSaleWeight));
                $("#totalSaleBoxes").html(res.totalSaleBoxes);
                $("#totalLirun").html(toFloat(res.totalLirun));
            }
        }
    });
}

