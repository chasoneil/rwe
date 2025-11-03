const prefix = "/rwe/keep_account";

let conditionFlag = 0;

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
                        // 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset: params.offset,
                        tradeDetail: $('#tradeDetail').val(),
                        consumer: $('#consumer').val(),
                        tradeStatistics: $('#tradeStatistics').val(),
                        tradeTime: $('#tradeDate').val()
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
                        visible: false,
                        field: 'id',
                        title: '交易ID'
                    },
                    {
                        field: 'tradeTime',
                        title: '交易日期',
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
                        field: 'amount',
                        title: '金额(元)',
                        align : 'center'
                    },
                    {
                        field: 'tradeVariety',
                        title: '一级分类',
						align : 'center'
                    },
                    {
                        field: 'tradeType',
                        title: '二级分类',
                        align : 'center'
                    },
                    {
                        field: 'consumer',
                        title: '消费人',
                        align : 'center'
                    },
                    {
                        field: 'tradeStatistics',
                        title: '深度支出分类',
                        align : 'center'
                    },
                    {
                        field: 'tradePeriod',
                        title: '账单周期',
                        align : 'center'
                    },
                    {
                        field: 'payAccount',
                        title: '支付账户',
                        align : 'center'
                    },
                    {
                        field: 'payMethod',
                        title: '支付方式',
                        align : 'center'
                    },
                    {
                        field: 'tradeDetail',
                        title: '明细',
                        align : 'center'
                    },
                    {
                        field: 'tradeComment',
                        title: '备注'
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function (value, row, index) {
                            let e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id + '\')"><i class="fa fa-edit"></i> 编辑</a> ';
                            let d = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="singleRemove(\''
                                + row.id + '\')"><i class="fa fa-remove"></i> 删除</a>';
                            return e + d;
                        }
                    }]
            });
}

function addSearchContent(content) {

    if (conditionFlag === 0) {
        $('#conditionBox').append('<div class="ibox ibox-content"> '
            + '<div class="fixed-table-toolbar"> '
            + '<div class="columns pull-left"> '
            + '<div id="searchBox"></div> '
            + '</div> '
            + '</div> '
            + '</div> ');
        conditionFlag++;
    }

    let tmpVal = '';
    if (content === 'consumer') {
        tmpVal = $('#consumer').val();
        $('#consumer_tmp').remove();
        $('#searchBox').append('<button type="button" id="consumer_tmp" style="margin-right: 10px;" '
            + 'class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="clearSearchContent(\'consumer\')">'
            + tmpVal + '<i class="fa fa-remove" aria-hidden="true"></i>'
            + '</button>');
    } else if (content === 'statistics') {
        tmpVal = $('#tradeStatistics').val();
        $('#statistics_tmp').remove();
        $('#searchBox').append('<button type="button" id="statistics_tmp" style="margin-right: 10px;" '
            + 'class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="clearSearchContent(\'statistics\')">'
            + tmpVal + '<i class="fa fa-remove" aria-hidden="true"></i>'
            + '</button>');
    } else if (content === 'tradeDate') {
        tmpVal = $('#tradeDate').val();
        $('#tradeDate_tmp').remove();
        $('#searchBox').append('<button type="button" id="tradeDate_tmp" style="margin-right: 10px;" '
            + 'class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="clearSearchContent(\'tradeDate\')">'
            + tmpVal + '<i class="fa fa-remove" aria-hidden="true"></i>'
            + '</button>');
    } else if (content === 'tradeDetail') {
        tmpVal = $('#tradeDetail').val();
        $('#tradeDetail_tmp').remove();
        $('#searchBox').append('<button type="button" id="tradeDetail_tmp" style="margin-right: 10px;" '
            + 'class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="clearSearchContent(\'tradeDetail\')">'
            + tmpVal + '<i class="fa fa-remove" aria-hidden="true"></i>'
            + '</button>');
    }
    reload();
}

function clearSearchContent(content) {
    conditionFlag--;
    if (conditionFlag === 0) {
        $('#conditionBox').html('');
    }

    if (content === 'consumer') {
        $('#consumer').val('').trigger('chosen:updated');
        $('#consumer_tmp').remove();
    } else if (content === 'statistics') {
        $('#tradeStatistics').val('').trigger('chosen:updated');
        $('#statistics_tmp').remove();
    } else if (content === 'tradeDate') {
        $('#tradeDate').val('');
        $('#tradeDate').trigger('change');
        $('#tradeDate_tmp').remove();
    } else if (content === 'tradeDetail') {
        $('#tradeDetail').val('');
        $('#tradeDetail_tmp').remove();
    }
    reload();
}

function refreshPage() {
    $('#searchBox').html('')
    $('#tradeDetail').val('');
    $('#consumer').val('').trigger('chosen:updated');
    $('#tradeStatistics').val('').trigger('chosen:updated');
    $('#tradeDate').val('');
    $('#conditionBox').html('');
    conditionFlag = 0;
    reload();
    layer.msg("重置搜索条件成功");
}

function reload() {
    $('#exampleTable').bootstrapTable('refresh');
}

function add() {
    layer.open({
        type: 2,
        title: '记账',
        maxmin: true,
        shadeClose: false,
        area: ['900px', '620px'],
        content: prefix + '/add'
    });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '修改记账',
        maxmin: true,
        shadeClose: false,
        area: ['900px', '620px'],
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
    const rows = $('#exampleTable').bootstrapTable('getSelections');
    if (rows.length === 0) {
        layer.msg("请选择要删除的数据");
        return;
    }

    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
    }, function () {
        const ids = [];
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
