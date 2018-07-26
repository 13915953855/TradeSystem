$(function () {
    //1.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();

    //2.初始化Table
    var oTable = new TableInit();
    oTable.Init();






/*var uploader = WebUploader.create({
    // 选完文件后，是否自动上传。
    auto: true,
    // swf文件路径
    swf: '/assets/webuploader/Uploader.swf',

    // 文件接收服务端。
    server: 'http://webuploader.duapp.com/server/fileupload.php',

    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',

    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false
});

// 当有文件被添加进队列的时候
uploader.on( 'fileQueued', function( file ) {
    $("#thelist").append( '<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
    '</div>' );
});
// 文件上传过程中创建进度条实时显示。
uploader.on( 'uploadProgress', function( file, percentage ) {
    var $li = $( '#'+file.id ),
        $percent = $li.find('.progress .progress-bar');

    // 避免重复创建
    if ( !$percent.length ) {
        $percent = $('<div class="progress progress-striped active">' +
          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
          '</div>' +
        '</div>').appendTo( $li ).find('.progress-bar');
    }

    $li.find('p.state').text('上传中');

    $percent.css( 'width', percentage * 100 + '%' );
});
uploader.on( 'uploadSuccess', function( file ) {
    $( '#'+file.id ).find('p.state').text('已上传');
});

uploader.on( 'uploadError', function( file ) {
    $( '#'+file.id ).find('p.state').text('上传出错');
});

uploader.on( 'uploadComplete', function( file ) {
    $( '#'+file.id ).find('.progress').fadeOut();
});*/





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
                field: 'businessMode',
                title: '业务模式'
            }, {
                field: 'companyNo',
                title: '厂号'
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
                field: 'costPrice',
                title: '成本单价(CNY/KG)'
            }, {
                field: 'realStoreMoney',
                title: '库存金额'
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
            }, {
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
            $("#myModal").modal('show');
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
            cargo.businessMode = $("#businessMode").val();
            cargo.companyNo = $("#companyNo").val();
            cargo.boxes = $("#boxes").val() == "" ? 0:parseInt($("#boxes").val());//箱数(小计)
            cargo.unitPrice = $("#unitPrice").val() == "" ? 0:toFloat($("#unitPrice").val());//单价
            cargo.contractAmount = $("#contractAmount").val() == "" ? 0:toFloat($("#contractAmount").val());//合同数量(小计)
            cargo.contractMoney = $("#contractMoney").val() == "" ? 0:toFloat($("#contractMoney").val());//合同金额(小计)
            cargo.invoiceAmount = $("#invoiceAmount").val() == "" ? 0:toFloat($("#invoiceAmount").val());//发票数量(小计)
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
    $('#_id').val('');
    $('#cargoId').val('');
    $("#cargoName").val('').trigger("change");
    $("#level").val('').trigger("change");
    $("#businessMode").val('').trigger("change");
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
    $("#totalBoxes").val(parseInt(totalBoxes));
    $("#totalInvoiceMoney").val(toFloat(totalInvoiceMoney));
    $("#totalInvoiceAmount").val(toFloat4(totalInvoiceAmount));
    $("#totalContractMoney").val(toFloat(totalContractMoney));
    $("#totalContractAmount").val(toFloat4(totalContractAmount));
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
    $("#businessMode").val(data.businessMode).trigger("change");
    $("#companyNo").val(data.companyNo);
    $("#unitPrice").val(data.unitPrice);
    $("#boxes").val(data.boxes);
    $("#contractAmount").val(data.contractAmount);
    $("#contractMoney").val(data.contractMoney);
    $("#invoiceAmount").val(data.invoiceAmount);
    $("#invoiceMoney").val(data.invoiceMoney);
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

    contract.externalContract = $("#externalContract").val();
    contract.insideContract = $("#insideContract").val();
    contract.contractDate = $("#contractDate").val();
    contract.externalCompany = $("#externalCompany").val();
    contract.originCountry = $("#originCountry").val();
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
    contract.preRate = $("#preRate").val() == "" ? 0:toFloat($("#preRate").val());
    contract.finalPayment = $("#finalPayment").val() == "" ? 0:toFloat($("#finalPayment").val());
    contract.finalPaymentDate = $("#finalPaymentDate").val();
    contract.finalRate = $("#finalRate").val() == "" ? 0:toFloat($("#finalRate").val());
    if($("#isFinancing").is(':checked')){
        contract.isFinancing = "1";
    }else{
        contract.isFinancing = "0";
    }
    if($("#isYahui").is(':checked')){
        contract.isYahui = "1";
    }else{
        contract.isYahui = "0";
    }
    contract.yahuiMoney = $("#yahuiMoney").val() == "" ? 0:toFloat($("#yahuiMoney").val());
    contract.yahuiYearRate = $("#yahuiYearRate").val() == "" ? 0:toFloat($("#yahuiYearRate").val());
    contract.yahuiDayRate = $("#yahuiDayRate").val() == "" ? 0:toFloat($("#yahuiDayRate").val());
    contract.yahuidaoqiDate = $("#yahuidaoqiDate").val();
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
    if($("#isCheckElec").is(':checked')){
        contract.isCheckElec = "1";
    }else{
        contract.isCheckElec = "0";
    }
    if($("#qacertificate").is(':checked')){
        contract.qacertificate = "1";
    }else{
        contract.qacertificate = "0";
    }
    if($("#hasbaoguan").is(':checked')){
        contract.hasbaoguan = "1";
    }else{
        contract.hasbaoguan = "0";
    }
    contract.agent = $("#agent").val();
    contract.agentSendDate = $("#agentSendDate").val();
    contract.tariff = $("#tariff").val() == "" ? 0:toFloat($("#tariff").val());
    contract.tariffNo = $("#tariffNo").val();
    contract.addedValueTax = $("#addedValueTax").val() == "" ? 0:toFloat($("#addedValueTax").val());
    contract.taxPayDate = $("#taxPayDate").val();
    contract.taxSignDate = $("#taxSignDate").val();
    contract.taxDeductibleParty = $("#taxDeductibleParty").val();
    contract.agentPassDate = $("#agentPassDate").val();
    contract.warehouse = $("#warehouse").val();
    contract.storeDate = $("#storeDate").val();
    contract.remark = $("#remark").val();
    contract.zhixiangfei = $("#zhixiangfei").val() == "" ? 0:toFloat($("#zhixiangfei").val());
    contract.zhigangfei = $("#zhigangfei").val() == "" ? 0:toFloat($("#zhigangfei").val());

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