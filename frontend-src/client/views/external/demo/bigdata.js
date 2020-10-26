var ctxPath = $('meta[name="__ctx__"]').length > 0 ? $('meta[name="__ctx__"]')[0].content : ""; //上下文
// $(document).toggleFullScreen();
// $(document).toggleFullScreen();
$(document).ready(function () {

    setInterval("TimeRefresh()", 120000);

    var defLineNo = $("#lineNoDiv").find("input.current").attr("lineNo");
    getOperationalData(defLineNo);

    $(".btns input").on("click", function () {
        fullscreen();
        $(this).addClass('current').siblings('input').removeClass('current');
        var index = $(this).index();
        var lineNo = $(this).attr("lineNo");
        //显示对应的div
        // $('.item').eq(index).show().siblings().hide();
        getOperationalData(lineNo);//根据产线号 查询对应的数据
    });
    // 显示时间
    // jeDate(".choose_li", {
    //     trigger: "click",  //可绑定一个或多个事件
    //     festival: true,    //是否显示农历
    //     isinitVal: true,   //是否初始化时间
    //     initDate: [{DD: "-6"}, true],  //增加时间
    //     // minDate: '2000-06-16',   //最大时间
    //     maxDate: function () {
    //         var maxDate = $(".choose_li1").text();
    //         return maxDate === "" ? jeDate.nowDate({DD: ""}) : maxDate;
    //     },   //最小时间
    //     format: "YYYY-MM-DD ",  //显示格式
    //     zIndex: 100
    // });
    // jeDate(".choose_li1", {
    //     trigger: "click",  //可绑定一个或多个事件
    //     festival: true,    //是否显示农历
    //     isinitVal: true,   //是否初始化时间
    //     initDate: [{DD: ""}, true],  //增加时间
    //     minDate: function () {
    //         var minDate = $(".choose_li").text();
    //         return minDate === "" ? jeDate.nowDate({DD: "-6"}) : minDate;
    //     },   //最大时间
    //     // maxDate: '2125-06-16',   //最小时间
    //     format: "YYYY-MM-DD ",  //显示格式
    //     zIndex: 100
    // });
    jeDate(".icon1", {
        trigger: "click",  //可绑定一个或多个事件
        festival: true,    //是否显示农历
        isinitVal: true,   //是否初始化时间
        initDate: [{DD: "-6"}, true],  //增加时间
        maxDate: function () {
            var maxDate = $(".icon3").text();
            return maxDate === "" ? jeDate.nowDate({DD: ""}) : maxDate;
        },   //最大时间
        // maxDate: '2125-06-16',   //最小时间
        format: "YYYY-MM-DD ",  //显示格式
        // zIndex:100
    });
    jeDate(".icon3", {
        trigger: "click",  //可绑定一个或多个事件
        festival: true,    //是否显示农历
        isinitVal: true,   //是否初始化时间
        initDate: [{DD: ""}, true],  //增加时间
        minDate: function () {
            var minDate = $(".icon1").text();
            return minDate === "" ? jeDate.nowDate({DD: "-6"}) : minDate;
        },   //最大时间
        // maxDate: '2125-06-16',   //最小时间
        format: "YYYY-MM-DD ",  //显示格式
        // zIndex:100
    });

    //产线产量
    getLineYield();
    //项目进度统计
    getProjectProgress();
    //堆场曲线
    getYardcurve();
    //年度累计产量
    getCumulative();

    //导航车间拖拽事件
    $('.line-box').mousedown(function (e) {
        var width = $('.line-box').width();
        var widthBox = $('#lineNoDiv').width();
        var positionDiv = $(this).offset();
        var distenceX = e.pageX - positionDiv.left;
        $(document).mousemove(function (e) {
            var x = e.pageX - distenceX;
            if (x < -width) {
                x = -width;
            } else if (x > 0) {
                x = 0;
            }
            $('.line-box').css({
                'left': x + 'px',
            });
        });

        $(document).mouseup(function () {
            $(document).off('mousemove');
        });
    });
});

var fontSize = 20;

