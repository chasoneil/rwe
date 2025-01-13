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

function remove(lessonId) {

	layer.confirm('删除课程会导致该课程下所有单词被删除？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : "/rwe/lesson/remove",
			type : "post",
			data : {
				'lessonId' : lessonId
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					loadLesson();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}



function addWord(lessonId) {

	layer.open({
		type : 2,
		title : '添加单词',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '420px' ],
		content : '/rwe/word/add' + '/' + lessonId
	});
}

