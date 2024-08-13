var prefix = "/rwe/device/register"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
		.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/list", // 服务器数据的加载地址
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
			            keyWord : $('#searchDevIp').val(),
			            devGroupCode:$('#searchDevGroupCode').val()
					};
				},
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true,
						},
						{
							title: '序号',
							width: '20px',
							align: 'center',
							formatter : function(value, row, index) {
								var e = index + 1;
								return e;
							}
						},
						{
							field : 'devCode',
							title : '设备IP地址' ,
							align : 'center'
						},
						{
							field : 'devNumber',
							title : '序列号' ,
							align : 'center'
						},
						{
							field : 'devGroupCode',
							title : '分组号' ,
							align : 'center',
							visible : false
						},
						{
							field : 'devType',
							title : '终端类型' ,
							align : 'center',
							visible : false ,
							formatter : function(value, row, index) {
								var e = '';
								$.ajax({
									async:false,
									type : 'get',
									data : {
										"type" : 'device_type', //传递参数
										"value" : row.devType
									},
									url : '/common/sysDict/detail',
									success : function(r) {
										e += r.name;
									}
								});
								return e;
							}
						},
						{
							field : 'devRegTime',
							title : '注册时间' ,
							align : 'center'
						},
						{
							field : 'devStatus',
							title : '状态' ,
							align : 'center',
							formatter : function(value, row, index)
							{
								if (row.devStatus == '00')
								{
									return '<span class="label label-danger">新发现</span>';
								}
								else if (row.devStatus == '10')
								{
									return '<span class="label label-warning">离线</span>';
								}
								else if (row.devStatus == '11')
								{
									return '<span class="label label-primary">联机中</span>';
								}
							}
						},
						{
							title : '操作',
							field : 'id',
							align : 'center',
							visible : false,
							formatter : function(value, row, index) {
								var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ row.devId + '\')"><i class="fa fa-edit"></i></a> ';
								var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="singleRemove(\''
										+ row.devId
										+ '\')"><i class="fa fa-remove"></i></a> ';
								var f = '<a class="btn btn-success btn-sm" href="#" title="备用"  mce_href="#" onclick="resetPwd(\''
										+ row.devId
										+ '\')"><i class="fa fa-key"></i></a> ';
								return e + d ;
							}
						}]
			});
}

function search() {
	$("#searchDevGroupCode").val("");

	var opt = {
			query : {
				pageNumber : 1
			}
		}
	$('#exampleTable').bootstrapTable('refresh', opt);
}

function reLoad() {
	$("#searchDevGroupCode").val("");
	$("#searchDevIp").val("");

	var opt = {
			query : {
				pageNumber : 1
			}
		}
	$('#exampleTable').bootstrapTable('refresh', opt);
	layer.msg('刷新成功');
}

/*
 * 发现设备
 */
function finder() {
	$.ajax({
		url : "/rwe/device/finder",
		type : "get",
		success : function(r) {
			layer.msg(r.msg);
		}
	});
}

/*
 * 查看新设备
 */
function finderList() {
	layer.open({
		type : 2,
		title : '发现设备',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : '/rwe/device/finder/list' // iframe的url
	});
}

/*
 * 新增设备
 */
function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
	});
}

/*
 * 修改设备
 * @param devId
 */
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

/*
 * 删除设备
 * @param devId
 */
function singleRemove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : "/rwe/device/register/remove",
			type : "post",
			data : {
				'devId' : id
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

function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中33的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['devId'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : '/rwe/device/register/batchRemove',
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

/*
 * 批量导入设备
 */
function deviceImport()
{
	layer.open({
		type : 2,
		title : '导入设备',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/devImport'
	});
}

