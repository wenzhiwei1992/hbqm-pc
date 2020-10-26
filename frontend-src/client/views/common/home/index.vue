<template>
  <!--<div class="mod-home">-->
  <!--<h3>项目介绍</h3>-->
  <!--<ul>-->
  <!--<li>持云智能工厂管理系统</li>-->
  <!--<li>项目管理、生产订单、构件检验、堆场管理</li>-->
  <!--</ul>-->
  <!--<h3>获取帮助</h3>-->
  <!--<ul>-->
  <!--<li>维略信息：<a href="http://www.valuprosys.com/" target="_blank">www.valuprosys.com</a></li>-->
  <!--<li>邮箱:info@valuprosys.com</li>-->
  <!--</ul>-->
  <!--</div>-->
  <div class="mod-demo-echarts">
    <el-row>
      <el-col :span="24">
        <!-- 选项卡 -->
        <div class="tab-bar">
          <div class="tab-title">日期选择:</div>
          <ul class="tab-list">
            <li 
            v-for="(item,index) of tabBarData" 
            :key="index" 
            :class="item.isActive?'is-active':''"
            @click="handleClickTabActive(index)"
            >
              <span>{{item.name}}</span>
            </li>
          </ul>
        </div>
        <!-- 显示状态 -->
        <div class="state">
          <ul class="state-list">
            <li v-for="(item,index) of stateListData" :key="index">
              <div class="title">{{item.typeName}}</div>
              <div class="square">{{item.productVol}}m³</div>
              <div class="square">{{item.projectConcrete}}m³</div>
              <div class="number">{{item.countNum}}片</div>
            </li>
          </ul>
        </div>
      </el-col>
      <!-- <el-col :span="5">
        <div class="fast-nav">
          <div class="title">快捷导航:</div>
          <ul class="fast-list">
            <router-link 
              v-for="(item,index) of fastListData" 
              :key="index" 
              :to="{name:item.url}" 
              tag="li">
                {{item.name}}
            </router-link>
          </ul>
        </div>
      </el-col> -->
    </el-row>
    <el-row :gutter="24" style="margin-top:15px;">
      <el-col :span="16">
        <!-- 柱状图 -->
        <el-form ref="form">
          <el-form-item label="选择日期">
            <el-date-picker
              v-model="staticByDay"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              @change="projectYieldMethod"
              end-placeholder="结束日期"
            >
            </el-date-picker>
          </el-form-item>
        </el-form>
        <div
          id="projectYield"
          class="chart-box"
        ></div>
      </el-col>
      <!-- 饼图 -->
      <el-col :span="8">
        <el-form ref="form">
          <el-form-item label="项目名称">
            <el-select
              v-model="oneProjectPieCondition.projectId"
              @change="oneProjectPieMethod"
            >
              <el-option
                v-for="item in projects"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div
          id="oneProjectPie"
          class="chart-box"
        ></div>
      </el-col>

      <!-- 饼图 -->
      <el-col :span="8">
        <div
          style="margin-top:40px;"
          id="oneProjectPie1"
          class="chart-box"
        ></div>
      </el-col>
      <el-col :span="16">
        <!-- 柱状图 -->
        <el-form ref="form">
          <el-form-item label="选择日期">
            <el-date-picker
              v-model="projectDate1"
              type="month"
              placeholder="选择月"
              value-format="yyyy-MM"
              @change="projectYieldMethod1"
            >
            </el-date-picker>
          </el-form-item>
        </el-form>
        <div
          id="projectYield1"
          class="chart-box"
        ></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
var echarts = require('echarts');
var numeral = require('numeral')
import projectModel from '@/model/project/project'
import clientModel from '@/model/client-model'
import plateModel from '@/model/basicdata/plate'

