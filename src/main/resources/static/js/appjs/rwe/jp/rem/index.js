const prefix = "/rwe/jp/rem";

$(function () {
    $('.contact-box').css({
        transition: 'transform 0.5s ease-in-out' // 延长动画时间为1.5秒
    });

    $('.contact-box').hover(
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

function add() {
    layer.open({
        type: 2,
        title: '新增课程',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/add'
    });
}

function doRem(id) {
    layer.confirm('开始背单词？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/rem/" + id,
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
    })
}


function refreshPage() {
    reload();
    layer.msg("刷新成功");
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}
