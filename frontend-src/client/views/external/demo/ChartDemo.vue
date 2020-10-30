<template>
  <div class="wrap">
    <div id="bigbox">
        <div class="wrap-title">{{title}}</div>
        <!-- 左边上部切换开始 -->
        <div class="btns" id="lineNoDiv">
            <div class="line-box">
                <!--<input th:each="lineNo:${lineMap}"  type="button" th name="" value="移动产线" class="current">-->
                <input type="button" name="lineNo" lineNo="001"
                   value="柔性线" class="current"><input type="button" name="lineNo" lineNo="002"
                   value="进口线"><input type="button" name="lineNo" lineNo="003"
                   value="固定线"><input type="button" name="lineNo" lineNo="004"
                   value="综合线">
            </div>
        </div>
        <!-- 左边上部切换结束 -->
        <!-- 计划字幕开始 -->
        <p class="planz">计划执行</p>
        <!-- 计划字幕结束 -->
        <!-- 左边上部表格内容开始 -->
        <div class="planbox">
            <!-- 切换表格开始 -->
            <div class="cons">
                <div class="clearfloat item active" style="display: block;">
                    <table border="1" width="85%" cellpadding="0" cellspacing="0">
                        <thead align="center">
                        <th colspan="3" rowspan="3">计划数量</th>
                        <th colspan="3" rowspan="3">计划方量</th>
                        <th colspan="3" rowspan="3">浇筑数量</th>
                        <th colspan="3" rowspan="3">浇筑方量</th>
                        </thead>
                        <tbody>
                        <tr>
                            <td colspan="3" rowspan="3" id="prodCount">数据</td>
                            <td colspan="3" rowspan="3" id="planProdVol">数据</td>
                            <td colspan="3" rowspan="3" id="pouringCount">数据</td>
                            <td colspan="3" rowspan="3" id="pouringVol">数据</td>
                        </tr>
                        </tbody>
                    </table>
                    <table border="1" cellspacing="0" width="85%" cellpadding="0">
                        <thead align="center">
                        <th colspan="3" rowspan="3">入库数量</th>
                        <th colspan="3" rowspan="3">入库方量</th>
                        <th colspan="3" rowspan="3">发货数量</th>
                        <th colspan="3" rowspan="3">发货方量</th>
                        </thead>
                        <tbody>
                        <tr>
                            <td colspan="3" rowspan="3" id="storageCount">数据</td>
                            <td colspan="3" rowspan="3" id="storageVol"></td>
                            <td colspan="3" rowspan="3" id="shipCount"></td>
                            <td colspan="3" rowspan="3" id="shipVol"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 切换表格结束 -->
        </div>
        <!-- 左边上部表格内容结束 -->
        <!-- 左边中部计划盒子开始 -->
        <div class="planlv">
            <p class="planlz">计划完成率</p>
            <div id="Echarts01"></div>
        </div>
        <!-- 左边中部计划盒子结束 -->
        <!-- 模台盒子开始 -->
        <div class="motai">
            <p class="planlz1">模台使用率</p>
            <div id="Echarts02"></div>
        </div>
        <!-- 模台盒子结束 -->
        <!-- 质量模块开始 -->
        <div class="qualitybox">

            <div class="qualitybox_title">质量监控</div>
            <div class="quality1">
                <ul>
                    <li id="hckCount">0</li>
                    <li>隐蔽检验</li>
                    <li id="ckCount">0</li>
                    <li>成品检验</li>
                </ul>
            </div>
            <div class="quality2">
                <div class="Img">

                </div>
            </div>
            <div class="quality3">
                <ul>
                    <li id="hckImgCount">0张</li>
                    <li>隐检拍照</li>
                    <li id="firstPasssRate">0.00%</li>
                    <li>成品合格率</li>
                </ul>
            </div>
        </div>
        <!-- 质量模块结束 -->
        <!-- 产线产量模块开始 -->
        <div class="yieldbox">
            <div class="tabs1" id="yjEcharts01"></div>
        </div>
        <!-- 产线产量模块结束 -->
        <!-- <div class="select-data" onclick="getLineYield()">
            查询
        </div> -->
        <!-- <div class="selected-data" onclick="getYardcurve()">
            查询
        </div> -->
        <!-- 项目进度模块 -->
        <div class="projectbox">
            <div class="tabs2" id="yjEcharts02"></div>
        </div>
        <!-- 项目进度模块结束 -->
        <!-- 堆场模块开始 -->
        <div class="yardbox">
            <!--<div class="contqi">-->
                <!--<div class="choose_li">-->
                    <!--<input id="test1" type="text" placeholder="请选择" readonly>-->
                <!--</div>-->
                <!--<div class="choose_li1">-->
                    <!--<input id="test2" type="text" placeholder="请选择" readonly>-->
                <!--</div>-->
            <!--</div>-->
            <div class="tabs3" id="yjEcharts03"></div>
        </div>
        <!-- 堆场模块结束 -->
        <!-- 年度累计产量 -->
        <div class="annual_output">
            年度累计产量(m³)
        </div>
        <!-- 累计产量模快 -->
        <div class="yearl">
            <div class="tabs4" id="yjEcharts04">

            </div>
        </div>
        <!-- 累计产量模块结束 -->
        <!-- 构件生产周期 -->
        <div class="component_quantity">
            <div class="quantityzi">
                构件平均生产周期
            </div>
            <div class="quantityImg">
                <div class="quantityshuju">
                    <p id="avgProdCycle">0</p>
                    <!--<p>小时</p>-->
                </div>
            </div>
        </div>
        <!-- 构件生产周期结束 -->
        <!-- 上面日历展示 -->
        <div class="calendar1 clearfix">
            <div class="chose_li">
                <div class="icon1">
                </div>
                <div class="icon2">

                </div>
            </div>
            <div class="chose_li1">
                <div class="icon3" onchange="getLineYield()">
                </div>
                <div class="icon4">

                </div>
            </div>
        </div>
        <!-- 日历展示结束 -->
        <!-- 库存周期 -->
        <div class="cunku">
            <div class="quantityzi">
                库存平均周转时间
            </div>
            <div class="cunkuImg">
                <div class="cunkushuju">
                    <p id="avgInvTurnover">0</p>
                    <!--<p>天</p>-->
                </div>
            </div>
        </div>
        <div class="yjSpan"></div>
        <!-- 库存周期结束 -->
        <!-- 天气日历 -->
        <!--<div class="forecast">
          <iframe id="fancybox-frame1" name="fancybox-frame1566785448214" frameborder="0" scrolling="no" hspace="0"  src="html/forest.html">
          </iframe>
        </div>-->
        <!-- 天气预报 -->
    </div>
  </div>
