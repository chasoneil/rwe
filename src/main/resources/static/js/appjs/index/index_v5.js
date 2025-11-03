
$(document).ready(function () {

    $(".chosen-select").chosen();
    initCalendar();
    initEchartsData();

    initStatistic();

});


function initStatistic() {

    const month = new Date().getMonth() + 1;
    const year = new Date().getFullYear();

    $.ajax({
        cache : true,
        type : "POST",
        url : "rwe/index/statistic",
        data : {
            "year" : year,
            "month" : month
        },
        async : false,
        error : function(request) {
            parent.layer.alert("Connection error");
        },
        success : function(data) {
            if (data.code === 0) {

            } else {
                parent.layer.msg("获取支出数据失败");
            }
        }
    });
}

// echarts
let pieChart1 = echarts.init(document.getElementById('pieChart1'));
let pieChart2 = echarts.init(document.getElementById('pieChart2'));

let pieOption1 = {
    tooltip: {
        trigger: 'item'
    },
    title: {
        text: '支出',
        left: 'center'
    },
    legend: {
        top: '5%',
        left: 'center'
    },
    series: [{
        name: '所占比例',
        type: 'pie',
        radius: ['50%', '70%'],     // 饼图的直径 50% - 70% ->内层50% - 70%外层
        avoidLabelOverlap: false,
        itemStyle: {        // 定义边框样式
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
        },
        emphasis: {
            itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
        },
        data: []
    }]
};

let pieOption2 = {
    tooltip: {
        trigger: 'item'
    },
    title: {
        text: '收入',
        left: 'center'
    },
    legend: {
        top: '5%',
        left: 'center'
    },
    series: [{
        name: '所占比例',
        type: 'pie',
        radius: ['50%', '70%'],     // 饼图的直径 50% - 70% ->内层50% - 70%外层
        avoidLabelOverlap: false,
        itemStyle: {        // 定义边框样式
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
        },
        emphasis: {
            itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
        },
        data: []
    }]
};

pieChart1.setOption(pieOption1);
pieChart2.setOption(pieOption2);


function initEchartsData() {

    let date = '';

    let defaultData = [
        { value: 335, name: '食品' },
        { value: 310, name: '交通' },
        { value: 234, name: '娱乐' },
        { value: 135, name: '购物' },
    ];

    $.ajax({
        cache : true,
        type : "POST",
        url : "rwe/index/pie/out",
        data : {
            "date" : date
        },
        async : false,
        error : function(request) {
            parent.layer.alert("Connection error");
        },
        success : function(data) {
            if (data.code === 0) {
                pieOption1.series[0].data = data.data;
                pieChart1.setOption(pieOption1);
            } else {
                parent.layer.msg("获取支出数据失败");
                pieOption1.series[0].data = defaultData;
                pieChart1.setOption(pieOption1);
            }
        }
    });

    $.ajax({
        cache : true,
        type : "POST",
        url : "rwe/index/pie/in",
        data : {
            "date" : date
        },
        async : false,
        error : function(request) {
            parent.layer.alert("Connection error");
        },
        success : function(data) {
            if (data.code === 0) {
                pieOption2.series[0].data = data.data;
                pieChart2.setOption(pieOption2);
            } else {
                parent.layer.msg("获取收入数据失败");
                pieOption2.series[0].data = defaultData;
                pieChart2.setOption(pieOption2);
            }
        }
    });

}

function initCalendar() {
    /*---- initialize the calendar ------*/
    let date = new Date();
    let d = date.getDate();
    let m = date.getMonth();
    let y = date.getFullYear();

    $('#calendar').fullCalendar({
        height: 600,
        contentHeight: 500,
        header: {
            left: 'prev,next',
            center: 'title',
            right: 'month'
        },
        editable: true,
        droppable: true,
        drop: function (date, allDay) {

            let originalEventObject = $(this).data('eventObject');
            let copiedEventObject = $.extend({}, originalEventObject);

            copiedEventObject.start = date;
            copiedEventObject.allDay = allDay;

            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

            if ($('#drop-remove').is(':checked')) {
                $(this).remove();
            }
        },
        dayClick: function (date, jsEvent, view) {
            alert('开始时间: ' + date);
        },
        dayRender: function (date, cell) {
            let cDate = new Date(date);
            let year = cDate.getFullYear();
            let month = String(cDate.getMonth() + 1).padStart(2, '0');
            let day = String(cDate.getDate()).padStart(2, '0');
            let dateStr = `${year}-${month}-${day}`;
            $.ajax({
                cache : true,
                type : "POST",
                url : "rwe/index/calendar",
                data : {
                    "date" : dateStr
                },
                async : false,
                error : function(request) {
                    parent.layer.alert("Connection error");
                },
                success : function(data) {
                    console.log(data);
                    if (data.code === 0) {
                        cell.append('<span style="font-weight: bold; color: lightskyblue; margin-left: 5px;"> 收入: ' + data.income + '</span><br/>');
                        cell.append('<span style="font-weight: bold; color: indianred; margin-left: 5px;"> 支出: ' + data.spent + '</span>');
                    }
                }
            });
        },
        events: []
    });
}

function updateClock() {
    const now = new Date();

    // 格式化日期
    // const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
    // const currentDate = now.toLocaleDateString('zh-CN', options);
    // document.getElementById('date').textContent = currentDate;

    // 格式化时间
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    document.getElementById('clock').textContent = `${hours}:${minutes}:${seconds}`;
}

setInterval(updateClock, 1000);
updateClock(); // 初始调用以显示时钟



