$(function () {
    //1.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    //2.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    $(".form-date").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        minView: 2,
        maxView: 4,
        todayHighlight: true,
        language: 'zh-CN'
    });
    initLevel();
    initUser();
    $("#cargoDiv input[type=text]").attr('disabled','disabled');
});

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_sale').bootstrapTable({
            url: '/trade/presale/list?cargoId='+$("#cargoId").val(),         //请求后台的URL（*）
            method: 'get',
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            showColumns: true,                  //是否显示所有的列
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            showColumns:true,                  //是否显示内容列下拉框。
            showRefresh: true,
            responseHandler:function(res){
                return res.rows;
            },
            /*onLoadSuccess:function(data){
                autoSetTotal(data);
            },*/
            columns: [{
                checkbox: true
            }, {
                field: 'saleId',
                title: '序号',
                visible: false
            }, {
                field: 'cargoId',
                title: '商品序号',
                visible: false
            },{
                 field: 'pickupUser',
                 title: '销售经理'
             }, {
                field: 'customerName',
                title: '客户名称'
             },{
                field: 'expectSaleWeight',
                title: '预售重量'
             },{
                field: 'expectSaleUnitPrice',
                title: '预售单价'
            }, {
                 field: 'expectSaleDate',
                 title: '预售时间'
             }]
        });
    };
    return oTableInit;
};

var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        $("#cancel").click(function(){
            window.location.href = "/trade/cargomanage";
        });
        $("#btn_add").click(function(){
            resetForm("myModal");
        });
        $("#btn_edit").click(function(){
            var data = $("#tb_sale").bootstrapTable("getSelections");
            if(data.length == 1){
                $("#myModal").modal('show');
                setFormData(data[0]);
            }else if(data.length > 1){
                $("#myModal").modal('hide');
                swal("只能选择一项进行编辑!","","warning");
            }else {
                $("#myModal").modal('hide');
                swal("请选中一行！","","warning");
            }
        });
        $("#btn_del").click(function(){
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
                    var a = $('#tb_sale').bootstrapTable('getSelections');
                    var ids = "";
                    for(var i=0;i<a.length;i++) {
                        ids += a[i].saleId;
                        if(i<a.length-1){
                            ids += ",";
                        }
                    }
                    if(ids != ""){
                        $.ajax({
                            url:"/trade/presale/delete",
                            type:"POST",
                            dataType:"json",
                            data:{"ids":ids},
                            success:function(res){
                                if(res.status == "1"){
                                    swal("删除成功!","","success");
                                }else{
                                    swal("删除失败","","error");
                                }
                                setTimeout(function(){window.location.href = window.location.href;},2000);
                            }
                        });
                    }
                });
        });
        $("#save_sale").click(function(){
            var sale = {};
            sale.saleId = $("#saleId").val();
            sale.cargoId = $("#cargoId").val();
            sale.pickupUser = $("#pickupUser").val();
            sale.customerName = $("#customerName").val();
            sale.expectSaleUnitPrice = $("#expectSaleUnitPrice").val() == "" ? 0:toFloat($("#expectSaleUnitPrice").val());
            sale.expectSaleWeight = $("#expectSaleWeight").val() == "" ? 0:toFloat4($("#expectSaleWeight").val());
            sale.expectSaleDate = $("#expectSaleDate").val();
            $.ajax({
                url:"/trade/presale/add",
                type:"POST",
                dataType:"json",
                data:sale,
                success:function(res){
                    if(res.status == "1"){
                        swal("保存成功!","","success").then(
                            function(){
                                $('#myModal').modal('hide');
                                resetForm("myModal");
                                window.location.href = window.location.href;
                            },function(dismiss){
                                $('#myModal').modal('hide');
                                resetForm("myModal");
                                window.location.href = window.location.href;
                            });

                    }
                }
            });
        });
    };

    return oInit;
};

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
function resetForm(formId){
    $("#"+formId+" input[type=text]").val('');
}

function setFormData(data){
    $("#saleId").val(data.saleId);
    $("#pickupUser").val(data.pickupUser).trigger("change");
    $("#customerName").val(data.customerName);
    $("#expectSaleUnitPrice").val(data.expectSaleUnitPrice);
    $("#expectSaleWeight").val(data.expectSaleWeight);
    $("#expectSaleDate").val(data.expectSaleDate);
}

function initUser(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>董建</option>";
    opts += "<option>景远方</option>";
    opts += "<option>凌骁</option>";
    opts += "<option>陶静</option>";
    opts += "<option>唐琦</option>";
    opts += "<option>唐誉天</option>";
    opts += "<option>徐舰艇</option>";
    opts += "<option>奚璐</option>";
    $("#pickupUser").append(opts);
}