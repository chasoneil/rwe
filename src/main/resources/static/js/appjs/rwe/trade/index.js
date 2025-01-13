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
                        field: 'categoryName',
                        title: '交易类型(I)',
						align : 'center'
                    },
                    {
                        field: 'categoryType',
                        title: '交易类型(II)',
                        align : 'center'
                    },
                    {
                        field: 'product',
                        title: '商品说明'
                    },
                    {
                        field: 'tradeObj',
                        title: '交易对方'
                    },
                    {
                        field: 'amount',
                        title: '金额(元)',
                        align: 'center'
                    },
                    {
                        visible: false,
                        field: 'orderId',
                        title: '交易订单号'
                    },
                    {
                        field: 'tradeComment',
                        title: '交易备注'
                    },
                    // {
                    //     field: 'tradeStatus',
                    //     title: '交易状态',
					// 	align : 'center',
                    //     formatter: function (value, row, index) {
                    //         if (row.tradeStatus === '交易成功' || row.tradeStatus === '支付成功' ) {
                    //             return '<span class="btn btn-primary btn-xs" style="cursor: default;">' + row.tradeStatus + '</span>';
                    //         } else if(row.tradeStatus === '已关闭' || row.tradeStatus === '已转账') {
                    //             return '<span class="btn btn-default btn-xs" style="cursor: default;">' + row.tradeStatus + '</span>';
                    //         } else if(row.tradeStatus === '退款成功' || row.tradeStatus === '已存入零钱' || row.tradeStatus.includes('%已退款%')) {
                    //             return '<span class="btn btn-primary btn-xs btn-outline" style="cursor: default;">' + row.tradeStatus + '</span> ';
                    //         }
                    //     }
                    // },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            let e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="拆分" onclick="seperateTrade(\''
                                + row.orderId + '\')"><i class="fa fa-copy"></i> 拆分</a> ';
                            let f = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="拆分" onclick="edit(\''
                                + row.orderId + '\')"><i class="fa fa-edit"></i> 编辑</a> ';
                            let d = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="singleRemove(\''
                                + row.orderId + '\')"><i class="fa fa-remove"></i> 删除</a>';
                            return f + e + d;
                        }
                    }]
            });
}

function seperateTrade(orderId) {
    layer.open({
        type: 2,
        title: '拆分账单',
        maxmin: true,
        shadeClose: false,
        area: ['1000px', '520px'],
        content: prefix + '/split/' + orderId
    });
}

function edit(orderId) {
    layer.open({
        type: 2,
        title: '编辑账单',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + orderId
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
