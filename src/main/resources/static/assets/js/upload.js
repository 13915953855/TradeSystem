$(function(){


// 优化retina, 在retina下这个值是2
var ratio = window.devicePixelRatio || 1;
// 缩略图大小
var thumbnailWidth = 100 * ratio;
var thumbnailHeight = 100 * ratio;
// Web Uploader实例
var uploader = WebUploader.create({
    // 选完文件后，是否自动上传。
    auto: true,
    // swf文件路径
    swf: '/assets/webuploader/Uploader.swf',

    // 文件接收服务端。
    server: '/trade/upload',

    formData: { "contractId": $('#contractId').val(), "fileRef": $('#fileRefHidden').val()},

    fileSingleSizeLimit: 10*1024*1024,//限制大小10M，单文件
    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',

    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false
});

uploader.on( 'uploadBeforeSend', function( block, data ) {
    // 修改data可以控制发送哪些携带数据。
    data.contractId =  $('#contractId').val();
    data.fileRef =  $('#fileRef').val();
});

// 当有文件被添加进队列的时候
uploader.on( 'fileQueued', function( file ) {
    $("#thelist").append( '<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
    '</div>' );
    // 制作缩略图
    // error：不是图片，则有error
    // src:代表生成缩略图的地址
    uploader.makeThumb(file, function(error, src) {
        if (error) {
            $("#" + file.id).find("img").replaceWith("<span>无法预览&nbsp;</span>");
        } else {
            $("#" + file.id).find("img").attr("src", src);
        }
    });
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
    //$( '#'+file.id ).find('p.state').text('已上传');
    $( '#'+file.id ).text('');
    getAllFile();
});

uploader.on( 'uploadError', function( file ) {
    $( '#'+file.id ).find('p.state').text('上传出错');
});

uploader.on( 'uploadComplete', function( file ) {
    $( '#'+file.id ).find('.progress').fadeOut();
});


});