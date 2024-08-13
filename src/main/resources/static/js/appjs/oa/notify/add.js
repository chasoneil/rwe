$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/oa/notify/save",
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
				required : icon + "请输入姓名"
			}
		}
	})
}

/**
 * 人员选择对话框
 * */
var openUser = function(){
	layer.open({
		type:2,
		title:"选择人员",
		area : [ '300px', '450px' ],
		content:"/sys/user/treeView"
	})
}

/**
 * 选择用户的主键和名称回写
 * */
function loadUser(userIds,userNames){
	$("#userIds").val(userIds);
	$("#userNames").val(userNames);
}

/**
 * 上传文件的文件名，回写至页面
 * */
function loadFileNames(fileNames){
	$("#files").val(fileNames);
}

/**
 * 附件上传对话框
 * */
var openUpload = function(){
	layer.open({
		type:2,
		title:"上传附件",
		area : [ '600px', '400px' ],
		content:"/common/sysFile/attachment"
	})
}