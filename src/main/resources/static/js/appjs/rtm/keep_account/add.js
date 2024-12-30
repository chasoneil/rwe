
let prefix = '/rwe/keep_account';

$().ready(function() {
	validateRule();

	let config = {
		'.chosen-select': {},
		'.chosen-select-deselect': {
			allow_single_deselect: true
		},
		'.chosen-select-no-single': {
			disable_search_threshold: 10
		},
		'.chosen-select-no-results': {
			no_results_text: 'Oops, nothing found!'
		},
		'.chosen-select-width': {
			width: "40%"
		}
	}
	for (let selector in config) {
		$(selector).chosen(config[selector]);
	}
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
		url : prefix + '/save',
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			console.log(data);
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.load();
				const index = parent.layer.getFrameIndex(window.name);
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
			tradeTime : {
				required : true
			},
			amount : {
				required : true
			},
			tradeType : {
				required : true
			}
		},
		messages : {
			tradeTime : {
				required : icon + "不能为空"
			},
			amount : {
				required : icon + "不能为空"
			},
			tradeType : {
				required : icon + "不能为空"
			}
		}
	})
}




