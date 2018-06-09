$(function () {
    //1.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
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
    };

    return oInit;
};

function saveContract(){
    $.ajax({
        url:"/trade/contract/add",
        type:"POST",
        dataType:"json",
        data:{"username":username},
        success:function(res){
            if(res.status == "1"){
                window.location.href = "/index";
            }else{
                $("#notice").show();
            }
        }
    });
}