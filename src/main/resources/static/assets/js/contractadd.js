$(function () {
    //1.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    //2.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    initOriginCountry();
    initStorageCondition();
    initPriceCondition();
    initDestinationPort();
    initPayType();
    initExternalCompany();
    initBusinessMode();
    initLevel();
    initAgent();
    initBank();
    initWarehouse();
    initCargoList();
    initCaiyangcangku();
    initOwnerCompanyList();
    $("#level").select2({
        tags: true
    });

    $("#currency").blur(function(){
        $("#currencyNotice").html("提示：当前选择的币种是 "+$("#currency").val());
    });
    $("#currency").change(function(){
        $("#currencyNotice").html("提示：当前选择的币种是 "+$("#currency").val());
    });
    $("#cargoType").change(function(){
        $("#cargoName").empty();
        initCargoList();
        $("#externalCompany").empty();
        initExternalCompany();
    });

    payTypeChange();

    if($("#isNeedInsurance").checked){
        $("#baoxianDiv").show();
    }else{
        $("#baoxianDiv").hide();
    }
    $("#isNeedInsurance").change(function(){
        if(this.checked){
            $("#baoxianDiv").show();
        }else{
            $("#baoxianDiv").hide();
        }
    });

    if($("#caiyang").checked){
        $("#caiyangDiv").show();
    }else{
        $("#caiyangDiv").hide();
    }
    $("#caiyang").change(function(){
        if(this.checked){
            $("#caiyangDiv").show();
        }else{
            $("#caiyangDiv").hide();
        }
    });

    if($("#isFinancing").checked){
        $("#financingDiv").show();
    }else{
        $("#financingDiv").hide();
    }
    $("#isFinancing").change(function(){
        if(this.checked){
            $("#financingDiv").show();
        }else{
            $("#financingDiv").hide();
        }
    });

    if($("#isYahui").checked){
        $("#yahuiDiv").show();
    }else{
        $("#yahuiDiv").hide();
    }
    $("#isYahui").change(function(){
        if(this.checked){
            $("#yahuiDiv").show();
        }else{
            $("#yahuiDiv").hide();
        }
    });

    $("#unitPrice").blur(function(){
        var contractMoney = 0;
        var invoiceMoney = 0;
        var unitPrice = $("#unitPrice").val();
        var contractAmount = $("#contractAmount").val();
        var invoiceAmount = $("#invoiceAmount").val();
        contractMoney = toFloat(unitPrice * contractAmount);
        invoiceMoney = toFloat(unitPrice * invoiceAmount);
        $("#contractMoney").val(contractMoney);
        $("#invoiceMoney").val(invoiceMoney);

        //自动计算成本单价
        var costPrice = 0;
        var originCountry = $("#originCountry").val();
        var exchangeRate = $("#exchangeRate").val();
        if(originCountry == "澳大利亚"){
            costPrice = unitPrice*exchangeRate*1.06*1.09+0.6;
        }else if(originCountry == "新西兰" || originCountry == "哥斯达黎加"){
            costPrice = unitPrice*exchangeRate*1.09+0.6;
        }else{
            costPrice = unitPrice*exchangeRate*1.12*1.09+0.6;
        }
        $("#costPrice").val(toFloat(costPrice));

        //autoCalculateCostPrice();
    });

    $("#contractAmount").blur(function(){
        var contractMoney = 0;
        var unitPrice = $("#unitPrice").val();
        var contractAmount = $("#contractAmount").val();
        contractMoney = toFloat(unitPrice * contractAmount);
        $("#contractMoney").val(contractMoney);
    });
    $("#invoiceAmount").blur(function(){
        var invoiceMoney = 0;
        var unitPrice = $("#unitPrice").val();
        var invoiceAmount = $("#invoiceAmount").val();
        invoiceMoney = toFloat(unitPrice * invoiceAmount);
        $("#invoiceMoney").val(invoiceMoney);
        var realStoreMoney = 0;
        var costPrice = $("#costPrice").val();
        realStoreMoney = toFloat(costPrice * invoiceAmount);
        $("#realStoreMoney").val(realStoreMoney);
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

    getAllFile();

    var r = auth($("#user").val());
    if($("#action").val() == "view"){
        if(r != "1"){
            $("#totalContractMoney").hide();
            $("#totalInvoiceMoney").hide();
            $("#LCDiv").hide();
            $("#TTDiv").hide();
            $("#fushuiDiv").hide();
        }
    }

    //initChuanqi();
});
//自动计算成本单价
function autoCalculateCostPrice(){
    var costPrice = 0;
    var eta = $("#eta").val();
    var exchangeRate = $("#exchangeRate").val();
    var cargoType = $("#cargoType").val();
    var cargoName = $("#cargoName").val();

    var unitPrice = $("#unitPrice").val();
    var originCountry = $("#originCountry").val();

    if(eta == '' || exchangeRate == '' || cargoType == '' || cargoName == '' || originCountry == ''){
        return;
    }

    $.ajax({
        url:'/trade/cargo/autoCalculateCostPrice',
        type:"POST",
        dataType:"json",
        data:{"eta":eta,"originCountry":originCountry,"rate":exchangeRate,"unitPrice":unitPrice,"cargoType":cargoType,"cargoName":cargoName},
        success:function(res){
            $("#costPrice").val(res);
        }
    });

}

function rukudan(){
    var id = $("#id").val();
    var url = "/trade/contract/rukudan?id="+id;
    window.open(url);
}
function initChuanqi(){
    var now = new Date();
    var date = new Date(now.getTime() + 60 * 24 * 3600 * 1000);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    if($("#expectSailingDate").val() == ''){
        $("#expectSailingDate").val(year+'-'+month+'-'+day);
    }
}
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
function payTypeChange(){
    var type = $("#payType").val();
    if(type == "L/C"){
        $("#LCDiv").show();
        $("#TTDiv").hide();
    }else if(type == "TT"){
        $("#TTDiv").show();
        $("#LCDiv").hide();
    }else{
        $("#TTDiv").hide();
        $("#LCDiv").hide();
    }
    var r = auth($("#user").val());
    if($("#action").val() == "view"){
        if(r != "1"){
            $("#LCDiv").hide();
            $("#TTDiv").hide();
        }
    }
}
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
            showColumns:true,                  //是否显示内容列下拉框。
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
                field: 'businessMode',
                title: '业务模式',
                visible: false
            }, {
                field: 'companyNo',
                title: '厂号'
            }, {
                field: 'boxes',
                title: '箱数(小计)'
            }, {
                field: 'invoiceAmount',
                title: '发票重量(小计)'
            }, {
                field: 'unitPrice',
                title: '单价'
            }, {
                field: 'contractAmount',
                title: '合同重量(小计)',
                visible: false
            }, {
                field: 'contractMoney',
                title: '合同金额(小计)',
                visible: false
            }, {
                field: 'invoiceMoney',
                title: '发票金额(小计)',
                visible: false
            }, {
                field: 'costPrice',
                title: '成本单价(CNY/KG)',
                formatter: function(value, row, index){
                    var r = auth($("#user").val());
                    if(r == "1"){
                        return value;
                    }else{
                        return '--';
                    }
                }
            }, {
                field: 'realStoreBoxes',
                title: '库存箱数'
            }, {
                field: 'realStoreWeight',
                title: '库存重量'
            }, {
                field: 'realStoreMoney',
                title: '库存金额',
                visible: false
            }, {
                field: 'status',
                title: '状态',
                formatter: function(value, row, index){
                    if(value == 4) {
                        return '已入库';
                    }else if(value == 5){
                        return '已售完';
                    }else if(value == 1){
                        return '已下单';
                    }else if(value == 2){
                        return '已装船';
                    }else if(value == 3){
                        return '已到港';
                    }else{
                        return '-';
                    }
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function(value, row, index){
                    var link = '';
                    if($("#status").val() < 4){
                        link += '<a href="/trade/cargo/presale?id='+value+'">预售</a>';
                    }
                    if($("#status").val() == 4 || $("#status").val() == 5) {
                        link += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="/trade/cargo/view?id='+value+'">销售</a>';
                    }
                    return link;

                }
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
        $("#save").click(function(){
            saveContract();
        });
        $("#cancel").click(function(){
            window.location.href = "/trade/contract";
        });
        $("#btn_add").click(function(){
            resetForm("cargoForm");
            autoSetCargoNo();
            $("#myModal").modal('show');
        });
        $("#btn_edit").click(function(){
            var data = $("#tb_cargo").bootstrapTable("getSelections");
            if(data.length == 1){
                /*var status = data[0].status;
                if(status == 5){
                    swal("已售完的商品不可修改!","","warning");
                }else{
                    $("#myModal").modal('show');
                    setFormData(data[0]);
                }*/
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
            cargo.businessMode = $("#businessMode").val();
            cargo.companyNo = $("#companyNo").val();
            cargo.boxes = $("#boxes").val() == "" ? 0:parseInt($("#boxes").val());//箱数(小计)
            cargo.unitPrice = $("#unitPrice").val() == "" ? 0:toFloat($("#unitPrice").val());//单价
            cargo.contractAmount = $("#contractAmount").val() == "" ? 0:toFloat4($("#contractAmount").val());//合同数量(小计)
            cargo.contractMoney = $("#contractMoney").val() == "" ? 0:toFloat($("#contractMoney").val());//合同金额(小计)
            cargo.invoiceAmount = $("#invoiceAmount").val() == "" ? 0:toFloat4($("#invoiceAmount").val());//发票数量(小计)
            cargo.invoiceMoney = $("#invoiceMoney").val() == "" ? 0:toFloat($("#invoiceMoney").val());//发票金额(小计)
            cargo.costPrice = $("#costPrice").val() == "" ? 0:toFloat($("#costPrice").val());
            cargo.realStoreMoney = $("#realStoreMoney").val() == "" ? 0:toFloat($("#realStoreMoney").val());

            $.ajax({
                url:"/trade/cargo/add",
                type:"POST",
                dataType:"json",
                data:cargo,
                success:function(res){
                    if(res.status == "1"){
                        //自动计算发票总数量等值
                        autoSetTotalMoney();
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
    $("#cargoName").val('').trigger("change");
    $("#level").val('').trigger("change");
    $("#businessMode").val('').trigger("change");
    autoSetTotalMoney();
}
function autoSetTotalMoney(){
    /*if($("#action").val() == "update"){
        return;
    }*/
    var all = $('#tb_cargo').bootstrapTable('getData');
    var totalBoxes = 0;
    var totalInvoiceMoney = 0;
    var totalInvoiceAmount = 0;
    var totalContractMoney = 0;
    var totalContractAmount = 0;
    for(var i=0;i<all.length;i++) {
        totalBoxes += all[i].boxes;
        totalInvoiceMoney += all[i].invoiceMoney;
        totalInvoiceAmount += all[i].invoiceAmount;
        totalContractMoney += all[i].contractMoney;
        totalContractAmount += all[i].contractAmount;
    }
    $("#totalBoxes").val(parseInt(totalBoxes));
    $("#totalInvoiceMoney").val(toFloat(totalInvoiceMoney));
    $("#totalInvoiceAmount").val(toFloat4(totalInvoiceAmount));
    $("#totalContractMoney").val(toFloat(totalContractMoney));
    $("#totalContractAmount").val(toFloat4(totalContractAmount));
}
function setFormData(data){
    var r = auth($("#user").val());
    $("#_id").val(data.id);
    $("#cargoId").val(data.cargoId);
    $("#cargoNo").val(data.cargoNo);
    checkSelectOptionExist("cargoName",data.cargoName);
    $("#cargoName").val(data.cargoName).trigger("change");
    checkSelectOptionExist("level",data.level);
    $("#level").val(data.level).trigger("change");
    checkSelectOptionExist("businessMode",data.businessMode);
    $("#businessMode").val(data.businessMode).trigger("change");
    $("#companyNo").val(data.companyNo);
    $("#boxes").val(data.boxes);
    $("#contractAmount").val(data.contractAmount);
    $("#contractMoney").val(data.contractMoney);
    $("#invoiceAmount").val(data.invoiceAmount);
    $("#invoiceMoney").val(data.invoiceMoney);
    $("#unitPrice").val(data.unitPrice);
    $("#costPrice").val(data.costPrice);
    $("#realStoreMoney").val(data.realStoreMoney);
}

function saveContract(){
    var $btn = $("#save").button('loading');

    var contract = {};
    contract.id = $("#id").val();
    contract.contractId = $("#contractId").val();
    if($("#externalContract").val() == "") {
        $("#externalContract").parent().addClass("has-error");
        $btn.button('reset');
        return;
    }

    contract.ownerCompany = $("#ownerCompany").val();
    contract.externalContract = $("#externalContract").val();
    contract.insideContract = $("#insideContract").val();
    contract.contractDate = $("#contractDate").val();
    contract.externalCompany = $("#externalCompany").val();
    contract.originCountry = $("#originCountry").val();
    contract.storageCondition = $("#storageCondition").val();
    contract.destinationPort = $("#destinationPort").val();
    contract.priceCondition = $("#priceCondition").val();
    contract.payType = $("#payType").val();
    contract.currency = $("#currency").val();
    contract.expectSailingDate = $("#expectSailingDate").val();
    contract.exchangeRate = $("#exchangeRate").val() == "" ? 0:toFloat($("#exchangeRate").val());
    contract.totalContractAmount = $("#totalContractAmount").val() == "" ? 0:toFloat4($("#totalContractAmount").val());
    contract.totalContractMoney = $("#totalContractMoney").val() == "" ? 0:toFloat($("#totalContractMoney").val());
    contract.totalInvoiceAmount = $("#totalInvoiceAmount").val() == "" ? 0:toFloat4($("#totalInvoiceAmount").val());
    contract.totalInvoiceMoney = $("#totalInvoiceMoney").val() == "" ? 0:toFloat($("#totalInvoiceMoney").val());
    contract.totalBoxes = $("#totalBoxes").val() == "" ? 0:parseInt($("#totalBoxes").val());
    contract.issuingBank = $("#issuingBank").val();
    contract.issuingDate = $("#issuingDate").val();
    contract.lcno = $("#lcno").val();
    contract.remittanceDate = $("#remittanceDate").val();
    contract.remittanceRate = $("#remittanceRate").val() == "" ? 0:toFloat($("#remittanceRate").val());
    contract.prePayment = $("#prePayment").val() == "" ? 0:toFloat($("#prePayment").val());
    contract.prePaymentDate = $("#prePaymentDate").val();
    contract.prePayBank = $("#prePayBank").val();
    contract.preRate = $("#preRate").val() == "" ? 0:toFloat($("#preRate").val());
    contract.finalPayment = $("#finalPayment").val() == "" ? 0:toFloat($("#finalPayment").val());
    contract.finalPaymentDate = $("#finalPaymentDate").val();
    contract.finalPayBank = $("#finalPayBank").val();
    contract.finalRate = $("#finalRate").val() == "" ? 0:toFloat($("#finalRate").val());
    if($("#isFinancing").is(':checked')){
        contract.isFinancing = "1";
    }else{
        contract.isFinancing = "0";
    }
    contract.baoguandan = "0";

    if($("#isYahui").is(':checked')){
        contract.isYahui = "1";
    }else{
        contract.isYahui = "0";
    }
    contract.yahuiMoney = $("#yahuiMoney").val() == "" ? 0:toFloat($("#yahuiMoney").val());
    contract.yahuiYearRate = $("#yahuiYearRate").val() == "" ? 0:toFloat($("#yahuiYearRate").val());
    contract.yahuiDayRate = $("#yahuiDayRate").val() == "" ? 0:toFloat($("#yahuiDayRate").val());
    contract.yahuidaoqiDate = $("#yahuidaoqiDate").val();
    contract.financingBank = $("#financingBank").val();
    contract.financingMoney = $("#financingMoney").val() == "" ? 0:toFloat($("#financingMoney").val());
    contract.financingRate = $("#financingRate").val() == "" ? 0:toFloat($("#financingRate").val());
    contract.daoqiRate = $("#daoqiRate").val() == "" ? 0:toFloat($("#daoqiRate").val());
    contract.financingDaoqi = $("#financingDaoqi").val();
    contract.containerNo = $("#containerNo").val();
    contract.ladingbillNo = $("#ladingbillNo").val();
    contract.containerSize = $("#containerSize").val();
    if($("#isNeedInsurance").is(':checked')){
        contract.isNeedInsurance = "1";
    }else{
        contract.isNeedInsurance = "0";
    }
    contract.insuranceBuyDate = $("#insuranceBuyDate").val();
    contract.insuranceMoney = $("#insuranceMoney").val() == "" ? 0:toFloat($("#insuranceMoney").val());
    contract.insuranceCompany = $("#insuranceCompany").val();
    contract.etd = $("#etd").val();
    contract.eta = $("#eta").val();
    contract.agentMoney = $("#agentMoney").val();
    contract.agent = $("#agent").val();
    contract.dzdjsdrq = $("#dzdjsdrq").val();
    contract.jyzqfrq = $("#jyzqfrq").val();
    contract.jyzzbsdrq = $("#jyzzbsdrq").val();
    contract.bgdcjrq = $("#bgdcjrq").val();
    contract.agentSendDate = $("#agentSendDate").val();
    contract.tariff = $("#tariff").val() == "" ? 0:toFloat($("#tariff").val());
    contract.tariffNo = $("#tariffNo").val();
    contract.addedValueTax = $("#addedValueTax").val() == "" ? 0:toFloat($("#addedValueTax").val());
    contract.tariff1 = $("#tariff1").val() == "" ? 0:toFloat($("#tariff1").val());
    contract.addedValueTax1 = $("#addedValueTax1").val() == "" ? 0:toFloat($("#addedValueTax1").val());
    contract.tariff2 = $("#tariff2").val() == "" ? 0:toFloat($("#tariff2").val());
    contract.addedValueTax2 = $("#addedValueTax2").val() == "" ? 0:toFloat($("#addedValueTax2").val());
    contract.tariff3 = $("#tariff3").val() == "" ? 0:toFloat($("#tariff3").val());
    contract.addedValueTax3 = $("#addedValueTax3").val() == "" ? 0:toFloat($("#addedValueTax3").val());
    contract.tariff4 = $("#tariff4").val() == "" ? 0:toFloat($("#tariff4").val());
    contract.addedValueTax4 = $("#addedValueTax4").val() == "" ? 0:toFloat($("#addedValueTax4").val());
    contract.tariff5 = $("#tariff5").val() == "" ? 0:toFloat($("#tariff5").val());
    contract.addedValueTax5 = $("#addedValueTax5").val() == "" ? 0:toFloat($("#addedValueTax5").val());
    contract.tariff6 = $("#tariff6").val() == "" ? 0:toFloat($("#tariff6").val());
    contract.addedValueTax6 = $("#addedValueTax6").val() == "" ? 0:toFloat($("#addedValueTax6").val());
    contract.taxPayDate = $("#taxPayDate").val();
    contract.taxSignDate = $("#taxSignDate").val();
    contract.taxDeductibleParty = $("#taxDeductibleParty").val();
    contract.agentPassDate = $("#agentPassDate").val();
    contract.warehouse = $("#warehouse").val();
    contract.storeDate = $("#storeDate").val();
    contract.remark = $("#remark").val();
    contract.cargoType = $("#cargoType").val();
    if($("#caiyang").is(':checked')){
        contract.caiyang = "1";
        contract.caiyangdate = $("#caiyangdate").val();
        contract.caiyangcangku = $("#caiyangcangku").val();
    }else{
        contract.caiyang = "0";
        contract.caiyangdate = "";
        contract.caiyangcangku = "";
    }
    contract.zhixiangfei = $("#zhixiangfei").val() == "" ? 0:toFloat($("#zhixiangfei").val());
    contract.zhigangfei = $("#zhigangfei").val() == "" ? 0:toFloat($("#zhigangfei").val());

    contract.jiesuanMoney = $("#jiesuanMoney").val();
    contract.jiesuanDate = $("#jiesuanDate").val();
    contract.dingjinMoney = $("#dingjinMoney").val();
    contract.dingjinDate = $("#dingjinDate").val();
    contract.weikuanMoney = $("#weikuanMoney").val();
    contract.weikuanDate = $("#weikuanDate").val();

    var a = $("#tb_cargo").bootstrapTable("getData");
    var cargoIds = "";
    for(var i=0;i<a.length;i++) {
        cargoIds += a[i].id;
        if(i < a.length - 1){
            cargoIds += ",";
        }
    }
    contract.cargoId = cargoIds;

    var url = "/trade/contract/add";
    if($("#version").val() != undefined){
        contract.version = $("#version").val();
        url = "/trade/contract/update";
    }
    $.ajax({
        url:url,
        type:"POST",
        dataType:"json",
        data:contract,
        success:function(res){
            if(res.status == "1"){
                swal("保存成功!","","success").then(function(){window.location.href = "/trade/contract";},function(dismiss){window.location.href = "/trade/contract";});
            }else if(res.status == "-2"){
                swal("此合同已被他人编辑过，请刷新页面后重新编辑再保存!","","warning");
            }
            $btn.button('reset');
        },error:function(){
            $btn.button('reset');
        }
    });
}

function downloadFile(id,contractId){
    var url = "/trade/attachment/download?id="+id+"&contractId="+contractId;
    window.open(url);
}
function getAllFile(){
    var contractId = $("#contractId").val();
    $.ajax({
        url:'/trade/attachment/getAll',
        type:"POST",
        dataType:"json",
        data:{'contractId':contractId},
        success:function(res){
            setFileDiv(res.data);
        }
    });
}
function setFileDiv(data){
    $("#fileDiv").html('');
    for(var i=0;i<data.length;i++){
        var fileRef = data[i].fileRef;
        var fileName = data[i].fileName;
        if(fileName.length > 60){
            fileName = fileName.substring(0,48)+"...";
        }

        var s = '<div class="col-md-3">';
        s += '<div class="metric" style="height:90px;">';
        s += '<span class="icon" style="cursor:pointer" onclick="downloadFile('+data[i].id+',\''+data[i].contractId+'\')"><i class="fa fa-download"></i></span>';
        s += '<p style="word-break:break-all;">';
        s += '<span style="font-size:16px;">'+fileRef+':'+fileName+'</span>';
        s += '</p>';
        s += '<div style="position:absolute; right:20px; bottom:30px;"><span style="cursor:pointer" onclick="deleteAttachment('+data[i].id+',\''+data[i].contractId+'\')"><i class="lnr lnr-trash"></i></span>';
        s += '</div></div></div>';

        $("#fileDiv").append(s);
    }
}

function deleteAttachment(id,contractId){
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
        $.ajax({
                url:'/trade/attachment/delete',
                type:"POST",
                dataType:"json",
                data:{'id':id,'contractId':contractId},
                success:function(res){
                    swal(
                        '删除！',
                        '你的文件已经被删除。',
                        'success'
                        );
                    getAllFile();
                }
            });
    }, function(dismiss) {
      // dismiss的值可以是'cancel', 'overlay','esc'
      // 'close', 'timer'
      if (dismiss === 'cancel' || dismiss === 'close' || dismiss === 'overlay') {
        swal('已取消！','','error');
      }
    });
}

/**
 * 1、 在新增台账时，当合同代理公司=海企长城，内合同号已经输入后，在新增第一个产品时，库号=内合同号；
 * 在新增第二个产品时，库号=内合同号，并且内合同号的第九位、第十位、第十一位的数字改成902；
 * 在新增第三个产品时，库号=内合同号，并且内合同号的第九位、第十位、第十一位的数字改成903；依次类推。。。
 * 2、 在新增台账时，当合同代理公司=紫荆华美，内合同号已经输入后，
 * 在新增第一个产品时，库号=内合同号，且去掉第四位数（M）,后面的数字往前顺移；
 * 在新增第二个产品时，库号=内合同号，且去掉第四位数（M）,后面的数字往前顺移，且将顺移后，第九位、第十位、第十一位的数字改成902；
 *
 * */
function autoSetCargoNo(){
    var ownerCompany = $("#ownerCompany").val();
    var insideContract = $("#insideContract").val();
    var contractId = $("#contractId").val();
//    $.ajax({
//        url:'/trade/cargo/count',
//        type:"POST",
//        dataType:"json",
//        data:{'contractId':contractId},
//        success:function(res){
//            var cargoNo = insideContract;
//
//            if(res.count > 0 && cargoNo.length > 10){
//                var tmp = 901 + res.count;
//                if(ownerCompany == "紫荆华美"){
//                    cargoNo = cargoNo.substring(0, 9) + tmp + cargoNo.substring(12,cargoNo.length);
//                }else{
//                    cargoNo = cargoNo.substring(0, 8) + tmp + cargoNo.substring(11,cargoNo.length);
//                }
//            }
//            if(ownerCompany == "紫荆华美"){
//                cargoNo = cargoNo.substring(0, 3) + cargoNo.substring(4,cargoNo.length);
//            }
//
//            $("#cargoNo").val(cargoNo);
//        }
//    });

}