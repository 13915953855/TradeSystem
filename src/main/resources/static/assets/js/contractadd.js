$(function () {
    //1.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    //2.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    initCargoList();
    initOriginCountry();
    initExternalCompany();

    $("#currency").blur(function(){
        $("#currencyNotice").html("提示：当前选择的币种是 "+$("#currency").val());
    });
    $("#currency").change(function(){
        $("#currencyNotice").html("提示：当前选择的币种是 "+$("#currency").val());
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
function payTypeChange(){
    var type = $("#payType").val();
    if(type == "L/C"){
        $("#LCDiv").show();
        $("#TTDiv").hide();
    }else{
        $("#TTDiv").show();
        $("#LCDiv").hide();
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
                field: 'boxes',
                title: '箱数(小计)'
            }, {
                field: 'unitPrice',
                title: '单价'
            }, {
                field: 'contractAmount',
                title: '合同重量(小计)'
            }, {
                field: 'contractMoney',
                title: '合同金额(小计)'
            }, {
                field: 'invoiceAmount',
                title: '发票重量(小计)'
            }, {
                field: 'invoiceMoney',
                title: '发票金额(小计)'
            }, {
                field: 'id',
                title: '操作',
                formatter: function(value, row, index){
                    var s = '<a href="/trade/cargo/view?id='+value+'">查看销售记录</a>';
                    return s;
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

        });
        $("#btn_edit").click(function(){
            var data = $("#tb_cargo").bootstrapTable("getSelections");
            if(data.length == 1){
                $("#myModal").modal('show');
                setFormData(data[0]);
            }else if(data.length > 1){
                toastr.warning("只能选择一项进行编辑");
                $("#myModal").modal('hide');
            }else {
                toastr.warning("请选中一行！");
                $("#myModal").modal('hide');
            }
        });
        $("#btn_del").click(function(){
            if(confirm("确认删除吗？")){
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
                                toastr.success("删除成功");
                            }else{
                                toastr.error("删除失败");
                            }
                            $("#tb_cargo").bootstrapTable("refresh");
                        }
                    });
                }
            }
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
            cargo.boxes = $("#boxes").val() == "" ? 0:$("#boxes").val();//箱数(小计)
            cargo.unitPrice = $("#unitPrice").val() == "" ? 0:$("#unitPrice").val();//单价
            cargo.contractAmount = $("#contractAmount").val() == "" ? 0:$("#contractAmount").val();//合同数量(小计)
            cargo.contractMoney = $("#contractMoney").val() == "" ? 0:$("#contractMoney").val();//合同金额(小计)
            cargo.invoiceAmount = $("#invoiceAmount").val() == "" ? 0:$("#invoiceAmount").val();//发票数量(小计)
            cargo.invoiceMoney = $("#invoiceMoney").val() == "" ? 0:$("#invoiceMoney").val();//发票金额(小计)

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
    autoSetTotalMoney();
}
function autoSetTotalMoney(){
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
    $("#totalBoxes").val(totalBoxes);
    $("#totalInvoiceMoney").val(totalInvoiceMoney);
    $("#totalInvoiceAmount").val(totalInvoiceAmount);
    $("#totalContractMoney").val(totalContractMoney);
    $("#totalContractAmount").val(totalContractAmount);
}
function setFormData(data){
    $("#_id").val(data.id);
    $("#cargoId").val(data.cargoId);
    $("#cargoNo").val(data.cargoNo);
    $("#cargoName").val(data.cargoName).trigger("change");
    $("#level").val(data.level).trigger("change");
    $("#unitPrice").val(data.unitPrice);
    $("#boxes").val(data.boxes);
    $("#contractAmount").val(data.contractAmount);
    $("#contractMoney").val(data.contractMoney);
    $("#invoiceAmount").val(data.invoiceAmount);
    $("#invoiceMoney").val(data.invoiceMoney);
    $("#costPrice").val(data.costPrice);
    $("#costMoney").val(data.costMoney);
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
    contract.externalContract = $("#externalContract").val();
    if($("#insideContract").val() == "") {
        $("#insideContract").parent().addClass("has-error");
        $btn.button('reset');
        return;
    }
    contract.insideContract = $("#insideContract").val();
    contract.contractDate = $("#contractDate").val();
    if($("#externalCompany").val() == "") {
        $("#externalCompany").parent().addClass("has-error");
        $btn.button('reset');
        return;
    }
    contract.externalCompany = $("#externalCompany").val();
    contract.originCountry = $("#originCountry").val();
    contract.companyNo = $("#companyNo").val();
    //contract.shipmentPort = $("#shipmentPort").val();
    contract.destinationPort = $("#destinationPort").val();
    contract.priceCondition = $("#priceCondition").val();
    contract.payType = $("#payType").val();
    contract.currency = $("#currency").val();
    contract.expectSailingDate = $("#expectSailingDate").val();
    contract.businessMode = $("#businessMode").val();
    contract.totalContractAmount = $("#totalContractAmount").val() == "" ? 0:$("#totalContractAmount").val();
    contract.totalContractMoney = $("#totalContractMoney").val() == "" ? 0:$("#totalContractMoney").val();
    contract.totalInvoiceAmount = $("#totalInvoiceAmount").val() == "" ? 0:$("#totalInvoiceAmount").val();
    contract.totalInvoiceMoney = $("#totalInvoiceMoney").val() == "" ? 0:$("#totalInvoiceMoney").val();
    contract.totalBoxes = $("#totalBoxes").val() == "" ? 0:$("#totalBoxes").val();
    contract.issuingBank = $("#issuingBank").val();
    contract.issuingDate = $("#issuingDate").val();
    contract.LCNo = $("#LCNo").val();
    contract.bankDaodanDate = $("#bankDaodanDate").val();
    contract.remittanceDate = $("#remittanceDate").val();
    contract.yahuidaoqiDate = $("#yahuidaoqiDate").val();
    contract.remittanceRate = $("#remittanceRate").val() == "" ? 0:$("#remittanceRate").val();
    contract.prePayment = $("#prePayment").val() == "" ? 0:$("#prePayment").val();
    contract.prePaymentDate = $("#prePaymentDate").val();
    contract.preRate = $("#preRate").val() == "" ? 0:$("#preRate").val();
    contract.finalPayment = $("#finalPayment").val() == "" ? 0:$("#finalPayment").val();
    contract.finalPaymentDate = $("#finalPaymentDate").val();
    contract.finalRate = $("#finalRate").val() == "" ? 0:$("#finalRate").val();
    contract.containerNo = $("#containerNo").val();
    contract.ladingbillNo = $("#ladingbillNo").val();
    //contract.shipCompany = $("#shipCompany").val();
    contract.containerSize = $("#containerSize").val();
    if($("#isNeedInsurance").is(':checked')){
        contract.isNeedInsurance = "1";
    }else{
        contract.isNeedInsurance = "0";
    }
    contract.insuranceBuyDate = $("#insuranceBuyDate").val();
    contract.insuranceMoney = $("#insuranceMoney").val() == "" ? 0:$("#insuranceMoney").val();
    contract.insuranceCompany = $("#insuranceCompany").val();
    contract.ETD = $("#etd").val();
    contract.ETA = $("#eta").val();
    if($("#isCheckElec").is(':checked')){
        contract.isCheckElec = "1";
    }else{
        contract.isCheckElec = "0";
    }
    if($("#QACertificate").is(':checked')){
        contract.QACertificate = "1";
    }else{
        contract.QACertificate = "0";
    }
    //contract.elecSendDate = $("#elecSendDate").val();
    //contract.exCompanySendBillDate = $("#exCompanySendBillDate").val();
    //contract.billSignDate = $("#billSignDate").val();
    contract.agent = $("#agent").val();
    contract.agentSendDate = $("#agentSendDate").val();
    contract.tariff = $("#tariff").val() == "" ? 0:$("#tariff").val();
    contract.tariffNo = $("#tariffNo").val();
    contract.addedValueTax = $("#addedValueTax").val() == "" ? 0:$("#addedValueTax").val();
    contract.taxPayDate = $("#taxPayDate").val();
    contract.taxSignDate = $("#taxSignDate").val();
    contract.taxDeductibleParty = $("#taxDeductibleParty").val();
    contract.agentPassDate = $("#agentPassDate").val();
    contract.warehouse = $("#warehouse").val();
    contract.storeDate = $("#storeDate").val();
    contract.remark = $("#remark").val();
    /*contract.tariffRate = $("#tariffRate").val() == "" ? 0:$("#tariffRate").val();
    contract.taxRate = $("#taxRate").val() == "" ? 0:$("#taxRate").val();
    contract.exchangeRate = $("#exchangeRate").val() == "" ? 0:$("#exchangeRate").val();*/

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
                toastr.success("保存成功");

                window.location.href = "/trade/contract";
            }else if(res.status == "-2"){
                toastr.warning("此合同已被他人编辑过，请刷新页面后重新编辑再保存。");
            }
            $btn.button('reset');
        },error:function(){
            $btn.button('reset');
        }
    });
}