function TimeRefresh() {
    var nowTime = getNowTime();
    var sixDaysAgo = getBeforeDate(6);
    $(".icon1").text(sixDaysAgo);
    $(".icon3").text(nowTime);
    $(".choose_li").text(sixDaysAgo);
    $(".choose_li1").text(nowTime);
    var lineNo = $("#lineNoDiv").find("input.current").attr("lineNo");
    getOperationalData(lineNo);
    getLineYield();
    getProjectProgress();
    getYardcurve();
    getCumulative();
}

//根据产线号 查询对应的数据
function getOperationalData(lineNo) {

    $.ajax({
        type: "post",
        url: ctxPath + "/webServer/getOperationalData",
        data: {"lineNo": lineNo},
        dataType: "json",
        success: function (data) {
            if (data.flag === 1) {
                var _data = data.result;
                $("#prodCount").text(_data.prodCount);
                $("#planProdVol").text(_data.planProdVol);
                $("#pouringCount").text(_data.pouringCount);
                $("#pouringVol").text(_data.pouringVol);
                $("#storageCount").text(_data.storageCount);
                $("#storageVol").text(_data.storageVol);
                $("#shipCount").text(_data.shipCount);
                $("#shipVol").text(_data.shipVol);
                //渲染计划完成率
                planCompletionRate(_data.unPlan, _data.okPlan);
                //渲染模台使用率 <空闲，占用>
                plateUsageRate(_data.idlePlate, _data.occupyPlate);
                $("#hckCount").text(_data.hckCount);
                $("#ckCount").text(_data.ckCount);
                $("#hckImgCount").text(_data.hckImgCount + "张");
                $("#firstPasssRate").text(_data.firstPasssRate);
                $("#avgProdCycle").text(_data.avgProdCycle);
                $("#avgInvTurnover").text(_data.avgInvTurnover);

            }
        },
        error: function () {
        }
    });
}

// 第一个数据初始化列表
function planCompletionRate(unPlan, okPlan) {
    var plan1 = (okPlan / (unPlan + okPlan) * 100 > 0 ? okPlan / (unPlan + okPlan) * 100 : 0).toFixed(2);
    var myChart1 = echarts.init(document.getElementById('Echarts01'));
    var option1 = {
        legend: {
            // top: '0%',
            // right: '1.17004%',
            // left: '5%',
            // itemGap: 0,
            orient: 'horizontal',
            // x: 'top',
            // y: 'center',
            data: [
                {
                    name: '已完成',
                    textStyle: {fontWeight: 'bold', color: '#289df5', fontSize: 10}
                },
                {
                    name: '未完成',
                    textStyle: {fontWeight: 'bold', color: '#ff5050', fontSize: 10}
                }],
            icon: 'rect',
            // top: 100,
            // right: '1.17004%',

            // textStyle: {
            //     padding: [0, 0, 0, 5],
            //     color: 'rgba(255,255,255,0.87)'
            // }
        },
        tooltip: {
            trigger: 'item',
            formatter: "{b} : <br>{c}({d}%)",
            backgroundColor: 'rgba(0,0,0,0.3)', // 背景
            // padding: [8, 10], //内边距
            extraCssText: 'box-shadow: 0 0 3px rgba(255, 255, 255, 0.87);', //添加阴影
        },
        color: ['#289df5', '#ff5050'],
        series: [{
            type: 'pie',
            radius: ['70%', '50%'],
            center: ['50%', '60%'],
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: okPlan,
                name: '已完成',
                label: {
                    normal: {
                        formatter: plan1 + '%\n已完成',
                        textStyle: {
                            color: '#fff',
                            fontSize: 14
                        }
                    }
                }
            }, {
                value: unPlan,
                name: '未完成',
                label: {
                    normal: {
                        formatter: "",
                        textStyle: {
                            color: 'rgba(255,255,255,0.87)',
                            fontSize: 14
                        }
                    }
                }
            }]
        }]
    };
    var index = 0; //播放所在下标,使得tootip每隔三秒自动显示
    myChart1.setOption(option1);
    $(window).on('resize', function () {
        myChart1.resize();
    });
}

