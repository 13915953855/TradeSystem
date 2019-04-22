$(function(){
    var toFloat = function (value) {
        value = Math.round(parseFloat(value) * 100) / 100;
        if (value.toString().indexOf(".") < 0) {
            value = value.toString() + ".00";
        }
        return value;
    }

//解决页面拉伸后，表格列不对齐的问题。
    $("#arrow").click(function(){
        setTimeout(function(){
            $("table").bootstrapTable("resetView");
        },300);
    });

    /*$("select").select2({
        tags: true
    });*/

    $.ajaxSetup({
                    contentType:"application/x-www-form-urlencoded;charset=utf-8",
                    complete:function(XMLHttpRequest,textStatus){
                        //通过XMLHttpRequest取得响应头，sessionstatus，
                        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");
                        if(sessionstatus=="timeout"){
                            //如果超时就处理 ，指定要跳转的页面
                            window.location = "/login";
                        }
                    }
                });


});

function checkSelectOptionExist(id,val){
    var flag = false;
    $("#"+id+" option").each(function(){
          //遍历所有option
          var value = $(this).val();   //获取option值
          var text = $(this).text();
          if(value == val){
              flag = true;
          }
    });
    if(!flag){
        $("#"+id).append("<option>"+val+"</option>");
    }
}
function initCargoList(){
    var group = "";
    if($("#cargoType").val() == undefined || $("#cargoType").val() == null || $("#cargoType").val() == ''){
        group = "牛产品";
    }else{
        group = $("#cargoType").val();
    }
    var cargoList = '';
    $.ajax({
        url:'/trade/common/getOption',
        type:"POST",
        dataType:"json",
        data:{'group':group,'field':'cargoName'},
        success:function(res){
            for(var i=0;i<res.data.length;i++){
                cargoList += "<option>"+res.data[i].name+"</option>";
            }
            $("#cargoName").append(cargoList);
        }
    });
}

function initWarehouse(){
    var opts = "";
    opts += "<option>库外</option>";
    opts += "<option>澳米特</option>";
    opts += "<option>名联纪丰</option>";
    opts += "<option>名联青浦</option>";
    opts += "<option>领升</option>";
    opts += "<option>鑫汇洋</option>";
    opts += "<option>瑞源</option>";
    opts += "<option>洋山普菲斯</option>";
    opts += "<option>上港冷链</option>";
    opts += "<option>华辰</option>";
    $("#warehouse").append(opts);
}

function initBank(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>光大银行</option>";
    opts += "<option>广发银行</option>";
    opts += "<option>工商银行</option>";
    opts += "<option>建设银行</option>";
    opts += "<option>江苏银行</option>";
    opts += "<option>交通银行</option>";
    opts += "<option>南京银行</option>";
    opts += "<option>农业发展银行</option>";
    opts += "<option>浦发银行</option>";
    opts += "<option>紫金农商银行</option>";
    opts += "<option>招商银行</option>";
    $("#prePayBank").append(opts);
    $("#finalPayBank").append(opts);
    $("#issuingBank").append(opts);
}

function initOriginCountry(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>澳大利亚</option>";
    opts += "<option>爱尔兰</option>";
    opts += "<option>阿根廷</option>";
    opts += "<option>白俄罗斯</option>";
    opts += "<option>巴西</option>";
    opts += "<option>比利时</option>";
    opts += "<option>丹麦</option>";
    opts += "<option>德国</option>";
    opts += "<option>法国</option>";
    opts += "<option>荷兰</option>";
    opts += "<option>加拿大</option>";
    opts += "<option>美国</option>";
    opts += "<option>葡萄牙</option>";
    opts += "<option>乌拉圭</option>";
    opts += "<option>西班牙</option>";
    opts += "<option>匈牙利</option>";
    opts += "<option>新西兰</option>";
    opts += "<option>英国</option>";
    opts += "<option>智利</option>";
    $("#originCountry").append(opts);
    /*var group = "";
    if($("#cargoType").val() == undefined || $("#cargoType").val() == null || $("#cargoType").val() == ''){
        group = "牛产品";
    }else{
        group = $("#cargoType").val();
    }
    var opts = '';
    $.ajax({
        url:'/trade/common/getOption',
        type:"POST",
        dataType:"json",
        data:{'group':group,'field':'originCountry'},
        success:function(res){
            for(var i=0;i<res.data.length;i++){
                opts += "<option>"+res.data[i].name+"</option>";
            }
            $("#originCountry").append(opts);
        }
    });*/
}

function initAgent() {
    var opts = "";
    opts += "<option></option>";
    opts += "<option>佳克</option>";
    opts += "<option>欧恒</option>";
    opts += "<option>捷嘉</option>";
    opts += "<option>源丰祥</option>";
    opts += "<option>同华</option>";
    opts += "<option>亚东</option>";
    opts += "<option>创祥</option>";
    $("#agent").append(opts);
}

function initStorageCondition(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>冷冻</option>";
    opts += "<option>冰鲜</option>";
    $("#storageCondition").append(opts);
    $("#storageCondition").val('冷冻').trigger("change");
}

