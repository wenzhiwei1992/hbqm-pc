<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true">
                            <el-form-item prop="projectId">
                                <el-select v-model="floorProductDetailQuery.projectId"
                                           placeholder="项目名称"
                                           style="width:150px"
                                >
                                    <el-option
                                            v-for="item in arrProject"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model='buttonData' style="display:none;"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <div class="cont clearfix" sthle="height:100%;" v-show="Object.keys(project).length">
                    <div class="frm">
                        <div class="marg-box">
                            <div class="frm_marg">
                                <div class="frm_marg_title">已发货量占比</div>
                                <div class="echar" id="chart" style="margin-top: 6px;"></div>
                            </div>
                            <ul class="dashboard">
                                <li>
                                    <div class="icon-wrap">
                                        <i class="el-icon-tickets"></i>
                                    </div>
                                    <div class="text-wrap">
                                        <div class="title">总需求量</div>
                                        <div class="num">{{project.prodCount}}块</div>
                                        <div class="cube-m">{{project.prodCountVol}}m³</div>
                                    </div>
                                </li>
                                <li>
                                    <div class="icon-wrap">
                                        <i class="el-icon-box"></i>
                                    </div>
                                    <div class="text-wrap">
                                        <div class="title">总在库量</div>
                                        <div class="num">{{project.stockProdCount}}块</div>
                                        <div class="cube-m">{{project.stockProdCountVol}}m³</div>
                                    </div>
                                </li>
                                <li>
                                    <div class="icon-wrap">
                                        <i class="el-icon-truck"></i>
                                    </div>
                                    <div class="text-wrap">
                                        <div class="title">已发货量</div>
                                        <div class="num">{{project.sendProdCount}}块</div>
                                        <div class="cube-m">{{project.sendProdCountVol}}m³</div>
                                    </div>
                                </li>
                                <li>
                                    <div class="icon-wrap">
                                        <i class="el-icon-s-promotion"></i>
                                    </div>
                                    <div class="text-wrap">
                                        <div class="title">待发货量</div>
                                        <div class="num">{{project.waitProdCount}}块</div>
                                        <div class="cube-m">{{project.waitProdCountVol}}m³</div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        
                        <div class="frm_con">
                            <div class="frm_birg clearfix" id="houseType"></div>
                        </div>
                    </div>
                </div>

            </el-main>
        </el-container>


    </div>
</template>