// 第二个数据初始化列表    模台使用率
function plateUsageRate(idlePlate, occupyPlate) {
    var myChart2 = echarts.init(document.getElementById('Echarts02'));
    // 下面设置属性开始
    var option2 = {
        title: {
            // text: '某站点用户访问来源',
            // subtext: '纯属虚构',
            // x:'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)",
            // position: ['5%', '50%']
            position: 'inside'

            // formatter: '{hr|}\n{b|{b}：}\n{c}  ',
        },
        legend: {
            orient: 'horizontal',
            left: '5%',
            data: [
                {
                    name: '使用中',
                    icon: 'rect',
                    textStyle: {fontWeight: 'bold', color: '#6d8fd0', fontSize: 10}
                },
                {
                    name: '空闲',
                    icon: 'rect',
                    textStyle: {fontWeight: 'bold', color: '#6d8fd0', fontSize: 10}
                },
            ]
        },
        series: [
            {
                name: '模台使用率',
                type: 'pie',
                radius: '65%',
                center: ['50%', '60%'],
                labelLine: {
                    normal: {
                        length: 0
                    }
                },
                data: [
                    {
                        value: occupyPlate,
                        name: '使用中',
                        itemStyle: {
                            normal: {
                                color: '#2f9ee3',
                                label: {
                                    show: true
                                }
                            }
                        },
                    },
                    {
                        value: idlePlate,
                        name: '空闲',
                        itemStyle: {
                            normal: {
                                color: '#ed971c',
                                label: {
                                    show: true,
                                    textStyle: {
                                        color: '#27727B'
                                    }
                                }
                            }
                        },
                    },
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    normal: {
                        label: {
                            show: true,
                            formatter: '{c}  ({d}%)'
                            // formatter: function (data) {
                            //     if (data.percent > 45) {
                            //         return data.data.value + "    \n(" + data.percent + "%)";
                            //     } else {
                            //         return data.data.value + "   (" + data.percent + "%)";
                            //     }
                            // }
                        },
                        labelLine: {show: true}
                    }
                }
            }
        ]
    };
    myChart2.setOption(option2);
    $(window).on('resize', function () {
        myChart2.resize();
    });
}


