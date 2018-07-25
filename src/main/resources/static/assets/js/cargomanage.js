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
    initCargoList();
});


var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_cargo').bootstrapTable({
            url: '/trade/cargo/all',         //请求后台的URL（*）
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
                window.location.href = "/trade/cargo/view?id="+row.id;
            },
            columns: [{
                checkbox: true
            },{
                field: 'contractNo',
                title: '外合同编号',
                formatter: function(value,row,index){
                    var s = '<a href="/trade/contract/viewByEC?externalContract='+value+'">'+value+'</a>';
                    return s;
                }
            },{
                field: 'insideContract',
                title: '内合同编号'
            },{
                field: 'containerNo',
                title: '柜号'
            },{
                field: 'ladingbillNo',
                title: '提单号'
            },{
                field: 'cargoName',
                title: '产品名称'
            }, {
                field: 'level',
                title: '级别'
            }, {
                field: 'cargoNo',
                title: '库号'
            }, {
                field: 'businessMode',
                title: '业务模式'
            }, {
                field: 'warehouse',
                title: '仓库'
            }, {
                field: 'storeDate',
                title: '入库日期'
            }, {
                field: 'realStoreWeight',
                title: '当前库存重量'
            }, {
                field: 'realStoreBoxes',
                title: '当前库存箱数'
            }, {
                field: 'status',
                title: '状态',
                formatter: function(value, row, index){
                    if(row.status == "1") return "已保存";
                    else if(row.status == "5") return "已售完";
                    else if(row.status == "9") return "编辑中";
                    else if(row.status == "0") return "已删除";
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function(value, row, index){
                    var status = row.status;
                    if(status == "1"){
                        var s = '<a href="/trade/cargo/view?id='+value+'">查看</a>';
                        return s;
                    }
                    return "";

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
            insideContract: $("#insideContract").val(),
            level: $("#level").val() == "全部"?"":$("#level").val(),
            cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
            cargoNo: $("#cargoNo").val(),
            customerName: $("#customerName").val(),
            containerNo: $("#containerNo").val(),
            ladingbillNo: $("#ladingbillNo").val(),
            status: $("#status").val() == "全部"?"":$("#status").val()
        };
        return temp;
    };
    return oTableInit;
};

function getTotalStore(){
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
        containerNo: $("#containerNo").val(),
        ladingbillNo: $("#ladingbillNo").val()
    };
    $.ajax({
        url:"/trade/cargo/getTotalStore",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            if(res.status == "1"){
                $("#totalStoreWeight").html(res.totalStoreWeight);
                $("#totalStoreBoxes").html(res.totalStoreBoxes);
            }
        }
    });
}

var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

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
    $("#storeStartDate").val("");
    $("#storeEndDate").val("");
}