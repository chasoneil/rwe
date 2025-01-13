
const prefix = "/rwe/consume_category";

$(function () {
    load();
});

function load() {
    $('#exampleTable')
        .bootstrapTreeTable(
            {
                method: 'get',
                url: prefix + "/list",
                id: 'id',
                code : 'id',
                parentCode : 'parentId',
                singleSelect : false,
                dataType: "json",
                ajaxParams : {},
                expandColumn : '1',
                striped : true,
                bordered : true,
                expandAll : false,
                toolbar : '#exampleToolbar',
                columns: [
                    {
                        field: 'id',
                        title: '交易ID',
                        width: 20,
                    },
                    {
                        field: 'categoryName',
                        title: '一级菜单',
                        align: 'left',
                    },
                    {
                        field: 'categoryType',
                        title: '二级菜单',
                        align : 'center'
                    },
                    {
                        field: 'inOut',
                        title: '收支类型',
                        align: 'center',
                        formatter: function (item, index) {
                            if (item.inOut === '收入') {
                                return '<span class="btn btn-danger btn-xs" style="cursor: default;">' + item.inOut + '</span>';
                            } else if(item.inOut === '支出') {
                                return '<span class="btn btn-default btn-xs" style="cursor: default;">' + item.inOut + '</span>';
                            }
                        }
                    },
                    {
                        field: 'budget',
                        title: '预算(元)',
                        align : 'center'
                    },
                    {
                        field: 'billPeriod',
                        title: '账单周期',
						align : 'center'
                    },
                    {
                        field: 'deepType',
                        title: '深度支出类型',
                        align : 'center'
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (item, index) {
                            let f = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="添加子类" onclick="addType(\''
                                + item.id + '\')"><i class="fa fa-plus"></i> 添加子类</a> ';
                            let e = '<a class="btn btn-success btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + item.id + '\')"><i class="fa fa-edit"></i> 编辑</a> ';
                            let d = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="singleRemove(\''
                                + item.id + '\', \'' + item.level + '\')"><i class="fa fa-remove"></i> 删除</a>';
                            if (item.level === 1) {
                                return f + e + d;
                            } else {
                                return e + d;
                            }
                        }
                    }]
            });
}

function reload() {
    load();
    layer.msg("操作成功");
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
