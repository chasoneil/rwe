
const prefix = "/system/sysDept";

$(function() {
	load();
});

function load() {
	$('#exampleTable')
		.bootstrapTreeTable(
			{
				method : 'get',
				url : prefix + '/list',
				id : 'deptId',
				code : 'deptId',
				parentCode : 'parentId',
				singleSelect : false,
				dataType: "json",
				ajaxParams : {}, // 请求数据的ajax的data属性
				expandColumn : '1', // 在哪一列上面显示展开按钮
				striped : true,
				bordered : true,  // show border
				expandAll : false,
				toolbar : '#exampleToolbar',
				columns : [
					{
						field : 'deptId',
						title : '编号',
						visible : false
					},
					{
						field : 'name',
						title : '部门名称',
						width :20
					},
					{
						field : 'orderNum',
						title : '排序',
                        align : 'center'
					},
					{
						field : 'delFlag',
						title : '状态',
						align : 'center',
						formatter : function(item, index) {
							if (item.delFlag == '0') {
								return '<span class="label label-danger">禁用</span>';
							} else if (item.delFlag == '1') {
								return '<span class="label label-primary">正常</span>';
							}
						}
					},
					{
						title : '操作',
						field : 'id',
						align : 'center',
						formatter : function(item, index) {
							var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ item.deptId
								+ '\')"><i class="fa fa-edit"></i> 编辑</a>';
							var a = '<a style="margin-left:5px" class="btn btn-primary btn-sm ' + s_add_h + '" href="#" title="增加下級"  mce_href="#" onclick="add(\''
								+ item.deptId
								+ '\')"><i class="fa fa-plus"></i> 增加下级</a>';
							var d = '<a style="margin-left:5px" class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="singleRemove(\''
								+ item.deptId
								+ '\')"><i class="fa fa-remove"></i> 删除</a>';
							return e + a + d;
						}
					} ]
			});
}

function reLoad() {
	load();
}

// add dialog
function add(pId) {
	layer.open({
		type : 2,
		title : '新增部门',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '420px' ],
		content : prefix + '/add/' + pId
	});
}

function edit(id) {
	layer.open({
		type : 2,
		title : '编辑部门信息',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '420px' ],
		content : prefix + '/edit/' + id
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
				'deptId' : id
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
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
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['deptId'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {});
}

