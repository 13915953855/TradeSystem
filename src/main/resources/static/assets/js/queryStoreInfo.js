$(function () {

    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

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
            url: '/trade/queryStoreInfoList',         //请求后台的URL（*）
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
               field: 'cargoName',
               title: '商品'
           }, {
               field: 'level',
               title: '级别'
           },{
                 field: 'companyNo',
                 title: '厂号'
             }, {
                 field: 'cargoNo',
                 title: '库号'
             }, {
                field: 'externalContract',
                title: '外合同编号'
            }, {
                field: 'insideContract',
                title: '内合同编号'
            }, {
                field: 'containerNo',
                title: '柜号'
            }, {
                field: 'ladingbillNo',
                title: '提单号'
            }, {
                field: 'storeDate',
                title: '入库时间'
            }, {
                field: 'warehouse',
                title: '仓库'
            }, {
                field: 'invoiceAmount',
                title: '发票重量',
                formatter: function(value, row, index){
                    return toFloat4(value);
                }
            }, {
                field: 'boxes',
                title: '发票箱数'
            }, {
                field: 'expectStoreWeight',
                title: '预库存重量',
                  formatter: function(value, row, index){
                      return toFloat4(value);
                  }
            }, {
                field: 'expectStoreBoxes',
                title: '预库存箱数'
            }, {
                field: 'realStoreWeight',
                title: '现库存重量',
                  formatter: function(value, row, index){
                      return toFloat4(value);
                  }
            }, {
                field: 'realStoreBoxes',
                title: '现库存箱数'
            }, {
                field: 'realStoreMoney',
                title: '库存成本',
                 formatter: function(value, row, index){
                     return toFloat4(value);
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
        var levelArr = $("#level").val();
        var level = "";
        if(levelArr != null){
            for(var i=0;i<levelArr.length;i++){
                if(levelArr[i] != '全部'){
                    level += "'"+levelArr[i] + "',";
                }else{
                    level = "";break;
                }
            }
        }
        if(level.length > 1){
            level = level.substring(0,level.length-1);
        }
        var cargoName = $("#cargoName").val() == "全部" ? "":$("#cargoName").val();
        var businessModeArr = $("#businessMode").val();
        var businessMode = "";
        if(businessModeArr != null){
            for(var i=0;i<businessModeArr.length;i++){
                if(businessModeArr[i] != '全部'){
                    businessMode += "'"+businessModeArr[i] + "',";
                }else{
                    businessMode = "";break;
                }
            }
        }
        if(businessMode.length > 1){
            businessMode = businessMode.substring(0,businessMode.length-1);
        }
        var statusArr = $("#status").val();
                var status = "";
                if(statusArr != null){
                    for(var i=0;i<statusArr.length;i++){
                        if(statusArr[i] != '全部'){
                            status += statusArr[i] + ",";
                        }else{
                            status = "";break;
                        }
                    }
                }
                if(status.length > 1){
                    status = status.substring(0,status.length-1);
                }
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            contractStartDate: $("#contractStartDate").val(),
            contractEndDate: $("#contractEndDate").val(),
            cargoName: cargoName,
            level: level,
            status: status,
            companyNo: $("#companyNo").val(),
            businessMode: businessMode,
            externalCompany: externalCompany
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
    $("#contractStartDate").val("");
    $("#contractEndDate").val("");
    $("#companyNo").val("");
    $("#businessMode").val("全部").trigger("change");
    $("#externalCompany").val("全部").trigger("change");
    $("#level").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
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
        var levelArr = $("#level").val();
        var level = "";
        if(levelArr != null){
            for(var i=0;i<levelArr.length;i++){
                if(levelArr[i] != '全部'){
                    level += "'"+levelArr[i] + "',";
                }else{
                    level = "";break;
                }
            }
        }
        if(level.length > 1){
            level = level.substring(0,level.length-1);
        }
        var cargoName = $("#cargoName").val() == "全部" ? "":$("#cargoName").val();
        var businessModeArr = $("#businessMode").val();
        var businessMode = "";
        if(businessModeArr != null){
            for(var i=0;i<businessModeArr.length;i++){
                if(businessModeArr[i] != '全部'){
                    businessMode += "'"+businessModeArr[i] + "',";
                }else{
                    businessMode = "";break;
                }
            }
        }
        if(businessMode.length > 1){
            businessMode = businessMode.substring(0,businessMode.length-1);
        }
var statusArr = $("#status").val();
                var status = "";
                if(statusArr != null){
                    for(var i=0;i<statusArr.length;i++){
                        if(statusArr[i] != '全部'){
                            status += statusArr[i] + ",";
                        }else{
                            status = "";break;
                        }
                    }
                }
                if(status.length > 1){
                    status = status.substring(0,status.length-1);
                }
    var queryParams = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        contractStartDate: $("#contractStartDate").val(),
        contractEndDate: $("#contractEndDate").val(),
        cargoName: cargoName,
        level: level,
        status: status,
        companyNo: $("#companyNo").val(),
        businessMode: businessMode,
        externalCompany: externalCompany
    };

    $.ajax({
        url:"/trade/query/getTotalStoreInfo",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            if(res.status == "1"){
                $("#totalInvoiceMoney").html(toFloat(res.totalInvoiceMoney));
                $("#totalInvoiceWeight").html(toFloat(res.totalInvoiceWeight));
                $("#totalInvoiceBoxes").html(toFloat(res.totalInvoiceBoxes));
                $("#expectStoreWeight").html(toFloat(res.expectStoreWeight));
                $("#realStoreWeight").html(toFloat(res.realStoreWeight));
                $("#expectStoreBoxes").html(toFloat4(res.expectStoreBoxes));
                $("#realStoreBoxes").html(toFloat4(res.realStoreBoxes));
                $("#realStoreMoney").html(toFloat4(res.realStoreMoney));
            }
        }
    });
}