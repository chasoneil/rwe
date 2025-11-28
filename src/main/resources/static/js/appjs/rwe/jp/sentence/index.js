const prefix = "/rwe/jp/sentence";

$(function () {
    $('.contact-box').css({
        transition: 'transform 0.5s ease-in-out'
    });

    $('.contact-box').hover(
        function () {
            $(this).css('transform', 'scale(1.1)');
        },
        function () {
            $(this).css('transform', 'scale(1)');
        }
    );
});

function practice(lessonName) {
    layer.open({
        type: 2,
        title: '日语句子练习',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/practice/' + lessonName,
    });
}


function refreshPage() {
    layer.msg("刷新成功");
}