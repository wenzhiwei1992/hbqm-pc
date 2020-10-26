<template>
  <div class="daily-indicators">
    <el-card class="chart-card">
      <div class="header" slot="header">
        <div class="header-left">
          <span class="chart-title">堆场库存曲线</span>
          <el-select v-model="stockId" placeholder="请选择" @change="getLineChartData">
            <el-option
              label="全部"
              value="">
            </el-option>
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.stockNo"
              :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="header-right">
          <el-radio v-model="radioLineChartData" label="vol" @change="getLineChartData">显示方量（m³）</el-radio>
          <el-radio v-model="radioLineChartData" label="num" @change="getLineChartData">显示数量（片）</el-radio>
          <ul class="circle-box">
            <li v-for="(item,index) of circle" :key="index">
              <span class="circle" :style="`background:${item.colorCode}`"></span>
              <span class="text">{{item.name}}</span>
            </li>
          </ul>
        </div>
      </div>
      <div id="yardInventoryLineChart" class="chart-box" style="height:300px;"></div>
      <div class="footer">
        <div class="text">* 系统在每天23:55分统计当天的库存情况，报表支持最长时间段为最近 180 天。</div>
      </div>
    </el-card>
    <el-card class="chart-card">
      <div class="header" slot="header">
        <div class="header-left">
          <span class="chart-title">实时库存状况</span>
        </div>
        <div class="header-right">
          <el-radio v-model="radioTableData" label="vol" @change="getTableData">显示方量（m³）</el-radio>
          <el-radio v-model="radioTableData" label="num" @change="getTableData">显示数量（片）</el-radio>
        </div>
      </div>
      <el-table :data="tableData[radioTableData]" style="font-size:14px;width: 100%;" border>
        <el-table-column label="库区" prop="warehouse"></el-table-column>
        <el-table-column v-for="(item,index) of tableLabel" :key="index" :label="item.label" :prop="item.prop" align="right" :render-header="renderHeader">
          <template slot-scope="scope">
            <div v-if="radioTableData==='vol'">{{scope.row[item.prop]}} m³</div>
            <div v-else-if="radioTableData==='num'">{{scope.row[item.prop]}} 片</div>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <div class="total">总计：{{tableTotal[radioTableData]}}</div>
      </div>
    </el-card>
  </div>
</template>

<script>
import echarts from 'echarts'
import { dateFormat, dateOperation } from '@/util/date.js'
import homeModel from '@/model/charts/homeChart.js'
import stockModel from '@/model/basicdata/stock.js'

