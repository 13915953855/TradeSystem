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
    initOriginCountry();

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
            url: '/trade/queryCargoList',         //请求后台的URL（*）
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
                field: 'externalCompany',
                title: '外商'
            }, {
                 field: 'originCountry',
                 title: '原产地'
             }, {
                field: 'contractDate',
                title: '合同日期'
            }, {
                field: 'containerNo',
                title: '柜号',
                visible: false
            }, {
                field: 'companyNo',
                title: '厂号'
            }, {
                field: 'cargoName',
                title: '商品'
            }, {
                field: 'level',
                title: '级别'
            }, {
                field: 'unitPrice',
                title: '单价'
            }, {
                field: 'contractAmount',
                title: '合同重量'
            }, {
                field: 'contractMoney',
                title: '合同金额'
            }, {
                field: 'invoiceAmount',
                title: '发票重量'
            }, {
                field: 'invoiceMoney',
                title: '发票金额'
            }, {
                field: 'etd',
                title: 'ETD'
            }, {
                field: 'eta',
                title: 'ETA'
            }, {
                field: 'expectSailingDate',
                title: '预计船期'
            }, {
                field: 'status',
                title: '商品状态',
                formatter: function(value, row, index){
                    if(value == 4) {
                        return '已入库';
                    }else if(value == 5){
                        return '已售完';
                    }else if(value == 1){
                        return '已下单';
                    }else if(value == 2){
                        return '已装船';
                    }else if(value == 3){
                        return '已到港';
                    }else{
                        return '-';
                    }
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
        var originCountryArr = $("#originCountry").val();
        var originCountry = "";
        if(originCountryArr != null){
            for(var i=0;i<originCountryArr.length;i++){
                if(originCountryArr[i] != '全部'){
                    originCountry += "'"+originCountryArr[i] + "',";
                }else{
                    originCountry = "";break;
                }
            }
        }
        if(originCountry.length > 1){
            originCountry = originCountry.substring(0,originCountry.length-1);
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
            storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
            contractStartDate: $("#contractStartDate").val(),
            contractEndDate: $("#contractEndDate").val(),
            startDate: $("#startDate").val(),
            endDate: $("#endDate").val(),
            etaStartDate: $("#etaStartDate").val(),
            etaEndDate: $("#etaEndDate").val(),
            etdStartDate: $("#etdStartDate").val(),
            etdEndDate: $("#etdEndDate").val(),
            cargoName: cargoName,
            level: level,
            status: status,
            companyNo: $("#companyNo").val(),
            businessMode: businessMode,
            externalCompany: externalCompany,
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
            originCountry: originCountry
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
            var originCountryArr = $("#originCountry").val();
            var originCountry = "";
            if(originCountryArr != null){
                for(var i=0;i<originCountryArr.length;i++){
                    if(originCountryArr[i] != '全部'){
                        originCountry += "'"+originCountryArr[i] + "',";
                    }else{
                        originCountry = "";break;
                    }
                }
            }
            if(originCountry.length > 1){
                originCountry = originCountry.substring(0,originCountry.length-1);
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
            var params = "?externalCompany="+externalCompany;
            params += "&businessMode="+businessMode;
            params += "&companyNo="+$("#companyNo").val();
            params += "&level="+level;
            params += "&externalContract="+ $("#externalContract").val();
            params += "&storageCondition="+ $("#storageCondition").val();
            params += "&insideContract="+$("#insideContract").val();
            params += "&status="+status;
            params += "&cargoName="+cargoName;
            params += "&etaEndDate="+$("#etaStartDate").val();
            params += "&etaStartDate="+$("#etaEndDate").val();
            params += "&endDate="+$("#endDate").val();
            params += "&startDate="+$("#startDate").val();
            params += "&etdStartDate="+$("#etdStartDate").val();
            params += "&etdEndDate="+$("#etdEndDate").val();
            params += "&contractEndDate="+$("#contractEndDate").val();
            params += "&contractStartDate="+$("#contractStartDate").val();
            params += "&originCountry="+originCountry;
            var ownerCompany = $("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val();
            params += "&ownerCompany="+ownerCompany;
            var url = "/trade/queryCargo/output"+params;
            window.open(url);
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
    $("#status").val("全部").trigger("change");
    $("#level").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
    $("#originCountry").val("全部").trigger("change");
    $("#ownerCompany").val("全部").trigger("change");
    $("#storageCondition").val("全部").trigger("change");
    $("#startDate").val("");
    $("#endDate").val("");
    $("#etaStartDate").val("");
        $("#etaEndDate").val("");
        $("#etdStartDate").val("");
        $("#etdEndDate").val("");
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
        var originCountryArr = $("#originCountry").val();
        var originCountry = "";
        if(originCountryArr != null){
            for(var i=0;i<originCountryArr.length;i++){
                if(originCountryArr[i] != '全部'){
                    originCountry += "'"+originCountryArr[i] + "',";
                }else{
                    originCountry = "";break;
                }
            }
        }
        if(originCountry.length > 1){
            originCountry = originCountry.substring(0,originCountry.length-1);
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
        storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val(),
        contractEndDate: $("#contractEndDate").val(),
        externalContract: $("#externalContract").val(),
        insideContract: $("#insideContract").val(),
        startDate: $("#startDate").val(),
        endDate: $("#endDate").val(),
        etaStartDate: $("#etaStartDate").val(),
        etaEndDate: $("#etaEndDate").val(),
        etdStartDate: $("#etdStartDate").val(),
        etdEndDate: $("#etdEndDate").val(),
        cargoName: cargoName,
        level: level,
        status: status,
        companyNo: $("#companyNo").val(),
        businessMode: businessMode,
        externalCompany: externalCompany,
        ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
        originCountry: originCountry
    };

    $.ajax({
        url:"/trade/query/getTotalInfo",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            if(res.status == "1"){
                $("#totalCNYContractMoney").html(toFloat(res.totalCNYContractMoney));
                $("#totalCNYInvoiceMoney").html(toFloat(res.totalCNYInvoiceMoney));
                $("#totalUSDContractMoney").html(toFloat(res.totalUSDContractMoney));
                $("#totalUSDInvoiceMoney").html(toFloat(res.totalUSDInvoiceMoney));
                $("#totalAUDContractMoney").html(toFloat(res.totalAUDContractMoney));
                $("#totalAUDInvoiceMoney").html(toFloat(res.totalAUDInvoiceMoney));
                $("#totalContractAmount").html(toFloat4(res.totalContractAmount));
                $("#totalInvoiceAmount").html(toFloat4(res.totalInvoiceAmount));
            }
        }
    });
}