</template>

<script>
import echarts from 'echarts'
import $ from 'jquery'
import jeDate from './jedate.min.js'
import clientModel from '@/model/client-model.js'
import chartDemoModel from '@/model/charts/chartDemo.js'
import storage from 'good-storage'
export default {
  data(){
    return {
      dataIndex:0,
      oneProjectPie:null,
      oneProjectPieOption: {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          left: 'center',
          data: ['可用库存', '已用库存']
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: ['50%', '70%'],
            label: {
              normal: {
                show: true,
                position: 'center',
                formatter(params) {
                  if (params.name == '已用库存') {
                    return params.percent + "%\r\n已用库存"
                  } else {
                    return ''
                  }
                },
                textStyle: {
                  fontSize: '18',
                  fontWeight: 'bold',
                  color: '#333'
                }
              },
            },
            hoverAnimation: true,//是否开启hover在扇区上的放大效果
            silent: false,//图形是否不响应和触发鼠标事件,默认为false(响应),
            data: [
              { value: 335, name: '已用库存' },
              { value: 310, name: '可用库存' }
            ]
          },

        ]
      },
      title:'',
      baseData:{},
      baseData1:{},
      baseData2:{},
      baseData3:{},
      baseData01:{},
      baseData02:{},
      baseData03:{},
      baseData04:{},
      orgId:[],
      seconds:60
    }
  },
  mounted(){
    if(this.$route.params.chartId&&this.$route.params.chartTitle){
      storage.session.set('chartId', this.$route.params.chartId)
      storage.session.set('chartTitle', this.$route.params.chartTitle)
    }else{
      if(!sessionStorage.chartId&&!sessionStorage.chartTitle){
        this.$router.push('/mapIndex')
        return 
      }
    }
    this.orgId = storage.session.get('chartId')
    this.title = storage.session.get('chartTitle')

    

    this.bigdata()
  },
  methods:{
    firstLoad(){
      this.baseData1 = storage.get('baseData1')
      this.baseData2 = storage.get('baseData2')
      this.baseData3 = storage.get('baseData3')
      this.baseData01 = storage.get('baseData01')
      this.baseData02 = storage.get('baseData02')
      this.baseData03 = storage.get('baseData03')
      this.baseData04 = storage.get('baseData04')
    },
    getIndexMapNum1(){
      return chartDemoModel.getIndexMapNum1(this.orgId).then(data=>{
        this.baseData1 = data.entity
        storage.set('baseData1', data.entity)
      })
    },
    getIndexMapNum2(){
      return chartDemoModel.getIndexMapNum2(this.orgId).then(data=>{
        this.baseData2 = data.entity
        storage.set('baseData2', data.entity)
      })
    },
    getIndexMapNum3(){
      return chartDemoModel.getIndexMapNum3(this.orgId).then(data=>{
        this.baseData3 = data.entity
        storage.set('baseData3', data.entity)
      })
    },
    getIndexMapEcharts01(){
      return chartDemoModel.getIndexMapEcharts01(this.orgId).then(data=>{
        this.baseData01 = data.entity
        storage.set('baseData01', data.entity)
      })
    },
    getIndexMapEcharts02(){
      return chartDemoModel.getIndexMapEcharts02(this.orgId).then(data=>{
        this.baseData02 = data.entity
        storage.set('baseData02', data.entity)
      })
    },
    getIndexMapEcharts03(){
      return chartDemoModel.getIndexMapEcharts03(this.orgId).then(data=>{
        this.baseData03 = data.entity
        storage.set('baseData03', data.entity)
      })
    },
    getIndexMapEcharts04(){
      return chartDemoModel.getIndexMapEcharts04(this.orgId).then(data=>{
        this.baseData04 = data.entity
        storage.set('baseData04', data.entity)
      })
    },
    fullscreen() {
        //全屏
        var de = document.documentElement;
        log(de)
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
    },
    bigdata(){
      if(storage.get('baseData1')&&storage.get('baseData2')&&storage.get('baseData3')&&storage.get('baseData01')&&storage.get('baseData02')&&storage.get('baseData03')&&storage.get('baseData04')){
        this.baseData1 = storage.get('baseData1')
        this.baseData2 = storage.get('baseData2')
        this.baseData3 = storage.get('baseData3')
        this.baseData01 = storage.get('baseData01')
        this.baseData02 = storage.get('baseData02')
        this.baseData03 = storage.get('baseData03')
        this.baseData04 = storage.get('baseData04')
        getOperationalData(this.baseData1)
        $("#hckCount").text(this.baseData2.hckCount);
        $("#ckCount").text(this.baseData2.ckCount);
        $("#hckImgCount").text(this.baseData2.hckImgCount);
        $("#firstPasssRate").text(this.baseData2.firstPasssRate);
        $("#avgProdCycle").text(this.baseData3.avgProdCycle);
        $("#avgInvTurnover").text(this.baseData3.avgInvTurnover);
        getLineYield(this.baseData01)
        getProjectProgress(this.baseData02)
        getYardcurve(this.baseData03)
        getCumulative(this.baseData04)
      }
      

      this.getIndexMapNum1().then(_=>{
        getOperationalData(this.baseData1)
      })
      this.getIndexMapNum2().then(_=>{
        $("#hckCount").text(this.baseData2.hckCount);
        $("#ckCount").text(this.baseData2.ckCount);
        $("#hckImgCount").text(this.baseData2.hckImgCount);
        $("#firstPasssRate").text(this.baseData2.firstPasssRate);
      })
      this.getIndexMapNum3().then(_=>{
        $("#avgProdCycle").text(this.baseData3.avgProdCycle);
        $("#avgInvTurnover").text(this.baseData3.avgInvTurnover);
      })
      // 产线产量
      this.getIndexMapEcharts01().then(_=>{
        getLineYield(this.baseData01)
      })
      // // 项目进度
      this.getIndexMapEcharts02().then(_=>{
        getProjectProgress(this.baseData02)
      })
      // 堆场曲线
      this.getIndexMapEcharts03().then(_=>{
        getYardcurve(this.baseData03)
      })
      // 年度累计
      this.getIndexMapEcharts04().then(_=>{
        getCumulative(this.baseData04)
      })
      // this.$nextTick(()=>{
      //     var defLineNo = $("#lineNoDiv").find("input.current").attr("lineNo");
      //     getOperationalData(defLineNo);

      //     $(".btns input").on("click", function () {
      //         fullscreen();
      //         $(this).addClass('current').siblings('input').removeClass('current');
      //         var index = $(this).index();
      //         var lineNo = $(this).attr("lineNo");
      //         //显示对应的div
      //         // $('.item').eq(index).show().siblings().hide();
      //         getOperationalData(lineNo);//根据产线号 查询对应的数据
      //     });
      //     // //产线产量
      //     // getLineYield();
      //     // //项目进度统计
      //     // getProjectProgress();
      //     // //堆场曲线
      //     // getYardcurve();
      //     // //年度累计产量
      //     // getCumulative();

      //     //导航车间拖拽事件
      //     $('.line-box').mousedown(function (e) {
      //         var width = $('.line-box').width();
      //         var widthBox = $('#lineNoDiv').width();
      //         var positionDiv = $(this).offset();
      //         var distenceX = e.pageX - positionDiv.left;
      //         $(document).mousemove(function (e) {
      //             var x = e.pageX - distenceX;
      //             if (x < -width) {
      //                 x = -width;
      //             } else if (x > 0) {
      //                 x = 0;
      //             }
      //             $('.line-box').css({
      //                 'left': x + 'px',
      //             });
      //         });
      //         $(document).mouseup(function () {
      //             $(document).off('mousemove');
      //         });
      //     });
      // });

    var fontSize = 16;

    // function TimeRefresh() {
    //     var nowTime = getNowTime();
    //     var sixDaysAgo = getBeforeDate(6);
    //     $(".icon1").text(sixDaysAgo);
    //     $(".icon3").text(nowTime);
    //     $(".choose_li").text(sixDaysAgo);
    //     $(".choose_li1").text(nowTime);
    //     var lineNo = $("#lineNoDiv").find("input.current").attr("lineNo");
    //     getOperationalData(lineNo);
    //     getLineYield();
    //     getProjectProgress();
    //     getYardcurve();
    //     getCumulative();
    // }

    //根据产线号 查询对应的数据
    function getOperationalData(baseData1,baseData2,baseData3) {
        $("#prodCount").text(baseData1.prodCount);
        $("#planProdVol").text(baseData1.planProdVol);
        $("#pouringCount").text(baseData1.pouringCount);
        $("#pouringVol").text(baseData1.pouringVol);
        $("#storageCount").text(baseData1.storageCount);
        $("#storageVol").text(baseData1.storageVol);
        $("#shipCount").text(baseData1.shipCount);
        $("#shipVol").text(baseData1.shipVol);
        //渲染计划完成率
        planCompletionRate(baseData1.unPlan, baseData1.okPlan);
        //渲染模台使用率 <空闲，占用>
        plateUsageRate(baseData1.idlePlate, baseData1.occupyPlate);
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
function getLineYield(baseData) {
    var staTime = $(".icon1").text();
    var endTime = $(".icon3").text();
    var dataDetil = baseData.yjEcharts01.result;
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
            $.each(v.dataMap, function (m, n) {
                xAxisData.push(m);
            })
        }
        var seriesData = [];
        $.each(v.dataMap, function (m, n) {
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
            text: '产线产量(m³)',
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
                //type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            type: 'scroll',
            right: '4%', // 'center' | 'left' | {number},
            y: 'top', // 'center' | 'bottom' | {number}
            width: legendWidth,
            borderColor: 'rgba(178,34,34,0.8)',
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
        }],
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
    // getYardcurve();
}

//项目进度统计
function getProjectProgress(baseData) {
  var color = {"未生产": "#D34634", "在库": "#F09723", "已发货": "#319CEC", "已浇筑": "#17B091"};
  // if (data.flag === 1) {
  var dataDetil = baseData.yjEcharts02.result;
  var selected = {};
  var legendData = [];
  var xAxisData = [];
  var series = [];
  var seriesBarWidth = 0;
  try {
    if(dataDetil && dataDetil[0] && Object.keys(dataDetil[0].dataMap).length < 6){
        seriesBarWidth = 25
    }
  }catch(e) {}
  $.each(dataDetil, function (k, v) {
    var lineName = v.lineName;
    selected["'" + v.lineName + "'"] = true;
    legendData.push(
        {
            name: v.lineName,
            icon: 'rect',
            textStyle: {fontWeight: 'bold', color: '#6d8fd0'}
        }
    );
    if (xAxisData.length === 0) {
        $.each(v.dataMap, function (m, n) {
            xAxisData.push(m);
        })
    }
    var seriesData = [];
    $.each(v.dataMap, function (m, n) {
        seriesData.push(n);
    });
    series.push(
        {
            name: lineName,
            type: 'bar',
            barWidth: seriesBarWidth,
            barGap: '30',//柱图间距
            itemStyle: {
                normal: {
                    color: color[lineName],
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
        text: '项目进度(m³)',
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
//堆场曲线
function getYardcurve(baseData) {
    var staTime = $(".icon1").text();
    var endTime = $(".icon3").text();
                var dataDetil = baseData.yjEcharts03.result;
                var selected = {};
                var legendData = [];
                var xAxisData = [];
                var series = [];
    
                $.each(dataDetil, function (k, v) {
                    var type = v.lineName;
                    selected["'" + v.lineName + "'"] = true;
                    legendData.push(v.lineName);
                    if (xAxisData.length === 0) {
                        $.each(v.dataMap, function (m, n) {
                            xAxisData.push(m);
                        })
                    }
                    var seriesData = [];
                    $.each(v.dataMap, function (m, n) {
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

                var legendWidth = $('#yjEcharts03').width() - 120;
                var option = {
                    title: {
                        text: '堆场曲线(m³)',
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

//年度累计产量
function getCumulative(baseData) {
    var dataDetil = baseData.yjEcharts04.result;
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

//获取当前时间的前N天
function getBeforeDate(n) {
    var n = n;
    var d = new Date();
    var year = d.getFullYear();
    var mon = d.getMonth() + 1;
    var day = d.getDate();
    var s;
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
            // TimeRefresh();
        }
    } else if (width <= 992) {
        if (fontSize != 16) {
            fontSize = 16;
            // TimeRefresh();
        }
    } else if (width <= 1200) {

        if (fontSize != 20) {
            fontSize = 20;
            // TimeRefresh();
        }

    } else if (width <= 2000) {
        if (fontSize != 22) {
            fontSize = 22;
            // TimeRefresh();
        }

    } else if (width <= 3000) {
        if (fontSize != 25) {
            fontSize = 25;
            // TimeRefresh();
        }
    } else if (width <= 4000) {
        if (fontSize != 28) {
            fontSize = 28;
            // TimeRefresh();
        }

    } else if (width > 4000) {
        if (fontSize != 30) {
            fontSize = 30;
            // TimeRefresh();
        }

    }

});

    }
  }
}
</script>

<style lang="scss" scoped>
  .wrap{
    width: 100%;
    height:100vh;
    background:url('~@/assets/img/chart-bg.jpg') no-repeat;
    background-size: 100% 100%;
    overflow:hidden;
    .chart-box {
      min-height: 200px;
      width: 100%;
    }
  }
  :root {
    font-size: 16px;
}
* {
    padding: 0;
    margin: 0;
    user-select: none;
    list-style: none;
}

#bigdatabox {
    width: 100%;
    height: 100vh;
    /* background: red; */
    margin: 0 auto;
    /*background: url("../img/bg1.png") no-repeat;*/
    background-size: 100% 100%;
}

#bigbox {
    width: 100%;
    height: 100vh;
    position: relative;
    overflow: hidden;
    /* background: red; */
}

/* 切换样式 */
.btns {
    position: absolute;
    left: 9.7%;
    top: 10%;
    width: 22%;
    height: 4%;
}

.btns input {
    width: 25.6%;
    height: 100%;
    border: 0;
    cursor: pointer;
    background: url("~@/assets/img/bg2.png") no-repeat;
    font-weight: initial;
    text-align: center;
    font-size: 0.9rem;
    color: #26d3da;
    background-size: 100% 100%;
}

.btns .current {
    /* background-color: gold;*/
    background: url("~@/assets/img/bg4.png") no-repeat;
    color: #002358;
    background-size: 100% 100%;
}

.planz {
    position: absolute;
    top: 12%;
    left: 4.3%;
    font-size: 1rem;
    font-weight: 600;
    color: #26d3da;
}

.planbox {
    width: 31%;
    height: 18%;
    /* background: red; */
    position: absolute;
    top: 15%;
    left: 2%;
}

.planbox table {
    text-align: center;
    margin: 2% auto 0;
    border-color: #2b327a;
    color: #29d4db;
    font-size: 1rem;
    padding: 4px;
}

/* 计划完成率样式 */
.planlv {
    width: 15.2%;
    height: 30.6%;
    position: relative;
     /*background: gray;*/
    left: 2%;
    top: 33.5%;
}

.planlv .planlz {
    position: absolute;
    font-size: 1rem;
    color: #26d3da;
    font-weight: 600;
    left: 7%;
    top: 5%;
    /* background: green; */
}

#Echarts01 {
    width: 99%;
    height: 80%;
     /*background: red;*/
     /*margin: 0 auto;*/
    position: absolute;
    top: 19%;
    left: 0%;
}

/* 模台使用率样式 */
.motai {
    width: 15.2%;
    height: 30.6%;
    position: relative;
    /* background: red; */
    left: 17.8%;
    top: 2.6%;
}

.motai .planlz1 {
    position: absolute;
    font-size: 1rem;
    color: #26d3da;
    font-weight: 600;
    left: 7%;
    top: 5%;
    /* background: green; */
}

#Echarts02 {
    width: 99%;
    height: 80%;
    /* background: gray; */
    /* margin: 0 auto; */
    position: absolute;
    top: 19%;
    left: 0%;
}

/* 质量模块样式开始 */
.qualitybox {
    width: 31%;
    height: 29.8%;
    position: relative;
    top: 5.2%;
    left: 2%;
     /*background: red;*/
    border-bottom-left-radius: 21.7%;
    display:flex;
    justify-content:center;
    align-items:center;
    /* border-image-slice: 21.7%; */
}
.qualitybox_title{
  position:absolute;
  top:5%;
  left:4%;
  font-size: 1rem;
  color: #29d4db;
  font-weight: 600;
}
.quality1 {
    width: 25%;
    height: 100px;
}


.quality1 ul li:nth-child(1), .quality1 ul li:nth-child(3) {
    margin: 10% 0 5% 15%;
    font-size: 1rem;
    color: #29d4db;
    font-weight: 600;
}

.quality1 ul li:nth-child(2), .quality1 ul li:nth-child(4) {
    margin: 10% 0 5% 15%;
    font-size: .8rem;
    color: #29d4db;
    font-weight: 600;
}

.quality2 {
    width: 150px;
    height: 150px;
}

.quality2 .Img {
    width: 100%;
    height: 100%;
    // margin-top: 3%;
    background: url("~@/assets/img/bg7.jpg") no-repeat;
    background-size: 100% 100%;
}

.quality3 {
    width: 25%;
    height: 100px;
}
.quality3 ul li:nth-child(1), .quality3 ul li:nth-child(3) {
    margin: 10% 0 5% 15%;
    font-size: 1rem;
    color: #29d4db;
    font-weight: 600;
}

.quality3 ul li:nth-child(2), .quality3 ul li:nth-child(4) {
    margin: 10% 0 5% 15%;
    font-size: .8rem;
    color: #29d4db;
    font-weight: 600;
}

/* 产线产量样式 */
.yieldbox {
    position: absolute;
    top: 14.2%;
    left: 34.2%;
    width: 48.2%;
    height: 26.9%;
    /* background: green; */
    border-radius: 9px;
}

#yjEcharts01 {
    width: 100%;
    height: 100%;
    /*background: gray;*/
    border-radius: 9px;
}

/* 项目进度样式 */
.projectbox {
    position: absolute;
    top: 42.5%;
    left: 34%;
    width: 48.3%;
    height: 22.7%;
    /* background: gray; */
}

#yjEcharts02 {
    width: 100%;
    height: 100%;
    /* background: gray; */
    border-radius: 10px;
}

/* 堆场样式 */
.yardbox {
    position: relative;
    top: -24.1%;
    left: 34%;
    width: 48.3%;
    height: 29.7%;
    /* background: greenyellow; */
}

.yardbox .contqi {
    position: relative;
    width: 50%;
    height: 12%;
    /* background: red; */
    /* top: 0.5%; */
    left: 15%;
    z-index: 10;
    background: url('~@/assets/img/bg9.png') no-repeat;
    background-size: 100% 100%;
}

.yardbox > .contqi > .choose_li {
    display: flex;
    align-items: center;
    position: relative;
    width: 40%;
    height: 70%;
    top: 12%;
    left: 8%;
    /* background: red; */
    /* border-bottom-left-radius: 30px; */
    cursor: pointer;
    font-size: 1rem;
    color: #35ffff;
    /* line-height: 100%; */
    /* text-align: center; */
}

.yardbox > .contqi > .choose_li1 {
    display: flex;
    align-items: center;
    position: absolute;
    width: 40%;
    height: 70%;
    top: 12%;
    left: 56%;
    /* background: red; */
    border-top-right-radius: 30px;
    cursor: pointer;
    font-size: 1rem;
    color: #35ffff;
}

#yjEcharts03 {
    width: 100%;
    height: 100%;
    /* background: gray; */
    border-radius: 10px;
    position: absolute;
    top: 0;
}

.annual_output {
    position: absolute;
    width: 11%;
    height: 5%;
    /* background: red; */
    top: 12%;
    right: 4.8%;
    border-radius: 9px;
    text-align: center;
    font-size: 1rem;
    color: #26d3da;
    vertical-align: middle;
    font-weight: 600;
    /* line-height: 100%; */
}

/* 年度累计产量样式 */
.yearl {
    position: absolute;
    top: 17.8%;
    right: 2%;
    width: 14.7%;
    height: 31.3%;
    /* background: gray; */
}

#yjEcharts04 {
    width: 100%;
    height: 100%;
    /* background: red; */
    border-radius: 10px;
}

/* 构件样式 */
.component_quantity {
    position: absolute;
    top: 51%;
    right: 2%;
    width: 14.7%;
    height: 22.4%;
    /* background: gray; */
}

.quantityzi {
    width: 100%;
    height: 13%;
    /* background: red; */
    text-align: center;
    font-size: 1rem;
    font-weight: 600;
    color: #35ffff;
    padding-top: 5px;
    padding-bottom: 2px;
}

.quantityImg {
    position: relative;
    width: 70%;
    height: 75%;
    /* background: forestgreen; */
    background: url("~@/assets/img/bg11.png") no-repeat;
    background-size: 100% 100%;
    margin: 0 auto;
}

.quantityshuju {
    position: absolute;
    width: 35%;
    height: 30px;
    line-height: 30px;
    /* background: red; */
    top: 38%;
    left: 33%;
}

.quantityshuju p:nth-child(1) {
    text-align: center;
    font-size: 0.8rem;
    color: #35ffff;
    font-weight: 600;
}

.quantityshuju p:nth-child(2) {
    text-align: center;
    font-size: 1.2rem;
    color: #35ffff;
    /* font-weight: 600; */
}

/* 日历样式 */
.calendar1 {
    position: relative;
    width: 25.5%;
    height: 3.2%;
    /* background: red; */
    top: -109.8%;
    left: 41.5%;
    /*border-radius: 20px;*/
    background: url('~@/assets/img/bg9.png') no-repeat;
    background-size: 100% 100%;
}

.calendar1 .chose_li {
    position: relative;
    width: 50%;
    height: 100%;
    /* background: gray; */
    border-bottom-left-radius: 20px;
    cursor: pointer;
    font-size: 1rem;
    color: #35ffff;
    float: left;
}

.calendar1 .chose_li .icon1 {
    padding-left: 8%;
    display: flex;
    align-items: center;
    position: absolute;
    width: 95%;
    height: 100%;
    left: 5%;
    /* background: green; */
    border-bottom-left-radius: 20px;
    z-index: 10;
}

.calendar1 .chose_li .icon1 input {
    background: #121356;
    width: 100%;
    height: 90%;
    border: 0;
    border-bottom-left-radius: 15px;
}

.calendar1 .chose_li .icon2 {
    right: 8%;
    position: absolute;
    width: 20%;
    height: 90%;
    /* background: gold; */
    /*background: url("../img/bg5.png") no-repeat;*/
    /*background-size: 100% 100%;*/
}

.calendar1 .chose_li1 {
    display: flex;
    align-items: center;
    position: absolute;
    width: 45%;
    height: 100%;
     /*background: gray;*/
    border-top-left-radius: 20px;
    cursor: pointer;
    font-size: 1rem;
    color: #35ffff;
    float: left;
    margin-left: 55%;
}

.calendar1 .chose_li1 .icon {
    display: flex;
    align-items: center;
    position: absolute;
    width: 95%;
    height: 100%;
    left: 5%;
    /* background: green; */
    border-top-right-radius: 20px;
    z-index: 10;
}
.calendar1 .chose_li1 .icon3 {
    height: 100%;
    width: 95%;
    display: flex;
    align-items: center;
}
.calendar1 .chose_li1 .icon3 input {
    background: #121356;
    width: 100%;
    height: 90%;
    border: 0;
    border-top-right-radius: 15px;
    /* padding-left: 10px; */
}

.calendar1 .chose_li1 .icon4 {
    right: 8%;
    pointer-events: none;
    position: absolute;
    /*right: 5.5%;*/
    width: 20%;
    height: 90%;
    /*background: url("../img/bg5.png") no-repeat;*/
    /*background-size: 100% 100%;*/
}

.calendar1 .zi {
    float: left;
    font-size: 1rem;
    color: #35ffff;
}

/* 库存样式开始 */
.cunku {
    position: relative;
    top: -45.6%;
    right: -83.3%;
    width: 14.7%;
    height: 22.4%;
}
.cunkuImg {
    width: 70%;
    height: 75%;
    margin: 0 auto;
    background: url("~@/assets/img/bg11.png") no-repeat;
    background-size: 100% 100%;
}

.cunkushuju {
    position: absolute;
    width: 35%;
    text-align:center;
    height: 30px;
    line-height:30px;
    /* background: red; */
    top: 50%;
    left: 35%;
    margin-top:-10px;
}

.cunkushuju p:nth-child(1) {
    text-align: center;
    font-size: 0.8rem;
    color: #35ffff;
    font-weight: 600;
}

.cunkushuju p:nth-child(2) {
    text-align: center;
    font-size: 0.8rem;
    color: #35ffff;
    /* font-weight: 600; */
}

/* 天气预报 */
.forecast {
    position: absolute;
    top: 3%;
    right: 4.5%;
    width: 24.5%;
    height: 7.9%;
    font-size: 14px;
    color: #35ffff;
    /* background: red; */
}

#fancybox-frame1 {

}

.forecast1 {
    font-size: 14px;
    color: #35ffff;
}

.select-data {
    font-size: 1.1rem;
    position: absolute;
    width: 60px;
    height: 30px;
    /*background: #131048FF;*/
    top: 9.5%;
    left: 67%;
    /*text-align: center;*/
    line-height: 34px;
    /*border-radius: 4px;*/
    color: #35ffff;
    cursor: pointer;
}

.selected-data {
    position: absolute;
    width: 60px;
    height: 30px;
    /*background: #131048FF;*/
    bottom: 29.5%;
    left: 66.4%;
    z-index: 99999;
    /*text-align: center;*/
    line-height: 30px;
    /*border-radius: 4px;*/
    color: #35ffff;
    cursor: pointer;
}
/*时间框*/
.yjSpan {
    position: absolute;
    display: block;
    width: 15%;
    height: 5%;
    right: 14.8%;
    text-align: center;
    /*line-height: 5%;*/
    top:5%;
    color: #3af9ff;
    font-size: 1.4rem;
    font-weight: 700;
    font-family: unset;
    white-space: nowrap;

}

.btns {
    position: absolute;
    left: 9.7%;
    top: 10%;
    width:22%;
    height: 4%;
    overflow-x: auto;
    overflow-y: hidden;
    white-space: nowrap;
    float: left;
    scroll:hidden;
}
.btns::-webkit-scrollbar {
    /*opacity: 0;*/
    /*width: 100%;*/
    /*display: block;*/
    /*background: transparent;*/
    /*height: 100%;*/
    /*z-index: ;*/
}
#lineNoDiv {
    overflow: hidden;
    transform: skewX(30deg);
    position: relative;
}
.line-box {
    width: 100%;
    height: 100%;
    transform: skewX(-30deg);
    position: absolute;
    left: 0;
    bottom: 0;
}
.cunku .quantityzi {
    padding-top: 9px;
}
.wrap-title{
    position: absolute;
    width: 500px;
    top:3%;
    left: 50%;
    margin-left:-250px;
    font-size: 1.5rem;
    color: #26d3da;
    font-weight: 600;
    text-align: center;
}





/*rem 自适应*/
@media screen and (max-width: 768px) {
    :root {
        font-size: 8px;
    }
}
@media screen and (min-width: 768px) {
    :root {
        font-size: 10px;
    }
}
@media screen and (min-width: 992px) {
    :root {
        font-size: 12px;
    }
}
@media screen and (min-width: 1200px) {
    :root {
        font-size: 14px;
    }
}
@media screen and (min-width: 2000px) {
    :root {
        font-size: 16px;
    }
}
@media screen and (min-width: 3000px) {
    :root {
        font-size: 18px;
    }
}
@media screen and (min-width: 4000px) {
    :root {
        font-size: 18px;
    }
}


</style>
<style>
.jedatefont {font-family:"jedatefont" !important;font-style:normal;-webkit-font-smoothing: antialiased;-moz-osx-font-smoothing: grayscale;}
.jedate{height:auto; font-family:'PingFangSC-Light','PingFang SC','Segoe UI','Lucida Grande','NotoSansHans-Light','Microsoft YaHei', '\5FAE\8F6F\96C5\9ED1', STHeiti, 'WenQuanYi Micro Hei', SimSun, sans-serif;font-size:12px; cursor:default;margin: 0;padding: 0;overflow: hidden;position: relative;border-radius:4px;display: inline-block;border: 1px solid #e2e2e2;box-shadow: 0 1px 6px rgba(0,0,0,.15);background-color:#fff;}
.jedate *{margin: 0;padding: 0;list-style-type:none;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-style:normal;font-family:'PingFangSC-Light','PingFang SC','Segoe UI','Lucida Grande','NotoSansHans-Light','Microsoft YaHei', '\5FAE\8F6F\96C5\9ED1', STHeiti, 'WenQuanYi Micro Hei', SimSun, sans-serif;}

.jedate table thead,.jedate table td{border: 1px #fff solid;}
.jedate ul,.jedate ol,.jedate li,.jedate dl{list-style-type:none;font-style:normal;font-weight: 300;}

.jedate .yearprev{left:0;font-size: 12px;}
.jedate .monthprev{left:25px;font-size: 12px;}
.jedate .yearnext{right:0;font-size: 12px;}
.jedate .monthnext{right:25px;font-size: 12px;}
.jedate .jedate-tips{position: absolute; top: 40%; left: 50%;z-index: 800; width: 200px; margin-left: -100px; line-height: 20px; padding: 15px; text-align: center; font-size: 12px; color: #ff0000;background-color: #FFFEF4;border: 1px rgb(247, 206, 57) solid;display: none;}
.jedate .timecontent ul::-webkit-scrollbar,.jedate-menu::-webkit-scrollbar{height:6px;width:6px;margin-right:5px;background-color: #f5f5f5;transition:all 0.3s ease-in-out;border-radius:0px}
.jedate .timecontent ul::-webkit-scrollbar-track,.jedate-menu::-webkit-scrollbar-track { -webkit-border-radius: 0px;border-radius: 0px;}
.jedate .timecontent ul::-webkit-scrollbar-thumb,.jedate-menu::-webkit-scrollbar-thumb{-webkit-border-radius: 0px;border-radius: 0px;background: rgba(0,0,0,0.5); }
.jedate .timecontent ul::-webkit-scrollbar-thumb:hover,.jedate-menu::-webkit-scrollbar-thumb:hover{background:rgba(0,0,0,0.6)}
.jedate .timecontent ul::-webkit-scrollbar-thumb:active,.jedate-menu::-webkit-scrollbar-thumb:active{background:rgba(0,0,0,0.8)}
.jedate .timecontent ul::-webkit-scrollbar-thumb:window-inactive,.jedate-menu::-webkit-scrollbar-thumb:window-inactive {background: rgba(0,0,0,0.4);}
.jedate .jedate-hmsmask{width:100%;display: block;background-color: rgba(0,0,0,.7);background-color:#fff;position: absolute;top: 0;left:0;right:0;bottom: 36px;z-index: 100;}
.jedatetipscon{color:#333; float:left; overflow:hidden;background-color: #FFFEF4; line-height:22px;padding:6px;border: 1px rgb(247, 206, 57) solid;font-style:normal;font-family: Arial, "\5b8b\4f53", 'sans-serif';font-size:12px;font-weight: 300;}
.jedatetipscon p{padding: 0;margin: 0;font-size:12px;}
.jedatetipscon p.red{color: #ff0000;}

.jedate.leftmenu{padding-left: 90px;}
.jedate .jedate-menu{width:90px;position: absolute;top: 0;left:0;bottom: 0;z-index: 10;background: #f2f2f2;border-right: 1px solid #efefef;border-radius: 4px 0 0 4px;overflow:auto;display: block;padding:4px 0;}
.jedate .jedate-menu p{height: 30px;line-height: 30px;padding-left: 8px;overflow:hidden;font-size: 12px;cursor: pointer;}
.jedate .jedate-menu p:hover{background-color: #00A680;color:#FFFFFF;}
.jedate .jedate-wrap{min-width:230px;background: #fff;overflow: hidden;}
.jedate .jedate-pane{width: 230px;float: left;overflow: hidden;}
.jedate .jedate-header{width:100%;height:36px;line-height: 36px;float: left;background-color: #f2f2f2;text-align: center;font-size: 14px;padding: 0 50px;position: relative;}
.jedate .jedate-header em{width:25px;height:36px;line-height: 36px;position:absolute;color: #666;top:0;background-repeat: no-repeat;background-position: center center;cursor: pointer;}
.jedate .jedate-header .ymbtn{padding: 8px;border-radius: 4px;cursor:pointer;font-size: 14px;}
/* .jedate .jedate-header em:hover,.jedate .jedate-header .ymbtn:hover{color: #00A680;} */
.jedate .jedate-content{width:100%;height: 210px;float: left;padding: 3px;overflow: hidden;}
.jedate .jedate-content.bordge{border-left: 1px #e9e9e9 solid;}
.jedate .jedate-content .yeartable,.jedate .jedate-content .monthtable{width: 100%;border-collapse: collapse;border-spacing: 0;border: 1px solid #fff;}
.jedate .jedate-content .yeartable td,.jedate .jedate-content .monthtable td{width:73px;height: 51px;line-height: 51px;text-align:center; position:relative; overflow:hidden;font-size: 14px;}
.jedate .jedate-content .yeartable td span,.jedate .jedate-content .monthtable td span{padding: 8px 10px;border: 1px solid #fff;}
.jedate .jedate-content .yeartable td.action span,.jedate .jedate-content .monthtable td.action span,
.jedate .jedate-content .yeartable td.action span:hover,.jedate .jedate-content .monthtable td.action span:hover{background-color:#00A680;border:1px #00A680 solid;color:#fff;}
.jedate .jedate-content .yeartable td span:hover,.jedate .jedate-content .monthtable td span:hover{background-color:#f2f2f2;border: 1px #f2f2f2 solid;}
.jedate .jedate-content .yeartable td.disabled span,.jedate .jedate-content .monthtable td.disabled span,
.jedate .jedate-content .yeartable td.disabled span:hover,.jedate .jedate-content .monthtable td.disabled span:hover{color:#bbb;background-color:#fff;border: 1px solid #fff;}
.jedate .jedate-content .yeartable td.contain span,.jedate .jedate-content .monthtable td.contain span,
.jedate .jedate-content .yeartable td.contain span:hover,.jedate .jedate-content .monthtable td.contain span:hover{background-color: #D0F0E3;border:1px #D0F0E3 solid;}

.jedate.grid .daystable thead,.jedate.grid .daystable td{border: 1px #f2f2f2 solid;}
.jedate .jedate-content .daystable{width: 100%;border-collapse: collapse;border-spacing: 0;border: 1px solid #fff;}
.jedate .jedate-content .daystable thead{background-color:#fff;}
.jedate .jedate-content .daystable th{width:31px;height:27px;  text-align:center; position:relative; overflow:hidden;font-size: 12px;font-weight: 400;}
.jedate .jedate-content .daystable td{width:31px;height:30px;  text-align:center; position:relative; overflow:hidden;font-size: 14px;font-family: Arial, "\5b8b\4f53", 'sans-serif';}
.jedate .jedate-content .daystable td .nolunar{line-height:29px;font-size: 14px;font-family: Arial, "\5b8b\4f53", 'sans-serif';}
.jedate .jedate-content .daystable td .solar{height:14px;line-height:14px;font-size: 14px;padding-top: 2px;display: block;font-family: Arial, "\5b8b\4f53", 'sans-serif';}
.jedate .jedate-content .daystable td .lunar{height:15px;line-height:15px;font-size: 12px;overflow:hidden;display: block;font-family: Arial, "\5b8b\4f53", 'sans-serif';color: #888;transform: scale(.95);}
.jedate .jedate-content .daystable td.action,.jedate .jedate-content .daystable td.action:hover,
.jedate .jedate-content .daystable td.action .lunar{background-color: #00A680;color:#fff;}
.jedate .jedate-content .daystable td.other,.jedate .jedate-content .daystable td.other .nolunar,.jedate .jedate-content .daystable td.other .lunar{color:#00DDAA;}
.jedate .jedate-content .daystable td.disabled,.jedate .jedate-content .daystable td.disabled .nolunar,.jedate .jedate-content .daystable td.disabled .lunar{ color:#bbb;}
.jedate .jedate-content .daystable td.contain,.jedate .jedate-content .daystable td.contain:hover{background-color: #00DDAA;color:#fff;}
.jedate .jedate-content .daystable td.disabled:hover{background-color:#fff;}
.jedate .jedate-content .daystable td:hover{background-color:#f2f2f2;}
.jedate .jedate-content .daystable td.red{ color:#ff0000;}
.jedate .jedate-content .daystable td .marks{ width:5px; height:5px; background-color:#ff0000; -webkit-border-radius:50%;border-radius:50%; position:absolute; right:2px; top:4px;}
.jedate .jedate-content .daystable td.action .marks{ width:5px; height:5px; background-color:#fff; -webkit-border-radius:50%;border-radius:50%; position:absolute; right:2px; top:4px;}

.jedate .jedate-time{overflow: hidden;padding-bottom: 4px; background-color:#fff;position: absolute;top:0;right: 0;z-index: 150;}
.jedate .jedate-time .timepane{width:230px;float:left;}
.jedate .jedate-time .timeheader{width: 100%;float:left;height: 36px;line-height: 36px;background-color: #f2f2f2;text-align: center;font-size: 14px;position: relative;}
.jedate .jedate-time .timecontent{width: 100%;float:left;}
.jedate .jedate-time .hmstitle{width: 211px;margin: 0 auto;overflow: hidden;padding-top: 4px;text-align: center;}
.jedate .jedate-time .hmstitle p{width: 33.33%;float:left;height: 30px;line-height: 30px;font-size: 13px;}
.jedate .jedate-time .hmslist{width: 211px;margin: 0 auto 6px auto;border: 1px solid #ddd;border-right: none;overflow: hidden;}
.jedate .jedate-time .hmslist .hmsauto{height: 100%;margin: 0;text-align: center;}
.jedate .jedate-time .hmslist ul {width: 70px;height: 174px;float: left;border-right: 1px solid #ddd;overflow: hidden;}
.jedate .jedate-time .hmslist .hmsauto:hover ul {overflow-y: auto}
.jedate .jedate-time .hmslist ul li {width: 130%;padding-left:26px;text-align: left;height: 25px;line-height: 25px;font-size: 14px;font-family: Arial, "\5b8b\4f53", 'sans-serif';}
.jedate .jedate-time .hmslist ul li:hover{background-color: #F2F2F2;}
.jedate .jedate-time .hmslist ul li.action,.jedate-time .hmslist ul li.action:hover{background-color: #00A680;color:#fff;}
.jedate .jedate-time .hmslist ul li.disabled{ background-color: #fbfbfb;color:#ccc;}
.jedate .jedate-time .hmslist ul li.disabled.action{ background-color: #00A680;color:#FFFFFF;filter:Alpha(opacity=30);opacity:.3; }

.jedate .jedate-footbtn{height: 33px;padding: 0 3px;border-top: 1px #e9e9e9 solid;overflow: hidden;}
.jedate .jedate-footbtn .timecon{line-height:28px;padding:0 5px;background-color:#00A680;color:#fff;display:block;float: left;font-size: 12px;margin-top:4px;border-radius:4px;overflow: hidden;}
.jedate .jedate-footbtn .btnscon{line-height:28px;margin-top:4px;display:block;float: right;font-size: 12px;border-radius:4px;overflow: hidden;}
.jedate .jedate-footbtn .btnscon span{float:left; padding:0 5px;border-right: 1px #fff solid;background-color:#00A680;color:#fff;display:block;height:28px;line-height:28px;text-align:center;overflow:hidden;}
.jedate .jedate-footbtn .btnscon span:last-child{border-right:none;}



.line-box,.calendar1{
  display:none;
}
</style>