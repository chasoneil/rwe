const PREFIX = "/rwe/keep_account/";

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

document.addEventListener("DOMContentLoaded", function() {
	const selectElements = document.querySelectorAll("select");
	selectElements.forEach(select => {
		const selectedValue = select.getAttribute("value");
		if (selectedValue) {
			Array.from(select.options).forEach(option => {
				if (option.text === selectedValue) {
					option.selected = true;
					$(select).trigger("chosen:updated");
				}
			});
		}
	});
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});

function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : PREFIX + "update",
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.reload();
				let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);
			} else {
				parent.layer.alert(data.msg)
			}
		}
	});
}

function validateRule() {
	let icon = "<i class='fa fa-times-circle'></i> ";
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
			},
			payFor : {
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
			},
			payFor : {
				required : icon + "不能为空"
			}
		}
	})
}
