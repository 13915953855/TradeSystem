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

    $("#expectSaleUnitPrice").blur(function(){
        autoSetExpectSaleMoney();
    });
    $("#expectSaleWeight").blur(function(){
        autoSetExpectSaleMoney();
    });
    $("#realSaleUnitPrice").blur(function(){
        autoSetRealSaleMoney();
    });
    $("#realSaleWeight").blur(function(){
        autoSetRealSaleMoney();
    });
    $("#realSaleMoney").blur(function(){
        autoSetCustomerMoney();
    });
    $("#customerPayMoney").blur(function(){
        autoSetCustomerMoney();
    });

    $("#realSaleUnitPrice").blur(function(){
        autoSetProfit();
    });
    $("#realSaleWeight").blur(function(){
        autoSetProfit();
    });
    $("#deposit").blur(function(){
        autoSetCustomerMoney();
    });
});
function autoSetProfit(){
    var profit = 0;
    var realSaleUnitPrice = $("#realSaleUnitPrice").val() == ""?0:$("#realSaleUnitPrice").val();
    var realSaleWeight = $("#realSaleWeight").val() == ""?0:$("#realSaleWeight").val();
    var costPrice = $("#costPrice").val() == ""?0:$("#costPrice").val();
    profit = toFloat((realSaleUnitPrice - costPrice)*realSaleWeight);
    $("#profit").val(profit);
}
function autoSetCustomerMoney(){
    var paymentDiff = 0;
    var customerPayMoney = $("#customerPayMoney").val() == ""?0:$("#customerPayMoney").val();
    var realSaleMoney = $("#realSaleMoney").val() == ""?0:$("#realSaleMoney").val();
    var deposit = $("#deposit").val() == ""?0:$("#deposit").val();
    paymentDiff = toFloat(parseFloat(customerPayMoney) + parseFloat(deposit) - realSaleMoney);
    $("#paymentDiff").val(paymentDiff);
    if(paymentDiff != 0){
        $("#deviationNotice").show();
    }else{
        $("#deviationNotice").hide();
    }
}
function autoSetExpectSaleMoney(){
    var expectSaleMoney = 0;
    var expectSaleWeight = $("#expectSaleWeight").val() == ""?0:$("#expectSaleWeight").val();
    var expectSaleUnitPrice = $("#expectSaleUnitPrice").val() == ""?0:$("#expectSaleUnitPrice").val();
    expectSaleMoney = toFloat(expectSaleWeight * expectSaleUnitPrice);
    $("#expectSaleMoney").val(expectSaleMoney);
}
function autoSetRealSaleMoney(){
    var realSaleMoney = 0;
    var realSaleWeight = $("#realSaleWeight").val() == ""?0:$("#realSaleWeight").val();
    var realSaleUnitPrice = $("#realSaleUnitPrice").val() == ""?0:$("#realSaleUnitPrice").val();
    realSaleMoney = toFloat(realSaleWeight * realSaleUnitPrice);
    $("#realSaleMoney").val(realSaleMoney);
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
                field: 'saleContractNo',
                title: '购销合同/订单号'
            }, {
                 field: 'pickupUser',
                 title: '销售经理'
             }, {
                field: 'customerName',
                title: '客户名称'
            }, {
                field: 'expectSaleWeight',
                title: '预销售重量(KG)'
            }, {
                field: 'expectSaleUnitPrice',
                title: '预销售单价(CNY/KG)'
            }, {
                field: 'expectSaleMoney',
                title: '预销售金额'
            }, {
                field: 'expectSaleDate',
                title: '预出库时间'
            }, {
                field: 'realSaleWeight',
                title: '实际销售重量(KG)'
            }, {
                field: 'realSaleBoxes',
                title: '实际销售箱数'
            }, {
                field: 'realSaleUnitPrice',
                title: '实际销售单价(CNY/KG)'
            }, {
                field: 'realSaleMoney',
                title: '实际销售金额'
            }, {
                field: 'realSaleDate',
                title: '出库单时间'
            }, {
                field: 'customerPayDate',
                title: '客户来款时间'
            }, {
                field: 'customerPayMoney',
                title: '客户来款金额'
            }, {
                field: 'depositDate',
                title: '定金时间'
            }, {
                field: 'deposit',
                title: '定金'
            }, {
                field: 'paymentDiff',
                title: '货款差额'
            }, {
                field: 'profit',
                title: '利润'
            }, {
                field: 'moneyClear',
                title: '是否已结清',
                formatter:function(value, row, index){
                    if(value == "1") return "是";
                    else return "否";
                }
            }, {
                 field: 'pickupDate',
                 title: '提货时间'
             } , {
                field: 'remark',
                title: '备注',
                visible:false
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
                $("#myModal").modal('show');
                setFormData(data[0]);
            }else if(data.length > 1){
                $("#myModal").modal('hide');
                toastr.warning("只能选择一项进行编辑");
            }else {
                $("#myModal").modal('hide');
                toastr.warning("请选中一行！");
            }
        });
        $("#btn_del").click(function(){
            if(confirm("确认删除吗？")){
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
                                toastr.success("删除成功");
                            }else{
                                toastr.error("删除失败");
                            }
                            setTimeout(function(){window.location.href = window.location.href;},2000);
                        }
                    });
                }
            }
        });
        $("#save_sale").click(function(){
            var sale = {};
            /*if($("#saleContractNo").val() == "") {
                $("#saleContractNo").parent().addClass("has-error");
                return;
            }*/
            sale.saleId = $("#saleId").val();
            sale.cargoId = $("#cargoId").val();
            sale.pickupDate = $("#pickupDate").val();
            sale.pickupUser = $("#pickupUser").val();
            sale.saleContractNo = $("#saleContractNo").val();//合同序号
            sale.customerName = $("#customerName").val();
            sale.expectSaleUnitPrice = $("#expectSaleUnitPrice").val() == "" ? 0:toFloat($("#expectSaleUnitPrice").val());
            sale.expectSaleWeight = $("#expectSaleWeight").val() == "" ? 0:toFloat($("#expectSaleWeight").val());
            sale.expectSaleBoxes = $("#expectSaleBoxes").val() == "" ? 0:parseInt($("#expectSaleBoxes").val());
            sale.expectSaleMoney = $("#expectSaleMoney").val() == "" ? 0:toFloat($("#expectSaleMoney").val());
            sale.expectSaleDate = $("#expectSaleDate").val();
            sale.realSaleUnitPrice = $("#realSaleUnitPrice").val() == "" ? 0:toFloat($("#realSaleUnitPrice").val());
            sale.realSaleWeight = $("#realSaleWeight").val() == "" ? 0:toFloat($("#realSaleWeight").val());
            sale.realSaleBoxes = $("#realSaleBoxes").val() == "" ? 0:parseInt($("#realSaleBoxes").val());
            sale.realSaleMoney = $("#realSaleMoney").val() == "" ? 0:toFloat($("#realSaleMoney").val());
            sale.realSaleDate = $("#realSaleDate").val();
            sale.customerPayDate = $("#customerPayDate").val();
            sale.depositDate = $("#depositDate").val();
            sale.remark = $("#remark").val();
            if($("#moneyClear").is(':checked')){
                    sale.moneyClear = "1";
                }else{
                    sale.moneyClear = "0";
                }
            sale.customerPayMoney = $("#customerPayMoney").val() == "" ? 0:toFloat($("#customerPayMoney").val());
            sale.deposit = $("#deposit").val() == "" ? 0:toFloat($("#deposit").val());
            sale.paymentDiff = $("#paymentDiff").val() == "" ? 0:toFloat($("#paymentDiff").val());
            sale.profit = $("#profit").val() == "" ? 0:toFloat($("#profit").val());

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

var toFloat = function (value) {
    value = Math.round(parseFloat(value) * 100) / 100;
    if (value.toString().indexOf(".") < 0) {
        value = value.toString() + ".00";
    }
    return value;
}

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
    $("#expectSaleUnitPrice").val(data.expectSaleUnitPrice);
    $("#expectSaleWeight").val(data.expectSaleWeight);
    $("#expectSaleBoxes").val(data.expectSaleBoxes);
    $("#expectSaleMoney").val(data.expectSaleMoney);
    $("#expectSaleDate").val(data.expectSaleDate);
    $("#realSaleUnitPrice").val(data.realSaleUnitPrice);
    $("#realSaleWeight").val(data.realSaleWeight);
    $("#realSaleBoxes").val(data.realSaleBoxes);
    $("#realSaleMoney").val(data.realSaleMoney);
    $("#realSaleDate").val(data.realSaleDate);
    $("#customerPayDate").val(data.customerPayDate);
    $("#depositDate").val(data.depositDate);
    $("#deposit").val(data.deposit);
    $("#customerPayMoney").val(data.customerPayMoney);
    $("#profit").val(data.profit);
    $("#paymentDiff").val(data.paymentDiff);
    $("#remark").html(data.remark);
    if(data.moneyClear == 1){
        $("#moneyClear").attr("checked","checked");
    }
}