<script>
    var echarts = require('echarts');
    import $ from 'jquery'
    import buildingModel from "../../../model/project/building";
    import projectModel from '../../../model/project/project'


    export default {
        name: "ProjectProductSendInfo",
        data() {
            return {
                arrBuilding: [],
                arrProject: [],
                total: '',
                buildCode: '',
                listData: [],
                floorProductDetailQuery: {
                    buildingId: '',
                    productTypeIds: [],
                    typeDesc: '发货',
                    projectId: '',
                },
                checkList: [],
                productNameOptions: [],
                buildingId: '',
                projectId: '',
                checkAll: false,
                buttonData: [
                    {
                        name: '新建',
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: 'click'
                    },
                ],
                project:{}
            }
        },
        created() {
            this.getProjects()
        },
        methods: {
            //初始化户型 楼栋  楼层构建
            initAddHouseTypeDiv(houseTypeList, buildMap, floorMap) {
                var projectId = this.floorProductDetailQuery.projectId;
                var _houseTypeDiv = $("#houseType");
                _houseTypeDiv.html(""); //先清空
                var html = [];

                if (!houseTypeList || houseTypeList.length === 0) {
                    return false;
                }
                for (var i = 0; i < houseTypeList.length; i++) {
                    var houseType = houseTypeList[i];
                    var buildList = buildMap[houseType.typeId + ''];

                    if (buildList.length != 0) {
                        html.push("<div class='frm_birg_o clearfix' >");
                        html.push("     <div class='frm_birg_o_tit'>");
                        html.push("         <p class='quantity_one' style='font-weight: bold'>" + houseType.houseType + "<i>" + "共" + buildList.length + "栋" + "</i></p>");
                        html.push("     </div>");
                        html.push("     <ul class='frm_birg_o_list'>");
                    }
                    if (buildList.length != 0) {
                        for (var k = 0; k < buildList.length; k++) {
                            var build = buildList[k];
                            var floorList = floorMap[build.buildId + ''];
                            html.push("<li class='frm_birg_o_cen'>");
                            if (floorList.length != 0) {
                                html.push('<ul class="frm_birg_o_firs">');
                                for (var j = 0; j < floorList.length; j++) {
                                    var floor = floorList[j];

                                    if (floor.floor == 0) {
                                        continue;
                                    }
                                    html.push("<li>");
                                    html.push("    <p class='frm_birg_o_firs_txt' style='margin-right: 5px;'>" + floor.floor + "F" + "</p>");
                                    html.push("    <div class='frm_birg_o_firs_zpt'>");
                                    // html.push("        <div class='frm_birg_o_firs_box' style='width: " + (floor.sendQty / floor.needQty) * 100 + "%;'>" + (floor.sendQty) + "</div>");
                                    html.push("        <div class='frm_birg_o_firs_box' style='width: " + (floor.sendQty / floor.needQty) * 100 + "%;'></div>");
                                    html.push(`<div class='num-box'>${floor.sendQty}/${(floor.needQty - floor.sendQty)}</div>`)
                                    if ((floor.needQty - floor.sendQty) == 0) {
                                        html.push("");
                                    } else {
                                        // html.push("    <div class='frm_birg_o_firs_boxs' style='width:" + (100 - (floor.sendQty / floor.needQty) * 100) + "%;'>" + (floor.needQty - floor.sendQty) + "</div>");
                                        html.push("    <div class='frm_birg_o_firs_boxs' style='width:" + (100 - (floor.sendQty / floor.needQty) * 100) + "%;'></div>");
                                    }
                                    html.push("     </div>");
                                    html.push("</li>")

                                }
                                html.push("</ul>");

                                html.push("<p class='frm_birg_o_cen_stairs'>" +
                                    // "<a href='#' style='text-decoration:underline' "
                                  //  + "onclick='buildNoClick(\"" + houseType.houseType + "\",\"" + build.buildNo + "\", \"" + projectId + "\",\"" + houseType.typeId + "\"," + build.buildId + ")'"
                                    // +">"
                                    // + build.buildCode + "</a>"
                                    build.buildCode
                                    + "</p>");
                                html.push('</li>');
                            }
                        }
                    }
                    html.push("</ul>");
                    html.push("</div>");
                }
                _houseTypeDiv.html(html.join(""));
            },

            //初始化追加Div层方法
            // initAddPorjectDiv(project) {
            //     var arr = [];
            //     var _projectName = $("<h2 class='frm_msg_tit'>" +
            //         project.projectName + "</h2>");
            //     var cubage = (project.prodCountVol / project.prodCount);   //体积

            //     //1.总需求量
            //     var descTotal = "<table><tr><td class=' quantity_one' style='margin-left: 0;font-weight: bold;'>"
            //         + "总需求量   " + "</td>"
            //         + "<td class=' blu' style='text-align: right;width:85px;'>" + project.prodCount + " 块" + "</td>"
            //         + "<td class=' blu' style='text-align: right;width:135px;'>" + project.prodCountVol + " m³" + "</td></tr>";
            //     //2.总在库量
            //     var stockTotal = "<tr><td class=' quantity_tow1' style='margin-left: 0;font-weight: bold;'>"
            //         + "总在库量   " + "</td>"
            //         + "<td class=' gren' style='color: #990000;text-align: right;width:85px;'>" + project.stockProdCount + " 块" + "</td>"
            //         + "<td class=' gren' style='color: #990000;text-align: right;width:135px;'>" + project.stockProdCountVol + " m³" + "</td></tr>";
            //     //3.已发货量
            //     var descDelivered = "<tr><td class=' quantity_tow' style='margin-left: 0;font-weight: bold;'>"
            //         + "已发货量   " + "</td>"
            //         + "<td class=' gren' style='text-align: right;width:85px;'>" + project.sendProdCount + " 块" + "</td>"
            //         + "<td class=' gren' style='text-align: right;width:135px;'>" + project.sendProdCountVol + " m³" + "</td></tr>";

            //     //4.待发货量
            //     var descShipping = "<tr><td class=' quantity_thre' style='margin-left: 0;font-weight: bold;'>"
            //         + "待发货量   " + "</td>"
            //         + "<td class=' rea' style='text-align: right;width:85px;'>" + project.waitProdCount + " 块" + "</td>"
            //         + "<td class=' rea' style='text-align: right;width:135px;'>" + project.waitProdCountVol + " m³" + "</td></tr></table>";

            //     var _divContext = $("#projectContext");
            //     var _divContextLi = $("#projectContextLi");
            //     _divContext.empty();//先清空
            //     _divContextLi.empty();

            //     //再追加
            //     _divContext.append(_projectName);
            //     arr.push(descTotal);
            //     arr.push(stockTotal);
            //     arr.push(descDelivered);
            //     arr.push(descShipping);
            //     _divContextLi.html(arr.join(""));
            //     _divContext.append(_divContextLi);
            // },
            // initAddPorjectDiv(project) {
            //     var arr = [];
            //     var _projectName = $("<h2 class='frm_msg_tit'>" +
            //         project.projectName + "</h2>");
            //     var cubage = (project.prodCountVol / project.prodCount);   //体积

            //     //1.总需求量
            //     var descTotal = "<table><tr><td class=' quantity_one' style='margin-left: 0;font-weight: bold;'>"
            //         + "总需求量   " + "</td>"
            //         + "<td class=' blu' style='text-align: right;width:85px;'>" + project.prodCount + " 块" + "</td>"
            //         + "<td class=' blu' style='text-align: right;width:135px;'>" + project.prodCountVol + " m³" + "</td></tr>";
            //     //2.总在库量
            //     var stockTotal = "<tr><td class=' quantity_tow1' style='margin-left: 0;font-weight: bold;'>"
            //         + "总在库量   " + "</td>"
            //         + "<td class=' gren' style='color: #990000;text-align: right;width:85px;'>" + project.stockProdCount + " 块" + "</td>"
            //         + "<td class=' gren' style='color: #990000;text-align: right;width:135px;'>" + project.stockProdCountVol + " m³" + "</td></tr>";
            //     //3.已发货量
            //     var descDelivered = "<tr><td class=' quantity_tow' style='margin-left: 0;font-weight: bold;'>"
            //         + "已发货量   " + "</td>"
            //         + "<td class=' gren' style='text-align: right;width:85px;'>" + project.sendProdCount + " 块" + "</td>"
            //         + "<td class=' gren' style='text-align: right;width:135px;'>" + project.sendProdCountVol + " m³" + "</td></tr>";

            //     //4.待发货量
            //     var descShipping = "<tr><td class=' quantity_thre' style='margin-left: 0;font-weight: bold;'>"
            //         + "待发货量   " + "</td>"
            //         + "<td class=' rea' style='text-align: right;width:85px;'>" + project.waitProdCount + " 块" + "</td>"
            //         + "<td class=' rea' style='text-align: right;width:135px;'>" + project.waitProdCountVol + " m³" + "</td></tr></table>";

            //     var _divContext = $("#projectContext");
            //     var _divContextLi = $("#projectContextLi");
            //     _divContext.empty();//先清空
            //     _divContextLi.empty();

            //     //再追加
            //     // _divContext.append(_projectName);
            //     arr.push(descTotal);
            //     arr.push(stockTotal);
            //     arr.push(descDelivered);
            //     arr.push(descShipping);
            //     _divContextLi.html(arr.join(""));
            //     _divContext.append(_divContextLi);
            // },
            a() {
                for (var i = 0; i < $(".frm_birg_o").length; i++) {
                    var max = [];
                    for (var j = 0; j < $(".frm_birg_o").eq(i).find(".frm_birg_o_list .frm_birg_o_cen").length; j++) {
                        max.push($(".frm_birg_o").eq(i).find(".frm_birg_o_list .frm_birg_o_cen").eq(j).height());
                    }
                    $(".frm_birg_o").eq(i).find(".frm_birg_o_list .frm_birg_o_cen").height((Math.max.apply(null, max) + 20));
                    $(".frm_birg_o").eq(i).find(".frm_birg_o_list .frm_birg_o_cen").find(".frm_birg_o_firs").css({
                        "position": "absolute",
                        "bottom": "24px",
                        "left": "0"
                    })
                }
            },
            getProjects() {
                projectModel.getAllProjects().then((data) => {
                    this.arrProject = data.entity
                })
            },
            click() {

                var projectId = this.floorProductDetailQuery.projectId
                let _houseTypeDiv = document.getElementById("houseType");
                _houseTypeDiv.innerHTML = "";

                var _houseTypeDiv1 = $("#houseType");
                _houseTypeDiv1.html(""); //先清空

                if (projectId === "") {
                    // GysAlert({
                    //     content: "请选择一个项目"
                    // });
                    return false;
                }
                ;
                buildingModel.findProductBelongToFloorNum1(projectId).then((data) => {
                    //console.log('data------>', data.entity)
                    this.listData = data.entity;

                    //1.项目
                    var project1 = data.entity[0];
                    //console.log(project1)
                    project1.sendProdCount = project1.productShipNumber;
                    project1.prodCount = project1.productTotalVol;


                    var project = {
                        "projectId": project1.projectId,
                        "projectName": project1.projectName,
                        "num1": project1.productWaitShipNumber ? project1.productWaitShipNumber.toFixed(0) : 0,//未发货
                        "num2": project1.productWaitShipVol ? project1.productWaitShipVol.toFixed(3) : 0,//未发货
                        "prodCountVol": project1.productTotalVol ? project1.productTotalVol.toFixed(3) : 0, //总数
                        "prodCount": project1.productTotalNumber ? project1.productTotalNumber.toFixed(0) : 0, //总数
                        "sendProdCountVol": project1.productShipVol ? project1.productShipVol.toFixed(3) : 0, //已发货
                        "sendProdCount": project1.productShipNumber ? project1.productShipNumber.toFixed(0) : 0,//已发货
                        "stockProdCountVol": project1.productWaitShipVol ? project1.productWaitShipVol.toFixed(3) : 0,
                        "stockProdCount": project1.productWaitShipNumber ? project1.productWaitShipNumber.toFixed(0) : 0,

                    }

                    project.waitProdCountVol = (project1.productTotalVol - project1.productShipVol).toFixed(3);
                    project.waitProdCount = (project1.productTotalNumber - project1.productShipNumber).toFixed(0);


                    var goodsGercentTemp = Math.round((project.sendProdCount / project.prodCount) == 0 ? 0 : (project.sendProdCount / project.prodCount) * 100000) / 1000;

                    var goodsGercent = isNaN(goodsGercentTemp) ? 0 : goodsGercentTemp.toFixed(1) + "%";
                    var goodsFinish = Math.round((project.sendProdCount / project.prodCount) * 100000) / 1000;
                    var goodsNoFinish = (100 - goodsFinish);
                    this.initEcharts(goodsGercent, goodsFinish, goodsNoFinish); //初始化（Echart）百分比

                    // this.initAddPorjectDiv(project); //初始化（项目）追加Div层方法
                    this.project = project
                    console.log(this.project)
                    //  //2.户型  楼栋  楼层构建
                    var houseTypeList = data.entity[1];
                    //console.log("houseTypeList:---------",houseTypeList)
                    var houseMap = data.entity[2];
                    //console.log("houseMap:---------",houseMap)
                    var buildMap = data.entity[3];
                    //console.log("buildMap:---------",buildMap)
                    this.initAddHouseTypeDiv(houseTypeList, houseMap, buildMap);


                })
                this.a();
            },
            //初始化百分比
            initEcharts(goodsGercent, goodsFinish, goodsNoFinish) {

                if (goodsGercent == 0) {
                    goodsGercent = "";
                    goodsFinish = "";
                    goodsNoFinish = "";
                }
                //echarts圆形进度条
                var option = {
                    title: {
                        text: goodsGercent,
                        x: 'center',
                        y: 'center',
                        textStyle: {
                            fontWeight: 'normal',
                            color: '#3E8EF7',
                            fontSize: '16'
                        }
                    },
                    color: '#e3e4e3',
                    series: [{
                        name: 'Line 1',
                        type: 'pie',
                        clockWise: true,
                        radius: ['60%', '66%'],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: false
                                },
                                labelLine: {
                                    show: false
                                }
                            }
                        },
                        hoverAnimation: false,
                        data: [{
                            value: goodsFinish,
                            name: '01',
                            itemStyle: {
                                normal: {
                                    color: { // 完成的圆环的颜色
                                        colorStops: [{
                                            offset: 0,
                                            color: '#3E8EF7' // 0% 处的颜色
                                        }, {
                                            offset: 1,
                                            color: '#3E8EF7' // 100% 处的颜色
                                        }]
                                    },
                                    label: {
                                        show: false
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                }
                            }
                        }, {
                            name: '02',
                            value: goodsNoFinish
                        }]
                    }]
                }

                var myChart = echarts.init(document.getElementById('chart'));
                myChart.setOption(option);
            },
            // 保存
            list() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                const floorProductDetailQuery = {
                    buildingId: this.buildingId,
                    projectId: this.projectId,
                    productTypeIds: [],
                    list: this.listData.fp
                }
                this.list();
            }
        }
    }
