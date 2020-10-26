<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="formInline"
                                class="demo-form-inline"
                                size="mini"
                        >
                            <el-form-item>
                                <el-select
                                        v-model="formInline.projectId"
                                        placeholder="项目名称"
                                        clearable
                                >
                                    <el-option
                                            v-for="item in projectSelect"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="endTime">
                                <el-date-picker
                                        v-model="formInline.endTime"
                                        type="date"
                                        placeholder="日期"
                                        value-format="yyyy-MM-dd"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model="searchButtonData"></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <my-button-group :model="buttonData" style="display: none"></my-button-group>
                    </el-col>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                    :data="datas"
                    size="mini"
                    stripe
                    border
                    style="width: 100%"
                >
                    <el-table-column
                            type="index"
                            :index="indexMethod"
                            label=""
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="planProjectDuration"
                            label="工期"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productVal"
                            label="方量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="avgProductVal"
                            label="平均每日方量"
                            width="180"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="avgProjectId"
                            label="平均楼层工期"
                    >
                    </el-table-column>
                </el-table>
                <el-col :span="8">
                    <div id="projectYield" class="chart-box"></div>
                </el-col>
            </el-main>


        </el-container>

    </div>
</template>

<script>
    import prodcutTrack from '../../../../model/project/productTrack'
    import projectModel from "../../../../model/project/project";
    import clientModel from "../../../../model/client-model";
    import {Notification} from "element-ui";
    import echarts from "echarts";
    import numeral from "numeral";

    export default {
        data() {
            return {
                datas: [],
                listAble:[],
                charts:[],
                selected: [],
                expands: [],
                subDatas: [],
                formInline: {
                    projectId: '',
                    endTime: '',
                },
                projectSelect: [],
                buildings: [],
                floors: [],
                buttonData:[
                    {
                        name:'新建'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "onSubmit"
                    }
                ],
                rowButtonData: [],
                projectYield: '',
                projectYieldOption1:{
                  legend: {},
                  tooltip: {
                      formatter: params=>{
                        let index = params.componentIndex+1
                        let key = params.dimensionNames[index]
                        return `${key}:${params.data[key]}%`
                      }
                  },
                  dataset: {
                      dimensions: ['product', '工期', '方量', '楼层'],
                      source: [
                          {product: '完成比率', '工期': 43.3, '方量': 85.8, '楼层': 93.7},
                      ]
                  },
                  xAxis: {
                      type: 'value',
                      axisLabel: {
                          formatter: '{value}%'
                      }
                  },
                  yAxis: {type: 'category'},
                  series: [
                      {type: 'bar'},
                      {type: 'bar'},
                      {type: 'bar'}
                  ]
                }
            }
        },
        mounted() {
            this.getProjects().then(_=>{
              if(this.projectSelect.length){
                this.formInline.projectId = this.projectSelect[0].id
                this.onSubmit()
              }
            });
        },
        methods: {
            //表格左侧的头部信息
            indexMethod(index) {
                if(index == 0){
                    return '计划';
                }else if(index == 1){
                    return '实际';
                }else if (index == 2){
                    return '完成比率';
                }else{
                    return '';
                }
            },
            /**
             * 增加修改product为项目下拉框赋值
             */
            getProjects() {
                return projectModel.projectListS().then((data) => {
                    this.projectSelect = data.entity
                })
            },
            onSubmit() {
                if(!this.formInline.projectId){
                  Notification({
                      message: '请先选择项目',
                      type: 'warning'
                  })
                  return 
                }
                this.projectYield = echarts.init(document.getElementById('projectYield'));
                window.addEventListener('resize', () => {
                    this.projectYield.resize()
                })
                prodcutTrack.findProjectActual(this.formInline).then(data => {
                    this.datas = data.entity.tables;
                    let echartData = data.entity.eacharts;
                    this.datas[2] = this.Clone(echartData);

                    this.datas[2].planProjectDuration = (this.datas[2].planProjectDuration || 0) + '%'
                    this.datas[2].productVal = ((this.datas[2].productVal>100?'100':this.datas[2].productVal) || 0) + '%'
                    this.datas[2].floorNum = ((this.datas[2].floorNum>100?'100':this.datas[2].floorNum )|| 0) + '%'
                    this.datas[2].avgProductVal = ((this.datas[2].avgProductVal>100?'100':this.datas[2].avgProductVal) || 0) + '%'
                    this.datas[2].avgProjectId = ((this.datas[2].avgProjectId>100?'100':this.datas[2].avgProjectId )|| 0) + '%'

                    this.projectYieldOption1.dataset.source[0].工期 = echartData.planProjectDuration||0;
                    this.projectYieldOption1.dataset.source[0].方量 = echartData.floorNum;
                    this.projectYieldOption1.dataset.source[0].楼层 = echartData.productVal;
                    this.projectYield.setOption(this.projectYieldOption1, true)
                })
            }
        }
    }
</script>

<style scoped>
    .chart-box {
        min-height: 300px;
        width: 200%;
    }
</style>