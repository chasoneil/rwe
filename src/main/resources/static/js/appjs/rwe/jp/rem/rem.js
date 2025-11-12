const prefix = "/rwe/jp/rem";

let wordsArray = [];
let index = 0;
let length = 0;

$(function () {
    loadData();
});

/*
加载本课所有的单词
*/
function loadData() {

    let id = $('#lessonId').val();

    $.ajax({
        cache: false,
        type: "POST",
        url : prefix + "/rem/load/words",
        async: false,
        data : {
            'lessonId': id
        },
        error : function(request) {
            parent.layer.alert("加载单词数据失败");
        },
        success : function(data) {
            if (data.code == 0) {
                // js 处理单词数据
                wordsArray = JSON.parse(data.msg);
                length = wordsArray.length;
                exercise();
            } else {
                parent.layer.msg("加载单词数据失败");
            }
        }

    });
}

function exercise() {
    /*
     产生随机数表示练习的类型
     1. 根据日文写出中文含义
     2. 根据日文汉字写出假名
     3. 根据假名写出日语的中文
    */
    let testType = Math.floor(Math.random() * 3) + 1;
    let jpword = wordsArray[index];
    setText(jpword, testType);
}

function setText(jpword, testType) {
    $('#testType').val(testType);
    $('#word_voice').text(jpword.wordVoice);
    $('#word_type').text(jpword.wordType);
    // 中文含义测试
    if (testType === 1) {
        $('#word_jia').text(jpword.word);
        $('#word_cn').text(jpword.wordCn);
        $('#word_mean').html("<div style='display: flex; justify-content: center;'><input id='mean' class='form-control' type='text' placeholder='请输入中文含义' style='width: 30%;'></div>");
    } else if (testType === 2) {  // 假名测试
        $('#word_jia').html("<div style='display: flex; justify-content: center;'><input id='jia' class='form-control' type='text' placeholder='请输入日文假名' style='width: 30%;'></div>");
        $('#word_cn').text(jpword.wordCn);
        $('#word_mean').text(jpword.zhMean);
    } else if (testType === 3) {
        $('#word_jia').text(jpword.word);
        $('#word_cn').html("<div style='display: flex; justify-content: center;'><input id='cn' class='form-control' type='text' placeholder='请输入日语单词(非假名)' style='width: 30%;'></div>");
        $('#word_mean').text(jpword.zhMean);
    }
}

function next() {

    index++;
    if (index === length) {
        // 练习结束 将数据传回后台
        parent.layer.msg("本课学习完成")

        $.ajax({
            cache: false,
            type: "POST",
            url : prefix + "/learn",
            async: false,
            data : {
                "data": JSON.stringify(wordsArray)
            },
            error : function(request) {
                parent.layer.alert("更新单词数据失败");
            },
            success : function(data) {
                if (data.code == 0) {
                    parent.layer.msg("操作成功");
                    let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                    parent.layer.close(index);
                } else {
                    parent.layer.msg("更新单词数据失败");
                }
            }
        });
        return;
    }

    checkExercise();
    let testType = Math.floor(Math.random() * 3) + 1;
    $('#testType').val(testType);
    let jpword = wordsArray[index];
    setText(jpword, testType);
}

function checkExercise() {

    let testType = $('#testType').val();
    let jpword = wordsArray[index];
    if (jpword.learned === 0) {
        jpword.learned = 1;
    }
    jpword.learnTime = jpword.learnTime + 1;
    if (testType === '1') {
        let means = jpword.zhMean;
        let res = 0;
        let ans = $('#mean').val();
        if (ans === 'undefined') {
            parent.layer.msg("回答错误");
            return;
        }
        means.split(';').forEach(item=>{
            if (item === ans) {
                res = 1;
            }
        });
        if (res === 0) {
            parent.layer.msg("回答错误");
            return;
        } else if (res === 1) {
            parent.layer.msg("回答正确");
            return;
        }

    } else if (testType === '2') {
        let jia = jpword.word;
        let ans = $('#jia').val();
        if (ans === 'undefined') {
            parent.layer.msg("回答错误");
            return;
        }
        if (jia === ans) {
            parent.layer.msg("回答正确");
            return;
        } else {
            parent.layer.msg("回答错误");
            return;
        }
    } else if (testType === '3') {
        let cn = jpword.wordCn;
        let ans = $('#cn').val();
        if (ans === 'undefined') {
            parent.layer.msg("回答错误");
            return;
        }
        if (cn === ans) {
            parent.layer.msg("回答正确");
            return;
        } else {
            parent.layer.msg("回答错误");
            return;
        }
    }
}

function prev() {
    if (index === 0) {
        parent.layer.msg("已经是最前一个单词啦");
        return;
    }

    let jpword = wordsArray[--index];
    setText(jpword);
}

// 点击已学会
function passed() {
    let jpword = wordsArray[index];
    jpword.learned = 2;
    next();
}

function remWord() {

    let id = $('#lessonId').val();
    let index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);

    parent.layer.open({
        type: 2,
        title: '背单词',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/rem/start/' + id,
    });

}