</script>
<style>
    ul, li {
        list-style: none;
        padding: 0;
        margin:0;
    }
    i {
        font-style: normal;
    }
    .cont {
        width: 100%;
        height: 100%;
    }
    .cont .frm {
        width: 100%;
        float: left;
        /* background-color: #fffeda; */
        min-height: calc(100% - 80px);
        padding-bottom: 30px;
    }
    .clearfix:after {
        content: "";
        display: block;
        clear: both;
    }
    .frm .frm_con {
        width: 100%;
        min-width: 1000px;
        margin: 0 auto;
    }
    .frm_msg .frm_msg_tit {
        font-size: 24px;
    }
    .marg-box{
        display: flex;
    }
    .marg-box .frm_marg{
        width: 250px;
        border: 1px solid #EBEEF5;
        position: relative;
    }
    .frm_marg_title{
        position: absolute;
        color: rgba(0, 0, 0, 0.45);
        font-weight: bold;
        font-size: 12px;
        left:50%;
        top:20px;
        transform:translateX(-50%)
    }
    .marg-box .dashboard{
        flex:1;
    }
    .frm_msg .quantity {
        width: 100%;
    }
    .frm_marg .frm_msg {
        float: left;
        width: 320px;
    }
    .frm_marg .echar {
        width: 250px;
        height: 250px;
        float: left;
    }
    .frm_con .frm_birg {
        margin-top: 40px;
    }
    .quantity ul {
        float: left;
        width: 100%;
    }

    .quantity li {
        float: left;
        margin-top: 5px;
    }
    .frm_birg .frm_birg_o {
        width: 100%;
    }

    .frm_birg_o .frm_birg_o_tit {
        width: 100%;
        float: left;
        margin:20px 0;
    }

    .frm_birg_o_tit p {
        float: left;
    }

    .frm_birg_o_tit i {
        font-size: 20px;
        margin-left: 15px;
    }

    .frm_birg_o .frm_birg_o_list {
        display: flex;
        flex-wrap: wrap;
        width: 100%;
        float: left;
        margin-top: 15px;
        margin-left: 10px;
    }

    .frm_birg_o_list .frm_birg_o_cen {
        float: left;
        width: 30%;
        /* margin-left: 20px; */
        min-width: 190px;
        position: relative;
    }

    .frm_birg_o_list .frm_birg_o_cen:nth-child(1) {
        margin-left: 0;
    }

    .frm_birg_o_cen .frm_birg_o_cen_stairs {
        float: left;
        width: 100%;
        text-align: center;
    }

    .frm_birg_o_list .frm_birg_o_firs {
        float: left;
        width: 100%;
        font-size: 18px;
        padding-bottom: 25px;
    }

    .frm_birg_o_firs li {
        float: left;
        width: 70%;
        position: relative;
        margin-left: 20px;
        border: 1px solid black;
        border-top: 0;
        border-bottom: 0;

    }

    .frm_birg_o_firs li:nth-child(1) {
        border-top: 1px solid black;
    }

    .frm_birg_o_firs .frm_birg_o_firs_txt {
        float: left;
        position: absolute;
        /* top: -17px; */
        left: -35px;
    }

    .frm_birg_o_firs .frm_birg_o_firs_zpt {
        position:relative;
        float: left;
        width: 100%;
    }
    .frm_birg_o_firs .frm_birg_o_firs_zpt .num-box{
        position:absolute;
        width:50px;
        top:0;
        left:50%;
        margin-left:-25px;
        text-align:center;
    }
    .frm_birg_o_firs_zpt .frm_birg_o_firs_box {
        float: left;
        width: 50%;
        height:22px;
        text-align: center;
        background-color: #3E8EF7;
        color:#fff;
        border-bottom: 1px solid black;
    }

    .frm_birg_o_firs_zpt .frm_birg_o_firs_boxs {
        float: left;
        width: 50%;
        height:22px;
        text-align: center;
        background-color: #fff;
        border-bottom: 1px solid black;
    }
    .frm_birg_o_list .frm_birg_o_cen {
        position: relative;
        margin-bottom: 25px;
        /*height:600px;*/
    }

    /* .frm_birg_o_list .frm_birg_o_firs {
        margin-top: 0px;
    } */

    .frm_birg_o_cen .frm_birg_o_cen_stairs {
        position: absolute;
        font-size:18px;
        bottom: 0px;
        left: -30px;
    }

    .frm_birg_o .frm_birg_o_list {
        margin-top: 0 !important;
    }
    .fl {
        margin-left: 25px;
        font-size: 14px;
    }

    .blu {
        color: #0000ff;
    }

    .gren {
        color: #008000;
    }

    .rea {
        color: #ff0000;
    }
    
    .quantity_one {
        padding-left: 10px;
        border-left: 4px solid #3E8EF7;
    }

    .quantity_tow1 {
        padding-left: 10px;
        border-left: 4px solid #990000;
    }

    .quantity_tow {
        padding-left: 10px;
        border-left: 4px solid #008000;
    }

    .cont .frm table tr {
        float: left;
        margin-top: 5px;
    }

    .quantity_thre {
        padding-left: 10px;
        border-left: 4px solid #ff0000;
    }
</style>
<style lang="scss" scoped>
    .dashboard{
        display: flex;
        flex-wrap: wrap;
        li{
            width: 45%;
            height: 48%;
            padding:10px;
            border: 1px solid #EBEEF5;
            border-radius: 4px;
            margin-left:10px;
            transition: .3s;
            display:flex;
            &:hover{
                box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
            }
            &:nth-child(3),&:nth-child(4){
                margin-top:1%;
            }
            .icon-wrap{
                flex: 1;
                i{
                    width: 100%;
                    height:100%;
                    font-size: 55px;
                    text-align: center;
                    line-height: 1.5;
                    color: #3E8EF7;
                }
            }
            .text-wrap{
                flex: 1;
                .title{
                    color:rgba(0, 0, 0, 0.45);
                    font-weight:bold;
                    font-size:12px;
                    text-align: center;
                    margin-top:6px;
                }
                .num,.cube-m{
                    color: #000;
                    font-weight: bold;
                    font-size: 16px;
                    text-align: center;
                    margin-top:12px;
                }
            }
        }
    }
</style>
