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
    initDestinationPort();
    initAgent();
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
            url: '/trade/queryContractList',         //请求后台的URL（*）
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
                field: 'externalContract',
                title: '外合同编号'
            }, {
                field: 'insideContract',
                title: '内合同编号'
            }, {
                field: 'totalInvoiceAmount',
                title: '发票总重量'
            }, {
                field: 'totalInvoiceMoney',
                title: '发票总金额'
            }, {
                field: 'containerNo',
                title: '柜号'
            }, {
                field: 'ladingbillNo',
                title: '提单号'
            }, {
                field: 'agent',
                title: '货代'
            }, {
                field: 'destinationPort',
                title: '目的港'
            }, {
                field: 'eta',
                title: 'ETA'
            }, {
                field: 'jyzqfrq',
                title: '检疫证签发日期'
            }, {
                field: 'jyzzbsdrq',
                title: '检疫证正本收到日期'
            }, {
                field: 'agentPassDate',
                title: '放行日期'
            }, {
                field: 'storeDate',
                title: '入库日期'
            }, {
                field: 'dzdjsdrq',
                title: '电子单据收到日期'
            }, {
                field: 'agentSendDate',
                title: '正本单据收到日期'
            },{
              field: 'status',
              title: '状态',
              formatter: function(value, row, index){//0-作废，1-已下单，2-已装船，3-已到港，4-已入库,5-已售完
                  if(value == "1"){
                      return "已下单";
                  }else if(value == "2"){
                      return "已装船";
                  }else if(value == "3"){
                      return "已到港";
                  }else if(value == "4"){
                      return "已入库";
                  }else if(value == "5"){
                       return "已售完";
                   }else{
                      return "-";
                  }
              }
          }]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var agentArr = $("#agent").val();
        var agent = "";
        if(agentArr != null){
            for(var i=0;i<agentArr.length;i++){
                if(agentArr[i] != '全部'){
                    agent += "'"+agentArr[i] + "',";
                }else{
                    agent = "";break;
                }
            }
        }
        if(agent.length > 1){
            agent = agent.substring(0,agent.length-1);
        }

        var destinationPortArr = $("#destinationPort").val();
        var destinationPort = "";
        if(destinationPortArr != null){
            for(var i=0;i<destinationPortArr.length;i++){
                if(destinationPortArr[i] != '全部'){
                    destinationPort += "'"+destinationPortArr[i] + "',";
                }else{
                    destinationPort = "";break;
                }
            }
        }
        if(destinationPort.length > 1){
            destinationPort = destinationPort.substring(0,destinationPort.length-1);
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
            externalContract: $("#externalContract").val(),
            insideContract: $("#insideContract").val(),
            jyzqfrqStart: $("#jyzqfrqStart").val(),
            jyzqfrqEnd: $("#jyzqfrqEnd").val(),
            jyzzbsdrqStart: $("#jyzzbsdrqStart").val(),
            jyzzbsdrqEnd: $("#jyzzbsdrqEnd").val(),
            agentPassDateStart: $("#agentPassDateStart").val(),
            agentPassDateEnd: $("#agentPassDateEnd").val(),
            storeStartDate: $("#storeStartDate").val(),
            storeEndDate: $("#storeEndDate").val(),
            etaStartDate: $("#etaStartDate").val(),
            etaEndDate: $("#etaEndDate").val(),
            storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
            agent: agent,
            status: status,
            destinationPort: destinationPort
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
    $("#destinationPort").val("全部").trigger("change");
    $("#agent").val("全部").trigger("change");
    $("#status").val("全部").trigger("change");
    $("#storageCondition").val("全部").trigger("change");
    $("#ownerCompany").val("全部").trigger("change");
    $("#etaStartDate").val("");
    $("#etaEndDate").val("");
    $("#externalContract").val("");
    $("#insideContract").val("");
    $("#jyzqfrqStart").val("");
    $("#jyzqfrqEnd").val("");
    $("#jyzzbsdrqStart").val("");
    $("#jyzzbsdrqEnd").val("");
    $("#agentPassDateStart").val("");
    $("#agentPassDateEnd").val("");
    $("#storeStartDate").val("");
    $("#storeEndDate").val("");
}

function getTotalInfo(){
        var agentArr = $("#agent").val();
        var agent = "";
        if(agentArr != null){
            for(var i=0;i<agentArr.length;i++){
                if(agentArr[i] != '全部'){
                    agent += "'"+agentArr[i] + "',";
                }else{
                    agent = "";break;
                }
            }
        }
        if(agent.length > 1){
            agent = agent.substring(0,agent.length-1);
        }
    var destinationPortArr = $("#destinationPort").val();
    var destinationPort = "";
    if(destinationPortArr != null){
        for(var i=0;i<destinationPortArr.length;i++){
            if(destinationPortArr[i] != '全部'){
                destinationPort += "'"+destinationPortArr[i] + "',";
            }else{
                destinationPort = "";break;
            }
        }
    }
    if(destinationPort.length > 1){
        destinationPort = destinationPort.substring(0,destinationPort.length-1);
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
       etaStartDate: $("#etaStartDate").val(),
       etaEndDate: $("#etaEndDate").val(),
        externalContract: $("#externalContract").val(),
        insideContract: $("#insideContract").val(),
        jyzqfrqStart: $("#jyzqfrqStart").val(),
        jyzqfrqEnd: $("#jyzqfrqEnd").val(),
        jyzzbsdrqStart: $("#jyzzbsdrqStart").val(),
        jyzzbsdrqEnd: $("#jyzzbsdrqEnd").val(),
        agentPassDateStart: $("#agentPassDateStart").val(),
        agentPassDateEnd: $("#agentPassDateEnd").val(),
        storeStartDate: $("#storeStartDate").val(),
        storeEndDate: $("#storeEndDate").val(),
       agent: agent,
        storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
       status: status,
       ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
       destinationPort: destinationPort
    };

    $.ajax({
        url:"/trade/contract/getTotalInfo",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            if(res.status == "1"){
                $("#totalCNYInvoiceMoney").html(toFloat(res.totalCNYInvoiceMoney));
                $("#totalUSDInvoiceMoney").html(toFloat(res.totalUSDInvoiceMoney));
                $("#totalAUDInvoiceMoney").html(toFloat(res.totalAUDInvoiceMoney));
                $("#totalInvoiceAmount").html(toFloat4(res.totalInvoiceAmount));
            }
        }
    });
}