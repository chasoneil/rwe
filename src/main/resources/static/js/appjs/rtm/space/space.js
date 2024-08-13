
var prefix = "/rwe/space"
$(function() {
	load();
});


var load = function() {
	$('#exampleTable')
			.bootstrapTreeTable(
					{
						id : 'spaceId',
						code : 'spaceId',
		                parentCode : 'spaceParentId',
						type : "GET", // 请求数据的ajax类型
						url : prefix + '/list', // 请求数据的ajax的url
						ajaxParams : {
							//"keyWord" : $("#searchName").val()
						}, // 请求数据的ajax的data属性
						expandColumn : '1',// 在哪一列上面显示展开按钮
						striped : true, // 是否各行渐变色
						bordered : true, // 是否显示边框
						expandAll : true, // 是否全部展开
						// toolbar : '#exampleToolbar',
						columns : [
								{
									field : 'spaceAddress',
									title : '分组名'
								},
								{
									field : 'spaceManagerBy',
									title : '授权用户'
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(item, index) {
										var e = '<a class="btn btn-primary btn-sm '
												+ s_edit_h
												+ '" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ item.spaceId
												+ '\')"><i class="fa fa-edit"></i> 编辑</a> ';
										var a = '<a class="btn btn-primary btn-sm ' + s_add_h + '" href="#" title="增加下級"  mce_href="#" onclick="add(\''
												+ item.spaceId
												+ '\')"><i class="fa fa-plus"></i> 添加下级</a> ';
										var d = '<a class="btn btn-warning btn-sm '
												+ s_remove_h
												+ '" href="#" title="删除"  mce_href="#" onclick="singleRemove(\''
												+ item.spaceId
												+ '\')"><i class="fa fa-remove"></i> 删除分组</a> ';
										return e + a + d;
									}
								} ]
					});
}

function reLoad() {
	load();
}

function refrashPage()
{
	load();
	layer.msg('刷新成功');
}

function add(id) {

	if(typeof(id) == 'undefined')
	{
		id = 'root';
	}

	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add/' + id // iframe的url
	});
}

function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}
function singleRemove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'spaceId' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function resetPwd(id) {
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
			ids[i] = row['spaceId'];
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
	}, function() {

	});
}
