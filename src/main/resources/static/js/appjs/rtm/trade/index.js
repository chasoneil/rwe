const prefix = "/rwe/trade";

$(function () {
    let config = {
        '.chosen-select': {},
        '.chosen-select-deselect': {
            allow_single_deselect: true
        },
        '.chosen-select-no-single': {
            disable_search_threshold: 10
        },
        '.chosen-select-no-results': {
            no_results_text: 'Oops, nothing found!'
        },
        '.chosen-select-width': {
            width: "40%"
        }
    }
    for (let selector in config) {
        $(selector).chosen(config[selector]);
    }

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
                        platform: $('#platform').val(),
                        searchText: $('#searchText').val()
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
                        field: 'tradeTime',
                        title: '交易时间',
                        align: 'center',
                        formatter: function (value) {
                            if (value) {
                                const date = new Date(value);
                                const year = date.getFullYear();
                                const month = String(date.getMonth() + 1).padStart(2, '0');
                                const day = String(date.getDate()).padStart(2, '0');
                                return `${year}-${month}-${day}`;
                            }
                            return value;
                        }
                    },
                    {
                        field: 'platform',
                        title: '交易平台',
                        align : 'center'
                    },
                    {
                        field: 'tradeType',
                        title: '交易类型',
						align : 'center'
                    },
                    {
                        field: 'product',
                        title: '商品说明'
                    },
                    {
                        field: 'amount',
                        title: '金额(元)'
                    },
                    {
                        visible: false,
                        field: 'orderId',
                        title: '交易订单号'
                    },
                    {
                        field: 'tradeStatus',
                        title: '交易状态',
						align : 'center',
                        formatter: function (value, row, index) {
                            if (row.tradeStatus === '交易成功' || row.tradeStatus === '支付成功' ) {
                                return '<a class="btn btn-primary btn-xs btn-outline" href="#" mce_href="#">' + row.tradeStatus + '</a>';
                            } else if(row.tradeStatus === '已关闭' || row.tradeStatus === '已转账') {
                                return '<a class="btn btn-default btn-xs btn-outline" href="#" mce_href="#">' + row.tradeStatus + '</a>';
                            } else if(row.tradeStatus === '退款成功' || row.tradeStatus === '已存入零钱' || row.tradeStatus.includes('%已退款%')) {
                                return '<a class="btn btn-primary btn-xs btn-outline" href="#" mce_href="#">' + row.tradeStatus + '</a> ';
                            }
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            let e = '<a class="btn btn-success btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.orderId + '\')"><i class="fa fa-edit"></i> 编辑</a> ';
                            let d = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="singleRemove(\''
                                + row.orderId + '\')"><i class="fa fa-remove"></i> 删除</a>';
                            return d;
                        }
                    }]
            });
}

function refreshPage() {
    $('#platform').val('');
    $('#searchText').val('');
    reload();
    layer.msg("刷新成功");
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}

function add() {
    layer.open({
        type: 2,
        title: '记账',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add' // iframe的url
    });
}

function edit(orderId) {

    layer.open({
        type: 2,
        title: '修改账单',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + orderId // iframe的url
    });
}

function singleRemove(orderId) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'orderId': orderId
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
        title: '导入账单',
        maxmin: true,
        shadeClose: false,
        area: ['520px', '320px'],
        content: prefix + '/import' // iframe的url
    });
}

function exportTrade() {
    window.location.href = prefix + '/export';
}