//产线产量
function getLineYield() {
    var staTime = $(".icon1").text();
    var endTime = $(".icon3").text();
    $.ajax({
        type: "post",
        url: ctxPath + "/webServer/getLineYield",
        data: {"staTime": staTime.trim(), "endTime": endTime.trim()},
        dataType: "json",
        success: function (data) {
            if (data.flag === 1) {
                var dataDetil = data.result;
                var selected = {};
                var legendData = [];
                var xAxisData = [];
                var series = [];
                //检查产线名是否超过五个字
                $('#lineNoDiv input').each(function (idx, dom) {
                    var lineName = $(dom).val();
                    if(lineName.length > 4){
                        var lastLineName = lineName.slice(0,4) + '...';
                        $(dom).val(lastLineName)
                    }
                });
                $.each(dataDetil, function (k, v) {
                    var lineName = v.lineName;
                    selected["'" + v.lineName + "'"] = true;
                    legendData.push(
                        {
                            name: v.lineName,
                            icon: 'rect',
                            // textStyle: {fontWeight: 'bold', color: '#6d8fd0'}
                        }
                    );
                    if (xAxisData.length === 0) {
                        $.each(v.dayDataMap, function (m, n) {
                            xAxisData.push(m);
                        })
                    }
                    var seriesData = [];
                    $.each(v.dayDataMap, function (m, n) {
                        seriesData.push(n);
                    });
                    series.push({
                        name: lineName,
                        type: 'bar',
                        // barWidth: 25,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: false
                                }
                            }
                        },
                        data: seriesData
                    });
                });
                var legendWidth = $('#yjEcharts01').width() - 120;
                // 产线产量数据开始
                var myChart3 = echarts.init(document.getElementById('yjEcharts01'));
                //指定图标的配置和数据
                var option3 = {
                    title: {
                        text: '产线产量',
                        textStyle: {
                            color: '#34ebf1',
                            fontSize: fontSize,
                            y: "center"
                        },
                        x: 10,
                        y: 5,
                    },
                    color: ['#2DA1EC', '#E69D10', '#10B18F', '#DB4234', '#00D259', '#F1846F', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
//                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        type: 'scroll',
                        right: '4%', // 'center' | 'left' | {number},
                        y: 'top', // 'center' | 'bottom' | {number}
                        width: legendWidth,
				        // backgroundColor: '#eee',
                        borderColor: 'rgba(178,34,34,0.8)',
//				        borderWidth: 4,
                        padding: 10,    // [5, 10, 15, 20]
                        itemGap: 20,
                        textStyle: {color: '#3AFAFF'},
                        selected: selected,
                        data: legendData
                    },
                    calculable: true,
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        top: 40,
                        containLabel: true,
                        // height: '70%'
                    },
                    xAxis: [{
                        type: 'category',
                        data: xAxisData,
                        axisLine: {
                            lineStyle: {
                                color: "#6d8fd0",
                                width: 2,
                            }
                        },
                    },
                        // {
                        //     type: 'category',
                        //     axisLine: {
                        //         show: false
                        //     },
                        //     axisTick: {
                        //         show: false
                        //     },
                        //     axisLabel: {
                        //         show: false
                        //     },
                        //     splitArea: {
                        //         show: false
                        //     },
                        //     splitLine: {
                        //         show: false
                        //     },
                        // },
                    ],
                    yAxis: [{
                        //name: "项目总方量",
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}',
                            interval: 10
                        },
                        axisLine: {
                            lineStyle: {
                                color: "#6d8fd0",
                                width: 2
                            }
                        },
                        //设置网格线颜色
                        splitLine: {
                            show: true,
                            lineStyle: {
                                color: ['#233382'],
                                width: 2,
                                type: 'solid',
                            }
                        },
                    }],
                    series: series
                };
                //使用制定的配置项和数据显示图表
                // $("#yjEcharts01").html("");
                myChart3.setOption(option3);
                $(window).on('resize', function () {
                    myChart3.resize();
                });
            }
        },
        error: function () {
        }
    });
    getYardcurve();
}

