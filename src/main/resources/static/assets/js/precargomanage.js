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
    $("select").select2({
        //tags: true
    });
    initLevel();
    initBusinessMode();
    $("select").on("change",function(){
        $("#btn_query").click();
    });
    $("#cargoType").change(function(){
        $("#cargoName").empty();
        $("#cargoName").append("<option>全部</option>");
        initCargoList();
        $("#externalCompany").empty();
        $("#externalCompany").append("<option>全部</option>");
        initExternalCompany();
    });
    initCargoList();
    initExternalCompany();
    initOriginCountry();
    initWarehouse();
    initDestinationPort();
});


var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_cargo').bootstrapTable({
            url: '/trade/cargo/preall',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            onLoadSuccess:function(data){
                getTotalStore();
            },
            onDblClickRow:function(row){
                window.location.href = "/trade/cargo/presale?id="+row.id;
            },
            columns: [{
                checkbox: true
            },{
                field: 'contractNo',
                title: '内合同编号',
                formatter: function(value,row,index){
                    var s = '<a href="/trade/contract/viewByEC?externalContract='+value+'">'+value+'</a>';
                    return s;
                }
            },{
                field: 'importContractNo',
                title: '进口合同编号'
            },{
                field: 'companyNo',
                title: '厂号'
            },{
                field: 'cargoName',
                title: '产品名称'
            }, {
                field: 'level',
                title: '级别'
            }, {
                field: 'storageCondition',
                title: '存储条件'
            }, {
                field: 'cargoNo',
                title: '库号'
            }, {
                field: 'etd',
                title: 'ETD'
            }, {
                field: 'eta',
                title: 'ETA'
            }, {
                field: 'expectStoreWeight',
                title: '未预售重量(KG)',
                formatter: function(value, row, index){
                    if(value == null || value == '') return 0;
                    else return toFloat(value);
                }
            }, {
                field: 'yysWeight',
                title: '已预售重量(KG)',
                visible: false
            }, {
                field: 'customerName',
                title: '预售客户名称',
                visible: false
            }, {
                field: 'expectSailingDate',
                title: '预计船期',
                visible: false
            }, {
                field: 'destinationPort',
                title: '目的港',
                visible: false
            }, {
                field: 'businessMode',
                title: '业务模式',
                visible: false
            }, {
                field: 'status',
                title: '状态',
                formatter: function(value, row, index){
                    if(row.status == "1") return "已下单";
                    else if(row.status == "2") return "已装船";
                    else if(row.status == "3") return "已到港";
                    else if(row.status == "4") return "已入库";
                    else if(row.status == "5") return "已售完";
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function(value, row, index){
                        var s = '<a href="/trade/cargo/presale?id='+value+'">查看</a>';
                        return s;
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

        var level = $("#level").val() == "全部" ? "" : $("#level").val();

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
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            contractNo: $("#contractNo").val(),
            insideContract: $("#insideContract").val(),
            cargoNo: $("#cargoNo").val(),
            customerName: $("#customerName").val(),
            cargoType: $("#cargoType").val(),
            externalCompany: externalCompany,
            originCountry: originCountry,
            cargoName: cargoName,
            level: level,
            destinationPort: destinationPort,
            companyNo: $("#companyNo").val(),
            cmpRel: $("#cmpRel").val(),
            status: $("#status").val() == "全部"?"":$("#status").val(),
            contractStartDate: $("#contractStartDate").val(),
            expectSaleDateStart: $("#expectSaleDateStart").val(),
            expectSaleDateEnd: $("#expectSaleDateEnd").val(),
            contractEndDate: $("#contractEndDate").val(),
            minWeight: $("#minWeight").val(),
            maxWeight: $("#maxWeight").val(),
            etdStartDate: $("#etdStartDate").val(),
            etdEndDate: $("#etdEndDate").val(),
            etaStartDate: $("#etaStartDate").val(),
            etaEndDate: $("#etaEndDate").val(),
            businessMode: businessMode,
            ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
            storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val()
        };
        return temp;
    };
    return oTableInit;
};

function getTotalStore(){
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

    var level = $("#level").val() == "全部" ? "" : $("#level").val();

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
    var queryParams = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        contractNo: $("#contractNo").val(),
        insideContract: $("#insideContract").val(),
        cargoNo: $("#cargoNo").val(),
        customerName: $("#customerName").val(),
        cargoType: $("#cargoType").val(),
        externalCompany: externalCompany,
        originCountry: originCountry,
        cargoName: cargoName,
        level: level,
        cmpRel: $("#cmpRel").val(),
        destinationPort: destinationPort,
        companyNo: $("#companyNo").val(),
        expectSaleDateStart: $("#expectSaleDateStart").val(),
        expectSaleDateEnd: $("#expectSaleDateEnd").val(),
        status: $("#status").val() == "全部"?"":$("#status").val(),
        contractStartDate: $("#contractStartDate").val(),
        contractEndDate: $("#contractEndDate").val(),
        minWeight: $("#minWeight").val(),
        maxWeight: $("#maxWeight").val(),
        etdStartDate: $("#etdStartDate").val(),
        etdEndDate: $("#etdEndDate").val(),
        etaStartDate: $("#etaStartDate").val(),
        etaEndDate: $("#etaEndDate").val(),
        businessMode: businessMode,
        ownerCompany:$("#ownerCompany").val() == "全部"?"":$("#ownerCompany").val(),
        storageCondition: $("#storageCondition").val() == "全部"?"":$("#storageCondition").val()
    };
    $.ajax({
        url:"/trade/cargo/getPreTotal",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            $("#totalStoreAmount").html("0");
            $("#totalContractAmount").html("0");
            if(res.status == "1"){
                $("#totalStoreAmount").html(toFloat(res.totalStoreAmount));
                $("#totalContractAmount").html(toFloat(res.totalContractAmount));
            }
        }
    });
}

