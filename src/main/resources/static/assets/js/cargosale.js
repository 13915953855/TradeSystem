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

   /* $("#expectSaleUnitPrice").blur(function(){
        autoSetExpectSaleMoney();
    });
    $("#expectSaleWeight").blur(function(){
        autoSetExpectSaleMoney();
    });*/
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
    $("#customerPayMoney2").blur(function(){
            autoSetCustomerMoney();
        });
    $("#customerPayMoney3").blur(function(){
            autoSetCustomerMoney();
        });
    $("#customerPayMoney4").blur(function(){
            autoSetCustomerMoney();
        });
    $("#customerPayMoney5").blur(function(){
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
    var invoiceAmount = $("#invoiceAmount").val();
    $("#invoiceAmount").val(toFloat4(invoiceAmount));
    /*var expectStoreWeight = $("#expectStoreWeight").val();
    $("#expectStoreWeight").val(toFloat4(expectStoreWeight));*/
    var realStoreWeight = $("#realStoreWeight").val();
    $("#realStoreWeight").val(toFloat4(realStoreWeight));
    var r = auth($("#user").val());
        if(r!="1"){
            $("#costPrice").hide();
        }
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
    var customerPayMoney2 = $("#customerPayMoney2").val() == ""?0:$("#customerPayMoney2").val();
    var customerPayMoney3 = $("#customerPayMoney3").val() == ""?0:$("#customerPayMoney3").val();
    var customerPayMoney4 = $("#customerPayMoney4").val() == ""?0:$("#customerPayMoney4").val();
    var customerPayMoney5 = $("#customerPayMoney5").val() == ""?0:$("#customerPayMoney5").val();
    var realSaleMoney = $("#realSaleMoney").val() == ""?0:$("#realSaleMoney").val();
    var deposit = $("#deposit").val() == ""?0:$("#deposit").val();
    paymentDiff = toFloat(parseFloat(customerPayMoney)+parseFloat(customerPayMoney2)+parseFloat(customerPayMoney3)+parseFloat(customerPayMoney4)+parseFloat(customerPayMoney5) + parseFloat(deposit) - realSaleMoney);
    $("#paymentDiff").val(paymentDiff);
    if(paymentDiff != 0){
        $("#deviationNotice").show();
    }else{
        $("#deviationNotice").hide();
    }
}
/*function autoSetExpectSaleMoney(){
    var expectSaleMoney = 0;
    var expectSaleWeight = $("#expectSaleWeight").val() == ""?0:$("#expectSaleWeight").val();
    var expectSaleUnitPrice = $("#expectSaleUnitPrice").val() == ""?0:$("#expectSaleUnitPrice").val();
    expectSaleMoney = toFloat(expectSaleWeight * expectSaleUnitPrice);
    $("#expectSaleMoney").val(expectSaleMoney);
}*/
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
            showColumns:true,                  //是否显示内容列下拉框。
            showRefresh: true,
            responseHandler:function(res){
                return res.rows;
            },
            onLoadSuccess:function(data){
                autoSetTotal(data);
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
                field: 'pickupDate',
                title: '提货<br>时间'
            }, {
                field: 'saleContractNo',
                title: '购销合同<br>/订单号',
                visible: false
            }, {
                 field: 'pickupUser',
                 title: '销售经理'
             }, {
                field: 'customerName',
                title: '客户名称'
             }, {
                field: 'customerType',
                title: '客户属性'
            }, {
                field: 'realSaleWeight',
                title: '实际销售<br>重量(KG)'
            }, {
                field: 'realSaleBoxes',
                title: '实际销<br>售箱数'
            }, {
                field: 'realSaleUnitPrice',
                title: '实际销售<br>单价(CNY/KG)'
            }, {
                field: 'realSaleMoney',
                title: '实际销<br>售金额'
            }, {
                field: 'realSaleDate',
                title: '出库单<br>时间',
                visible: false
            }, {
                field: 'customerPayDate',
                title: '客户来款<br>时间No.1',
                visible:false
            }, {
                field: 'customerPayMoney',
                title: '客户来款<br>金额No.1',
                visible:false
            }, {
                field: 'customerPayDate2',
                title: '客户来款<br>时间No.2',
                visible:false
            }, {
                field: 'customerPayMoney2',
                title: '客户来款<br>金额No.2',
                visible:false
            }, {
                field: 'customerPayDate3',
                title: '客户来款<br>时间No.3',
                visible:false
            }, {
                field: 'customerPayMoney3',
                title: '客户来款<br>金额No.3',
                visible:false
            }, {
                field: 'customerPayDate4',
                title: '客户来款<br>时间No.4',
                visible:false
            }, {
                field: 'customerPayMoney4',
                title: '客户来款<br>金额No.4',
                visible:false
            }, {
                field: 'customerPayDate5',
                title: '客户来款<br>时间No.5',
                visible:false
            }, {
                field: 'customerPayMoney5',
                title: '客户来款<br>金额No.5',
                visible:false
            }, {
                field: 'depositDate',
                title: '定金<br>时间',
                visible:false
            }, {
                field: 'deposit',
                title: '定金',
                visible:false
            }, {
                field: 'paymentDiff',
                title: '货款<br>差额',
                visible:false
            }, {
                field: 'profit',
                title: '利润'
            }, {
                field: 'moneyClear',
                title: '是否<br>结清',
                formatter:function(value, row, index){
                    if(value == "1") return "是";
                    else return "否";
                },
                visible:false
            }, {
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
                            url:"/trade/sale/delete",
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
            sale.customerType = $("#customerType").val();
            //sale.expectSaleUnitPrice = $("#expectSaleUnitPrice").val() == "" ? 0:toFloat($("#expectSaleUnitPrice").val());
            //sale.expectSaleWeight = $("#expectSaleWeight").val() == "" ? 0:toFloat4($("#expectSaleWeight").val());
            //sale.expectSaleBoxes = $("#expectSaleBoxes").val() == "" ? 0:parseInt($("#expectSaleBoxes").val());
            //sale.expectSaleMoney = $("#expectSaleMoney").val() == "" ? 0:toFloat($("#expectSaleMoney").val());
            //sale.expectSaleDate = $("#expectSaleDate").val();
            sale.realSaleUnitPrice = $("#realSaleUnitPrice").val() == "" ? 0:toFloat($("#realSaleUnitPrice").val());
            sale.realSaleWeight = $("#realSaleWeight").val() == "" ? 0:toFloat4($("#realSaleWeight").val());
            sale.realSaleBoxes = $("#realSaleBoxes").val() == "" ? 0:parseInt($("#realSaleBoxes").val());
            sale.realSaleMoney = $("#realSaleMoney").val() == "" ? 0:toFloat($("#realSaleMoney").val());
            sale.realSaleDate = $("#realSaleDate").val();
            sale.depositDate = $("#depositDate").val();
            sale.remark = $("#remark").val();
            if($("#moneyClear").is(':checked')){
                sale.moneyClear = "1";
            }else{
                sale.moneyClear = "0";
            }
            if($("#kaifapiao").is(':checked')){
                sale.kaifapiao = "1";
            }else{
                sale.kaifapiao = "0";
            }
            sale.customerPayDate = $("#customerPayDate").val();
            sale.customerPayMoney = $("#customerPayMoney").val() == "" ? 0:toFloat($("#customerPayMoney").val());
            sale.customerPayDate2 = $("#customerPayDate2").val();
            sale.customerPayMoney2 = $("#customerPayMoney2").val() == "" ? 0:toFloat($("#customerPayMoney2").val());
            sale.customerPayDate3 = $("#customerPayDate3").val();
            sale.customerPayMoney3 = $("#customerPayMoney3").val() == "" ? 0:toFloat($("#customerPayMoney3").val());
            sale.customerPayDate4 = $("#customerPayDate4").val();
            sale.customerPayMoney4 = $("#customerPayMoney4").val() == "" ? 0:toFloat($("#customerPayMoney4").val());
            sale.customerPayDate5 = $("#customerPayDate5").val();
            sale.customerPayMoney5 = $("#customerPayMoney5").val() == "" ? 0:toFloat($("#customerPayMoney5").val());
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
    //$("#expectForm input[type=text]").attr("disabled","disabled");
    $("#saleId").val(data.saleId);
    $("#pickupWeight").val(data.pickupWeight);
    $("#pickupBoxes").val(data.pickupBoxes);
    $("#pickupDate").val(data.pickupDate);
    $("#pickupUser").val(data.pickupUser).trigger("change");
    $("#saleContractNo").val(data.saleContractNo);
    $("#customerName").val(data.customerName);
    $("#customerType").val(data.customerType).trigger("change");
    /*$("#expectSaleUnitPrice").val(data.expectSaleUnitPrice);
    $("#expectSaleWeight").val(data.expectSaleWeight);
    $("#expectSaleBoxes").val(data.expectSaleBoxes);
    $("#expectSaleMoney").val(data.expectSaleMoney);
    $("#expectSaleDate").val(data.expectSaleDate);*/
    $("#realSaleUnitPrice").val(data.realSaleUnitPrice);
    $("#realSaleWeight").val(data.realSaleWeight);
    $("#realSaleBoxes").val(data.realSaleBoxes);
    $("#realSaleMoney").val(data.realSaleMoney);
    $("#realSaleDate").val(data.realSaleDate);
    $("#depositDate").val(data.depositDate);
    $("#deposit").val(data.deposit);
    $("#customerPayMoney").val(data.customerPayMoney);
    $("#customerPayDate").val(data.customerPayDate);
    $("#customerPayMoney2").val(data.customerPayMoney2);
    $("#customerPayDate2").val(data.customerPayDate2);
    $("#customerPayMoney3").val(data.customerPayMoney3);
    $("#customerPayDate3").val(data.customerPayDate3);
    $("#customerPayMoney4").val(data.customerPayMoney4);
    $("#customerPayDate4").val(data.customerPayDate4);
    $("#customerPayMoney5").val(data.customerPayMoney5);
    $("#customerPayDate5").val(data.customerPayDate5);
    $("#profit").val(data.profit);
    $("#paymentDiff").val(data.paymentDiff);
    $("#remark").html(data.remark);
    if(data.moneyClear == 1){
        $("#moneyClear").attr("checked","checked");
    }
    if(data.kaifapiao == 1){
            $("#kaifapiao").attr("checked","checked");
        }
}

function autoSetTotal(data){
    var totalWeight = 0;
    var totalMoney = 0;
    for(var i=0;i<data.length;i++){
        var res = data[i];
        totalWeight = parseFloat(totalWeight) + parseFloat(res.realSaleWeight);
        totalMoney = parseFloat(totalMoney) + parseFloat(res.realSaleMoney);
    }
    $("#totalSaleWeight").html(toFloat(totalWeight));
    $("#totalSaleMoney").html(toFloat(totalMoney));
}

function checkStore(){
    var totalBoxes = 0;
    var all = $('#tb_sale').bootstrapTable('getData');
    for(var i=0;i<all.length;i++) {
        totalBoxes += all[i].realSaleBoxes;
    }
    var weight = $("#invoiceAmount").val() - $("#totalSaleWeight").html();
    weight = toFloat4(weight);
    var boxes = $("#boxes").val() - totalBoxes;
    var cargoId = $("#cargoId").val();

    $.ajax({
            url:"/trade/sale/checkStore",
            type:"POST",
            dataType:"json",
            data:{"cargoId":cargoId,"weight":weight,"boxes":boxes},
            success:function(res){
                window.location.href = window.location.href;
            }
        });
}

function initUser(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>顾春玲</option>";
    opts += "<option>张坚</option>";
    $("#pickupUser").append(opts);
}