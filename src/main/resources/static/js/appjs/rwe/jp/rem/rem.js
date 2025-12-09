const prefix = "/rwe/jp/rem";

let wordsArray = [];
let index = 0;
let length = 0;

$(function () {
    loadData();

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Enter' || event.code === 'Enter' || event.keyCode === 13) {
            // 防止表单默认的提交行为
            event.preventDefault(); 
            next();
        }
    });
});

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
                wordsArray = JSON.parse(data.msg);
                length = wordsArray.length;
                wordsArray.sort(() => Math.random() - 0.5);
                exercise();
            } else {
                parent.layer.msg("加载单词数据失败");
            }
        }

    });
}

/*
 *  产生随机数表示练习的类型
 *  1. 写出中文翻译
 *  2. 写出假名
 *  3. 日语单词
 */
function exercise() { 
    // 默认练习： 写出中文翻译
    let jpword = wordsArray[index];   
    setText(jpword, '1');
}

function getPracticeType () {
    let form =  $('#selectForm');
    let practiceType = 4;
    let selectedRadio = form.find('input[name="practiceType"]:checked');
    if (selectedRadio.length > 0) {
        practiceType = selectedRadio.val();
    }
    return practiceType;
}

function next(passed) {

    if (index + 1 === length) {
        parent.layer.msg("已经是最后一个单词");
        return;
    }

    if (passed !== 'passed') {
        checkExercise();
    }

    $.ajax({
        cache: false,
        type: "POST",
        url : prefix + "/learn/word",
        async: false,
        data : {
            "data": JSON.stringify(wordsArray[index])
        },
        error : function(request) {
            parent.layer.alert("更新单词数据失败");
        },
        success : function(data) {
            if (data.code === 0) {
                // do nothing
            } else {
                parent.layer.msg("更新单词数据失败");
            }
        }
    });

    let testType = $('#testType').val();
    let practiceType = getPracticeType();
    if (practiceType === 4) {
        testType = Math.floor(Math.random() * 3) + 1;
    } else {
        testType = practiceType;
    }
    let jpword = wordsArray[++index];
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
        if (ans === 'undefined' || ans === '') {
            return;
        }
        means.split(';').forEach(item=>{
            if (item === ans) {
                res = 1;
            }
        });
        if (res === 0) {
            parent.layer.alert("回答错误,正确答案:" + means);
            return;
        } else if (res === 1) {
            parent.layer.msg("回答正确");
            return;
        }

    } else if (testType === '2') {
        let jia = jpword.word;
        let ans = $('#jia').val();
        if (ans === 'undefined' || ans === '') {
            return;
        }
        if (jia === ans) {
            parent.layer.msg("回答正确");
            return;
        } else {
            parent.layer.alert("回答错误,正确答案:" + jia);
            return;
        }
    } else if (testType === '3') {
        let cn = jpword.wordCn;
        let ans = $('#cn').val();
        if (ans === 'undefined' || ans === '') {
            return;
        }
        if (cn === ans) {
            parent.layer.msg("回答正确");
            return;
        } else {
            parent.layer.alert("回答错误,正确答案:" + cn);
            return;
        }
    }
}

function setText(jpword, testType) {

    $('#testType').val(testType);
    $('#word_voice').text(jpword.wordVoice);
    $('#word_type').text(jpword.wordType);
    
    if (testType === '1') {
        $('#word_jia').text(jpword.word);
        $('#word_cn').text(jpword.wordCn);
        $('#word_mean').html("<div style='display: flex; justify-content: center;'><input id='mean' class='form-control' autocomplete='off' type='text' placeholder='请输入中文含义' style='width: 30%;'></div>");
    } else if (testType === '2') {
        $('#word_jia').html("<div style='display: flex; justify-content: center;'><input id='jia' class='form-control' autocomplete='off' type='text' placeholder='请输入日文假名' style='width: 30%;'></div>");
        $('#word_cn').text(jpword.wordCn);
        $('#word_mean').text(jpword.zhMean);
    } else if (testType === '3') {
        $('#word_jia').text(jpword.word);
        $('#word_cn').html("<div style='display: flex; justify-content: center;'><input id='cn' class='form-control' autocomplete='off' type='text' placeholder='请输入日语单词(非假名)' style='width: 30%;'></div>");
        $('#word_mean').text(jpword.zhMean);
    }

    doBlur(testType);
}

function doBlur(testType) {
    if (testType === '1') {
        //$('#word_jia').addClass('blurred-span');
        // $('#word_cn').addClass('blurred-span');
    } else if (testType === '2') {
        $('#word_cn').addClass('blurred-span');
    } else if (testType === '3') {
        $('#word_jia').addClass('blurred-span');
    }
}

function show(id) {
    $('#' + id).removeClass('blurred-span');
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

    $.ajax({
        cache: false,
        type: "POST",
        url : prefix + "/learn/word",
        async: false,
        data : {
            "data": JSON.stringify(jpword)
        },
        error : function(request) {
            parent.layer.alert("更新单词数据失败");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("已学会该单词");
            } else {
                parent.layer.msg("更新单词数据失败");
            }
        }
    });

    next('passed');
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




