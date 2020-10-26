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
                            <el-form-item prop="shipments">
                                <el-select
                                        clearable
                                        v-model="formInline.listType1"
                                        style="width: 150px"
                                        placeholder="质检状态"
                                >
                                    <el-option
                                            v-for="item in optionsStatus"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="dates">
                                <el-date-picker
                                        v-model="formInline.dates"
                                        type="daterange"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        range-separator="至"
                                        start-placeholder="开始时间"
                                        end-placeholder="结束时间"
                                        :default-time="['00:00:00', '23:59:59']">
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
                    :style="`height:0px`"
            ></el-main>

            <div class="yieldbox" v-show="showyjEcharts01">
                <div class="chart-box" id="yjEcharts01"></div>
            </div>
            </br>
            </br>
            </br>
            <div class="yieldbox" v-show="showyjEcharts02">
                <div class="chart-box" id="yjEcharts02"></div>
            </div>

        </el-container>


    </div>
</template>

<script>
    import prodcutTrack from '../../../model/project/productTrack'
    import clientModel from "../../../model/client-model";
    import {Notification} from "element-ui";
    import echarts from "echarts";
    import $ from 'jquery'


    export default {
        data() {
            return {
                charts: [],
                fontSize: 16,
                formInline: {
                    dates: [],
                },
                buttonData: [
                    {
                        name: '新建'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "onSubmit"
                    }
                ],
                rowButtonData: [],
                optionsStatus: [
                    {
                        value: '隐检',
                        label: '隐检'
                    },
                    {
                        value: '成品检',
                        label: '成品检'
                    },
                ],
                showyjEcharts01:true,
                showyjEcharts02:true,
            }
        },
        mounted() {
            this.onSubmit()
        },
        methods: {
            /**
             * 增加修改product为项目下拉框赋值
             */
            onSubmit() {
                let query = this.formInline;
                query.listType = '隐检'
                prodcutTrack.getCheckErrorEchart(query).then(data => {
                    if(this.formInline.listType1 === '隐检'){
                        this.showyjEcharts01 =  true;
                        this.showyjEcharts02 =  false;
                    }else if(this.formInline.listType1 === '成品检'){
                        this.showyjEcharts01 =  false;
                        this.showyjEcharts02 =  true;
                    }else{
                        this.showyjEcharts01 =  true;
                        this.showyjEcharts02 =  true;
                    }

                    if(this.formInline.listType1 !== '成品检'){
                        this.getLineYield(data.entity,'yjEcharts01','隐蔽工程检验缺陷分布图')
                    }

                    if(this.formInline.listType1 !== '隐检'){
                        let query2 = this.formInline;
                        query2.listType = '成品检'
                        prodcutTrack.getCheckErrorEchart(query2).then(data => {
                            this.getLineYield(data.entity,'yjEcharts02','成品检验缺陷分布图')
                        })
                    }
                })
            },

            getLineYield(baseData,formId,titleName) {
                var dataDetil = baseData;
                var xAxisData = [];
                var series = [];
                var seriesData = [];

                let num = 0;
                $.each(dataDetil, function (k, v) {
                    if(num%2==0){
                        xAxisData.push(k);
                    }else{
                        xAxisData.push(k);
                        //xAxisData.push(" \n "+" \n "+k);
                    }
                    seriesData.push(v);
                   num++;
                });

                series.push({
                    name: '总计',
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

                // 产线产量数据开始
                var myChart3 = echarts.init(document.getElementById(formId));
                //指定图标的配置和数据
                var option3 = {
                    title: {
                        text: titleName,
                        left: 'center',
                        // textStyle: {
                        //     color: '#34ebf1',
                        //     fontSize: this.fontSize,
                        //     y: "center"
                        // },
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
                myChart3.setOption(option3);
                $(window).on('resize', function () {
                    myChart3.resize();
                });
                // getYardcurve();
            }
        }
    }


    $(window).resize(function () {
        var fontSize = 16;
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
            }

        }
        this.fontSize = fontSize;

    });

</script>

<style scoped>
    .chart-box {
        min-height: 300px;
        width: 100%;
    }
</style>