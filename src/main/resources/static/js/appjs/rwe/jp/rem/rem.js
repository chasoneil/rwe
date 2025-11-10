const prefix = "/rwe/jp/rem";

$(function () {
    $('.rem-btn').css({
        transition: 'transform 0.5s ease-in-out' // 延长动画时间为1.5秒
    });

    $('.rem-btn').hover(
        function () {
            // 鼠标进入时放大元素
            $(this).css('transform', 'scale(1.1)');
        },
        function () {
            // 鼠标离开时恢复原始大小
            $(this).css('transform', 'scale(1)');
        }
    );
});

function listWords() {
}

function remWord() {

    let id = $('#lessonId').val();
    console.log(id);

    $.ajax({
        url: prefix + "/dorem/" + id,
        type: "get",
        success: function (response) {
            let element = document.getElementById('rem-content');
            element.innerHTML = response;
            layer.msg('开始背单词');
        },
        error: function (xhr, status, error) {
            console.error('AJAX请求失败：', error);
        }
    });

}

function doTest() {
    alert('开发中...');
}




