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

    /*$("select").on("change",function(){
        $("#btn_query").click();
    });
    initLevel();
    initBusinessMode();
    initCargoList();*/
    initExternalCompany();
    initAgent();
    initOriginCountry();
    initOwnerCompanyList();
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
            url: '/trade/queryDutyList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "taxPayDate",                     //排序字段名
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 1000,                       //每页的记录行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
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
                field: 'tariffNo',
                title: '报关单号',
                formatter: function(value, row, index){
                    if(value.length >0) return value.substring(0,20)+"...";
                    else return "";
                }
            }, {
                field: 'taxPayDate',
                title: '付税日期'
            }, {
                field: 'tariff',
                title: '关税',
                formatter: function(value, row, index){
                    return toFloat(value);
                }
            }, {
                field: 'addedValueTax',
                title: '增值税',
                formatter: function(value, row, index){
                    return toFloat(value);
                }
            }, {
                field: 'agent',
                title: '货代'
            }, {
                field: 'agentPassDate',
                title: '放行日期'
            }, {
                field: 'warehouse',
                title: '仓库'
            }, {
                field: 'storeDate',
                title: '入库日期'
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
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            taxPayDateStart: $("#taxPayDateStart").val(),
            taxPayDateEnd: $("#taxPayDateEnd").val(),
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
            etaStartDate: $("#etaStartDate").val(),
            etaEndDate: $("#etaEndDate").val(),
            cargoType: $("#cargoType").val(),
            cmpRel: $("#cmpRel").val(),
            externalCompany: externalCompany,
            originCountry:originCountry,
            agent:agent
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
            var params = "?externalCompany="+externalCompany;
            params += "&taxPayDateStart="+$("#taxPayDateStart").val();
            params += "&taxPayDateEnd="+$("#taxPayDateEnd").val();
            var ownerCompany = $("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val();
            params += "&ownerCompany="+ownerCompany;
            params += "&etaStartDate="+$("#etaStartDate").val();
            params += "&etaEndDate="+$("#etaEndDate").val();
            params += "&cmpRel="+$("#cmpRel").val();
            params += "&cargoType="+$("#cargoType").val();
            params += "&agent="+agent;
            params += "&originCountry="+originCountry;
            var url = "/trade/queryDuty/output"+params;
            window.open(url);
        });
    };

    return oInit;
};

function resetQuery(){
    $("#taxPayDateStart").val("");
    $("#taxPayDateEnd").val("");
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
    var queryParams = {
        taxPayDateStart: $("#taxPayDateStart").val(),
        taxPayDateEnd: $("#taxPayDateEnd").val(),
        ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
        etaStartDate: $("#etaStartDate").val(),
        etaEndDate: $("#etaEndDate").val(),
        cargoType: $("#cargoType").val(),
        cmpRel: $("#cmpRel").val(),
        externalCompany: externalCompany,
        originCountry:originCountry,
        agent:agent
    };
    $.ajax({
        url:"/trade/queryDutyTotal",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            if(res.status == "1"){
                $("#totalTariff").html(toFloat(res.totalTariff));
                $("#totalAddedValueTax").html(toFloat(res.totalAddedValueTax));
            }
        }
    });
}