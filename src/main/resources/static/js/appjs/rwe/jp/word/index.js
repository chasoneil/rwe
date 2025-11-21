const prefix = "/rwe/jp/word";

$(function () {
    $(".chosen-select").chosen();
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
                        word:$('#searchWord').val(),
                        lessonId:$('#lesson').val()
                    };
                },
                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'word',
                        title: '单词假名',
                        align: 'center'
                    },
                    {
                        field: 'wordCn',
                        title: '单词',
						align : 'center'
                    },
                    {
                        field: 'wordType',
                        title: '词性',
                        align : 'center'
                    },
                    {
                        field: 'wordVoice',
                        title: '发音',
                        align : 'center'
                    },
                    {
                        field: 'zhMean',
                        title: '中文含义',
                        align : 'center'
                    },
                    {
                        field: 'learned',
                        title: '掌握情况',
                        align : 'center',
                        formatter: function (value, row, index) {
                            if (row.learned === 0) {
                                return '<span class="btn btn-danger btn-xs btn-outline" style="cursor: default;">未学习</span>';
                            } else if(row.tradeStatus === 1) {
                                return '<span class="btn btn-default btn-xs btn-outline" style="cursor: default;">学习中</span>';
                            } else if(row.tradeStatus === 2) {
                                return '<span class="btn btn-primary btn-xs btn-outline" style="cursor: default;">已掌握</span> ';
                            }
                        }
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
        title: '修改单词信息',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id
    });
}

function refreshPage() {
    $('#lesson').val('-1');
    resetSelect();
    $('#searchWord').val('');
    reload();
    layer.msg("刷新成功");
}

function resetSelect() {
    const selectElements = document.querySelectorAll("select");
    selectElements.forEach(select => {
        const selectedValue = '-1';
        if (selectedValue) {
            Array.from(select.options).forEach(option => {
                if (option.value === selectedValue) {
                    option.selected = true;
                    $(select).trigger("chosen:updated");
                }
            });
        }
    });
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
