var prefix = "/rwe/device/group"
$(function() {
	getTreeData();
	load();
});

function load() {
	$('#exampleTable').bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/list", // 服务器数据的加载地址
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
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
			            keyWord : $('#searchDevNumber').val(),
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
							title : '设备IP',
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
							field : '',
							title : '定时模式' ,
							align : 'center',
							formatter : function(value, row, index) {
								if(row.devSwitchMode == '自动')
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
									return '<span class="btn btn-xs btn-danger btn-rounded btn-outline">新发现</span>';
								}
								else if (row.devStatus == '10')
								{
									return '<span class="btn btn-xs btn-warning btn-rounded btn-outline">离线</span>';
								}
								else if (row.devStatus == '11')
								{
									return '<span class="btn btn-xs btn-primary btn-rounded btn-outline">在线</span>';
								}
							}
						},
						{
							title : '操作',
							field : 'id',
							align : 'center',
							formatter : function(value, row, index) {
								var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="分组" onclick="edit(\''
										+ row.devId
										+ '\')"><i class="fa fa-users"></i> 分组</a> ';
								return  e;
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

function groupFileImp()
{
	layer.open({
		type : 2,
		title : '导入设备分组文件',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/groupFileImp'
	});
}

//
function getTreeData() {
	$.ajax({
		type : "GET",
		url : "/rwe/space/tree",
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
