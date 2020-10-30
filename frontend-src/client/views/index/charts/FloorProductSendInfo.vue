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
                                           @change="showProjectIdAngHouseTypeId">
                                    <el-option
                                            v-for="item in arrProject"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="buildingId">
                                <el-select v-model="floorProductDetailQuery.buildingId"
                                           clearable
                                           placeholder="楼号"
                                           @change="showFloorNum"
                                           style="width:150px">
                                    <el-option
                                            v-for="item in arrBuilding"
                                            :key="item.id"
                                            :label="item.buildCode"
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
                    <el-col>
                        <el-form :inline="true">
                            <el-form-item>
                                <el-checkbox v-show="productNameOptions.length" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
                            </el-form-item>
                            <el-form-item>
                                <el-checkbox-group v-model="checkList" :min="1" @change="handleCheckedCitiesChange">
                                    <el-checkbox
                                            v-for="item in productNameOptions"
                                            :key="item.id"
                                            :label="item.name">
                                        {{item.name}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col></el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
                <el-row>
                    <ul class="catalog">
                        <li>
                          <div class="color-box"></div>
                          <div class="text-box">已发货</div>
                        </li>
                        <li>
                          <div class="color-box"></div>
                          <div class="text-box">部分发货</div>
                        </li>
                        <li>
                          <div class="color-box"></div>
                          <div class="text-box">未发货</div>
                        </li>
                    </ul>
                </el-row>
            </el-header>
            <!-- <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`">
                <el-table
                        class="floor_product_send_info"
                        :data="listData.fp"
                        height="100%"
                        ref="moviesTable"
                        id="exportXlsx"
                        size="mini"
                        stripe
                        border>
                    <template v-for="(item,index) in this.listData.pc">
                        <el-table-column
                                :key="index"
                                :label="item.productTypeName"
                                width="90"
                                align="center">
                            <el-table-column
                                    :label="item.productName"
                                    width="90"
                                    align="center">
                                <template slot-scope="scope">
                                    <div v-if="index===0" class="floor">{{scope.row.floorNum}}F</div>
                                    <div v-else class="clear-input-default focus-active">
                                      <div v-if="scope.row.floorProductTypeDetailVOList[index-1].numStr==='已发'" class="light">{{ scope.row.floorProductTypeDetailVOList[index-1].numStr}}</div>
                                      <div v-else-if="scope.row.floorProductTypeDetailVOList[index-1].num==scope.row.floorProductTypeDetailVOList[index-1].numStr" class="nothing">{{ scope.row.floorProductTypeDetailVOList[index-1].numStr}}</div>
                                      <div v-else class="yellow">{{ scope.row.floorProductTypeDetailVOList[index-1].numStr}}</div>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table-column>
                    </template>
                </el-table>
            </el-main> -->
            <el-main ref="mainContent" :style="`padding:0;height:${this.$store.state.mainContentHeight - 60}px;padding-bottom:10px;overflow:hidden;`">
                <span id="diagram"></span>
                <div class="show_list" style="height:100%;">
                    <div class="list_info" style="height:100%;" v-show="tableShow">
                        <table
                            id="floor-product-table"
                            class="exportXlsx"
                            width="auto"
                            border="1"
                            cellpadding="0"
                            cellspacing="0"
                            style="table-layout: fixed;">
                        </table>
                    </div>
                    <div v-show="!tableShow" style="text-align:center;margin-top:50px;">{{GLOBAL.PURCHASE_MSG.SELECTED_BUILDING_BEFORE}}</div>
                </div>
            </el-main>
            <el-footer ref="footOperationBar">
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    import buildingModel from "../../../model/project/building";
    import projectModel from '../../../model/project/project'
    import floorRelevanceModel from '../../../model/project/floorRelevance'
    import fileSaver from 'file-saver';
    import xlsx from 'xlsx'
    import {Notification} from "element-ui";
    import $ from 'jquery'

    export default {
        name: "FloorProductSendInfo",
        data() {
            return {
                arrBuilding: [],
                arrProject: [],
                total:'',
                buildCode: '',
                listData: [],
                tableShow:false,
                floorProductDetailQuery: {
                    buildingId: '',
                    productTypeIds: [],
                    typeDesc: '发货',
                    projectId:'',
                },
                checkList:[],
                productNameOptions:[],
                buildingId:'',
                projectId:'',
                // 全选按钮的状态1
                checkAll: false,
                // 全选按钮的状态2
                isIndeterminate: true,
                buttonData:[
                    {
                        name:'导出',
                        event:'exportExcel'
                    }
                ],
                searchButtonData:[
                    {
                        name:'查询',
                        event:'click'
                    },
                ]
            }
        },
        watch:{
            'checkList':{
                handler(n,o){
                    this.floorProductDetailQuery.productTypeIds = []
                    if(n.length){
                        this.tableShow = true
                        this.productNameOptions.forEach(val=>{
                            n.forEach(item=>{
                                if(item==val.name){
                                    this.floorProductDetailQuery.productTypeIds.push(val.id)
                                }
                            })
                        })
                        this.list()
                    }else{
                        this.tableShow = false
                    }
                },
                deep:true
            },
        },
        created() {
            this.getProjects()
        },
        mounted(){
          //选中的一行变色
          $('#floor-product-table').on('click', 'tr', function () {
              if (!$(this).hasClass('total') &&!$(this).hasClass('titCss')) {
                  if ($(this).hasClass('addcolor')) {
                      $(this).removeClass('addcolor');
                      $('#diagram').hide();
                  } else {
                      $('#diagram').show();
                      $('#floor-product-table tr').removeClass('addcolor');
                      $(this).addClass('addcolor');
                      $('#diagram').html(
                          $(this).find('td').eq(0).html()
                      );
                      $('#diagram').css(
                          'top',
                          document.body.scrollLeft + event.clientY
                      );
                  }
              }
          });
        },
        methods: {
            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                //TODO 分页
                setTimeout(() => {
                    var excelName = new Date().toLocaleDateString().replace("/","-").replace("/","-")+".xlsx";
                    excelName = "楼栋发货进度"+excelName;
                    // console.log(excelName)
                    /* generate workbook object from table */
                    let table = document.querySelector('.exportXlsx');
                    var wb = xlsx.utils.table_to_book(table);
                    /* get binary string as output */
                    var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                    try {
                        fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                    } catch (e) {
                        if (typeof console !== 'undefined') console.log(e, wbout)
                    }

                    loading.close();
                    return wbout;
                }, 500);
            },
            showFloorNum(){
                if(this.floorProductDetailQuery.buildingId&&this.floorProductDetailQuery.projectId){
                    this.buildingId = this.floorProductDetailQuery.buildingId
                    this.projectId = this.floorProductDetailQuery.projectId
                    buildingModel.findPTByBuildingId(this.buildingId).then((data)=>{
                        this.productNameOptions = data.entity
                        this.defaultDelection()
                    }).then(()=>{
                      // 获取数据
                      this.list()
                      // 获取楼栋
                      this.getBuildCode()
                    })
                }
            },
            /**
             * 产生project下拉框赋值
             */
            getProjects() {
                projectModel.getAllProjects().then((data) => {
                    this.arrProject = data.entity
                })
            },
            /**
             *二级联动([户型下拉框][楼号下拉框])
             */
            showProjectIdAngHouseTypeId(value) {
                this.arrBuilding = []
                this.floorProductDetailQuery.buildingId = ''
                floorRelevanceModel.getBuildCodeByProjectIdAndExistProduct(value).then((data) => {
                    this.arrBuilding = data.entity
                })
            },
            list() {
                if (!this.floorProductDetailQuery.buildingId || !this.floorProductDetailQuery.projectId) {
                    if (this.floorProductDetailQuery.projectId == '') {
                        this.$message('请先选择项目!')
                    }
                    else if (this.floorProductDetailQuery.buildingId == '') {
                        this.$message('请先选择楼栋!')
                    }
                    return;
                }
                this.floorProductDetailQuery.buildingId = this.buildingId
                buildingModel.findFloorSendPrDetail(this.floorProductDetailQuery).then((data) => {
                    //列名
                        var col = data.entity.pc;
                        //每个格子的数据
                        var tableData = data.entity.fp;
                        //开始画表格
                        var table = $('#floor-product-table').html('');
                        var cellCount = 0; //列数
                        var colFields = []; //每列对应的filed字段，用于画表格时对应数据；
                        var colAttributes = []; //定义全局的一个类型，用于区分颜色
                        //第一行表头
                        var titleTr = $("<tr class='titCss'></tr>");
                        //第二行表头
                        var titleTr2 = $("<tr class='titCss'></tr>");
                        for (var k = 0; k < col.length; k++) {
                            if (col[k].floorProductTypeDetailVOList != undefined) {
                                //添加第一行表头
                                var th = $(
                                    "<td colspan='" +
                                        col[k].floorProductTypeDetailVOList.length +
                                        "'>" +
                                        col[k].productTypeName +
                                        '</td>'
                                );
                                th.appendTo(titleTr);
                                cellCount += col[k].floorProductTypeDetailVOList.length; //计算总行数++
                                for (var m = 0;m < col[k].floorProductTypeDetailVOList.length;m++) {
                                    colFields.push(
                                        col[k].floorProductTypeDetailVOList[m].field
                                    );
                                    //添加第二行表头
                                    var th = $(
                                        '<td>' + col[k].floorProductTypeDetailVOList[m].productName + '</td>'
                                    );
                                    th.appendTo(titleTr2);
                                }
                            }
                        }
                        titleTr.appendTo(table); //第一行加入表格
                        titleTr2.appendTo(table); //第二行加入表格
                        console.log('cellcount:' + cellCount);
                        //开始绑定数据至table表格中
                        var arr = [];
                        for (var i = 0; i < tableData.length; i++) {
                            arr.push(
                                "<tr data-floor='" + tableData[i]['floor'].floorNum + "'>"
                            );
                            for (var j = 0; j < cellCount; j++) {
                                var colTemp = colFields[j]; //列名
                                var dataValue = tableData[i][colTemp].num; //获得数据的值
                                // 此处因为呀实现为0不显示的功能 所判断是否为0 为0则转为字符串'' 算合计时有给转回数值0 并且后台接受字符串''也会转回0
                                if(dataValue === 0)dataValue = ''
                                let backgroundColor
                                if(tableData[i][colTemp].numStr==='已发'){
                                  backgroundColor = 'light'
                                  dataValue = tableData[i][colTemp].invoiceDateStr
                                }else if(Number(tableData[i][colTemp].numStr)===tableData[i][colTemp].num){
                                  backgroundColor = 'nothing'
                                }else{
                                  backgroundColor = 'yellow'
                                }
                                var floorValue = tableData[i]['floor'].floorNum;
                                var floorId = tableData[i][colTemp].floorId;
                                var floorRelevanceId = tableData[i][colTemp].floorRelevanceId;
                                var buildingId = tableData[i][colTemp].buildingId;
                                var productId = tableData[i][colTemp].productId;
                                var td = '';
                                if (j == 0) {
                                    td =
                                        "<td>"+floorValue+"F</td>";
                                } else {
                                    td = `
                                    <td
                                      class='${backgroundColor}'
                                      data-type='${colTemp}'
                                      data-floor='${floorValue}'
                                      data-floor-id='${floorId}'
                                      data-building-id='${buildingId}'
                                      data-product-id='${productId}'
                                      data-floor-relevance-id='${floorRelevanceId}'
                                      data-lbl='${j}'
                                      data-num='${dataValue}'
                                      value='${dataValue}'
                                      data-send='${dataValue}'
                                      type='number'
                                      min='0'>${dataValue}
                                    </td>`
                                }
                                arr.push(td);
                            }
                            arr.push('</tr>');
                        }
                        table.append(arr.join(''));
                })
            },
            getBuildCode(){
                buildingModel.buildingLoad(this.buildingId).then((data) => {
                    this.buildCode = data.entity.buildCode
                })
            },
            // 初始默认选择第一个
            defaultDelection(){
                // 默认选择第一个
                this.checkList = []
                this.floorProductDetailQuery.productTypeIds = []
                this.checkList.push(this.productNameOptions[0].name)
                this.floorProductDetailQuery.productTypeIds.push(this.productNameOptions[0].id)

                // 此处判断多选框总量是否超过1个  超过1个全选按钮不勾选  
                if(this.productNameOptions.length===1){
                  this.isIndeterminate = false
                  this.checkAll = true
                }else{
                  this.isIndeterminate = true
                  this.checkAll = false
                }
            },
            // 多选框全选事件
            handleCheckAllChange(val) {
                this.isIndeterminate = false
                let arr = []
                if(val){
                    this.productNameOptions.forEach(item=>{
                        arr.push(item.name)
                    })
                }
                this.checkList = arr
            },
            handleCheckedCitiesChange(value){
                let self = this
                let checkedCount = value.length
                self.checkAll = checkedCount === self.productNameOptions.length
                self.isIndeterminate = checkedCount > 0 && checkedCount < self.productNameOptions.length
            }
        }
    }
</script>

<style scoped lang="scss">
    .clear-input-default {
        width: 100%;
        height: 100%;
        border: none;
        outline: none;
        background: rgba(0, 0, 0, 0);
        text-align: center;
    }

    // .focus-active{
    //     color: red !important;
    // }
    .floor {
        font-weight: bold;
    }
</style>
<style lang="scss">
    .floor_product_send_info{
        .el-table__row{
            td{
                background: #fffeda !important;
                &:first-child{
                    background: #daedf1 !important;
                }
            }
            .light{
                background: #6cdf46 !important;
            }
            .nothing{
                background: #fff !important;
            }
            .yellow{
                background: #fffeda !important;
            }
        }
        .is-group{
            tr{
                &:nth-child(1){
                    th{
                        background: #c2d69b !important;
                    }
                }
                &:nth-child(2){
                    th{
                        background: #daedf1 !important;
                    }
                }
            }
        }
    }
    .catalog{
        display: flex;
        li{
          display: flex;
          margin-right: 10px;
          .color-box{
            width: 15px;
            height: 15px;
          }
          .text-box{

          }
          &:nth-child(1){
            .color-box{
              background: rgb(108, 223, 70);
            }
          }
          &:nth-child(2){
            .color-box{
              background: #FFFEAD;
            }
          }
          &:nth-child(3){
            .color-box{
              background: #fff;
              border:1px solid #DCDFE6;
            }
          }
        }

    }
    #floor-product-table {
                border-collapse: collapse;
                border-spacing: 1px;
                min-width: 100%;
                .titCss{
                  td{
                    white-space:nowrap;
                  }
                }
            }

            .show_list {
                width: calc(100% - 20px);
                padding: 0 10px;
                margin-top: 10px;
                overflow: hidden;
                height: calc(100% - 100px);
            }

            .list_info {
                width: 100%;
                overflow: auto;
                height: 100%;
            }

            td {
                text-align: center;
                min-width: 60px;
                &.light{
                    background: #6cdf46 !important;
                }
                &.nothing{
                    background: #fff !important;
                }
                &.yellow{
                    background: #fffeda !important;
                }
            }
            
            #floor-product-table tr:nth-child(1) td {
                background-color: #ed7d31 !important;
                color: #ffffff !important;
            }

            #floor-product-table tr:nth-child(2) td {
                background-color: #4472c4 !important;
                color: #ffffff !important;
            }

            #floor-product-table .huizong td {
                background-color: #c6efce !important;
                color: #307f2d !important;
                font-weight: 700;
            }

            #floor-product-table tr td:nth-child(-n + 1) {
                background-color: #fff;
                color: #1a5dab;
            }

            #floor-product-table tr td {
                background-color: #fff;
            }

            #floor-product-table tr td input {
                width: 95%;
                height: 100%;
                border: none;
                background-color: transparent;
                text-align: center;
            }

            input::-webkit-outer-spin-button,
            input::-webkit-inner-spin-button {
                -webkit-appearance: none !important;
                margin: 0;
            }

            #tit {
                margin-top: -25px;
                margin-left: 10px;
                /*width: 130px;*/
                font-weight: 700;
                color: #333;
                font-size: 16px;
                background-color: #f1f1f1;
                text-align: center;
            }

            .clearfix:after {
                content: '.'; /*加一段内容*/
                display: block; /*让生成的元素以块级元素显示，占满剩余空间*/
                height: 0; /*避免生成的内容破坏原有布局高度*/
                clear: both; /*清除浮动*/
                visibility: hidden; /*让生成的内容不可见*/
            }

            .clearfix {
                zoom: 1; /*为IE6，7的兼容性设置*/
            }

            #floor-product-table tr.addcolor,
            #floor-product-table tr.addcolor td {
                background-color: #7ebcf7 !important;
            }

            #diagram {
                position: fixed;
                width: 30px;
                height: 30px;
                line-height: 30px;
                text-align: center;
                color: #313131;
                right: 0;
                background-color: #eae8e8;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
                display: none;
            }

            .bttn {
                width: 50px !important;
            }
</style>