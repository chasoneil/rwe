
var prefix = "/rwe/policy"
$(function()
{
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
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								policyName : $('#searchName').val(),
								spaceId : $('#spaceId').val()
							};
						},
						columns : [
								{
									checkbox : true
								},
								{
									field : 'policyName',
									title : '策略名' ,
									align : 'center'
								},
								{
									field : 'policyWeek',
									title : '适用星期数'
								},
								{
									field : 'policyStartTime',
									title : '开始时间',
									align : 'center'
								},
								{
									field : 'policyDur',
									title : '持续时间(分钟)',
									align : 'center'
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.policyId
												+ '\')"><i class="fa fa-edit"></i> 修改</a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="singleRemove(\''
												+ row.policyId
												+ '\')"><i class="fa fa-remove"></i> 删除</a> ';
										var f = '<a class="btn btn-success btn-sm" href="#" title="详情"  mce_href="#" onclick="detail(\''
												+ row.policyId
												+ '\')"><i class="fa fa-binoculars"></i> 详情</a> ';
										return e + f + d;
									}
								}]
					});
}

function reLoad()
{
	$('#exampleTable').bootstrapTable('refresh');
}

function refrashPage()
{
	$('#searchName').val('');
	$('#spaceName').text('全部策略');
	$('#exampleTable').bootstrapTable('refresh');
	layer.msg('刷新成功');
}

/**
 * 策略详情
 * @returns
 */
function detail(policyId)
{
	layer.open({
		type : 2,
		title : '策略详情',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/detail/' + policyId // iframe的url
	});
}

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

/**
 * 添加随机策略
 * 随机策略的运行情况，根据适用的设备数量，随机开启，运行一定时间后关闭并重新随机开启一批
 * @returns
 */
function addRandom(){
	layer.open({
		type : 2,
		title : '增加随机策略',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/addRandom' // iframe的url
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
				'policyId' : id
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
			ids[i] = row['policyId'];
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

//处理页面中班级的选择
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
	if (data.selected == -1)
	{
		$('#spaceId').val('root');
		$('#spaceName').text('全部策略');
		var opt = {
				query : {
					spaceId : 'root',
				}
			}
		$('#exampleTable').bootstrapTable('refresh', opt);
	}
	else
	{
		$('#spaceId').val(data.selected[0]);
		$('#spaceName').text(data.node.text);
		reLoad();
	}
});
