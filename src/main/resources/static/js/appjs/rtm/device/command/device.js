var prefix = "/rwe/device/command"
$(function() {
	getTreeData();
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
				singleSelect : false, // 设置为true将禁止多选
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit : params.limit,
						offset :params.offset,
			            devCode : $('#searchDevNumber').val(),
			            spaceId : $('#searchSpaceId').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
						{
							checkbox : true
						},
						{
							field : 'deviceInfo.devGroupCode',
							title : '所属空间',
							align : 'center',
							formatter : function(value, row, index) {
								if (value == null)
								{
									return "-"
								}
								var e = '';
								$.ajax({
									async:false,
									type : 'get',
									data : {
										'devId' : row.deviceInfo.devId
									},
									url : '/rwe/device/group/detail',
									success : function(r)
									{
										e += r.devGroupCode;
									}
								});
								return e;
							}
						},
						{
							field : 'deviceInfo.devCode',
							title : '设备IP' ,
							align : 'center'
						},
						{
							field : 'deviceInfo.devNumber',
							title : '设备号',
							align : 'center'
						},
						{
							visible : false,
							field : 'deviceInfo.devType',
							title : '终端类型' ,
							align : 'center',
							formatter : function(value, row, index) {
								var e = '';
								$.ajax({
									async:false,
									type : 'get',
									data : {
										"type" : 'device_type', //传递参数
										"value" : row.deviceInfo.devType
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
							field : 'deviceInfo.devSwitchMode',
							title : '定时模式' ,
							align : 'center',
							formatter : function(value, row, index) {
								if(row.deviceInfo.devSwitchMode == '自动')
								{
									return '<div class="btn btn-xs btn-rounded btn-outline btn-primary">开启</div>'
								}
								else
								{
									return '<div class="btn btn-xs btn-rounded btn-outline btn-danger">关闭</div>'
								}
							}
						},
						{
							visible : false,
							field : 'switchInfo.switch',
							title : '继电器' ,
							align : 'center'
						},
						{
							visible : false,
							field : 'switchInfo.current',
							title : '负载情况' ,
							align : 'center'
						},
						{
							field : 'deviceInfo.devOnlineLastTime',
							title : '最近在线时间' ,
							align : 'center'
						},
						{
							field : 'devicePrompt',
							title : '运行情况' ,
							align : 'center'
						},
						{
							title : '操作',
							field : 'id',
							align : 'center',
							formatter : function(value, row, index) {
								var mode = '';
								var so = '';
								var sc = '';
								var boot = '';
								if(row.devicePrompt != '-')
								{
									if(row.deviceInfo.devSwitchMode == '自动')
									{
										mode = '<a class="btn btn-warning btn-sm" title="手自模式切换" onclick="switchModeChange(\''
											+ row.deviceInfo.devId
											+ '\')"> 定时关闭</a> ';
									}
									else
									{
										mode = '<a class="btn btn-primary btn-sm" title="手自模式切换" onclick="switchModeChange(\''
											+ row.deviceInfo.devId
											+ '\')"> 定时开启</a> ';
									}

									if(row.devicePrompt == '未上电')
									{
										so = '<a class="btn btn-success btn-sm" href="#" title="通电"  mce_href="#" onclick="switchOpen(\''
											+ row.deviceInfo.devId
											+ '\')"><i class="fa fa-toggle-on"></i> 通电</a> ';
									}
									else
									{
										sc = '<a class="btn btn-danger btn-sm" href="#" title="断电"  mce_href="#" onclick="switchClose(\''
											+ row.deviceInfo.devId
											+ '\')"><i class="fa fa-toggle-off"></i> 断电</a> ';
									}

									boot = '<a class="btn btn-primary btn-sm" href="#" title="重启"  mce_href="#" onclick="reboot(\''
										+ row.deviceInfo.devId
										+ '\')"><i class="fa fa-spinner"></i> 重启</a> ';
								}
								return  so + sc + boot + mode ;
							}
						}]
			});
}

function search() {
	var opt = {
			query : {
				pageNumber : 1
			}
		}
	$('#exampleTable').bootstrapTable('refresh', opt);
}

function refrashPage()
{
	$("#searchSpaceId").val("");
	$("#searchDevNumber").val("");
	$('#spaceName').text('全部设备');
	var opt = {
			query : {
				pageNumber : 1
			}
		}
	$('#exampleTable').bootstrapTable('refresh', opt);
	layer.msg('刷新成功');
}

function reLoad() {
	$("#searchSpaceId").val("");
	$("#searchDevNumber").val("");
	var opt = {
			query : {
				pageNumber : 1
			}
		}
	$('#exampleTable').bootstrapTable('refresh', opt);
}

function switchOpen(id)
{
	layer.confirm('确定要通电吗？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/switchOpen",
			type : "post",
			data : {
				'devIds' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg("已发送设备通电指令，请稍等3-5秒后刷新页面!");
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function switchClose(id)
{
	layer.confirm('确定要断电吗？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/switchClose",
			type : "post",
			data : {
				'devIds' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg("已发送设备断电指令，请稍等3-5秒后刷新页面!");
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function switchModeChange(id)
{
	layer.confirm('确定修改定时模式吗？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/switchModeChange",
			type : "post",
			data : {
				'devIds' : id
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

function reboot(id)
{
	layer.confirm('确定要重启吗？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/reboot",
			type : "post",
			data : {
				'devIds' : id
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

/**
 * 批量重启
 * @returns
 */
function batchReboot()
{
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要重启的设备");
		return;
	}
	layer.confirm("确认选中的'" + rows.length + "'台设备重启吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = "";
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids = ids + row['devId'] + ",";
		});
		$.ajax({
			type : 'POST',
			data : {
				"devIds" : ids
			},
			url : prefix + '/reboot',
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

/**
 * 批量通电
 * */
function batchSwitchOpen() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要通电的设备");
		return;
	}
	layer.confirm("确认选中的'" + rows.length + "'台设备通电吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = "";
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids = ids + row['devId'] + ",";
		});
		$.ajax({
			type : 'POST',
			data : {
				"devIds" : ids
			},
			url : prefix + '/switchOpen',
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

/**
 * 批量断电
 * */
function batchSwitchClose() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要断电的设备");
		return;
	}
	layer.confirm("确认选中的'" + rows.length + "'台设备断电吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids = ids + row['devId'] + ",";
		});
		$.ajax({
			type : 'POST',
			data : {
				"devIds" : ids
			},
			url : prefix + '/switchClose',
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

/**
 * 批量切换通电方式
 * */
function batchSwitchModeChange() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要切换的设备");
		return;
	}
	layer.confirm("确认选中的'" + rows.length + "'台设备切换吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids = ids + row['devId'] + ",";
		});
		$.ajax({
			type : 'POST',
			data : {
				"devIds" : ids
			},
			url : prefix + '/switchModeChange',
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

//处理页面中班级的选择
function getTreeData() {
	$.ajax({
		type : "GET",
		url : "/rwe/space/treeByManager",
		success : function(tree) {
			loadTree(tree);
		}
	});
}

function loadTree(tree) {
	$('#jstree').jstree({
		'core' : {
			'data' : tree
		},
		"plugins" : [ "search" ]
	});
	$('#jstree').jstree().open_all();
}

$('#jstree').on("changed.jstree", function(e, data) {
	$("#searchDevNumber").val("");
	if (data.selected == -1) {
		$("#searchSpaceId").val("");
		$('#spaceName').text('全部设备');
		var opt = {
			query : {
				spaceId : '',
			}
		}
		$('#exampleTable').bootstrapTable('refresh', opt);
	} else {
		$("#searchSpaceId").val(data.selected[0]);
		$('#spaceName').text(data.node.text);
		var opt = {
			query : {
				spaceId : data.selected[0],
			}
		}
		$('#exampleTable').bootstrapTable('refresh',opt);
	}
});
