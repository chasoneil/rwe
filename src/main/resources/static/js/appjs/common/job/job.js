var prefix = "/common/job";

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
                // showToggle : true,
                // showColumns : true,
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
                        // 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset: params.offset,
                        jobName:$('#searchJobName').val()
                        // username:$('#searchName').val()
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'jobName',
                        title: '任务名称'
                    },
                    {
                        field: 'jobGroup',
                        title: '任务分组',
						align : 'center'
                    },
                    {
                        field: 'beanClass',
                        title: '任务类'
                    },
                    {
                        field: 'cronExpression',
                        title: 'cron表达式'
                    },
                    {
                        visible: false,
                        field: 'methodName',
                        title: '方法名称'
                    },
                    {
                        visible: false,
                        field: 'isConcurrent',
                        title: '任务是否有状态'
                    },
                    {
                        field: 'description',
                        title: '任务描述'
                    },
                    {
                        visible: false,
                        field: 'updateBy',
                        title: '更新者'
                    },

                    {
                        visible: false,
                        field: 'createDate',
                        title: '创建时间'
                    },


                    {
                        visible: false,
                        field: 'updateDate',
                        title: '更新时间'
                    },
                    {
                        visible: false,
                        field: 'createBy',
                        title: '创建者'
                    },
                    {
                        visible: false,
                        field: 'springBean',
                        title: 'Spring bean'
                    },

                    {
                        field: 'jobStatus',
                        title: '停起操作',
						align : 'center',
                        formatter: function (value, row, index) {
                            var e = '<a class="btn btn-danger btn-xs" href="#" mce_href="#" title="点击开启" onclick="changeStatus(\''
                                + row.id + '\',\'' + row.jobStatus
                                + '\')">已关闭</a> ';
                            var f = '<a class="btn btn-success btn-xs" href="#" mce_href="#" title="点击关闭" onclick="changeStatus(\''
                                + row.id + '\',\'' + row.jobStatus
                                + '\')">开启中</a> ';
                            if (row.jobStatus == 0) {
                                return e;
                            } else {
                                return f;
                            }

                        }
                    },

                    {
                        title: '操作',
                        field: 'id',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id + '\',\'' + row.jobStatus
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="singleRemove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return e + d;
                        }
                    }]
            });
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}

function add() {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add' // iframe的url
    });
}

function edit(id, status) {
    if (status == '1') {
        layer.alert('修改之前请先停止任务');
        return;
    }
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id
    });
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
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reload();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function changeStatus(id, status) {
    var actCh;
    var cmd;
    if (status == 0) {
        cmd = 'start';
        actCh = "确认要开启任务吗？";
    } else {
        cmd = 'stop';
        actCh = "确认要停止任务吗？";
    }
    layer.confirm(actCh, {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/changeJobStatus",
            type: "post",
            data: {
                'id': id,
                'cmd': cmd
            },
            success: function (r) {
                if (r.code == 0) {
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
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
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