function initCargoList(){
    var cargoList = "<optgroup label='去骨'>";
    cargoList += "<option></option>";
    cargoList += "<option>前胸</option>";
    cargoList += "<option>后胸</option>";
    cargoList += "<option>肋条肉</option>";
    cargoList += "<option>肋排边</option>";
    cargoList += "<option>内裙肉</option>";
    cargoList += "<option>外裙肉</option>";
    cargoList += "<option>薄裙肉</option>";
    cargoList += "<option>厚裙肉</option>";
    cargoList += "<option>嫩肩</option>";
    cargoList += "<option>台规腱</option>";
    cargoList += "<option>前后腱</option>";
    cargoList += "<option>牛霖</option>";
    cargoList += "<option>牛柳</option>";
    cargoList += "<option>牛腩</option>";
    cargoList += "<option>牛腩排</option>";
    cargoList += "<option>前胸盖</option>";
    cargoList += "<option>三角肩肉</option>";
    cargoList += "<option>后三角肉</option>";
    cargoList += "<option>上脑心</option>";
    cargoList += "<option>上脑</option>";
    cargoList += "<option>臀肉</option>";
    cargoList += "<option>臀腰肉心</option>";
    cargoList += "<option>西冷</option>";
    cargoList += "<option>眼肉</option>";
    cargoList += "<option>小米龙</option>";
    cargoList += "<option>大米龙</option>";
    cargoList += "<option>翼板肉</option>";
    cargoList += "<option>板腱</option>";
    cargoList += "<option>保乐肩</option>";
    cargoList += "<option>方切肩</option>";
    cargoList += "<option>腹肉心</option>";
    cargoList += "<option>牛小排</option>";
    cargoList += "<option>50CL碎肉</option>";
    cargoList += "<option>65CL碎肉</option>";
    cargoList += "</optgroup>";
    cargoList += "<optgroup label='带骨'>";
    cargoList += "<option>肩胛骨</option>";
    cargoList += "<option>肩胛仔骨</option>";
    cargoList += "<option>仔骨</option>";
    cargoList += "<option>战斧</option>";
    cargoList += "<option>胸排</option>";
    cargoList += "<option>椎骨</option>";
    cargoList += "<option>脊骨</option>";
    cargoList += "<option>腿骨</option>";
    cargoList += "<option>窝骨</option>";
    cargoList += "<option>脖骨</option>";
    cargoList += "<option>肩脊骨</option>";
    cargoList += "</optgroup>";
    cargoList += "<optgroup label='牛副'>";
    cargoList += "<option>腩筋</option>";
    cargoList += "<option>腿筋</option>";
    cargoList += "<option>板筋</option>";
    cargoList += "<option>隔膜</option>";
    cargoList += "<option>牛心</option>";
    cargoList += "<option>牛肾</option>";
    cargoList += "<option>牛尾</option>";
    cargoList += "</optgroup>";
    cargoList += "<optgroup label='脂肪'>";
    cargoList += "<option>胸口油</option>";
    cargoList += "<option>热切脂肪</option>";
    cargoList += "<option>冷切脂肪</option>";
    cargoList += "</optgroup>";
    $("#cargoName").append(cargoList);
}

function initOriginCountry(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>澳大利亚</option>";
    opts += "<option>乌拉圭</option>";
    opts += "<option>巴西</option>";
    opts += "<option>加拿大</option>";
    opts += "<option>美国</option>";
    opts += "<option>阿根廷</option>";
    opts += "<option>白俄罗斯</option>";
    opts += "<option>南非</option>";
    opts += "<option>哥斯达黎加</option>";
    opts += "<option>新西兰</option>";
    $("#originCountry").append(opts);
}

function initExternalCompany(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>JOC AUS</option>";
    opts += "<option>KPC HK</option>";
    opts += "<option>STANBROKE</option>";
    opts += "<option>JBS AUS</option>";
    opts += "<option>HARBEY</option>";
    opts += "<option>TEYS AUS</option>";
    opts += "<option>NH AUS</option>";
    opts += "<option>TIF</option>";
    opts += "<option>SANGER</option>";
    opts += "<option>MARFRIG</option>";
    opts += "<option>AACO</option>";
    $("#externalCompany").append(opts);
}