var ButtonInit = function () {
    var oInit = new Object();

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        $("#btn_query").click(function(){
            $('#tb_cargo').bootstrapTable("refresh",{pageNumber:1});
            getTotalStore();
        });
        $("#btn_reset").click(function(){
            resetQuery();
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
    $("#level").val("全部").trigger("change");
    $("#status").val("全部").trigger("change");
    $("#storageCondition").val("全部").trigger("change");
    $("#businessMode").val("全部").trigger("change");
    $("#ownerCompany").val("全部").trigger("change");
    $("#cargoType").val("全部").trigger("change");
    $("#cmpRel").val("1").trigger("change");
    $("#storeStartDate").val("");
    $("#storeEndDate").val("");
    $("#containerNo").val("");
    $("#companyNo").val("");
    $("#customerName").val("");
    $("#totalStoreAmount").html("");
    $("#totalContractAmount").html("");
    $("#externalContract").val("");
    $("#insideContract").val("");
    $("#contractStartDate").val("");
    $("#contractEndDate").val("");
    $("#containerNo").val("");
    $("#ladingbillNo").val("");
    $("#storageCondition").val("全部").trigger("change");
    $("#agent").val("全部").trigger("change");
    $("#companyNo").val("");
    $("#destinationPort").val("全部").trigger("change");
    $("#businessMode").val("全部").trigger("change");
    $("#status").val("全部").trigger("change");
    $("#externalCompany").val("全部").trigger("change");
    $("#level").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
    $("#originCountry").val("全部").trigger("change");
    $("#ownerCompany").val("全部").trigger("change");
    $("#etaStartDate").val("");
    $("#etaEndDate").val("");
    $("#etdStartDate").val("");
    $("#expectSaleDateStart").val("");
    $("#expectSaleDateEnd").val("");
    $("#etdEndDate").val("");
    $("#cargoNo").val("");
}
var toFloat = function (value) {
    value = Math.round(parseFloat(value) * 100) / 100;
    if (value.toString().indexOf(".") < 0) {
        value = value.toString() + ".00";
    }
    return value;
}