//项目进度统计
function getProjectProgress() {
    $.ajax({
        type: "post",
        url: ctxPath + "/webServer/getProjectProgress",
        dataType: "json",
        success: function (data) {
            var color = {"未生产": "#D34634", "在库": "#F09723", "已发货": "#319CEC", "已浇筑": "#17B091"};
            if (data.flag === 1) {
                var dataDetil = data.result;
                var selected = {};
                var legendData = [];
                var xAxisData = [];
                var series = [];
                var seriesBarWidth = 0;
                try {
                    if(dataDetil && dataDetil[0] && Object.keys(dataDetil[0].projectDataMap).length < 6){
                        seriesBarWidth = 25
                    }
                }catch(e) {}
                $.each(dataDetil, function (k, v) {
                    var type = v.type;
                    selected["'" + v.type + "'"] = true;
                    legendData.push(
                        {
                            name: v.type,
                            icon: 'rect',
                            textStyle: {fontWeight: 'bold', color: '#6d8fd0'}
                        }
                    );
                    if (xAxisData.length === 0) {
                        $.each(v.projectDataMap, function (m, n) {
                            xAxisData.push(m);
                        })
                    }
                    var seriesData = [];
                    $.each(v.projectDataMap, function (m, n) {
                        seriesData.push(n);
                    });
                    series.push(
                        {
                            name: type,
                            type: 'bar',
                            barWidth: seriesBarWidth,
                            barGap: '30',//柱图间距
                            itemStyle: {
                                normal: {
                                    color: color[type],
                                    label: {
                                        show: true,
                                        textStyle: {
                                            color: 'white'
                                        }
                                    }
                                }
                            },
                            stack: '总量',
                            label: {
                                normal: {
                                    show: false,
                                    position: 'insideRight'
                                }
                            },
                            data: seriesData
                        });
                });
                var legendWidth = $('#yjEcharts02').width() - 120;
                var myChart4 = echarts.init(document.getElementById('yjEcharts02'));
                var option4 = {
                    title: {
                        text: '项目进度',
                        textStyle: {
                            color: '#34F4F7',
                            fontSize: fontSize
                        },
                        x: 10,
                        y: 5
                    },
                    tooltip: {
                        trigger: 'axis',
                        confine: true,
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        type: "scroll",
                        orient: 'horizontal', // 'vertical'
                        right: '4%', // 'center' | 'left' | {number},
                        y: 'top', // 'center' | 'bottom' | {number}
                        width: legendWidth,
//				        backgroundColor: '#eee',
                        borderColor: 'rgba(178,34,34,0.8)',
//				        borderWidth: 4,
                        padding: 10,    // [5, 10, 15, 20]
                        itemGap: 20,
                        selected: selected,
                        data: legendData
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        top: 40,
                        containLabel: true,
                        // height: '68%'
                    },
                    xAxis: {
                        type: 'category',
                        data: xAxisData,
                        axisLine: {
                            lineStyle: {
                                color: "#6d8fd0",
                                width: 2,
                            }
                        },
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        },
                        axisLine: {
                            lineStyle: {
                                color: "#6d8fd0",
                                width: 2
                            }
                        },
                        // max:maxY,
                        // min:minY,
                        // splitNumber:100,
                        //设置网格线颜色
                        splitLine: {
                            show: true,
                            lineStyle: {
                                color: ['#233382'],
                                width: 2,
                                type: 'solid',
                            }
                        },
                    },
                    series: series
                };
                //使用制定的配置项和数据显示图表
                myChart4.setOption(option4);
                $(window).on('resize', function () {
                    myChart4.resize();
                })

            }
        },
        error: function () {
        }
    });


}
//堆场曲线
function getYardcurve() {
    var staTime = $(".icon1").text();
    var endTime = $(".icon3").text();
    // var staTime = $(".choose_li").text();
    // var endTime = $(".choose_li1").text();
    $.ajax({
        type: "post",
        url: ctxPath + "/webServer/getYardcurve",
        data: {"staTime": staTime.trim(), "endTime": endTime.trim()},
        dataType: "json",
        success: function (data) {
            if (data.flag === 1) {
                var dataDetil = data.result;
                var selected = {};
                var legendData = [];
                var xAxisData = [];
                var series = [];
                $.each(dataDetil, function (k, v) {
                    var type = v.type;
                    selected["'" + v.type + "'"] = true;
                    legendData.push(v.type);
                    if (xAxisData.length === 0) {
                        $.each(v.dayDataMap, function (m, n) {
                            xAxisData.push(m);
                        })
                    }
                    var seriesData = [];
                    $.each(v.dayDataMap, function (m, n) {
                        seriesData.push(n);
                    });
                    if (type === "在库") {
                        series.push({
                            name: type,
                            type: 'line',
                            smooth: true,
                            yAxisIndex: 1,
                            // stack: '总量',
                            data: seriesData
                        });
                    } else {
                        series.push({
                            name: type,
                            type: 'line',
                            smooth: true,
                            // stack: '总量',
                            data: seriesData
                        });
                    }
                });
                // 堆场数据展示
                var myChart5 = echarts.init(document.getElementById('yjEcharts03'));
                // var option5 = {
                //     title: {
                //         text: '堆场曲线',
                //         textStyle: {
                //             color: '#34ebf1',
                //             fontSize: fontSize
                //         },
                //         x: 10,
                //         y: 5
                //     },
                //     tooltip: {
                //         trigger: 'axis'
                //     },
                //     selected: selected,
                //     color: ['#2F9FE1', '#E99B1D', '#808bc6'],
                //     legend: {
                //         x: '70%', // 'center' | 'left' | {number},
                //         y: '3%',
                //         data: legendData,
                //         textStyle: {fontWeight: 'bold', color: '#6d8fd0'}
                //     },
                //     grid: {
                //         left: '3%',
                //         right: '4%',
                //         bottom: '3%',
                //         containLabel: true,
                //         height:'70%'
                //     },
                //     xAxis: [{
                //         type: 'category',
                //         boundaryGap: false,
                //         data: xAxisData,
                //         axisLine: {
                //             lineStyle: {
                //                 color: "#6d8fd0",
                //                 width: 2,
                //             }
                //         },
                //     }],
                //     yAxis: [{
                //         type: 'value',
                //         axisLine: {
                //             lineStyle: {
                //                 color: "#6d8fd0",
                //                 width: 2
                //             }
                //         },
                //         //设置网格线颜色
                //         splitLine: {
                //             show: true,
                //             lineStyle: {
                //                 color: ['#233382'],
                //                 width: 2,
                //                 type: 'solid',
                //             }
                //         },
                //     }],
                //     series: series
                // };

                var legendWidth = $('#yjEcharts03').width() - 120;
                var option = {
                    title: {
                        text: '堆场曲线',
                        textStyle: {
                            color: '#34ebf1',
                            fontSize: fontSize
                        },
                        x: 10,
                        y: 5
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    selected: selected,
                    color: ['#2D9DEF', '#EC9A1A', '#3CF9FB'],
                    legend: {
                        type: "scroll",
                        orient: 'horizontal', // 'vertical'
                        right: '4%', // 'center' | 'left' | {number},
                        y: '3%',
                        width: legendWidth,
                        data: legendData,
                        textStyle: {fontWeight: 'bold', color: '#6d8fd0'}
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        top: 65,
                        containLabel: true,
                        // height: '70%'
                    },
                    xAxis: [{
                        type: 'category',
                        boundaryGap: false,
                        data: xAxisData,
                        axisLine: {
                            lineStyle: {
                                color: "#6d8fd0",
                                width: 2,
                            }
                        },
                    }],
                    calculable: true,
                    yAxis: [
                        {
                            type: 'value',
                            name: "入库、发货",
                            position: 'left',
                            axisLine: {
                                lineStyle: {
                                    color: "#6d8fd0",
                                    width: 2
                                }
                            },
                            //设置网格线颜色
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: ['#233382'],
                                    width: 2,
                                    type: 'solid',
                                }
                            },
                        },
                        {
                            type: 'value',
                            name: "在库",
                            position: 'right',
                            axisLine: {
                                lineStyle: {
                                    color: "#3CF9FB",
                                    width: 2
                                }
                            },
                            //设置网格线颜色
                            splitLine: {
                                show: false,
                                lineStyle: {
                                    color: ['#233382'],

                                    type: 'solid',
                                }
                            }
                        }
                    ],
                    series: series
                };


                //使用制定的配置项和数据显示图表
                myChart5.setOption(option);
                $(window).on('resize', function () {
                    myChart5.resize();
                })

            }
        },
        error: function () {
        }
    });
}

