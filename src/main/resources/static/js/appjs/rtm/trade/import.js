
$().ready(function() {
	$( 'input[type="file"]' ).prettyFile();
});

function importData() {
	let fileInput = document.getElementById('file');
	let file = fileInput.files[0];

	if (!file) {
		layer.alert("请选择文件");
		return;
	}

	const formData = new FormData();
	formData.append("file", file);

	$.ajax({
		cache : false,
		type : "POST",
		url :"/rwe/trade/uploadFile",
		data : formData,
		contentType : false,
		processData : false,
		async : false,
		error : function(request) {
			layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg(data.msg || "上传成功！");
				parent.reload();
				const index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);
			} else {
				parent.layer.alert(data.msg || "上传失败！");
			}
		}
	});
}