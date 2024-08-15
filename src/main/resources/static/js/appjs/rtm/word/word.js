var prefix = "/rwe/word";

$(function() {
	getTreeData();
	loadDevice();
	loadDeviceCount();
});

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
		loadDevice();
	}
	else
	{
		$('#spaceId').val(data.selected[0]);
		loadDevice();
	}
});

/*
 * 获取设备列表
 */
function loadDevice()
{
	var spaceId = $('#spaceId').val();
	if(spaceId == null || spaceId == '' || spaceId == 'undefined')
	{
		spaceId = 'root';
	}
	$.ajax({
		type : 'GET',
		url : prefix + '/device',
		async : false,
		data : {
			'spaceId' : spaceId
		},
		success : function(r)
		{
			$('#device').html(r);
		}
	});
}

/*
 * 获取设备统计信息
 */

function loadDeviceCount()
{
	$.ajax({
		type : 'GET',
		url : prefix + '/deviceCount',
		async : false,
		success : function(r)
		{
			$('#deviceCount').html(r);
		}
	});
}
