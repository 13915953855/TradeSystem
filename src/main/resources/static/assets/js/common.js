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
            if(sessionstatus == "limit"){
                swal("没有权限","","error");
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
function initCaiyangcangku(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>大宛</option>";
    opts += "<option>上港冷链</option>";
    opts += "<option>洋山普菲斯</option>";
    opts += "<option>外高桥普菲斯</option>";
    opts += "<option>名联纪丰</option>";
    opts += "<option>名联青浦</option>";
    opts += "<option>同盛保税库</option>";
    $("#caiyangcangku").append(opts);
}
function initWarehouse(){
    var opts = "";
    opts += "<option>库外</option>";
    opts += "<option>澳米特</option>";
    opts += "<option>名联纪丰</option>";
    opts += "<option>名联青浦</option>";
    opts += "<option>九曳</option>";
    opts += "<option>领升</option>";
    opts += "<option>鑫汇洋</option>";
    opts += "<option>瑞源</option>";
    opts += "<option>洋山普菲斯</option>";
    opts += "<option>外高桥普菲斯</option>";
    opts += "<option>上港冷链</option>";
    opts += "<option>华辰</option>";
    opts += "<option>镇江汇鸿</option>";
    opts += "<option>同盛保税库</option>";
    opts += "<option>虎易保税库</option>";
    opts += "<option>前海湾保税库</option>";
    opts += "<option>宝山宏制冷库</option>";
    opts += "<option>新宜虹桥北冷链</option>";
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
    opts += "<option>宁波银行</option>";
    opts += "<option>农业发展银行</option>";
    opts += "<option>浦发银行</option>";
    opts += "<option>紫金农商银行</option>";
    opts += "<option>招商银行</option>";
    $("#prePayBank").append(opts);
    $("#finalPayBank").append(opts);
    $("#issuingBank").append(opts);
    //$("#financingBank").append(opts);
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
    opts += "<option>源鑫</option>";
    opts += "<option>誉尚</option>";
    opts += "<option>宸信</option>";
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
    opts += "<option>青岛</option>";
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
    $("#businessMode").append(opts);
}

function initLevel(){
    var opts = "<option></option>";
    $.ajax({
        url:'/trade/common/getLevel',
        type:"POST",
        dataType:"json",
        data:{},
        success:function(res){
            for(var i=0;i<res.data.length;i++){
                opts += "<option>"+res.data[i]+"</option>";
            }
            $("#level").append(opts);
        }
    });
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
        var curDate = new Date();
        var preDate = new Date(curDate.getTime() - 24*60*60*180*1000);
        var year = preDate.getFullYear();
        var month = preDate.getMonth() + 1;
        var date = preDate.getDate();
        var storeEndDate = year + '-' + p(month) + "-" + p(date);
        var url = "/trade/query/storeInfo?storeStartDate=2018-01-01&storeEndDate="+storeEndDate+"&status="+escape("已入库");
        window.location.href = url;
    }else if (index == 3){
        window.location.href = "/trade/contract?type=n3";
    }else if(index == 4){
        window.location.href = "/trade/cargomanage?status="+escape("已入库")+"&minBox=0.001&maxBox=5";
    }else if(index == 5){

    }
}
function p(s) {
    return s < 10 ? '0' + s : s;
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

//获取最近7天日期

//getDay(0);//当天日期

//getDay(-7);//7天前日期



//获取最近3天日期

//getDay(0);//当天日期

//getDay(-3);//3天前日期



function getDay(day){

　　var today = new Date();



　　var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;



　　today.setTime(targetday_milliseconds); //注意，这行是关键代码



　　var tYear = today.getFullYear();

　　var tMonth = today.getMonth();

　　var tDate = today.getDate();

　　tMonth = doHandleMonth(tMonth + 1);

　　tDate = doHandleMonth(tDate);

　　return tYear+"-"+tMonth+"-"+tDate;

}

function doHandleMonth(month){

　　var m = month;

　　if(month.toString().length == 1){

　　　　m = "0" + month;

　　}

　　return m;

}

function auth(name){
    /*$.ajax({
        url:'/trade/common/getAuth',
        type:"POST",
        dataType:"json",
        data:{'name':name},
        success:function(res){
            return res.data;
        }
    });*/
    if($("#user").val() == "zhangjian" || $("#user").val() == "dingjintian"){
        return "0";
    }else{
        return "1";
    }
}