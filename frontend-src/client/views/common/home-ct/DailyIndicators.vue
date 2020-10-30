<template>
  <div class="daily-indicators">
    <!-- 时间选项 -->
    <div class="time-bar">
      <ul class="time-option-box">
        <li class="time-option" :class="item.active?'time-active':''" v-for="(item,index) of timeBar" :key="index" @click="selectTime(index)">{{item.name}}</li>
        <li class="time-option custom-time">
          <el-popover
            placement="bottom"
            trigger="hover">
            <div class="block">
              <el-date-picker
                v-model="customTime"
                type="daterange"
                value-format="yyyy-MM-dd"
                unlink-panels
                @change="customTimeActive"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions">
              </el-date-picker>
            </div>
            <div style="color:#2196f3;" slot="reference" v-if="customTime&&customTime.length">{{customTime[0]}} ~ {{customTime[1]}}</div>
            <div slot="reference" v-else>自定义时间</div>
          </el-popover>
        </li>
      </ul>
    </div>
    <!-- 选项卡(浇筑量/入库量/发货量) -->
    <div class="dashboard">
      <ul class="dashboard-option-box">
        <li class="dashboard-option" :class="item.active?'dashboard-active':''" v-for="(item,index) of dashboard" :key="index" @click="selectDashboard(index)">
          <div class="dashboard-option-title">{{item.name}}</div>
          <div class="dashboard-option-value">
            <span class="vol">{{item.vol}}</span>
            <span class="vol-unit">m³</span>
            <span class="num">({{item.num}}片)</span>
          </div>
          <div class="dashboard-chart-box" :class="item.index">{{item.index}}</div>
          <div class="dashboard-chart-padding" :class="item.class"></div>
          <div class="dashboard-detail" @click="openDashboardDetail(item.detailsData)">
            <span class="text">数据概况</span>
            <i class="icon el-icon-warning-outline"></i>
          </div>
        </li>
      </ul>
    </div>
    <!-- 折线图 -->
    <el-card class="chart-card">
      <div slot="header">
        <span class="chart-title">{{chartTitle.name}}曲线</span>
        <el-radio v-model="radio" label="vol" @change="getLineChartData">显示方量（m³）</el-radio>
        <el-radio v-model="radio" label="num" @change="getLineChartData">显示数量（片）</el-radio>
        <ul class="circle-box" v-show="chartTitle.index==='warehousingData'">
          <li v-for="(item,index) of dashboard.warehousingData.circle" :key="index">
            <span class="circle" :style="`background:${item.colorCode}`"></span>
            <span class="text">{{item.name}}</span>
          </li>
        </ul>
      </div>
      <div id="dailyIndicatorsLineChart" class="chart-box" style="height:300px;"></div>
    </el-card>
    <!-- 数据概况 -->
    <el-dialog
      :title="`${date.startTime} 至 ${date.endTime} ${chartTitle.name}项目数据概况`"
      :visible.sync="dashboardDetailShow"
      :lock-scroll="false"
      width="45%">
      <el-table :data="tableData" :show-header="false" style="font-size:14px;width: 100%;" border>
        <el-table-column label="项目"  prop="projectName"></el-table-column>
        <el-table-column label="数量/方量">
          <template slot-scope="scope">
            <div>
              {{scope.row.num}}片/{{scope.row.vol}}m³
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    
  </div>
</template>

<script>
import echarts from 'echarts'
import { dateFormat, dateOperation } from '@/util/date.js'
import homeModel from '@/model/charts/homeChart.js'

