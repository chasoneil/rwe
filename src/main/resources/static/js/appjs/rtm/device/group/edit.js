$().ready(function()
{
	getSpaceTreeData();
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});

function loadSpaceTree(spaceTree) {
	$('#spaceTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			"multiple": false, //单选
			'data' : spaceTree
		},
		"checkbox" : {
			//"keep_selected_style" : false,
			//"undetermined" : true
			"three_state" : false,  //父子级不关联选中
			//"cascade" : ' up'
		}
	});
	$('#spaceTree').jstree('open_all');
}

function getAllSelectNodes()
{
	var ref = $('#spaceTree').jstree(true); // 获得整个树
	spaceIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组
	$("#spaceTree").find(".jstree-undetermined").each(function(i, element)
	{
		spaceIds.push($(element).closest('.jstree-node').attr("id"));
	});
	//console.log('spaceIds:' + spaceIds);
}

function getSpaceTreeData()
{
	$.ajax({
		type : "GET",
		url : "/rwe/space/tree/",
		success : function(data)
		{
			loadSpaceTree(data);
		}
	});
}

function update() {
	var spaces = spaceIds[0];

	if(spaces == -1)
	{
		layer.msg('不能选择全部');
		return;
	}

	if(spaces == '' || spaces == null)
	{
		layer.msg('必须选择一个对应的空间');
		return;
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : "/rwe/device/group/groupDo",
		data : {
			'devIds' : $('#devIds').val(),
			'spaceId' : spaces,
			'now' : new Date()
		},
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}
