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
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            showColumns: true,                  //是否显示所有的列
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
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
        $("#save").click(function(){
            saveContract();
        });
        $("#cancel").click(function(){
            window.location.href = "/trade/contract";
        });
        $("#btn_add").click(function(){
            alert(JSON.stringify($("#tb_cargo").bootstrapTable("getData")));
        });
        $("#save_cargo").click(function(){
            //todo 保存商品信息
            $("#tb_cargo").bootstrapTable("prepend",data);
        });
    };

    return oInit;
};

function saveContract(){
    var $btn = $("#save").button('loading');

    var contract = {};
    contract.id = $("#id").val();
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
    $.ajax({
        url:"/trade/contract/add",
        type:"POST",
        dataType:"json",
        data:contract,
        success:function(res){
            if(res.status == "1"){
                $btn.button('reset');
                alert("保存成功");
                window.location.href = "/trade/contract";
            }else{

            }
        }
    });
}