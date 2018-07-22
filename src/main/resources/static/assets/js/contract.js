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
    initExternalCompany();
    initCargoList();

    getTotalInfo();
});
function getTotalInfo(){
    var queryParams = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        externalContract: $("#externalContract").val(),
        insideContract: $("#insideContract").val(),
        contractStartDate: $("#contractStartDate").val(),
        contractEndDate: $("#contractEndDate").val(),
        etaStartDate: $("#etaStartDate").val(),
        etaEndDate: $("#etaEndDate").val(),
        cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
        cargoNo: $("#cargoNo").val(),
        level: $("#level").val() == "全部"?"":$("#level").val(),
        agent: $("#agent").val() == "全部"?"":$("#agent").val(),
        containerNo: $("#containerNo").val(),
        companyNo: $("#companyNo").val(),
        ladingbillNo: $("#ladingbillNo").val(),
        destinationPort: $("#destinationPort").val() == "全部"?"":$("#destinationPort").val(),
        businessMode: $("#businessMode").val() == "全部"?"":$("#businessMode").val(),
        externalCompany: $("#externalCompany").val() == "全部"?"":$("#externalCompany").val(),
        status: $("#status").val() == "全部"?"":$("#status").val(),
    };

    $.ajax({
        url:"/trade/contract/getTotalInfo",
        type:"POST",
        dataType:"json",
        data:queryParams,
        success:function(res){
            if(res.status == "1"){
                $("#totalContractMoney").html(res.totalContractMoney);
                $("#totalContractAmount").html(res.totalContractAmount);
                $("#totalInvoiceMoney").html(res.totalInvoiceMoney);
                $("#totalInvoiceAmount").html(res.totalInvoiceAmount);
            }
        }
    });
}
function initExternalCompany(){
    var opts = "";
    opts += "<option>JOC AUS</option>";
    opts += "<option>KPC HK</option>";
    opts += "<option>STANBROKE</option>";
    opts += "<option>JBS AUS</option>";
    opts += "<option>HARVEY</option>";
    opts += "<option>TEYS AUS</option>";
    opts += "<option>NH AUS</option>";
    opts += "<option>TIF</option>";
    opts += "<option>SANGER</option>";
    opts += "<option>MARFRIG</option>";
    opts += "<option>AACO</option>";
    $("#externalCompany").append(opts);
}

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_contract').bootstrapTable({
            url: '/trade/list',         //请求后台的URL（*）
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
                checkbox: true
            }, {
                field: 'id',
                title: '序号'
            }, {
                field: 'externalContract',
                title: '外合同编号'
            }, {
                field: 'insideContract',
                title: '内合同编号'
            }, {
                field: 'externalCompany',
                title: '外商'
            }, {
                field: 'contractDate',
                title: '合同日期'
            }, {
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
            }, {
                 field: 'id',
                 title: '操作',
                 formatter: function(value, row, index){
                    var s = '<a href="/trade/contract/view?id='+value+'">查看</a>';
                    return s;
                 }
             }  ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            externalContract: $("#externalContract").val(),
            insideContract: $("#insideContract").val(),
            contractStartDate: $("#contractStartDate").val(),
            contractEndDate: $("#contractEndDate").val(),
            etaStartDate: $("#etaStartDate").val(),
            etaEndDate: $("#etaEndDate").val(),
            cargoName: $("#cargoName").val() == "全部"?"":$("#cargoName").val(),
            cargoNo: $("#cargoNo").val(),
            level: $("#level").val() == "全部"?"":$("#level").val(),
            agent: $("#agent").val() == "全部"?"":$("#agent").val(),
            containerNo: $("#containerNo").val(),
            companyNo: $("#companyNo").val(),
            ladingbillNo: $("#ladingbillNo").val(),
            destinationPort: $("#destinationPort").val() == "全部"?"":$("#destinationPort").val(),
            businessMode: $("#businessMode").val() == "全部"?"":$("#businessMode").val(),
            externalCompany: $("#externalCompany").val() == "全部"?"":$("#externalCompany").val(),
            status: $("#status").val() == "全部"?"":$("#status").val(),
        };
        return temp;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        $("#htBtn").click(function(){
             if (this.checked){
                 $("#htDiv input[name='excelChk']:checkbox").prop("checked", true);
             } else {
                 $("#htDiv input[name='excelChk']:checkbox").prop("checked", false);
             }
         });

         $("#spBtn").click(function(){
             if (this.checked){
                 $("#spDiv input[name='excelChk']:checkbox").prop("checked", true);
             } else {
                 $("#spDiv input[name='excelChk']:checkbox").prop("checked", false);
             }
         });
         $("#jxBtn").click(function(){
             if (this.checked){
                 $("#jxDiv input[name='excelChk']:checkbox").prop("checked", true);
             } else {
                 $("#jxDiv input[name='excelChk']:checkbox").prop("checked", false);
             }
         });
        $("#btn_query").click(function(){
            $('#tb_contract').bootstrapTable("refresh",{pageNumber:1});
        });
        $("#btn_reset").click(function(){
            resetQuery();
        });
        $("#btn_add").click(function(){
            window.location.href="/trade/contract/add";
        });
        $("#btn_edit").click(function(){
            var a = $('#tb_contract').bootstrapTable('getSelections');
            if(a.length == 1){
                 var id = a[0].id;
                 window.location.href="/trade/contract/update?id="+id;
            }else if(a.length > 1){
                toastr.warning("仅支持编辑一行！");
            }else{
                toastr.warning("请选中一行！");
            }
        });
        $("#btn_delete").click(function(){
            if(confirm("确认删除吗？")){
                var a = $('#tb_contract').bootstrapTable('getSelections');
                var ids = "";
                for(var i=0;i<a.length;i++) {
                    ids += a[i].id+",";
                }
                if(ids != ""){
                    $.ajax({
                        url:"/trade/contract/delete",
                        type:"POST",
                        dataType:"json",
                        data:{"ids":ids},
                        success:function(res){
                            if(res.status == "1"){
                                toastr.success("删除成功");
                            }else{
                                toastr.error("删除失败");
                            }
                            $("#tb_contract").bootstrapTable("refresh",{pageNumber:1});
                        }
                    });
                }
            }
        });
        $("#btn_output").click(function(){
            var externalContract = $("#externalContract").val();
            var insideContract=$("#insideContract").val();
            var contractStartDate= $("#contractStartDate").val();
            var contractEndDate= $("#contractEndDate").val();
            var etaStartDate= $("#etaStartDate").val();
            var etaEndDate= $("#etaEndDate").val();
            var cargoName= $("#cargoName").val() == "全部"?"":$("#cargoName").val();
            var level= $("#level").val() == "全部"?"":$("#level").val();
            var agent= $("#agent").val() == "全部"?"":$("#agent").val();
            var containerNo=$("#containerNo").val();
            var cargoNo=$("#cargoNo").val();
            var companyNo= $("#companyNo").val();
            var ladingbillNo= $("#ladingbillNo").val();
            var destinationPort= $("#destinationPort").val() == "全部"?"":$("#destinationPort").val();
            var businessMode= $("#businessMode").val() == "全部"?"":$("#businessMode").val();
            var externalCompany=$("#externalCompany").val() == "全部"?"":$("#externalCompany").val();
            var status=$("#status").val() == "全部"?"":$("#status").val();

            var chk = new Array();
            $("input[name='excelChk']:checkbox").each(function(i){
                if (true == $(this).is(':checked')) {
                    chk[i] = "1";
                }else{
                    chk[i] = "0";
                }
            });

            var params = "?externalContract="+externalContract;
            params += "&insideContract="+insideContract;
            params += "&contractStartDate="+contractStartDate;
            params += "&contractEndDate="+contractEndDate;
            params += "&etaStartDate="+etaStartDate;
            params += "&etaEndDate="+etaEndDate;
            params += "&cargoName="+cargoName;
            params += "&level="+level;
            params += "&agent="+agent;
            params += "&containerNo="+containerNo;
            params += "&cargoNo="+cargoNo;
            params += "&companyNo="+companyNo;
            params += "&ladingbillNo="+ladingbillNo;
            params += "&destinationPort="+destinationPort;
            params += "&businessMode="+businessMode;
            params += "&externalCompany="+externalCompany;
            params += "&status="+status;
            params += "&chk="+chk;
        	var url = "/trade/contract/output"+params;
        	window.open(url);
        });
    };

    return oInit;
};

function resetQuery(){
    $("#externalContract").val("");
    $("#insideContract").val("");
    $("#contractStartDate").val("");
    $("#contractEndDate").val("");
    $("#containerNo").val("");
    $("#ladingbillNo").val("");
    $("#agent").val("全部").trigger("change");
    $("#companyNo").val("");
    $("#destinationPort").val("全部").trigger("change");
    $("#businessMode").val("全部").trigger("change");
    $("#status").val("全部").trigger("change");
    $("#externalCompany").val("全部").trigger("change");
    $("#level").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
    $("#etaStartDate").val("");
    $("#etaEndDate").val("");
    $("#cargoNo").val("");
}