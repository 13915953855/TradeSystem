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

    $("#cargoDiv input[type=text]").attr('disabled','disabled');
    $("#realSaleMoney").blur(function(){
        var realSaleMoney = $("#realSaleMoney").val();
        var customerPayMoney = $("#customerPayMoney").val();
        if(customerPayMoney > 0 && realSaleMoney != customerPayMoney){
            $("#deviationNotice").show();
        }else{
            $("#deviationNotice").hide();
        }
    });
    $("#customerPayMoney").blur(function(){
        var realSaleMoney = $("#realSaleMoney").val();
        var customerPayMoney = $("#customerPayMoney").val();
        if(realSaleMoney > 0 && realSaleMoney != customerPayMoney){
            $("#deviationNotice").show();
        }else{
            $("#deviationNotice").hide();
        }
    });
});
var toFloat = function (value) {
    value = Math.round(parseFloat(value) * 100) / 100;
    if (value.toString().indexOf(".") < 0) {
        value = value.toString() + ".00";
    }
    return value;
}

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_sale').bootstrapTable({
            url: '/trade/sale/list?cargoId='+$("#cargoId").val(),         //请求后台的URL（*）
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
            showColumns:false,                  //是否显示内容列下拉框。
            showRefresh: true,
            responseHandler:function(res){
                return res.rows;
            },
            onLoadSuccess:function(data){
                //autoSetTotal();
            },
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
            }, {
                field: 'pickupWeight',
                title: '提货重量(kg)'
            }, {
                field: 'pickupBoxes',
                title: '提货箱数'
            }, {
                field: 'pickupDate',
                title: '提货时间'
            }, {
                field: 'pickupUser',
                title: '提货人'
            }, {
                field: 'saleContractNo',
                title: '销售合同号'
            }, {
                field: 'customerName',
                title: '客户名称'
            }, {
                field: 'expectSaleWeight',
                title: '预销售重量(kg)'
            }, {
                field: 'expectSaleMoney',
                title: '预销售金额(元)'
            }, {
                field: 'expectSaleDate',
                title: '预出库时间'
            }, {
                field: 'realSaleWeight',
                title: '实际销售重量(kg)'
            }, {
                field: 'realSaleBoxes',
                title: '实际销售箱数'
            }, {
                field: 'realSaleMoney',
                title: '实际销售金额(元)'
            }, {
                field: 'realSaleDate',
                title: '出库单时间'
            }, {
                field: 'customerPayDate',
                title: '客户来款时间'
            }, {
                field: 'customerPayMoney',
                title: '客户来款金额(元)'
            } ]
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
        });
        $("#btn_edit").click(function(){
            var data = $("#tb_sale").bootstrapTable("getSelections");
            if(data.length == 1){
                $("#myModal").modal('hide');
                setFormData(data[0]);
            }else if(data.length > 1){
                alert("只能选择一项进行编辑");
                $("#myModal").modal('show');
            }else {
                alert("请选中一行！");
                $("#myModal").modal('show');
            }
        });
        $("#btn_del").click(function(){
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
                    url:"/trade/sale/delete",
                    type:"POST",
                    dataType:"json",
                    data:{"ids":ids},
                    success:function(res){
                        if(res.status == "1"){
                            alert("删除成功");
                        }else{
                            alert("删除失败");
                        }
                        window.location.href = window.location.href;
                    }
                });
            }
        });
        $("#save_sale").click(function(){
            var sale = {};
            if($("#saleContractNo").val() == "") {
                $("#saleContractNo").parent().addClass("has-error");
                return;
            }
            sale.saleId = $("#saleId").val();
            sale.cargoId = $("#cargoId").val();
            sale.pickupWeight = $("#pickupWeight").val() == "" ? 0:$("#pickupWeight").val();
            sale.pickupBoxes = $("#pickupBoxes").val() == "" ? 0:$("#pickupBoxes").val();
            sale.pickupDate = $("#pickupDate").val();
            sale.pickupUser = $("#pickupUser").val();
            sale.saleContractNo = $("#saleContractNo").val();//合同序号
            sale.customerName = $("#customerName").val();
            sale.expectSaleWeight = $("#expectSaleWeight").val() == "" ? 0:$("#expectSaleWeight").val();
            sale.expectSaleBoxes = $("#expectSaleBoxes").val() == "" ? 0:$("#expectSaleBoxes").val();
            sale.expectSaleMoney = $("#expectSaleMoney").val() == "" ? 0:$("#expectSaleMoney").val();
            sale.expectSaleDate = $("#expectSaleDate").val();
            sale.realSaleWeight = $("#realSaleWeight").val() == "" ? 0:$("#realSaleWeight").val();
            sale.realSaleBoxes = $("#realSaleBoxes").val() == "" ? 0:$("#realSaleBoxes").val();
            sale.realSaleMoney = $("#realSaleMoney").val() == "" ? 0:$("#realSaleMoney").val();
            sale.realSaleDate = $("#realSaleDate").val();
            sale.customerPayDate = $("#customerPayDate").val();
            sale.customerPayMoney = $("#customerPayMoney").val() == "" ? 0:$("#customerPayMoney").val();

            $.ajax({
                url:"/trade/sale/add",
                type:"POST",
                dataType:"json",
                data:sale,
                success:function(res){
                    if(res.status == "1"){
                        $('#myModal').modal('hide');
                        //$("#tb_sale").bootstrapTable("refresh");
                        resetForm("myModal");
                        window.location.href = window.location.href;
                    }
                }
            });
        });
    };

    return oInit;
};
function resetForm(formId){
    $("#"+formId+" input[type=text]").val('');
}

function setFormData(data){
    //$("#expectForm input[type=text]").attr("disabled","disabled");
    $("#saleId").val(data.saleId);
    $("#pickupWeight").val(data.pickupWeight);
    $("#pickupBoxes").val(data.pickupBoxes);
    $("#pickupDate").val(data.pickupDate);
    $("#pickupUser").val(data.pickupUser);
    $("#saleContractNo").val(data.saleContractNo);
    $("#customerName").val(data.customerName);
    $("#expectSaleWeight").val(data.expectSaleWeight);
    $("#expectSaleBoxes").val(data.expectSaleBoxes);
    $("#expectSaleMoney").val(data.expectSaleMoney);
    $("#expectSaleDate").val(data.expectSaleDate);
    $("#realSaleWeight").val(data.realSaleWeight);
    $("#realSaleBoxes").val(data.realSaleBoxes);
    $("#realSaleMoney").val(data.realSaleMoney);
    $("#realSaleDate").val(data.realSaleDate);
    $("#customerPayDate").val(data.customerPayDate);
    $("#customerPayMoney").val(data.customerPayMoney);
}