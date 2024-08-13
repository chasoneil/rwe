
$().ready(function()
{
	getSpaceTreeData();
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function()
	{
		getAllSelectNodes();
		save();
	}
});

function loadSpaceTree(spaceTree) {
	$('#spaceTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			//"multiple": false, //单选
			'data' : spaceTree
		},
		"checkbox" : {
			//"keep_selected_style" : false,
			//"undetermined" : true
			//"three_state" : false,  //父子级不关联选中
			//"cascade" : ' up'
		}
	});
	$('#spaceTree').jstree('open_all');
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

function save() {
	var spaces = '';
	for(var i in spaceIds)
	{
		spaces += (spaceIds[i] + ',');
	}
	$('#policySpaceKeys').val(spaces);
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rwe/policy/save",
		data : $('#signupForm').serialize(),
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
				parent.layer.alert(data.msg)
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
				required : icon + "请输入姓名"
			}
		}
	})
}
