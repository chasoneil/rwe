const prefix = "/rwe/jp/rem";

$(function () {

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


function refreshPage() {
    reload();
    layer.msg("刷新成功");
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}