export default {
  data(){
    let self = this
    return{
      timeBar:[
        {
          name:'今日',
          startTime:0,
          endTime:0,
          active:false
        },
        {
          name:'昨日',
          startTime:-1,
          endTime:-1,
          active:false
        },
        {
          name:'近7日',
          startTime:-6,
          endTime:0,
          active:true
        },
        {
          name:'近30日',
          startTime:-29,
          endTime:0,
          active:false
        },
        {
          name:'全年',
          startTime:-364,
          endTime:0,
          active:false
        }
      ],
      customTime:[],
      dashboard:{
        pouringData:{
          index:'pouringData',
          name:'浇筑量',
          num:100,
          vol:100,
          backgroundColor:'#329afb',
          class:'dashboard-chart-padding-green',
          active:true,
          circle:[
            {
              name:'浇筑量',
              color:'blue',
              colorCode:'#178de8'
            }
          ],
        },
        warehousingData:{
          index:'warehousingData',
          name:'入库量',
          num:100,
          vol:100,
          backgroundColor:'#6f7be1',
          class:'dashboard-chart-padding-grey',
          active:false,
          circle:[
            {
              name:'全部',
              color:'blue',
              colorCode:'#178de8'
            },
            {
              name:'良品',
              color:'green',
              colorCode:'#00cb00'
            },
            {
              name:'不良品',
              color:'red',
              colorCode:'#f44336'
            }
          ],
        },
        deliveringData:{
          index:'deliveringData',
          name:'发货量',
          num:100,
          vol:100,
          backgroundColor:'#2ea99f',
          class:'dashboard-chart-padding-blue',
          active:false,
          circle:[
            {
              name:'发货量',
              color:'blue',
              colorCode:'#178de8'
            }
          ],
        }
      },
      radio:"vol",
      chartTitle:{},
      lineChart:'',
      lineChartData:{
          tooltip: {
              trigger: 'axis'
          },
          grid: {
              left: '3%',
              right: '4%',
              bottom: '15%',
              top:'3%',
              containLabel: true
          },
          xAxis: {
              type: 'category',
              boundaryGap: false,
          },
          yAxis: {
              type: 'value',
              splitLine :{    //网格线
                  lineStyle:{
                      type:'dashed'    //设置网格线类型 dotted：虚线   solid:实线
                  }
              }

          },
          dataZoom: [
            {
                type: 'slider',
                start:92,
                end:100
            }
          ],
          series: [
              {
                  name: '浇筑量',
                  type: 'line',
                  stack: '总量1',
                  data: [],
                  itemStyle:{
                    normal:{
                        color:'#178de8',
                        lineStyle:{ 
                          color:'#178de8' //改变折线颜色
                        } 
                    },
                  },
              },
              {
                  name: '入库量',
                  type: 'line',
                  stack: '总量2',
                  data: [],
                  itemStyle:{
                    normal:{
                        color:'#00cb00',
                        lineStyle:{ 
                          color:'#00cb00' //改变折线颜色
                        } 
                    },
                  },
              },
              {
                  name: '发货量',
                  type: 'line',
                  stack: '总量3',
                  data: [],
                  itemStyle:{
                    normal:{
                        color:'#f44336',
                        lineStyle:{ 
                          color:'#f44336' //改变折线颜色
                        } 
                    },
                  },
              }
          ]
      },
      pouringData:{
        all:[
          {
            num:122,
            vol:612.23,
            date:'08-10'
          },
          {
            num:155,
            vol:512.23,
            date:'08-11'
          },
          {
            num:166,
            vol:412.23,
            date:'08-12'
          },
          {
            num:123,
            vol:312.23,
            date:'08-13'
          },
          {
            num:100,
            vol:12.23,
            date:'08-14'
          },
          {
            num:119,
            vol:212.23,
            date:'08-15'
          }
        ]
      },
      warehousingData:{
          all:[
            {
              num:121,
              vol:11.23,
              date:'08-10'
            },
            {
              num:551,
              vol:121.23,
              date:'08-11'
            },
            {
              num:661,
              vol:212.23,
              date:'08-12'
            },
            {
              num:133,
              vol:312.23,
              date:'08-13'
            },
            {
              num:210,
              vol:412.23,
              date:'08-14'
            },
            {
              num:191,
              vol:512.23,
              date:'08-15'
            }
          ],
          goodProduct:[
            {
              num:122,
              vol:111.23,
              date:'08-10'
            },
            {
              num:515,
              vol:121.23,
              date:'08-11'
            },
            {
              num:616,
              vol:202.23,
              date:'08-12'
            },
            {
              num:153,
              vol:1312.23,
              date:'08-13'
            },
            {
              num:300,
              vol:212.23,
              date:'08-14'
            },
            {
              num:129,
              vol:112.23,
              date:'08-15'
            }
          ],
          ungoodProduct:[
            {
              num:312,
              vol:43.23,
              date:'08-10'
            },
            {
              num:255,
              vol:191.23,
              date:'08-11'
            },
            {
              num:366,
              vol:612.23,
              date:'08-12'
            },
            {
              num:423,
              vol:112.23,
              date:'08-13'
            },
            {
              num:100,
              vol:212.23,
              date:'08-14'
            },
            {
              num:59,
              vol:312.23,
              date:'08-15'
            }
          ]
      },
      deliveringData:{
        all:[
          {
            num:121,
            vol:10.23,
            date:'08-10'
          },
          {
            num:551,
            vol:9.23,
            date:'08-11'
          },
          {
            num:661,
            vol:8.23,
            date:'08-12'
          },
          {
            num:123,
            vol:7.23,
            date:'08-13'
          },
          {
            num:20,
            vol:6.23,
            date:'08-14'
          },
          {
            num:191,
            vol:5.23,
            date:'08-15'
          }
        ]
      },
      detailsData:{},
      date:{
        startTime:dateOperation('YYYY-mm-dd',-7),
        endTime:dateOperation('YYYY-mm-dd',0)
      },
      dashboardChart:{
        pouringData:{
          tooltip: {
              trigger: 'axis',
              axisPointer:{
                type:'none'
              },
              textStyle:{
                fontSize: 10
              },
              formatter:function(params){
                params[0].marker = params[0].marker.replace('border-radius:10px;width:10px;height:10px','border-radius:8px;width:8px;height:8px')
                return params[0].marker+''+params[0].data[0] +': '+ params[0].data[1]
              }
          },
          grid: {
              left: '0%',
              right: '0%',
              bottom: '0%',
              top:'9%',
              containLabel: true
          },
          xAxis: {
              type: 'category',
              boundaryGap: false,
              show:false,
              axisLabel: {
                  show: false
              }
          },
          yAxis: {
              show:false,
              type: 'value',
              splitLine: {
                  show: false
          　　 },
              axisLabel: {
                  show: false
              }
          },
          series: [
              {
                  name: '浇筑量',
                  type: 'line',
                  stack: '总量1',
                  data: [],
                  smooth: true,
                  symbolSize:1,
                  areaStyle: {
                    color:'#b5efc3'
                  },
                  itemStyle:{
                    normal:{
                        color:'#1fca3e',
                        lineStyle:{ 
                          color:'#1fca3e' //改变折线颜色
                        } 
                    },
                  },
              }
          ]
        },
        warehousingData:{
            tooltip: {
                trigger: 'axis',
                axisPointer:{
                  type:'none'
                },
                textStyle:{
                  fontSize: 10
                },
                formatter:function(params){
                  params[0].marker = params[0].marker.replace('border-radius:10px;width:10px;height:10px','border-radius:8px;width:8px;height:8px')
                  return params[0].marker+''+params[0].data[0] +': '+ params[0].data[1]
                }
            },
            grid: {
                left: '0%',
                right: '0%',
                bottom: '0%',
                top:'9%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                show:false,
                axisLabel: {
                    show: false
                }
            },
            yAxis: {
                show:false,
                type: 'value',
                splitLine: {
                    show: false
            　　 },
                axisLabel: {
                    show: false
                }
            },
            series: [
                {
                    name: '入库量',
                    type: 'line',
                    stack: '总量1',
                    data: [],
                    smooth: true,
                    symbolSize:1,
                    areaStyle: {
                      color:'#c6cad9'
                    },
                    itemStyle:{
                      normal:{
                          color:'#425185',
                          lineStyle:{ 
                            color:'#425185' //改变折线颜色
                          } 
                      },
                    },
                }
            ]
        },
        deliveringData:{
            tooltip: {
                trigger: 'axis',
                axisPointer:{
                  type:'none'
                },
                textStyle:{
                  fontSize: 10
                },
                formatter:function(params){
                  params[0].marker = params[0].marker.replace('border-radius:10px;width:10px;height:10px','border-radius:8px;width:8px;height:8px')
                  return params[0].marker+''+params[0].data[0] +': '+ params[0].data[1]
                }
            },
            grid: {
                left: '0%',
                right: '0%',
                bottom: '0%',
                top:'9%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                show:false,
                axisLabel: {
                    show: false
                }
            },
            yAxis: {
                show:false,
                type: 'value',
                splitLine: {
                    show: false
            　　 },
                axisLabel: {
                    show: false
                }
            },
            series: [
                {
                    name: '发货量',
                    type: 'line',
                    stack: '总量1',
                    data: [],
                    smooth: true,
                    symbolSize:1,
                    areaStyle: {
                      color:'#c2e3fe'
                    },
                    itemStyle:{
                      normal:{
                          color:'#37a2fc',
                          lineStyle:{ 
                            color:'#37a2fc' //改变折线颜色
                          } 
                      },
                    },
                }
            ]
        },
      },
      dashboardDetailShow:false,
      tableData:[
        // {projectName:'',num:'',vol:''}
      ],
      dateRang: [new Date(), ''],
      pickerOptions: {
        disabledDate (time) {
          let startyear = self.dateRang[0].getFullYear() - 1
          let endYear = self.dateRang[0].getFullYear() + 1
 
          let endDate = endYear + '-' + (self.dateRang[0].getMonth() + 1) + '-' +
                 self.dateRang[0].getDate()
          let startDate = startyear + '-' + (self.dateRang[0].getMonth() + 1) + '-' +
                  self.dateRang[0].getDate()
          let _endDate = new Date(endDate)
          let _startDate = new Date(startDate)
          //即大于开始时间小于结束时间
          return time.getTime() > _endDate.getTime() || time.getTime() < 
           _startDate.getTime()
 
                 },
        onPick ({minDate, maxDate}) {
           //重新设置开始时间
          self.dateRang[0] = minDate
          console.log(minDate)
        }
      }
    }
  },
  mounted(){
      this.init()
  },
  methods:{
    init(){
      this.chartTitle = this.dashboard.pouringData
      this.drawLine()
      this.getDashboard()
      this.getDetailsData()
      this.getDashboardChart()
    },
    getDetailsData(){
      homeModel.findPrDailyProjectDetail(this.date).then(data=>{
        Object.keys(this.dashboard).forEach(option=>{
          this.dashboard[option].detailsData = data.entity[option]
        })
      })
    },
    getDashboard(){
      homeModel.findPrDailyTotalData(this.date).then(data=>{
        log('仪表盘',data.entity)
        Object.keys(data.entity).forEach((val,index)=>{
          this.dashboard[val].num = data.entity[val].num
          this.dashboard[val].vol = data.entity[val].vol
        })  
      })
    },
    // 时间选择
    selectTime(index){
      this.customTime = ""
      this.timeBar.forEach(option => {
        option.active = false
      });
      this.timeBar[index].active = true
      this.date.startTime = dateOperation('YYYY-mm-dd',this.timeBar[index].startTime)
      this.date.endTime = dateOperation('YYYY-mm-dd',this.timeBar[index].endTime)
      this.getDashboard()
      this.getDetailsData()
    },
    // 自定义时间 change
    customTimeActive(){
      this.timeBar.forEach(option => {
        option.active = false
      });
      if(this.customTime&&this.customTime.length){
        this.date.startTime = this.customTime[0]
        this.date.endTime = this.customTime[1]
      }else{
        this.date.startTime = ""
        this.date.endTime = ""
      }
      this.getDashboard()
      this.getDetailsData()
    },
    // 仪表盘选择
    selectDashboard(index){
      Object.keys(this.dashboard).forEach((option,i) => {
        this.dashboard[Object.keys(this.dashboard)[i]].active = false
      });
      this.dashboard[index].active = true
      this.chartTitle = this.dashboard[index]
      
      this.getLineChartData();
    },
    drawLine(){
      this.lineChart = echarts.init(document.getElementById('dailyIndicatorsLineChart'))
      window.addEventListener('resize', () => {
        this.lineChart.resize()
      })
      let date = {
        startTime:dateOperation('YYYY-mm-dd',-182),
        endTime:dateOperation('YYYY-mm-dd',0)
      }
      homeModel.findPrDailyData(date).then(data=>{
        log(data.entity)
        this.pouringData.all = data.entity.pouringData.all

        this.warehousingData.all = data.entity.warehousingData.all
        this.warehousingData.goodProduct = data.entity.warehousingData.goodProduct
        this.warehousingData.ungoodProduct = data.entity.warehousingData.ungoodProduct

        this.deliveringData.all = data.entity.deliveringData.all
        this.getLineChartData()
        // this.lineChartData.dataZoom[0].startValue = dateOperation('mm-dd',-14)
      })
    },
    getDashboardChart(){
      this.pouringChart = echarts.init(document.querySelector('.pouringData'))
      this.warehousingChart = echarts.init(document.querySelector('.warehousingData'))
      this.deliveringChart = echarts.init(document.querySelector('.deliveringData'))
      window.addEventListener('resize', () => {
        this.pouringChart.resize()
        this.warehousingChart.resize()
        this.deliveringChart.resize()
      })
      homeModel.findPrDailyData(this.date).then(data=>{
        Object.keys(data.entity).forEach((val,index)=>{
          this.dashboardChart[val].series[0].data = data.entity[val].all.map(option=>{
            return [option.date,option.vol]
          })
        })
        this.pouringChart.setOption(this.dashboardChart.pouringData, true)
        this.warehousingChart.setOption(this.dashboardChart.warehousingData, true)
        this.deliveringChart.setOption(this.dashboardChart.deliveringData, true)
      })
    },
    getLineChartData(){
      let keyNameArr = this.chartTitle.index
      // 图表数据初始化
      this.lineChartData.series = this.lineChartData.series.map(val=>{
        val.data = []
        return val
      })
      this.lineChart.setOption(this.lineChartData, true)
      // 图表数据赋值
      Object.keys(this[keyNameArr]).forEach((keyName,keyNameIndex)=>{
        this[keyNameArr][keyName].forEach((value,valueIndex)=>{
          let arr = [value.date,value[this.radio]]
          this.lineChartData.series[keyNameIndex].data[valueIndex] = arr
          this.lineChartData.series[keyNameIndex].name = this.chartTitle.circle[keyNameIndex].name
        })
      })
      this.lineChart.setOption(this.lineChartData, true)
    },
    openDashboardDetail(detailsData){
      this.tableData = detailsData
      log(this.tableData)
      this.dashboardDetailShow = true
    }
  }
}
</script>

