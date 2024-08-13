var prefix = "/rwe/device/policy"
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
			//	showRefresh : true,
			//	showToggle : true,
			//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit: params.limit,
						offset:params.offset,
			            devCode:$('#searchDevNumber').val(),
			            devGroupCode:$('#searchDevGroupCode').val()
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
							field : 'devGroupCode',
							title : '所属空间' ,
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
										'devId' : row.devId
									},
									url : prefix + '/detail',
									success : function(r)
									{
										e += r.devGroupCode;
									}
								});
								return e;
							}
						},
						{
							field : 'devCode',
							title : '设备IP' ,
							align : 'center'
						},
						{
							field : 'devNumber',
							title : '设备号' ,
							align : 'center'
						},
						{
							visible : false,
							field : 'devType',
							title : '终端类型' ,
							align : 'center',
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
							field : 'devSwitchMode',
							title : '上电方式' ,
							align : 'center'
						},
						{
							field : '',
							title : '管理员' ,
							align : 'center'
						},
						{
							field : 'devOnlineLastTime',
							title : '最近在线时间' ,
							align : 'center'
						},
						{
							field : 'devStatus',
							title : '状态' ,
							align : 'center',
							formatter : function(value, row, index) {
								if (row.devStatus == '00')
								{
									return '<span class="label label-danger">新发现</span>';
								}
								else if (row.devStatus == '10')
								{
									return '<span class="label label-warning">已离线</span>';
								}
								else if (row.devStatus == '11')
								{
									return '<span class="label label-primary">联机中</span>';
								}
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

function reLoad() {
	//$('#exampleTable').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:10, devNumber:$('#searchDevNumber').val()});
	$("#searchDevGroupCode").val("");
	$("#searchDevNumber").val("");

	var opt = {
			query : {
				pageNumber : 1
			}
		}
	$('#exampleTable').bootstrapTable('refresh', opt);
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

/*
 * 批量分组
 */
function groupEdit() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要分组的设备");
		return;
	}
	var ids = new Array();
	// 遍历所有选择的行数据，取每条数据对应的ID
	$.each(rows, function(i, row) {
		ids[i] = row['devId'];
	});
	layer.open({
		type : 2,
		title : '批量分组',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/groupEdit/' + ids
	});
}

/**
 * 定时设置
 * @returns
 */
function timeSet()
{
	layer.open({
		type : 2,
		title : '设备分组定时设置',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/timeSet'
	});
}

//
function getTreeData() {
	$.ajax({
		type : "GET",
		url : "/rwe/space/tree",
//		data : {
//			"manager" : 'device_group'
//		},
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
		$("#searchDevGroupCode").val("");
		var opt = {
			query : {
				devGroupCode : '',
			}
		}
		$('#exampleTable').bootstrapTable('refresh', opt);
	} else {
		$("#searchDevGroupCode").val(data.selected[0]);
		var opt = {
			query : {
				devGroupCode : data.selected[0],
			}
		}
		$('#exampleTable').bootstrapTable('refresh',opt);
	}
});
