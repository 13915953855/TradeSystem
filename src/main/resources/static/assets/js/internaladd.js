$(function () {
    //1.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    //2.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    initCargoList();
    initLevel();

    $("#contractAmount").blur(function(){
        var contractMoney = 0;
        var unitPrice = $("#unitPrice").val();
        var contractAmount = $("#contractAmount").val();
        contractMoney = toFloat(unitPrice * contractAmount);
        $("#contractMoney").val(contractMoney);
    });

    $("#costPrice").blur(function(){
        var realStoreMoney = 0;
        var costPrice = $("#costPrice").val();
        var invoiceAmount = $("#invoiceAmount").val();
        realStoreMoney = toFloat(costPrice * invoiceAmount);
        $("#realStoreMoney").val(realStoreMoney);
    });

    $(".form-date").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        minView: 2,
        maxView: 4,
        todayHighlight: true,
        language: 'zh-CN'
    });
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
        $('#tb_cargo').bootstrapTable({
            url: '/trade/internal/cargo/list?contractId='+$("#contractId").val(),         //请求后台的URL（*）
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
                autoSetTotalMoney();
            },
            columns: [{
                checkbox: true
            }, {
                field: 'id',
                title: '序号',
                visible: false
            }, {
                field: 'cargoId',
                title: '商品序号',
                visible: false
            }, {
                field: 'contractId',
                title: '合同序号',
                visible: false
            }, {
                field: 'cargoName',
                title: '产品名称'
            }, {
                field: 'level',
                title: '级别'
            }, {
                field: 'cargoNo',
                title: '库号'
            }, {
                field: 'companyNo',
                title: '厂号'
            }, {
                field: 'boxes',
                title: '采购箱数(小计)'
            }, {
                field: 'unitPrice',
                title: '采购单价'
            }, {
                field: 'costPrice',
                title: '成本单价(CNY/KG)'
            }, {
                field: 'contractAmount',
                title: '合同重量(小计)'
            }, {
                field: 'contractMoney',
                title: '合同金额(小计)'
            }, {
                field: 'invoiceAmount',
                title: '实际重量(小计)'
            }, {
                field: 'invoiceMoney',
                title: '实际金额(小计)'
            }, {
                field: 'status',
                title: '状态',
                formatter: function(value, row, index){
                    if(value == 1) {
                        return "已保存";
                    }else if(value == 5) {
                        return "已售完";
                    }else if(value == 9) {
                         return "编辑中";
                     }
                }
            }/*, {
                field: 'id',
                title: '操作',
                formatter: function(value, row, index){
                    var status = row.status;
                    if(status == 1 || status == 5) {
                        return '<a href="/trade/cargo/view?id='+value+'">查看销售记录</a>';
                    }else{
                        return "";
                    }

                }
            } */]
        });
    };
    return oTableInit;
};

