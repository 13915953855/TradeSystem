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