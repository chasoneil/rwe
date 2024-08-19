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

function add() {
	layer.open({
		type : 2,
		title : '增加课程',
		maxmin : true,
		shadeClose : false,
		area : [ '500px', '270px' ],
		content : prefix + '/add'
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