function initPriceCondition(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>CIF</option>";
    opts += "<option>CFR</option>";
    $("#priceCondition").append(opts);
}
function initPayType(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>TT</option>";
    opts += "<option>L/C</option>";
    $("#payType").append(opts);
    $("#payType").val('TT').trigger("change");
}
function initDestinationPort(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>上海</option>";
    opts += "<option>天津</option>";
    opts += "<option>深圳</option>";
    opts += "<option>连云港</option>";
    opts += "<option>盐田</option>";
    opts += "<option>南京</option>";
    opts += "<option>太仓</option>";
    opts += "<option>张家港</option>";
    opts += "<option>上海空港</option>";
    $("#destinationPort").append(opts);
}

function initExternalCompany(){
    var group = "";
    if($("#cargoType").val() == undefined || $("#cargoType").val() == null || $("#cargoType").val() == ''|| $("#cargoType").val() == '全部'){
        group = "牛产品";
    }else{
        group = $("#cargoType").val();
    }
    var opts = "";
    $.ajax({
        url:'/trade/common/getOption',
        type:"POST",
        dataType:"json",
        data:{'group':group,'field':'externalCompany'},
        success:function(res){
            for(var i=0;i<res.data.length;i++){
                opts += "<option>"+res.data[i].name+"</option>";
            }
            $("#externalCompany").append(opts);
        }
    });
}

function initBusinessMode(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>自营</option>";
    opts += "<option>定向</option>";
    opts += "<option>代理(增值税专用发票)</option>";
    opts += "<option>代理(代理费发票)</option>";
    opts += "<option>HDL</option>";
    opts += "<option>MJ</option>";
    opts += "<option>WP</option>";
    $("#businessMode").append(opts);
}

function initLevel(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>A</option>";
    opts += "<option>B</option>";
    opts += "<option>S</option>";
    opts += "<option>YP</option>";
    opts += "<option>YG</option>";
    opts += "<option>GF S</option>";
    opts += "<option>GF S ANGUS</option>";
    opts += "<option>S 3R</option>";
    opts += "<option>S IW</option>";
    opts += "<option>S MW</option>";
    opts += "<option>YG MW</option>";
    opts += "<option>YP 5R</option>";
    opts += "<option>YP ANGUS</option>";
    opts += "<option>YP ANGUS MW</option>";
    opts += "<option>YP IW</option>";
    opts += "<option>YP PIECES</option>";
    opts += "<option>GF 冷切</option>";
    opts += "<option>GF 热切</option>";
    opts += "<option>GF S IW</option>";
    opts += "<option>HEEL MUSCLE</option>";
    opts += "<option>HIND</option>";
    opts += "<option>GF S ANGUS 7R</option>";
    opts += "<option>GF S ANGUS IW</option>";
    opts += "<option>GF S/PR</option>";
    opts += "<option>GF WAGYU</option>";
    opts += "<option>GF YP</option>";
    opts += "<option>GF YP 3R</option>";
    opts += "<option>GF YP 7R</option>";
    opts += "<option>GF YP ANGUS</option>";
    opts += "<option>GF YP ANGUS 3R</option>";
    opts += "<option>GF YP ANGUS IW</option>";
    opts += "<option>GF YP ANGUS LONG</option>";
    opts += "<option>GF YP ANGUS PICECS</option>";
    opts += "<option>GF YP IW</option>";
    opts += "<option>GF YP LONG</option>";
    opts += "<option>GF YP PIECES</option>";
    opts += "<option>A MW</option>";
    opts += "<option>A/YP</option>";
    opts += "<option>GF 150D</option>";
    opts += "<option>GF 150D IW</option>";
    opts += "<option>GF 200D</option>";
    opts += "<option>GF 210D</option>";
    opts += "<option>GF 210D IW</option>";
    opts += "<option>GF 270D</option>";
    opts += "<option>GF IW</option>";
    opts += "<option>GF M6+</option>";
    opts += "<option>GF MW</option>";
    opts += "<option>GF PR</option>";
    opts += "<option>GF S 1R</option>";
    opts += "<option>GF S 3R</option>";
    opts += "<option>GF S 5R</option>";
    opts += "<option>GF S 7R</option>";
    opts += "<option>无</option>";
    $("#level").append(opts);
}

var getNoticeNum = function(){
    $.ajax({
        url:'/trade/notice',
        type:"POST",
        dataType:"json",
        data:{},
        success:function(res){
            if(res.status == "1"){
                $("#noticeNum").html(res.total);
                $("#n1").html(res.n1);
                $("#n2").html(res.n2);
                $("#n3").html(res.n3);
                $("#n4").html(res.n4);
                $("#n5").html(res.n5);
            }
        }
    });
}
function getParam(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
function alarm(index){
    if(index == 1){
        window.location.href = "/trade/contract?type=n1";
    }else if(index == 2){
        window.location.href = "/trade/contract?type=n2";
    }else if (index == 3){
        window.location.href = "/trade/contract?type=n3";
    }else if(index == 4){
        window.location.href = "/trade/cargomanage?status=已入库&minBox=0.001&maxBox=5";
    }else if(index == 5){

    }
}

function initOptions(id) {
    var group = "";
    if($("#cargoType").val() == undefined || $("#cargoType").val() == null || $("#cargoType").val() == ''){
        group = "牛产品";
    }else{
        group = $("#cargoType").val();
    }
    var opts = "";
    $.ajax({
        url:'/trade/common/getOption',
        type:"POST",
        dataType:"json",
        data:{'group':group,'field':id},
        success:function(res){
            for(var i=0;i<res.data.length;i++){
                opts += "<option>"+res.data[i].name+"</option>";
            }
            $("#"+id).append(opts);
        }
    });
}