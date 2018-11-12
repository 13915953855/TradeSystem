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

    $("select").select2({
        tags: true
    });

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
    var cargoList = "<optgroup label='去骨'>";
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
    opts += "<option>张家港</option>";
    $("#destinationPort").append(opts);
}

function initExternalCompany(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>FRISA</option>";
    opts += "<option>HARVEY</option>";
    opts += "<option>GORINA</option>";
    opts += "<option>JBS AUS</option>";
    opts += "<option>JOC AUS</option>";
    opts += "<option>KPC HK</option>";
    opts += "<option>MATABOI</option>";
    opts += "<option>MINEVER</option>";
    opts += "<option>MOGILEV MEAT</option>";
    opts += "<option>SANGER</option>";
    opts += "<option>STANBROKE</option>";
    opts += "<option>SWIFT</option>";
    opts += "<option>TEYS AUS</option>";
    opts += "<option>THOMAS</option>";
    opts += "<option>MARFRIG</option>";
    $("#externalCompany").append(opts);
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