
$(document).ready(function () {

    initCalendar();

    let data =[];
    initEchartsData();

    var mapData = {
        "US": 298,
        "SA": 200,
        "DE": 220,
        "FR": 540,
        "CN": 120,
        "AU": 760,
        "BR": 550,
        "IN": 200,
        "GB": 120,
    };

    $('#world-map').vectorMap({
        map: 'world_mill_en',
        backgroundColor: "transparent",
        regionStyle: {
            initial: {
                fill: '#e4e4e4',
                "fill-opacity": 0.9,
                stroke: 'none',
                "stroke-width": 0,
                "stroke-opacity": 0
            }
        },

        series: {
            regions: [{
                values: mapData,
                scale: ["#1ab394", "#22d6b1"],
                normalizeFunction: 'polynomial'
            }]
        },
    });

});

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
        radius: ['40%', '70%'],     // 饼图的直径 40% - 70% ->内层40% - 70%外层
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
        radius: ['40%', '70%'],     // 饼图的直径 40% - 70% ->内层40% - 70%外层
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
        header: {
            left: 'prev,next',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
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
        events: [
            {
                title: '日事件',
                start: new Date(y, m, 1)
            },
            {
                id: 999,
                title: '重复事件',
                start: new Date(y, m, d - 3, 16, 0),
                allDay: false,
            },
            {
                id: 999,
                title: '重复事件',
                start: new Date(y, m, d + 4, 16, 0),
                allDay: false
            },
            {
                title: '会议',
                start: new Date(y, m, d, 10, 30),
                allDay: false
            },
            {
                title: '午餐',
                start: new Date(y, m, d, 12, 0),
                end: new Date(y, m, d, 14, 0),
                allDay: false
            },
            {
                title: '生日',
                start: new Date(y, m, d + 1, 19, 0),
                end: new Date(y, m, d + 1, 22, 30),
                allDay: false
            },
            {
                title: '打开百度',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                url: 'http://baidu.com/'
            }
        ],
    });
}

function updateClock() {
    const now = new Date();

    // 格式化日期
    const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
    const currentDate = now.toLocaleDateString('zh-CN', options);
    document.getElementById('date').textContent = currentDate;

    // 格式化时间
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const currentTime = `${hours}:${minutes}:${seconds}`;
    document.getElementById('clock').textContent = currentTime;
}


// function generateCalendar() {
//     const calendar = document.getElementById('calendar');
//     const now = new Date();
//     const year = now.getFullYear();
//     const month = now.getMonth();
//
//     // 获取当前月份的第一天和最后一天
//     const firstDay = new Date(year, month, 1);
//     const lastDay = new Date(year, month + 1, 0);
//
//     // 找到第一天是星期几，注意这里要调整为周一开始
//     const startDay = (firstDay.getDay() + 6) % 7; // 将 Sunday (0) 转为 Saturday (6)
//     const daysInMonth = lastDay.getDate();
//
//     // 添加表头
//     const weekdays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
//     let headerRow = '<tr>';
//     weekdays.forEach(day => {
//         headerRow += `<th>${day}</th>`;
//     });
//     headerRow += '</tr>';
//     calendar.innerHTML += headerRow;
//
//     // 填充日期
//     let dateRow = '<tr>';
//     // 填充前面的空白
//     for (let i = 0; i < startDay; i++) {
//         dateRow += '<td></td>';
//     }
//
//     // 填充日期
//     for (let day = 1; day <= daysInMonth; day++) {
//         dateRow += `<td>${day}</td>`;
//         if ((day + startDay) % 7 === 0) { // 每七天换行
//             calendar.innerHTML += dateRow;
//             dateRow = '<tr>'; // 换行
//         }
//     }
//
//     // 如果当前行没有结束，添加行
//     if (dateRow !== '<tr>') {
//         calendar.innerHTML += dateRow;
//     }
// }
//
// generateCalendar();

setInterval(updateClock, 1000);
updateClock(); // 初始调用以显示时钟



