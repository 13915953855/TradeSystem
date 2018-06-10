$(function () {

    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    $("#contractDate").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        minView: 2,
        maxView: 4,
        todayHighlight: true,
        language: 'zh-CN'
    });
});


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
                title: '外合同'
            }, {
                field: 'insideContract',
                title: '内合同'
            }, {
                field: 'businessMode',
                title: '业务模式'
            }, {
                field: 'companyNo',
                title: '厂号'
            }, {
                field: 'contractDate',
                title: '合同日期',
                //sortable: true,
                formatter: function(value, row, index){
                    return value;
                }
            } ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            tradeName: $("#tradeName").val()
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
        $("#btn_add").click(function(){
            window.location.href="/trade/contract/add";
        });
        $("#btn_edit").click(function(){
            var a = $('#tb_contract').bootstrapTable('getSelections');
            if(a.length == 1){
                 var id = a[0].id;
                 window.location.href="/trade/contract/update?id="+id;
            }else if(a.length > 1){
                alert("仅支持编辑一行！");
            }else{
                alert("请选中一行！");
            }
        });
        $("#btn_delete").click(function(){
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
                            alert("删除成功");
                        }else{
                            alert("删除失败");
                        }
                    }
                });
            }
        });
        $("#btn_output").click(function(){
            $.ajax({
                url:"/trade/contract/output",
                type:"POST",
                dataType:"json",
                data:{},
                success:function(res){
                    if(res.status == "1"){
                        alert("下载成功");
                    }else{
                        alert("下载失败");
                    }
                }
            });
        });
    };

    return oInit;
};