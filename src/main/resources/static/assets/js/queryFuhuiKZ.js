$(function () {

    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
$("select").select2({
        //tags: true
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
    $("#cargoType").change(function(){
            $("#externalCompany").empty();
            $("#externalCompany").append("<option>全部</option>");
            initExternalCompany();
        });
        initBank();
    initExternalCompany();
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
                field: 'totalInvoiceMoney',
                title: '发票总金额'
            }, {
                field: 'issuingDate',
                title: '开证日期'
            }, {
                field: 'issuingBank',
                title: '开证行'
            }, {
                field: 'lcno',
                title: 'LC NO.'
            }, {
                field: 'remittanceDate',
                title: '付汇日'
            }, {
                field: 'remittanceRate',
                title: '付汇汇率'
            }, {
                field: 'yahuiMoney',
                title: '押汇金额'
            }, {
                field: 'yahuiYearRate',
                title: '押汇年利率'
            }, {
                field: 'yahuidaoqiDate',
                title: '押汇到期日'
            }, {
                field: 'yahuiDayRate',
                title: '到期日汇率'
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

        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
cargoType:$("#cargoType").val(),
        issuingBank:$("#issuingBank").val(),
        issuingDateStart:$("#issuingDateStart").val(),
        issuingDateEnd:$("#issuingDateEnd").val(),
        remittanceDateStart:$("#remittanceDateStart").val(),
        remittanceDateEnd:$("#remittanceDateEnd").val(),
        yahuidaoqiDateStart:$("#yahuidaoqiDateStart").val(),
        yahuidaoqiDateEnd:$("#yahuidaoqiDateEnd").val(),
        yahuiMoneyMin:$("#yahuiMoneyMin").val(),
        yahuiMoneyMax:$("#yahuiMoneyMax").val(),
        cmpRel: $("#cmpRel").val(),
       externalCompany: externalCompany,
       originCountry: originCountry,
            payType: "L/C"
            //isYahui: "1"
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
    $("#ownerCompany").val("全部").trigger("change");
    $("#externalCompany").val("全部").trigger("change");
    $("#originCountry").val("全部").trigger("change");
    $("#issuingBank").val("").trigger("change");
    $("#cargoType").val("").trigger("change");
    $("#issuingDateStart").val("");
    $("#issuingDateEnd").val("");
    $("#remittanceDateStart").val("");
    $("#remittanceDateEnd").val("");
    $("#cmpRel").val("1").trigger("change");
    $("#yahuidaoqiDateStart").val("");
    $("#yahuidaoqiDateEnd").val("");
    $("#yahuiMoneyMin").val("");
    $("#yahuiMoneyMax").val("");
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
    var queryParams = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
       ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
       cargoType:$("#cargoType").val(),
       issuingBank:$("#issuingBank").val(),
               issuingDateStart:$("#issuingDateStart").val(),
               issuingDateEnd:$("#issuingDateEnd").val(),
               remittanceDateStart:$("#remittanceDateStart").val(),
               remittanceDateEnd:$("#remittanceDateEnd").val(),
               yahuidaoqiDateStart:$("#yahuidaoqiDateStart").val(),
               yahuidaoqiDateEnd:$("#yahuidaoqiDateEnd").val(),
               yahuiMoneyMin:$("#yahuiMoneyMin").val(),
               yahuiMoneyMax:$("#yahuiMoneyMax").val(),
               cmpRel: $("#cmpRel").val(),
       externalCompany: externalCompany,
       originCountry: originCountry,
       payType: "L/C"
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
                $("#totalCNYYahuiMoney").html(toFloat4(res.totalCNYYahuiMoney));
                $("#totalUSDYahuiMoney").html(toFloat4(res.totalUSDYahuiMoney));
                $("#totalAUDYahuiMoney").html(toFloat4(res.totalAUDYahuiMoney));
            }
        }
    });
}