export default {
  data(){
    return{
      radioLineChartData:"num",
      radioTableData:"num",
      options: [],
      stockId: '',
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
        },
        {
          name:'报废品',
          color:'black',
          colorCode:'#2a2b3c'
        }
      ],
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
                  name: '全部',
                  type: 'line',
                  stack: '总量1',
                  data: [],
                  areaStyle: {},
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
                  name: '良品',
                  type: 'line',
                  stack: '总量2',
                  data: [],
                  areaStyle: {},
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
                  name: '不良品',
                  type: 'line',
                  stack: '总量3',
                  data: [],
                  areaStyle: {},
                  itemStyle:{
                    normal:{
                        color:'#f44336',
                        lineStyle:{ 
                          color:'#f44336' //改变折线颜色
                        } 
                    },
                  },
              },
              {
                  name: '报废品',
                  type: 'line',
                  stack: '总量4',
                  data: [],
                  areaStyle: {},
                  itemStyle:{
                    normal:{
                        color:'#303133',
                        lineStyle:{ 
                          color:'#303133' //改变折线颜色
                        } 
                    },
                  },
              }
          ]
      },
      yardStockData:{
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
        ],
        scrap:[
          {
            num:1,
            vol:1,
            date:'08-10'
          },
          {
            num:2,
            vol:2,
            date:'08-11'
          },
          {
            num:3,
            vol:3,
            date:'08-12'
          },
          {
            num:4,
            vol:4,
            date:'08-13'
          },
          {
            num:5,
            vol:5,
            date:'08-14'
          },
          {
            num:6,
            vol:6,
            date:'08-15'
          }
        ]
      },
      tableLabel:[
        {
          label:'不良品',
          prop:'ungoodProduct'
        },
        {
          label:'良品',
          prop:'goodProduct'
        },
        {
          label:'报废品',
          prop:'scrap'
        },
        {
          label:'总库存',
          prop:'all'
        }
      ],
      tableData:{
        num:[],
        vol:[]
      },
      date:{
        startTime:dateOperation('YYYY-mm-dd',-180),
        endTime:dateOperation('YYYY-mm-dd',0)
      },
      tableTotal:{
        num:'',
        vol:''
      }
    }
  },
  mounted(){
    this.init()
  },
  methods:{
    init(){
      this.getOptionSelect()
      this.drawLine()
      this.getTableData()
    },
    // 时间选择
    selectTime(index){
      this.customTime = ""
      this.timeBar.forEach(option => {
        option.active = false
      });
      this.timeBar[index].active = true
    },
    // 自定义时间 change
    customTimeActive(){
      this.timeBar.forEach(option => {
        option.active = false
      });
    },
    drawLine(){
      this.lineChart = echarts.init(document.getElementById('yardInventoryLineChart'))
      window.addEventListener('resize', () => {
        this.lineChart.resize()
      })
      this.getLineChartData();
    },
    getLineChartData(){
      let obj = {
        stockId:this.stockId,
        startTime:this.date.startTime,
        endTime:this.date.endTime
      }
      homeModel.findCyDsiData(obj).then(data=>{
        // log(data)
        this.yardStockData.all = data.entity.all
        this.yardStockData.goodProduct = data.entity.goodProduct
        this.yardStockData.ungoodProduct = data.entity.ungoodProduct
        this.yardStockData.scrap = data.entity.scrap
        Object.keys(this.yardStockData).forEach((keyName,keyNameIndex)=>{
          this.yardStockData[keyName].forEach((value,valueIndex)=>{
            this.lineChartData.series[keyNameIndex].data[valueIndex] = [value.date,value[this.radioLineChartData]]
          })
        })
        this.lineChart.setOption(this.lineChartData, true)
      })
      
    },
    getTableData(){
      let obj = {
        stockId:"",
        startTime:dateOperation('YYYY-mm-dd',0),
        endTime:dateOperation('YYYY-mm-dd',0)
      }
      homeModel.findCyDsiDetail(obj).then(data=>{
        log(data.entity)
        this.tableData.num = []
        this.tableData.vol = []
        if(data.entity.data&&data.entity.data.length){
          data.entity.data.forEach((option,index)=>{
            // 数量赋值
            let num = {
              warehouse:option.stockNo,
              all:option.allNum,
              goodProduct:option.goodNum,
              ungoodProduct:option.unGoodNum,
              scrap:option.scrapNum
            }
            this.tableData.num.push(num)
            // 方量赋值
            let vol = {
              warehouse:option.stockNo,
              all:option.allVol,
              goodProduct:option.goodVol,
              ungoodProduct:option.unGoodVol,
              scrap:option.scrapVol
            }
            this.tableData.vol.push(vol)
          })
          // 总计赋值
          this.tableTotal.num = `${data.entity.totalNum}片`
          this.tableTotal.vol = `${data.entity.totalVol}m³`
        }
      })
    },
    // 自定义表头
    renderHeader(h, { column, $index }){
      let label
      switch(column.label){
        case "不良品":{
          label = '<span class="circle red"></span>'
          break
        }
        case "良品":{
          label = '<span class="circle green"></span>'
          break
        }
        case "报废品":{
          label = '<span class="circle black"></span>'
          break
        }
        case "总库存":{
          label = '<span class="circle blue"></span>'
          break
        }
      }
      return h('div', {
                  attrs: {class: 'cell'},
                  domProps: {innerHTML: label+'<span style="vertical-align: middle;">'+column.label+'</span>'}
               })
    },
    // 获取下拉框选项
    getOptionSelect(){
      return stockModel.getDataByParam({}).then(data=>{
        this.options = data.entity 
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.daily-indicators{
  .chart-card{
    margin-top:24px;
    .header{
      display:flex;
      align-items: center;
      justify-content: space-between;
      .header-left{
        display:flex;
        .chart-title{
          display:flex;
          align-items: center;
          margin-right:20px;
          font-size:18px;
          font-weight:500;
        }
      }
      .header-right{
        // display:flex;
        .circle-box{
          float:right;
          display:flex;
          justify-content: center;
          align-items:center;
          li{
            span{
              display:inline-block;
            }
            .text{
              color:#999;
              font-size:12px;
            }
            .circle{
              // 写在下面 穿透
            }
          }
        }
      }
    }
    .chart-box{
      height:300px;
    }
    .footer{
      margin-top:20px;
      border-top:1px solid hsla(210,8%,51%,.13);
      .text{
        margin-top:20px;
        color:#999;
        font-size:12px;
      }
    }
    .table-footer{
      background: #fafafa;
      border:1px solid #EBEEF5;
      border-top:0px;
      padding:15px;
      .total{
        height:40px;
        line-height:40px;
        text-align:right;
        font-size:16px;
        font-weight:bold;
        margin-right:50px;
      }
    }
  }
  /deep/.red{
    background-color: #f44336;
  }
  /deep/.green{
    background-color: #00cb00;
  }
  /deep/.blue{
    background-color: #178de8;
  }
  /deep/.black{
    background-color: #2a2b3c;
  }
  /deep/.circle{
    display:inline-block;
    vertical-align: middle;
    width: 5px;
    height: 5px;
    border-radius:50%;
    margin:0 5px 0 20px;
  }
  /deep/.el-table .cell{
    padding: 5px 10px;
  }
}
</style>