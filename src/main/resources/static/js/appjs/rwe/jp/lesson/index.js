const prefix = "/rwe/jp/lesson";

$(function () {
    load();
});

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get',
                url: prefix + "/list",
                showRefresh : false,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true,
                dataType: "json",
                pagination: true,
                singleSelect: false,
                pageSize: 10,
                pageNumber: 1,
                showColumns: false,
                sidePagination: "server",
                queryParams: function (params) {
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        // username:$('#searchName').val()
                    };
                },
                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'lesson',
                        title: '课程名',
                        align: 'center'
                    },
                    {
                        field: 'count',
                        title: '本课单词数',
						align : 'center'
                    },
                    {
                        field: 'passed',
                        title: '已学习',
                        align : 'center'
                    },

                    {
                        field: 'lastLearnTime',
                        title: '上次学习时间',
                        align : 'center'
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            let f = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id + '\')"><i class="fa fa-edit"></i> 编辑</a> ';
                            let d = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="singleRemove(\''
                                + row.id + '\')"><i class="fa fa-remove"></i> 删除</a>';
                            return f + d;
                        }
                    }]
            });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '修改课程信息',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id
    });
}

function refreshPage() {
    reload();
    layer.msg("刷新成功");
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}

function singleRemove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code === 0) {
                    layer.msg(r.msg);
                    reload();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function batchRemove() {
    const rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length === 0) {
        layer.msg("请选择要删除的数据");
        return;
    }

    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
    }, function () {
        const ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['orderId'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code === 0) {
                    layer.msg(r.msg);
                    reload();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}

function importTrade() {
    layer.open({
        type: 2,
        title: '导入单词',
        maxmin: true,
        shadeClose: false,
        area: ['520px', '320px'],
        content: prefix + '/import'
    });
}
