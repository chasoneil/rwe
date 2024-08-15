var prefix = "/rwe/lesson";

$(function() {
	loadLesson();
});

function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id
	});
}

function loadLesson()
{
	$.ajax({
		type : 'GET',
		url : prefix + '/lesson',
		async : false,
		data : {},
		success : function(r) {
			$('#lesson').html(r);
		}
	});
}