var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        $("#save").click(function(){
            saveContract();
        });
        $("#cancel").click(function(){
            window.location.href = "/trade/internal";
        });
        $("#btn_add").click(function(){
            resetForm("cargoForm");
            $("#myModal").modal('show');
        });
        $("#btn_edit").click(function(){
            var data = $("#tb_cargo").bootstrapTable("getSelections");
            if(data.length == 1){
                $("#myModal").modal('show');
                setFormData(data[0]);
            }else if(data.length > 1){
                swal("只能选择一项进行编辑!","","warning");
                $("#myModal").modal('hide');
            }else {
                swal("请选中一行!","","warning");
                $("#myModal").modal('hide');
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
              confirmButtonText: '确定删除！',
              cancelButtonText: '取消删除！',
              confirmButtonClass: 'btn btn-success',
              cancelButtonClass: 'btn btn-danger',
              buttonsStyling: false
            }).then(function() {
                var a = $('#tb_cargo').bootstrapTable('getSelections');
                var ids = "";
                for(var i=0;i<a.length;i++) {
                    ids += a[i].id;
                    if(i<a.length-1){
                        ids += ",";
                    }
                }
                if(ids != ""){
                    $.ajax({
                        url:"/trade/cargo/delete",
                        type:"POST",
                        dataType:"json",
                        data:{"ids":ids},
                        success:function(res){
                            if(res.status == "1"){
                                swal("删除成功!","","success");
                            }else{
                                swal("删除失败!","","error");
                            }
                            $("#tb_cargo").bootstrapTable("refresh");
                        }
                    });
                }
            });

        });
        $("#save_cargo").click(function(){
            var cargo = {};
            cargo.id = $("#_id").val();
            cargo.cargoId = $("#cargoId").val();
            cargo.contractId = $("#contractId").val();//合同序号
            if($("#cargoName").val() == "") {
                $("#cargoName").parent().addClass("has-error");
                return;
            }
            cargo.cargoName = $("#cargoName").val();//产品名称
            cargo.level = $("#level").val();//级别
            cargo.cargoNo = $("#cargoNo").val();//库号
            cargo.companyNo = $("#companyNo").val();
            cargo.boxes = $("#boxes").val() == "" ? 0:parseInt($("#boxes").val());//箱数(小计)
            cargo.unitPrice = $("#unitPrice").val() == "" ? 0:toFloat($("#unitPrice").val());//单价
            cargo.contractAmount = $("#amount").val() == "" ? 0:toFloat4($("#amount").val());//合同数量(小计)
            cargo.costPrice = $("#costPrice").val() == "" ? 0:toFloat($("#costPrice").val());
            cargo.contractMoney = $("#money").val() == "" ? 0:toFloat4($("#money").val());
            cargo.invoiceAmount = $("#sjAmount").val() == "" ? 0:toFloat4($("#sjAmount").val());
            cargo.invoiceMoney = $("#sjMoney").val() == "" ? 0:toFloat4($("#sjMoney").val());
            cargo.realStoreMoney = 0;

            $.ajax({
                url:"/trade/cargo/add",
                type:"POST",
                dataType:"json",
                data:cargo,
                success:function(res){
                    if(res.status == "1"){
                        swal("保存成功!","","success").then(
                            function(){
                                $('#myModal').modal('hide');
                                $("#tb_cargo").bootstrapTable("refresh");
                                resetForm("cargoForm");
                            },function(dismiss){
                                $('#myModal').modal('hide');
                                $("#tb_cargo").bootstrapTable("refresh");
                                resetForm("cargoForm");
                            });

                    }
                },error:function(){
                    window.location.href="/login";
                }
            });
        });
    };

    return oInit;
};
function resetForm(formId){
    $("#"+formId+" input[type=text]").val('');
    $('#_id').val('');
    $('#cargoId').val('');
    $('#cargoNo').val('');
    $('#companyNo').val('');
    $('#unitPrice').val('');
    $('#costPrice').val('');
    $('#amount').val('');
    $('#boxes').val('');
    $("#cargoName").val('').trigger("change");
    $("#level").val('').trigger("change");
    autoSetTotalMoney();
}
function autoSetTotalMoney(){
    var all = $('#tb_cargo').bootstrapTable('getData');
    var totalBoxes = 0;
    var totalMoney = 0;
    var totalAmount = 0;
    for(var i=0;i<all.length;i++) {
        totalBoxes += all[i].boxes;
        totalMoney += all[i].contractMoney;
        totalAmount += all[i].contractAmount;
    }
    $("#totalBoxes").val(parseInt(totalBoxes));
    $("#totalMoney").val(toFloat(totalMoney));
    $("#totalAmount").val(toFloat4(totalAmount));
    $("#realMoney").val(toFloat(totalMoney));
    $("#realAmount").val(toFloat4(totalAmount));
}
function setFormData(data){
    $("#_id").val(data.id);
    $("#cargoId").val(data.cargoId);
    $("#cargoNo").val(data.cargoNo);
    checkSelectOptionExist("cargoName",data.cargoName);
    $("#cargoName").val(data.cargoName).trigger("change");
    checkSelectOptionExist("level",data.level);
    $("#level").val(data.level).trigger("change");
    checkSelectOptionExist("businessMode",data.businessMode);
    $("#companyNo").val(data.companyNo);
    $("#unitPrice").val(data.unitPrice);
    $("#boxes").val(data.boxes);
    $("#amount").val(data.contractAmount);
    $("#money").val(data.contractMoney);
    $("#costPrice").val(data.costPrice);
    $("#sjMoney").val(data.invoiceMoney);
    $("#sjAmount").val(data.invoiceAmount);
}

function saveContract(){
    var $btn = $("#save").button('loading');

    var contract = {};
    contract.id = $("#id").val();
    contract.contractId = $("#contractId").val();
    if($("#contractNo").val() == "") {
        $("#contractNo").parent().addClass("has-error");
        $btn.button('reset');
        return;
    }

    contract.ownerCompany = $("#ownerCompany").val();
    contract.contractNo = $("#contractNo").val();
    contract.contractDate = $("#contractDate").val();
    contract.supplier = $("#supplier").val();
    contract.payDate = $("#payDate").val();
    contract.receiptDate = $("#receiptDate").val();
    contract.payMoney = $("#payMoney").val() == "" ? 0:toFloat4($("#payMoney").val());
    contract.totalAmount = $("#totalAmount").val() == "" ? 0:toFloat($("#totalAmount").val());
    contract.totalMoney = $("#totalMoney").val() == "" ? 0:toFloat4($("#totalMoney").val());
    contract.realAmount = $("#realAmount").val() == "" ? 0:toFloat($("#realAmount").val());
    contract.realMoney = $("#realMoney").val() == "" ? 0:toFloat4($("#realMoney").val());
    contract.totalBoxes = $("#totalBoxes").val() == "" ? 0:parseInt($("#totalBoxes").val());
    contract.storeDate = $("#storeDate").val();
    contract.warehouse = $("#warehouse").val();

    var a = $("#tb_cargo").bootstrapTable("getData");
    var cargoIds = "";
    for(var i=0;i<a.length;i++) {
        cargoIds += a[i].id;
        if(i < a.length - 1){
            cargoIds += ",";
        }
    }
    contract.cargoId = cargoIds;

    var url = "/trade/internal/contract/add";
    if($("#version").val() != undefined){
        contract.version = $("#version").val();
        url = "/trade/internal/contract/update";
    }
    $.ajax({
        url:url,
        type:"POST",
        dataType:"json",
        data:contract,
        success:function(res){
            if(res.status == "1"){
                swal("保存成功!","","success").then(function(){window.location.href = "/trade/internal";},function(dismiss){window.location.href = "/trade/internal";});
            }else if(res.status == "-2"){
                swal("此合同已被他人编辑过，请刷新页面后重新编辑再保存!","","warning");
            }
            $btn.button('reset');
        },error:function(){
            $btn.button('reset');
        }
    });
}