export default {

  data() {
    return {
      stateListData:[],
      tabBarData:[
        {
          name:'今日',
          day:'0',
          isActive:true
        },
        {
          name:'昨日',
          day:'1',
          isActive:false
        },
        {
          name:'7天',
          day:'7',
          isActive:false
        },
        {
          name:'30天',
          day:'30',
          isActive:false
        },
        {
          name:'90天',
          day:'90',
          isActive:false
        },
        // {
        //   name:'一年',
        //   day:'365',
        //   isActive:false
        // }
      ],
      fastListData:[
        {
          name:'物料编码',
          url:'materialStorageList',
        },
        {
          name:'构件BOM模板',
          url:'',
        },
        {
          name:'楼栋发货进度',
          url:'FloorProductSendInfo',
        },
        {
          name:'楼层形象表',
          url:'planFloor',
        },
      ],
      // 第一个柱状图
      projectYieldOption: {
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} 片",
          // formatter(params){
          //   console.log(params)
          // }
        },
        legend: {
          data: ['邮件营销', '联盟广告', '视频广告']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
          }
        ],
        yAxis: [
          {
            // name: '砼方量',
            type: 'value'
          }
        ],
        series: [
          {
            name: '邮件营销',
            type: 'bar',
            stack: '广告',
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '联盟广告',
            type: 'bar',
            stack: '广告',
            data: [220, 182, 191, 234, 290, 330, 310]
          },
          {
            name: '视频广告',
            type: 'bar',
            stack: '广告',
            data: [150, 232, 201, 154, 190, 330, 410]
          },
        ]
      },
      // 第二个柱状图
      projectYieldOption1: {
        tooltip: {
          trigger: 'item',
          // axisPointer:{
          //   type:'item'
          // },
          formatter: "{a} <br/>{b} : {c} m³",
        },
        legend: {
          data: ['邮件营销', '联盟广告', '视频广告']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {

            type: 'category',
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
          }
        ],
        yAxis: [
          {
            // name: '砼方量',
            type: 'value'
          }
        ],
        series: [
          {
            name: '邮件营销',
            type: 'bar',
            stack: '广告',
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '联盟广告',
            type: 'line',
            stack: '广告',
            data: [220, 182, 191, 234, 290, 330, 310]
          },
        ]
      },
      projectYield: null,//项目产量
      oneProjectPie: null,
      oneProjectPie1: null,
      oneProjectPieCondition: {
        projectId: ''
      },
      oneProjectPie1Condition: {
        projectId: null
      },
      // 第一个饼图
      oneProjectPieOption: {
        tooltip: {
          show: true,//是否打开悬浮提示
          trigger: 'item',
          formatter: "{b} : {c}m³ ({d}%)"
        },
        legend: {
          //orient: '',
          left: 'center',
          data: ['已发货','已入库','未完成']
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['50%', '60%'],
            label: {
              normal: {
                show: true,
                position: 'left',
                formatter(params) {
                  // console.log(params)
                  // return params.name + '\r\n' + params.percent +'%'
                  return params.name + params.percent +'%'
                }
              }
            },
            data: [
              { value: 335, name: '未完成' },
              { value: 310, name: '已入库' },
              { value: 234, name: '已发货' },
            ],
            hoverAnimation: true,//是否开启hover在扇区上的放大效果
            silent: false,//图形是否不响应和触发鼠标事件,默认为false(响应)
          }
        ]
      },
      // 第二个饼图
      oneProjectPie1Option: {
        tooltip: {
          show: true,
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          show: false,
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
      projects: [],
      years: [],
      productStatus: ['', '待入库', '成品入库', '已发货'],
      statistic1Year: null,
      staticByDay: [
        new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() - 7),
        new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate())
      ],
      projectDate: new Date().getFullYear(),
      projectDate1: new Date().getFullYear() + '-' + (new Date().getMonth() + 1),
      headerStatus: [
        {
          'title': '日构件完成量',
          'num': '999',
          iconColorPilot: false,
          icon: 'el-icon-time',
          'totalconcrete': '999'
        },
        {
          'title': '月构件完成量',
          'num': '9999',
          iconColorPilot: false,
          icon: 'el-icon-date',
          'totalconcrete': '999'
        },
        {
          'title': '年构件完成量',
          'num': '99999',
          iconColorPilot: false,
          icon: 'el-icon-more',
          'totalconcrete': '999'
        }
      ]
    }
  },
  mounted() {
    this.getIndexChartInfoByDay(0)
    this.getProjects()
    this.countProductsByOverTimeToMonthAndDay()
    for (var i = 2018; i <= 2050; i++) {
      this.years.push(i)
    }
    //此时加载产线信息
    this.getProdline();
    var now = new Date();
    this.staticByDay[0] = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + (now.getDate() - 7);
    this.staticByDay[1] = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();


    this.projectYield = echarts.init(document.getElementById('projectYield'))
    window.addEventListener('resize', () => {
      this.projectYield.resize()
    })
    this.projectYieldMethod();


    this.oneProjectPie = echarts.init(document.getElementById('oneProjectPie'))
    window.addEventListener('resize', () => {
      this.oneProjectPie.resize()
    })
    this.oneProjectPieMethod();


    this.projectYield1 = echarts.init(document.getElementById('projectYield1'))
    window.addEventListener('resize', () => {
      this.projectYield1.resize()
    })
    this.projectYieldMethod1();


    this.oneProjectPie1 = echarts.init(document.getElementById('oneProjectPie1'))
    window.addEventListener('resize', () => {
      this.oneProjectPie1.resize()
    })
    this.oneProjectPieMethod1();

  },
  methods: {
    handleClickTabActive(index){
      this.tabBarData.map(val=>{
        val.isActive = false
      })
      this.tabBarData[index].isActive = true
      this.getIndexChartInfoByDay(this.tabBarData[index].day)
    },
    getIndexChartInfoByDay(day){
      plateModel.getIndexChartInfoByDay(day).then(data=>{
        // console.log('getIndexChartInfoByDay',data)
        this.stateListData = data.entity
      })
    },
    getProdline() {
      plateModel.getAllProdLines().then(data => {
        this.statisticProdlines = data.entity;
      })
    },
    getProjects() {
      projectModel.getAllProjects().then((data) => {
        let projects = data.entity
        for (var j = 0; j < projects.length; j++) {
          var pName = {
            value: projects[j].id,
            label: projects[j].projectName,
            molds: []
          }
          this.projects.push(pName)
        }
      })
    },


    oneProjectPieMethod() {
      if (this.oneProjectPieCondition.projectId != null) {
        clientModel.ax.get('/echarts/oneProjectPie?projectId=' + this.oneProjectPieCondition.projectId).then((data) => {
          console.log('第一个饼图', data)
          var entity = data.entity;
          let color = ['#ffb822','#43d2fd','#fc6841']
          entity.map((val,index)=>{
            val.itemStyle = {color:color[index]}
          })
          this.oneProjectPieOption.series[0].data = entity;
          this.oneProjectPie.setOption(this.oneProjectPieOption, true)
        }).catch(data => {
          this.oneProjectPie.setOption(this.oneProjectPieOption, true)
        })
      } else {
        this.oneProjectPie.setOption(this.oneProjectPieOption, true)
      }
    },
    oneProjectPieMethod1() {
      clientModel.ax.get('echarts/getStockSpaceInfo').then((data) => {
        console.log('第二个饼图', data)
        var entity = data.entity;

        let color = ['#ff4b50','#f2f2f2']
        entity.map((val,index)=>{
          val.itemStyle = {color:color[index]}
        })
        // entity[0].value:已用
        // entity[1].value:总数(接口数据有问题)
        entity[1].value = entity[1].value - entity[0].value
        
        this.oneProjectPie1Option.series[0].data = entity;
        this.oneProjectPie1.setOption(this.oneProjectPie1Option, true)
      }).catch(data => {
        this.oneProjectPie1.setOption(this.oneProjectPie1Option, true)
      })
    },
    projectYieldMethodFormatter(){
      let that = this
      // 第一个柱状图的黑色浮框
      that.projectYieldOption.tooltip.formatter = function(params){
        let series = that.projectYieldOption.series
        let dataIndex = params.dataIndex
        let seriesIndex = params.seriesIndex
        let seriesName = params.seriesName
        let total = 0
        let num = Number(series[seriesIndex].data[dataIndex])
        series.map(val=>{
          total+=Number(val.data[dataIndex])
        })
        let percentage = that.ToFixed(num/total*100,2) + '%'
        return params.seriesName + ':' + num + 'm³' + '(' + percentage + ')'
      }
    },
    projectYieldMethod1Formatter(){
      let that = this
      // 第二个柱状图的黑色浮框
      that.projectYieldOption1.tooltip.formatter = function(params){
        let series = that.projectYieldOption1.series
        let dataIndex = params.dataIndex
        let seriesIndex = params.seriesIndex
        let seriesName = params.seriesName
        let data = Number(series[seriesIndex].data[dataIndex])
        let dataNum = Number(series[seriesIndex].dataNum[dataIndex])
        return params.seriesName + ':' + data + 'm³('+ dataNum +'片)'
      }
    },
    projectYieldMethod() {
      if (this.staticByDay) {
        let staticByDay = this.staticByDay;
        let startDate = staticByDay[0];
        let endDate = staticByDay[1];
        clientModel.ax.get('/statistic/getInspectionInfoByDay?startDates=' + startDate + '&endDates=' + endDate)
          .then((data) => {
            console.log('第一个柱状图', data)
            var entity = data.entity;
            let color = ['#fc6841','#ffb822','#43d2fd']
            entity.series.map((val,index)=>{
              val.itemStyle = {color:color[index]}
              val.stack = '1'
            })

            this.projectYieldMethodFormatter()

            this.projectYieldOption.xAxis[0].data = entity.xAxisData;
            this.projectYieldOption.legend.data = entity.legendData;
            this.projectYieldOption.series = entity.series;
            this.projectYield.setOption(this.projectYieldOption, true)

          }).catch(data => {
            this.projectYield.setOption(this.projectYieldOption, true)
          })
      }

    },
    projectYieldMethod1() {
      if (this.projectDate1) {
        clientModel.ax.get('/statistic/getRecordInfoByMonth?month=' + this.projectDate1)
          .then((data) => {
            console.log('第二个柱状图', this.projectDate1, data)
            var entity = data.entity;
            let color = ['#ffb822','#fc6841','#43d2fd']
            entity.series.map((val,index) => {
              val.itemStyle = {color:color[index]}
              val.smooth = true
              if (val.name == '库存结余') {
                val.type = 'bar'
              }
            })

            this.projectYieldMethod1Formatter()
            
            this.projectYieldOption1.xAxis[0].data = entity.xAxisData;
            this.projectYieldOption1.legend.data = entity.legendData;
            this.projectYieldOption1.series = entity.series;
            this.projectYield1.setOption(this.projectYieldOption1, true)
          }).catch(data => {
            this.projectYield1.setOption(this.projectYieldOption1, true)
          })
      }
    },
    countProductsByOverTimeToMonthAndDay() {
      projectModel.countProductDetailsByOverTimeMonthAndDay().then((data) => {
        this.headerStatus[0].num = data.entity.day
        this.headerStatus[1].num = data.entity.month
        this.headerStatus[2].num = data.entity.year
        this.headerStatus[0].totalconcrete = data.entity.sumday
        this.headerStatus[1].totalconcrete = data.entity.summonth
        this.headerStatus[2].totalconcrete = data.entity.sumyear
        this.headerStatus.forEach(val => {
          val.num = parseFloat(val.num).toLocaleString()
        })
      })
    },
    numeralS(num) {
      return numeral(num).format('0.000')
    },
  }
}
</script>

