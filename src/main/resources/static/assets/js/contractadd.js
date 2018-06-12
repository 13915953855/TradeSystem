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
});

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_cargo').bootstrapTable({
            url: '/trade/cargo/list?contractId='+$("#contractId").val(),         //请求后台的URL（*）
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
                field: 'externalCompany',
                title: '外商'
            }, {
                field: 'cargoNo',
                title: '库号'
            }, {
                field: 'cargoName',
                title: '产品名称'
            }, {
                field: 'amount',
                title: '数量'
            }, {
                field: 'unitPrice',
                title: '单价'
            }, {
                field: 'contractValue',
                title: '合同金额'
            }, {
                field: 'saleCustomer',
                title: '销售客户'
            }, {
                field: 'unitPrePayAmount',
                title: '来款金额'
            }, {
                field: 'unitPrePayDate',
                title: '来款日期'
            }, {
                field: 'unitFinalPayAmount',
                title: '尾款金额'
            }, {
                field: 'unitFinalPayDate',
                title: '来款日期'
            }, {
                field: 'invoiceNumber',
                title: '发票数量'
            }, {
                field: 'invoiceValue',
                title: '发票金额'
            }, {
                field: 'elecSendDate',
                title: '电子版发送日期'
            }, {
                field: 'hystereticFee',
                title: '滞报费'
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
        $("#save").click(function(){
            saveContract();
        });
        $("#cancel").click(function(){
            window.location.href = "/trade/contract";
        });
        $("#btn_add").click(function(){

        });
        $("#btn_edit").click(function(){
            var data = $("#tb_cargo").bootstrapTable("getSelections");
            if(data.length == 1){
                setFormData(data[0]);
            }else if(data.length > 1){
                alert("只能选择一项进行编辑");
            }
        });
        $("#btn_del").click(function(){
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
                            alert("删除成功");
                        }else{
                            alert("删除失败");
                        }
                        $("#tb_cargo").bootstrapTable("refresh");
                    }
                });
            }
        });
        $("#save_cargo").click(function(){
            //todo 保存商品信息
            var cargo = {};
            if($("#contractId").val() != "") {
                cargo.contractId = $("#contractId").val();//合同序号
            }else {
                cargo.contractId = -1;//合同序号
            }
            if($("#externalCompany").val() == "") {
                $("#externalCompany").parent().addClass("has-error");
                return;
            }
            cargo.externalCompany = $("#externalCompany").val();//外商
            cargo.cargoNo = $("#cargoNo").val();//库号
            if($("#cargoName").val() == "") {
                $("#cargoName").parent().addClass("has-error");
                return;
            }
            cargo.cargoName = $("#cargoName").val();//产品名称
            cargo.amount = $("#amount").val() == "" ? 0:$("#amount").val();//数量
            cargo.unitPrice = $("#unitPrice").val() == "" ? 0:$("#unitPrice").val();//单价
            cargo.contractValue = $("#contractValue").val() == "" ? 0:$("#contractValue").val();//合同金额
            cargo.saleCustomer = $("#saleCustomer").val();//销售客户
            cargo.unitPrePayAmount = $("#unitPrePayAmount").val() == "" ? 0:$("#unitPrePayAmount").val();//来款金额
            cargo.unitPrePayDate = $("#unitPrePayDate").val();//来款日期
            cargo.unitFinalPayAmount = $("#unitFinalPayAmount").val() == "" ? 0:$("#unitFinalPayAmount").val();//尾款金额
            cargo.unitFinalPayDate = $("#unitFinalPayDate").val();//来款日期
            cargo.invoiceNumber = $("#invoiceNumber").val() == "" ? 0:$("#invoiceNumber").val();//发票数量
            cargo.invoiceValue = $("#invoiceValue").val() == "" ? 0:$("#invoiceValue").val();//发票金额
            cargo.elecSendDate = $("#elecSendDate").val();//电子版发送日期
            cargo.hystereticFee = $("#hystereticFee").val() == "" ? 0:$("#hystereticFee").val();//滞报费

            $.ajax({
                url:"/trade/cargo/add",
                type:"POST",
                dataType:"json",
                data:cargo,
                success:function(res){
                    if(res.status == "1"){
                        $('#myModal').modal('hide');
                        $("#tb_cargo").bootstrapTable("refresh");
                        resetForm("cargoForm");
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
    $("#id").val(data.id);
    $("#cargoId").val(data.cargoId);
    $("#externalCompany").val(data.externalCompany);
    $("#cargoNo").val(data.cargoNo);
    $("#cargoName").val(data.cargoName);
    $("#amount").val(data.amount);
    $("#unitPrice").val(data.unitPrice);
    $("#contractValue").val(data.contractValue);
    $("#saleCustomer").val(data.saleCustomer);
    $("#unitPrePayAmount").val(data.unitPrePayAmount);
    $("#unitPrePayDate").val(data.unitPrePayDate);
    $("#unitFinalPayAmount").val(data.unitFinalPayAmount);
    $("#unitFinalPayDate").val(data.unitFinalPayDate);
    $("#invoiceNumber").val(data.invoiceNumber);
    $("#invoiceValue").val(data.invoiceValue);
    $("#hystereticFee").val(data.hystereticFee);
    $("#elecSendDate").val(data.elecSendDate);
}
function saveContract(){
    var $btn = $("#save").button('loading');

    var contract = {};
    contract.id = $("#id").val();
    contract.contractId = $("#contractId").val();
    contract.externalContract = $("#externalContract").val();
    contract.insideContract = $("#insideContract").val();
    contract.businessMode = $("#businessMode").val();
    contract.companyNo = $("#companyNo").val();
    contract.contractDate = $("#contractDate").val();
    contract.warehouse = $("#warehouse").val();
    contract.storeDate = $("#storeDate").val();
    contract.specification = $("#specification").val();
    contract.originCountry = $("#originCountry").val();
    contract.shipmentPort = $("#shipmentPort").val();
    contract.destinationPort = $("#destinationPort").val();
    contract.priceCondition = $("#priceCondition").val();
    contract.etd = $("#etd").val();
    contract.eta = $("#eta").val();
    contract.prePayment = $("#prePayment").val() == "" ? 0:$("#prePayment").val();
    contract.prePaymentDate = $("#prePaymentDate").val();
    contract.preRate = $("#preRate").val() == "" ? 0:$("#preRate").val();
    contract.prePaymentRMB = $("#prePaymentRMB").val() == "" ? 0:$("#prePaymentRMB").val();
    contract.finalPayment = $("#finalPayment").val() == "" ? 0:$("#finalPayment").val();
    contract.finalPaymentDate = $("#finalPaymentDate").val();
    contract.finalRate = $("#finalRate").val() == "" ? 0:$("#finalRate").val();
    contract.finalPaymentRMB = $("#finalPaymentRMB").val() == "" ? 0:$("#finalPaymentRMB").val();
    if($("#isCheckElec").is(':checked')){
        contract.isCheckElec = "1";
    }else{
        contract.isCheckElec = "0";
    }
    contract.insuranceBuyDate = $("#insuranceBuyDate").val();
    contract.insuranceSendDate = $("#insuranceSendDate").val();
    contract.insuranceSignDate = $("#insuranceSignDate").val();
    contract.containerNo = $("#containerNo").val();
    contract.ladingbillNo = $("#ladingbillNo").val();
    contract.agent = $("#agent").val();
    contract.agentSendDate = $("#agentSendDate").val();
    contract.agentPassDate = $("#agentPassDate").val();
    contract.taxDeductibleParty = $("#taxDeductibleParty").val();
    contract.tariff = $("#tariff").val() == "" ? 0:$("#tariff").val();
    contract.addedValueTax = $("#addedValueTax").val() == "" ? 0:$("#addedValueTax").val();
    contract.taxPayDate = $("#taxPayDate").val();
    contract.taxSignDate = $("#taxSignDate").val();

    var a = $("#tb_cargo").bootstrapTable("getData");
    var cargoIds = "";
    for(var i=0;i<a.length;i++) {
        cargoIds += a[i].id;
        if(i < a.length - 1){
            cargoIds += ",";
        }
    }
    contract.cargoId = cargoIds;

    $.ajax({
        url:"/trade/contract/add",
        type:"POST",
        dataType:"json",
        data:contract,
        success:function(res){
            if(res.status == "1"){
                alert("保存成功");
                window.location.href = "/trade/contract";
            }else{

            }
            $btn.button('reset');
        }
    });
}