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
    initWarehouse();
    initCargoList();
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
            url: '/trade/internal/list',         //请求后台的URL（*）
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
            onDblClickRow:function(row){
                window.location.href = "/trade/internal/contract/view?id="+row.id;
            },
            columns: [{
                checkbox: true
            }, {
                field: 'contractNo',
                title: '合同编号'
            }, {
                field: 'importContractNo',
                title: '进口合同号'
            }, {
                field: 'warehouse',
                title: '仓库'
            }, {
                field: 'contractDate',
                title: '合同日期'
            }, {
                field: 'storeDate',
                title: '入库日期'
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
                    var s = '<a href="/trade/internal/contract/view?id='+value+'">查看</a>';
                    return s;
                 }
             }  ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
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
        var cargoName = $("#cargoName").val() == "全部" ? "":$("#cargoName").val();
        var warehouse = $("#warehouse").val() == "全部" ? "":$("#warehouse").val();

        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            cargoName: cargoName,
            warehouse: warehouse,
            contractNo: $("#contractNo").val(),
            importContractNo: $("#importContractNo").val(),
            contractStartDate: $("#contractStartDate").val(),
            contractEndDate: $("#contractEndDate").val(),
            storeStartDate: $("#storeStartDate").val(),
            storeEndDate: $("#storeEndDate").val(),
            status: status,
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
        $("#btn_query").click(function(){
            $('#tb_contract').bootstrapTable("refresh",{pageNumber:1});
        });
        $("#btn_reset").click(function(){
            resetQuery();
        });
        $("#btn_add").click(function(){
            window.location.href="/trade/internal/contract/add";
        });
        $("#btn_edit").click(function(){
            var a = $('#tb_contract').bootstrapTable('getSelections');
            if(a.length == 1){
                 var id = a[0].id;
                 window.location.href="/trade/internal/contract/update?id="+id;
            }else if(a.length > 1){
                swal('仅支持编辑一行！','','warning');
            }else{
                swal('请选中一行！','','warning');
            }
        });
        $("#btn_copy").click(function(){
            swal({
              title: '复制合同',
              text: '确定复制吗？',
              type: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#3085d6',
              cancelButtonColor: '#d33',
              confirmButtonText: '确定',
              cancelButtonText: '取消',
            }).then(function(){
              var a = $('#tb_contract').bootstrapTable('getSelections');
              if(a.length == 1){
                   var id = a[0].id;
                   $.ajax({
                     url:"/trade/internal/contract/copy",
                     type:"POST",
                     dataType:"json",
                     data:{"id":id},
                     success:function(res){
                         if(res.status == "1"){
                             swal('复制成功！','','success');
                         }else{
                             swal('复制失败！','','error');
                         }
                         $("#tb_contract").bootstrapTable("refresh",{pageNumber:1});
                     }
                 });
              }else if(a.length > 1){
                  swal('仅支持编辑一行！','','warning');
              }else{
                  swal('请选中一行！','','warning');
              }
            });
        });
        $("#btn_delete").click(function(){
            swal({
              title: '确定删除吗？',
              text: '你将无法恢复它！',
              type: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#3085d6',
              cancelButtonColor: '#d33',
              confirmButtonText: '确定',
              cancelButtonText: '取消',
            }).then(function(){
              var a = $('#tb_contract').bootstrapTable('getSelections');
              var ids = "";
              for(var i=0;i<a.length;i++) {
                  ids += a[i].id+",";
              }
              if(ids != ""){
                  $.ajax({
                      url:"/trade/internal/contract/delete",
                      type:"POST",
                      dataType:"json",
                      data:{"ids":ids},
                      success:function(res){
                          if(res.status == "1"){
                              swal('删除成功！','','success');
                          }else{
                              swal('删除失败！','','error');
                          }
                          $("#tb_contract").bootstrapTable("refresh",{pageNumber:1});
                      }
                  });
              }
            });
        });
    };

    return oInit;
};

function resetQuery(){
    $("#contractNo").val("");
    $("#importContractNo").val("");
    $("#contractStartDate").val("");
    $("#contractEndDate").val("");
    $("#storeStartDate").val("");
    $("#storeEndDate").val("");
    $("#status").val("全部").trigger("change");
    $("#warehouse").val("全部").trigger("change");
    $("#cargoName").val("全部").trigger("change");
}