<style lang="scss" scoped>
.daily-indicators{
  .time-bar{
    .time-option-box{
      display:flex;
      justify-content: center;
      .time-option{
        box-sizing:border;
        width:65px;
        text-align: center;
        font-size:14px;
        margin-right:20px;
        height:30px;
        line-height:30px;
      }
      .time-active{
        background:#1890ff;
        font-weight:500;
        color:#fff;
        border-radius:50px;
      }
      .custom-time{
        text-align: left;
        width:auto;
      }
    }
  }
  .dashboard{
    margin-top: 24px;
    .dashboard-option-box{
      width: 100%;
      display: flex;
      justify-content: space-between;
      height: 150px;
      .dashboard-option{
        position:relative;
        height: 100%;
        flex:.315;
        background:#fff;
        color:#000;
        padding:20px;
        display:flex;
        flex-direction:column;
        border:2px solid rgb(240, 242, 245);
        .dashboard-option-title{
          color: rgba(0,0,0,.45);
          font-size: 16px;
        }
        .dashboard-option-value{
          margin-top:20px;
          .vol{
            font-size:30px;
          }
          .vol-unit{
            font-size: 16px;
          }
          .num{
            font-size: 16px;
            margin-left: 20px;
            font-weight: bold;
          }
        }
        .dashboard-chart-box{
          width: 100%;
          height:30%;
          position:absolute;
          bottom:10px;
          left:0;
          // border-top:1px solid #000;
        }
        .dashboard-chart-padding{
          width: 100%;
          height:10px;
          position:absolute;
          bottom:0;
          left:0;
        }
        .dashboard-detail{
          position:absolute;
          right:10px;
          top:15px;
          font-size: 12px;
          color: rgba(0,0,0,.45);
          .text{}
          .icon{}
        }
        .dashboard-chart-padding-green{
          background:#ccf3d6;
        }
        .dashboard-chart-padding-grey{
          background:#d7dae4;
        }
        .dashboard-chart-padding-blue{
          background:#d5ebfd;
        }
      }
      .dashboard-active{
        border-color:#1890ff;
      }
    }
  }
  .chart-card{
    margin-top:24px;
    .chart-title{
      margin-right:56px;
      font-size:18px;
      font-weight:500;
    }
    .circle-box{
      float:right;
      display:flex;
      justify-content: center;
      align-items:center;
      li{
        span{
          vertical-align: middle;
        }
        .text{
          color:#999;
          font-size:12px;
        }
        .circle{
          display:inline-block;
          width: 5px;
          height: 5px;
          border-radius:50%;
          margin:0 5px 0 20px;
        }
        .red{
          background-color: #f44336;
        }
        .green{
          background-color: #00cb00  ;
        }
        .blue{
          background-color: #178de8  ;
        }
      }
    }
    .chart-box{
      height:300px;
    }
  }
}
</style>