//年度累计产量
function getCumulative() {
    $.ajax({
        type: "post",
        url: ctxPath + "/webServer/getCumulative",
        dataType: "json",
        success: function (data) {
            if (data.flag === 1) {
                var dataDetil = data.result;
                var legendData = [];
                var seriesData = [];
                var total = 0.000;
                $.each(dataDetil, function (k, v) {
                    legendData.push(k);
                    seriesData.push({
                        value: v,
                        name: k,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true
                                }
                            }
                        }
                    });
                    total += v;
                });

                var myChart6 = echarts.init(document.getElementById('yjEcharts04'));
                // 下面设置属性开始
                var option6 = {
                    title: {
                        text: total.toFixed(3),
                        // subtext: '纯属虚构',
                        x: "center",
                        y: "top",
                        textStyle: {//主标题文本样式{"fontSize": 18,"fontWeight": "bolder","color": "#333"}
                            fontSize: fontSize,
                            color: "#26d3da",
                            fontStyle: 'normal'
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    series: [
                        {
                            name: '年度生产量',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '50%'],
                            labelLine: {
                                normal: {
                                    length: 3
                                }
                            },
                            data: seriesData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                },
                                normal: {
                                    label: {
                                        show: true,
                                        formatter: '{b}\n{c}'
                                    },
                                    labelLine: {show: true}
                                }
                            }
                        }
                    ]
                };
                myChart6.setOption(option6);
                $(window).on('resize', function () {
                    myChart6.resize();
                });
            }
        },
        error: function () {
        }
    });

}

