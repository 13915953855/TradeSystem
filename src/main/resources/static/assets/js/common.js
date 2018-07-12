$(function(){
    toastr.options = {
        closeButton: true,
        debug: false,
        progressBar: true,
        positionClass: "toast-top-center",
        onclick: null,
        showDuration: "300",
        hideDuration: "1000",
        timeOut: "2000",
        extendedTimeOut: "1000",
        showEasing: "swing",
        hideEasing: "linear",
        showMethod: "fadeIn",
        hideMethod: "fadeOut"
    };

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

function initExternalCompany(){
    var opts = "";
    opts += "<option></option>";
    opts += "<option>JOC AUS</option>";
    opts += "<option>KPC HK</option>";
    opts += "<option>STANBROKE</option>";
    opts += "<option>JBS AUS</option>";
    opts += "<option>HARVEY</option>";
    opts += "<option>TEYS AUS</option>";
    opts += "<option>NH AUS</option>";
    opts += "<option>TIF</option>";
    opts += "<option>SANGER</option>";
    opts += "<option>MARFRIG</option>";
    opts += "<option>AACO</option>";
    $("#externalCompany").append(opts);
}