<style lang="scss" scoped>
$mainColor: #3e8ef7;
$matchColor: #fff;
$contentColor: #000;
.tab-bar{
  display: flex;
  color:#606266;
  font-size:14px;
  .tab-title{}
  .tab-list{
    display: flex;
    li{
      position: relative;
      margin-left:20px;
      width: 2.5em;
      cursor: pointer;
      span{
        display: block;
        text-align: center;
        width: 100%;
        height: 100%;
      }
    }
    .is-active{
      color:#000;
      font-weight:bold;
      span:after{
        content:'';
        position: absolute;
        bottom:-5px;
        left:0;
        width: 100%;
        height: 2px;
        background:#6ccfc4;
      }
    }
    
  }
}
.state{
  margin-top: 30px;
  .state-list{
    display: flex;
    color:#606266;
    font-size:14px;
    li{
      border-radius:2px;
      border: 1px solid #f4f4f4;
      flex: .2;
      box-sizing:border-box;
      padding:10px;
      padding-bottom: 8%;
      margin-left:10px;
      &:first-child{
        margin-left:0;
      }
      &:nth-child(1){
        background:url(~@/assets/img/1.jpg) center 0/100% no-repeat;
      }
      &:nth-child(2){
        background:url(~@/assets/img/2.jpg) center 0/100% no-repeat;
      }
      &:nth-child(3){
        background:url(~@/assets/img/3.jpg) center 0/100% no-repeat;
      }
      &:nth-child(4){
        background:url(~@/assets/img/4.jpg) center 0/100% no-repeat;
      }
      &:nth-child(5){
        background:url(~@/assets/img/5.jpg) center 0/100% no-repeat;
      }
      div{
        margin-left:12px;
      }
      .title{}
      .square{
        font-size:20px;
        font-weight:bold;
        color:#000;
      }
      .number{
        font-size:12px;
      }
    }
  }
}
.fast-nav{
  box-sizing: border-box;
  padding:10px;
  border: 1px solid #ccc;
  border-radius: 2px;
  background:#bbe0e3;
  margin-left:10px;
  .title{
    font-size:20px;
  }
  .fast-list{
    li{
      margin:10px 0;
      font-size:16px;
      text-indent: 2em;
    }
  }
}
.mod-home {
  line-height: 1.5;
}
.mod-demo-echarts {
  > .el-alert {
    margin-bottom: 10px;
  }
  > .el-row {
    margin-top: -10px;
    margin-bottom: -10px;

    .el-col {
      padding-top: 10px;
      padding-bottom: 10px;
    }
  }

  .chart-box {
    min-height: 200px;
    width: 100%;
  }
}
#J_chartBarBoxByDay {
  margin-top: -0.8px;
}
.header-status {
  padding-bottom: 30px;
  .wrap {
    display: flex;
    justify-content: space-around;
    align-items: center;
    .icon {
      padding: 10px;
      background: $matchColor;
      color: $mainColor;
      border-radius: 5px;
      transition-property: background, color;
      transition-duration: 0.5s, 0.5s;
      &.active {
        background: $mainColor;
        color: $matchColor;
      }
      i {
        font-size: 70px;
      }
    }
    .title {
      color: rgba(0, 0, 0, 0.45);
      font-weight: bold;
      font-size: 16px;
    }
    .text {
      font-size: 20px;
      text-align: center;
      overflow: hidden;
      color: $contentColor;
      font-weight: bold;
      div {
        margin-top: 15px;
        &:first-child {
          margin-top: 0;
        }
      }
    }
  }
}
</style>