//获取当前时间的前N天
function getBeforeDate(n) {
    var n = n;
    var d = new Date();
    var year = d.getFullYear();
    var mon = d.getMonth() + 1;
    var day = d.getDate();
    if (day <= n) {
        if (mon > 1) {
            mon = mon - 1;
        }
        else {
            year = year - 1;
            mon = 12;
        }
    }
    d.setDate(d.getDate() - n);
    year = d.getFullYear();
    mon = d.getMonth() + 1;
    day = d.getDate();
    s = year + "-" + (mon < 10 ? ('0' + mon) : mon) + "-" + (day < 10 ? ('0' + day) : day);
    return s;
}

function fullscreen() {
    //全屏
    var de = document.documentElement;
    if (de.requestFullscreen) {
        de.requestFullscreen();
    } else if (de.mozRequestFullScreen) {
        de.mozRequestFullScreen();
    } else if (de.webkitRequestFullScreen) {
        de.webkitRequestFullScreen();
    } else if (de.msRequestFullscreen) {
        de.msRequestFullscreen();
    } else {
        wtx.info("当前浏览器不支持全屏！");
    }

}

// 时间事件
function showLeftTime() {
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var day = now.getDate();
    if (day < 10) {
        day = '0' + day;
    }
    var hours = now.getHours();
    if (hours < 10) {
        hours = '0' + hours;
    }
    var minutes = now.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    var seconds = now.getSeconds();
    if (seconds < 10) {
        seconds = '0' + seconds;
    }
    var days = now.getDay();
    switch (days) {
        case 1:
            days = '星期一';
            break;
        case 2:
            days = '星期二';
            break;
        case 3:
            days = '星期三';
            break;
        case 4:
            days = '星期四';
            break;
        case 5:
            days = '星期五';
            break;
        case 6:
            days = '星期六';
            break;
        case 0:
            days = '星期日';
            break;

    }
    var showTime = year + "-" + month + "-" + day + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + hours + ":" + minutes + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + days;
    //一秒刷新一次显示时间
    var timeID = setTimeout(showLeftTime, 1000);
    // console.log(showTime);
    $('#bigdatabox #bigbox .yjSpan').html(showTime);
    // console.log(dateSpan.innerHTML);
}

function getNowTime() {
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    if (month < 10) {
        month = '0' + month;
    }
    var day = now.getDate();
    if (day < 10) {
        day = '0' + day;
    }
    var nowTime = year + "-" + month + "-" + day;
    return nowTime;
}

showLeftTime();
// window.addEventListener("resize", function () {
//
//     myChart1.resize();
//     myChart2.resize();
//     myChart3.resize();
//     myChart4.resize();
//     myChart5.resize();
//     myChart6.resize();
// });

$(window).resize(function () {

    var width = $(this).width();

    if (width <= 768) {
        if (fontSize != 16) {
            fontSize = 16;
            TimeRefresh();
        }
    } else if (width <= 992) {
        if (fontSize != 16) {
            fontSize = 16;
            TimeRefresh();
        }
    } else if (width <= 1200) {

        if (fontSize != 20) {
            fontSize = 20;
            TimeRefresh();
        }

    } else if (width <= 2000) {
        if (fontSize != 22) {
            fontSize = 22;
            TimeRefresh();
        }

    } else if (width <= 3000) {
        if (fontSize != 25) {
            fontSize = 25;
            TimeRefresh();
        }
    } else if (width <= 4000) {
        if (fontSize != 28) {
            fontSize = 28;
            TimeRefresh();
        }

    } else if (width > 4000) {
        if (fontSize != 30) {
            fontSize = 30;
            TimeRefresh();
        }

    }

});
