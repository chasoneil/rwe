const prefix = "/rwe/consume_category";

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
                        field: 'categoryName',
                        title: '交易类别',
                        align: 'center',
                    },
                    {
                        field: 'categoryType',
                        title: '详细类别',
                        align : 'center'
                    },
                    {
                        field: 'billType',
                        title: '账本类型',
						align : 'center'
                    },
                    {
                        field: 'deepType',
                        title: '深度支出类型',
                        align : 'center'
                    },
                    {
                        visible: false,
                        field: 'id',
                        title: '交易ID'
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            let f = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="添加细类" onclick="addType(\''
                                + row.id + '\')"><i class="fa fa-plus"></i> 添加细类</a> ';
                            let e = '<a class="btn btn-success btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id + '\')"><i class="fa fa-edit"></i> 编辑</a> ';
                            let d = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="singleRemove(\''
                                + row.id + '\', \'' + row.level + '\')"><i class="fa fa-remove"></i> 删除</a>';
                            if (row.categoryType === '-') {
                                return f + e + d;
                            } else {
                                return e + d;
                            }
                        }
                    }]
            });
}

function refreshPage() {
    reload();
    layer.msg("刷新成功");
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}

function add() {
    layer.open({
        type: 2,
        title: '新增消费类型',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '320px'],
        content: prefix + '/add' // iframe的url
    });
}

function addType(id) {
    layer.open({
        type: 2,
        title: '新增消费细类',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/addType/' + id // iframe的url
    });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '修改消费类型',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

function singleRemove(id, level) {

    let confirmMsg = "确认删除消费类型吗？";
    if (level === '1') {  // 一级菜单
        confirmMsg = "删除一级菜单会同时删除其下所有子菜单，确认删除？";
    }

    layer.confirm(confirmMsg, {
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
