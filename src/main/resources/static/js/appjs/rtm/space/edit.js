$().ready(function() {
	getMenuTreeData();
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});

function loadMenuTree(menuTree) {
	$('#menuTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			'data' : menuTree
		},
		"checkbox" : {
			//"keep_selected_style" : false,
			//"undetermined" : true
			//"three_state" : false,
			//"cascade" : ' up'
		}
	});
	$('#menuTree').jstree('open_all');
}

function getAllSelectNodes() {
	var ref = $('#menuTree').jstree(true); // 获得整个树
	menuIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组
	$("#menuTree").find(".jstree-undetermined").each(function(i, element) {
		menuIds.push($(element).closest('.jstree-node').attr("id"));
	});
	console.log(menuIds);
}

function getMenuTreeData() {
	/*
	 * 如果设备的管理员没有设置，初始化管理员为根节点
	 */
	var managerIds = $('#spaceManagerBy').val();
	if(managerIds == "" || managerIds == null)
	{
		managerIds = '-1';
	}
	$.ajax({
		type : "GET",
		url : "/sys/user/spaceManagerTree/" + managerIds,
		success : function(data) {
			loadMenuTree(data);
		}
	});
}

function update() {
	var managers = '';
	for(var i in menuIds)
	{
		managers += (menuIds[i] + ',');
	}
	$('#spaceManagerBy').val(managers);
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rwe/space/update",
		data : $('#signupForm').serialize(),// 你的formid
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
				required : icon + "请输入名字"
			}
		}
	})
}
