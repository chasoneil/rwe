var prefix = "/sys/role";
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get',
						url : prefix + "/list",
						striped : true,
						dataType : "json",
						pagination : true,
						singleSelect : false,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						pageSize : 10,
						pageNumber : 1,
						search : true,
						showColumns : true,
						sidePagination : "client",
						// "server"
						// queryParams : queryParams,
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [
								{
									// 数据类型，详细参数配置参见文档http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/
									checkbox : true
								},
								{
									field : 'roleId',
									title : '序号'
								},
								{
									field : 'roleName',
									title : '角色名'
								},
								{
									field : 'remark',
									title : '备注'
								},
								{
									field : 'roleSign',
									title : '权限'
								},
								{
									title : '操作',
									field : 'roleId',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.roleId
												+ '\')"><i class="fa fa-edit"></i> 编辑</a>';
										var d = '<a style="margin-left: 5px" class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="singleRemove(\''
												+ row.roleId
												+ '\')"><i class="fa fa-remove"></i> 删除</a>';
										return e + d;
									}
								}]
					});
}

function reload() {
	$('#exampleTable').bootstrapTable('refresh');
}

function add() {
	// iframe层
	layer.open({
		type : 2,
		title : '添加角色',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '580px' ],
		content : prefix + '/add'
	});
}

function singleRemove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code === 0) {
					layer.msg("删除成功");
					reload();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}

function edit(id) {
	layer.open({
		type : 2,
		title : '修改角色',
		maxmin : true,
		shadeClose : true,
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id
	});
}
function batchRemove() {

	var rows = $('#exampleTable').bootstrapTable('getSelections');
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	}, function() {
		var ids = new Array();
		$.each(rows, function(i, row) {
			ids[i] = row['roleId'];
		});
		console.log(ids);
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reload();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {});
}
